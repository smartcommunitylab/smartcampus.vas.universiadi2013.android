package smartcampus.android.template.standalone.Activity.ProfileBlock.CalendarSubBlock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.Map;

import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import smartcampus.android.template.standalone.IntroBlock.UserConstant;
import smartcampus.android.template.universiadi.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class FilterCalendarioActivity extends Activity {

	private static String filterPersonale = null;
	private static FunzioneObj filterFunzione = null;
	private static long filterDataFrom = 0L;
	private static long filterDataTo = 0L;

	private Spinner mSpinnerLuogo;
	private Spinner mSpinnerCategoria;

	private SimpleSpinnerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendario_filter);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Avviso");
		builder.setMessage("Calendario in fase Beta\nPotrebbe risultare lento");
		builder.setCancelable(false);
		builder.setPositiveButton(getString(R.string.CHIUDI),
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();

						startCalendar();
					}
				});
		builder.create().show();
	}

	private void startCalendar() {
		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;
			private ArrayList<FunzioneObj> listCategoria;
			private ArrayList<FunzioneObj> listPersonalCategoria;
			private Map<String, Object> mMapResult;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(FilterCalendarioActivity.this);
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
				mMapResult = ManagerData.getCategorieVolontari();
				if (!(Boolean) (mMapResult.get("connectionError"))) {
					listCategoria = (ArrayList<FunzioneObj>) mMapResult
							.get("params");
					mMapResult = ManagerData.getFunzioneForUser(UserConstant
							.getUser());
					listPersonalCategoria = (ArrayList<FunzioneObj>) mMapResult
							.get("params");
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialog.dismiss();

				// START ONPOST

				final ArrayList<Long> mSettimana = new ArrayList<Long>();
				for (int i = 0; i < 7; i++)
					mSettimana.add(Calendar.getInstance().getTimeInMillis()
							+ (i * 3600 * 24 * 1000));

				filterDataFrom = mSettimana.get(0);
				final Spinner mSpinnerDataFrom = (Spinner) findViewById(R.id.spinner_cal_data_from);
				mSpinnerDataFrom.setSelection(0, true);
				mSpinnerDataFrom.setAdapter(new SpinnerAdapter(
						FilterCalendarioActivity.this, mSettimana));
				mSpinnerDataFrom
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								filterDataFrom = mSettimana.get(arg2);

								// reloadCalendario();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}

						});

				filterDataTo = mSettimana.get(0);
				final Spinner mSpinnerDataTo = (Spinner) findViewById(R.id.spinner_cal_data_to);
				mSpinnerDataTo.setSelection(0, true);
				mSpinnerDataTo.setAdapter(new SpinnerAdapter(
						FilterCalendarioActivity.this, mSettimana));
				mSpinnerDataTo
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								filterDataTo = mSettimana.get(arg2);

								// reloadCalendario();
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}

						});

				mSpinnerCategoria = (Spinner) findViewById(R.id.spinner_cal_categoria);
				mSpinnerCategoria.setSelection(0, true);
				filterFunzione = listCategoria.get(0);

				ArrayList<String> simplePersonalListCategoria = new ArrayList<String>();
				for (int i = 0; i < listPersonalCategoria.size(); i++)
					simplePersonalListCategoria.add(listPersonalCategoria
							.get(i).getFunzione());

				mAdapter = new SimpleSpinnerAdapter(getApplicationContext(),
						simplePersonalListCategoria);
				mSpinnerCategoria.setAdapter(mAdapter);
				mSpinnerCategoria
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								filterFunzione = listCategoria.get(arg2);
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}

						});

				mSpinnerLuogo = (Spinner) findViewById(R.id.spinner_cal_personale);
				mSpinnerLuogo.setSelection(0, true);
				final ArrayList<String> listPersonale = new ArrayList<String>();

				listPersonale.add("Turni personali");
				listPersonale.add("Turni tutto staff");

				filterPersonale = listPersonale.get(0);

				mSpinnerLuogo.setAdapter(new SimpleSpinnerAdapter(
						getApplicationContext(), listPersonale));
				mSpinnerLuogo
						.setOnItemSelectedListener(new OnItemSelectedListener() {

							@Override
							public void onItemSelected(AdapterView<?> arg0,
									View arg1, int arg2, long arg3) {
								// TODO Auto-generated method stub
								filterPersonale = listPersonale.get(arg2);
								if (filterPersonale
										.equalsIgnoreCase("Turni personali")) {
									ArrayList<String> simplePersonalListCategoria = new ArrayList<String>();
									for (int i = 0; i < listPersonalCategoria
											.size(); i++)
										simplePersonalListCategoria
												.add(listPersonalCategoria.get(
														i).getFunzione());

									mAdapter = new SimpleSpinnerAdapter(
											getApplicationContext(),
											simplePersonalListCategoria);
									mSpinnerCategoria.setAdapter(mAdapter);
									mAdapter.notifyDataSetChanged();
								} else {
									ArrayList<String> simpleListCategoria = new ArrayList<String>();
									for (int i = 0; i < listCategoria.size(); i++)
										simpleListCategoria.add(listCategoria
												.get(i).getFunzione());

									mAdapter = new SimpleSpinnerAdapter(
											getApplicationContext(),
											simpleListCategoria);
									mSpinnerCategoria.setAdapter(mAdapter);
									mAdapter.notifyDataSetChanged();
								}
							}

							@Override
							public void onNothingSelected(AdapterView<?> arg0) {
								// TODO Auto-generated method stub

							}
						});

				((ImageView) findViewById(R.id.btn_calendario))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View arg0, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) findViewById(R.id.btn_calendario))
											.setImageResource(R.drawable.btn_cal_show_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) findViewById(R.id.btn_calendario))
											.setImageResource(R.drawable.btn_cal_show);

									if (filterDataFrom > filterDataTo) {
										AlertDialog.Builder builder = new AlertDialog.Builder(
												FilterCalendarioActivity.this);
										builder.setTitle("Avviso");
										builder.setMessage("Data iniziale maggiore della data finale");
										builder.setCancelable(false);
										builder.setPositiveButton(
												getString(R.string.CHIUDI),
												new android.content.DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														dialog.dismiss();

														startCalendar();
													}
												});
										builder.create().show();
									} else {
										Intent mCaller = new Intent(
												getApplicationContext(),
												Calendario.class);
										mCaller.putExtra("dataFrom",
												filterDataFrom);
										mCaller.putExtra("dataTo", filterDataTo);
										mCaller.putExtra("personale",
												filterPersonale);
										mCaller.putExtra("funzione",
												filterFunzione);
										startActivity(mCaller);
									}

									return true;
								}
								return false;
							}

						});

				((RelativeLayout) findViewById(R.id.btn_spinner_data_from))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) findViewById(R.id.img_cal_data_from))
											.setImageResource(R.drawable.btn_cal_data_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) findViewById(R.id.img_cal_data_from))
											.setImageResource(R.drawable.btn_cal_data);

									mSpinnerDataFrom.performClick();

									return true;
								}
								return false;
							}

						});

				((RelativeLayout) findViewById(R.id.btn_spinner_data_to))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) findViewById(R.id.img_cal_data_to))
											.setImageResource(R.drawable.btn_cal_data_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) findViewById(R.id.img_cal_data_to))
											.setImageResource(R.drawable.btn_cal_data);

									mSpinnerDataTo.performClick();

									return true;
								}
								return false;
							}

						});

				((RelativeLayout) findViewById(R.id.btn_spinner_luogo))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) findViewById(R.id.img_cal_luogo))
											.setImageResource(R.drawable.btn_cal_luogo_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) findViewById(R.id.img_cal_luogo))
											.setImageResource(R.drawable.btn_cal_luogo);

									mSpinnerLuogo.performClick();

									return true;
								}
								return false;
							}

						});

				((RelativeLayout) findViewById(R.id.btn_spinner_categoria))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) findViewById(R.id.img_cal_categoria))
											.setImageResource(R.drawable.btn_cal_categoria_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) findViewById(R.id.img_cal_categoria))
											.setImageResource(R.drawable.btn_cal_categoria);

									mSpinnerCategoria.performClick();

									return true;
								}
								return false;
							}

						});

				// END ONPOST
			}

		}.execute();
	}

	private class SpinnerAdapter extends ArrayAdapter<Long> {

		private Context context;
		private ArrayList<Long> values;

		public SpinnerAdapter(Context context, ArrayList<Long> values) {
			super(context, R.layout.row_spinner, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return getCustomView(position, convertView, parent, 1);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			return getCustomView(position, convertView, parent, 0);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent, int style) {
			// TODO Auto-generated method stub
			// return super.getView(position, convertView, parent);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = null;

			SimpleDateFormat dateFormatter = new SimpleDateFormat("EEEE-dd.MM",
					Locale.getDefault());
			String dateString = dateFormatter.format(values.get(position));
			String[] dateTokenized = dateString.split("-");

			if (style == 0) {
				rowView = inflater.inflate(android.R.layout.simple_list_item_1,
						parent, false);
				((TextView) rowView.findViewById(android.R.id.text1))
						.setText(dateTokenized[0] + " " + dateTokenized[1]);
				((TextView) rowView.findViewById(android.R.id.text1))
						.setTextColor(Color.WHITE);
				((TextView) rowView.findViewById(android.R.id.text1))
						.setTypeface(Typeface.createFromAsset(
								getApplicationContext().getAssets(),
								"PatuaOne-Regular.otf"));
			}
			if (style == 1) {
				rowView = inflater.inflate(R.layout.row_spinner, parent, false);
				((TextView) rowView.findViewById(R.id.text_giorno))
						.setText(dateTokenized[0]);
				((TextView) rowView.findViewById(R.id.text_giorno))
						.setTextColor(Color.parseColor("#3294ad"));
				((TextView) rowView.findViewById(R.id.text_giorno))
						.setTypeface(Typeface.createFromAsset(
								getApplicationContext().getAssets(),
								"PatuaOne-Regular.otf"));
				((TextView) rowView.findViewById(R.id.text_data))
						.setText(dateTokenized[1]);
				((TextView) rowView.findViewById(R.id.text_data))
						.setTextColor(Color.parseColor("#3294ad"));
				((TextView) rowView.findViewById(R.id.text_data))
						.setTypeface(Typeface.createFromAsset(
								getApplicationContext().getAssets(),
								"PatuaOne-Regular.otf"));
			}

			return rowView;
		}
	}

	private class SimpleSpinnerAdapter extends ArrayAdapter<String> {

		private Context context;
		private ArrayList<String> values;

		public SimpleSpinnerAdapter(Context context, ArrayList<String> values) {
			super(context, android.R.layout.simple_list_item_1, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		@Override
		public View getDropDownView(int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub
			return getCustomView(position, convertView, parent,
					Color.parseColor("#3294ad"));
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			return getCustomView(position, convertView, parent, Color.WHITE);
		}

		public View getCustomView(int position, View convertView,
				ViewGroup parent, int color) {
			// TODO Auto-generated method stub
			// return super.getView(position, convertView, parent);
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(
					android.R.layout.simple_list_item_1, parent, false);
			((TextView) rowView.findViewById(android.R.id.text1))
					.setText(values.get(position));
			((TextView) rowView.findViewById(android.R.id.text1))
					.setTextColor(color);
			((TextView) rowView.findViewById(android.R.id.text1))
					.setTypeface(Typeface.createFromAsset(
							getApplicationContext().getAssets(),
							"PatuaOne-Regular.otf"));
			if (values.get(position).length() > 13)
				((TextView) rowView.findViewById(android.R.id.text1))
						.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

			return rowView;
		}
	}

}
