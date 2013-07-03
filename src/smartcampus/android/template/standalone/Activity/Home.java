package smartcampus.android.template.standalone.Activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.anim;
import smartcampus.android.template.standalone.R.drawable;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.RestRequest;

import eu.trentorise.smartcampus.ac.authenticator.AMSCAccessProvider;
import eu.trentorise.smartcampus.ac.model.UserData;
import eu.trentorise.smartcampus.discovertrento.DiscoverTrentoConnector;
import eu.trentorise.smartcampus.discovertrento.DiscoverTrentoConnectorException;
import eu.trentorise.smartcampus.dt.model.EventObject;
import eu.trentorise.smartcampus.dt.model.ObjectFilter;
import eu.trentorise.smartcampus.profileservice.ProfileService;
import eu.trentorise.smartcampus.profileservice.ProfileServiceException;
import eu.trentorise.smartcampus.profileservice.model.BasicProfile;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends FragmentActivity {

	private ImageView mEventi;
	private ImageView mEventiLogoUp;
	private ImageView mEventiLogoDown;

	private ImageView mSport;
	private ImageView mSportLogoUp;
	private ImageView mSportLogoDown;

	private ImageView mFacilities;
	private ImageView mFacilitiesLogoUp;
	private ImageView mFacilitiesLogoDown;

	private ImageView mProfile;

	private ViewPager mPager;
	private PagerAdapter mAdapter;

	private Timer rotateTimer;
	private static int countPage = -1;
	private boolean goUp = true;

	private RestRequest request;

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		FragmentManager manager = this.getSupportFragmentManager();
		if (manager.findFragmentByTag("info") == null) {

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Allert");
			builder.setMessage("Are you sure to quit and logout?");
			builder.setCancelable(true);
			builder.setPositiveButton("Logout",
					new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int id) {

							(new AMSCAccessProvider()).invalidateToken(
									getApplicationContext(), "unitn");
							startActivity(new Intent(getApplicationContext(),
									Intro.class));
							dialog.dismiss();
						}
					});
			builder.setNegativeButton("Cancel",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							// TODO Auto-generated method stub
							arg0.dismiss();
						}

					});
			AlertDialog alert = builder.create();
			alert.show();
		} else {
			manager.popBackStack();
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setContentView(R.layout.activity_home);

		setupButton();
		
		Log.i("Now", Long.toString(Calendar.getInstance().getTimeInMillis()));

		mPager = (ViewPager) findViewById(R.id.pager_sport);

		request = new RestRequest(getApplicationContext());
		request.execute(new String[] {
				getApplicationContext().getString(R.string.AUTH_TOKEN),
				"evento"});
		
		JSONArray response = null;
		try {
			response = request.get();
			for (int i = 0; i < response.length(); i++) {

				Log.i("Nome " + response.getJSONObject(i).getString("id"),
						response.getJSONObject(i).getString("nome"));
				Log.i("Descrizione "
						+ response.getJSONObject(i).getString("id"),
						response.getJSONObject(i).getString("descrizione"));
				Log.i("Data " + response.getJSONObject(i).getString("id"),
						response.getJSONObject(i).getString("data"));
			}
		} catch (InterruptedException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ExecutionException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		try
//		{
//			
//		}
//		catch(JSONException e)
//		{
//			try {
//				Log.i("Nome " + response.getString("id"),
//						response.getString("nome"));
//				Log.i("Descrizione " + response.getString("id"),
//						response.getString("descrizione"));
//				Log.i("Data " + response.getString("id"),
//						response.getString("data"));
//			} catch (JSONException e1) {
//				// TODO Auto-generated catch block
//				e1.printStackTrace();
//			}
//		}

		//
		// rotateTimer = new Timer();
		// rotateTimer.schedule(new TimerTask(){
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// if (goUp)
		// {
		// countPage++;
		// if (countPage != mPager.getChildCount())
		// goUp = true;
		// else
		// goUp = false;
		// }
		// else
		// {
		// countPage--;
		// if (countPage == 0)
		// goUp = false;
		// else
		// goUp = true;
		// }
		// mPager.setCurrentItem(countPage, true);
		// }
		// }, 0, 5000);

		class TapGestureListener extends
				GestureDetector.SimpleOnGestureListener {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			@Override
			public boolean onSingleTapConfirmed(MotionEvent e) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				if (fragmentManager.findFragmentByTag("info") == null) {
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.setCustomAnimations(
							R.anim.slide_in_bottom, R.anim.slide_out_bottom,
							R.anim.slide_in_bottom, R.anim.slide_out_bottom);
					fragmentTransaction.addToBackStack(null);
					fragmentTransaction.add(
							R.id.container_bottoni,
							new InfoEventi(((PageEventiOggi) mAdapter
									.getItem(mPager.getCurrentItem()))
									.getEvento(), fragmentManager), "info");
					fragmentTransaction.commit();
				} else
					fragmentManager.popBackStack();
				return true;
			}
		}

		final GestureDetector tapGestureDetector = new GestureDetector(this,
				new TapGestureListener());

		mPager.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				tapGestureDetector.onTouchEvent(event);
				return false;
			}
		});

		final FragmentManager manager = this.getSupportFragmentManager();

		mPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				if (manager.findFragmentByTag("info") != null)
					manager.popBackStack();
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub

			}

		});
	}

	private void setupButton() {
		mEventi = (ImageView) findViewById(R.id.image_eventi);
		mEventiLogoUp = (ImageView) findViewById(R.id.image_eventi_up);
		mEventiLogoDown = (ImageView) findViewById(R.id.image_eventi_down);
		mEventi.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					mEventi.setImageResource(R.drawable.button_events_down);

					AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
					fadeOut.setDuration(300);
					fadeOut.setFillAfter(true);
					fadeOut.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							mEventiLogoDown.setVisibility(View.VISIBLE);
							AlphaAnimation fadeIn = new AlphaAnimation(0.0f,
									1.0f);
							fadeIn.setDuration(300);
							fadeIn.setFillAfter(true);
							mEventiLogoDown.startAnimation(fadeIn);
						}

					});
					mEventiLogoUp.startAnimation(fadeOut);

					return true;
				}
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					startActivity(new Intent(arg0.getContext(), Evento.class));
					return true;
				}
				return false;
			}
		});

		mSport = (ImageView) findViewById(R.id.image_sport);
		mSportLogoUp = (ImageView) findViewById(R.id.image_sport_up);
		mSportLogoDown = (ImageView) findViewById(R.id.image_sport_down);
		mSport.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					mSport.setImageResource(R.drawable.button_sports_down);

					AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
					fadeOut.setDuration(300);
					fadeOut.setFillAfter(true);
					fadeOut.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							mSportLogoDown.setVisibility(View.VISIBLE);
							AlphaAnimation fadeIn = new AlphaAnimation(0.0f,
									1.0f);
							fadeIn.setDuration(300);
							fadeIn.setFillAfter(true);
							mSportLogoDown.startAnimation(fadeIn);
						}

					});
					mSportLogoUp.startAnimation(fadeOut);

					return true;
				}
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					startActivity(new Intent(arg0.getContext(), Sport.class));
					return true;
				}
				return false;
			}
		});

		mFacilities = (ImageView) findViewById(R.id.image_facilities);
		mFacilitiesLogoUp = (ImageView) findViewById(R.id.image_facilities_up);
		mFacilitiesLogoDown = (ImageView) findViewById(R.id.image_facilities_down);
		mFacilities.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					mFacilities
							.setImageResource(R.drawable.button_facilities_down);

					AlphaAnimation fadeOut = new AlphaAnimation(1.0f, 0.0f);
					fadeOut.setDuration(300);
					fadeOut.setFillAfter(true);
					fadeOut.setAnimationListener(new AnimationListener() {

						@Override
						public void onAnimationEnd(Animation animation) {
							// TODO Auto-generated method stub
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							mFacilitiesLogoDown.setVisibility(View.VISIBLE);
							AlphaAnimation fadeIn = new AlphaAnimation(0.0f,
									1.0f);
							fadeIn.setDuration(300);
							fadeIn.setFillAfter(true);
							mFacilitiesLogoDown.startAnimation(fadeIn);
						}

					});
					mFacilitiesLogoUp.startAnimation(fadeOut);

					return true;
				}
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					// SetImage
					startActivity(new Intent(arg0.getContext(), Booking.class));
					return true;
				}
				return false;
			}
		});

		mProfile = (ImageView) findViewById(R.id.image_profilo_utente);
		mProfile.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
					mProfile.setImageResource(R.drawable.button_profile_down);
					return true;
				}
				if (arg1.getAction() == MotionEvent.ACTION_UP) {
					// SetImage
					mProfile.setImageResource(R.drawable.button_profile_up);
					startActivity(new Intent(arg0.getContext(), Profile.class));
					return true;
				}
				return false;
			}
		});
	}

	private class PagerAdapter extends FragmentPagerAdapter {

		// fragments to instantiate in the viewpager
		private List<Fragment> fragments;

		// constructor
		public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
			super(fm);
			this.fragments = fragments;
		}

		// return access to fragment from position, required override
		@Override
		public Fragment getItem(int position) {
			return this.fragments.get(position);
		}

		// number of fragments in list, required override
		@Override
		public int getCount() {
			return this.fragments.size();
		}

		@Override
		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}
	}

	// private class DownloadEventiOggiAsync extends AsyncTask<Void, Integer,
	// JSONArray>
	// {
	// private ViewPager mPager;
	//
	// public DownloadEventiOggiAsync(ViewPager pager)
	// {
	// mPager = pager;
	// }
	//
	// @Override
	// protected JSONArray doInBackground(Void... arg0) {
	// // TODO Auto-generated method stub
	//
	// return null;
	// }
	//
	// @Override
	// protected void onPostExecute(JSONArray result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	//
	// Collection<List<?>> a = result.values();
	// Iterator<List<?>> it = a.iterator();
	// ArrayList<Fragment> fragment = new ArrayList<Fragment>();
	//
	// while(it.hasNext())
	// {
	// List<?> tmp = it.next();
	// for (int i=0; i<tmp.size(); i++)
	// {
	// if (((EventObject)tmp.get(i)).getToTime() == null)
	// fragment.add(new PageEventiOggi((EventObject) tmp.get(i), i));
	// else if (((EventObject)tmp.get(i)).getFromTime() <=
	// ((EventObject)tmp.get(i)).getToTime())
	// fragment.add(new PageEventiOggi((EventObject) tmp.get(i), i));
	// }
	// }
	//
	// mAdapter = new PagerAdapter(getSupportFragmentManager(), fragment);
	// mPager.setAdapter(mAdapter);
	// mAdapter.notifyDataSetChanged();
	// }
	//
	// }

}
