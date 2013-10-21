package smartcampus.android.template.standalone.Activity.SportBlock;

import java.io.ByteArrayOutputStream;

import smartcampus.android.template.universiadi.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class SportImageConstant {

	public static final int ALPINE = 0;
	public static final int BIATLHON = 1;
	public static final int CROSS = 2;
	public static final int CURLING = 3;
	public static final int FIGURE = 4;
	public static final int FREESTYLE = 5;
	public static final int ICE = 6;
	public static final int NORDIC = 7;
	public static final int SHORTTRACK = 8;
	public static final int SKIIJUMPING = 9;
	public static final int SNOWBOARDING = 10;
	public static final int SPEEDSKATING = 11;

	public static byte[] resourcesFromID(int id, Context cnt) {
		switch (id) {
		case ALPINE:
			return fromResourcesToByteArray(R.drawable.alpine, cnt);
		case BIATLHON:
			return fromResourcesToByteArray(R.drawable.biathlon, cnt);
		case CROSS:
			return fromResourcesToByteArray(R.drawable.cross, cnt);
		case CURLING:
			return fromResourcesToByteArray(R.drawable.curling, cnt);
		case FIGURE:
			return fromResourcesToByteArray(R.drawable.figure, cnt);
		case FREESTYLE:
			return fromResourcesToByteArray(R.drawable.freestyle, cnt);
		case ICE:
			return fromResourcesToByteArray(R.drawable.ice, cnt);
		case NORDIC:
			return fromResourcesToByteArray(R.drawable.nordic, cnt);
		case SHORTTRACK:
			return fromResourcesToByteArray(R.drawable.shorttrack, cnt);
		case SKIIJUMPING:
			return fromResourcesToByteArray(R.drawable.skijumping, cnt);
		case SNOWBOARDING:
			return fromResourcesToByteArray(R.drawable.snowboarding, cnt);
		case SPEEDSKATING:
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
