package smartcampus.android.template.standalone.IntroBlock;

import android.smartcampus.template.standalone.Utente;

public class UserConstant {

	private static Utente user;
	private static String username;
	private static String password;

	public static void setUser(Utente usr) {
		user = usr;
	}

	public static Utente getUser() {
		return user;
	}

	public static String getUsername() {
		return username;
	}

	public static void setUsername(String username) {
		UserConstant.username = username;
	}

	public static String getPassword() {
		return password;
	}

	public static void setPassword(String password) {
		UserConstant.password = password;
	}
}
