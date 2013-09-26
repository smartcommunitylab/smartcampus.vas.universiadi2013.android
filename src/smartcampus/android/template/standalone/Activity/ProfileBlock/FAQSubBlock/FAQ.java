package smartcampus.android.template.standalone.Activity.ProfileBlock.FAQSubBlock;

import java.util.ArrayList;

import smartcampus.android.template.universiadi.R;
import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.smartcampus.template.standalone.ExtendedAnswer;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

public class FAQ extends Activity {

	private EditText mTextDomanda;

	private ArrayList<ExtendedAnswer> mListaRisposte;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_faq);

		mTextDomanda = (EditText) findViewById(R.id.text_domanda);
		mTextDomanda.setTypeface(Typeface.createFromAsset(
				getApplicationContext().getAssets(), "PatuaOne-Regular.otf"));
		mTextDomanda.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(
							((EditText) findViewById(R.id.text_domanda))
									.getWindowToken(), 0);
					inviaDomandaFAQ(mTextDomanda.getText().toString());
				}
				return false;
			}

		});

		((RelativeLayout) findViewById(R.id.btn_invia_domanda))
				.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							((ImageView) findViewById(R.id.image_search))
									.setImageResource(R.drawable.btn_main_cerca_press);
							return true;
						}
						if (event.getAction() == MotionEvent.ACTION_UP) {
							((ImageView) findViewById(R.id.image_search))
									.setImageResource(R.drawable.btn_main_cerca);
							InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.hideSoftInputFromWindow(
									((EditText) findViewById(R.id.text_domanda))
											.getWindowToken(), 0);
							inviaDomandaFAQ(mTextDomanda.getText().toString());

							return true;
						}
						return false;
					}
				});
	}

	private void inviaDomandaFAQ(final String mDomanda) {
		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(FAQ.this);
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
					}
				});

			}

			@Override
			protected Void doInBackground(Void... params) {
				// TODO Auto-generated method stub
				mListaRisposte = ManagerData.getRispostaPerDomanda(mDomanda);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialog.dismiss();

				// START ONPOST

				if (mListaRisposte.size() != 0) {
					((TextView) findViewById(R.id.text_nessun_risultato_faq))
							.setVisibility(View.GONE);
					((ListView) findViewById(R.id.lista_risposte))
							.setAdapter(new RowAnswer(getApplicationContext(),
									mListaRisposte));
					((ListView) findViewById(R.id.lista_risposte))
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
										View arg1, int arg2, long arg3) {
									// TODO Auto-generated method stub
									Dialog dialog = new Dialog(FAQ.this);
									dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
									dialog.setContentView(R.layout.dialog_dettaglio);
									dialog.getWindow()
											.setBackgroundDrawableResource(
													R.drawable.dialog_rounded_corner);

									((TextView) dialog
											.findViewById(R.id.text_dettaglio_question))
											.setText(mListaRisposte.get(arg2)
													.getQuestion());
									((TextView) dialog
											.findViewById(R.id.text_dettaglio_answer))
											.setText(mListaRisposte.get(arg2)
													.getAnswer());
									((TextView) dialog
											.findViewById(R.id.text_dettaglio_answer))
											.setMovementMethod(new ScrollingMovementMethod());
									((TextView) dialog
											.findViewById(R.id.text_dettaglio_accuracy)).setText(Float
											.toString(getAccuracy(
													mListaRisposte.get(arg2)
															.getTotalTag(),
													mListaRisposte.get(arg2)
															.getUsefulTag()))
											+ "%");

									dialog.show();
								}
							});
				} else
					((TextView) findViewById(R.id.text_nessun_risultato_faq))
							.setVisibility(View.VISIBLE);

				// END ONPOST
			}

		}.execute();
	}

	private float getAccuracy(int totTag, int usefullTag) {
		return (usefullTag * 100) / totTag;
	}

	private class RowAnswer extends ArrayAdapter<ExtendedAnswer> {
		private final Context context;
		private final ArrayList<ExtendedAnswer> values;

		public RowAnswer(Context context, ArrayList<ExtendedAnswer> values) {
			super(context, R.layout.row_answer, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View rowView = inflater.inflate(R.layout.row_answer, parent, false);

			((TextView) rowView.findViewById(R.id.text_question))
					.setText(values.get(position).getQuestion());
			((TextView) rowView.findViewById(R.id.text_answer)).setText(values
					.get(position).getAnswer());
			((TextView) rowView.findViewById(R.id.text_accuracy)).setText(Float
					.toString(getAccuracy(values.get(position).getTotalTag(),
							values.get(position).getUsefulTag()))
					+ "%");

			return rowView;
		}
	}
}
