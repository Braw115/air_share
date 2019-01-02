package com.air.user.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.constant.MD5;
import com.air.constant.UserCreateLog;
import com.air.notice.service.NoticeService;
import com.air.pojo.entity.AppUser;
import com.air.pojo.entity.Notice;
import com.air.pojo.vo.AppUserVo;
import com.air.pojo.vo.ItripTokenVO;
import com.air.token.service.TokenBiz;
import com.air.user.mapper.AppUserMapper;
import com.air.user.service.AppUserService;
import com.air.utils.EmptyUtils;
import com.air.utils.FileUploader;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class AppUserServiceImpl implements AppUserService {
	private Logger logger = LoggerFactory.getLogger(AppUserServiceImpl.class);
	
	@Resource
	private AppUserMapper appUserMapper;
	@Autowired
	private TokenBiz tokenBiz;
	@Autowired
	private NoticeService noticeService;
	
	@Override
	public List<AppUser> queryBySql(HashMap<String, String> map) {
		
		return appUserMapper.selectBySql(map);
	}

	@Override
	public Boolean addWxappUser(String openid, String wxnickname, String headimgurl, String sex, String unionid, String phone) {
//		String passeord = "000000";
//		String md5password = MD5.getMd5(passeord, 32);
		return createdbUser(wxnickname,headimgurl,sex,phone,openid,null,null,unionid,wxnickname);
	}

	@Override
	public AppUser queryByPhone(String phone) {
		return appUserMapper.selectByPhone(phone);
	}

	@Override
	public Boolean modifyByPrimaryKeySelective(AppUser user) {
		return appUserMapper.updateAppUser(user);
	}

	@Override
	public Dto doLogin(String phone, String password) {
		AppUser appUser = appUserMapper.selectByPhone(phone);
		if (appUser == null || appUser.getPassword() == null) {
			return DtoUtil.returnFail("该手机账号不存在", "error");
		}
		String dbpwd = appUser.getPassword();
		if (!dbpwd.equals(password)) {
			return DtoUtil.returnFail("密码错误", "error");
		}
		return DtoUtil.returnSuccess("ok", appUser);
	}

	public Boolean createdbUser(String nickname, String pic,String sex, String phone,
			 String wxappopenid, String zfbid, String zfb, String wxunionid,String wxnickname) {

		logger.info("create user-nickname:{},pic:{},phone:{}", nickname, pic);

		AppUser user = new AppUser();
		user.setTelephone(phone);

		if (StringUtils.isEmpty(nickname)) {
			user.setNickname(phone);
		} else {
			user.setNickname(nickname.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*"));
		}
		if (StringUtils.isEmpty(wxnickname)) {
			user.setWxnickname(phone);;
		} else {
			user.setWxnickname(wxnickname.replaceAll("[\\ud800\\udc00-\\udbff\\udfff\\ud800-\\udfff]", "*"));
		}
		

//		user.setPassword(password);
		user.setHeadimg(pic);
		user.setTelephone(phone);
		user.setWxappopenid(wxappopenid);
		user.setWxunionid(wxunionid);
		user.setHeadimg(pic);
		user.setSex("1".equals(sex)?"M":"W");
		Boolean bool = appUserMapper.insertAppUser(user);
		// user = userService.queryByUsername(username);
		// if (user != null)
		UserCreateLog.info(JSON.toJSONString(user));

		return bool;
	}

	@Override
	public Boolean technicianRegister(AppUserVo appUserVo) {
		String authUrl = FileUploader.upload(appUserVo.getAuthUrl()).getMsg();
		String idCardUrl = FileUploader.upload(appUserVo.getIdCardUrl()).getMsg();
		String perCardUrl = FileUploader.upload(appUserVo.getPerCardUrl()).getMsg();
		String personUrl = FileUploader.upload(appUserVo.getPersonUrl()).getMsg();
		AppUser appUser =appUserMapper.selectById(appUserVo.getAppusersId());
		if (EmptyUtils.isEmpty(appUser)) {
			return false;
		}
		if("yes".equals(appUser.getReviewStatus())) {
			return false;
		}
		appUser.setReviewStatus("no");
		appUser.setIdCardUrl(idCardUrl);
		appUser.setPerm("T");
		appUser.setAuthUrl(authUrl);
		appUser.setPerCardUrl(perCardUrl);
		appUser.setPersonUrl(personUrl);
		appUser.setPartnerId(appUserVo.getPartnerId());
		
		return appUserMapper.updateAppUser(appUser);
	}

	@Override
	public Dto addPhoneUser(HttpServletRequest request,
			HttpSession session,String phone) {
		AppUser user = new AppUser();
		user.setTelephone(phone);
//		user.setPassword(MD5.getMd5("000000", 32));
		Boolean bool = appUserMapper.insertAppUser(user);
		if (!bool) {
			return DtoUtil.returnFail("添加失败", "");
		}
		AppUser appUser = appUserMapper.selectByPhone(phone);
		ItripTokenVO tokenVO = getToken(request, session, appUser);
		appUser.setToken(tokenVO.getToken());
		return DtoUtil.returnSuccess("首次登录添加成功",appUser);
	}
	
	public ItripTokenVO getToken(HttpServletRequest request,
			HttpSession session,AppUser appUser){
		
			String token = tokenBiz.generateToken(
					request.getHeader("user-agent"), appUser);
			tokenBiz.save(token, appUser);
			
			//返回ItripTokenVO
			ItripTokenVO tokenVO=new ItripTokenVO(token,
					Calendar.getInstance().getTimeInMillis()+TokenBiz.SESSION_TIMEOUT*1000,//14h有效期
					Calendar.getInstance().getTimeInMillis());			
			AppUser u =queryByPhone(appUser.getTelephone());
			tokenVO.setAccountId((int) u.getAppusersId());
			tokenVO.setIdentity("AppUser");
			
		
			//tokenVO.setBeadHouseid(u.getBeadHouseid());
			//新增语句
			System.out.println("token：" + token);
			String value = JSONArray.toJSONString(tokenVO);
			System.out.println("登录成功：" + value);
			session.setAttribute("phone", appUser.getTelephone());
			return tokenVO;
	}

	@Override
	public Dto modifyPhoneUser(AppUser appUser) {
		
		return null;
	}

	@Override
	public Dto resetPwd(String phone, String oldPwd, String newPwd) {
		
		AppUser appUser = appUserMapper.selectByPhone(phone);
		
		if (EmptyUtils.isNotEmpty(appUser)) {
			return DtoUtil.returnFail("无该用户", "");
		}
		oldPwd = MD5.getMd5(oldPwd, 32);
		newPwd = MD5.getMd5(newPwd, 32);
		if (oldPwd !=null&&appUser.getPassword() != oldPwd) {
			return DtoUtil.returnFail("老密码输入错误", ErrorCode.RESP_ERROR);
		}
		
		appUser.setPassword(newPwd);
		Boolean bool = appUserMapper.updateAppUser(appUser);
		
		if (!bool) {
			return DtoUtil.returnFail("修改失败", ErrorCode.RESP_ERROR);
		}
		
		return DtoUtil.returnSuccess("修改成功",ErrorCode.RESP_SUCCESS);
	}

	@Override
	public Dto modifyPhoneForUser(Integer appusersId, String newPhone) {
		AppUser dbOldUser = appUserMapper.selectById(appusersId);
		AppUser dbUser = appUserMapper.selectByPhone(newPhone);
		if (EmptyUtils.isNotEmpty(dbUser)) {
			return DtoUtil.returnFail("该手机号已注册请重新输入", ErrorCode.RESP_ERROR);
		}
		if (EmptyUtils.isEmpty(dbOldUser)) {
			return DtoUtil.returnFail("无该用户", ErrorCode.RESP_ERROR);
		}
		dbOldUser.setTelephone(newPhone);
		Boolean bool = appUserMapper.updateAppUser(dbOldUser);
		
		if (!bool) {
			return DtoUtil.returnFail("修改失败", ErrorCode.RESP_ERROR);
		}
		
		return DtoUtil.returnSuccess("修改成功",ErrorCode.RESP_SUCCESS);
	}

	@Override
	public Dto addAddressForUser(AppUser appUser) {
		Boolean bool = appUserMapper.updateAppUser(appUser);
		if (!bool) {
			return DtoUtil.returnFail("修改失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("修改成功",ErrorCode.RESP_SUCCESS);
	}

	@Override
	public Dto queryTechnician(Integer curPage, Integer pageSize,String reviewStatus) {
		AppUser user = new AppUser();
		user.setReviewStatus(reviewStatus);
		user.setPerm("repair");
		PageHelper.startPage(curPage, pageSize);
		List<AppUser> userList = appUserMapper.selectTechnicianByReviewStatus(user);
		PageInfo<AppUser> userInfo = new PageInfo<AppUser>(userList);
		if (EmptyUtils.isEmpty(userInfo)) {
			return DtoUtil.returnFail("没有维修人员", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("ok",userList);
	}

	/**
	 * 通过支付宝获取的userId查询
	 */
	@Override
	public AppUser queryByZfbUserId(String aliUserId) {
		
		return appUserMapper.selectAppUserByZfbUserId(aliUserId);
	}

	@Override
	public Dto addZfbAppUser(HttpServletRequest req, HttpSession session,String user_id, String avatar, String phone, String nick_name, String gender) {
		AppUser appUser = new AppUser();
		appUser.setAliUserId(user_id);
		appUser.setAliImg(avatar);
		appUser.setTelephone(phone);
		appUser.setSex("m".equals(gender)?"M":"W");
		appUser.setNickname(nick_name);
		appUser.setAliname(nick_name);
		appUser.setHeadimg(avatar);
		Boolean bool = appUserMapper.insertAppUser(appUser);
		if (!bool) {
			return DtoUtil.returnFail("添加支付宝用户失败",ErrorCode.RESP_ERROR);
		}
		ItripTokenVO tokenVo = getToken(req, session, appUser);
		
		appUser.setToken(tokenVo.getToken());
		return DtoUtil.returnSuccess("success",appUser);
	}

	@Override
	public Dto queryAppUserById(Integer appusersId) {
		AppUser dbAppUser = appUserMapper.selectById(appusersId);
		if (EmptyUtils.isEmpty(dbAppUser)) {
			return DtoUtil.returnFail("查询失败",ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnDataSuccess(dbAppUser);
	}

	@Override
	public Dto modifyFechnicianInfo(AppUser appUser) {
		Boolean bool = appUserMapper.updateAppUser(appUser);
		if (!bool) {
			return DtoUtil.returnFail("修改失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("修改成功",ErrorCode.RESP_SUCCESS);
	}

	@Override
	public Dto modifyBalance(AppUser appUser) {
		AppUser dbUser = appUserMapper.selectAppUser(appUser);
		if (dbUser.getBalance()==null) {
			dbUser.setBalance(new BigDecimal(0));
		}
		dbUser.setBalance(dbUser.getBalance().add(dbUser.getRedBoxValue()));
		dbUser.setRedBoxValue(new BigDecimal(0));
		Boolean bool = appUserMapper.updateAppUser(dbUser);
		if (!bool) {
			return DtoUtil.returnFail("转入失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("转入成功",ErrorCode.RESP_SUCCESS);
	}

	@Override
	public Dto modifyAppUser(AppUserVo appUserVo) {
		AppUser appUser = new AppUser();
		String headimg ="";
		if (!(appUserVo.getHeadimg()==null)) {
			headimg = FileUploader.upload(appUserVo.getHeadimg()).getMsg();
		}
		appUser.setHeadimg(headimg);
		appUser.setAppusersId(appUserVo.getAppusersId());
		appUser.setNickname(appUserVo.getNickname());
		appUser.setSignature(appUserVo.getSignature());
		appUser.setPassword(appUserVo.getPassword());
		appUser.setIdDoZmrz(appUserVo.getIsDoZmrz());
		appUser.setSex(appUserVo.getSex());
		appUser.setAge(appUserVo.getAge());
		appUser.setCareer(appUserVo.getCareer());
		Boolean bool = appUserMapper.updateAppUser(appUser);
		if (!bool) {
			return DtoUtil.returnFail("修改失败", ErrorCode.RESP_ERROR);
		}
		AppUser dbAppUser = appUserMapper.selectAppUser(appUser);
		return DtoUtil.returnDataSuccess(dbAppUser);
	}
	
	@Override
	public Boolean modifyAppUserCrm(AppUserVo appUserVo) {
		AppUser appUser = new AppUser();
		
		appUser.setAppusersId(appUserVo.getAppusersId());
		if (EmptyUtils.isNotEmpty(appUserVo.getPassword())) {
			appUser.setPassword(appUserVo.getPassword());
			Notice notice = new Notice();
			notice.setAppusersId(appUserVo.getAppusersId());
			notice.setTitle("修改密码");
			notice.setContent("修改后的密码为："+appUserVo.getPassword());
			Boolean bool = noticeService.addNotice(notice);
			return bool;
			
		}else {
			appUser.setPerm("C");
			appUser.setReviewStatus("Del");
			appUser.setIdCardUrl("Del");
			appUser.setPerCardUrl("Del");
			appUser.setPersonUrl("Del");
			appUser.setAuthUrl("Del");
			appUser.setPassword("      ");
		}
		
		return appUserMapper.updateAppUser(appUser);
	}

	@Override
	public Dto queryAppUser(AppUser appUser) {
		AppUser dbAppUser = appUserMapper.selectAppUser(appUser);
		if (EmptyUtils.isEmpty(appUser)) {
			return DtoUtil.returnFail("用户不存在", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("转入成功",dbAppUser);
	}

	@Override
	public Dto queryAppUser(AppUserVo appUserVo) {
		List<AppUser> userList = appUserMapper.selectAppUserInfo(appUserVo);
		return DtoUtil.returnSuccess("ok",userList);
	}

	@Override
	public Dto queryAppUserByPage(AppUserVo appUserVo) {
		PageHelper.startPage(appUserVo.getCurPage(), appUserVo.getPageSize());
		List<AppUser> userList = appUserMapper.selectAppUserInfo(appUserVo);
		PageInfo<AppUser> userInfo = new PageInfo<AppUser>(userList);
		return DtoUtil.returnSuccess("ok",userInfo);
	}

	@Override
	public Boolean modifyAppUserPartner(Integer crmuserId) {
		
		AppUserVo appUserVo = new AppUserVo();
		appUserVo.setPartnerId(crmuserId);
		List<AppUser> list = appUserMapper.selectAppUserInfo(appUserVo );
		
		if (list.isEmpty()) {
			return true;
		}
		
		return appUserMapper.updateAppUserPartner(crmuserId);
	}
	
}
