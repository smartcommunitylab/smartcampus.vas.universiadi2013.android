package smartcampus.android.template.standalone.Activity.ProfileBlock.RisolutoreSubBlock;

import org.apache.http.util.EncodingUtils;

import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.IntroBlock.UserConstant;
import smartcampus.android.template.universiadi.R;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

public class IceFireWebView extends Activity {

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_ice_fire);

		WebView webView = (WebView) findViewById(R.id.ice_fire_webview);
		byte[] post = EncodingUtils.getBytes(
				"username=" + UserConstant.getUsername() + "&password="
						+ UserConstant.getPassword() + "&csrf_token="
						+ ManagerData.getToken(), "BASE64");
		webView.postUrl(getString(R.string.URL_ICE_AND_FIRE), post);
	}

}
