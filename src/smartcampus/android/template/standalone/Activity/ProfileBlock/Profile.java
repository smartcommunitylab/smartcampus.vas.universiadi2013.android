package smartcampus.android.template.standalone.Activity.ProfileBlock;

import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import smartcampus.android.template.universiadi.R;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.HomeBlock.Home;
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
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings.TextSize;
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
	private ArrayList<String> funzione;
	private ArrayList<Utente> mListaSuperiori = new ArrayList<Utente>();

	private ListView listSuperiori;
	private RowVolontario mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		listSuperiori = ((ListView) findViewById(R.id.lista_superiori));

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;
			private Map<String, Object> mMapUserData;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

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
				mMapUserData = ManagerData.readUserData();
				if (!((Boolean) mMapUserData.get("connectionError"))) {
					user = (Utente) mMapUserData.get("params");
					funzione = (ArrayList<String>) ManagerData
							.getFunzioneForUser(user).get("params");
				}
				// mListaSuperiori = (ArrayList<Utente>) (ManagerData
				// .getSuperioriForUser(user, funzione.get(0))
				// .get("params"));
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialog.dismiss();

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

					((TextView) findViewById(R.id.text_nome_user)).setText(user
							.getNome() + " " + user.getCognome());

					((Spinner) findViewById(R.id.spinner_multiple_funzioni))
							.setAdapter(new SpinnerAdapter(Profile.this,
									funzione));
					if (funzione.size() == 1) {
						// String[] funzioneTokenized =
						// funzione.get(0).split(":");
						// ((TextView) findViewById(R.id.text_role))
						// .setText(funzioneTokenized[funzioneTokenized.length -
						// 1]);
						// ((TextView)
						// findViewById(R.id.text_role)).setTextSize(
						// 12, TypedValue.COMPLEX_UNIT_SP);
						// ((Spinner)
						// findViewById(R.id.spinner_multiple_funzioni))
						// .setVisibility(View.GONE);
						((Spinner) findViewById(R.id.spinner_multiple_funzioni))
								.setClickable(false);
					} else {
						((Spinner) findViewById(R.id.spinner_multiple_funzioni))
								.setOnItemSelectedListener(new OnItemSelectedListener() {

									@Override
									public void onItemSelected(
											AdapterView<?> arg0, View arg1,
											final int arg2, long arg3) {
										// TODO Auto-generated method stub

										new AsyncTask<Void, Void, Void>() {
											private Dialog dialog;
											private Map<String, Object> mMapListaSuperiori;

											@Override
											protected void onPreExecute() {
												// TODO Auto-generated method
												// stub
												super.onPreExecute();

												dialog = new Dialog(
														Profile.this);
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
												mMapListaSuperiori = ManagerData.getSuperioriForUser(
														user,
														funzione.get(arg2));
												if (!((Boolean) mMapListaSuperiori
														.get("connectionError"))) {
													mListaSuperiori = (ArrayList<Utente>) mMapListaSuperiori
															.get("params");
												}
												return null;
											}

											@Override
											protected void onPostExecute(
													Void result) {
												// TODO Auto-generated method
												// stub
												super.onPostExecute(result);

												dialog.dismiss();

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
													mAdapter = new RowVolontario(
															getApplicationContext(),
															mListaSuperiori);
													listSuperiori
															.setAdapter(mAdapter);
													mAdapter.notifyDataSetChanged();
												}
											}
										}.execute();
									}

									@Override
									public void onNothingSelected(
											AdapterView<?> arg0) {
										// TODO Auto-generated method stub

									}
								});
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

				if (mListaSuperiori.size() != 0) {
					mAdapter = new RowVolontario(getApplicationContext(),
							mListaSuperiori);
					listSuperiori.setAdapter(mAdapter);
					listSuperiori
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									final Utente mUtente = mListaSuperiori
											.get(arg2);

									AlertDialog.Builder builder = new AlertDialog.Builder(
											Profile.this);
									builder.setTitle("Contatto");
									builder.setMessage("Vuoi chiamare "
											+ mUtente.getNome() + " al numero "
											+ mUtente.getNumeroTelefonico());
									builder.setCancelable(false);
									builder.setPositiveButton(
											"Chiama",
											new android.content.DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.dismiss();
													Intent intent = new Intent(
															Intent.ACTION_CALL);
													intent.setData(Uri.parse("tel:"
															+ mUtente
																	.getNumeroTelefonico()));
													startActivityForResult(
															intent, 0);
												}
											});
									builder.setNegativeButton(
											"Chiudi",
											new android.content.DialogInterface.OnClickListener() {
												public void onClick(
														DialogInterface dialog,
														int id) {
													dialog.dismiss();
												}
											});
									builder.create().show();
								}
							});
				} else
					((TextView) findViewById(R.id.text_nessun_superiore))
							.setVisibility(View.VISIBLE);

				// END ONPOST
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

	private class RowVolontario extends ArrayAdapter<Utente> {
		private final Context context;
		private final ArrayList<Utente> values;

		public RowVolontario(Context context, ArrayList<Utente> values) {
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
					.setText(values.get(position).getNome());
			((TextView) rowView.findViewById(R.id.text_categoria_volontario))
					.setText("Categoria: " + values.get(position).getAmbito());
			((TextView) rowView.findViewById(R.id.text_ruolo_volontario))
					.setText("Ruolo: " + values.get(position).getRuolo());

			return rowView;
		}
	}

	private class SpinnerAdapter extends ArrayAdapter<String> {

		private Context context;
		private ArrayList<String> values;

		public SpinnerAdapter(Context context, ArrayList<String> values) {
			super(context, R.layout.row_spinner_funzioni, values);
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
					Color.parseColor("#3294ad"));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			return getCustomView(position, convertView, parent, Color.WHITE);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent, int color) {
			// TODO Auto-generated method stub
			// return super.getView(position, convertView, parent);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.row_spinner_funzioni,
					parent, false);

			String[] funzioneTokenized = values.get(position).split(":");
			((TextView) rowView.findViewById(R.id.text_spinner_funzione))
					.setText(funzioneTokenized[funzioneTokenized.length - 1]);
			if (values.get(position).length() >= 13)
				((TextView) rowView.findViewById(R.id.text_spinner_funzione))
						.setTextSize(12, TypedValue.COMPLEX_UNIT_SP);

			return rowView;
		}
	}
}
