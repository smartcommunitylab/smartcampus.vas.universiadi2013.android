package smartcampus.android.template.standalone;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import eu.trentorise.smartcampus.dt.model.EventObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class InfoEventi extends Fragment implements LocationListener{

	private EventObject mEvento;
	private FragmentManager manager;
	
	private LatLng mMarker;
	private GoogleMap mMappa;
	private LocationManager locationManager;
	
	public InfoEventi(EventObject evento, FragmentManager fm)
	{
		mEvento = evento;
		manager = fm;
	}
	
	public InfoEventi()
	{
		this(null, null);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.activity_info_eventi, null);
		
		FontTextView mDesc = (FontTextView)mView.findViewById(R.id.text_descrizione);
		if (mEvento.getDescription() != null)
			mDesc.setText(mEvento.getDescription());
		mDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		mDesc.setMovementMethod(new ScrollingMovementMethod());
		
		mMappa = ((SupportMapFragment)manager.findFragmentById(R.id.mappa))
					.getMap();

		UiSettings mMapController = mMappa.getUiSettings();
        
        mMapController.setCompassEnabled(true);
        mMapController.setMyLocationButtonEnabled(false);
        mMapController.setZoomControlsEnabled(false);
		
        mMappa.setMyLocationEnabled(true);
        
        if (mEvento.getLocation() != null)
        {
            mMarker = new LatLng(mEvento.getLocation()[0], mEvento.getLocation()[1]);
            mMappa.addMarker(new MarkerOptions().position(mMarker));
            mMappa.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker, 10));
        }
		
        //Otteniamo il riferimento al LocationManager
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
		return mView;
	}
	

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		FragmentTransaction trans = manager.beginTransaction();
		trans.remove(manager.findFragmentById(R.id.mappa));
		trans.commit();
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
