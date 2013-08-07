package smartcampus.android.template.standalone.Activity.EventiBlock;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.Activity.Model.DBManager;
import smartcampus.android.template.standalone.R.drawable;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Evento extends Activity {

	private ListView mListaEventi;
	private ListArrayAdapter mAdapter;
	private LinearLayout mContainer;

	private ArrayList<android.smartcampus.template.standalone.Evento> mLista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evento);

		SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy",
				Locale.getDefault());
		String dateString = dateFormatter.format(Calendar.getInstance()
				.getTime());
		mLista = (DBManager.getInstance(getApplicationContext()))
				.getEventiPerData(dateString);

		mListaEventi = (ListView) findViewById(R.id.list_eventi);
		mAdapter = new ListArrayAdapter(getApplicationContext(), mLista);
		mListaEventi.setAdapter(mAdapter);

		mContainer = (LinearLayout) findViewById(R.id.container_scroll_date);
		for (int i = 0; i < 12; i++) {
			Calendar mCal = Calendar.getInstance();
			mCal.setTimeInMillis(Calendar.getInstance().getTimeInMillis()
					+ 86400000 * i);

			RelativeLayout mCell = new RelativeLayout(this);
			mCell.setTag(mCal.getTimeInMillis());
			mCell.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT));

			final ImageView mImgCella = new ImageView(this);
			mImgCella.setImageResource(R.drawable.cell_event);
			mImgCella.setTag(i);
			mImgCella.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			mCell.addView(mImgCella);

			FontTextView mData = new FontTextView(this);
			mData.setText(Integer.toString(mCal.get(Calendar.DATE)));
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.setMargins(0, (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 7, getResources()
							.getDisplayMetrics()), 0, 0);
			mData.setLayoutParams(params);
			mData.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 45);
			mData.setTextColor(Color.argb(255, 50, 147, 172));

			mCell.addView(mData);

			FontTextView mGiorno = new FontTextView(this);
			SimpleDateFormat format = new SimpleDateFormat("MMMM");
			String formattedDate = format.format(mCal.getTime());
			mGiorno.setText(formattedDate.substring(0, 1).toUpperCase()
					+ formattedDate.substring(1));
			params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
			params.addRule(RelativeLayout.CENTER_HORIZONTAL);
			params.setMargins(0, (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
							.getDisplayMetrics()), 0, 0);
			mGiorno.setLayoutParams(params);
			mGiorno.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
			mGiorno.setTextColor(Color.argb(255, 50, 147, 172));

			mCell.addView(mGiorno);

			mContainer.addView(mCell);

			mCell.setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent arg1) {
					// TODO Auto-generated method stub
					if (arg1.getAction() == MotionEvent.ACTION_UP) {

						SimpleDateFormat dateFormatter = new SimpleDateFormat(
								"dd.MM.yyyy", Locale.getDefault());
						Calendar mCal = Calendar.getInstance();
						mCal.setTimeInMillis((Long) v.getTag());
						String dateString = dateFormatter.format(mCal.getTime());
						mAdapter.values = (DBManager
								.getInstance(getApplicationContext()))
								.getEventiPerData(dateString);
						mAdapter.notifyDataSetChanged();

						return true;
					}
					return true;
				}
			});
		}

		mListaEventi.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent mCaller = new Intent(arg1.getContext(),
						DettaglioEvento.class);
				android.smartcampus.template.standalone.Evento a = (android.smartcampus.template.standalone.Evento) (mListaEventi
						.getAdapter().getItem(arg2));
				mCaller.putExtra("Nome", a.getNome());
				mCaller.putExtra("GPS", a.getLatGPS() +","+ a.getLngGPS());
				mCaller.putExtra("Desc", a.getDescrizione());
				startActivity(mCaller);
			}
		});
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
					rowView = inflater.inflate(R.layout.row_eventi, parent,
							false);
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
						.setText(values.get(position).getOra());

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

	// private class DownloadEventiForGiorni extends AsyncTask<Void, Void,
	// Map<String, List<?>>>
	// {
	// private ListView mList;
	// private Date mData;
	//
	// public DownloadEventiForGiorni(ListView mPrimaLista, Date data)
	// {
	// mList = mPrimaLista;
	// mData = data;
	//
	// }
	//
	// @Override
	// protected Map<String, List<?>> doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// DiscoverTrentoConnector service = new DiscoverTrentoConnector
	// ("https://vas-dev.smartcampuslab.it");
	// Thread.currentThread().setContextClassLoader(EventObject.class.getClassLoader());
	// ObjectFilter filter = new ObjectFilter();
	// filter.setClassName(EventObject.class.getCanonicalName());
	// filter.setFromTime(mData.getTime());
	// Calendar mCal = Calendar.getInstance();
	// mCal.setTime(mData);
	// Log.i("Data", mCal.get(Calendar.DATE)+"/"+mCal.get(Calendar.MONTH)+1+
	// "/"+mCal.get(Calendar.YEAR));
	// filter.setToTime(mCal.getTimeInMillis()+86400000);
	// try {
	// return service.getObjects(filter, Intro.mToken);
	// } catch (DiscoverTrentoConnectorException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Map<String, List<?>> result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	//
	// Collection<List<?>> a = result.values();
	// Iterator<List<?>> it = a.iterator();
	// ArrayList<EventObject> mResult = new ArrayList<EventObject>();
	//
	// while(it.hasNext())
	// {
	// List<?> tmp = it.next();
	// for (int i=0; i<tmp.size(); i++)
	// {
	// mResult.add((EventObject)tmp.get(i));
	// }
	// }
	// mList.setAdapter(new ListArrayAdapter(getApplicationContext(), mResult));
	// }
	//
	// }

}
