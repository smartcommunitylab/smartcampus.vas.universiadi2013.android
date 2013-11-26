package smartcampus.android.template.standalone.Activity.FacilitiesBlock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import smartcampus.android.template.standalone.Activity.EventiBlock.InfoEventi;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import smartcampus.android.template.universiadi.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.smartcampus.template.standalone.Evento;
import android.smartcampus.template.standalone.POI;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class DettaglioEventoPerPOI extends Activity {

	private ArrayList<Evento> mListaEventiForPOI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dettaglio_evento_per_poi);

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(DettaglioEventoPerPOI.this);
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
				Map<String, Object> mResult = ManagerData
						.getEventiForPOI(new POI(null, null, null, null, getIntent()
								.getDoubleExtra("latGPS", 0), getIntent()
								.getDoubleExtra("lngGPS", 0)));
				if ((Boolean) mResult.get("connectionError")) {
					Dialog noConnection = new Dialog(DettaglioEventoPerPOI.this);
					noConnection.requestWindowFeature(Window.FEATURE_NO_TITLE);
					noConnection.setContentView(R.layout.dialog_no_connection);
					noConnection.getWindow().setBackgroundDrawableResource(
							R.drawable.dialog_rounded_corner_light_black);
					noConnection.show();
					noConnection.setCancelable(true);
					noConnection.setOnCancelListener(new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							// TODO Auto-generated method stub
							finish();
						}
					});
				} else
					mListaEventiForPOI = (ArrayList<Evento>) mResult
							.get("params");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialog.dismiss();

				// START ONPOST

				if (mListaEventiForPOI.size() != 0) {
					((ListView) findViewById(R.id.list_eventi_per_poi))
							.setAdapter(new ListArrayAdapter(
									getApplicationContext(), mListaEventiForPOI));
					((ListView) findViewById(R.id.list_eventi_per_poi))
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									Intent mCaller = new Intent(arg1
											.getContext(), InfoEventi.class);
									mCaller.putExtra("evento",
											mListaEventiForPOI.get(arg2));
									startActivity(mCaller);
								}
							});
				} else
					((TextView) findViewById(R.id.text_nessun_evento_per_poi))
							.setVisibility(View.VISIBLE);

				// END ONPOST
			}

		}.execute();
	}

	private class ListArrayAdapter extends
			ArrayAdapter<android.smartcampus.template.standalone.Evento> {

		private Context context;
		private ArrayList<android.smartcampus.template.standalone.Evento> values;

		public ListArrayAdapter(Context context,
				ArrayList<android.smartcampus.template.standalone.Evento> values) {
			super(context, R.layout.row_eventi, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = new View(context);

			if (values.size() != 0) {
				rowView = inflater.inflate(R.layout.row_eventi, parent, false);
				// Calendar mCal = Calendar.getInstance();
				// mCal.setTimeInMillis(values.get(position).getFromTime());
				//
				// String ora = null;
				//
				// if (mCal.get(Calendar.MINUTE) < 10)
				// ora = mCal.get(Calendar.HOUR_OF_DAY) + ":0"
				// + mCal.get(Calendar.MINUTE);
				// else
				// ora = mCal.get(Calendar.HOUR_OF_DAY) + ":"
				// + mCal.get(Calendar.MINUTE);
				// if (mCal.get(Calendar.AM_PM) == Calendar.AM)
				// ora = ora + " AM";
				// else
				// ora = ora + " PM";

				((FontTextView) rowView.findViewById(R.id.text_orario))
						.setText(new SimpleDateFormat("HH:mm", Locale
								.getDefault()).format(values.get(position)
								.getOraInizio())
								+ " - "
								+ new SimpleDateFormat("HH:mm", Locale
										.getDefault()).format(values.get(
										position).getOraFine()));

				((FontTextView) rowView.findViewById(R.id.text_nome_evento))
						.setText(values.get(position).getNome().toUpperCase());

				if (values.get(position).getNome().length() > 30)
					((FontTextView) rowView.findViewById(R.id.text_nome_evento))
							.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				else if (values.get(position).getNome().length() > 15)
					((FontTextView) rowView.findViewById(R.id.text_nome_evento))
							.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

				((FontTextView) rowView.findViewById(R.id.text_tipo_gara))
						.setText(values.get(position).getTipoSport());

				((ImageView) rowView.findViewById(R.id.image_logo_row_eventi))
						.setImageBitmap((values.get(position).getImage() == null) ? BitmapFactory
								.decodeResource(getResources(),
										R.drawable.img_event_list)
								: BitmapFactory.decodeByteArray(
										values.get(position).getImage(), 0,
										values.get(position).getImage().length));

				// ((FontTextView)rowView.findViewById(R.id.text_tipo_gara)).setText
				// (values.get(position).ge);
			}
			return rowView;
		}
	}
}
