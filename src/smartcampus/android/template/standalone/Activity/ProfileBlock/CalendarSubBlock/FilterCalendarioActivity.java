package smartcampus.android.template.standalone.Activity.ProfileBlock.CalendarSubBlock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import smartcampus.android.template.standalone.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class FilterCalendarioActivity extends Activity {

	private static String filterLuogo = null;
	private static String filterCategoria = null;
	private static long filterData = 0L;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendario_filter);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Avviso");
		builder.setMessage("Calendario in fase Beta\nPotrebbe risultare lento");
		builder.setCancelable(false);
		builder.setPositiveButton("Chiudi",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		builder.create().show();

		final ArrayList<Long> mSettimana = new ArrayList<Long>();
		for (int i = 0; i < 7; i++)
			mSettimana.add(Calendar.getInstance().getTimeInMillis()
					+ (i * 3600 * 24 * 1000));

		filterData = mSettimana.get(0);
		final Spinner mSpinnerData = (Spinner) findViewById(R.id.spinner_cal_data);
		mSpinnerData.setSelection(0, true);
		mSpinnerData.setAdapter(new SpinnerAdapter(this, mSettimana));
		mSpinnerData.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				filterData = mSettimana.get(arg2);

				// reloadCalendario();
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});

		final Spinner mSpinnerLuogo = (Spinner) findViewById(R.id.spinner_cal_luogo);
		mSpinnerLuogo.setSelection(0, true);
		final ArrayList<String> listLuogo = new ArrayList<String>();

		listLuogo.add("Tutti");
		for (int i = 0; i < 5; i++)
			listLuogo.add("Luogo " + (Integer.toString(i + 1)));

		mSpinnerLuogo.setAdapter(new SimpleSpinnerAdapter(
				getApplicationContext(), listLuogo));
		mSpinnerLuogo.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				filterLuogo = listLuogo.get(arg2);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}
		});

		final Spinner mSpinnerCategoria = (Spinner) findViewById(R.id.spinner_cal_categoria);
		mSpinnerCategoria.setSelection(0, true);

		final ArrayList<String> listCategoria = new ArrayList<String>();

		listCategoria.add("Tutti");
		for (int i = 0; i < 3; i++)
			listCategoria.add("Ambito " + (Integer.toString(i + 1)));

		mSpinnerCategoria.setAdapter(new SimpleSpinnerAdapter(
				getApplicationContext(), listCategoria));
		mSpinnerCategoria
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						filterCategoria = listCategoria.get(arg2);
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

							Intent mCaller = new Intent(
									getApplicationContext(), Calendario.class);
							mCaller.putExtra("data", filterData);
							mCaller.putExtra("luogo", !filterLuogo
									.equalsIgnoreCase("Tutti") ? filterLuogo
									: null);
							mCaller.putExtra(
									"categoria",
									!filterCategoria.equalsIgnoreCase("Tutti") ? filterCategoria
											: null);
							startActivity(mCaller);

							return true;
						}
						return false;
					}

				});

		((RelativeLayout) findViewById(R.id.btn_spinner_data))
				.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							((ImageView) findViewById(R.id.img_cal_data))
									.setImageResource(R.drawable.btn_cal_data_press);
							return true;
						}
						if (event.getAction() == MotionEvent.ACTION_UP) {
							((ImageView) findViewById(R.id.img_cal_data))
									.setImageResource(R.drawable.btn_cal_data);

							mSpinnerData.performClick();

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

			return rowView;
		}
	}

}
