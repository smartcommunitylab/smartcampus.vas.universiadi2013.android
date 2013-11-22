package smartcampus.android.template.standalone.Activity.EventiBlock;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.Utilities.ElementDescRoute;
import smartcampus.android.template.standalone.Utilities.MapUtilities;
import smartcampus.android.template.standalone.Utilities.MapUtilities.ErrorType;
import smartcampus.android.template.standalone.Utilities.MapUtilities.ILocation;
import smartcampus.android.template.universiadi.R;
import android.annotation.SuppressLint;
import android.app.Dialog;
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

	private ArrayList<ElementDescRoute> mRouteText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_eventi);

		fromSearch = getIntent().getBooleanExtra("search", false);

		mMapUtilities = new MapUtilities(InfoEventi.this, InfoEventi.this);

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
							String title = null;
							String description = null;
							if (Locale.getDefault().getDisplayLanguage()
									.equalsIgnoreCase("it_IT")) {
								description = (obj.getJSONObject("customData")
										.getJSONObject("description").has("IT")) ? obj
										.getJSONObject("customData")
										.getJSONObject("description")
										.getString("IT")
										: obj.getJSONObject("customData")
												.getJSONObject("description")
												.getString("EN");
								title = (obj.getJSONObject("customData")
										.getJSONObject("title").has("IT")) ? obj
										.getJSONObject("customData")
										.getJSONObject("title").getString("IT")
										: obj.getJSONObject("customData")
												.getJSONObject("title")
												.getString("EN");
							} else {
								description = (obj.getJSONObject("customData")
										.getJSONObject("description").has("EN")) ? obj
										.getJSONObject("customData")
										.getJSONObject("description")
										.getString("EN")
										: obj.getJSONObject("customData")
												.getJSONObject("description")
												.getString("IT");
								title = (obj.getJSONObject("customData")
										.getJSONObject("title").has("EN")) ? obj
										.getJSONObject("customData")
										.getJSONObject("title").getString("EN")
										: obj.getJSONObject("customData")
												.getJSONObject("title")
												.getString("IT");
							}
							Evento evento = new Evento(
									null,
									title,
									obj.getLong("fromTime"),
									obj.getLong("fromTime"),
									obj.getLong("toTime"),
									Html.fromHtml(description).toString(),
									(!obj.isNull("location")) ? obj
											.getJSONArray("location")
											.getDouble(0) : 0,
									(!obj.isNull("location")) ? obj
											.getJSONArray("location")
											.getDouble(1) : 0,
									obj.getJSONObject("customData").getString(
											"category"),
									(!obj.getJSONObject("customData")
											.getString("imageUrl").equals("")) ? downloadImageFormURL(obj
											.getJSONObject("customData")
											.getString("imageUrl"))
											: new byte[1]);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				} else {
					mEvento = (android.smartcampus.template.standalone.Evento) (getIntent()
							.getSerializableExtra("evento"));
					mResult = new HashMap<String, Object>();
					mResult.put("connectionError", false);
					mResult.put("params", null);
				}

				if (mEvento.getLatGPS() != 0 && mEvento.getLngGPS() != 0) {
					mMarkerEvento = new LatLng(mEvento.getLatGPS(),
							mEvento.getLngGPS());
					mMarkerUser = new LatLng(mMapUtilities
							.getLastKnownLocation().getLatitude(),
							mMapUtilities.getLastKnownLocation().getLongitude());

					mRouteText = parseGoogleDescRoute(new double[] {
							mMarkerUser.latitude, mMarkerUser.longitude },
							new double[] { mMarkerEvento.latitude,
									mMarkerEvento.longitude });
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

					if (mEvento.getLatGPS() != 0 && mEvento.getLngGPS() != 0) {

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

							// mDesc.setText(parseGoogleDescRoute(descRoute.get()));
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

	private byte[] downloadImageFormURL(String url) {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(
					url).getContent());
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			return stream.toByteArray();
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

	private ArrayList<ElementDescRoute> parseGoogleDescRoute(
			double[] gpsSource, double[] gpsDest) {
		URL url;
		try {
			String srcGPS = gpsSource[0] + "," + gpsSource[1];
			String destGPS = gpsDest[0] + "," + gpsDest[1];

			String path = "http://maps.googleapis.com/maps/api/directions/json?origin="
					+ srcGPS
					+ "&destination="
					+ destGPS
					+ "&sensor=false&language="
					+ (Locale.getDefault().getDisplayLanguage()
							.equalsIgnoreCase("it_IT") ? "it" : "en");
			url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader(
					(conn.getInputStream())));

			String line = "";
			String output = "";
			System.out.println("Output from Server .... \n");
			while ((line = br.readLine()) != null) {
				output = output + line;
			}

			conn.disconnect();

			JSONObject object = new JSONObject(output);

			ArrayList<ElementDescRoute> result = new ArrayList<ElementDescRoute>();
			// Get routes
			JSONArray legs = object.getJSONArray("routes").getJSONObject(0)
					.getJSONArray("legs");
			JSONObject leg = legs.getJSONObject(0);
			result.add(new ElementDescRoute(leg.getJSONObject("distance")
					.getString("text"), leg.getJSONObject("duration")
					.getString("text"), leg.getString("end_address"), -1));

			JSONArray steps = leg.getJSONArray("steps");

			for (int j = 0; j < steps.length(); j++) {
				int img = -1;
				JSONObject step = steps.getJSONObject(j);
				try {
					String man = step.getString("maneuver");
					if (man.equals("turn-right"))
						img = R.drawable.turn_right;
					if (man.equals("turn-left"))
						img = R.drawable.turn_left;
					if (man.equals("merge"))
						img = R.drawable.enter;
					if (man.equals("ramp-right"))
						img = R.drawable.out_right;
					if (man.equals("ramp-left"))
						img = R.drawable.out_left;
					if (man.equals("fork-right"))
						img = R.drawable.turn_right_little;
					if (man.equals("fork-left"))
						img = R.drawable.turn_left_little;
				} catch (JSONException e) {
				}

				String desc = Html
						.fromHtml(step.getString("html_instructions"))
						.toString();
				desc.replace("\n", "");
				result.add(new ElementDescRoute(step.getJSONObject("distance")
						.getString("text"), step.getJSONObject("duration")
						.getString("text"), desc, img));
			}

			return result;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
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
