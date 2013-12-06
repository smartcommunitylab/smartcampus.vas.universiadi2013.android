package smartcampus.android.template.standalone.Activity.EventiBlock;

import java.util.ArrayList;

import smartcampus.android.template.standalone.Utilities.ElementDescRoute;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import eu.trentorise.smartcampus.universiade.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
		this(-1, null);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.activity_page_info_eventi, null);
		LinearLayout mContainer = (LinearLayout) mView
				.findViewById(R.id.container_page_eventi);

		switch (position) {
		case 0:
			String desc = (String) params;

			FontTextView mDesc = new FontTextView(mView.getContext());
			mDesc.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.5f));
			mDesc.setTextColor(Color.parseColor("#3293AC"));
			mDesc.setText(desc.substring(0, desc.indexOf("http:")));
			mDesc.setGravity(Gravity.CENTER);
			mContainer.addView(mDesc);

			final FontTextView mLink = new FontTextView(mView.getContext());
			mLink.setLayoutParams(new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 0.5f));
			mLink.setTextColor(Color.parseColor("#3293AC"));
			mLink.setText(desc.substring(desc.indexOf("http:"),
					desc.length() - 1));
			mLink.setGravity(Gravity.CENTER_HORIZONTAL);
			mLink.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent browserIntent = new Intent(
							android.content.Intent.ACTION_VIEW, Uri.parse(mLink
									.getText().toString()));

					startActivity(browserIntent);
				}
			});
			mContainer.addView(mLink);

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
						.setText(getContext().getString(R.string.ROUTE_FOR)
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