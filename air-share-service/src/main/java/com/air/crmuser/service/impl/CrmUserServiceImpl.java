package com.air.crmuser.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.area.mapper.AreaMapper;
import com.air.constant.Dto;
import com.air.constant.DtoUtil;
import com.air.constant.ErrorCode;
import com.air.constant.MD5;
import com.air.crmuser.mapper.CrmUserMapper;
import com.air.crmuser.service.CrmUserService;
import com.air.pojo.entity.Area;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.CrmUserAndRole;
import com.air.pojo.entity.Perm;
import com.air.pojo.entity.Role;
import com.air.pojo.entity.RoleAndPerm;
import com.air.pojo.entity.UserAndPerm;
import com.air.utils.EmptyUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

@Service
@Transactional
public class CrmUserServiceImpl implements CrmUserService {

	
	@Resource
	private CrmUserMapper crmUserMapper;
	@Resource
	private AreaMapper areaMapper;
	@Override
	public CrmUser queryUserByUsername(String username) {
			CrmUser crmUser = crmUserMapper.selectCrmUserByUsername(username);
			String roleName; 
			List<Role> roleList= crmUserMapper.selectRoleByCrmUser(crmUser);
			if(roleList == null ||roleList.size() == 0) {
				roleName = null;
			}else {
				roleName = roleList.get(0).getRoleName();
				}
			crmUser.setRole(roleName);
			if(crmUser.getAreaId() == null) {
				crmUser.setAreaName(null);
			}else {
				Area area = new Area();
				area.setAreaId(crmUser.getAreaId());
				String areaName;
				List<Area> areaList = areaMapper.selectAreaList(area);
				if(areaList == null || areaList.size() == 0) {
					areaName =null;
				}else {
					areaName = areaList.get(0).getName();
				}
				crmUser.setAreaName(areaName);
			}
			
			return crmUser;
	}

	@Override
	public PageInfo<Perm> queryAllPerm(Integer curPage, Integer pageSize) {
		PageHelper.startPage(curPage, pageSize);
		List<Perm> perm = crmUserMapper.selectPerm(new Perm());
		PageInfo<Perm> pageInfo = new PageInfo<Perm>(perm);
		return pageInfo;
	}

	@Override
	public Boolean addCrmUser(CrmUser crmuser) {
		String newPs = MD5.getMd5(crmuser.getPassword(), 32);
		crmuser.setPassword(newPs);
		CrmUser dbCrmUser = crmUserMapper.selectCrmUserByUsername(crmuser.getUsername());
		if (EmptyUtils.isNotEmpty(dbCrmUser)) {
			return false;
		}
		return crmUserMapper.insertCrmUser(crmuser);
	}

	@Override
	public PageInfo<CrmUser> getAllCrmUser(Integer curPage, Integer pageSize,CrmUser crmuser) {
		PageHelper.startPage(curPage, pageSize);
		List<CrmUser> crmuesrs = crmUserMapper.selectCrmUser(crmuser);
		PageInfo<CrmUser> pageInfo = new PageInfo<CrmUser>(crmuesrs);
		return pageInfo;
	}

	@Override
	public Boolean modifyCrmUser(CrmUser crmuser) {
		String newPs = MD5.getMd5(crmuser.getPassword(), 32);
		crmuser.setPassword(newPs);
		return crmUserMapper.updateCrmUser(crmuser);
	}

	@Override
	public Boolean delCrmUser(Integer crmuserId) {
		return crmUserMapper.deleteCrmUser(crmuserId);
	}

	@Override
	public Boolean addRole(Role role) {
		return crmUserMapper.insertRole(role);
	}

	@Override
	public PageInfo<Role> getAllRole(Integer curPage, Integer pageSize) {
		PageHelper.startPage(curPage, pageSize);
		List<Role> role = crmUserMapper.selectRole(new Role());
		PageInfo<Role> pageInfo = new PageInfo<Role>(role);
		return pageInfo;
	}

	@Override
	public Boolean delRole(Integer roleId) {
		return crmUserMapper.deleteRole(roleId);
	}
	
