package smartcampus.android.template.standalone.Activity.ProfileBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.Activity.ProfileBlock.CalendarSubBlock.FilterCalendarioActivity;
import smartcampus.android.template.standalone.Activity.ProfileBlock.FAQSubBlock.FAQ;
import smartcampus.android.template.standalone.Activity.ProfileBlock.RisolutoreSubBlock.Problema;
import smartcampus.android.template.standalone.HomeBlock.Home;
import smartcampus.android.template.standalone.IntroBlock.Intro;
import smartcampus.android.template.standalone.R.drawable;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;

import eu.trentorise.smartcampus.profileservice.ProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import android.R.array;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.smartcampus.template.standalone.Utente;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class Profile extends Activity {

	private Utente user;
	private ArrayList<Utente> mListaSuperiori;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;

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
				user = ManagerData.readUserData();
				mListaSuperiori = ManagerData.getUserForCategoriaAndAmbito(user
						.getAmbito(), Integer.toString((Integer.parseInt(user
						.getRuolo()) - 1)));
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialog.dismiss();

				// START ONPOST

				((TextView) findViewById(R.id.text_nome_user)).setText(user
						.getNome() + " " + user.getCognome());
				((TextView) findViewById(R.id.text_role)).setText(user
						.getAmbito());

				if (mListaSuperiori.size() != 0) {
					((ListView) findViewById(R.id.lista_superiori))
							.setAdapter(new RowVolontario(
									getApplicationContext(), mListaSuperiori));
					((ListView) findViewById(R.id.lista_superiori))
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
}
