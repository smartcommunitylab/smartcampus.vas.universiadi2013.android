package smartcampus.android.template.standalone.Activity.ProfileBlock.RisolutoreSubBlock;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationClient;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

public class Problema extends Activity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		OnConnectionFailedListener {

	private LocationClient mLocationClient;

	private EditText mTextIndirizzo;

	private static int CAMERA_REQUEST = 1000;

	private ArrayList<String> mCategorie;

	// private PushServiceActivity mNotification;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_problema);

		ContainerTicket.getInstance();

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(Problema.this);
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
				mCategorie = ManagerData.getCategorieVolontari();
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialog.dismiss();

				// START ONPOST

				mTextIndirizzo = (EditText) findViewById(R.id.text_indirizzo_problema);
				mTextIndirizzo.setTypeface(Typeface.createFromAsset(
						getApplicationContext().getAssets(),
						"PatuaOne-Regular.otf"));

				((RelativeLayout) findViewById(R.id.btn_here))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) findViewById(R.id.image_helper_location))
											.setImageResource(R.drawable.btn_helper_location_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) findViewById(R.id.image_helper_location))
											.setImageResource(R.drawable.btn_helper_location);

									mLocationClient = new LocationClient(
											getApplicationContext(),
											Problema.this, Problema.this);
									mLocationClient.connect();
									return true;
								}
								return false;
							}
						});

				((EditText) findViewById(R.id.text_descrizione_problema))
						.setTypeface(Typeface.createFromAsset(
								getApplicationContext().getAssets(),
								"PatuaOne-Regular.otf"));

				final Spinner mSpinner = (Spinner) findViewById(R.id.spinner_categoria);
				mSpinner.setAdapter(new SpinnerAdapter(Problema.this,
						mCategorie));

				((RelativeLayout) findViewById(R.id.btn_open_spinner))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) findViewById(R.id.image_helper_categoria))
											.setImageResource(R.drawable.btn_helper_category_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) findViewById(R.id.image_helper_categoria))
											.setImageResource(R.drawable.btn_helper_category);

									mSpinner.performClick();

									return true;
								}
								return false;
							}

						});

				((ImageView) findViewById(R.id.button_pick_photo))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) findViewById(R.id.button_pick_photo))
											.setImageResource(R.drawable.btn_helper_pic_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) findViewById(R.id.button_pick_photo))
											.setImageResource(R.drawable.btn_helper_pic);

									Intent cameraIntent = new Intent(
											android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
									startActivityForResult(cameraIntent,
											CAMERA_REQUEST);

									return true;
								}
								return false;
							}

						});

				((ImageView) findViewById(R.id.go_forward))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) findViewById(R.id.go_forward))
											.setImageResource(R.drawable.btn_helper_send_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) findViewById(R.id.go_forward))
											.setImageResource(R.drawable.btn_helper_send);

									String indirizzo = mTextIndirizzo.getText()
											.toString();
									String desc = ((EditText) findViewById(R.id.text_descrizione_problema))
											.getText().toString();

									if (!indirizzo.equalsIgnoreCase("")) {
										ContainerTicket.setmAddress(indirizzo);

										if (!desc.equalsIgnoreCase("")) {
											ContainerTicket.setmDesc(desc);
											ContainerTicket.setmCategoria(mCategorie.get(mSpinner
													.getSelectedItemPosition()));
											// startActivity(new Intent(
											// getApplicationContext(),
											// MainActivity.class));
											new ChooseChannel(Problema.this);
										} else
											callError("Inserire una descrizione");
									} else
										callError("Inserire un indirizzo");

									return true;
								}
								return false;
							}
						});

				// END ONPOST
			}

		}.execute();

	}

	private void callError(String error) {
		AlertDialog.Builder builder = new AlertDialog.Builder(Problema.this);
		builder.setTitle("Errore");
		builder.setMessage(error);
		builder.setCancelable(false);
		builder.setPositiveButton("OK",
				new android.content.DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		if (mLocationClient.getLastLocation() != null) {
			try {
				Geocoder mGeoCoder = new Geocoder(getApplicationContext(),
						Locale.getDefault());
				Address address = mGeoCoder.getFromLocation(
						mLocationClient.getLastLocation().getLatitude(),
						mLocationClient.getLastLocation().getLongitude(), 1)
						.get(0);
				String extendedAddress = address.getAddressLine(0);
				for (int i = 1; i < address.getMaxAddressLineIndex(); i++)
					extendedAddress = extendedAddress + ", "
							+ address.getAddressLine(i);
				extendedAddress = extendedAddress + ", "
						+ address.getCountryName();
				((EditText) findViewById(R.id.text_indirizzo_problema))
						.setText(extendedAddress);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			final Bitmap photo = (Bitmap) data.getExtras().get("data");
			final Dialog dialog = new Dialog(Problema.this);
			dialog.setCancelable(true);
			dialog.setContentView(R.layout.dialog_image);
			dialog.setTitle("Image");
			ImageView image = (ImageView) dialog.findViewById(R.id.image);
			image.setImageBitmap(photo);

			((Button) dialog.findViewById(R.id.button_ok))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							((TextView) findViewById(R.id.text_photo_save))
									.setText("Foto salvata");
							((TextView) findViewById(R.id.text_photo_save))
									.setTextColor(Color.GREEN);

							ContainerTicket.setmPhoto(photo);

							dialog.dismiss();
						}
					});

			((Button) dialog.findViewById(R.id.button_delete))
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (ContainerTicket.getmPhoto() == null) {
								((TextView) findViewById(R.id.text_photo_save))
										.setText("Scatta una foto");
								((TextView) findViewById(R.id.text_photo_save))
										.setTextColor(Color.RED);
							}

							dialog.dismiss();
						}
					});

			dialog.show();
		}
	}

	private class SpinnerAdapter extends ArrayAdapter<String> {

		private Context context;
		private ArrayList<String> values;

		public SpinnerAdapter(Context context, ArrayList<String> values) {
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
