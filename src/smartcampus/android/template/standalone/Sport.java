package smartcampus.android.template.standalone;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class Sport extends Activity {

	public static ArrayList<ElementSport> mSport = new ArrayList<ElementSport>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sport);
		
		ArrayList<ElementAtleta> mAtleti = new ArrayList<ElementAtleta>();
		mAtleti.add(new ElementAtleta("Mattia", "Scagno", "22", "Italia", "19.14", null));
		mAtleti.add(new ElementAtleta("Jacopo", "Penzo", "20", "Italia", "19.14", null));
		mAtleti.add(new ElementAtleta("Damiano", "Donati", "20", "Italia", "19.14", null));
		
		
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, mAtleti, R.drawable.alpine, "Alpine"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.biathlon, "Biathlon"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.cross, "Cross"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.curling, "Curling"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.figure, "Figure"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.freestyle, "Freestyle"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.ice, "Ice"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.nordic, "Nordic"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.shorttrack, "Shorttracking"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.skijumping, "Ski Jumping"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.snowboarding, "Snowboarding"));
		mSport.add(new ElementSport(new double[]{0.0, 0.0}, null, R.drawable.speedskating, "SpeedSkating"));
		
		GridView mGrigliaSport = (GridView)findViewById(R.id.griglia_sport);
		mGrigliaSport.setAdapter(new GridArrayAdapter(this, mSport));
		
		mGrigliaSport.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent mCaller = new Intent(arg1.getContext(), DettaglioSport.class);
				mCaller.putExtra("Index", arg2);
				startActivity(mCaller);
			}
		});
	}
	
	private class GridArrayAdapter extends ArrayAdapter<ElementSport>
	{
		private final Context context;
		private final ArrayList<ElementSport> values;

		public GridArrayAdapter(Context context, ArrayList<ElementSport> values) {
			super(context, R.layout.grid_sport, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.grid_sport, parent, false);
			
			ImageView mSport = (ImageView)rowView.findViewById(R.id.image_sport);
			mSport.setImageResource(values.get(position).getmImageSport());
			
			return rowView;
		}
	}

}
