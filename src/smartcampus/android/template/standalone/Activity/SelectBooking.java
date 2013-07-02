package smartcampus.android.template.standalone.Activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.drawable;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import eu.trentorise.smartcampus.discovertrento.DiscoverTrentoConnector;
import eu.trentorise.smartcampus.discovertrento.DiscoverTrentoConnectorException;
import eu.trentorise.smartcampus.dt.model.EventObject;
import eu.trentorise.smartcampus.dt.model.ObjectFilter;
import eu.trentorise.smartcampus.dt.model.POIObject;
import android.os.AsyncTask;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

@SuppressLint("ValidFragment")
public class SelectBooking extends Fragment {
	
	private FragmentManager fm;
	private ImageView mPOICopy;
	private GoogleMap mMap;
	
	public SelectBooking(FragmentManager fm, ImageView copy, GoogleMap map)
	{
		this.fm = fm;
		mPOICopy = copy;
		mMap = map;
	}
	
	public SelectBooking()
	{
		this(null, null, null);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.activity_select_booking, null);
		
		ArrayList<String> mValues = new ArrayList<String>();
		mValues.add("Buy Tickets");
		mValues.add("Info Points");
		mValues.add("Game Places");
		mValues.add("Hotels");
		mValues.add("Other POIs");
		ListView mSelect = (ListView)mView.findViewById(R.id.list_select);
		mSelect.setAdapter(new ListArrayAdapter(mView.getContext(), mValues));
		
		mSelect.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				fm.popBackStack();
				
				TranslateAnimation anim = new TranslateAnimation(0,0,
						-500, 0);
				anim.setDuration(300);
				anim.setFillAfter(true);
				mPOICopy.startAnimation(anim);
				mPOICopy.setVisibility(View.VISIBLE);
				
				new DownloadPOI(arg2).execute(new Void[1]);
			}
		});
		
		return mView;
	}

	private class DownloadPOI extends AsyncTask<Void, Void, Map<String, List<?>>>
	{
		private int mIndex;
		
		public DownloadPOI(int index)
		{
			mIndex = index;
		}
		
		@Override
		protected Map<String, List<?>> doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			DiscoverTrentoConnector service = new DiscoverTrentoConnector
					("https://vas-dev.smartcampuslab.it");
			Thread.currentThread().setContextClassLoader(EventObject.class.getClassLoader());
			ObjectFilter filter = new ObjectFilter();
			filter.setClassName(POIObject.class.getCanonicalName());
			switch(mIndex)
			{
			case 0: filter.setType("Accomodation");
				break;
			case 1: filter.setType("Museums");
				break;
			case 2: filter.setType("Offices");
				break;
			case 3: filter.setType("Food");
				break;
			case 4: filter.setType("University");
				break;
			}
			filter.setCenter(new double[]{46.07513,11.120739});
			filter.setRadius(0.05);
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
 			ArrayList<POIObject> mResult = new ArrayList<POIObject>();
 			
		    LatLngBounds.Builder builder = new LatLngBounds.Builder();
		    mMap.clear();
 			while(it.hasNext()) 
 			{
 				List<?> tmp = it.next();
 				for (int i=0; i<tmp.size(); i++)
 				{
 					POIObject obj = (POIObject)tmp.get(i);

 					LatLng mMarker = new LatLng(obj.getLocation()[0], 
 							obj.getLocation()[1]);
 			    	builder.include(mMarker);
 					Marker mPin = mMap.addMarker(new MarkerOptions().position(mMarker)
 							.title(obj.getTitle()+"("+obj.getType()+")")
 							.snippet(obj.getPoi().getStreet()+","+obj.getPoi().getCity()));
 				}
 			}
 			LatLngBounds bounds = builder.build();
 			mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
			
		}
	}
	
	private class ListArrayAdapter extends ArrayAdapter<String> {
		
		private final Context context;
		private final ArrayList<String> values;

		public ListArrayAdapter(Context context, ArrayList<String> values) {
			super(context, R.layout.row_select, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			View rowView = inflater.inflate(R.layout.row_select, parent, false);
			
			FontTextView mSelect = (FontTextView)rowView.findViewById(R.id.text_select);
			mSelect.setText(values.get(position).toUpperCase());
			
			ImageView mLogo = (ImageView)rowView.findViewById(R.id.image_logo);
			
			switch(position)
			{
			case 0: mLogo.setImageResource(R.drawable.tik);
				break;
			case 1: mLogo.setImageResource(R.drawable.info);
				break;
			case 2: mLogo.setImageResource(R.drawable.game);
				break;
			case 3: mLogo.setImageResource(R.drawable.hot);
				break;
			case 4: mLogo.setImageResource(R.drawable.poi);
				break;
			}
			
			return rowView;
		}
}

}
