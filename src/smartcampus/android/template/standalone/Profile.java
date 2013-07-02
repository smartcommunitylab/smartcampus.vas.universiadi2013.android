package smartcampus.android.template.standalone;

import java.io.IOException;
import java.util.List;

import it.sayservice.platform.smartplanner.data.message.Itinerary;
import it.sayservice.platform.smartplanner.data.message.journey.SingleJourney;

import org.codehaus.jackson.map.ObjectMapper;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SendCallback;

import eu.trentorise.smartcampus.ac.ACService;
import eu.trentorise.smartcampus.ac.AcServiceException;
import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.ac.model.Attribute;
import eu.trentorise.smartcampus.ac.model.UserData;
import eu.trentorise.smartcampus.journeyplanner.JourneyPlannerConnector;
import eu.trentorise.smartcampus.profileservice.ProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import android.os.AsyncTask;
import android.os.Bundle;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.TextView;

public class Profile extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		((FontTextView)findViewById(R.id.text_nome_user)).setText("Mario Rossi");
		
		final ImageView mBadge = (ImageView)findViewById(R.id.image_badge);
		mBadge.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction() == MotionEvent.ACTION_DOWN)
				{
					mBadge.setImageResource(R.drawable.button_badge_down);
					return true;
				}
				if(arg1.getAction() == MotionEvent.ACTION_UP)
				{
					mBadge.setImageResource(R.drawable.button_badge_up);
					startActivity(new Intent(arg0.getContext(), Batch.class));
					return true;
				}
				return false;
			}
		});
		
		final ImageView mPush = (ImageView)findViewById(R.id.image_push);
		mPush.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if(arg1.getAction() == MotionEvent.ACTION_DOWN)
				{
					mPush.setImageResource(R.drawable.button_push_down);
					return true;
				}
				if(arg1.getAction() == MotionEvent.ACTION_UP)
				{
					mPush.setImageResource(R.drawable.button_push_up);
					ParsePush push = new ParsePush();
					push.setChannel("Volunteer");
					push.setMessage("TEST PUSH");
					push.setPushToAndroid(true);
					push.sendInBackground(new SendCallback(){

						@Override
						public void done(ParseException arg0) {
							// TODO Auto-generated method stub
							Log.i("PUSH","SEND");
						}
						
					});
					return true;
				}
				return false;
			}
		});
		
		if (((TextView)findViewById(R.id.text_role)).getText().equals("Volunteer"))
		{
//			mBadge.setVisibility(View.GONE);
			((ImageView)findViewById(R.id.image_profile)).setImageResource(R.drawable.profile_volunteer);
		}
		else
			((ImageView)findViewById(R.id.image_profile)).setImageResource(R.drawable.profile_boss);
	}
}
