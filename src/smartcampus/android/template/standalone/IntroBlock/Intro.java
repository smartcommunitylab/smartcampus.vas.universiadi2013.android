package smartcampus.android.template.standalone.IntroBlock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.drawable;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Activity.Model.DBManager;
import smartcampus.android.template.standalone.Activity.Model.DownloadManager;
import smartcampus.android.template.standalone.HomeBlock.Home;
import smartcampus.android.template.standalone.Utilities.SaveDBService;

import eu.trentorise.smartcampus.ac.ACService;
import eu.trentorise.smartcampus.ac.AcServiceException;
import eu.trentorise.smartcampus.ac.Constants;
import eu.trentorise.smartcampus.ac.SCAccessProvider;
import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.ac.model.Attribute;
import eu.trentorise.smartcampus.ac.model.UserData;
import eu.trentorise.smartcampus.profileservice.ProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.*;

public class Intro extends Activity {

	private AMSCAccessProvider mAccessProvider;
	public static String mToken;

	public static boolean needDWN = true;

	private Context mContext = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		(DBManager.getInstance(getApplicationContext())).upgradeVersion();

		startUsingApp();
	}

	private void startUsingApp() {
		// new AsyncRequest().execute(new Void[1]);
		try {
			Constants.setAuthUrl(getApplicationContext(),
					"https://ac.smartcampuslab.it/accesstoken-provider-dev/ac");
		} catch (NameNotFoundException e1) {
			Log.e("Error", "problems with configuration.");
			finish();
		}

		// Retrieve the User's Token
		// mAccessProvider = new AMSCAccessProvider();
		// if (mAccessProvider.readUserData(this, "google") != null) {
		// mToken = mAccessProvider.readUserData(this, "google").getToken();
		// startActivity(new Intent(this, HomeGuest.class));
		// } else if (mAccessProvider.readUserData(this, "unitn") != null) {
		// mToken = mAccessProvider.readUserData(this, "unitn").getToken();
		// startActivity(new Intent(this, Home.class));
		// } else {

		final ImageView mGuest = (ImageView) findViewById(R.id.image_guest);
		mGuest.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					mGuest.setImageResource(R.drawable.button_guest_down);
					return true;
				}
				return false;
			}

		});

		final ImageView mLogin = (ImageView) findViewById(R.id.image_login);
		mLogin.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					mLogin.setImageResource(R.drawable.button_login_down);
					return true;
				}
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					mLogin.setImageResource(R.drawable.button_login_up);
					(new DownloadManager(mContext))
							.execute(new String[][] { new String[] {
									getString(R.string.AUTH_TOKEN), "evento" } });

					new Thread(new Runnable() {
						public void run() {
							while (true) {
//								if (DownloadManager.isReady()) {
									startActivity(new Intent(
											getApplicationContext(), Home.class));
									Intent mService = new Intent(
											getApplicationContext(),
											SaveDBService.class);
									startService(mService);
									break;
								}
							//}
						}
					}).start();
//					startActivity(new Intent(
//					getApplicationContext(), Home.class));
					// getLoginRequest("unitn");
					return true;
				}
				return false;
			}

		});
	}

	// private void getLoginRequest(String inAuth) {
	// try {
	// mAccessProvider.getAuthToken(this, inAuth);
	// } catch (OperationCanceledException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (AuthenticatorException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// } catch (SecurityException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	// protected void onActivityResult(int requestCode, int resultCode, Intent
	// data) {
	// // check the result of the authentication
	// if (requestCode == SCAccessProvider.SC_AUTH_ACTIVITY_REQUEST_CODE) {
	// // authentication successful
	// if (resultCode == RESULT_OK) {
	// Attribute att = null;
	// UserData a = mAccessProvider.readUserData(this, null);
	// if (mAccessProvider.readUserData(this, "google") != null)
	// att = mAccessProvider.readUserData(this, "google")
	// .getAttributes().get(0);
	// else if (mAccessProvider.readUserData(this, "unitn") != null)
	// att = mAccessProvider.readUserData(this, "unitn")
	// .getAttributes().get(0);
	//
	// if (att.getAuthority().getName().equals("unitn")) {
	// mToken = mAccessProvider.readUserData(this, "unitn")
	// .getToken();
	// startActivity(new Intent(this, Home.class));
	// } else {
	// mToken = mAccessProvider.readUserData(this, "google")
	// .getToken();
	// startActivity(new Intent(this, HomeGuest.class));
	// }
	// // authentication cancelled by user
	// } else if (resultCode == RESULT_CANCELED) {
	// // authentication failed
	// } else {
	// // Call Notification
	// }
	// }
	// super.onActivityResult(requestCode, resultCode, data);
	// }
}
