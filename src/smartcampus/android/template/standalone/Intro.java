package smartcampus.android.template.standalone;

import java.io.IOException;

import com.parse.Parse;
import com.parse.PushService;

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
import android.os.Bundle;
import android.accounts.AccountManager;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;

public class Intro extends Activity {
	
	private AMSCAccessProvider mAccessProvider;
	public static String mToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Parse.initialize(this, "AhQ6rwCF37SX07VVmnoDjA4hYWC7r0b76CbGfstk", "18K9cyvvzVDTwcsFYrkzUkzBWVR5fWNtZ86rq9yv"); 
		PushService.subscribe(this, "", Intro.class);
		
		try {
			Constants.setAuthUrl(getApplicationContext(), 
					"https://ac.smartcampuslab.it/accesstoken-provider-dev/ac");
		} catch (NameNotFoundException e1) {
			Log.e("Error", "problems with configuration.");
			finish();
		}

		//Retrieve the User's Token
		mAccessProvider = new AMSCAccessProvider();
		if (mAccessProvider.readUserData(this, "google") != null)
		{
			mToken = mAccessProvider.readUserData(this, "google").getToken();
			startActivity(new Intent(this, HomeGuest.class));
		}
		else if (mAccessProvider.readUserData(this, "unitn") != null)
		{
			mToken = mAccessProvider.readUserData(this, "unitn").getToken();
			startActivity(new Intent(this, Home.class));
		}
		else
		{
			setContentView(R.layout.activity_intro);
			
			final ImageView mGuest = (ImageView)findViewById(R.id.image_guest);
			mGuest.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						mGuest.setImageResource(R.drawable.button_guest_down);
						return true;
					}
					if (event.getAction() == MotionEvent.ACTION_UP)
					{
						mGuest.setImageResource(R.drawable.button_guest_up);
						getLoginRequest("google");
						return true;
					}
					return false;
				}
				
			});
			
			final ImageView mLogin = (ImageView)findViewById(R.id.image_login);
			mLogin.setOnTouchListener(new OnTouchListener(){

				@Override
				public boolean onTouch(View arg0, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (arg1.getAction() == MotionEvent.ACTION_DOWN)
					{
						mLogin.setImageResource(R.drawable.button_login_down);
						return true;
					}
					if (arg1.getAction() == MotionEvent.ACTION_UP)
					{
						mLogin.setImageResource(R.drawable.button_login_up);
						getLoginRequest("unitn");
						return true;
					}
					return false;
				}
				
			});
		}
	}
	
	private void getLoginRequest(String inAuth)
	{
		try {
			mAccessProvider.getAuthToken(this, inAuth);
		} catch (OperationCanceledException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AuthenticatorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// check the result of the authentication
		if (requestCode == SCAccessProvider.SC_AUTH_ACTIVITY_REQUEST_CODE) {
			// authentication successful
			if (resultCode == RESULT_OK) {
				Attribute att = null;
				UserData a = mAccessProvider.readUserData(this, null);
				if (mAccessProvider.readUserData(this, "google") != null)
					att = mAccessProvider.readUserData(this, "google")
						.getAttributes().get(0);
				else if (mAccessProvider.readUserData(this, "unitn") != null)
					att = mAccessProvider.readUserData(this, "unitn")
					.getAttributes().get(0);
					
				if (att.getAuthority().getName().equals("unitn"))
				{
					mToken = mAccessProvider.readUserData(this, "unitn").getToken();
					startActivity(new Intent(this, Home.class));
				}
				else
				{
					mToken = mAccessProvider.readUserData(this, "google").getToken();
					startActivity(new Intent(this, HomeGuest.class));
				}
			// authentication cancelled by user
			} else if (resultCode == RESULT_CANCELED) {
			// authentication failed	
			} else {
				//Call Notification
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
