package smartcampus.android.template.standalone.Activity.FacilitiesBlock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.Activity.EventiBlock.InfoEventi;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.smartcampus.template.standalone.Evento;
import android.smartcampus.template.standalone.POI;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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
				mListaEventiForPOI = ManagerData.getEventiForPOI(new POI(null,
						null, null, getIntent().getDoubleExtra("latGPS", 0),
						getIntent().getDoubleExtra("lngGPS", 0)));
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
									mCaller.putExtra("data", mListaEventiForPOI
											.get(arg2).getData());
									mCaller.putExtra("index", arg2);
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
						.setText(new SimpleDateFormat("hh:mm", Locale
								.getDefault()).format(values.get(position)
								.getData()));

				if (values.get(position).getNome().length() <= 12)
					((FontTextView) rowView.findViewById(R.id.text_nome_evento))
							.setText(values.get(position).getNome()
									.toUpperCase());
				else
					((FontTextView) rowView.findViewById(R.id.text_nome_evento))
							.setText(values.get(position).getNome()
									.toUpperCase().substring(0, 11)
									+ "...");

				// ((FontTextView)rowView.findViewById(R.id.text_tipo_gara)).setText
				// (values.get(position).ge);
			}
			return rowView;
		}
	}
}
