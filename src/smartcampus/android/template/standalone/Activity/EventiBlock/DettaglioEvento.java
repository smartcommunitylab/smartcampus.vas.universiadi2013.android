package smartcampus.android.template.standalone.Activity.EventiBlock;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import smartcampus.android.template.standalone.Utilities.MapTextRoute;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.widget.TextView;

public class DettaglioEvento extends FragmentActivity implements
		LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
		OnConnectionFailedListener {

	private GoogleMap mMappa;
	private LatLng mMarker;
	private LocationManager locationManager;
	private LocationClient mLocationClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dettaglio_evento);

		((FontTextView) findViewById(R.id.text_descrizione))
				.setText(getIntent().getStringExtra("Desc"));

		String[] mLocation = getIntent().getStringExtra("GPS").split(",");

		mMappa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mappa)).getMap();

		UiSettings mMapController = mMappa.getUiSettings();

		mMapController.setCompassEnabled(true);
		mMapController.setMyLocationButtonEnabled(false);
		mMapController.setZoomControlsEnabled(false);

		mMappa.setMyLocationEnabled(true);

		if (mLocation != null) {
			mMarker = new LatLng(Double.parseDouble(mLocation[0]),
					Double.parseDouble(mLocation[1]));
			Geocoder coder = new Geocoder(getApplicationContext(),
					Locale.getDefault());

			Address adrs;
			try {
				adrs = coder.getFromLocation(mMarker.latitude,
						mMarker.longitude, 1).get(0);
				mMappa.addMarker(new MarkerOptions()
						.position(mMarker)
						.icon(BitmapDescriptorFactory
								.fromBitmap(drawMarkerWithTileAndAddress(
										getIntent().getStringExtra("Nome"),
										adrs.getAddressLine(0) + " - "
												+ adrs.getAddressLine(1))))
						.anchor(0.5f, 1));

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// mMappa.animateCamera(CameraUpdateFactory.newLatLngZoom(mMarker,
			// 10));
		}

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

		mLocationClient = new LocationClient(this, this, this);
		mLocationClient.connect();
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
		if (measureText > 230 && measureText <= 460) {
			color.setTextSize(15);
			measureText = (int) ((color.measureText(add) >= color
					.measureText(title)) ? color.measureText(add) + 20 : color
					.measureText(title)) + 20;
		} else if (measureText > 460) {
			color.setTextSize(15);
			int lastChar = color.breakText(add, true, 460, null);
			add = add.substring(0, lastChar);
			measureText = (int) ((color.measureText(add) >= color
					.measureText(title)) ? color.measureText(add) + 20 : color
					.measureText(title)) + 20;
		}

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

	// private void drawRoute(JSONArray array) {
	// try {
	// for (int i = 0; i < array.length(); i++) {
	// Double srcLat = 0.0;
	// Double srcLng = 0.0;
	// Double destLat = 0.0;
	// Double destLng = 0.0;
	// if (i == 0) {
	// srcLat = mLocationClient.getLastLocation().getLatitude();
	// srcLng = mLocationClient.getLastLocation().getLongitude();
	// destLat = array.getJSONObject(i).getDouble("lat");
	// destLng = array.getJSONObject(i).getDouble("lon");
	// } else {
	// srcLat = destLat;
	// srcLng = destLng;
	// destLat = array.getJSONObject(i).getDouble("lat");
	// destLng = array.getJSONObject(i).getDouble("lon");
	// }
	//
	// mMappa.addPolyline(new PolylineOptions()
	// .add(new LatLng(srcLat, srcLng),
	// new LatLng(destLat, destLng)).width(2)
	// .color(Color.BLUE).geodesic(true));
	// }
	// } catch (JSONException e) {
	// }
	// }

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

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		LatLng mMyMarker = new LatLng(mLocationClient.getLastLocation()
				.getLatitude(), mLocationClient.getLastLocation()
				.getLongitude());

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(mMarker);
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

		// MapRoute route = new MapRoute();
		// route.execute(new String[] { Double.toString(mMarker.latitude),
		// Double.toString(mMarker.longitude) });
		//
		// try {
		// drawRoute(route.get());
		// } catch (InterruptedException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (ExecutionException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

}
