package com.air.crmuser.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.constant.TokenUtil;
import com.air.crmuser.service.CrmUserService;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.CrmUserAndRole;
import com.air.pojo.entity.Perm;
import com.air.pojo.entity.Role;
import com.air.pojo.entity.RoleAndPerm;
import com.air.pojo.entity.UserAndPerm;
import com.air.user.service.AppUserService;
import com.air.utils.EmptyUtils;
import com.github.pagehelper.PageInfo;


@Controller
@RequestMapping("/crm")
public class CrmUserController{
	
	@Resource
	private CrmUserService crmUserService;
	
	@Resource
	private AppUserService appUserService;
	
	/**
	 * 添加crm端用户
	 * @param crmuser
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addCrmUser",method=RequestMethod.POST)
	@RequiresPermissions(value= {"addCrmUser"})
	public Dto addCrmUser(@RequestBody CrmUser crmuser) {
		if (EmptyUtils.isEmpty(crmuser)) {
			return DtoUtil.returnFail("请正确填写用户账号", ErrorCode.RESP_ERROR);
		}
		Boolean bool = crmUserService.addCrmUser(crmuser);
		if (!bool) {
			return DtoUtil.returnFail("添加用户账号失败，请输入新的账号名称", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加成功",ErrorCode.RESP_SUCCESS);
	}
	
	/**
	 * 查询crm端用户
	 * @param crmuser
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/getAllCrmUser")
	@RequiresPermissions(value= {"queryCrmUsers"})
	@RequiresRoles(value= {"root","admin"},logical=Logical.OR)
	public Dto getAllCrmUser(HttpServletRequest request , Integer curPage,Integer pageSize,CrmUser crmUser) {
		String token = request.getHeader("token");
		Integer crmUserId = TokenUtil.getCrmUserId(token);
		CrmUser user = new CrmUser();
		user.setCrmuserId(crmUserId);
		Integer areaId = crmUserService.queryCrmUser(user).getAreaId();
		Subject subject = SecurityUtils.getSubject();
		
		if (subject.hasRole("root")) {
			
		}else if(subject.hasRole("admin")){
			crmUser.setAreaId(areaId);
		}
		PageInfo<CrmUser> crmuser= crmUserService.getAllCrmUser(curPage,pageSize,crmUser);
		return DtoUtil.returnSuccess("success",crmuser);
	}
	
	/**
	 * 查询crm端用户
	 * @param crmuser
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCrmUser",method=RequestMethod.POST)
	@RequiresPermissions(value= {"queryCrmUsers"})
	public Dto getCrmUser(@RequestBody CrmUser crmuser) {
//		List<CrmUser> crmuserList= crmUserService.queryCrmUser(crmuser);
		CrmUser crmUser = crmUserService.queryCrmUser(crmuser);
//		if (crmuserList.isEmpty()) {
//			return DtoUtil.returnFail("无用户", ErrorCode.RESP_ERROR);
//		}
//		return DtoUtil.returnSuccess("success",crmuserList);
		return DtoUtil.returnSuccess("success",crmUser);
	}
	
	/**
	 * 修改crm端用户
	 * @param crmuser
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/modifyCrmUser",method=RequestMethod.POST)
	@RequiresPermissions(value= {"updateCrmUser"})
	public Dto modifyCrmUser(@RequestBody CrmUser crmuser) {
		if (EmptyUtils.isEmpty(crmuser)) {
			return DtoUtil.returnFail("请正确填写用户账号信息", ErrorCode.RESP_ERROR);
		}
		Boolean bool = crmUserService.modifyCrmUser(crmuser);
		if (!bool) {
			return DtoUtil.returnFail("修改用户账号失败", ErrorCode.RESP_SUCCESS);
		}
		return DtoUtil.returnSuccess("修改成功",ErrorCode.RESP_SUCCESS);
	}
	
	/**
	 * 删除crm端用户
	 * @param crmuser
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delCrmUser",method=RequestMethod.DELETE)
	@RequiresPermissions(value= {"delCrmUser"})
	public Dto delCrmUser(Integer crmuserId) {
		Boolean bool = crmUserService.delCrmUser(crmuserId);
		Boolean boolR = crmUserService.delRoleAndCrmUser(0, crmuserId);
		Boolean boolU = appUserService.modifyAppUserPartner(crmuserId);
		if (!bool) {
			return DtoUtil.returnFail("删除用户账号失败", ErrorCode.RESP_ERROR);
		}
		if (!boolR) {
			return DtoUtil.returnFail("删除用户账号失败", ErrorCode.RESP_ERROR);
		}if (!boolU) {
			return DtoUtil.returnFail("删除用户账号失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("删除成功",ErrorCode.RESP_SUCCESS);
	}
	
	/**
	 * 添加角色信息
	 * @param role
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addRole",method=RequestMethod.POST)
	@RequiresPermissions(value= {"authCrmUser"})
	public Dto addRole(@RequestBody Role role) {
		Boolean bool = crmUserService.addRole(role);
		if (!bool) {
			return DtoUtil.returnFail("添加角色信息失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("添加成功",ErrorCode.RESP_SUCCESS);
	}
	
	/**
	 * 查询所有角色
	 * @param curPage
	 * @param pageSize
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getAllRole",method=RequestMethod.GET)
	@RequiresPermissions(value= {"authCrmUser"})
	public Dto getAllRole(Integer curPage,Integer pageSize) {
		PageInfo<Role> role = crmUserService.getAllRole(curPage, pageSize);
		return DtoUtil.returnSuccess("查出的数据", role);
	}
	
	/**
	 * 通过角色获取用户信息App端
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCrmUserByRoleName",method=RequestMethod.GET)
	public Dto getCrmUserByRoleName(String roleName) {
		List<CrmUser> list = crmUserService.getCrmUserByRoleName(roleName);
		return DtoUtil.returnSuccess("查出的数据", list);
	}
	
	/**
	 * 通过角色获取用户信息Crm端
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getCrmUserByRoleNameCrm",method=RequestMethod.GET)
	@RequiresPermissions(value= {"authCrmUser"})
	public Dto getCrmUserByRoleNameCrm(String roleName) {
		List<CrmUser> list = crmUserService.getCrmUserByRoleName(roleName);
		return DtoUtil.returnSuccess("查出的数据", list);
	}
	
	/**
	 * 删除角色信息
	 * @param roleId
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delRole",method=RequestMethod.DELETE)
	@RequiresPermissions(value= {"authCrmUser"})
	public Dto delRole(@RequestBody HashMap<String, Integer> map) {
		Integer roleId = map.get("roleId");
		Boolean bool = crmUserService.delRole(roleId);
		Boolean boolDel = crmUserService.delRoleAndCrmUser(roleId,0);
		Boolean boolPer = crmUserService.delPerm(roleId);
		if (!bool) {
			return DtoUtil.returnFail("删除失败", ErrorCode.RESP_ERROR);
		}
		if (!boolDel) {
			return DtoUtil.returnFail("删除失败", ErrorCode.RESP_ERROR);
		}
		if (!boolPer) {
			return DtoUtil.returnFail("删除失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("删除成功",ErrorCode.RESP_SUCCESS);
	}
	
	/**
	 * 为用户赋予角色
	 * @param roleAndPerm
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/addRoleForCrmUser",method=RequestMethod.POST)
	@RequiresPermissions(value= {"authCrmUser"})
	@RequiresRoles(value= {"root","admin"},logical=Logical.OR)
	public Dto addRoleForCrmUser(HttpServletRequest request ,@RequestBody CrmUserAndRole crmUserAndRole) {
		String token = request.getHeader("token");
		Integer crmUserId = TokenUtil.getCrmUserId(token);
		Boolean bool = crmUserService.addRoleAndCrmUser(crmUserAndRole,crmUserId);
		if (!bool) {
			return DtoUtil.returnFail("分配角色失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("分配角色成功",ErrorCode.RESP_SUCCESS);
	}
	
	/**
	 * 查询用户所拥有的角色
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getRoleByCrmUser",method=RequestMethod.POST)
	@RequiresPermissions(value= {"authCrmUser"})
	public Dto getRoleByCrmUser(Integer curPage,Integer pageSize,@RequestBody CrmUser crmuser) {
		PageInfo<Role> crmusers = crmUserService.queryRoleByCrmUser(curPage,pageSize,crmuser);
		if (EmptyUtils.isEmpty(crmusers)) {
			return DtoUtil.returnFail("该用户没有分配角色", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("查询到的信息", crmusers);
	}
	
	/**
	 * 
	 * 删除用户所拥有的角色
	 * @param crmuser
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/delRoleByCrmUser",method=RequestMethod.DELETE)
	@RequiresPermissions(value= {"authCrmUser"})
	public Dto delRoleByCrmUser(@RequestBody CrmUserAndRole crmUserAndRole) {
		Boolean bool = crmUserService.delRoleByCrmUser(crmUserAndRole);
		if (!bool) {
			return DtoUtil.returnFail("删除失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess("删除成功", ErrorCode.RESP_SUCCESS);
	}
	

	/**
	 * 查看用户所有权限
	 * @return
	 */	
	@ResponseBody
	@RequestMapping(value="/getAllPerm",method=RequestMethod.GET)
	@RequiresPermissions(value= {"authCrmUser"})
	public Dto getAllPerm(Integer curPage,Integer pageSize) {
		PageInfo<Perm> perm = crmUserService.queryAllPerm(curPage,pageSize);
		return DtoUtil.returnSuccess("查询权限信息成功", perm);
	}
	/**
	 * 
	 * @param curPage
	 * @param pageSize
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getUserPerm",method=RequestMethod.GET)
	@RequiresPermissions(value= {"authCrmUser"})
	public Dto getUserPerm(Integer crmUserId) {
		List<Perm> perm = crmUserService.queryPermByCrmUser(crmUserId);
		return DtoUtil.returnSuccess("查询权限信息成功", perm);
	}
	/**
	 * 查询角色所拥有的权限
	 * @param curPage
	 * @param pageSize
	 * @param token
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/getPermByRole",method=RequestMethod.POST)
	@RequiresPermissions(value= {"authCrmUser"})
	public Dto getPermByRole(@RequestBody Role role) {
		List<Perm> perm = crmUserService.queryPermByRole(role);
		return DtoUtil.returnSuccess("查询到该角色被赋予的所有权限", perm);
	}

	/**
	 * 更新角色拥有的默认权限
	 * @param roleAndPermList
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/putPermByRole",method=RequestMethod.POST)
	@RequiresPermissions(value= {"authCrmUser"})
	public Dto putPermByRole(@RequestBody HashMap<String, Object> map) 
	{
		Integer roleId = (Integer)map.get("roleId");
		List<Integer> permList = (List<Integer>)map.get("permList");
		if (EmptyUtils.isEmpty(roleId)) {
			return DtoUtil.returnFail("无参数", ErrorCode.RESP_ERROR);
		}
		Boolean bool = crmUserService.delPerm(roleId);
		if (permList.isEmpty()) {
			return DtoUtil.returnSuccess("成功", ErrorCode.RESP_SUCCESS);
		}
		List<RoleAndPerm> list = new ArrayList<>();
		for (Integer permId : permList) {
			RoleAndPerm roleAndPerm = new RoleAndPerm();
			roleAndPerm.setPermId(permId);
			roleAndPerm.setRoleId(roleId);
			list.add(roleAndPerm);
		}
		return crmUserService.addPermForRole(list);
	}
	
	
	/**
	 * 用户受权
	 * @param roleAndPermList
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value="/grantCrmUsePerm",method=RequestMethod.POST)
	@RequiresPermissions(value= {"authCrmUser"})
	@RequiresRoles(value= {"root","admin"},logical=Logical.OR)
	public Dto grantCrmUsePerm(@RequestBody HashMap<String, Object> map) {
		
		Integer crmUserId = (Integer) map.get("crmUserId");
		List<Integer> permList = (List<Integer>)map.get("permList");
		CrmUser crmUser =new CrmUser();
		crmUser.setCrmuserId(crmUserId);
		
		List<Role> roleList = crmUserService.queryRoleByCrmUser(crmUser);
		Subject subject = SecurityUtils.getSubject();
		if(roleList.isEmpty()) {
			return DtoUtil.returnFail("请先分配角色再分配权限!", "401");
		}
		String  roleName =  roleList.get(0).getRoleName();
		if (subject.hasRole("root")) {
			
		}else if(subject.hasRole("admin")) {
			if("root".equals(roleName) || "admin".equals(roleName)) {
				return DtoUtil.returnFail("用户无权限!", "401");
			}
		}else {
			return DtoUtil.returnFail("用户无权限!", "401");
		}
		
		if (EmptyUtils.isEmpty(crmUserId)) {
			return DtoUtil.returnFail("无参数", ErrorCode.RESP_ERROR);
		}
		//Boolean bool = crmUserService.delPerm(roleId);
		if (permList.isEmpty()) {
			//return DtoUtil.returnSuccess("成功", ErrorCode.RESP_SUCCESS);
			List<UserAndPerm> list = new ArrayList<>();
			return crmUserService.updatePermForCrmUser(list,crmUserId);
		}
		List<UserAndPerm> list = new ArrayList<>();
		for (Integer permId : permList) {
			UserAndPerm userAndPerm = new UserAndPerm();
			userAndPerm.setCrmUserId(crmUserId);
			userAndPerm.setPermId(permId);
			list.add(userAndPerm);
		}
		return crmUserService.updatePermForCrmUser(list,crmUserId);
	}
	
}
