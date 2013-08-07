package smartcampus.android.template.standalone.Activity.ProfileBlock;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.map.ObjectMapper;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.Activity.ProfileBlock.CalendarSubBlock.FilterCalendarioActivity;
import smartcampus.android.template.standalone.Activity.ProfileBlock.FAQSubBlock.FAQ;
import smartcampus.android.template.standalone.Activity.ProfileBlock.RisolutoreSubBlock.Problema;
import smartcampus.android.template.standalone.R.drawable;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import smartcampus.android.template.standalone.Utilities.Users;
import smartcampus.android.template.standalone.Utilities.Utente;

import eu.trentorise.smartcampus.ac.ACService;
import eu.trentorise.smartcampus.ac.AcServiceException;
import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.ac.model.Attribute;
import eu.trentorise.smartcampus.ac.model.UserData;
import eu.trentorise.smartcampus.profileservice.ProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;

public class Profile extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		final Activity mThis = this;

		((TextView) findViewById(R.id.text_nome_user)).setText(Users
				.returnMyName().getmNome());
		((TextView) findViewById(R.id.text_role)).setText(Users.returnMyName()
				.getmCategoria());

		((ListView) findViewById(R.id.lista_superiori))
				.setAdapter(new RowVolontario(getApplicationContext(), Users
						.returnUtenti()));
		((ListView) findViewById(R.id.lista_superiori))
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						final Utente mUtente = Users.returnUtenti()[arg2];
						
						AlertDialog.Builder builder = new AlertDialog.Builder(
								mThis);
						builder.setTitle("Contatto");
						builder.setMessage("Vuoi chiamare "+mUtente.getmNome()+" al numero "+mUtente.getmTelefono());
						builder.setCancelable(false);
						builder.setPositiveButton("Chiama",
								new android.content.DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
										Intent intent = new Intent(Intent.ACTION_CALL);
										intent.setData(Uri.parse("tel:"
												+ mUtente.getmTelefono()));
										startActivityForResult(intent, 0);
									}
								});
						builder.setNegativeButton("Chiudi",
								new android.content.DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										dialog.dismiss();
									}
								});
						builder.create().show();
					}
				});

		((ImageView) findViewById(R.id.button_calendario))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(getApplicationContext(),
								Problema.class));
					}
				});

		((ImageView) findViewById(R.id.button_risolutore))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(getApplicationContext(),
								FilterCalendarioActivity.class));
					}
				});

		((ImageView) findViewById(R.id.button_faq))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(getApplicationContext(),
								FAQ.class));
					}
				});
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
		private final Utente[] values;

		public RowVolontario(Context context, Utente[] values) {
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
					.setText(values[position].getmNome());
			((TextView) rowView.findViewById(R.id.text_categoria_volontario))
					.setText("Categoria: " + values[position].getmCategoria());
			((TextView) rowView.findViewById(R.id.text_ruolo_volontario))
					.setText("Ruolo: " + values[position].getmRuolo());

			return rowView;
		}
	}
}
