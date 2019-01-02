package com.air.crmuser.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.CrmUserAndRole;
import com.air.pojo.entity.Perm;
import com.air.pojo.entity.Role;
import com.air.pojo.entity.RoleAndPerm;
import com.air.pojo.entity.UserAndPerm;

public interface CrmUserMapper {

	CrmUser selectCrmUserByUsername(String username);

	List<Role> selectRole(Role role);

	List<Perm> selectPerm(Perm perm);

	List<Role> selectRoleByCrmUser(CrmUser crmuser);

	List<Perm> selectPermByRole(Role role);

	Boolean insertCrmUser(CrmUser crmuser);

	List<CrmUser> selectCrmUser(CrmUser crmUser);

	Boolean updateCrmUser(CrmUser crmuser);

	Boolean deleteCrmUser(@Param("crmuserId")Integer crmuserId);

	Boolean insertRole(Role role);

	Boolean deleteRole(@Param("roleId")Integer roleId);

	/**
	 * 为用户添加角色
	 * @param crmUserAndRole
	 * @return
	 */
	Boolean insertRoleAndCrmUser(CrmUserAndRole crmUserAndRole);

	/**
	 * 给用户删除角色
	 * @param roleAndPerm
	 * @return
	 */
	Boolean deleteRoleByCrmUser(CrmUserAndRole roleAndPerm);

	/**
	 * 为角色添加权限
	 * @param roleAndPermList
	 * @return
	 */
	Boolean insertPermForRole(@Param("roleAndPermList")List<RoleAndPerm> roleAndPermList);

	/**
	 * 为角色删除权限
	 * @param roleAndPerm
	 * @return
	 */
	Boolean deletePerm(@Param("roleId")Integer roleId);
	
	/**
	 * 
	 * <p>Title: getUserByUserNameAndRole</p>
	 * <p>Description: 根据账号查找业主</p>
	 * @param username
	 * @return
	 */
	CrmUser getUserByUserNameAndRole(Map map);

	/**
	 * 查询角色为业主的用户
	 * @param roleName 
	 * @return
	 */
	List<CrmUser> selectCrmUserByRoleName(String roleName);

	/**
	 * 删除角色后，相应的用户和此角色的关系也删除
	 * @param roleId
	 * @return
	 */
	Boolean deleteRoleAndCrmUser(@Param("roleId")Integer roleId,@Param("crmuserId") Integer crmuserId);

	/**
	 * 查询关系
	 * @param userAndRole
	 * @return
	 */
	List<CrmUserAndRole> selectRoleAndCrmUser(CrmUserAndRole userAndRole);
	/**
	 * 为用户授权
	 */
	Boolean insertPermForUser(@Param("userAndPermList")List<UserAndPerm> userAndPermList);
	/**
	 * 查询用户权限
	 */
	List<Perm>selectPermByUserId(Integer crmuserId);
	/**
	 * 删除用户权限
	 */
	Boolean delPermByUser(Integer crmuserId);

}
