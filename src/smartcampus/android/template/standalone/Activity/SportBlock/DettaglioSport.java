package smartcampus.android.template.standalone.Activity.SportBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import smartcampus.android.template.universiadi.R;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.smartcampus.template.standalone.Atleta;
import android.smartcampus.template.standalone.Evento;
import android.smartcampus.template.standalone.Sport;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class DettaglioSport extends FragmentActivity implements
		LocationListener, GooglePlayServicesClient.ConnectionCallbacks,
		OnConnectionFailedListener {

	private android.smartcampus.template.standalone.Sport mSport;
	private GoogleMap mMappa;
	private LocationManager locationManager;
	private LocationClient mLocationClient;

	private ArrayList<Evento> mListEventi;
	private ArrayList<Atleta> mListaAtleti = new ArrayList<Atleta>();
	ArrayList<Fragment> fragment = new ArrayList<Fragment>();
	LatLngBounds.Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dettaglio_sport);

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(DettaglioSport.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_wait);
				dialog.getWindow().setBackgroundDrawableResource(
						R.drawable.dialog_rounded_corner_light_black);
				dialog.show();
				dialog.setCancelable(true);
				dialog.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						cancel(true);
						finish();
					}
				});

			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				Map<String, Object> mResult = ManagerData
						.getSport(DettaglioSport.this);
				if ((Boolean) mResult.get("connectionError")) {
					Dialog noConnection = new Dialog(DettaglioSport.this);
					noConnection.requestWindowFeature(Window.FEATURE_NO_TITLE);
					noConnection.setContentView(R.layout.dialog_no_connection);
					noConnection.getWindow().setBackgroundDrawableResource(
							R.drawable.dialog_rounded_corner_light_black);
					noConnection.show();
					noConnection.setCancelable(true);
					noConnection.setOnCancelListener(new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							// TODO Auto-generated method stub
							finish();
						}
					});
				} else {
					if (getIntent().getBooleanExtra("search", false)) {
						for (Sport sport : (ArrayList<Sport>) mResult
								.get("params")) {
							if (sport.getNome().equalsIgnoreCase(
									getIntent().getStringExtra("sport")))
								mSport = sport;
						}
						mListEventi = (ArrayList<Evento>) ManagerData
								.getEventiPerSport(
										getIntent().getStringExtra("sport"))
								.get("params");
					} else {
						mSport = (Sport) (((ArrayList<Sport>) (ManagerData
								.getSport(DettaglioSport.this).get("params")))
								.get(getIntent().getIntExtra("Index", -1)));
						mListEventi = (ArrayList<Evento>) ManagerData
								.getEventiPerSport(mSport.getNome()).get(
										"params");
					}
					for (Evento evento : mListEventi) {
						ArrayList<Atleta> mListaAtletiTmp = (ArrayList<Atleta>) ManagerData
								.getAtletiPerEvento(evento).get("params");
						for (Atleta atleta : mListaAtletiTmp)
							mListaAtleti.add(atleta);
					}
					for (int i = 0; i < 3; i++)
						fragment.add(new PageInfoSport(mSport.getDescrizione(),
								mListEventi, mListaAtleti, i));
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				// START ONPOST
				ViewPager mPagerSport = (ViewPager) findViewById(R.id.pager_info_eventi);
				mPagerSport.setAdapter(new PagerAdapter(
						getSupportFragmentManager(), fragment));

				mMappa = ((SupportMapFragment) getSupportFragmentManager()
						.findFragmentById(R.id.mappa)).getMap();

				UiSettings mMapController = mMappa.getUiSettings();

				mMapController.setCompassEnabled(true);
				mMapController.setMyLocationButtonEnabled(false);
				mMapController.setZoomControlsEnabled(false);

				mMappa.setMyLocationEnabled(true);

				builder = new LatLngBounds.Builder();
				for (Evento mEvento : mListEventi) {
					LatLng mMarker = new LatLng(mEvento.getLatGPS(),
							mEvento.getLngGPS());
					builder.include(mMarker);
					Geocoder coder = new Geocoder(DettaglioSport.this,
							Locale.getDefault());

					Address adrs;
					try {
						adrs = coder.getFromLocation(mMarker.latitude,
								mMarker.longitude, 1).get(0);
						mMappa.addMarker(new MarkerOptions()
								.position(mMarker)
								.icon(BitmapDescriptorFactory
										.fromBitmap(drawMarkerWithTitleAndAddress(
												mEvento.getNome(),
												adrs.getAddressLine(0)
														+ " - "
														+ adrs.getAddressLine(1))))
								.anchor(0.5f, 1));
						mMappa.setOnMarkerClickListener(new OnMarkerClickListener() {

							@Override
							public boolean onMarkerClick(Marker marker) {
								// TODO Auto-generated method stub
								if (mLocationClient.getLastLocation() != null) {
									Intent intent = new Intent(
											android.content.Intent.ACTION_VIEW,
											Uri.parse("http://maps.google.com/maps?saddr="
													+ mLocationClient
															.getLastLocation()
															.getLatitude()
													+ ","
													+ mLocationClient
															.getLastLocation()
															.getLongitude()
													+ "&daddr="
													+ marker.getPosition().latitude
													+ ","
													+ marker.getPosition().longitude));
									startActivity(intent);
									return true;
								}
								return false;
							}

						});
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
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
								LocationManager.GPS_PROVIDER, 0, 100,
								DettaglioSport.this);
					} else if (gpsIsEnabled) {
						locationManager.requestLocationUpdates(
								LocationManager.NETWORK_PROVIDER, 0, 100,
								DettaglioSport.this);
					}
				}
				mLocationClient = new LocationClient(DettaglioSport.this,
						DettaglioSport.this, DettaglioSport.this);
				mLocationClient.connect();

				// END ONPOST

				dialog.dismiss();
			}

		}.execute();
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
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
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		LatLng mMyMarker = null;
		if (mLocationClient.getLastLocation() != null) {
			mMyMarker = new LatLng(mLocationClient.getLastLocation()
					.getLatitude(), mLocationClient.getLastLocation()
					.getLongitude());

			builder.include(mMyMarker);
		}
		final LatLngBounds bounds = builder.build();

		mMappa.setOnCameraChangeListener(new OnCameraChangeListener() {

			@Override
			public void onCameraChange(CameraPosition position) {
				// TODO Auto-generated method stub
				mMappa.animateCamera(CameraUpdateFactory.newLatLngBounds(
						bounds, 50));
				mMappa.setOnCameraChangeListener(null);

				// (new MapRoute()).execute(new String[]
				// {Double.toString(mMarker.latitude),
				// Double.toString(mMarker.longitude)});
				// drawPathToGeoPoint(45.11, 11.34);
			}
		});
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	private Bitmap drawMarkerWithTitleAndAddress(String title, String add) {

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

	private class PagerAdapter extends FragmentPagerAdapter {

		// fragments to instantiate in the viewpager
		private List<Fragment> fragments;

		// constructor
		public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
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
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

}
