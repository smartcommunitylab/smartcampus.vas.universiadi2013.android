package smartcampus.android.template.standalone.Activity.SportBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.Utilities.MapUtilities;
import smartcampus.android.template.standalone.Utilities.MapUtilities.ErrorType;
import smartcampus.android.template.standalone.Utilities.MapUtilities.ILocation;
import smartcampus.android.template.universiadi.R;
import android.app.Dialog;
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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.smartcampus.template.standalone.Evento;
import android.smartcampus.template.standalone.POI;
import android.smartcampus.template.standalone.Sport;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
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

public class DettaglioSport extends FragmentActivity implements ILocation {

	private android.smartcampus.template.standalone.Sport mSport;
	private GoogleMap mMappa;
	private MapUtilities mMapUtilities;

	private ArrayList<Evento> mListEventi;
	private ArrayList<POI> mListPOI;
	ArrayList<Fragment> fragment = new ArrayList<Fragment>();
	LatLngBounds.Builder builder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dettaglio_sport);

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;
			private Map<String, Object> mResult;

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
				mResult = ManagerData.getSport();
				if (!((Boolean) mResult.get("connectionError"))) {
					ArrayList<android.smartcampus.template.standalone.Sport> mListaSport = (ArrayList<Sport>) mResult
							.get("params");

					if (getIntent().getBooleanExtra("search", false)) {
						for (Sport sport : mListaSport) {
							if (sport.getNome().equalsIgnoreCase(
									getIntent().getStringExtra("sport"))) {
								mSport = sport;
								break;
							}
						}
					} else {
						mSport = (Sport) getIntent().getSerializableExtra(
								"sport");
					}

					mResult = ManagerData.getEventiPerSport(mSport.getNome());
					mListEventi = (ArrayList<Evento>) mResult.get("params");
					mListPOI = mSport.getSportPOI();

					for (int i = 0; i < 4; i++)
						fragment.add(new PageInfoSport(mSport.getDescrizione(),
								mListEventi, mSport.getAtleti(), mSport
										.getSpecialita(), i));
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				// START ONPOST
				dialog.dismiss();

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

					ViewPager mPagerSport = (ViewPager) findViewById(R.id.pager_info_eventi);
					mPagerSport.setAdapter(new PagerAdapter(
							getSupportFragmentManager(), fragment));

					mMappa = ((SupportMapFragment) getSupportFragmentManager()
							.findFragmentById(R.id.mappa)).getMap();
					mMapUtilities = new MapUtilities(DettaglioSport.this,
							DettaglioSport.this);

					UiSettings mMapController = mMappa.getUiSettings();

					mMapController.setCompassEnabled(true);
					mMapController.setMyLocationButtonEnabled(false);
					mMapController.setZoomControlsEnabled(false);

					mMappa.setMyLocationEnabled(true);

					builder = new LatLngBounds.Builder();
					if (mMapUtilities.getLastKnownLocation() != null)
						builder.include(new LatLng(mMapUtilities
								.getLastKnownLocation().getLatitude(),
								mMapUtilities.getLastKnownLocation()
										.getLongitude()));

					for (POI mPOI : mListPOI) {
						LatLng mMarker = new LatLng(mPOI.getLatGPS(),
								mPOI.getLngGPS());
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
													mPOI.getNome(),
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
					}
					mMappa.animateCamera(CameraUpdateFactory.newLatLngBounds(
							builder.build(), 50));
					// END ONPOST
				}
			}

		}.execute();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (mMapUtilities != null)
			mMapUtilities.close();
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

	@Override
	public void onLocationChaged(Location l) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onErrorOccured(ErrorType ex, String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, boolean isActive) {
		// TODO Auto-generated method stub

	}

}
