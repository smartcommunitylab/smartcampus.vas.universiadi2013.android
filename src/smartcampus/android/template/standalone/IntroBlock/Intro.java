package smartcampus.android.template.standalone.IntroBlock;

import java.util.HashMap;
import java.util.Map;

import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.HomeBlock.Home;
import smartcampus.android.template.universiadi.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.smartcampus.template.standalone.Utente;
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
	private static final int CONNECTION_ERROR = 0;
	private static final int LOGIN_SUCCESS = 1;
	private static final int LOGIN_FAILED = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_intro);

		ManagerData.getInstance(getApplicationContext());

		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setTitle("Credenziali");
		// builder.setMessage("Credenziali per l'accesso\nUsername: 'gzacco'\nPassword: 'gabriele'");
		// builder.setCancelable(false);
		// builder.setPositiveButton("Chiudi",
		// new android.content.DialogInterface.OnClickListener() {
		// public void onClick(DialogInterface dialog, int id) {
		// dialog.dismiss();
		// }
		// });
		// builder.create().show();

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
							private boolean connectionError;

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
								if (controlCredential()) {
									int loginResult = login(
											((EditText) findViewById(R.id.text_username))
													.getText().toString()
													.replace(" ", ""),
											((EditText) findViewById(R.id.text_password))
													.getText().toString());

									UserConstant
											.setUsername(((EditText) findViewById(R.id.text_username))
													.getText().toString()
													.replace(" ", ""));
									UserConstant
											.setPassword(((EditText) findViewById(R.id.text_password))
													.getText().toString());

									if (loginResult == Intro.CONNECTION_ERROR) {
										connectionError = true;
									} else if (loginResult == Intro.LOGIN_SUCCESS) {
										InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.hideSoftInputFromWindow(
												((EditText) findViewById(R.id.text_password))
														.getWindowToken(), 0);

										loginSuccess.put("success", true);
										// SAVE NEW UTENTE
										saveUtente();
										// SAVE USER&PASS
										saveUserAndPass();
									} else if (loginResult == Intro.LOGIN_FAILED) {
										loginSuccess.put("success", false);
										loginSuccess.put("notfound", true);

									}

								} else {
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

								if (connectionError) {
									Dialog noConnection = new Dialog(Intro.this);
									noConnection
											.requestWindowFeature(Window.FEATURE_NO_TITLE);
									noConnection
											.setContentView(R.layout.dialog_no_connection);
									noConnection
											.getWindow()
											.setBackgroundDrawableResource(
													R.drawable.dialog_rounded_corner_light_black);
									noConnection.show();
									noConnection.setCancelable(true);
								} else
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
								private boolean connectionError;

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
									if (controlCredential()) {
										int loginResult = login(
												((EditText) findViewById(R.id.text_username))
														.getText().toString()
														.replace(" ", ""),
												((EditText) findViewById(R.id.text_password))
														.getText().toString());

										if (loginResult == Intro.CONNECTION_ERROR) {
											connectionError = true;
										} else if (loginResult == Intro.LOGIN_SUCCESS) {
											InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
											imm.hideSoftInputFromWindow(
													((EditText) findViewById(R.id.text_password))
															.getWindowToken(),
													0);

											loginSuccess.put("success", true);
											// SAVE NEW UTENTE
											saveUtente();
											// SAVE USER&PASS
											saveUserAndPass();
										} else if (loginResult == Intro.LOGIN_FAILED) {
											loginSuccess.put("success", false);
											loginSuccess.put("notfound", true);

										}

									} else {
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

		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(Intro.this);
		final String username = preferences.getString("username", null);
		final String password = preferences.getString("password", null);

		if (username != null && password != null) {
			new AsyncTask<Void, Void, Void>() {
				private Dialog dialog;
				private boolean connectionError;

				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();

					dialog = new Dialog(Intro.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.dialog_wait);
					dialog.getWindow().setBackgroundDrawableResource(
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
					int loginResult = login(username, password);

					if (loginResult == Intro.CONNECTION_ERROR) {
						connectionError = true;
					} else if (loginResult == Intro.LOGIN_SUCCESS) {
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(
								((EditText) findViewById(R.id.text_password))
										.getWindowToken(), 0);

						loginSuccess.put("success", true);
						// SAVE NEW UTENTE
						saveUtente();
					} else if (loginResult == Intro.LOGIN_FAILED) {
						loginSuccess.put("success", false);
						loginSuccess.put("notfound", true);

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
	}

	private void saveUtente() {
		Utente user = (Utente) ManagerData.readUserData().get("params");
		UserConstant.setUser(user);
		Map<String, Object> mMapResult = ManagerData.saveUserInfo(user);
	}

	private void saveUserAndPass() {
		SharedPreferences preferences = PreferenceManager
				.getDefaultSharedPreferences(Intro.this);
		Editor editor = preferences.edit();
		editor.putString("username",
				((EditText) findViewById(R.id.text_username)).getText()
						.toString().replace(" ", ""));
		editor.putString("password",
				((EditText) findViewById(R.id.text_password)).getText()
						.toString());
		editor.commit();
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

	private int login(String username, String password) {

		// TODO federico login

		int mReturn = -1;
		Map<String, Object> mResult = ManagerData.getAuthToken(username,
				password);
		mReturn = ((Boolean) mResult.get("connectionError")) ? Intro.CONNECTION_ERROR
				: -1;
		if (mReturn == -1)
			mReturn = (((String) mResult.get("params")) == null) ? Intro.LOGIN_FAILED
					: Intro.LOGIN_SUCCESS;
		return mReturn;
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
