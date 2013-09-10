package smartcampus.android.template.standalone.Activity.ProfileBlock.RisolutoreSubBlock;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.Activity.ProfileBlock.FAQSubBlock.FAQ;
import smartcampus.android.template.standalone.HomeBlock.Home;
import android.os.Bundle;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.widget.*;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

public class ChooseChannel {

	public ChooseChannel(final Activity mThis) {

		final Dialog dialog = new Dialog(mThis);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.activity_main);
		dialog.getWindow().setBackgroundDrawableResource(
				R.drawable.dialog_rounded_corner);

		((ImageView) dialog.findViewById(R.id.image_helper_ticketing))
				.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							((ImageView) dialog
									.findViewById(R.id.image_helper_ticketing))
									.setImageResource(R.drawable.btn_send_ticket_press);
							return true;
						}
						if (event.getAction() == MotionEvent.ACTION_UP) {
							((ImageView) dialog
									.findViewById(R.id.image_helper_ticketing))
									.setImageResource(R.drawable.btn_send_ticket);

							new Ticketing(mThis);
							return true;
						}
						return false;
					}
				});

		((ImageView) dialog.findViewById(R.id.image_helper_sms))
				.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						if (event.getAction() == MotionEvent.ACTION_DOWN) {
							((ImageView) dialog
									.findViewById(R.id.image_helper_sms))
									.setImageResource(R.drawable.btn_send_sms_press);
							return true;
						}
						if (event.getAction() == MotionEvent.ACTION_UP) {
							((ImageView) dialog
									.findViewById(R.id.image_helper_sms))
									.setImageResource(R.drawable.btn_send_sms);

							new SendMessage(mThis, "sms");
							return true;
						}
						return false;
					}
				});

		dialog.show();

	}
}
