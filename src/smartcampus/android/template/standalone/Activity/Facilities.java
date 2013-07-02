package smartcampus.android.template.standalone.Activity;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Facilities extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_facilities);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.facilities, menu);
		return true;
	}

}
