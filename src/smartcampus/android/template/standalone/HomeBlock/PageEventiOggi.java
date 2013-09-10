package smartcampus.android.template.standalone.HomeBlock;

import java.text.SimpleDateFormat;
import java.util.Locale;

import smartcampus.android.template.standalone.R;
import smartcampus.android.template.standalone.Utilities.FontTextView;
import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.smartcampus.template.standalone.Evento;
import android.smartcampus.template.standalone.Meeting;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

@SuppressLint("ValidFragment")
public class PageEventiOggi extends Fragment {

	private Object mEvento;
	private int mPos;

	public PageEventiOggi(Object evento, int position) {
		mEvento = evento;
		mPos = position;
	}

	public PageEventiOggi() {
		this(null, 0);
	}

	public Evento getEvento() {
		return (Evento) mEvento;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.activity_page_eventi_oggi, null);

		if (mEvento instanceof Evento) {
			Evento evento = (Evento) mEvento;
			FontTextView mNome = (FontTextView) mView
					.findViewById(R.id.text_nome_evento);
			mNome.setText(evento.getNome().toUpperCase());

			FontTextView mOra = (FontTextView) mView
					.findViewById(R.id.text_ora_evento);

			mOra.setText(new SimpleDateFormat("hh:mm", Locale.getDefault())
					.format(evento.getData()));

			ImageView mImgSfondo = (ImageView) mView
					.findViewById(R.id.image_sfondo_evento);
			mImgSfondo.setImageResource(getResources().getIdentifier("cover1",
					"drawable", "smartcampus.android.template.standalone"));

			ImageView mMask = (ImageView) mView.findViewById(R.id.image_mask);
			if (mPos % 2 == 0) {
				Bitmap sprite = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.scroll_main);
				Matrix rotateRight = new Matrix();
				rotateRight.preScale(-1.0f, 1.0f);
				Bitmap rSprite = Bitmap.createBitmap(sprite, 0, 0,
						sprite.getWidth(), sprite.getHeight(), rotateRight,
						true);

				mMask.setImageBitmap(rSprite);

				mNome.setGravity(Gravity.RIGHT);
				mOra.setGravity(Gravity.RIGHT);
				((FontTextView) mView.findViewById(R.id.text_today))
						.setGravity(Gravity.RIGHT);
			}
		} else {
			Meeting meeting = (Meeting) mEvento;

			FontTextView mNome = (FontTextView) mView
					.findViewById(R.id.text_nome_evento);
			mNome.setText(meeting.getNome().toUpperCase());

			FontTextView mOra = (FontTextView) mView
					.findViewById(R.id.text_ora_evento);

			mOra.setText(new SimpleDateFormat("hh:mm", Locale.getDefault())
					.format(meeting.getData()));

			ImageView mImgSfondo = (ImageView) mView
					.findViewById(R.id.image_sfondo_evento);
			mImgSfondo.setImageResource(getResources().getIdentifier("cover1",
					"drawable", "smartcampus.android.template.standalone"));

			ImageView mMask = (ImageView) mView.findViewById(R.id.image_mask);
			if (mPos % 2 == 0) {
				Bitmap sprite = BitmapFactory.decodeResource(
						this.getResources(), R.drawable.scroll_main);
				Matrix rotateRight = new Matrix();
				rotateRight.preScale(-1.0f, 1.0f);
				Bitmap rSprite = Bitmap.createBitmap(sprite, 0, 0,
						sprite.getWidth(), sprite.getHeight(), rotateRight,
						true);

				mMask.setImageBitmap(rSprite);

				mNome.setGravity(Gravity.RIGHT);
				mOra.setGravity(Gravity.RIGHT);
				((FontTextView) mView.findViewById(R.id.text_today))
						.setGravity(Gravity.RIGHT);
			}
		}

		return mView;
	}

}
