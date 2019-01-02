package com.air.crmuser.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.air.crmuser.mapper.CrmUserMapper;
import com.air.crmuser.service.AuthorityService;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.Perm;
import com.air.pojo.entity.Role;


@Service
@Transactional
public class AuthorityServiceImpl implements AuthorityService {

	@Resource
	private CrmUserMapper crmUserMapper;
	
	@Override
	public List<Role> queryRole(Role role) {
		
		return crmUserMapper.selectRole(role);
	}

	@Override
	public List<Role> queryRoleByCrmUser(CrmUser crmuser) {
		return crmUserMapper.selectRoleByCrmUser(crmuser);
	}

	@Override
	public List<Perm> queryPerm(Perm perm) {
		return crmUserMapper.selectPerm(perm);
	}

	@Override
	public List<Perm> queryPermByRole(Role role) {
		return crmUserMapper.selectPermByRole(role);
	}

	
}

