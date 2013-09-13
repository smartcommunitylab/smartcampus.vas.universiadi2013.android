package smartcampus.android.template.standalone.Activity.Model;

public class JuniperResponse {
	private String access_token;
	private String token_type;
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getToken_type() {
		return token_type;
	}
	public void setToken_type(String token_type) {
		this.token_type = token_type;
	}
	public boolean isLogged() {
		return getAccess_token()!=null && getAccess_token().compareTo("")!=0;
	}

}
