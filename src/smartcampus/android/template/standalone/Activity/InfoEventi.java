package smartcampus.android.template.standalone.Activity;

import java.io.IOException;
import java.util.Locale;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import eu.trentorise.smartcampus.dt.model.EventObject;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

@SuppressLint("ValidFragment")
public class InfoEventi extends Fragment implements LocationListener {

	private android.smartcampus.template.standalone.Evento mEvento;
	private FragmentManager manager;

	private LatLng mMarker;
	private GoogleMap mMappa;
	private LocationManager locationManager;

	public InfoEventi(android.smartcampus.template.standalone.Evento evento,
			FragmentManager fm) {
		mEvento = evento;
		manager = fm;
	}

	public InfoEventi() {
		this(null, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.activity_info_eventi, null);

		FontTextView mDesc = (FontTextView) mView
				.findViewById(R.id.text_descrizione);
		if (mEvento.getDescrizione() != null)
			mDesc.setText(mEvento.getDescrizione());
		mDesc.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
		mDesc.setMovementMethod(new ScrollingMovementMethod());

		mMappa = ((SupportMapFragment) manager.findFragmentById(R.id.mappa))
				.getMap();

		UiSettings mMapController = mMappa.getUiSettings();

		mMapController.setCompassEnabled(true);
		mMapController.setMyLocationButtonEnabled(false);
		mMapController.setZoomControlsEnabled(false);

		mMappa.setMyLocationEnabled(true);

		if (mEvento.getLatGPS() != 0 && mEvento.getLngGPS() != 0) {
			mMarker = new LatLng(mEvento.getLatGPS(), mEvento.getLngGPS());
			Geocoder coder = new Geocoder(getActivity(), Locale.getDefault());

			Address adrs;
			try {
				adrs = coder.getFromLocation(mMarker.latitude,
						mMarker.longitude, 1).get(0);
				mMappa.addMarker(new MarkerOptions()
						.position(mMarker)
						.icon(BitmapDescriptorFactory
								.fromBitmap(drawMarkerWithTileAndAddress(
										mEvento.getNome(),
										adrs.getAddressLine(0) + " - "
												+ adrs.getAddressLine(1))))
						.anchor(0.5f, 1));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// final Marker mMark = mMappa.addMarker(new MarkerOptions()
			// .position(mMarker));
			// mMappa.setOnMarkerClickListener(new OnMarkerClickListener() {
			//
			// @Override
			// public boolean onMarkerClick(Marker marker) {
			// // TODO Auto-generated method stub
			// mMark.setTitle(mEvento.getNome() + " - "
			// + mEvento.getTipoSport());
			// Geocoder coder = new Geocoder(getActivity(),
			// Locale.getDefault());
			// try {
			// Address adrs = coder.getFromLocation(
			// marker.getPosition().latitude,
			// marker.getPosition().longitude, 1).get(0);
			// mMark.setSnippet(adrs.getAddressLine(0)
			// +" - "+adrs.getAddressLine(1));
			// } catch (IOException e) {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
			// return false;
			// }
			// });
			mMappa.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker, 10));
		}

		// Otteniamo il riferimento al LocationManager
		locationManager = (LocationManager) getActivity().getSystemService(
				Context.LOCATION_SERVICE);

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
		return mView;
	}

	private Bitmap drawMarkerWithTileAndAddress(String title, String add) {

		Bitmap.Config conf = Bitmap.Config.ARGB_8888;
		// Create text
		Paint color = new Paint();
		color.setAntiAlias(true);
		color.setTextAlign(Align.CENTER);
		color.setTextSize(20);
		color.setColor(Color.WHITE);
		int measureText = (int) ((color.measureText(add) >= color
				.measureText(title)) ? color.measureText(add) + 20 : color
				.measureText(title)) + 20;
		Bitmap bmpLabel = Bitmap.createBitmap(measureText + 20, 60, conf);
		Canvas canvasLabel = new Canvas(bmpLabel);
		bmpLabel.eraseColor(Color.argb(255, 50, 148, 173));
		canvasLabel.drawText(title, measureText / 2, 25, color);
		canvasLabel.drawText(add, measureText / 2, 50, color);
		// Create arrow
		Bitmap arrow = BitmapFactory.decodeResource(getResources(),
				R.drawable.marker);
		// Create all label
		Bitmap bmpBackground = Bitmap.createBitmap(measureText,
				40 + arrow.getWidth(), conf);
		Canvas canvasBackGround = new Canvas(bmpBackground);
		// Merge Canvas and Bitmap
		canvasBackGround.drawBitmap(bmpLabel, 0, 0, null);
		canvasBackGround.drawBitmap(arrow, measureText / 2 - arrow.getWidth()
				/ 2, 60, null);

		return bmpBackground;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		locationManager.removeUpdates(this);

		FragmentTransaction trans = manager.beginTransaction();
		trans.remove(manager.findFragmentById(R.id.mappa));
		trans.commit();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		LatLng mMyMarker = new LatLng(location.getLatitude(),
				location.getLongitude());

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
