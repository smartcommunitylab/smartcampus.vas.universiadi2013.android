package smartcampus.android.template.standalone.Activity.ProfileBlock.CalendarSubBlock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import eu.trentorise.smartcampus.universiade.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.smartcampus.template.standalone.Turno;
import android.smartcampus.template.standalone.Utente;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

public class Calendario extends Activity implements ScrollViewListener {

	private ArrayList<ScrollView> mAllAScrollView;
	private ArrayList<Turno> listTurni;
	private ExtendedTurno[] listaOrari;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calendario);

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;
			private Map<String, Object> mResult;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(Calendario.this);
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
				// mResult = ManagerData.getTurniForDataAndLuogoAndCategoria(
				// getIntent().getLongExtra("data", 0), getIntent()
				// .getStringExtra("luogo"), getIntent()
				// .getStringExtra("categoria"));
				mResult = ManagerData.getTurniForDataAndFunzione(getIntent()
						.getLongExtra("dataFrom", 0),
						getIntent().getLongExtra("dataTo", 0), getIntent()
								.getStringExtra("personale"),
						(FunzioneObj) (getIntent()
								.getSerializableExtra("funzione")));
				if (!((Boolean) mResult.get("connectionError")))
					listTurni = (ArrayList<Turno>) mResult.get("params");
				listaOrari = parseTurno(listTurni);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialog.dismiss();

				// START ONPOST

				if ((Boolean) mResult.get("connectionError")) {
					Dialog noConnection = new Dialog(Calendario.this);
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
					LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
					LinearLayout mContainerOra = (LinearLayout) findViewById(R.id.container_lista_ora);
					for (int i = 0; i < 21; i++) {
						int time = 800 + 50 * i;
						View rowOra = inflater.inflate(R.layout.row_ora, null,
								false);
						if (time % 100 == 0) {
							((TextView) rowOra.findViewById(R.id.text_ora))
									.setText(" " + (time / 100) + ":00 ");
						} else {
							((TextView) rowOra.findViewById(R.id.text_ora))
									.setText(" " + (time / 100) + ":30 ");
						}
						rowOra.setLayoutParams(new LayoutParams(
								LayoutParams.MATCH_PARENT,
								LayoutParams.MATCH_PARENT, 0.5f));
						mContainerOra.addView(rowOra);
					}

					mAllAScrollView = new ArrayList<ScrollView>();
					ObservableScrollView mScroll = (ObservableScrollView) findViewById(R.id.scrollView2);
					mScroll.setScrollViewListener(Calendario.this);
					mAllAScrollView.add(mScroll);

					ArrayList<String> mListaGiorni = getListaGiorni();
					ArrayList<View> list = new ArrayList<View>();

					for (int i = 0; i < mListaGiorni.size(); i++) // # giorni
					{

						View base = createBaseColoumn(mListaGiorni.get(i),
								listaOrari);
						list.add(base);

					}

					String[] funzioneTokenized = ((FunzioneObj) (getIntent()
							.getSerializableExtra("funzione"))).getFunzione()
							.split(":");
					View complex = createComplexColoumn(
							funzioneTokenized[funzioneTokenized.length - 1],
							list);
					complex.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.MATCH_PARENT, 0.5f));

					LinearLayout mContainer = (LinearLayout) findViewById(R.id.container_complex_coloumn);
					mContainer.addView(complex);

				}

				// END ONPOST
			}

		}.execute();

	}

	private ArrayList<String> getListaGiorni() {
		ArrayList<String> mReturn = new ArrayList<String>();
		SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE dd.MM",
				Locale.getDefault());
		Long date = getIntent().getLongExtra("dataFrom", 0);
		while (date <= getIntent().getLongExtra("dataTo", 0)) {
			mReturn.add(dateFormatter.format(date));
			date = date + (3600 * 1000 * 24);
		}
		return mReturn;
	}

	private View createBaseColoumn(String data, ExtendedTurno[] list) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View coloumn = inflater.inflate(R.layout.base_coloumn, null, false);

		((TextView) coloumn.findViewById(R.id.text_categoria)).setText(data);

		ObservableScrollView mScroll = (ObservableScrollView) coloumn
				.findViewById(R.id.scrollView1);
		mAllAScrollView.add(mScroll);
		mScroll.setScrollViewListener(this);

		LinearLayout mListaTurni = (LinearLayout) coloumn
				.findViewById(R.id.list_turni);

		int indexTurno = -1;
		int indexColor = 0;

		Random random = new Random();
		int color = Color.rgb(random.nextInt(255), random.nextInt(255),
				random.nextInt(255));

		for (final ExtendedTurno turno : list) {
			int indexTurnoTmp = turno.getNumberTurno();

			View row = inflater.inflate(R.layout.row_turno, null, false);
			if (indexTurnoTmp == -1) {
				((TextView) row.findViewById(R.id.text_turno))
						.setTextColor(Color.parseColor("#00FFFFFF"));
				if (indexColor % 2 == 0) {
					row.setBackgroundColor(Color.WHITE);
				} else {
					row.setBackgroundColor(Color.parseColor("#1E3294ad"));
				}
				indexColor++;
			} else {
				((TextView) row.findViewById(R.id.text_turno))
						.setTextColor(Color.parseColor("#FFFFFF"));
				if (indexTurnoTmp != indexTurno) {
					color = Color.rgb(random.nextInt(255), random.nextInt(255),
							random.nextInt(255));
					((TextView) row.findViewById(R.id.text_turno))
							.setText(turno.getNumeroVolontari() + " volontari");
				} else {
					((TextView) row.findViewById(R.id.text_turno))
							.setTextColor(color);
				}

				row.setBackgroundColor(color);
				row.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (turno.getVolontari() != null) {
							final Dialog dialog = new Dialog(Calendario.this);
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.dialog_turno);
							dialog.getWindow().setBackgroundDrawableResource(
									R.drawable.dialog_rounded_corner);

							((ListView) dialog
									.findViewById(R.id.lista_volontari))
									.setAdapter(new RowVolontario(
											getApplicationContext(), turno
													.getVolontari()));
							((ListView) dialog
									.findViewById(R.id.lista_volontari))
									.setOnItemClickListener(new OnItemClickListener() {

										@Override
										public void onItemClick(
												AdapterView<?> arg0, View arg1,
												int arg2, long arg3) {
											// TODO Auto-generated method stub
											final Utente mUtente = turno
													.getVolontari().get(arg2);

											AlertDialog.Builder builder = new AlertDialog.Builder(
													Calendario.this);
											builder.setTitle("Contatto");
											builder.setMessage("Vuoi chiamare "
													+ mUtente.getNome()
													+ " al numero "
													+ mUtente
															.getNumeroTelefonico());
											builder.setCancelable(false);
											builder.setPositiveButton(
													"Chiama",
													new android.content.DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {
															dialog.dismiss();
															Intent intent = new Intent(
																	Intent.ACTION_CALL);
															intent.setData(Uri.parse("tel:"
																	+ mUtente
																			.getNumeroTelefonico()));
															startActivityForResult(
																	intent, 0);
														}
													});
											builder.setNegativeButton(
													"Chiudi",
													new android.content.DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {
															dialog.dismiss();
														}
													});
											builder.create().show();
										}
									});

							dialog.show();
						}
					}
				});
			}

			indexTurno = indexTurnoTmp;

			// if (orari.contains(tmp)/* orariContainsOrario(orari, tmp) */) {
			// } else {
			// row.setBackgroundColor(Color.WHITE);
			// ((TextView) row.findViewById(R.id.text_turno))
			// .setTextColor(Color.WHITE);
			// }
			row.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, 0.5f));
			mListaTurni.addView(row);
		}

		coloumn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT, 0.5f));

		return coloumn;
	}

	private View createComplexColoumn(String luogo, ArrayList<View> list) {
		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View complexColoumn = inflater.inflate(R.layout.complex_coloumn, null,
				false);
		LinearLayout mContainer = (LinearLayout) complexColoumn
				.findViewById(R.id.container_base_coloumn);

		for (View coloumn : list) {
			coloumn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT, 0.5f));
			mContainer.addView(coloumn);
		}
		((TextView) complexColoumn.findViewById(R.id.text_luogo))
				.setText(luogo);

		return complexColoumn;
	}

	@Override
	public void onScrollChanged(ObservableScrollView scrollView, int x, int y,
			int oldx, int oldy) {
		// TODO Auto-generated method stub
		for (ScrollView child : mAllAScrollView)
			child.scrollTo(x, y);
	}

	// private ArrayList<String> getLuoghi(ArrayList<Turno> mList) {
	// ArrayList<String> mResult = new ArrayList<String>();
	// for (int i = 0; i < mList.size(); i++) {
	// if (!mResult.contains(mList.get(i).getLuogo()))
	// mResult.add(mList.get(i).getLuogo());
	// }
	// return mResult;
	// }

	// private ArrayList<String> getCategorie(ArrayList<Turno> mList) {
	// ArrayList<String> mResult = new ArrayList<String>();
	// for (int i = 0; i < mList.size(); i++) {
	// if (!mResult.contains(mList.get(i).getCategoria()))
	// mResult.add(mList.get(i).getCategoria());
	// }
	// return mResult;
	// }

	// private ArrayList<Turno> getTurniPerLuogo(String luogo) {
	// ArrayList<Turno> mResult = new ArrayList<Turno>();
	// for (int i = 0; i < listTurni.size(); i++) {
	// if (listTurni.get(i).getLuogo().equalsIgnoreCase(luogo))
	// mResult.add(listTurni.get(i));
	// }
	//
	// return mResult;
	// }

	// private ArrayList<Turno> getTurniPerLuogoECategoria(String luogo,
	// String categoria) {
	// ArrayList<Turno> mResult = new ArrayList<Turno>();
	// for (int i = 0; i < listTurni.size(); i++) {
	// if (listTurni.get(i).getLuogo().equalsIgnoreCase(luogo)
	// && listTurni.get(i).getCategoria()
	// .equalsIgnoreCase(categoria))
	// mResult.add(listTurni.get(i));
	// }
	//
	// return mResult;
	// }

	private ExtendedTurno[] parseTurno(ArrayList<Turno> list) {
		ExtendedTurno[] mResult = new ExtendedTurno[21];
		for (int i = 0; i < mResult.length; i++)
			mResult[i] = new ExtendedTurno(null, -1, null, -1);

		Calendar tmp = Calendar.getInstance(Locale.getDefault());
		tmp.set(Calendar.HOUR, 6);
		tmp.set(Calendar.MINUTE, 0);
		tmp.set(Calendar.SECOND, 0);
		tmp.set(Calendar.MILLISECOND, 0);
		tmp.set(Calendar.AM_PM, Calendar.PM);

		Date fine = (Date) tmp.getTime().clone();

		int numeroTurno = 0;

		ArrayList<Utente> mUtentiFake = new ArrayList<Utente>();
		mUtentiFake.add(new Utente("Gabriele", "Zacco", "", "", new byte[1],
				"000", "", "", ""));

		for (Turno turno : list) {

			tmp.set(Calendar.HOUR, 8);
			tmp.set(Calendar.AM_PM, Calendar.AM);
			Date index = (Date) tmp.getTime().clone();

			int i = 0;

			boolean startTurno = false;

			SimpleDateFormat dateFormatter = new SimpleDateFormat("HH:mm",
					Locale.getDefault());
			while (index.before(fine) || index.compareTo(fine) == 0) {
				String stringIndex = dateFormatter.format(index);
				if (stringIndex.equalsIgnoreCase(turno.getOraInizio())
						&& !startTurno) {
					mResult[i] = new ExtendedTurno((Date) index.clone(),
							mUtentiFake.size(), mUtentiFake, numeroTurno);
					startTurno = true;
				} else if (!stringIndex.equalsIgnoreCase(turno.getOraFine())
						&& startTurno) {
					mResult[i] = new ExtendedTurno((Date) index.clone(),
							mUtentiFake.size(), mUtentiFake, numeroTurno);
				} else if (stringIndex.equalsIgnoreCase(turno.getOraFine())) {
					startTurno = false;
				}
				i++;
				index.setTime(index.getTime() + (60 * 30 * 1000));
			}
			numeroTurno++;
		}

		return mResult;
	}

	// private int getNumeroVolontari(Turno turno) {
	// return ManagerData.getUtentiForTurno(turno).size();
	// }

	private class ExtendedTurno {
		Date date;
		int numeroVolontari;
		ArrayList<Utente> volontari;
		int numberTurno;

		public ExtendedTurno(Date date, int numeroVolontari,
				ArrayList<Utente> volontari, int numberTurno) {
			super();
			this.date = date;
			this.numeroVolontari = numeroVolontari;
			this.volontari = volontari;
			this.numberTurno = numberTurno;
		}

		public Date getDate() {
			return date;
		}

		public void setDate(Date date) {
			this.date = date;
		}

		public int getNumeroVolontari() {
			return numeroVolontari;
		}

		public void setNumeroVolontari(int numeroVolontari) {
			this.numeroVolontari = numeroVolontari;
		}

		public ArrayList<Utente> getVolontari() {
			return volontari;
		}

		public void setVolontari(ArrayList<Utente> volontari) {
			this.volontari = volontari;
		}

		public int getNumberTurno() {
			return numberTurno;
		}

		public void setNumberTurno(int numberTurno) {
			this.numberTurno = numberTurno;
		}
	}

	private class RowVolontario extends ArrayAdapter<Utente> {
		private final Context context;
		private final ArrayList<Utente> values;

		public RowVolontario(Context context, ArrayList<Utente> values) {
			super(context, R.layout.row_volontari, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.row_volontari, parent,
					false);

			((TextView) rowView.findViewById(R.id.text_anagrafiche_volontario))
					.setText(values.get(position).getNome());
			((TextView) rowView.findViewById(R.id.text_categoria_volontario))
					.setText("Categoria: " + values.get(position).getAmbito());
			((TextView) rowView.findViewById(R.id.text_ruolo_volontario))
					.setText("Ruolo: " + values.get(position).getRuolo());

			return rowView;
		}
	}
}
