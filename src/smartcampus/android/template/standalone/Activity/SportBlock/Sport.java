package smartcampus.android.template.standalone.Activity.SportBlock;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.Activity.Model.ContainerSport;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.smartcampus.template.standalone.Atleta;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

		ArrayList<Atleta> mAtleti = new ArrayList<Atleta>();
		// mAtleti.add(new Atleta("Mattia", "Scagno", "22", "Italia", "19.14",
		// null));
		// mAtleti.add(new Atleta("Jacopo", "Penzo", "20", "Italia", "19.14",
		// null));
		// mAtleti.add(new ElementAtleta("Damiano", "Donati", "20", "Italia",
		// "19.14", null));
		mSport = ContainerSport.getListaSport(this);
		

		GridView mGrigliaSport = (GridView) findViewById(R.id.griglia_sport);
		mGrigliaSport.setAdapter(new GridArrayAdapter(this, mSport));

		mGrigliaSport.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent mCaller = new Intent(arg1.getContext(),
						DettaglioSport.class);
				mCaller.putExtra("Index", arg2);
				startActivity(mCaller);
			}
		});
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
					values.get(position).getImmagine(), 0, values.get(position)
							.getImmagine().length)));

			return rowView;
		}
	}

}
