package smartcampus.android.template.standalone.IntroBlock;

import java.util.HashMap;
import java.util.Map;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.HomeBlock.Home;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class Intro extends Activity {

	public Map<String, Boolean> loginSuccess = new HashMap<String, Boolean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		ManagerData.getInstance(getApplicationContext());

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Credenziali");
		builder.setMessage("Credenziali per l'accesso\nUsername: 'nome3'\nPassword: 'test'");
		builder.setCancelable(false);
		builder.setPositiveButton("Chiudi",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		builder.create().show();

		// ///REGISTRATION NOTIFICATION//////
		// PushServiceConnector mRegistrator = new PushServiceConnector();
		// try {
		// mRegistrator.init(getApplicationContext());
		// } catch (CommunicatorConnectorException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		// ///////////////////////////////////

		((ImageView) findViewById(R.id.image_guest))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new AsyncTask<Void, Void, Void>() {
							private Dialog dialog;

							@Override
							protected void onPreExecute() {
								// TODO Auto-generated method stub
								super.onPreExecute();

								dialog = new Dialog(Intro.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_wait);
								dialog.getWindow()
										.setBackgroundDrawableResource(
												R.drawable.dialog_rounded_corner_light_black);
								dialog.show();
								dialog.setCancelable(true);
								dialog.setOnCancelListener(new OnCancelListener() {

									@Override
									public void onCancel(DialogInterface dialog) {
										// TODO Auto-generated method stub
										cancel(true);
									}
								});

							}

							@Override
							protected Void doInBackground(Void... params) {
								// TODO Auto-generated method stub
								ManagerData.getAnonymousToken();
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								// TODO Auto-generated method stub
								super.onPostExecute(result);

								dialog.dismiss();

								// START ONPOST

								Intent mCaller = new Intent(
										getApplicationContext(), Home.class);
								mCaller.putExtra("sessionLogin", false);
								startActivity(mCaller);

								// END ONPOST
							}

						}.execute();
					}
				});

		((ImageView) findViewById(R.id.image_login))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						new AsyncTask<Void, Void, Void>() {
							private Dialog dialog;

							@Override
							protected void onPreExecute() {
								// TODO Auto-generated method stub
								super.onPreExecute();

								dialog = new Dialog(Intro.this);
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_wait);
								dialog.getWindow()
										.setBackgroundDrawableResource(
												R.drawable.dialog_rounded_corner_light_black);
								dialog.show();
								dialog.setCancelable(true);
								dialog.setOnCancelListener(new OnCancelListener() {

									@Override
									public void onCancel(DialogInterface dialog) {
										// TODO Auto-generated method stub
										Log.i("", "Cancel");
										cancel(true);
									}
								});

							}

							@Override
							protected Void doInBackground(Void... params) {
								// TODO Auto-generated method stub
								if (controlCredential())
									if (login(
											((EditText) findViewById(R.id.text_username))
													.getText().toString(),
											((EditText) findViewById(R.id.text_password))
													.getText().toString())) {
										InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.hideSoftInputFromWindow(
												((EditText) findViewById(R.id.text_password))
														.getWindowToken(), 0);

										loginSuccess.put("success", true);

									} else {
										loginSuccess.put("success", false);
										loginSuccess.put("notfound", true);
									}
								else {
									loginSuccess.put("success", false);
									loginSuccess.put("notfound", false);
								}
								return null;
							}

							@Override
							protected void onPostExecute(Void result) {
								// TODO Auto-generated method stub
								super.onPostExecute(result);

								dialog.dismiss();

								// START ONPOST

								controlAfterLogin();

								// END ONPOST
							}

						}.execute();

					}
				});

		// ((EditText) findViewById(R.id.text_username)).setSelection(3);
		// ((EditText) findViewById(R.id.text_password)).setSelection(3);
		((EditText) findViewById(R.id.text_password)).setTypeface(Typeface
				.createFromAsset(getApplicationContext().getAssets(),
						"PatuaOne-Regular.otf"));
		((EditText) findViewById(R.id.text_username)).setTypeface(Typeface
				.createFromAsset(getApplicationContext().getAssets(),
						"PatuaOne-Regular.otf"));
		((EditText) findViewById(R.id.text_password))
				.setOnEditorActionListener(new OnEditorActionListener() {

					@Override
					public boolean onEditorAction(TextView v, int actionId,
							KeyEvent event) {
						// TODO Auto-generated method stub
						if (actionId == EditorInfo.IME_ACTION_DONE) {
							new AsyncTask<Void, Void, Void>() {
								private Dialog dialog;

								@Override
								protected void onPreExecute() {
									// TODO Auto-generated method stub
									super.onPreExecute();

									dialog = new Dialog(Intro.this);
									dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
									dialog.setContentView(R.layout.dialog_wait);
									dialog.getWindow()
											.setBackgroundDrawableResource(
													R.drawable.dialog_rounded_corner_light_black);
									dialog.show();
									dialog.setCancelable(true);
									dialog.setOnCancelListener(new OnCancelListener() {

										@Override
										public void onCancel(
												DialogInterface dialog) {
											// TODO Auto-generated method stub
											Log.i("", "Cancel");
											cancel(true);
										}
									});

								}

								@Override
								protected Void doInBackground(Void... params) {
									// TODO Auto-generated method stub
									if (controlCredential())
										if (login(
												((EditText) findViewById(R.id.text_username))
														.getText().toString(),
												((EditText) findViewById(R.id.text_password))
														.getText().toString())) {
											InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
											imm.hideSoftInputFromWindow(
													((EditText) findViewById(R.id.text_password))
															.getWindowToken(),
													0);

											loginSuccess.put("success", true);

										} else {
											loginSuccess.put("success", false);
											loginSuccess.put("notfound", true);
										}
									else {
										loginSuccess.put("success", false);
										loginSuccess.put("notfound", false);
									}
									return null;
								}

								@Override
								protected void onPostExecute(Void result) {
									// TODO Auto-generated method stub
									super.onPostExecute(result);

									dialog.dismiss();

									// START ONPOST

									controlAfterLogin();

									// END ONPOST
								}

							}.execute();
						}
						return false;
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		((EditText) findViewById(R.id.text_password)).setText("");
		((EditText) findViewById(R.id.text_username)).setText("");

	}

	private boolean controlCredential() {
		return (!((EditText) findViewById(R.id.text_password)).getText()
				.toString().equalsIgnoreCase("") && !((EditText) findViewById(R.id.text_username))
				.getText().toString().equalsIgnoreCase(""));
	}

	private boolean login(String username, String password) {
		
		//TODO federico login
		return (ManagerData.getAuthToken(username, password) != null);
	}

	private void controlAfterLogin() {
		if (loginSuccess.get("success")) {
			Intent mCaller = new Intent(getApplicationContext(), Home.class);
			mCaller.putExtra("sessionLogin", true);
			startActivity(mCaller);
		} else if (loginSuccess.get("notfound")) {
			AlertDialog.Builder builder = new AlertDialog.Builder(Intro.this);
			builder.setTitle("Attenzione");
			builder.setMessage("Username o password non corretti");
			builder.setCancelable(false);
			builder.setPositiveButton("Chiudi",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(Intro.this);
			builder.setTitle("Attenzione");
			builder.setMessage("Completa i campi Username e Password");
			builder.setCancelable(false);
			builder.setPositiveButton("Chiudi",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			builder.create().show();
		}
	}
}
