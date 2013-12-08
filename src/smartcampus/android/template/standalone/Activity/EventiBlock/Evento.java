package smartcampus.android.template.standalone.Activity.EventiBlock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import eu.trentorise.smartcampus.universiade.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Evento extends Activity {

	private ListView mListaEventi;
	private ListArrayAdapter mAdapter;
	private LinearLayout mContainer;

	private ArrayList<android.smartcampus.template.standalone.Evento> mLista;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evento);

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;
			private Map<String, Object> mResult;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(Evento.this);
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
				mResult = ManagerData.getEventiForData(Calendar.getInstance(
						Locale.getDefault()).getTimeInMillis());
				if (!((Boolean) mResult.get("connectionError")))
					mLista = (ArrayList<android.smartcampus.template.standalone.Evento>) mResult
							.get("params");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialog.dismiss();

				// START ONPOST

				if ((Boolean) mResult.get("connectionError")) {
					Dialog noConnection = new Dialog(Evento.this);
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
					mContainer = (LinearLayout) findViewById(R.id.container_scroll_date);

					Calendar cal = Calendar.getInstance(Locale.getDefault());
					cal.set(2013, Calendar.DECEMBER, 21, 0, 0);
					long date = cal.getTimeInMillis();
					cal.set(2013, Calendar.DECEMBER,
							cal.get(Calendar.DAY_OF_WEEK_IN_MONTH) + 1, 0, 0);
					long now = cal.getTimeInMillis();
					int daysLeft = (int) ((date - now) / (3600 * 24 * 1000));

					for (int i = 0; i < daysLeft; i++) {
						Calendar mCal = Calendar.getInstance();
						mCal.setTimeInMillis(Calendar.getInstance()
								.getTimeInMillis() + 86400000 * i);

						RelativeLayout mCell = new RelativeLayout(Evento.this);
						mCell.setTag(mCal.getTimeInMillis());
						mCell.setLayoutParams(new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));

						final ImageView mImgCella = new ImageView(Evento.this);
						mImgCella
								.setImageResource((i == 0) ? R.drawable.cell_event_press
										: R.drawable.cell_event);
						mImgCella.setTag(-4);
						mImgCella.setLayoutParams(new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
						mCell.addView(mImgCella);

						FontTextView mData = new FontTextView(Evento.this);
						mData.setTag(-3);
						mData.setText(Integer.toString(mCal.get(Calendar.DATE)));
						RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
						params.addRule(RelativeLayout.CENTER_HORIZONTAL);
						params.setMargins(0, (int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 7, getResources()
										.getDisplayMetrics()), 0, 0);
						mData.setLayoutParams(params);
						mData.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 45);
						mData.setTextColor((i == 0) ? Color
								.parseColor("#FFFFFF") : Color.argb(255, 50,
								147, 172));

						mCell.addView(mData);

						FontTextView mGiorno = new FontTextView(Evento.this);
						mGiorno.setTag(-2);
						SimpleDateFormat format = new SimpleDateFormat("MMMM");
						String formattedDate = format.format(mCal.getTime());
						mGiorno.setText(formattedDate.substring(0, 1)
								.toUpperCase() + formattedDate.substring(1));
						params = new RelativeLayout.LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT);
						params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
						params.addRule(RelativeLayout.CENTER_HORIZONTAL);
						params.setMargins(0, (int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50, getResources()
										.getDisplayMetrics()), 0, 0);
						mGiorno.setLayoutParams(params);
						mGiorno.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
						mGiorno.setTextColor((i == 0) ? Color
								.parseColor("#FFFFFF") : Color.argb(255, 50,
								147, 172));

						mCell.addView(mGiorno);

						mContainer.addView(mCell);

						mCell.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(final View v,
									MotionEvent arg1) {
								// TODO Auto-generated method stub
								if (arg1.getAction() == MotionEvent.ACTION_DOWN) {

									return true;
								}
								if (arg1.getAction() == MotionEvent.ACTION_UP) {

									for (int i = 0; i < mContainer
											.getChildCount(); i++) {
										if ((Long) (mContainer.getChildAt(i)
												.getTag()) != v.getTag()) {
											((ImageView) (((RelativeLayout) (mContainer
													.getChildAt(i)))
													.findViewWithTag(-4)))
													.setImageResource(R.drawable.cell_event);
											((TextView) (((RelativeLayout) (mContainer
													.getChildAt(i)))
													.findViewWithTag(-2))
													.findViewWithTag(-2)).setTextColor(Color
													.parseColor("#3293AC"));
											((TextView) (((RelativeLayout) (mContainer
													.getChildAt(i)))
													.findViewWithTag(-3))
													.findViewWithTag(-3)).setTextColor(Color
													.parseColor("#3293AC"));
										} else {
											((ImageView) v.findViewWithTag(-4))
													.setImageResource(R.drawable.cell_event_press);
											((TextView) v.findViewWithTag(-2)).setTextColor(Color
													.parseColor("#FFFFFF"));
											((TextView) v.findViewWithTag(-3)).setTextColor(Color
													.parseColor("#FFFFFF"));
										}
									}

									new AsyncTask<Void, Void, Void>() {
										private Dialog dialog;
										private Map<String, Object> mResult;

										@Override
										protected void onPreExecute() {
											// TODO Auto-generated method
											// stub
											super.onPreExecute();

											dialog = new Dialog(Evento.this);
											dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
											dialog.setContentView(R.layout.dialog_wait);
											dialog.getWindow()
													.setBackgroundDrawableResource(
															R.drawable.dialog_rounded_corner_light_black);
											dialog.show();
											dialog.setCancelable(true);
											dialog.setOnCancelListener(new OnCancelListener() {

												@Override
												public void onCancel(
														DialogInterface dialog) {
													// TODO Auto-generated
													// method
													// stub
													Log.i("", "Cancel");
													cancel(true);
												}
											});

										}

										@Override
										protected Void doInBackground(
												Void... params) {
											// TODO Auto-generated method
											// stub
											mResult = ManagerData
													.getEventiForData((Long) v
															.getTag());
											if (!((Boolean) mResult
													.get("connectionError")))
												mLista = (ArrayList<android.smartcampus.template.standalone.Evento>) mResult
														.get("params");
											return null;
										}

										@Override
										protected void onPostExecute(Void result) {
											// TODO Auto-generated method
											// stub
											super.onPostExecute(result);

											dialog.dismiss();

											// START ONPOST

											if ((Boolean) mResult
													.get("connectionError")) {
												Dialog noConnection = new Dialog(
														Evento.this);
												noConnection
														.requestWindowFeature(Window.FEATURE_NO_TITLE);
												noConnection
														.setContentView(R.layout.dialog_no_connection);
												noConnection
														.getWindow()
														.setBackgroundDrawableResource(
																R.drawable.dialog_rounded_corner_light_black);
												noConnection.show();
												noConnection
														.setCancelable(true);
												noConnection
														.setOnCancelListener(new OnCancelListener() {

															@Override
															public void onCancel(
																	DialogInterface dialog) {
																// TODO
																// Auto-generated
																// method
																// stub
																finish();
															}
														});
											} else {
												if (mLista.size() != 0) {
													((TextView) findViewById(R.id.text_nessun_evento))
															.setVisibility(View.GONE);
													mAdapter.clear();
													mAdapter = new ListArrayAdapter(
															getApplicationContext(),
															mLista);
													mAdapter.notifyDataSetChanged();
													mListaEventi
															.setAdapter(mAdapter);
												} else {
													((TextView) findViewById(R.id.text_nessun_evento))
															.setVisibility(View.VISIBLE);
													mAdapter.clear();
												}
											}
											// END ONPOST
										}

									}.execute();

									return true;
								}
								return true;
							}
						});
					}
					mListaEventi = (ListView) findViewById(R.id.list_eventi);
					mAdapter = new ListArrayAdapter(getApplicationContext(),
							mLista);
					mListaEventi.setAdapter(mAdapter);

					mListaEventi
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									Intent mCaller = new Intent(arg1
											.getContext(), InfoEventi.class);
									mCaller.putExtra("evento", mLista.get(arg2));
									startActivity(mCaller);
								}
							});

					if (mLista.size() != 0) {
						((TextView) findViewById(R.id.text_nessun_evento))
								.setVisibility(View.GONE);
					} else
						((TextView) findViewById(R.id.text_nessun_evento))
								.setVisibility(View.VISIBLE);
				}
				// END ONPOST
			}

		}.execute();
	}

	private class ListArrayAdapter extends
			ArrayAdapter<android.smartcampus.template.standalone.Evento> {

		private Context context;
		private ArrayList<android.smartcampus.template.standalone.Evento> values;

		public ListArrayAdapter(Context context,
				ArrayList<android.smartcampus.template.standalone.Evento> values) {
			super(context, R.layout.row_eventi, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = new View(context);

			if (values.size() != 0) {
				rowView = inflater.inflate(R.layout.row_eventi, parent, false);

				((FontTextView) rowView.findViewById(R.id.text_orario))
						.setText(new SimpleDateFormat("HH:mm", Locale
								.getDefault()).format(values.get(position)
								.getOraInizio())
								+ " - "
								+ new SimpleDateFormat("HH:mm", Locale
										.getDefault()).format(values.get(
										position).getOraFine()));

				((FontTextView) rowView.findViewById(R.id.text_nome_evento))
						.setText(values.get(position).getNome().toUpperCase());

				if (values.get(position).getNome().length() > 30)
					((FontTextView) rowView.findViewById(R.id.text_nome_evento))
							.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				else if (values.get(position).getNome().length() > 15)
					((FontTextView) rowView.findViewById(R.id.text_nome_evento))
							.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

				((FontTextView) rowView.findViewById(R.id.text_tipo_gara))
						.setText(values.get(position).getTipoSport());

				((ImageView) rowView.findViewById(R.id.image_logo_row_eventi))
						.setImageBitmap((values.get(position).getImage() == null) ? BitmapFactory
								.decodeResource(getResources(),
										R.drawable.img_event_list)
								: BitmapFactory.decodeByteArray(
										values.get(position).getImage(), 0,
										values.get(position).getImage().length));
			}
			return rowView;
		}
	}

	// private class DownloadEventiForGiorni extends AsyncTask<Void, Void,
	// Map<String, List<?>>>
	// {
	// private ListView mList;
	// private Date mData;
	//
	// public DownloadEventiForGiorni(ListView mPrimaLista, Date data)
	// {
	// mList = mPrimaLista;
	// mData = data;
	//
	// }
	//
	// @Override
	// protected Map<String, List<?>> doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// DiscoverTrentoConnector service = new DiscoverTrentoConnector
	// ("https://vas-dev.smartcampuslab.it");
	// Thread.currentThread().setContextClassLoader(EventObject.class.getClassLoader());
	// ObjectFilter filter = new ObjectFilter();
	// filter.setClassName(EventObject.class.getCanonicalName());
	// filter.setFromTime(mData.getTime());
	// Calendar mCal = Calendar.getInstance();
	// mCal.setTime(mData);
	// Log.i("Data", mCal.get(Calendar.DATE)+"/"+mCal.get(Calendar.MONTH)+1+
	// "/"+mCal.get(Calendar.YEAR));
	// filter.setToTime(mCal.getTimeInMillis()+86400000);
	// try {
	// return service.getObjects(filter, Intro.mToken);
	// } catch (DiscoverTrentoConnectorException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(Map<String, List<?>> result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	//
	// Collection<List<?>> a = result.values();
	// Iterator<List<?>> it = a.iterator();
	// ArrayList<EventObject> mResult = new ArrayList<EventObject>();
	//
	// while(it.hasNext())
	// {
	// List<?> tmp = it.next();
	// for (int i=0; i<tmp.size(); i++)
	// {
	// mResult.add((EventObject)tmp.get(i));
	// }
	// }
	// mList.setAdapter(new ListArrayAdapter(getApplicationContext(), mResult));
	// }
	//
	// }

}
