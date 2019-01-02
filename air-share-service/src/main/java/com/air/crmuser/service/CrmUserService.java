package com.air.crmuser.service;

import java.util.List;

import com.air.constant.Dto;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.CrmUserAndRole;
import com.air.pojo.entity.Perm;
import com.air.pojo.entity.Role;
import com.air.pojo.entity.RoleAndPerm;
import com.air.pojo.entity.UserAndPerm;
import com.github.pagehelper.PageInfo;

public interface CrmUserService {

	CrmUser queryUserByUsername(String userName);

	PageInfo<Perm> queryAllPerm(Integer curPage, Integer pageSize);

	Boolean addCrmUser(CrmUser crmuser);

	PageInfo<CrmUser> getAllCrmUser(Integer curPage, Integer pageSize,CrmUser crmuser);

	Boolean modifyCrmUser(CrmUser crmuser);

	Boolean delCrmUser(Integer crmuserId);

	Boolean addRole(Role role);

	PageInfo<Role> getAllRole(Integer curPage, Integer pageSize);

	Boolean delRole(Integer roleId);

	Boolean addRoleAndCrmUser(CrmUserAndRole crmUserAndRole,Integer crmuserId);

	PageInfo<Role> queryRoleByCrmUser(Integer curPage, Integer pageSize, CrmUser crmuser);

	Boolean delRoleByCrmUser(CrmUserAndRole crmUserAndRole);

	Dto addPermForRole(List<RoleAndPerm> roleAndPermList);
	
	
	List<Perm> queryPermByRole(Role role);

	/**
	 * 删除角色的所有权限
	 * @param roleId
	 * @return
	 */
	Boolean delPerm(Integer roleId);

	/**
	 * 根据用户某些信息查询用户
	 * @param crmuser
	 * @return
	 */
//	List<CrmUser> queryCrmUser(CrmUser crmuser);
	CrmUser queryCrmUser(CrmUser crmuser);

	/**
	 * 查询角色为业主,合作商的用户
	 * @param roleName 
	 * @return
	 */
	List<CrmUser> getCrmUserByRoleName(String roleName);

	/**
	 * 删除角色后，相应的用户和此角色的关系也删除
	 * 删除用户后，相应的此用户对应的角色的关系也删除
	 * @param roleId
	 * @return
	 */
	Boolean delRoleAndCrmUser(Integer roleId,Integer crmuserId);
	
	/**
	 * 更新用户权限
	 * @param userAndPermList
	 * @return
	 */
	Dto updatePermForCrmUser(List<UserAndPerm> userAndPermList,Integer crmUserId);
	
	
	/**
	 * 查询用户权限
	 * @param crmUserId
	 * @return
	 */
	List<Perm> queryPermByCrmUser(Integer crmUserId);
	/**
	 * 查询用户角色
	 * @param crmuser
	 * @return
	 */
	List<Role> queryRoleByCrmUser(CrmUser crmuser);
}
