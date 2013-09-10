package smartcampus.android.template.standalone.Activity.ProfileBlock.RisolutoreSubBlock;

import smartcampus.android.template.standalone.R;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Ticketing {

	public Ticketing(final Activity mThis) {

		final Dialog dialog = new Dialog(mThis);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.activity_ticketing);
		dialog.getWindow().setBackgroundDrawableResource(
				R.drawable.dialog_rounded_corner);

		((ImageView) dialog.findViewById(R.id.image_helper_helpdesk))
				.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							((ImageView) dialog
									.findViewById(R.id.image_helper_helpdesk))
									.setImageResource(R.drawable.btn_send_help_press);
							return true;
						}
						if (event.getAction() == MotionEvent.ACTION_UP) {
							((ImageView) dialog
									.findViewById(R.id.image_helper_helpdesk))
									.setImageResource(R.drawable.btn_send_help);

							Notification.sendNotification(mThis,
									"Inviato all'helpdesk il ticketing");
							return true;
						}
						return false;
					}
				});

		((ImageView) dialog.findViewById(R.id.image_helper_notifica))
				.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							((ImageView) dialog
									.findViewById(R.id.image_helper_notifica))
									.setImageResource(R.drawable.btn_send_notify_press);
							return true;
						}
						if (event.getAction() == MotionEvent.ACTION_UP) {
							((ImageView) dialog
									.findViewById(R.id.image_helper_notifica))
									.setImageResource(R.drawable.btn_send_notify);

							new SendMessage(mThis, "notifica");
							return true;
						}
						return false;
					}
				});

		dialog.show();
	}
}
