package smartcampus.android.template.standalone.Activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;

import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.discovertrento.DiscoverTrentoConnector;
import eu.trentorise.smartcampus.discovertrento.DiscoverTrentoConnectorException;
import eu.trentorise.smartcampus.dt.model.EventObject;
import eu.trentorise.smartcampus.dt.model.ObjectFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import android.smartcampus.template.standalone.Atleta;
import android.smartcampus.template.standalone.Sport;

@SuppressLint("ValidFragment")
public class PageInfoSport extends Fragment {
	
	private Sport mSport;
	private int mPosition;

	public PageInfoSport(Sport sport, int position)
	{
		mSport = sport;
		mPosition = position;
	}
	
	public PageInfoSport()
	{
		this(null, -1);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.activity_page_info_sport, null);
		
		RelativeLayout mContainerDesc = (RelativeLayout)mView.findViewById(R.id.container_descrizione);
		RelativeLayout mContainerEventi = (RelativeLayout)mView.findViewById(R.id.container_eventi_correlati);
		RelativeLayout mContainerAtleti = (RelativeLayout)mView.findViewById(R.id.container_atleti);

		switch(mPosition)
		{
		case 0: mContainerDesc.setVisibility(View.VISIBLE);
			mContainerEventi.setVisibility(View.GONE);
			mContainerAtleti.setVisibility(View.GONE);
			
			((FontTextView)mView.findViewById(R.id.text_descrizione)).setText(mSport.getDescrizione());
			break;
		case 1: mContainerDesc.setVisibility(View.GONE);
			mContainerEventi.setVisibility(View.VISIBLE);
			mContainerAtleti.setVisibility(View.GONE);
			
			new DownloadEventiCorrelatiSport(mView.getContext(), 
					(ListView)mView.findViewById(R.id.list_eventi_correlati)).execute
					(new Void[1]);
			break;
		case 2: mContainerDesc.setVisibility(View.GONE);
			mContainerEventi.setVisibility(View.GONE);
			mContainerAtleti.setVisibility(View.VISIBLE);
			
//			ListView mGrigliaAtleti = (ListView)mView.findViewById(R.id.list_atleti);
//			mGrigliaAtleti.setAdapter(new ListSportArrayAdapter(mView.getContext(),
//					(ArrayList<Atleta>)mSport.getAtletaList()));
			break;
		}
		
		return mView;
	}
	
private class ListArrayAdapter extends ArrayAdapter<EventObject> {
		
		private final Context context;
		private final ArrayList<EventObject> values;

		public ListArrayAdapter(Context context, ArrayList<EventObject> values) {
			super(context, R.layout.row_eventi, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View rowView = inflater.inflate(R.layout.row_eventi, parent, false);
		
			Calendar mCal = Calendar.getInstance();
			mCal.setTimeInMillis(values.get(position).getFromTime());
			
			String ora = null;
			
			if (mCal.get(Calendar.MINUTE) < 10)
				ora = mCal.get(Calendar.HOUR_OF_DAY)+":0"+mCal.get(Calendar.MINUTE);
			else
				ora = mCal.get(Calendar.HOUR_OF_DAY)+":"+mCal.get(Calendar.MINUTE);
			if (mCal.get(Calendar.AM_PM) == Calendar.AM)
				ora = ora + " AM";
			else
				ora = ora + " PM";
			
			((FontTextView)rowView.findViewById(R.id.text_orario)).setText(ora);
			
			if (values.get(position).getTitle().length() <= 12)
				((FontTextView)rowView.findViewById(R.id.text_nome_evento)).setText
					(values.get(position).getTitle().toUpperCase());
			else
				((FontTextView)rowView.findViewById(R.id.text_nome_evento)).setText
				(values.get(position).getTitle().toUpperCase().substring(0, 11)+"...");
				
			
//			((FontTextView)rowView.findViewById(R.id.text_tipo_gara)).setText
//				(values.get(position).ge);
			
			return rowView;
		}	
	}

private class ListSportArrayAdapter extends ArrayAdapter<Atleta>
{
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
		View rowView = inflater.inflate(R.layout.grid_atleti, parent, false);
		
		((FontTextView)rowView.findViewById(R.id.text_nome_cognome)).setText
			(values.get(position).getCognome()+" "+
					values.get(position).getNome());
		
		((FontTextView)rowView.findViewById(R.id.text_nazionalita)).setText
		(values.get(position).getNazionalita());
		
		return rowView;
	}
}

private class DownloadEventiCorrelatiSport extends AsyncTask<Void, Void, Map<String, List<?>>>
{
	private Context mContext;
	private ListView mList;
	private String mNomeSport;
	
	public DownloadEventiCorrelatiSport(Context context, ListView mPrimaLista)
	{
		mContext = context;
		mList = mPrimaLista;
		
	}
	
	@Override
	protected Map<String, List<?>> doInBackground(Void... params) {
		// TODO Auto-generated method stub
		DiscoverTrentoConnector service = new DiscoverTrentoConnector
				("https://vas-dev.smartcampuslab.it");
		Thread.currentThread().setContextClassLoader(EventObject.class.getClassLoader());
		ObjectFilter filter = new ObjectFilter();
		filter.setClassName(EventObject.class.getCanonicalName());
		
		Calendar mCal = Calendar.getInstance();
		
		filter.setFromTime(mCal.getTimeInMillis());
		filter.setToTime(mCal.getTimeInMillis()+86400000);
		try {
			return service.getObjects(filter, Intro.mToken);
		} catch (DiscoverTrentoConnectorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPostExecute(Map<String, List<?>> result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		Collection<List<?>> a = result.values();
		Iterator<List<?>> it = a.iterator();
		ArrayList<EventObject> mResult = new ArrayList<EventObject>();
			
		while(it.hasNext()) 
		{
			List<?> tmp = it.next();
			for (int i=0; i<tmp.size(); i++)
			{
				mResult.add((EventObject)tmp.get(i));
			}
		}
		mList.setAdapter(new ListArrayAdapter(mContext, mResult));
	}
}
}
