package smartcampus.android.template.standalone.Activity.FacilitiesBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.drawable;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import smartcampus.android.template.standalone.Utilities.RestRequest;

import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Address;
import android.location.Geocoder;
import android.location.LocationListener;
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
	private LocationClient mLocationClient;

	private ArrayList<POIObject> mListPOI = new ArrayList<POIObject>();

	public SelectBooking(FragmentManager fm, ImageView copy, GoogleMap map,
			LocationClient locationClient) {
		this.fm = fm;
		mPOICopy = copy;
		mMap = map;
		mLocationClient = locationClient;
	}

	public SelectBooking() {
		this(null, null, null, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.activity_select_booking, null);

		POIObject obj = null;
		obj = new POIObject(45.542322, 11.592636, "Buy Tickets",
				"Via 1, Vicenza");
		mListPOI.add(obj);
		obj = new POIObject(44.542322, 11.702636, "Info Points",
				"Via 1, Vicenza");
		mListPOI.add(obj);
		obj = new POIObject(46.745522, 12.702436, "Game Places",
				"Via 1, Vicenza");
		mListPOI.add(obj);
		obj = new POIObject(44.423522, 11.701636, "Hotel", "Via 1, Vicenza");
		mListPOI.add(obj);
		obj = new POIObject(43.542322, 11.207636, "Other POIs",
				"Via 1, Vicenza");
		mListPOI.add(obj);

		ArrayList<String> mValues = new ArrayList<String>();
		mValues.add("Buy Tickets");
		mValues.add("Info Points");
		mValues.add("Game Places");
		mValues.add("Hotels");
		mValues.add("Other POIs");
		ListView mSelect = (ListView) mView.findViewById(R.id.list_select);
		mSelect.setAdapter(new ListArrayAdapter(mView.getContext(), mValues));

		mSelect.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				fm.popBackStack();

				TranslateAnimation anim = new TranslateAnimation(0, 0, -500, 0);
				anim.setDuration(300);
				anim.setFillAfter(true);
				mPOICopy.startAnimation(anim);
				mPOICopy.setVisibility(View.VISIBLE);

				String mParams = null;

				switch (arg2) {
				case 0:
					mParams = "Buy Tickets";
					break;
				case 1:
					mParams = "Info Points";
					break;
				case 2:
					mParams = "Game Places";
					break;
				case 3:
					mParams = "Hotel";
					break;
				case 4:
					mParams = "Other POIs";
					break;
				}
				try {
					setPOIsMap(new DownloadPOI().execute(mParams).get());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		return mView;
	}

	private void setPOIsMap(ArrayList<POIObject> list) throws IOException {
		LatLng mMyMarker = null;
		mMap.clear();
		if (mLocationClient.getLastLocation() != null)
			mMyMarker = new LatLng(mLocationClient.getLastLocation()
					.getLatitude(), mLocationClient.getLastLocation()
					.getLongitude());

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (POIObject obj : list) {
			builder.include(new LatLng(obj.getGPSlat(), obj.getGPSlng()));

			mMap.addMarker(new MarkerOptions().position(
					new LatLng(obj.getGPSlat(), obj.getGPSlng())).title(
					obj.getAddress()).snippet(obj.getType()));
		}
		if (mMyMarker != null)
			builder.include(mMyMarker);
		final LatLngBounds bounds = builder.build();

		mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
	}

	private class DownloadPOI extends
			AsyncTask<String, Void, ArrayList<POIObject>> {

		@Override
		protected ArrayList<POIObject> doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			ArrayList<POIObject> mResult = new ArrayList<POIObject>();
			for (POIObject obj : mListPOI)
				if (obj.getType().equalsIgnoreCase(arg0[0]))
					mResult.add(obj);

			return mResult;
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

			FontTextView mSelect = (FontTextView) rowView
					.findViewById(R.id.text_select);
			mSelect.setText(values.get(position).toUpperCase());

			ImageView mLogo = (ImageView) rowView.findViewById(R.id.image_logo);

			switch (position) {
			case 0:
				mLogo.setImageResource(R.drawable.tik);
				break;
			case 1:
				mLogo.setImageResource(R.drawable.info);
				break;
			case 2:
				mLogo.setImageResource(R.drawable.game);
				break;
			case 3:
				mLogo.setImageResource(R.drawable.hot);
				break;
			case 4:
				mLogo.setImageResource(R.drawable.poi);
				break;
			}

			return rowView;
		}
	}

	private class POIObject {
		private double GPSlat;
		private double GPSlng;
		private String type;
		private String address;

		public POIObject(double gPSlat, double gPSlng, String type,
				String address) {
			super();
			GPSlat = gPSlat;
			GPSlng = gPSlng;
			this.type = type;
			this.address = address;
		}

		public double getGPSlat() {
			return GPSlat;
		}

		public void setGPSlat(double gPSlat) {
			GPSlat = gPSlat;
		}

		public double getGPSlng() {
			return GPSlng;
		}

		public void setGPSlng(double gPSlng) {
			GPSlng = gPSlng;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}
	}

}
