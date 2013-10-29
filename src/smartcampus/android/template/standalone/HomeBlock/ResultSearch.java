package smartcampus.android.template.standalone.HomeBlock;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import smartcampus.android.template.universiadi.R;
import smartcampus.android.template.standalone.Activity.EventiBlock.InfoEventi;
import smartcampus.android.template.standalone.Activity.FacilitiesBlock.Booking;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.Activity.SportBlock.DettaglioSport;
import smartcampus.android.template.standalone.Activity.SportBlock.SportImageConstant;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.smartcampus.template.standalone.Evento;
import android.smartcampus.template.standalone.POI;
import android.smartcampus.template.standalone.Sport;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ResultSearch extends Activity {

	private ArrayList<JSONObject> mListaObj;
	private ArrayList<Evento> mListaEvento = new ArrayList<Evento>();
	private ArrayList<POI> mListaPOI = new ArrayList<POI>();
	private ArrayList<Sport> mListaSport = new ArrayList<Sport>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result_search);

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;
			private Map<String, Object> mResult;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(ResultSearch.this);
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
				mResult = ManagerData.getSearchForFilter(getIntent()
						.getStringExtra("search"),
						getIntent().getStringExtra("rest"));
				if (!((Boolean) mResult.get("connectionError"))) {
					mListaObj = (ArrayList<JSONObject>) mResult.get("params");
					try {
						if (getIntent().getStringExtra("rest")
								.equalsIgnoreCase("/evento/search")) {
							for (JSONObject obj : mListaObj) {
								Evento evento = new Evento(null,
										obj.getString("title"),
										obj.getLong("fromTime"),
										obj.getString("description"),
										(!obj.isNull("location")) ? obj
												.getJSONArray("location")
												.getDouble(0) : 0,
										(!obj.isNull("location")) ? obj
												.getJSONArray("location")
												.getDouble(1) : 0, "Sport 1",
										downloadImageFormURL(obj.getJSONObject(
												"customData").getString(
												"imageUrl")));

								mListaEvento.add(evento);
							}
						} else if (getIntent().getStringExtra("rest")
								.equalsIgnoreCase("/poi/search")) {
							for (JSONObject obj : mListaObj) {
								POI poi = new POI(null, obj.getString("nome"),
										obj.getString("categoria"), obj
												.getJSONObject("GPS")
												.getDouble("latGPS"), obj
												.getJSONObject("GPS")
												.getDouble("lngGPS"));
								poi.getIndirizzo();
								mListaPOI.add(poi);
							}
						} else if (getIntent().getStringExtra("rest")
								.equalsIgnoreCase("/sport/search")) {
							for (JSONObject obj : mListaObj) {
								Sport sport = new Sport(obj.getString("nome"),
										SportImageConstant.resourcesFromID(
												obj.getInt("foto"),
												ResultSearch.this),
										obj.getString("descrizione"));

								mListaSport.add(sport);
							}
						}
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
					Dialog noConnection = new Dialog(ResultSearch.this);
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
					if (getIntent().getStringExtra("rest").equalsIgnoreCase(
							"/evento/search")) {

						if (mListaEvento.size() != 0) {
							((ListView) findViewById(R.id.lista_risultati_search))
									.setAdapter(new RowSearch<Evento>(
											getApplicationContext(),
											mListaEvento));
							((ListView) findViewById(R.id.lista_risultati_search))
									.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											// TODO Auto-generated method stub

											Intent mCaller = new Intent(arg1
													.getContext(),
													InfoEventi.class);
											mCaller.putExtra("search", true);
											mCaller.putExtra(
													"searchString",
													getIntent().getStringExtra(
															"search"));
											mCaller.putExtra("data",
													mListaEvento.get(arg2)
															.getData());
											mCaller.putExtra("index", arg2);
											startActivity(mCaller);
										}
									});
						} else
							((TextView) findViewById(R.id.text_nessun_risultato))
									.setVisibility(View.VISIBLE);

					} else if (getIntent().getStringExtra("rest")
							.equalsIgnoreCase("/poi/search")) {

						if (mListaPOI.size() != 0) {
							((ListView) findViewById(R.id.lista_risultati_search))
									.setAdapter(new RowSearch<POI>(
											getApplicationContext(), mListaPOI));
							((ListView) findViewById(R.id.lista_risultati_search))
									.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											// TODO Auto-generated method stub
											Intent mCaller = new Intent(arg1
													.getContext(),
													Booking.class);
											mCaller.putExtra("search", true);
											mCaller.putExtra(
													"searchString",
													getIntent().getStringExtra(
															"search"));
											mCaller.putExtra("index", arg2);
											startActivity(mCaller);
										}
									});
						} else
							((TextView) findViewById(R.id.text_nessun_risultato))
									.setVisibility(View.VISIBLE);

					} else if (getIntent().getStringExtra("rest")
							.equalsIgnoreCase("/sport/search")) {

						if (mListaSport.size() != 0) {
							((ListView) findViewById(R.id.lista_risultati_search))
									.setAdapter(new RowSearch<Sport>(
											getApplicationContext(),
											mListaSport));
							((ListView) findViewById(R.id.lista_risultati_search))
									.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											// TODO Auto-generated method stub
											Intent mCaller = new Intent(arg1
													.getContext(),
													DettaglioSport.class);
											mCaller.putExtra("sport",
													mListaSport.get(arg2)
															.getNome());
											mCaller.putExtra("search", true);
											startActivity(mCaller);
										}
									});
						} else
							((TextView) findViewById(R.id.text_nessun_risultato))
									.setVisibility(View.VISIBLE);

					}
				}
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

	public class RowSearch<E> extends ArrayAdapter<E> {
		private final Context context;
		private final ArrayList<E> values;

		public RowSearch(Context context, ArrayList<E> values) {
			super(context, R.layout.row_eventi, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = null;

			if (getIntent().getStringExtra("rest").equalsIgnoreCase(
					"/evento/search")) {
				if (values.size() != 0) {
					rowView = inflater.inflate(R.layout.row_eventi, parent,
							false);
					Evento evento = (Evento) values.get(position);

					((FontTextView) rowView.findViewById(R.id.text_orario))
							.setText(new SimpleDateFormat("hh:mm", Locale
									.getDefault()).format(evento.getData()));

					if (evento.getNome().length() <= 12)
						((FontTextView) rowView
								.findViewById(R.id.text_nome_evento))
								.setText(evento.getNome().toUpperCase());
					else
						((FontTextView) rowView
								.findViewById(R.id.text_nome_evento))
								.setText(evento.getNome().toUpperCase()
										.substring(0, 11)
										+ "...");
				}
			} else if (getIntent().getStringExtra("rest").equalsIgnoreCase(
					"/poi/search")) {
				if (values.size() != 0) {
					rowView = inflater.inflate(R.layout.row_poi, parent, false);
					POI poi = (POI) values.get(position);

					((FontTextView) rowView
							.findViewById(R.id.text_indirizzo_poi_search))
							.setText(poi.getIndirizzo());
					((FontTextView) rowView
							.findViewById(R.id.text_categoria_poi_search))
							.setText(poi.getCategoria());
				}
			} else if (getIntent().getStringExtra("rest").equalsIgnoreCase(
					"/sport/search")) {
				if (values.size() != 0) {
					rowView = inflater.inflate(R.layout.row_sport, parent,
							false);
					Sport sport = (Sport) values.get(position);

					((FontTextView) rowView
							.findViewById(R.id.text_nome_sport_search))
							.setText(sport.getNome());
					((ImageView) rowView.findViewById(R.id.image_sport_search))
							.setImageBitmap(BitmapFactory.decodeByteArray(
									sport.getFoto(), 0, sport.getFoto().length));
				}
			}

			return rowView;
		}
	}
}
