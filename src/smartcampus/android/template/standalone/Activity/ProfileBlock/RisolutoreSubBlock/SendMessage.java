package smartcampus.android.template.standalone.Activity.ProfileBlock.RisolutoreSubBlock;

import java.util.ArrayList;

import smartcampus.android.template.universiadi.R;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.smartcampus.template.standalone.Utente;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SendMessage {

	private ArrayList<Utente> mVolontari;
	private ArrayList<Utente> mVolontariSelected;
	private String mType;

	private Dialog dialog;

	public SendMessage(final Activity mThis, final String type) {
		mType = type;

		dialog = new Dialog(mThis);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.activity_sms);
		dialog.getWindow().setBackgroundDrawableResource(
				R.drawable.dialog_rounded_corner);
		dialog.show();

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialogWait;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialogWait = new Dialog(mThis);
				dialogWait.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialogWait.setContentView(R.layout.dialog_wait);
				dialogWait.getWindow().setBackgroundDrawableResource(
						R.drawable.dialog_rounded_corner_light_black);
				dialogWait.show();
				dialogWait.setCancelable(true);
				dialogWait.setOnCancelListener(new OnCancelListener() {

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
				if (type.equalsIgnoreCase("sms"))
					mVolontari = ManagerData.getUserForCategoriaAndAmbito(
							ContainerTicket.getmCategoria(), null);
				else
					mVolontari = ManagerData.getUserForCategoriaAndAmbito(
							ContainerTicket.getmCategoria(), "0");
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialogWait.dismiss();

				// START ONPOST

				mVolontariSelected = new ArrayList<Utente>();

				((ListView) dialog.findViewById(R.id.lista_volontari))
						.setAdapter(new RowVolontari(mThis, mVolontari));

				((ImageView) dialog.findViewById(R.id.button_send))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) dialog
											.findViewById(R.id.button_send))
											.setImageResource(R.drawable.btn_send_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									((ImageView) dialog
											.findViewById(R.id.button_send))
											.setImageResource(R.drawable.btn_send);

									if (mType.equalsIgnoreCase("sms")) {
										for (Utente utente : mVolontariSelected)
											Notification.sendNotification(
													mThis,
													"Invio sms a "
															+ utente.getNome()
															+ " al numero +"
															+ utente.getNumeroTelefonico());

										if (mVolontariSelected.size() == 0)
											Notification
													.sendNotification(mThis,
															"Nessun volontario selezionato");
									} else {
										for (Utente utente : mVolontari)
											Notification.sendNotification(
													mThis, "Invio notifica a "
															+ utente.getNome());
									}

									dialog.dismiss();

									return true;
								}
								return false;
							}

						});

				if (mType.equalsIgnoreCase("sms"))
					((ImageView) dialog.findViewById(R.id.button_send))
							.setImageResource(R.drawable.btn_send_disable);
				else
					((ImageView) dialog.findViewById(R.id.button_send))
							.setImageResource(R.drawable.btn_send);

				// END ONPOST
			}

		}.execute();
	}

	public class RowVolontari extends ArrayAdapter<Utente> {
		private final Context context;
		private final ArrayList<Utente> values;
		private boolean[] checked;

		public RowVolontari(Context context, ArrayList<Utente> values) {
			super(context, R.layout.row_seleziona_volontari, values);
			this.context = context;
			this.values = values;

			checked = new boolean[values.size()];
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			final View rowView;
			if (mType.equalsIgnoreCase("sms")) {
				rowView = inflater.inflate(R.layout.row_seleziona_volontari,
						parent, false);

				((TextView) rowView.findViewById(R.id.text_anagrafiche_sms))
						.setText(values.get(position).getNome());
				((TextView) rowView.findViewById(R.id.text_categoria_sms))
						.setText("Categoria: "
								+ values.get(position).getAmbito());
				((TextView) rowView.findViewById(R.id.text_ruolo_sms))
						.setText("Ruolo: " + values.get(position).getRuolo());

				((ImageView) rowView.findViewById(R.id.img_check_send))
						.setOnTouchListener(new OnTouchListener() {

							@Override
							public boolean onTouch(View v, MotionEvent event) {
								// TODO Auto-generated method stub
								if (event.getAction() == MotionEvent.ACTION_DOWN) {
									((ImageView) rowView
											.findViewById(R.id.img_check_send))
											.setImageResource(R.drawable.btn_check_press);
									return true;
								}
								if (event.getAction() == MotionEvent.ACTION_UP) {
									if (checked[position]) {
										mVolontariSelected.remove(mVolontariSelected
												.indexOf(mVolontari
														.get(position)));
										((ImageView) rowView
												.findViewById(R.id.img_check_send))
												.setImageResource(R.drawable.btn_check);
									} else {
										mVolontariSelected.add(mVolontari
												.get(position));
										((ImageView) rowView
												.findViewById(R.id.img_check_send))
												.setImageResource(R.drawable.btn_check_press);
									}

									checked[position] = !checked[position];

									if (mVolontariSelected.size() != 0)
										((ImageView) dialog
												.findViewById(R.id.button_send))
												.setImageResource(R.drawable.btn_send);
									else
										((ImageView) dialog
												.findViewById(R.id.button_send))
												.setImageResource(R.drawable.btn_send_disable);

									return true;
								}
								return false;
							}

						});
			} else {
				rowView = inflater.inflate(R.layout.row_volontari, parent,
						false);

				((TextView) rowView
						.findViewById(R.id.text_anagrafiche_volontario))
						.setText(values.get(position).getNome());
				((TextView) rowView
						.findViewById(R.id.text_categoria_volontario))
						.setText("Categoria: "
								+ values.get(position).getAmbito());
				((TextView) rowView.findViewById(R.id.text_ruolo_volontario))
						.setText("Ruolo: " + values.get(position).getRuolo());
			}

			return rowView;
		}
	}
}
