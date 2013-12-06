package smartcampus.android.template.standalone.Activity.ProfileBlock;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.Activity.ProfileBlock.CalendarSubBlock.FunzioneObj;
import smartcampus.android.template.standalone.IntroBlock.UserConstant;
import eu.trentorise.smartcampus.universiade.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.smartcampus.template.standalone.Utente;
import android.smartcampus.template.standalone.UtenteSuperiore;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class Profile extends Activity {

	private Utente user;
	private ArrayList<FunzioneObj> funzione;
	private ArrayList<UtenteSuperiore> mListaSuperiori = new ArrayList<UtenteSuperiore>();

	private ListView listSuperiori;
	private RowVolontario mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profilo);

		listSuperiori = ((ListView) findViewById(R.id.lista_superiori));

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;
			private Map<String, Object> mMapUserData;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				Log.i("", "First dialog");
				dialog = new Dialog(Profile.this);
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
						cancel(true);
						finish();
					}
				});

			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				user = UserConstant.getUser();
				Log.i("", user.getId());
				mMapUserData = ManagerData.getFunzioneForUser(user);
				if (!((Boolean) mMapUserData.get("connectionError")))
					funzione = (ArrayList<FunzioneObj>) mMapUserData
							.get("params");
				//
				// ArrayList<String> funzioneString = new ArrayList<String>();
				// for (int i = 0; i < funzione.size(); i++)
				// funzioneString.add(funzione.get(i).getFunzione());
				//
				// ArrayList<UtenteSuperiore> listaTmp =
				// (ArrayList<UtenteSuperiore>) (ManagerData
				// .getSuperioriForUser(user, funzioneString.get(0))
				// .get("params"));
				// for (int i = 0; i < listaTmp.size(); i++)
				// if (!mListaSuperiori.contains(listaTmp.get(i))
				// && !listaTmp
				// .get(i)
				// .getCognome()
				// .equalsIgnoreCase(
				// UserConstant.getUser()
				// .getCognome())
				// && !listaTmp
				// .get(i)
				// .getNome()
				// .equalsIgnoreCase(
				// UserConstant.getUser()
				// .getNome()))
				// mListaSuperiori.add(listaTmp.get(i));
				// }
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				// START ONPOST

				if ((Boolean) mMapUserData.get("connectionError")) {
					Dialog noConnection = new Dialog(Profile.this);
					noConnection.requestWindowFeature(Window.FEATURE_NO_TITLE);
					noConnection.setContentView(R.layout.dialog_no_connection);
					noConnection.getWindow().setBackgroundDrawableResource(
							R.drawable.dialog_rounded_corner_light_black);
					noConnection.setCancelable(true);
					noConnection.show();
					noConnection.setOnCancelListener(new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							// TODO Auto-generated method stub
							finish();
						}
					});

				} else {

					((TextView) findViewById(R.id.text_nome_user))
							.setText(Profile.this
									.getString(R.string.PROFILO_SALUTO)
									+ user.getNome() + " " + user.getCognome());

					((Spinner) findViewById(R.id.spinner_multiple_funzioni))
							.setAdapter(new SpinnerAdapter(Profile.this,
									funzione));
					((Spinner) findViewById(R.id.spinner_multiple_funzioni))
							.setSelection(0);
					((Spinner) findViewById(R.id.spinner_multiple_funzioni))
							.setOnItemSelectedListener(new OnItemSelectedListener() {

								@Override
								public void onItemSelected(AdapterView<?> arg0,
										View arg1, final int arg2, long arg3) {
									// TODO Auto-generated method stub

									new AsyncTask<Void, Void, Void>() {
										private Dialog dialog;
										private Map<String, Object> mMapListaSuperiori;

										@Override
										protected void onPreExecute() {
											// TODO Auto-generated method
											// stub
											super.onPreExecute();

											Log.i("", "Second dialog");
											dialog = new Dialog(Profile.this);
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
													// TODO Auto-generated
													// method stub
													cancel(true);
													finish();
												}
											});

										}

										@Override
										protected Void doInBackground(
												Void... params) {
											// TODO Auto-generated method
											// stub
											user = UserConstant.getUser();
											Log.i("", user.getId());
											mMapUserData = ManagerData
													.getFunzioneForUser(user);
											if (!((Boolean) mMapUserData
													.get("connectionError"))) {
												funzione = (ArrayList<FunzioneObj>) mMapUserData
														.get("params");

												ArrayList<String> funzioneString = new ArrayList<String>();
												for (int i = 0; i < funzione
														.size(); i++)
													funzioneString.add(funzione
															.get(i)
															.getFunzione());

												ArrayList<UtenteSuperiore> listaTmp = (ArrayList<UtenteSuperiore>) (ManagerData
														.getSuperioriForUser(
																user,
																funzioneString
																		.get(arg2))
														.get("params"));
												for (int i = 0; i < listaTmp
														.size(); i++)
													if (!mListaSuperiori
															.contains(listaTmp
																	.get(i))
															&& !listaTmp
																	.get(i)
																	.getCognome()
																	.equalsIgnoreCase(
																			UserConstant
																					.getUser()
																					.getCognome())
															&& !listaTmp
																	.get(i)
																	.getNome()
																	.equalsIgnoreCase(
																			UserConstant
																					.getUser()
																					.getNome()))
														mListaSuperiori.add(listaTmp
																.get(i));
											}
											return null;
										}

										@Override
										protected void onPostExecute(Void result) {
											// TODO Auto-generated method
											// stub
											super.onPostExecute(result);

											// START ONPOST

											if ((Boolean) mMapUserData
													.get("connectionError")) {
												Dialog noConnection = new Dialog(
														Profile.this);
												noConnection
														.requestWindowFeature(Window.FEATURE_NO_TITLE);
												noConnection
														.setContentView(R.layout.dialog_no_connection);
												noConnection
														.getWindow()
														.setBackgroundDrawableResource(
																R.drawable.dialog_rounded_corner_light_black);
												noConnection
														.setCancelable(true);
												noConnection.show();
												noConnection
														.setOnCancelListener(new OnCancelListener() {

															@Override
															public void onCancel(
																	DialogInterface dialog) {
																// TODO
																// Auto-generated
																// method
																// stub
																finish();
															}
														});

											} else {
												if (mListaSuperiori.size() != 0) {
													mAdapter = new RowVolontario(
															getApplicationContext(),
															mListaSuperiori);
													listSuperiori
															.setAdapter(mAdapter);
													listSuperiori
															.setOnItemClickListener(new OnItemClickListener() {

																@Override
																public void onItemClick(
																		AdapterView<?> arg0,
																		View arg1,
																		int arg2,
																		long arg3) {
																	// TODO
																	// Auto-generated
																	// method
																	// stub
																	final UtenteSuperiore mUtente = mListaSuperiori
																			.get(arg2);

																	if (mUtente
																			.getNumeroTelefonico()
																			.equalsIgnoreCase(
																					"null")) {
																		AlertDialog.Builder builder = new AlertDialog.Builder(
																				Profile.this);
																		builder.setTitle(getString(R.string.CONTATTO));
																		builder.setMessage(getString(R.string.NESSUN_NUMERO));
																		builder.setCancelable(false);
																		builder.setNeutralButton(
																				getString(R.string.CHIUDI),
																				new android.content.DialogInterface.OnClickListener() {
																					public void onClick(
																							DialogInterface dialog,
																							int id) {
																						dialog.dismiss();
																					}
																				});
																		builder.create()
																				.show();
																	} else {
																		AlertDialog.Builder builder = new AlertDialog.Builder(
																				Profile.this);
																		builder.setTitle(getString(R.string.CONTATTO));
																		builder.setMessage(getString(R.string.PROFILO_PHONE1)
																				+ " "
																				+ mUtente
																						.getNome()
																				+ getString(R.string.PROFILO_PHONE2)
																				+ " "
																				+ mUtente
																						.getNumeroTelefonico());
																		builder.setCancelable(false);
																		builder.setPositiveButton(
																				getString(R.string.CHIAMA),
																				new android.content.DialogInterface.OnClickListener() {
																					public void onClick(
																							DialogInterface dialog,
																							int id) {
																						dialog.dismiss();
																						Intent intent = new Intent(
																								Intent.ACTION_CALL);
																						intent.setData(Uri
																								.parse("tel:"
																										+ mUtente
																												.getNumeroTelefonico()));
																						startActivityForResult(
																								intent,
																								0);
																					}
																				});
																		builder.setNegativeButton(
																				getString(R.string.CHIUDI),
																				new android.content.DialogInterface.OnClickListener() {
																					public void onClick(
																							DialogInterface dialog,
																							int id) {
																						dialog.dismiss();
																					}
																				});
																		builder.create()
																				.show();
																	}
																}
															});
												} else
													((TextView) findViewById(R.id.text_nessun_superiore))
															.setVisibility(View.VISIBLE);
											}

											dialog.dismiss();
										}
									}.execute();
								}

								@Override
								public void onNothingSelected(
										AdapterView<?> arg0) {
									// TODO Auto-generated method stub

								}
							});

					if (funzione.size() == 1)
						((Spinner) findViewById(R.id.spinner_multiple_funzioni))
								.setClickable(false);
					else {
						((RelativeLayout) findViewById(R.id.btn_open_spinner_funzioni))
								.setOnTouchListener(new OnTouchListener() {

									@Override
									public boolean onTouch(View v,
											MotionEvent event) {
										// TODO Auto-generated method stub
										if (event.getAction() == MotionEvent.ACTION_DOWN) {
											((ImageView) findViewById(R.id.image_spinner_funzioni))
													.setImageResource(R.drawable.btn_helper_category_press);
											return true;
										}
										if (event.getAction() == MotionEvent.ACTION_UP) {
											((ImageView) findViewById(R.id.image_spinner_funzioni))
													.setImageResource(R.drawable.btn_helper_category);

											((Spinner) findViewById(R.id.spinner_multiple_funzioni))
													.performClick();

											return true;
										}
										return false;
									}

								});
					}
				}

				// END ONPOST

				dialog.dismiss();
			}

		}.execute();

		/*
		 * ((FontTextView)findViewById(R.id.text_nome_user)).setText("Mario Rossi"
		 * );
		 * 
		 * final ImageView mBadge = (ImageView)findViewById(R.id.image_badge);
		 * mBadge.setOnTouchListener(new OnTouchListener(){
		 * 
		 * @Override public boolean onTouch(View arg0, MotionEvent arg1) { //
		 * TODO Auto-generated method stub if(arg1.getAction() ==
		 * MotionEvent.ACTION_DOWN) {
		 * mBadge.setImageResource(R.drawable.button_badge_down); return true; }
		 * if(arg1.getAction() == MotionEvent.ACTION_UP) {
		 * mBadge.setImageResource(R.drawable.button_badge_up); //
		 * startActivity(new Intent(arg0.getContext(), Batch.class)); return
		 * true; } return false; } });
		 * 
		 * final ImageView mPush = (ImageView)findViewById(R.id.image_push);
		 * mPush.setOnTouchListener(new OnTouchListener(){
		 * 
		 * @Override public boolean onTouch(View arg0, MotionEvent arg1) { //
		 * TODO Auto-generated method stub if(arg1.getAction() ==
		 * MotionEvent.ACTION_DOWN) {
		 * mPush.setImageResource(R.drawable.button_push_down); return true; }
		 * if(arg1.getAction() == MotionEvent.ACTION_UP) { return true; } return
		 * false; } });
		 * 
		 * if
		 * (((TextView)findViewById(R.id.text_role)).getText().equals("Volunteer"
		 * )) { // mBadge.setVisibility(View.GONE);
		 * ((ImageView)findViewById(R.id
		 * .image_profile)).setImageResource(R.drawable.profile_volunteer); }
		 * else
		 * ((ImageView)findViewById(R.id.image_profile)).setImageResource(R.
		 * drawable.profile_boss);
		 */
	}

	private class RowVolontario extends ArrayAdapter<UtenteSuperiore> {
		private final Context context;
		private final ArrayList<UtenteSuperiore> values;

		public RowVolontario(Context context, ArrayList<UtenteSuperiore> values) {
			super(context, R.layout.row_volontari, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.row_volontari, parent,
					false);

			((TextView) rowView.findViewById(R.id.text_anagrafiche_volontario))
					.setText(values.get(position).getNome() + " "
							+ values.get(position).getCognome());
			((TextView) rowView.findViewById(R.id.text_categoria_volontario))
					.setText(getString(R.string.PROFILO_CATEGORIA) + " "
							+ values.get(position).getRuolo());
			((TextView) rowView.findViewById(R.id.text_ruolo_volontario))
					.setText(getString(R.string.PROFILO_RUOLO) + " "
							+ values.get(position).getAmbito());

			return rowView;
		}
	}

	private class SpinnerAdapter extends ArrayAdapter<FunzioneObj> {

		private Context context;
		private ArrayList<FunzioneObj> values;

		public SpinnerAdapter(Context context, ArrayList<FunzioneObj> values) {
			super(context, android.R.layout.simple_list_item_1, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return getCustomView(position, convertView, parent,
					Color.parseColor("#3294ad"), false);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			return getCustomView(position, convertView, parent, Color.WHITE,
					true);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent, int color, boolean dropDown) {
			// TODO Auto-generated method stub
			// return super.getView(position, convertView, parent);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(
					android.R.layout.simple_list_item_1, parent, false);

			String[] funzioneTokenized = values.get(position).getFunzione()
					.split(":");
			((TextView) rowView.findViewById(android.R.id.text1))
					.setText(funzioneTokenized[funzioneTokenized.length - 1]);
			((TextView) rowView.findViewById(android.R.id.text1))
					.setTextColor(color);
			((TextView) rowView.findViewById(android.R.id.text1))
					.setTypeface(Typeface.createFromAsset(
							getApplicationContext().getAssets(),
							"PatuaOne-Regular.otf"));
			if (values.get(position).getFunzione().length() > 13 && dropDown)
				((TextView) rowView.findViewById(android.R.id.text1))
						.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

			return rowView;
		}
	}
}
