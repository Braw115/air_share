package com.air.crmuser.service;

import java.util.List;

import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.Perm;
import com.air.pojo.entity.Role;

public interface AuthorityService {

	List<Role> queryRole(Role role);

	List<Role> queryRoleByCrmUser(CrmUser crmuser);

	List<Perm> queryPerm(Perm perm);

	List<Perm> queryPermByRole(Role role);
}

