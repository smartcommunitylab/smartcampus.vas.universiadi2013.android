package smartcampus.android.template.standalone.Activity.SportBlock;

import java.util.ArrayList;
import java.util.Map;

import smartcampus.android.template.standalone.Activity.Model.ManagerData;
import eu.trentorise.smartcampus.universiade.R;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Sport extends Activity {

	public static ArrayList<android.smartcampus.template.standalone.Sport> mSport = new ArrayList<android.smartcampus.template.standalone.Sport>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sport);

		new AsyncTask<Void, Void, Void>() {
			private Dialog dialog;
			private Map<String, Object> mResult;

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();

				dialog = new Dialog(Sport.this);
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
				mResult = ManagerData.getSport();
				if (!((Boolean) mResult.get("connectionError")))
					mSport = (ArrayList<android.smartcampus.template.standalone.Sport>) mResult
							.get("params");
				// mSport = SportContainer.getListaSport(Sport.this);
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);

				dialog.dismiss();

				// START ONPOST

				GridView mGrigliaSport = (GridView) findViewById(R.id.griglia_sport);
				mGrigliaSport.setAdapter(new GridArrayAdapter(Sport.this,
						mSport));

				mGrigliaSport.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
						Intent mCaller = new Intent(arg1.getContext(),
								DettaglioSport.class);
						mCaller.putExtra("sport", mSport.get(arg2));
						startActivity(mCaller);
					}
				});

				// END ONPOST
			}

		}.execute();
	}

	private class GridArrayAdapter extends
			ArrayAdapter<android.smartcampus.template.standalone.Sport> {
		private final Context context;
		private final ArrayList<android.smartcampus.template.standalone.Sport> values;

		public GridArrayAdapter(Context context,
				ArrayList<android.smartcampus.template.standalone.Sport> values) {
			super(context, R.layout.grid_sport, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.grid_sport, parent, false);

			ImageView mSport = (ImageView) rowView
					.findViewById(R.id.image_btn_2);
			mSport.setImageBitmap((BitmapFactory.decodeByteArray(
					values.get(position).getFoto(), 0, values.get(position)
							.getFoto().length)));

			return rowView;
		}
	}

}
