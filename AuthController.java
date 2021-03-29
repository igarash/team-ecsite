package jp.co.internous.freesia.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import jp.co.internous.freesia.model.session.LoginSession;
import jp.co.internous.freesia.model.mapper.MstUserMapper;
import jp.co.internous.freesia.model.domain.MstUser;
import jp.co.internous.freesia.model.form.UserForm;
import jp.co.internous.freesia.model.mapper.TblCartMapper;



@RestController
@RequestMapping("/freesia/auth")
public class AuthController {
	
	private Gson gson = new Gson();

	@Autowired
	private LoginSession loginSession;

	@Autowired
	MstUserMapper mstUserMapper;

	@Autowired
	private TblCartMapper cartMapper;

	@PostMapping("/login")
	public String login(@RequestBody UserForm f) {
		MstUser user = mstUserMapper.findByUserNameAndPassword(f.getUserName(), f.getPassword());

		int tmpUserId = loginSession.getTemporaryUserId();
		// 仮IDでカート追加されていれば、本ユーザーIDに更新する。

		if (user != null && tmpUserId != 0) {
			int count = cartMapper.findCountByUserId(tmpUserId);
			if (count > 0) {
				cartMapper.updateUserId(user.getId(), tmpUserId);
			}
		}

		if (user != null ){

			loginSession.setUserName(user.getUserName());
			loginSession.setPassword(user.getPassword());
			loginSession.setUserId(user.getId());
			loginSession.setTemporaryUserId(0);
			loginSession.setIsLogin(true); 

		}else{

			loginSession.setUserName(null);
			loginSession.setPassword(null);
			loginSession.setUserId(0);
			loginSession.setIsLogin(false); 

		}

		return gson.toJson(user);
	
	}

	
	@PostMapping("/logout")
	public String logout() {
		
		loginSession.setUserName(null);
		loginSession.setPassword(null);
		loginSession.setUserId(0);
		loginSession.setTemporaryUserId(0);
		loginSession.setIsLogin(false); 
		
		return "";
	
	}
	
	@RequestMapping("/resetPassword")
	public String resetPassword(@RequestBody UserForm f) {
		String message = "パスワードが再設定されました。";
		String newPassword = f.getNewPassword();
		String newPasswordConfirm = f.getNewPasswordConfirm();

		MstUser user = mstUserMapper.findByUserNameAndPassword(f.getUserName(), f.getPassword());
		if (user == null) {
			return "現在のパスワードが正しくありません。";
		}

		if (user.getPassword().equals(newPassword)) {
			return "現在のパスワードと同一文字列が入力されました。";
		}

		if (!newPassword.equals(newPasswordConfirm)) {
			return "新パスワードと確認用パスワードが一致しません。";
		}
		// mst_userとloginSessionのパスワードを更新する
		mstUserMapper.updatePassword(user.getUserName(), f.getNewPassword());
		loginSession.setPassword(f.getNewPassword());

		return message;
		
	}
}
