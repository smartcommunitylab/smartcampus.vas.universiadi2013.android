package smartcampus.android.template.standalone;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DettaglioSport extends FragmentActivity implements LocationListener{

	private ElementSport mSport; 
	private GoogleMap mMappa;
	private LatLng mMarker;
	private LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dettaglio_sport);
		
		mSport = Sport.mSport.get(getIntent().getIntExtra("Index", -1));
		
		ArrayList<Fragment> fragment = new ArrayList<Fragment>();
		for (int i=0; i<3; i++)
			fragment.add(new PageInfoSport(mSport, i));
		ViewPager mPagerSport = (ViewPager)findViewById(R.id.pager_sport);
		mPagerSport.setAdapter(new PagerAdapter(getSupportFragmentManager(), fragment));
		
		mMappa = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mappa))
				.getMap();

		UiSettings mMapController = mMappa.getUiSettings();
    
		mMapController.setCompassEnabled(true);
		mMapController.setMyLocationButtonEnabled(false);
		mMapController.setZoomControlsEnabled(false);
	
		mMappa.setMyLocationEnabled(true);
    
		if (mSport.getmPosStadio() != null)
		{
			mMarker = new LatLng(mSport.getmPosStadio()[0], mSport.getmPosStadio()[1]);
			mMappa.addMarker(new MarkerOptions().position(mMarker));
			mMappa.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker, 10));
		}
	
		//Otteniamo il riferimento al LocationManager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if(locationManager != null)
		{
			boolean gpsIsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkIsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if(networkIsEnabled)
			{
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 100, this);        
			}
			else if(gpsIsEnabled)
			{
				locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 100, this);
			}
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		LatLng mMyMarker = new LatLng(location.getLatitude(), location.getLongitude());
    	
    	LatLngBounds.Builder builder = new LatLngBounds.Builder();
    	builder.include(mMarker);
    	builder.include(mMyMarker);
    	LatLngBounds bounds = builder.build();

    	mMappa.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
	}

	@Override
	public void onProviderDisabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		// TODO Auto-generated method stub
		
	}
	
	private class PagerAdapter extends FragmentPagerAdapter{

		   // fragments to instantiate in the viewpager
		   private List<Fragment> fragments;
		   
		   // constructor
		   public PagerAdapter(FragmentManager fm,List<Fragment> fragments) {
		      super(fm);
		      this.fragments = fragments;
		   }
		   
		   // return access to fragment from position, required override
		   @Override
		   public Fragment getItem(int position) {
		      return this.fragments.get(position);
		   }

		   // number of fragments in list, required override
		   @Override
		   public int getCount() {
		      return this.fragments.size();
		   }
		   
		   @Override
		   public int getItemPosition(Object object){
			     return POSITION_NONE;
			}
		}

}
