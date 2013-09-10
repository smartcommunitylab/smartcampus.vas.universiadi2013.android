package smartcampus.android.template.standalone.Activity.SportBlock;

import java.io.ByteArrayOutputStream;

import smartcampus.android.template.standalone.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SportImageConstant {

	public static final int SPORT_0 = 0;
	public static final int SPORT_1 = 1;
	public static final int SPORT_2 = 2;
	public static final int SPORT_3 = 3;
	public static final int SPORT_4 = 4;
	public static final int SPORT_5 = 5;
	public static final int SPORT_6 = 6;
	public static final int SPORT_7 = 7;
	public static final int SPORT_8 = 8;
	public static final int SPORT_9 = 9;
	public static final int SPORT_10 = 10;
	public static final int SPORT_11 = 11;

	public static byte[] resourcesFromID(int id, Context cnt) {
		switch (id) {
		case SPORT_0:
			return fromResourcesToByteArray(R.drawable.alpine, cnt);
		case SPORT_1:
			return fromResourcesToByteArray(R.drawable.biathlon, cnt);
		case SPORT_2:
			return fromResourcesToByteArray(R.drawable.cross, cnt);
		case SPORT_3:
			return fromResourcesToByteArray(R.drawable.curling, cnt);
		case SPORT_4:
			return fromResourcesToByteArray(R.drawable.figure, cnt);
		case SPORT_5:
			return fromResourcesToByteArray(R.drawable.freestyle, cnt);
		case SPORT_6:
			return fromResourcesToByteArray(R.drawable.ice, cnt);
		case SPORT_7:
			return fromResourcesToByteArray(R.drawable.nordic, cnt);
		case SPORT_8:
			return fromResourcesToByteArray(R.drawable.shorttrack, cnt);
		case SPORT_9:
			return fromResourcesToByteArray(R.drawable.skijumping, cnt);
		case SPORT_10:
			return fromResourcesToByteArray(R.drawable.snowboarding, cnt);
		case SPORT_11:
			return fromResourcesToByteArray(R.drawable.speedskating, cnt);
		default:
			return new byte[1];
		}
	}

	private static byte[] fromResourcesToByteArray(int res, Context cnt) {
		Bitmap bm = BitmapFactory.decodeResource(cnt.getResources(), res);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bm.compress(Bitmap.CompressFormat.PNG, 100, baos); // bm is the bitmap
															// object
		return baos.toByteArray();
	}
}
