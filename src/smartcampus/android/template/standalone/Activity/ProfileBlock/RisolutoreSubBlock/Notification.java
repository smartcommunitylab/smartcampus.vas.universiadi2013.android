package smartcampus.android.template.standalone.Activity.ProfileBlock.RisolutoreSubBlock;

import android.app.Activity;
import android.app.AlertDialog;

public class Notification {

	public static void sendNotification(Activity sender, String msg) {

		AlertDialog.Builder builder = new AlertDialog.Builder(sender);
		builder.setTitle("Avviso");
		builder.setMessage(msg);
		builder.setCancelable(true);
		builder.create().show();
	}

}
