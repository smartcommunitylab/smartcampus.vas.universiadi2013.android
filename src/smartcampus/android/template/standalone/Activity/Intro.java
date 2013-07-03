package smartcampus.android.template.standalone.Activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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
import org.json.simple.parser.JSONParser;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.drawable;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;

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
import android.widget.*;

public class Intro extends Activity {

	private AMSCAccessProvider mAccessProvider;
	public static String mToken;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_intro);
		startActivity(new Intent(getApplicationContext(), Home.class));
//		new AsyncRequest().execute(new Void[1]);
		// try {
		// Constants.setAuthUrl(getApplicationContext(),
		// "https://ac.smartcampuslab.it/accesstoken-provider-dev/ac");
		// } catch (NameNotFoundException e1) {
		// Log.e("Error", "problems with configuration.");
		// finish();
		// }
		//
		// //Retrieve the User's Token
		// mAccessProvider = new AMSCAccessProvider();
		// if (mAccessProvider.readUserData(this, "google") != null)
		// {
		// mToken = mAccessProvider.readUserData(this, "google").getToken();
		// startActivity(new Intent(this, HomeGuest.class));
		// }
		// else if (mAccessProvider.readUserData(this, "unitn") != null)
		// {
		// mToken = mAccessProvider.readUserData(this, "unitn").getToken();
		// startActivity(new Intent(this, Home.class));
		// }
		// else
		// {
		// setContentView(R.layout.activity_intro);
		//
		// final ImageView mGuest = (ImageView)findViewById(R.id.image_guest);
		// mGuest.setOnTouchListener(new OnTouchListener(){
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// // TODO Auto-generated method stub
		// if (event.getAction() == MotionEvent.ACTION_DOWN)
		// {
		// mGuest.setImageResource(R.drawable.button_guest_down);
		// return true;
		// }
		// if (event.getAction() == MotionEvent.ACTION_UP)
		// {
		// mGuest.setImageResource(R.drawable.button_guest_up);
		// getLoginRequest("google");
		// return true;
		// }
		// return false;
		// }
		//
		// });
		//
		// final ImageView mLogin = (ImageView)findViewById(R.id.image_login);
		// mLogin.setOnTouchListener(new OnTouchListener(){
		//
		// @Override
		// public boolean onTouch(View arg0, MotionEvent arg1) {
		// // TODO Auto-generated method stub
		// if (arg1.getAction() == MotionEvent.ACTION_DOWN)
		// {
		// mLogin.setImageResource(R.drawable.button_login_down);
		// return true;
		// }
		// if (arg1.getAction() == MotionEvent.ACTION_UP)
		// {
		// mLogin.setImageResource(R.drawable.button_login_up);
		// getLoginRequest("unitn");
		// return true;
		// }
		// return false;
		// }
		//
		// });
		// }
	}

	private class AsyncRequest extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			JSONArray responseString = null;
			try {
				HttpGet request = new HttpGet(
						"http://smartcampusvasuniversiadi2013web.app.smartcampuslab.it/evento");
				request.addHeader("AUTH_TOKEN",
						"aee58a92-d42d-42e8-b55e-12e4289586fc");
				response = httpclient.execute(request);
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = new JSONArray(out.toString());
				} else {
					// Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {
				// TODO Handle problems..
			} catch (IOException e) {
				// TODO Handle problems..
			}
			// Creating JSON Parser instance
			catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			for (int i = 0; i < responseString.length(); i++) {
				try {
					Log.i("Nome " + Integer.toString(i), responseString
							.getJSONObject(i).getString("nome"));
					Log.i("Descrizione " + Integer.toString(i), responseString
							.getJSONObject(i).getString("descrizione"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

			try {
				long id = responseString.getJSONObject(1).getLong("id");
				httpclient = new DefaultHttpClient();
				responseString = null;
				HttpGet request = new HttpGet(
						"http://smartcampusvasuniversiadi2013web.app.smartcampuslab.it/evento/"
								+ Long.toString(id));
				request.addHeader("AUTH_TOKEN",
						"aee58a92-d42d-42e8-b55e-12e4289586fc");
				response = httpclient.execute(request);
				StatusLine statusLine = response.getStatusLine();
				JSONObject responseID = null;
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseID = new JSONObject(out.toString());
				}

				Log.i("Nome from ID", responseID.getString("nome"));
				Log.i("Descrizione from ID", responseID.getString("descrizione"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
	}

	private void getLoginRequest(String inAuth) {
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
