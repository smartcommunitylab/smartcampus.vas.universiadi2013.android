package smartcampus.android.template.standalone.Activity;

import java.util.Calendar;
import java.util.Random;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.R.drawable;
import smartcampus.android.template.standalone.R.id;
import smartcampus.android.template.standalone.R.layout;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import android.smartcampus.template.standalone.*;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

@SuppressLint("ValidFragment")
public class PageEventiOggi extends Fragment {

	private android.smartcampus.template.standalone.Evento mEvento;
	private int mPos;
	
	public PageEventiOggi(android.smartcampus.template.standalone.Evento evento, int position)
	{
		mEvento = evento;
		mPos = position;
	}
	
	public PageEventiOggi()
	{
		this(null, 0);
	}
	
	public android.smartcampus.template.standalone.Evento getEvento()
	{
		return mEvento;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.activity_page_eventi_oggi, null);
		
		FontTextView mNome = (FontTextView)mView.findViewById(R.id.text_nome_evento);
		mNome.setText(mEvento.getNome().toUpperCase());
		
		String[] dataTokenized = mEvento.getData().split("/");
		FontTextView mOra = (FontTextView)mView.findViewById(R.id.text_ora_evento);
//		Calendar mHour = Calendar.getInstance();
//		mHour.setTimeInMillis(mEvento.getData().getTime());
//		String ora = null;
//		
//		if (mHour.get(Calendar.MINUTE) < 10)
//			ora = mHour.get(Calendar.HOUR)+":0"+mHour.get(Calendar.MINUTE);
//		else
//			ora = mHour.get(Calendar.HOUR)+":"+mHour.get(Calendar.MINUTE);
//		if (mHour.get(Calendar.AM_PM) == Calendar.AM)
//			ora = ora + " AM";
//		else
//			ora = ora + " PM";
		
		mOra.setText(dataTokenized[1]);
		
		ImageView mImgSfondo = (ImageView)mView.findViewById(R.id.image_sfondo_evento);
		int randomNum = (new Random()).nextInt(5);
		while (randomNum == 0)
			randomNum = (new Random()).nextInt(5);
		mImgSfondo.setImageResource(getResources().getIdentifier("cover"+randomNum, "drawable", "smartcampus.android.template.standalone"));
//		FontTextView mToday = (FontTextView)mView.findViewById(R.id.text_today);
//		Calendar mCal = Calendar.getInstance();
//		mCal.setTimeInMillis(mEvento.getFromTime());
//		mToday.setText(mToday.getText()+" ("+mCal.get(Calendar.DAY_OF_MONTH)+
//				"/"+Integer.toString(mCal.get(Calendar.MONTH)+1)+"/"+
//				mCal.get(Calendar.YEAR)+")");
		
		ImageView mMask = (ImageView)mView.findViewById(R.id.image_mask);
		if (mPos % 2 == 0)
		{
			Bitmap sprite = BitmapFactory.decodeResource(this.getResources(),
			        R.drawable.slide_mask);
			Matrix rotateRight = new Matrix();
			rotateRight.preScale(-1.0f, 1.0f);
			Bitmap rSprite = Bitmap.createBitmap(sprite, 0, 0,
			        sprite.getWidth(), sprite.getHeight(), rotateRight, true);
			
			mMask.setImageBitmap(rSprite);
			
			mNome.setGravity(Gravity.RIGHT);
			mOra.setGravity(Gravity.RIGHT);
			((FontTextView)mView.findViewById(R.id.text_today)).setGravity(Gravity.RIGHT);
		}
		
		return mView;
	}

}
