package jp.co.internous.freesia.model.session;

import java.io.Serializable;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

@Component
@Scope(value="session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class LoginSession implements Serializable{
	
	private static final long serialVersionUID = 1484173977722780073L;
	private String userName;
	private String password;
	private int userId;
	private int temporaryUserId;
	private boolean isLogin;
	
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getUserId() {
		return userId;
	}
	
	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public int getTemporaryUserId() {
		return temporaryUserId;
	}
	
	public void setTemporaryUserId(int temporaryUserId) {
		this.temporaryUserId = temporaryUserId;
	}
	
	public boolean getIsLogin() {
		return isLogin;
	}
	
	public void setIsLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}
	
}