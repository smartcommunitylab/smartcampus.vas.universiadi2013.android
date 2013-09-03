package smartcampus.android.template.standalone.Activity.SportBlock;

import java.io.IOException;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.Activity.EventiBlock.InfoEventi;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.HomeBlock.Home;
import smartcampus.android.template.standalone.IntroBlock.Intro;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;

//import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.smartcampus.template.standalone.Atleta;
import android.smartcampus.template.standalone.Sport;
import android.smartcampus.template.standalone.Evento;

@SuppressLint("ValidFragment")
public class PageInfoSport extends Fragment {

	private String mDescrizione;
	private ArrayList<Evento> mListaEventiSportivi;
	private ArrayList<Atleta> mListaAtleti;
	private int mPosition;

	public PageInfoSport(String mDescrizione,
			ArrayList<Evento> mListaEventiSportivi,
			ArrayList<Atleta> mListaAtleti, int mPosition) {
		super();
		this.mDescrizione = mDescrizione;
		this.mListaEventiSportivi = mListaEventiSportivi;
		this.mListaAtleti = mListaAtleti;
		this.mPosition = mPosition;
	}

	public PageInfoSport() {
		this(null, null, null, -1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View mView = inflater.inflate(R.layout.activity_page_info_sport,
				null);

		RelativeLayout mContainerDesc = (RelativeLayout) mView
				.findViewById(R.id.container_descrizione);
		RelativeLayout mContainerEventi = (RelativeLayout) mView
				.findViewById(R.id.container_eventi_correlati);
		RelativeLayout mContainerAtleti = (RelativeLayout) mView
				.findViewById(R.id.container_atleti);

		switch (mPosition) {
		case 0:
			mContainerDesc.setVisibility(View.VISIBLE);
			mContainerEventi.setVisibility(View.GONE);
			mContainerAtleti.setVisibility(View.GONE);

			((FontTextView) mView.findViewById(R.id.text_descrizione))
					.setText(mDescrizione);
			break;
		case 1:
			mContainerDesc.setVisibility(View.GONE);
			mContainerEventi.setVisibility(View.VISIBLE);
			mContainerAtleti.setVisibility(View.GONE);

			if (mListaEventiSportivi.size() != 0) {
				((TextView) mView
						.findViewById(R.id.text_nessun_evento_per_sport))
						.setVisibility(View.GONE);
				ListView mListaEventi = (ListView) mView
						.findViewById(R.id.list_eventi_correlati);
				mListaEventi.setAdapter(new ListArrayAdapter(getActivity(),
						mListaEventiSportivi));
				mListaEventi.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Intent mCaller = new Intent(arg1.getContext(),
								InfoEventi.class);
						mCaller.putExtra("data", mListaEventiSportivi.get(arg2)
								.getData());
						mCaller.putExtra("index", arg2);
						startActivity(mCaller);
					}
				});
			} else
				((TextView) mView
						.findViewById(R.id.text_nessun_evento_per_sport))
						.setVisibility(View.VISIBLE);

			break;
		case 2:
			mContainerDesc.setVisibility(View.GONE);
			mContainerEventi.setVisibility(View.GONE);
			mContainerAtleti.setVisibility(View.VISIBLE);

			if (mListaAtleti.size() != 0) {
				((TextView) mView
						.findViewById(R.id.text_nessun_atleta_per_sport))
						.setVisibility(View.GONE);
				ListView mGrigliaAtleti = (ListView) mView
						.findViewById(R.id.list_atleti);
				mGrigliaAtleti.setAdapter(new ListSportArrayAdapter(mView
						.getContext(), mListaAtleti));
			} else
				((TextView) mView
						.findViewById(R.id.text_nessun_atleta_per_sport))
						.setVisibility(View.VISIBLE);
			break;
		}

		return mView;
	}

	private class ListArrayAdapter extends ArrayAdapter<Evento> {
		private final Context context;
		private final ArrayList<Evento> values;

		public ListArrayAdapter(Context context, ArrayList<Evento> values) {
			super(context, R.layout.grid_atleti, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = new View(context);

			if (values.size() != 0) {
				rowView = inflater.inflate(R.layout.row_eventi, parent, false);

				((FontTextView) rowView.findViewById(R.id.text_orario))
						.setText(new SimpleDateFormat("hh:mm").format(values
								.get(position).getData()));

				if (values.get(position).getNome().length() <= 12)
					((FontTextView) rowView.findViewById(R.id.text_nome_evento))
							.setText(values.get(position).getNome()
									.toUpperCase());
				else
					((FontTextView) rowView.findViewById(R.id.text_nome_evento))
							.setText(values.get(position).getNome()
									.toUpperCase().substring(0, 11)
									+ "...");
			}

			return rowView;
		}
	}

	private class ListSportArrayAdapter extends ArrayAdapter<Atleta> {
		private final Context context;
		private final ArrayList<Atleta> values;

		public ListSportArrayAdapter(Context context, ArrayList<Atleta> values) {
			super(context, R.layout.grid_atleti, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater
					.inflate(R.layout.grid_atleti, parent, false);

			((FontTextView) rowView.findViewById(R.id.text_nome_cognome))
					.setText(values.get(position).getCognome() + " "
							+ values.get(position).getNome());

			((FontTextView) rowView.findViewById(R.id.text_nazionalita))
					.setText(values.get(position).getNazionalita());

			return rowView;
		}
	}

	// private class DownloadEventiCorrelatiSport extends AsyncTask<Void, Void,
	// Map<String, List<?>>>
	// {
	// private Context mContext;
	// private ListView mList;
	// private String mNomeSport;
	//
	// public DownloadEventiCorrelatiSport(Context context, ListView
	// mPrimaLista)
	// {
	// mContext = context;
	// mList = mPrimaLista;
	//
	// }
	//
	// @Override
	// protected Map<String, List<?>> doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// // DiscoverTrentoConnector service = new DiscoverTrentoConnector
	// // ("https://vas-dev.smartcampuslab.it");
	// //
	// Thread.currentThread().setContextClassLoader(EventObject.class.getClassLoader());
	// // ObjectFilter filter = new ObjectFilter();
	// // filter.setClassName(EventObject.class.getCanonicalName());
	// //
	// // Calendar mCal = Calendar.getInstance();
	// //
	// // filter.setFromTime(mCal.getTimeInMillis());
	// // filter.setToTime(mCal.getTimeInMillis()+86400000);
	// // try {
	// // return service.getObjects(filter, Intro.mToken);
	// // } catch (DiscoverTrentoConnectorException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
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
	// mList.setAdapter(new ListArrayAdapter(mContext, mResult));
	// }
	// }
}
