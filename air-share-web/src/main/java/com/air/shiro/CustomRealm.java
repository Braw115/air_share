package com.air.shiro;


import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.stereotype.Component;

import com.air.crmuser.service.AuthorityService;
import com.air.crmuser.service.CrmUserLogin;
import com.air.crmuser.service.CrmUserService;
import com.air.pojo.entity.CrmUser;
import com.air.pojo.entity.Perm;
import com.air.pojo.entity.Role;
import com.air.utils.EmptyUtils;
@Component
public class CustomRealm extends AuthorizingRealm{
	
	@Resource
	private CrmUserLogin crmUserLogin;
	@Resource
	private CrmUserService crmUserService;
	@Resource
	private AuthorityService authorityService;
	
	/**
     * 授权
     * @param principalCollection
     * @return
     */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
	
		// 给当前用户授权的权限（功能权限、角色）
		SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
		// 两种方式：
		// 方式1：工具类来获取(首长-)
		// User user=(User)SecurityUtils.getSubject().getPrincipal();
		// 方式2：通过参数获取首长(推荐)
		String username = (String) principals.getPrimaryPrincipal();
		System.err.println(username);
		CrmUser crmuser = crmUserService.queryUserByUsername(username);
		// 实际：需要根据当前用户的角色和功能权限来构建一个授权信息对象，交给安全管理器
		// 如果是CRM超级管理员
		if (username.equals("root")) {
			// 查询出所有的角色，给认证信息对象
			List<Role> roleList = authorityService.queryRole(new Role());
			for (Role role : roleList) {
				authorizationInfo.addRole(role.getRoleName());
			}
			// 查询出所有的功能权限，给认证对象
			List<Perm> permList = authorityService.queryPerm(new Perm());
			for (Perm perm : permList) {
				authorizationInfo.addStringPermission(perm.getPermName());
			}
			return authorizationInfo;// 将授权信息交给安全管理器接口。
		}
		
		// 非超管用户
//		List<Role> roleList = authorityService.queryRoleByCrmUser(crmuser);
//		for (Role role : roleList) {
//			authorizationInfo.addRole(role.getRoleName());
//			// 导航查询,获取某角色的拥有的功能权限
//			List<Perm> permList = authorityService.queryPermByRole(role);
//			
//			for (Perm perm : permList) {
//				authorizationInfo.addStringPermission(perm.getPermName());
//			}
//		}
		//非超管用户
		List<Perm> permList = crmUserService.queryPermByCrmUser(crmuser.getCrmuserId());
		List<Role> roleList = crmUserService.queryRoleByCrmUser(crmuser);
		for(Role role:roleList) {
			authorizationInfo.addRole(role.getRoleName());
		}
		for (Perm perm : permList) {
			authorizationInfo.addStringPermission(perm.getPermName());
		}
		return authorizationInfo;//将授权信息交给安全管理器接口。
	}
	
	/**
     * 认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
	
	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
		 // 1、把AuthenticationToken转换为UsernamePasswordToken
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) token;
        // 2、从UsernamePasswordToken中获取username
        String username = usernamePasswordToken.getUsername();
        char[] password = usernamePasswordToken.getPassword();
        StringBuffer sBuffer = new StringBuffer();
        for (char c : password) {
			sBuffer.append(c);
		}
        System.out.println("登录的用户名为:"+username);
        System.out.println("加盐后的密码为:"+sBuffer);
        System.out.println("加盐后的密码为:"+sBuffer.toString());
        
        // 3、调用数据库的方法，从数据库中查询username对应的用户记录
        System.out.println("1.从数据库中查询");
        CrmUser crmuser = null;
        try{
        	 crmuser = crmUserService.queryUserByUsername(username);
        }catch(Exception e) {
        	if(e instanceof AuthenticationException) {
        		throw new UnknownAccountException("用户不存在");
        	}
        }
        
        // 4、若用户不存在，则抛出UnknownAccountException异常
        if (EmptyUtils.isEmpty(crmuser)){
            throw new UnknownAccountException("用户不存在");
        }
        System.out.println(crmuser.getPassword());
        // 5、根据用户信息情况，决定是否需要抛出其他的AuthenticationException异常
        System.out.println(sBuffer.equals(crmuser.getPassword()));
        if (!sBuffer.toString().equals(crmuser.getPassword())) {
        	throw new UnknownAccountException("密码错误");
        }

        // 6、根据用户的情况，来构建AuthenticationInfo对象返回，通常使用的实现类为：SimpleAuthenticationInfo
        // 以下信息是从数据库中获取的
        // 参数 ： principal ---> 认证的实体信息，可以是username，也可以是数据表对应的用户实体类对象
        Object principal = username;
        // 参数 ：credentials ---> 密码
        Object credentials =crmuser.getPassword(); 
        // 参数 ： realmName ---> 当前realm对象的name。调用父类的getName()方法即可
        String realmName = this.getName();
        
        //将用户名作为盐
        ByteSource salt = ByteSource.Util.bytes(username);
        
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(principal, credentials, salt, realmName);

        return info;
	}

}
