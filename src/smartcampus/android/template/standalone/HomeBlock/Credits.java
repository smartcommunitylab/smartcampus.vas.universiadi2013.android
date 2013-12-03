package smartcampus.android.template.standalone.HomeBlock;

import eu.trentorise.smartcampus.universiade.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Credits extends Activity {

	private final String URL_FACEBOOK = "http://facebook.com/smartcampuslab";
	private final String URL_TWITTER = "http://twitter.com/smartcampus_lab";
	private final String URL_GOOGLE = "https://plus.google.com/u/0/113432938957693942163";
	private final String URL_YOUTUBE = "http://www.youtube.com/user/smartcampuslab";
	private final String URL_LINKEDIN = "http://www.linkedin.com/company/smart-campus-lab";
	private final String URL_GITHUB = "https://github.com/smartcampuslab";

	private final String URL_SITO = "http://www.smartcampuslab.it";
	private final String MAIL = "info@smartcampuslab.it";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_credits);

		((ImageView) findViewById(R.id.img_credits_logo_smartcampus))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(URL_SITO)));
					}
				});

		((RelativeLayout) findViewById(R.id.btn_credits_facebook))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(URL_FACEBOOK)));
					}
				});
		((RelativeLayout) findViewById(R.id.btn_credits_twitter))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(URL_TWITTER)));
					}
				});
		((RelativeLayout) findViewById(R.id.btn_credits_linkedin))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(URL_LINKEDIN)));
					}
				});
		((RelativeLayout) findViewById(R.id.btn_credits_google))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(URL_GOOGLE)));
					}
				});
		((RelativeLayout) findViewById(R.id.btn_credits_youtube))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(URL_YOUTUBE)));
					}
				});
		((RelativeLayout) findViewById(R.id.btn_credits_github))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(URL_GITHUB)));
					}
				});

		((RelativeLayout) findViewById(R.id.btn_credits_sito))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse(URL_SITO)));
					}
				});

		((RelativeLayout) findViewById(R.id.btn_credits_mail))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("plain/text");
						intent.putExtra(Intent.EXTRA_EMAIL,
								new String[] { MAIL });
						intent.putExtra(Intent.EXTRA_SUBJECT,
								getString(R.string.CREDITS_MAIL));
						startActivity(Intent.createChooser(intent, ""));
					}
				});

	}

}
