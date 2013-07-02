package smartcampus.android.template.standalone;

import java.util.ArrayList;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;

import eu.trentorise.smartcampus.dt.model.EventObject;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Booking extends FragmentActivity {

	private GoogleMap mMappa;
	private ImageView mButtonPOI;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_booking);
		
		mMappa = ((SupportMapFragment)getSupportFragmentManager().findFragmentById(R.id.mappa))
				.getMap();

		UiSettings mMapController = mMappa.getUiSettings();
    
		mMapController.setCompassEnabled(true);
		mMapController.setMyLocationButtonEnabled(false);
		mMapController.setZoomControlsEnabled(false);
	
		mMappa.setMyLocationEnabled(true);
		
		mMappa.setOnMapClickListener(new OnMapClickListener(){

			@Override
			public void onMapClick(LatLng point) {
				// TODO Auto-generated method stub
				FragmentManager fragmentManager = getSupportFragmentManager();
				if (fragmentManager.findFragmentByTag("select") != null)
				{
					fragmentManager.popBackStack();
					
					TranslateAnimation anim = new TranslateAnimation(0,0,
							-500, 0);
					anim.setDuration(300);
					anim.setFillAfter(true);
					mButtonPOI.startAnimation(anim);
					mButtonPOI.setVisibility(View.VISIBLE);
				}
			}
			
		});
		mButtonPOI = (ImageView)findViewById(R.id.image_button_poi);
		mButtonPOI.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_DOWN)
				{
					mButtonPOI.setImageResource(R.drawable.button_poi_down);
					return true;
				}
				if (arg1.getAction() == MotionEvent.ACTION_UP)
				{
					mButtonPOI.setImageResource(R.drawable.button_poi_up);
					if (getSupportFragmentManager().findFragmentByTag("select") == null)
					{
						TranslateAnimation anim = new TranslateAnimation(0,0,
								0, -500);
						anim.setDuration(500);
						anim.setFillAfter(true);
						mButtonPOI.startAnimation(anim);
						mButtonPOI.setVisibility(View.GONE);
						
						FragmentManager fragmentManager = getSupportFragmentManager();
						FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
						fragmentTransaction.setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_bottom,
								R.anim.slide_in_bottom, R.anim.slide_out_bottom);
						fragmentTransaction.addToBackStack(null);
						fragmentTransaction.add(R.id.container_select, new SelectBooking(fragmentManager, mButtonPOI, mMappa) ,"select");
						fragmentTransaction.commit();
					}
					else
					{
						getSupportFragmentManager().popBackStack();
						
						TranslateAnimation anim = new TranslateAnimation(0,0,
								-500, 0);
						anim.setDuration(300);
						anim.setFillAfter(true);
						mButtonPOI.startAnimation(anim);
						mButtonPOI.setVisibility(View.VISIBLE);
					}
					return true;
				}
				return false;
			}
			
		});
	}
}
