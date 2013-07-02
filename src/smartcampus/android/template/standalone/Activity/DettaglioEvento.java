package smartcampus.android.template.standalone.Activity;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;

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
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;

public class DettaglioEvento extends FragmentActivity implements LocationListener {

	private GoogleMap mMappa;
	private LatLng mMarker;
	private LocationManager locationManager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dettaglio_evento);
		
		((FontTextView)findViewById(R.id.text_descrizione)).setText
		(getIntent().getStringExtra("Desc"));
		
		double[] mLocation = getIntent().getDoubleArrayExtra("GPS");
		
		mMappa = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mappa))
				.getMap();

		UiSettings mMapController = mMappa.getUiSettings();
    
		mMapController.setCompassEnabled(true);
		mMapController.setMyLocationButtonEnabled(false);
		mMapController.setZoomControlsEnabled(false);
	
		mMappa.setMyLocationEnabled(true);
		
		if (mLocation != null)
		{
			mMarker = new LatLng(mLocation[0], mLocation[1]);
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

}
