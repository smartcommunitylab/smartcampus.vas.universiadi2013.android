package smartcampus.android.template.standalone.Activity.FacilitiesBlock;

import java.io.IOException;
import java.util.ArrayList;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.anim;
import smartcampus.android.template.standalone.R.drawable;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Booking extends FragmentActivity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		OnConnectionFailedListener {

	private GoogleMap mMappa;
	private ImageView mButtonPOI;
	private LocationManager locationManager;
	private LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking);

		mMappa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mappa)).getMap();

		UiSettings mMapController = mMappa.getUiSettings();

		mMapController.setCompassEnabled(true);
		mMapController.setMyLocationButtonEnabled(false);
		mMapController.setZoomControlsEnabled(false);

		mMappa.setMyLocationEnabled(true);

		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();

		mMappa.setOnMapClickListener(new OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				// TODO Auto-generated method stub
				FragmentManager fragmentManager = getSupportFragmentManager();
				if (fragmentManager.findFragmentByTag("select") != null) {
					fragmentManager.popBackStack();

					TranslateAnimation anim = new TranslateAnimation(0, 0,
							-500, 0);
					anim.setDuration(300);
					anim.setFillAfter(true);
					mButtonPOI.startAnimation(anim);
					mButtonPOI.setVisibility(View.VISIBLE);
				}
			}

		});
		mButtonPOI = (ImageView) findViewById(R.id.image_button_poi);
		mButtonPOI.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					mButtonPOI.setImageResource(R.drawable.button_poi_down);
					return true;
				}
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					mButtonPOI.setImageResource(R.drawable.button_poi_up);
					if (getSupportFragmentManager().findFragmentByTag("select") == null) {
						TranslateAnimation anim = new TranslateAnimation(0, 0,
								0, -500);
						anim.setDuration(500);
						anim.setFillAfter(true);
						mButtonPOI.startAnimation(anim);
						mButtonPOI.setVisibility(View.GONE);

						FragmentManager fragmentManager = getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager
								.beginTransaction();
						fragmentTransaction
								.setCustomAnimations(R.anim.slide_in_bottom,
										R.anim.slide_out_bottom,
										R.anim.slide_in_bottom,
										R.anim.slide_out_bottom);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.add(R.id.container_select,
								new SelectBooking(fragmentManager, mButtonPOI,
										mMappa, mLocationClient), "select");
						fragmentTransaction.commit();
					} else {
						getSupportFragmentManager().popBackStack();

						TranslateAnimation anim = new TranslateAnimation(0, 0,
								-500, 0);
						anim.setDuration(300);
						anim.setFillAfter(true);
						mButtonPOI.startAnimation(anim);
						mButtonPOI.setVisibility(View.VISIBLE);
					}
					return true;
				}
				return false;
			}

		});

		// Otteniamo il riferimento al LocationManager
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		if (locationManager != null) {
			boolean gpsIsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkIsEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

			if (networkIsEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 100, this);
			} else if (gpsIsEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER, 0, 100, this);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (getSupportFragmentManager().findFragmentByTag("select") != null) {
			getSupportFragmentManager().popBackStack();

			TranslateAnimation anim = new TranslateAnimation(0, 0, -500, 0);
			anim.setDuration(300);
			anim.setFillAfter(true);
			mButtonPOI.startAnimation(anim);
			mButtonPOI.setVisibility(View.VISIBLE);
		} else
			super.onBackPressed();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		LatLng mMyMarker = null;
		if (mLocationClient.getLastLocation() != null)
			mMyMarker = new LatLng(mLocationClient.getLastLocation()
					.getLatitude(), mLocationClient.getLastLocation()
					.getLongitude());

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		if (mMyMarker != null)
			builder.include(mMyMarker);
		final LatLngBounds bounds = builder.build();

		mMappa.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition position) {
				// TODO Auto-generated method stub
				mMappa.animateCamera(CameraUpdateFactory.newLatLngBounds(
						bounds, 50));
				mMappa.setOnCameraChangeListener(null);
			}
		});
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

}
