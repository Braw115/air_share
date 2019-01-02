package com.air.user.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.aspect.record;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.constant.SystemCode;
import com.air.constant.TokenUtil;
import com.air.crmuser.service.CrmUserService;
import com.air.notice.service.NoticeService;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.Notice;
import com.air.pojo.vo.AppUserVo;
import com.air.user.service.AppUserService;
import com.air.utils.EmptyUtils;
import com.air.utils.PwdUtil;

@Controller
@RequestMapping("/app")
public class AppUserController {

	@Resource
	private AppUserService appUserService;
	@Resource
	private NoticeService noticeService;
	@Resource
	private CrmUserService crmUserService;
	
	/**
	 * 技术人员注册
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/technicianRegister",method=RequestMethod.POST)
	public Dto technicianRegister( AppUserVo appUserVo,HttpServletRequest request) {
		if (EmptyUtils.isEmpty(appUserVo.getAuthUrl())&&EmptyUtils.isEmpty(appUserVo.getIdCardUrl())
				&&EmptyUtils.isEmpty(appUserVo.getPerCardUrl())&&EmptyUtils.isEmpty(appUserVo.getPersonUrl())
				&&EmptyUtils.isEmpty(appUserVo.getPartnerId())) {
			return DtoUtil.returnFail("上传图片缺失或未选择合作商", ErrorCode.RESP_ERROR);
		}
		appUserVo.setAppusersId(TokenUtil.getAppUserId(request.getHeader("token")));
		Boolean reg = appUserService.technicianRegister(appUserVo);
		if (!reg) {
			return DtoUtil.returnFail("注册失败", "error");
		}	
		return DtoUtil.returnSuccess("注册成功等待审核","ok");
	}
	
	
	/**
	 * 添加地址
	 * @param appUser
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addAddress",method=RequestMethod.POST)
	public Dto addAddress(@RequestBody AppUser appUser,HttpServletRequest request) {
		
		if (EmptyUtils.isEmpty(appUser)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		appUser.setAppusersId(TokenUtil.getAppUserId(request.getHeader("token")));
		return appUserService.addAddressForUser(appUser);
		
	}
	
	/**
	 * 根据审核状态查询技术人员
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/getTechnician")
	@RequiresPermissions(value= {"queryRepairer"})
	public Dto getTechnician(Integer curPage,Integer pageSize,String reviewStatus) {
		return appUserService.queryTechnician(curPage,pageSize,reviewStatus);
	}
	
	/**
	 * 查询所有技术人员
	 * 
	 */
	@ResponseBody
	@RequestMapping(value="/getAllTechnician")
	@RequiresPermissions(value= {"queryRepairer"})
	public Dto getAllTechnician(Integer curPage,Integer pageSize) {
		return appUserService.queryTechnician(curPage,pageSize,null);
	}
	
	/**
	 * 根据appUserVo的条件查询appUser
	 * @param appUserVo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAppUser")
	@RequiresPermissions(value= {"queryRepairer","appUserManage"},logical=Logical.OR)
	public Dto getAppUser(AppUserVo appUserVo,HttpServletRequest request){
		if (EmptyUtils.isEmpty(appUserVo)) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		String token = request.getHeader("token");
		Integer crmUserId = TokenUtil.getCrmUserId(token);
		
		Subject subject = SecurityUtils.getSubject();
		if(subject.hasRole("root") || subject.hasRole("admin")) {
			appUserVo.setPartnerId(null);
		}else if(subject.hasRole("partner")) {
			appUserVo.setPartnerId(crmUserId);
		}else {
			appUserVo.setPartnerId(null);
		}
		if (EmptyUtils.isNotEmpty(appUserVo.getCurPage())&&EmptyUtils.isNotEmpty(appUserVo.getCurPage())) {
			return appUserService.queryAppUserByPage(appUserVo);
		}
		return appUserService.queryAppUser(appUserVo);
	}
	
	/**
	 * 审核维修人员
	 * @return
	 */
	@ResponseBody
	@record(businessLogic="管理员审核")
	@RequestMapping(value="/modifyFechnicianInfo",method=RequestMethod.POST)
	@RequiresPermissions(value= {"auditingRepairer"})
	public Dto modifyFechnicianInfo(@RequestBody AppUser appUser) {
		
		if (EmptyUtils.isEmpty(appUser.getAppusersId())&&EmptyUtils.isEmpty(appUser.getReviewStatus())) {
			return DtoUtil.returnFail("参数为空", ErrorCode.RESP_ERROR);
		}
		
		if ("no".equals(appUser.getReviewStatus())) {
			return DtoUtil.returnFail("错误审核，请重新选择", ErrorCode.RESP_ERROR);
		}
		
		Notice notice = new Notice();
		notice.setAppusersId(appUser.getAppusersId());
		notice.setTitle(SystemCode.REVIEW_TITLE);
		//yes 审核通过 生成密码
		if ("yes".equals(appUser.getReviewStatus())) {
			String pwd = PwdUtil.getPasswords(6);
			appUser.setPassword(pwd);
			notice.setContent(SystemCode.REVIEW_SUCCESS+pwd);
			
		}else if ("err".equals(appUser.getReviewStatus())){
			notice.setContent(SystemCode.REVIEW_ERROR);
		}
		Boolean bool = noticeService.addNotice(notice);
		if (!bool) {
			return DtoUtil.returnFail("审核失败", ErrorCode.RESP_ERROR);
		}
		
		Subject subject = SecurityUtils.getSubject();
		
		if (subject.hasRole("admin")||subject.hasRole("root")) {
			appUser.setPartnerId(null);
		}
		return appUserService.modifyFechnicianInfo(appUser);
	}
	
	/**
	 * 
 	 * @param req
	 * @param appUser
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPerAppUserInfo")
	public Dto getAppUserInfo(HttpServletRequest req,AppUser appUser) {
		appUser.setAppusersId(TokenUtil.getAppUserId(req.getHeader("token")));
		return appUserService.queryAppUser(appUser);
	}
	
	/**
	 * 获取技术员信息
	 * @param appusersId
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPerFechnicianInfo")
	public Dto getPerFechnicianInfo(HttpServletRequest req) {
		
		return appUserService.queryAppUserById(TokenUtil.getAppUserId(req.getHeader("token")));
	}
	
	/**
	 * 修改个人资料（昵称，个性签名，头像,性别,职业,年龄）APP
	 * @param AppUser
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/modifyAppUser",method=RequestMethod.POST)
	public Dto modifyAppUser(AppUserVo appUserVo,HttpServletRequest req) {

		if (EmptyUtils.isEmpty(appUserVo)) {
			return DtoUtil.returnFail("参数为空", "error");
		}
		if ("".equals(appUserVo.getNickname())) {
			return DtoUtil.returnFail("昵称为空", "error");
		}

		appUserVo.setAppusersId(TokenUtil.getAppUserId(req.getHeader("token")));
		return appUserService.modifyAppUser(appUserVo);
	}
	
	/**
	 * 修改技术人员密码
	 * @param AppUser
	 * @return
	 */
	@record(businessLogic="修改技术人员密码")
	@ResponseBody
	@RequestMapping(value="/modifyAppUserCrm",method=RequestMethod.POST)
	@RequiresPermissions(value= {"updateRepairerPwd"})
	public Dto modifyAppUserCrm(@RequestBody AppUserVo appUserVo) {
		Boolean reg = appUserService.modifyAppUserCrm(appUserVo);
		if (!reg) {
			return DtoUtil.returnFail("修改失败", "error");
		}	
		return DtoUtil.returnSuccess("修改成功","ok");
	}

}