	/**
	 * 分配角色
	 */
	@Override
	public Boolean addRoleAndCrmUser(CrmUserAndRole crmUserAndRole,Integer crmUserId) {
		boolean flag;
		Subject subject = SecurityUtils.getSubject();
		
		Role role = new Role();
		role.setRoleId(crmUserAndRole.getRoleId());
		Role dbRole = crmUserMapper.selectRole(role).get(0);
		String nowRoleName = dbRole.getRoleName();
		CrmUser crmUser= new CrmUser();
		
		crmUser.setCrmuserId(crmUserAndRole.getCrmuserId());
		List<Role> roleList = crmUserMapper.selectRoleByCrmUser(crmUser);
		
		if(roleList.size()==0) {//之前角色为空
			if("root".equals(nowRoleName)) {
				flag = false;
			}else if ("admin".equals(nowRoleName)) {
				
				if(subject.hasRole("root")) {
					flag = true;
				}else {
					flag = false;
				}
			}else if("partner".equals(nowRoleName)) {
				if(subject.hasRole("admin")||subject.hasRole("root")) {
					flag = true;
				}else {
					flag = false;
				}
			}else if("owner".equals(nowRoleName)) {
				if(subject.hasRole("admin")||subject.hasRole("root")) {
					flag = true;
				}else {
					flag = false;
				}
			}else if("finance".equals(nowRoleName)) {
				if(subject.hasRole("admin")||subject.hasRole("root")) {
					flag = true;
				}else {
					flag = false;
				}
			}else {
				flag = false;
			}	
		}else{//之前存在角色
			String previousRolrNasme = roleList.get(0).getRoleName();
			if("root".equals(previousRolrNasme)) {//之前角色为root
				flag = false;
			}else if("admin".equals(previousRolrNasme)) {//之前角色为admin
				
				if(subject.hasRole("root") && !"root".equals(nowRoleName) ) {
					flag = true;
				}else {
					flag = false;
				}
			}else {//之前角色为partner owner finance
				if("root".equals(nowRoleName)) {
					flag = false;
				}else if ("admin".equals(nowRoleName)) {
					if(subject.hasRole("root") ) {
						flag = true;
					}else {
						flag = false;
					}
				}else {
					if(subject.hasRole("admin")||subject.hasRole("root")) {
						flag = true;
					}else {
						flag = false;
					}
				}
			}
		}
			
		if(flag) {
			crmUserMapper.deleteRoleByCrmUser(crmUserAndRole);
			//分配角色
			Boolean bool = crmUserMapper.insertRoleAndCrmUser(crmUserAndRole);
			return bool;
			//为角色分配默认权限
//			List<Perm> permList = crmUserMapper.selectPermByRole(role);
//			List<UserAndPerm> userAndPermList = new ArrayList<UserAndPerm>();
//			for(Perm perm : permList) {
//				UserAndPerm userAndPerm =new UserAndPerm();
//				userAndPerm.setPermId(perm.getPermId());
//				userAndPerm.setCrmUserId(crmUserAndRole.getCrmuserId());
//				userAndPermList.add(userAndPerm);
//			}
//			Boolean b = crmUserMapper.insertPermForUser(userAndPermList);
//			if(bool && b) {
//				return true;
//			}	
		}else {
			return false;
		}
		
	
}

	@Override
	public PageInfo<Role> queryRoleByCrmUser(Integer curPage, Integer pageSize, CrmUser crmuser) {
		PageHelper.startPage(curPage, pageSize);
		List<Role> role = crmUserMapper.selectRoleByCrmUser(crmuser);
		PageInfo<Role> pageInfo = new PageInfo<Role>(role);
		return pageInfo;
	}
	@Override
	public List<Role> queryRoleByCrmUser(CrmUser crmuser) {
		
		List<Role> roleList = crmUserMapper.selectRoleByCrmUser(crmuser);
		return roleList;
		
	}

	@Override
	public Boolean delRoleByCrmUser(CrmUserAndRole crmUserAndRole) {
		return crmUserMapper.deleteRoleByCrmUser(crmUserAndRole);
	}

	@Override
	public Dto addPermForRole(List<RoleAndPerm> roleAndPermList) {
		Boolean bool = crmUserMapper.insertPermForRole(roleAndPermList);
		if (!bool) {
			return DtoUtil.returnFail("失败", ErrorCode.RESP_ERROR);
		}
		return DtoUtil.returnSuccess();
	}

	@Override
	public List<Perm> queryPermByRole(Role role) {
		List<Perm> perm = crmUserMapper.selectPermByRole(role);
		return perm;
	}

	@Override
	public Boolean delPerm(Integer roleId) {
		
		Role role = new Role();
		role.setRoleId(roleId);
		List<Perm> list = crmUserMapper.selectPermByRole(role);
		
		if (list.isEmpty()) {
			return true;
		}
		
		return crmUserMapper.deletePerm(roleId);
	}

	@Override
//	public List<CrmUser> queryCrmUser(CrmUser crmuser) {
	public CrmUser queryCrmUser(CrmUser crmuser) {	
//		return crmUserMapper.selectCrmUser(crmuser);
		CrmUser dbCrmUser = crmUserMapper.selectCrmUser(crmuser).get(0);
		List<Role> list = crmUserMapper.selectRoleByCrmUser(crmuser);
		if (list.isEmpty()) {
			dbCrmUser.setRole("");
		}else {
			dbCrmUser.setRole(list.get(0).getRoleName());
		}
		
		return dbCrmUser;
	}

	@Override
	public List<CrmUser> getCrmUserByRoleName(String roleName) {
		
		return crmUserMapper.selectCrmUserByRoleName(roleName);
	}

	@Override
	public Boolean delRoleAndCrmUser(Integer roleId,Integer crmuserId) {
		CrmUserAndRole userAndRole = new CrmUserAndRole();
		userAndRole.setCrmuserId(crmuserId);
		userAndRole.setRoleId(roleId);
		List<CrmUserAndRole> list = crmUserMapper.selectRoleAndCrmUser(userAndRole);
		
		if (list.isEmpty()) {
			return true;
		}
		
		return crmUserMapper.deleteRoleAndCrmUser(roleId,crmuserId);
	}
	
	 
	/**
	 * 授权(更新)用户权限
	 */
	public Dto updatePermForCrmUser(List<UserAndPerm> userAndPermList,Integer crmUserId) {
		//删除原有权限
		Boolean bool;
		List<Perm> permList =crmUserMapper.selectPermByUserId(crmUserId);
		
		if(permList.size()==0) {
			bool =true;
		}else {
			bool = crmUserMapper.delPermByUser(crmUserId);
		}
		if (!bool) {
			return DtoUtil.returnFail("失败", ErrorCode.RESP_ERROR);
		}
		if(!userAndPermList.isEmpty()) {
			//插入权限
			crmUserMapper.insertPermForUser(userAndPermList);
		}
		return  DtoUtil.returnSuccess();
	}
	/**
	 * 查询用户权限
	 */
	public List<Perm> queryPermByCrmUser(Integer crmUserId) {
		
	
		List<Perm>  permList = crmUserMapper.selectPermByUserId(crmUserId);
		return permList;
	}
}
