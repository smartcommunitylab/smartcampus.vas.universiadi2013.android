package smartcampus.android.template.standalone.Activity.EventiBlock;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import smartcampus.android.template.universiadi.R;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.Utilities.ElementDescRoute;
import smartcampus.android.template.standalone.Utilities.MapTextRoute;
import smartcampus.android.template.standalone.Utilities.MapUtilities;
import smartcampus.android.template.standalone.Utilities.MapUtilities.ErrorType;
import smartcampus.android.template.standalone.Utilities.MapUtilities.ILocation;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.DataSetObserver;
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
import android.smartcampus.template.standalone.Evento;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.Window;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
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

//import com.google.android.maps.GeoPoint;

@SuppressLint("ValidFragment")
public class InfoEventi extends FragmentActivity implements ILocation {

	private android.smartcampus.template.standalone.Evento mEvento;

	private LatLng mMarkerEvento;
	private LatLng mMarkerUser;
	private MapUtilities mMapUtilities;
	private GoogleMap mMappa;

	// private FontTextView mDesc;
	private PagerAdapter mAdapter;
	private ViewPager mPager;
	private Dialog dialog;

	private boolean fromSearch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_eventi);

		fromSearch = getIntent().getBooleanExtra("search", false);

		new AsyncTask<Void, Void, Void>() {

			private Map<String, Object> mResult;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(InfoEventi.this);
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
				if (fromSearch) {
					ArrayList<JSONObject> arrayJSON = null;
					mResult = ManagerData.getSearchForFilter(getIntent()
							.getStringExtra("searchString"),
							getString(R.string.URL_EVENTO_SEARCH));
					if (!((Boolean) mResult.get("connectionError"))) {
						arrayJSON = (ArrayList<JSONObject>) mResult
								.get("params");

						JSONObject obj = arrayJSON.get(getIntent().getIntExtra(
								"index", 0));
						try {
							mEvento = new Evento(
									null,
									obj.getString("title"),
									obj.getLong("fromTime"),
									Html.fromHtml(obj.getString("description"))
											.toString(),
									(!obj.isNull("location")) ? obj
											.getJSONArray("location")
											.getDouble(0) : 0,
									(!obj.isNull("location")) ? obj
											.getJSONArray("location")
											.getDouble(1) : 0,
									"Sport 1",
									downloadImageFormURL(obj.getJSONObject(
											"customData").getString("imageUrl")));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					mResult = ManagerData.getEventiForData(getIntent()
							.getLongExtra(
									"data",
									Calendar.getInstance(Locale.getDefault())
											.getTimeInMillis()));
					if (!((Boolean) mResult.get("connectionError"))) {
						ArrayList<android.smartcampus.template.standalone.Evento> mLista = (ArrayList<android.smartcampus.template.standalone.Evento>) mResult
								.get("params");
						mEvento = mLista.get(getIntent()
								.getIntExtra("index", 0));
					}
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				// START ONPOST

				if ((Boolean) mResult.get("connectionError")) {
					dialog.dismiss();
					Dialog noConnection = new Dialog(InfoEventi.this);
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

					mPager = (ViewPager) findViewById(R.id.pager_info_eventi);
					ArrayList<Fragment> listFrag = new ArrayList<Fragment>();
					listFrag.add(new PageInfoEventi(0, mEvento.getDescrizione()));
					mAdapter = new PagerAdapter(getSupportFragmentManager(),
							listFrag);
					mPager.setAdapter(mAdapter);

					mMappa = ((SupportMapFragment) getSupportFragmentManager()
							.findFragmentById(R.id.mappa)).getMap();

					UiSettings mMapController = mMappa.getUiSettings();

					mMapController.setCompassEnabled(true);
					mMapController.setMyLocationButtonEnabled(false);
					mMapController.setZoomControlsEnabled(false);

					mMappa.setMyLocationEnabled(true);

					mMapUtilities = new MapUtilities(InfoEventi.this,
							InfoEventi.this);

					if (mEvento.getLatGPS() != 0 && mEvento.getLngGPS() != 0) {
						mMarkerEvento = new LatLng(mEvento.getLatGPS(),
								mEvento.getLngGPS());
						mMarkerUser = new LatLng(mMapUtilities
								.getLastKnownLocation().getLatitude(),
								mMapUtilities.getLastKnownLocation()
										.getLongitude());
						Geocoder coder = new Geocoder(InfoEventi.this,
								Locale.getDefault());

						Address adrs;
						try {
							adrs = coder.getFromLocation(
									mMarkerEvento.latitude,
									mMarkerEvento.longitude, 1).get(0);
							mMappa.addMarker(new MarkerOptions()
									.position(mMarkerEvento)
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
									if (mMapUtilities.getLastKnownLocation() != null) {
										Intent intent = new Intent(
												android.content.Intent.ACTION_VIEW,
												Uri.parse("http://maps.google.com/maps?saddr="
														+ mMapUtilities
																.getLastKnownLocation()
																.getLatitude()
														+ ","
														+ mMapUtilities
																.getLastKnownLocation()
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

						if (mMarkerEvento != null) {

							LatLngBounds.Builder builder = new LatLngBounds.Builder();
							builder.include(mMarkerUser);
							builder.include(mMarkerEvento);
							LatLngBounds bounds = builder.build();
							mMappa.animateCamera(CameraUpdateFactory
									.newLatLngBounds(bounds, 50));

							MapTextRoute descRoute = new MapTextRoute();
							descRoute
									.execute(new String[] {
											(Double.toString(mMarkerUser.latitude)
													+ "-" + Double
													.toString(mMarkerUser.longitude)),
											(Double.toString(mMarkerEvento.latitude)
													+ "-" + Double
													.toString(mMarkerEvento.longitude)) });

							// mDesc.setText(parseGoogleDescRoute(descRoute.get()));
							ArrayList<ElementDescRoute> mRouteText = mEvento
									.getRouteTestuale(new double[] {
											mMarkerUser.latitude,
											mMarkerUser.longitude });
							mAdapter.fragments.add(new PageInfoEventi(1,
									mRouteText));
							mAdapter.notifyDataSetChanged();
						} else
							mMappa.animateCamera(CameraUpdateFactory
									.newLatLng(new LatLng(mMarkerUser.latitude,
											mMarkerUser.longitude)));

					}
				}
				// }

				dialog.dismiss();

				// END ONPOST
			}

		}.execute();
	}

	private Bitmap downloadImageFormURL(String url) {
		try {
			return BitmapFactory.decodeStream((InputStream) new URL(url)
					.getContent());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mMapUtilities != null)
			mMapUtilities.close();
	}

	@Override
	public void onLocationChaged(Location location) {
		// TODO Auto-generated method stub
		LatLng mMyMarker = new LatLng(location.getLatitude(),
				location.getLongitude());

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		if (mMarkerEvento != null)
			builder.include(mMarkerEvento);
		builder.include(mMyMarker);
		LatLngBounds bounds = builder.build();

		mMappa.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
		// mMappa.animateCamera(CameraUpdateFactory.newLatLng(new
		// LatLng(location
		// .getLatitude(), location.getLongitude())));
	}

	@Override
	public void onErrorOccured(ErrorType ex, String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, boolean isActive) {
		// TODO Auto-generated method stub

	}

	private class PagerAdapter extends FragmentPagerAdapter {

		// fragments to instantiate in the viewpager
		private List<Fragment> fragments;

		// constructor
		public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		@Override
		public void registerDataSetObserver(DataSetObserver observer) {
			// TODO Auto-generated method stub
			super.registerDataSetObserver(observer);
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
