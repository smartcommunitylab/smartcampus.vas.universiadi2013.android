package smartcampus.android.template.standalone.Activity.FacilitiesBlock;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import smartcampus.android.template.universiadi.R;
import smartcampus.android.template.standalone.Activity.EventiBlock.Evento;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.smartcampus.template.standalone.POI;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnCameraChangeListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Booking extends FragmentActivity implements LocationListener,
		GooglePlayServicesClient.ConnectionCallbacks,
		OnConnectionFailedListener {

	private GoogleMap mMappa;
	private ImageView mButtonPOI;
	private LocationManager locationManager;
	private LocationClient mLocationClient;
	private ArrayList<POI> mListaPOI;
	private ArrayList<MarkerOptions> mListaMarkerOptions = new ArrayList<MarkerOptions>();

	private POI poiForSearch;

	private Dialog dialogSelectPOI;

	private LatLngBounds bounds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking);

		final ArrayList<POICategory> mValues = new ArrayList<POICategory>();
		mValues.add(new POICategory("Stadio del Ghiaccio", "stadioghiaccio"));
		mValues.add(new POICategory("Impianto Scii", "impiantosci"));
		mValues.add(new POICategory("Snowpark", "snowpark"));
		mValues.add(new POICategory("Stadio di salto", "stadiosalto"));

		dialogSelectPOI = new Dialog(Booking.this);
		dialogSelectPOI.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialogSelectPOI.setContentView(R.layout.activity_select_booking);
		dialogSelectPOI.getWindow().setBackgroundDrawableResource(
				R.drawable.dialog_rounded_corner);
		dialogSelectPOI.setCancelable(true);
		dialogSelectPOI.setOnCancelListener(new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
				TranslateAnimation anim = new TranslateAnimation(0, 0, -500, 0);
				anim.setDuration(300);
				anim.setFillAfter(true);
				mButtonPOI.startAnimation(anim);
				mButtonPOI.setVisibility(View.VISIBLE);
			}
		});

		ListView mSelect = (ListView) dialogSelectPOI
				.findViewById(R.id.list_select);
		mSelect.setAdapter(new ListArrayAdapter(Booking.this, mValues));

		mSelect.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// TODO Auto-generated method stub
				new AsyncTask<Void, Void, Void>() {
					private Dialog dialog;
					private Map<String, Object> mResult;

					@Override
					protected void onPreExecute() {
						// TODO Auto-generated method stub
						super.onPreExecute();
						dialogSelectPOI.dismiss();

						dialog = new Dialog(Booking.this);
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
								dialogSelectPOI.show();
							}
						});

					}

					@Override
					protected Void doInBackground(Void... params) {
						// TODO Auto-generated method stub
						mResult = ManagerData.getPOIForType(mValues.get(arg2)
								.getPrivateName());
						if (!((Boolean) mResult.get("connectionError"))) {
							mListaPOI = (ArrayList<POI>) mResult.get("params");
							for (POI obj : mListaPOI) {
								mListaMarkerOptions.add(new MarkerOptions()
										.position(
												new LatLng(obj.getLatGPS(), obj
														.getLngGPS()))
										.title(obj.getIndirizzo())
										.snippet(
												obj.getCategoria() + "/"
														+ obj.getLatGPS() + "-"
														+ obj.getLngGPS()));
							}
							setPOIsMap(mListaPOI);
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						// TODO Auto-generated method stub
						super.onPostExecute(result);

						dialog.dismiss();

						// START ONPOST

						TranslateAnimation anim = new TranslateAnimation(0, 0,
								-500, 0);
						anim.setDuration(300);
						anim.setFillAfter(true);
						mButtonPOI.startAnimation(anim);
						mButtonPOI.setVisibility(View.VISIBLE);

						if ((Boolean) mResult.get("connectionError")) {
							Dialog noConnection = new Dialog(Booking.this);
							noConnection
									.requestWindowFeature(Window.FEATURE_NO_TITLE);
							noConnection
									.setContentView(R.layout.dialog_no_connection);
							noConnection
									.getWindow()
									.setBackgroundDrawableResource(
											R.drawable.dialog_rounded_corner_light_black);
							noConnection.show();
							noConnection.setCancelable(true);
							noConnection
									.setOnCancelListener(new OnCancelListener() {

										@Override
										public void onCancel(
												DialogInterface dialog) {
											// TODO Auto-generated method stub
											finish();
										}
									});
						} else {
							mMappa.clear();
							for (MarkerOptions option : mListaMarkerOptions)
								mMappa.addMarker(option);

							mMappa.animateCamera(CameraUpdateFactory
									.newLatLngBounds(bounds, 50));
						}
						// END ONPOST
					}

				}.execute();
			}
		});

		mMappa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mappa)).getMap();

		UiSettings mMapController = mMappa.getUiSettings();

		mMapController.setCompassEnabled(true);
		mMapController.setMyLocationButtonEnabled(false);
		mMapController.setZoomControlsEnabled(false);

		mMappa.setMyLocationEnabled(true);
		mMappa.setOnMarkerClickListener(new OnMarkerClickListener() {

			@Override
			public boolean onMarkerClick(final Marker marker) {
				// TODO Auto-generated method stub
				final Dialog dialog = new Dialog(Booking.this);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_dettaglio_poi);
				dialog.getWindow().setBackgroundDrawableResource(
						R.drawable.dialog_rounded_corner);

				((TextView) dialog.findViewById(R.id.text_indirizzo_poi))
						.setText("Indirizzo: " + marker.getTitle() + "\n");
				((TextView) dialog.findViewById(R.id.text_categoria_poi))
						.setText("Categoria: "
								+ marker.getSnippet().split("/")[0] + "\n");
				((ImageView) dialog.findViewById(R.id.btn_go_event))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) dialog
											.findViewById(R.id.btn_go_event))
											.setImageResource(R.drawable.btn_eventi_poi_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) dialog
											.findViewById(R.id.btn_go_event))
											.setImageResource(R.drawable.btn_eventi_poi);

									dialog.dismiss();
									Intent mCaller = new Intent(Booking.this,
											DettaglioEventoPerPOI.class);
									mCaller.putExtra("latGPS",
											Double.parseDouble(marker
													.getSnippet().split("/")[1]
													.split("-")[0]));
									mCaller.putExtra("lngGPS",
											Double.parseDouble(marker
													.getSnippet().split("/")[1]
													.split("-")[1]));
									startActivity(mCaller);
									return true;
								}
								return false;
							}
						});

				dialog.show();

				return true;
			}
		});

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
					// if
					// (getSupportFragmentManager().findFragmentByTag("select")
					// == null) {
					// TranslateAnimation anim = new TranslateAnimation(0, 0,
					// 0, -500);
					// anim.setDuration(500);
					// anim.setFillAfter(true);
					// mButtonPOI.startAnimation(anim);
					// mButtonPOI.setVisibility(View.GONE);
					//
					// FragmentManager fragmentManager =
					// getSupportFragmentManager();
					// FragmentTransaction fragmentTransaction = fragmentManager
					// .beginTransaction();
					// fragmentTransaction
					// .setCustomAnimations(R.anim.slide_in_bottom,
					// R.anim.slide_out_bottom,
					// R.anim.slide_in_bottom,
					// R.anim.slide_out_bottom);
					// fragmentTransaction.addToBackStack(null);
					// fragmentTransaction.add(R.id.container_select,
					// new SelectBooking(fragmentManager, mButtonPOI,
					// mMappa, mLocationClient), "select");
					// fragmentTransaction.commit();
					// } else {
					// getSupportFragmentManager().popBackStack();
					//
					// TranslateAnimation anim = new TranslateAnimation(0, 0,
					// -500, 0);
					// anim.setDuration(300);
					// anim.setFillAfter(true);
					// mButtonPOI.startAnimation(anim);
					// mButtonPOI.setVisibility(View.VISIBLE);
					// }
					if (dialogSelectPOI.isShowing()) {
						dialogSelectPOI.dismiss();
						TranslateAnimation anim = new TranslateAnimation(0, 0,
								-500, 0);
						anim.setDuration(300);
						anim.setFillAfter(true);
						mButtonPOI.startAnimation(anim);
						mButtonPOI.setVisibility(View.VISIBLE);
					} else {
						dialogSelectPOI.show();
						TranslateAnimation anim = new TranslateAnimation(0, 0,
								0, -500);
						anim.setDuration(500);
						anim.setFillAfter(true);
						mButtonPOI.startAnimation(anim);
						mButtonPOI.setVisibility(View.GONE);
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
					.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

			if (networkIsEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 100, this);
			} else if (gpsIsEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.PASSIVE_PROVIDER, 0, 100, this);
			}
		}

		if (getIntent().getBooleanExtra("search", false))
			setMapForSearch(getIntent().getStringExtra("searchString"));
		else {
			mLocationClient = new LocationClient(this, this, this);
			mLocationClient.connect();
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
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		locationManager.removeUpdates(this);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (locationManager != null) {
			boolean gpsIsEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);
			boolean networkIsEnabled = locationManager
					.isProviderEnabled(LocationManager.PASSIVE_PROVIDER);

			if (networkIsEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.GPS_PROVIDER, 0, 100, this);
			} else if (gpsIsEnabled) {
				locationManager.requestLocationUpdates(
						LocationManager.PASSIVE_PROVIDER, 0, 100, this);
			}
		}
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		if (!(getIntent().getBooleanExtra("search", false))) {
			if (mLocationClient.getLastLocation() != null) {
				final LatLng mMyMarker = new LatLng(mLocationClient
						.getLastLocation().getLatitude(), mLocationClient
						.getLastLocation().getLongitude());
				if (mMyMarker != null) {
					mMappa.setOnCameraChangeListener(new OnCameraChangeListener() {

						@Override
						public void onCameraChange(CameraPosition position) {
							// TODO Auto-generated method stub
							mMappa.animateCamera(CameraUpdateFactory
									.newLatLngZoom(mMyMarker, 1));
							mMappa.setOnCameraChangeListener(null);
						}
					});
				}
			}
		} else {
			LatLng mMyMarker = null;
			if (mLocationClient.getLastLocation() != null)
				mMyMarker = new LatLng(mLocationClient.getLastLocation()
						.getLatitude(), mLocationClient.getLastLocation()
						.getLongitude());

			LatLngBounds.Builder builder = new LatLngBounds.Builder();

			builder.include(new LatLng(poiForSearch.getLatGPS(), poiForSearch
					.getLngGPS()));

			if (mMyMarker != null)
				builder.include(mMyMarker);
			LatLngBounds bounds = builder.build();

			mMappa.animateCamera(CameraUpdateFactory
					.newLatLngBounds(bounds, 50));
		}
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

	private void setPOIsMap(ArrayList<POI> list) {
		LatLng mMyMarker = null;
		if (mLocationClient.getLastLocation() != null)
			mMyMarker = new LatLng(mLocationClient.getLastLocation()
					.getLatitude(), mLocationClient.getLastLocation()
					.getLongitude());

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (POI obj : list) {
			builder.include(new LatLng(obj.getLatGPS(), obj.getLngGPS()));
		}
		if (mMyMarker != null)
			builder.include(mMyMarker);
		bounds = builder.build();
	}

	private void setMapForSearch(String string) {
		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;
			private ArrayList<JSONObject> mListaPOI;
			private Map<String, Object> mResult;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(Booking.this);
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
						Log.i("", "Cancel");
						cancel(true);
					}
				});

			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				mResult = ManagerData.getSearchForFilter(getIntent()
						.getStringExtra("searchString"), "/search/poi");
				if (!((Boolean) mResult.get("connectionError"))) {
					mListaPOI = (ArrayList<JSONObject>) mResult.get("params");
					JSONObject obj = mListaPOI.get(getIntent().getIntExtra(
							"index", 0));
					try {
						poiForSearch = new POI(null, obj.getString("nome"),
								obj.getString("categoria"), obj.getJSONObject(
										"GPS").getDouble("latGPS"), obj
										.getJSONObject("GPS").getDouble(
												"lngGPS"));
						poiForSearch.getIndirizzo();

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialog.dismiss();

				// START ONPOST
				if ((Boolean) mResult.get("connectionError")) {
					Dialog noConnection = new Dialog(Booking.this);
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
					mMappa.addMarker(new MarkerOptions()
							.position(
									new LatLng(poiForSearch.getLatGPS(),
											poiForSearch.getLngGPS()))
							.title(poiForSearch.getIndirizzo())
							.snippet(
									poiForSearch.getCategoria() + "/"
											+ poiForSearch.getLatGPS() + "-"
											+ poiForSearch.getLngGPS()));
					mLocationClient = new LocationClient(Booking.this,
							Booking.this, Booking.this);
					mLocationClient.connect();
				}
				// END ONPOST
			}

		}.execute();
	}

	private class ListArrayAdapter extends ArrayAdapter<POICategory> {

		private final Context context;
		private final ArrayList<POICategory> values;

		public ListArrayAdapter(Context context, ArrayList<POICategory> values) {
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
			mSelect.setText(values.get(position).getPublicName());

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

	private class POICategory {
		private String publicName;
		private String privateName;

		public POICategory(String publicName, String privateName) {
			super();
			this.publicName = publicName;
			this.privateName = privateName;
		}

		public String getPublicName() {
			return publicName;
		}

		public void setPublicName(String publicName) {
			this.publicName = publicName;
		}

		public String getPrivateName() {
			return privateName;
		}

		public void setPrivateName(String privateName) {
			this.privateName = privateName;
		}
	}
}
