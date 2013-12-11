package smartcampus.android.template.standalone.Activity.SportBlock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import smartcampus.android.template.standalone.Activity.EventiBlock.InfoEventi;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import eu.trentorise.smartcampus.universiade.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.smartcampus.template.standalone.Evento;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;

@SuppressLint("ValidFragment")
public class PageInfoSport extends Fragment {

	private String mDescrizione;
	private ArrayList<Evento> mListaEventiSportivi;
	private String mAtleti;
	private String mSpecialita;
	private int mPosition;

	public PageInfoSport(String mDescrizione,
			ArrayList<Evento> mListaEventiSportivi, String mAtleti,
			String mSpecialita, int mPosition) {
		super();
		this.mDescrizione = mDescrizione;
		this.mListaEventiSportivi = mListaEventiSportivi;
		this.mAtleti = mAtleti;
		this.mSpecialita = mSpecialita;
		this.mPosition = mPosition;
	}

	public PageInfoSport() {
		this(null, null, null, null, -1);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		final View mView = inflater.inflate(R.layout.activity_page_info_sport,
				null);

		RelativeLayout mContainerDesc = (RelativeLayout) mView
				.findViewById(R.id.container_descrizione);
		RelativeLayout mContainerEventi = (RelativeLayout) mView
				.findViewById(R.id.container_eventi_correlati);
		RelativeLayout mContainerAtleti = (RelativeLayout) mView
				.findViewById(R.id.container_atleti);
		RelativeLayout mContainerSpecialita = (RelativeLayout) mView
				.findViewById(R.id.container_specialita);

		switch (mPosition) {
		case 0:
			mContainerDesc.setVisibility(View.VISIBLE);
			mContainerEventi.setVisibility(View.GONE);
			mContainerAtleti.setVisibility(View.GONE);
			mContainerSpecialita.setVisibility(View.GONE);

			((FontTextView) mView.findViewById(R.id.text_descrizione))
					.setText(mDescrizione);
			((FontTextView) mView.findViewById(R.id.text_descrizione))
					.setMovementMethod(new ScrollingMovementMethod());

			break;
		case 1:
			mContainerDesc.setVisibility(View.GONE);
			mContainerEventi.setVisibility(View.VISIBLE);
			mContainerAtleti.setVisibility(View.GONE);
			mContainerSpecialita.setVisibility(View.GONE);

			if (mListaEventiSportivi.size() != 0) {
				((TextView) mView
						.findViewById(R.id.text_nessun_evento_per_sport))
						.setVisibility(View.GONE);
				ListView mListaEventi = (ListView) mView
						.findViewById(R.id.list_eventi_correlati);
				mListaEventi.setAdapter(new ListArrayAdapter(getActivity(),
						mListaEventiSportivi));
				mListaEventi.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Intent mCaller = new Intent(arg1.getContext(),
								InfoEventi.class);
						mCaller.putExtra("evento",
								mListaEventiSportivi.get(arg2));
						startActivity(mCaller);
					}
				});
			} else
				((TextView) mView
						.findViewById(R.id.text_nessun_evento_per_sport))
						.setVisibility(View.VISIBLE);

			break;
		case 2:
			mContainerDesc.setVisibility(View.GONE);
			mContainerEventi.setVisibility(View.GONE);
			mContainerAtleti.setVisibility(View.VISIBLE);
			mContainerSpecialita.setVisibility(View.GONE);

			((TextView) mView.findViewById(R.id.text_atleta_per_sport))
					.setText(mAtleti);
			((FontTextView) mView.findViewById(R.id.text_atleta_per_sport))
					.setMovementMethod(new ScrollingMovementMethod());

			break;
		case 3:
			mContainerDesc.setVisibility(View.GONE);
			mContainerEventi.setVisibility(View.GONE);
			mContainerAtleti.setVisibility(View.GONE);
			mContainerSpecialita.setVisibility(View.VISIBLE);

			if (mSpecialita.indexOf("Uomini e donne:") != -1) {
				// Una sola section
				String[] specialita = mSpecialita.replace("Uomini e donne: ",
						"").split(", ");
				String[] values = new String[specialita.length + 1];
				values[0] = "Uomini e donne";
				for (int i = 0; i < specialita.length; i++)
					values[i + 1] = specialita[i];

				((ListView) mView.findViewById(R.id.lista_specialita))
						.setAdapter(new SpecialitaArrayAdapter(getActivity(),
								values));
			} else if (mSpecialita.indexOf("Uomini:") != -1
					|| mSpecialita.indexOf("Donne:") != -1) {
				// Due section
				String[] specialitaString = mSpecialita.split("\nDonne: ");
				String[] specialitaDonne = specialitaString[1].split(", ");
				String[] specialitaUomini = (specialitaString[0].replace(
						"Uomini: ", "")).split(", ");
				String[] values = new String[specialitaUomini.length
						+ specialitaDonne.length + 2];
				values[0] = "Uomini";
				for (int i = 0; i < specialitaUomini.length; i++)
					values[i + 1] = specialitaUomini[i];
				values[specialitaUomini.length + 1] = "Donne";
				for (int i = 0; i < specialitaDonne.length; i++)
					values[i + specialitaUomini.length + 2] = specialitaDonne[i];

				((ListView) mView.findViewById(R.id.lista_specialita))
						.setAdapter(new SpecialitaArrayAdapter(getActivity(),
								values));
			} else {
				// Nessuna lista, solo testo
				((FontTextView) mView
						.findViewById(R.id.text_nessuna_specialita))
						.setText(mSpecialita);
				((FontTextView) mView
						.findViewById(R.id.text_nessuna_specialita))
						.setVisibility(View.VISIBLE);
				((FontTextView) mView
						.findViewById(R.id.text_nessuna_specialita))
						.setMovementMethod(new ScrollingMovementMethod());
			}
			break;
		}

		return mView;
	}

	private class ListArrayAdapter extends ArrayAdapter<Evento> {
		private final Context context;
		private final ArrayList<Evento> values;

		public ListArrayAdapter(Context context, ArrayList<Evento> values) {
			super(context, R.layout.row_eventi, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = new View(context);

			if (values.size() != 0) {
				rowView = inflater.inflate(R.layout.row_eventi, parent, false);

				((FontTextView) rowView.findViewById(R.id.text_orario))
						.setText(new SimpleDateFormat("hh:mm").format(values
								.get(position).getData()));

				if (values.get(position).getNome().length() <= 12)
					((FontTextView) rowView.findViewById(R.id.text_nome_evento))
							.setText(values.get(position).getNome()
									.toUpperCase());
				else
					((FontTextView) rowView.findViewById(R.id.text_nome_evento))
							.setText(values.get(position).getNome()
									.toUpperCase().substring(0, 11)
									+ "...");
			}

			return rowView;
		}
	}

	private class SpecialitaArrayAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;

		public SpecialitaArrayAdapter(Context context, String[] values) {
			super(context, R.layout.row_specialita, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.row_specialita, parent,
					false);

			if (values[position].equalsIgnoreCase("Uomini")
					|| values[position].equalsIgnoreCase("Donne")
					|| values[position].equalsIgnoreCase("Uomini e donne")) {
				((RelativeLayout) rowView
						.findViewById(R.id.container_row_specialita))
						.setBackgroundColor(Color.parseColor("#7f000000"));
				((FontTextView) rowView.findViewById(R.id.text_specialita))
						.setTextColor(Color.WHITE);
			} else
				((RelativeLayout) rowView
						.findViewById(R.id.container_row_specialita))
						.setBackgroundColor(Color.parseColor("#FFFFFF"));

			((FontTextView) rowView.findViewById(R.id.text_specialita))
					.setText(values[position]);

			return rowView;
		}
	}

	// private class DownloadEventiCorrelatiSport extends AsyncTask<Void, Void,
	// Map<String, List<?>>>
	// {
	// private Context mContext;
	// private ListView mList;
	// private String mNomeSport;
	//
	// public DownloadEventiCorrelatiSport(Context context, ListView
	// mPrimaLista)
	// {
	// mContext = context;
	// mList = mPrimaLista;
	//
	// }
	//
	// @Override
	// protected Map<String, List<?>> doInBackground(Void... params) {
	// // TODO Auto-generated method stub
	// // DiscoverTrentoConnector service = new DiscoverTrentoConnector
	// // ("https://vas-dev.smartcampuslab.it");
	// //
	// Thread.currentThread().setContextClassLoader(EventObject.class.getClassLoader());
	// // ObjectFilter filter = new ObjectFilter();
	// // filter.setClassName(EventObject.class.getCanonicalName());
	// //
	// // Calendar mCal = Calendar.getInstance();
	// //
	// // filter.setFromTime(mCal.getTimeInMillis());
	// // filter.setToTime(mCal.getTimeInMillis()+86400000);
	// // try {
	// // return service.getObjects(filter, Intro.mToken);
	// // } catch (DiscoverTrentoConnectorException e) {
	// // // TODO Auto-generated catch block
	// // e.printStackTrace();
	// // }
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
	// mList.setAdapter(new ListArrayAdapter(mContext, mResult));
	// }
	// }
}
