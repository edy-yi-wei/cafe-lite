package com.besoft.cafelite.auth;

import java.sql.Timestamp;
import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.CashierSession;
import com.besoft.cafelite.model.Role;
import com.besoft.cafelite.model.RoleModule;
import com.besoft.cafelite.model.User;
import com.besoft.cafelite.service.CashierSessionService;
import com.besoft.cafelite.service.UserService;

@Service
public class SecurityService {
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private UserDetailsService userDetailService;
	@Autowired
	private UserService userService;
	@Autowired
	private CashierSessionService sessionService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public String getLoginUser() {
		logger.info("SecurityService - getLoginUser");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth!=null) {
			return auth.getName();
		} else {
			return null;
		}		
	}
	
	public boolean isAuthenticatedMenu(String menuName) throws Exception {
		logger.info("SecurityService - isAuthenticatedMenu [menuName: "+menuName+"]");
		
		try {
			String userName = getLoginUser();
			User user = userService.getUser(userName);
			Role role = user.getRole();
			for(RoleModule m: role.getModuleList()) {
				if(m.getModule().getModuleName().equalsIgnoreCase(menuName)) {
					return true;
				}
			}
			return false;
		} catch(Exception ex) {
			logger.error("SecurityService - isAuthenticatedMenu "+ex.getMessage());
			throw ex;
		}
	}
	
	@Transactional(rollbackOn = Exception.class)
	public String login(String userName, String password) throws Exception{
		logger.info("SecurityService - login[user name:"+userName+"]");
		
		try {
			UserDetails userDetails = userDetailService.loadUserByUsername(userName);
			UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, password, userDetails.getAuthorities());
			authManager.authenticate(token);
			if(token.isAuthenticated()) {
				SecurityContextHolder.getContext().setAuthentication(token);
				logger.info(userName + " berhasil login!");
				/*
				 * CREATE SESSION
				 */
				User usr = userService.getUser(userName);
	//			if(usr.getRole().getRoleName().equalsIgnoreCase("Kasir")) {
					CashierSession session = sessionService.getCashierSession(userName);
					if(session==null) {
						session = new CashierSession();
						session.setCashAmount(0d);
						session.setCashier(usr);
						session.setLoginTime(new Timestamp(new Date().getTime()));
						sessionService.save(session);
					}
	//			}
				/* --CREATE LOGIN HISTORY-- */
				
					
				return token.getName();
			} else {
				return null;
			}
		} catch(Exception ex) {
			logger.info("ERROR SecurityService - login "+ex.getMessage());
			throw ex;
		}
		
	}
	
	public void logout() throws Exception{
		logger.info("SecurityService - logout");
//		String userName = getLoginUser();
//		System.out.println(userName);
//		logger.info(userName+" berhasil logout");
		SecurityContextHolder.getContext().setAuthentication(null);
	}
	
	public UserDetails isAuthenticated(String userName, String userPassword) {
		UserDetails userDetails = userDetailService.loadUserByUsername(userName);
		UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userDetails, userPassword, userDetails.getAuthorities());
		authManager.authenticate(token);
		if(token.isAuthenticated()) {
			return userDetails;
		} else {
			return null;
		}
	}		
}
