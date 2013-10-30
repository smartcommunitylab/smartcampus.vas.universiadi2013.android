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
import smartcampus.android.template.standalone.Utilities.MapUtilities;
import smartcampus.android.template.standalone.Utilities.MapUtilities.ErrorType;
import smartcampus.android.template.standalone.Utilities.MapUtilities.ILocation;
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
import android.util.TypedValue;
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

public class Booking extends FragmentActivity implements ILocation {

	private GoogleMap mMappa;
	private MapUtilities mMapUtilities;
	private ImageView mButtonPOI;
	private ArrayList<POI> mListaPOI;
	private ArrayList<POICategory> mListaFirstLevelCategory;
	private ArrayList<POICategory> mListaSecondLevelCategory;
	private ArrayList<MarkerOptions> mListaMarkerOptions = new ArrayList<MarkerOptions>();

	private POI poiForSearch;

	private Dialog dialogSelectPOI;

	private LatLngBounds bounds;

	private ListView mSelect;
	private ListArrayAdapter mAdapter;

	private int level;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking);

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
				if (level == 0) {
					TranslateAnimation anim = new TranslateAnimation(0, 0,
							-500, 0);
					anim.setDuration(300);
					anim.setFillAfter(true);
					mButtonPOI.startAnimation(anim);
					mButtonPOI.setVisibility(View.VISIBLE);
				} else if (level == 1) {
					dialogSelectPOI.show();

					mAdapter = new ListArrayAdapter(Booking.this,
							mListaFirstLevelCategory);
					mSelect.setAdapter(mAdapter);
					mAdapter.notifyDataSetChanged();
				}
				level--;
			}
		});

		setFirstLevelCategory();
		level = 0;

		mSelect = (ListView) dialogSelectPOI.findViewById(R.id.list_select);
		mAdapter = new ListArrayAdapter(Booking.this, mListaFirstLevelCategory);
		mSelect.setAdapter(mAdapter);

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

						level++;

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
								level = 1;
							}
						});

					}

					@Override
					protected Void doInBackground(Void... params) {
						// TODO Auto-generated method stub
						if (level == 1) {
							setSecondLevelCategory(mListaFirstLevelCategory
									.get(arg2).getWeight());
						} else { // level == 2
							mResult = ManagerData
									.getPOIForType(mListaSecondLevelCategory
											.get(arg2).getPrivateName());/*
																		 * mValues.
																		 * get
																		 * (arg2
																		 * ) .
																		 * getPrivateName
																		 * ());
																		 */
							if (!((Boolean) mResult.get("connectionError"))) {
								mListaPOI = (ArrayList<POI>) mResult
										.get("params");
								for (POI obj : mListaPOI) {
									mListaMarkerOptions.add(new MarkerOptions()
											.position(
													new LatLng(obj.getLatGPS(),
															obj.getLngGPS()))
											.title(obj.getIndirizzo())
											.snippet(
													obj.getCategoria() + "/"
															+ obj.getLatGPS()
															+ "-"
															+ obj.getLngGPS()));
								}
								setPOIsMap(mListaPOI);
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

						if (level == 1) {
							dialogSelectPOI.show();

							mAdapter = new ListArrayAdapter(Booking.this,
									mListaSecondLevelCategory);
							mSelect.setAdapter(mAdapter);
							mAdapter.notifyDataSetChanged();
						} else { // level == 2
							TranslateAnimation anim = new TranslateAnimation(0,
									0, -500, 0);
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
												// TODO Auto-generated method
												// stub
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
						}
						// END ONPOST
					}

				}.execute();
			}
		});

		mMappa = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mappa)).getMap();

		mMapUtilities = new MapUtilities(this, this);

		UiSettings mMapController = mMappa.getUiSettings();

		mMapController.setCompassEnabled(true);
		mMapController.setMyLocationButtonEnabled(false);
		mMapController.setZoomControlsEnabled(false);

		mMappa.setMyLocationEnabled(true);

		if (!(getIntent().getBooleanExtra("search", false))) {
			if (mMapUtilities.getLastKnownLocation() != null) {
				final LatLng mMyMarker = new LatLng(mMapUtilities
						.getLastKnownLocation().getLatitude(), mMapUtilities
						.getLastKnownLocation().getLongitude());
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
			setMapForSearch();
			// LatLng mMyMarker = null;
			// if (mMapUtilities.getLastKnownLocation() != null)
			// mMyMarker = new LatLng(mMapUtilities.getLastKnownLocation()
			// .getLatitude(), mMapUtilities.getLastKnownLocation()
			// .getLongitude());
			//
			// LatLngBounds.Builder builder = new LatLngBounds.Builder();
			//
			// builder.include(new LatLng(poiForSearch.getLatGPS(), poiForSearch
			// .getLngGPS()));
			//
			// if (mMyMarker != null)
			// builder.include(mMyMarker);
			// LatLngBounds bounds = builder.build();
			//
			// mMappa.animateCamera(CameraUpdateFactory
			// .newLatLngBounds(bounds, 50));
		}

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
						mAdapter = new ListArrayAdapter(Booking.this,
								mListaFirstLevelCategory);
						mSelect.setAdapter(mAdapter);
						mAdapter.notifyDataSetChanged();
						TranslateAnimation anim = new TranslateAnimation(0, 0,
								0, -500);
						anim.setDuration(500);
						anim.setFillAfter(true);
						mButtonPOI.startAnimation(anim);
						mButtonPOI.setVisibility(View.GONE);
					}
					level = 0;

					return true;
				}
				return false;
			}

		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (mMapUtilities != null)
			mMapUtilities.close();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		if (mMapUtilities == null)
			mMapUtilities = new MapUtilities(this, this);
	}

	private void setFirstLevelCategory() {
		mListaFirstLevelCategory = new ArrayList<Booking.POICategory>();
		mListaFirstLevelCategory.add(new POICategory(
				getString(R.string.CATEGORIA_SPORT), "sport", 100));
		mListaFirstLevelCategory.add(new POICategory(
				getString(R.string.CATEGORIA_TEMPOLIBERO), "tempolibero", 200));
		mListaFirstLevelCategory.add(new POICategory(
				getString(R.string.CATEGORIA_MANGIARE), "mangiare", 300));
		mListaFirstLevelCategory.add(new POICategory(
				getString(R.string.CATEGORIA_DORMIRE), "dormire", 400));
		mListaFirstLevelCategory.add(new POICategory(
				getString(R.string.CATEGORIA_MUOVERSI), "muoversi", 500));
		mListaFirstLevelCategory.add(new POICategory(
				getString(R.string.CATEGORIA_SERVIZI), "servizi", 600));
		mListaFirstLevelCategory.add(new POICategory(
				getString(R.string.CATEGORIA_COMPRARE), "comprare", 700));
	}

	private void setSecondLevelCategory(int weight) {
		mListaSecondLevelCategory = new ArrayList<Booking.POICategory>();
		switch (weight) {
		case 100:
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_NOLLEGGIO_SCI),
					"noleggioscii", 102));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_SCUOLA_SCI),
							"scuolasci", 104));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_IMPIANTO_SCII), "impiantosci",
					106));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_STADIO_GHIACCIO),
					"stadioghiaccio", 108));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_STADIO_SALTO), "stadiosalto",
					110));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_SNOWPARK), "snowpark", 112));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_CAMPO_SPORTIVO),
					"camposportivo", 114));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_CAMPO_TENNIS), "campotennis",
					116));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_MANEGGIO), "maneggio", 118));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_PISCINA), "piscina", 120));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_PALESTRA), "palestra", 122));
			break;
		case 200:
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_DISCO), "disco", 202));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_MINIGOLF), "minigolf", 204));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_WELLNESS), "wellness", 206));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_PARCO), "parco", 208));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_ATTRAZIONE),
							"attrazione", 210));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_MUSEO), "museo", 212));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_MONUMENTO), "monumento", 214));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_SITO_ARCHEOLOGICO),
					"sitoarcheologico", 216));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_TEATRO), "teatro", 218));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_LUOGO_CULTO), "luogoculto",
					220));
			break;
		case 300:
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_PIZZERIA), "pizzeria", 302));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_RISTORANTE),
							"ristorante", 304));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_AGRITURISMO), "agriturismo",
					306));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_MALGA), "malga", 308));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_BIRRERIA), "birreria", 310));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_BAR), "bar", 312));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_GELATERIA), "gelateria", 314));
			break;
		case 400:
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_HOTEL), "hotel", 402));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_RESIDENCE), "residence", 404));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_APPARTAMENTO_VACANZE),
					"appartamentovacanze", 406));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_AFFITTO_CAMERE),
					"affittocamere", 408));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_BED_BREAKFAST),
					"bedbreakfast", 410));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_GARNI), "garni", 412));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_RIFUGIO), "rifugio", 414));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_CAMPING), "camping", 416));
			break;
		case 500:
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_PARCHEGGIO),
							"parcheggio", 502));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_STAZIONE_SERVIZIO),
					"stazioneservizio", 504));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_TAXI), "taxi", 506));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_BUS_NAVETTA), "busnavetta",
					508));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_BUS_FERMATA), "busfermata",
					510));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_AUTOSTAZIONE), "autostazione",
					512));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_FUNIVIA), "funivia", 514));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_STAZIONE_TRENI),
					"stazionetreni", 516));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_SERVIZIO_BICI),
					"serviziobici", 518));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_NOLEGGIO_PRIVATO),
					"noleggioprivate", 520));
			break;
		case 600:
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_SERVIZI_SANITARI),
					"servizisanitari", 602));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_DENTISTA), "dentista", 604));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_VETERINARIO), "veterinario",
					606));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_FARMACIA), "farmacia", 608));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_IGIENICI), "igienici", 610));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_ISOLE_ECOLOGICHE),
					"isoleecologiche", 612));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_FONTANE), "fontane", 614));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_AUTOMOTO), "automoto", 616));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_SERVIZI_ABBIGLIAMENTO),
					"serviziabbigliamento", 618));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_PARRUCCHIERA), "parruchiera",
					620));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_ISTRUZIONE),
							"istruzione", 622));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_BIBLIOTECA),
							"biblioteca", 624));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_WIFI), "wifi", 626));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_AGENZIA_VIAGGI),
					"agenziaviaggi", 628));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_INFO), "info", 630));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_FORZE_ORDINE), "forzeordine",
					632));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_SERVIZI_MUNICIPALI),
					"servizimunicipali", 634));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_POSTA), "posta", 636));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_BANCA), "banca", 638));
			break;
		case 700:
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_PRODOTTI_TIPICI),
					"prodottitipici", 702));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_SOUVENIR), "souvenir", 704));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_MERCATO), "mercato", 706));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_ANTIQUARIATO), "antiquariato",
					708));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_CARTOLERIA),
							"cartoleria", 710));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_TABACCHINO),
							"tabacchino", 712));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_CENTROCOMMERCIALE),
					"centrocommerciale", 714));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_NEGOZIO_ANIMALI),
					"negozioanimali", 716));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_GIOCATTOLI),
							"giocattoli", 718));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_OTTICO), "ottico", 720));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_PROFUMERIA),
							"profumeria", 722));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_GIOIELLERIA), "gioielleria",
					724));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_ABBIGLIAMENTO),
					"abbigliamento", 726));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_NEGOZIO_SPORT),
					"negoziosport", 728));
			mListaSecondLevelCategory.add(new POICategory(
					getString(R.string.CATEGORIA_ELETTRONICA), "elettronica",
					730));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_FERRAMENTA),
							"ferramenta", 732));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_CASALINGHI),
							"casalinghi", 734));
			mListaSecondLevelCategory
					.add(new POICategory(
							getString(R.string.CATEGORIA_ALIMENTARI),
							"alimentari", 736));
			break;
		}
	}

	private void setPOIsMap(ArrayList<POI> list) {
		LatLng mMyMarker = null;
		if (mMapUtilities.getLastKnownLocation() != null)
			mMyMarker = new LatLng(mMapUtilities.getLastKnownLocation()
					.getLatitude(), mMapUtilities.getLastKnownLocation()
					.getLongitude());

		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (POI obj : list) {
			builder.include(new LatLng(obj.getLatGPS(), obj.getLngGPS()));
		}
		if (mMyMarker != null)
			builder.include(mMyMarker);
		bounds = builder.build();
	}

	private void setMapForSearch() {
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
						.getStringExtra("searchString"),
						getString(R.string.URL_POI_SEARCH));
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
			if (values.get(position).getPublicName().length() > 13)
				mSelect.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);

			ImageView mLogo = (ImageView) rowView.findViewById(R.id.image_logo);
			mLogo.setImageResource(R.drawable.game);

			return rowView;
		}
	}

	private class POICategory {
		private String publicName;
		private String privateName;
		private int weight;

		public POICategory(String publicName, String privateName, int weight) {
			super();
			this.publicName = publicName;
			this.privateName = privateName;
			this.weight = weight;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
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
