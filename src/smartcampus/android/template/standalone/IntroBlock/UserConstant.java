package smartcampus.android.template.standalone.IntroBlock;

import android.smartcampus.template.standalone.Utente;

public class UserConstant {

	private static Utente user;

	public static void setUser(Utente usr) {
		user = usr;
	}

	public static Utente getUser() {
		return user;
	}

}
