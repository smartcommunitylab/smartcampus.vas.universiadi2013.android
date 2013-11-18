package smartcampus.android.template.standalone.HomeBlock;

import smartcampus.android.template.universiadi.R;
import smartcampus.android.template.universiadi.R.layout;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class Credits extends Activity {

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
								.parse("http://www.smartcampuslab.it")));
					}
				});

		((ImageView) findViewById(R.id.img_credits_logo_codefish))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						startActivity(new Intent(Intent.ACTION_VIEW, Uri
								.parse("http://www.codefishstudio.com")));
					}
				});
	}

}
