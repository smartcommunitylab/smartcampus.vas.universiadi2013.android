package smartcampus.android.template.standalone.Activity.EventiBlock;

import java.util.ArrayList;
import java.util.List;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.R.menu;
import smartcampus.android.template.standalone.Utilities.ElementDescRoute;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class PageInfoEventi extends Fragment {

	private int position;
	private Object params;

	public PageInfoEventi(int pos, Object prm) {
		position = pos;
		params = prm;
	}
	
	public PageInfoEventi() {
		this(-1,null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.activity_page_info_eventi, null);
		RelativeLayout mContainer = (RelativeLayout) mView
				.findViewById(R.id.container_page_eventi);

		switch (position) {
		case 0:
			FontTextView mDesc = new FontTextView(mView.getContext());
			mDesc.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			mDesc.setTextColor(Color.parseColor("#3293AC"));
			mDesc.setText((String) params);
			mDesc.setGravity(Gravity.CENTER);
			mContainer.addView(mDesc);
			break;
		case 1:
			ListView mLista = new ListView(mView.getContext());
			mLista.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.MATCH_PARENT));
			mLista.setAdapter(new ListAdapterRoute(mView.getContext(),
					(ArrayList<ElementDescRoute>) params));
			mContainer.addView(mLista);
			break;
		}

		return mView;
	}
	
	

	private class ListAdapterRoute extends ArrayAdapter<ElementDescRoute> {

		private Context context;
		private ArrayList<ElementDescRoute> values;

		public ListAdapterRoute(Context context,
				ArrayList<ElementDescRoute> values) {
			super(context, R.layout.row_desc_route, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public void notifyDataSetChanged() {
			// TODO Auto-generated method stub
			super.notifyDataSetChanged();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.row_desc_route, parent,
					false);

			if (position == 0) {
				((TextView) rowView.findViewById(R.id.text_description))
						.setText("Route per "
								+ values.get(position).getDescription());
			} else {
				((TextView) rowView.findViewById(R.id.text_description))
						.setText(position + ". "
								+ values.get(position).getDescription());
			}
			((TextView) rowView.findViewById(R.id.text_distance))
					.setText(values.get(position).getDistance());
			((TextView) rowView.findViewById(R.id.text_duration))
					.setText(values.get(position).getDuration());
			if (values.get(position).getImage() != -1)
				((ImageView) rowView.findViewById(R.id.img_man))
						.setImageResource(values.get(position).getImage());

			return rowView;
		}
	}

}