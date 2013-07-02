package smartcampus.android.template.standalone;

import java.util.ArrayList;
import java.util.Date;

import com.google.android.gms.maps.model.LatLng;

public class ElementEvento {
	
	private String mNomeEvento;
	private Date mDataEvento;
	private int mImmagineEvento;
	private LatLng mGPS;
	private String mPrezzo;
	
	private ArrayList<ElementAtleta> mPartecipanti;
	private String mTipoGara;
	
	public ElementEvento(String mNomeEvento, Date mDataEvento, int mImmagineEvento,
			LatLng mGPS, String mPrezzo, ArrayList<ElementAtleta> mPartecipanti,
			String mTipoGara) {
		super();
		this.mNomeEvento = mNomeEvento;
		this.mDataEvento = mDataEvento;
		this.mImmagineEvento = mImmagineEvento;
		this.mGPS = mGPS;
		this.mPrezzo = mPrezzo;
		this.mPartecipanti = mPartecipanti;
		this.mTipoGara = mTipoGara;
	}

	public String getmNomeEvento() {
		return mNomeEvento;
	}



	public void setmNomeEvento(String mNomeEvento) {
		this.mNomeEvento = mNomeEvento;
	}



	public Date getmDataEvento() {
		return mDataEvento;
	}



	public void setmDataEvento(Date mDataEvento) {
		this.mDataEvento = mDataEvento;
	}



	public int getmImmagineEvento() {
		return mImmagineEvento;
	}



	public void setmImmagineEvento(int mImmagineEvento) {
		this.mImmagineEvento = mImmagineEvento;
	}



	public LatLng getmGPS() {
		return mGPS;
	}



	public void setmGPS(LatLng mGPS) {
		this.mGPS = mGPS;
	}



	public String getmPrezzo() {
		return mPrezzo;
	}



	public void setmPrezzo(String mPrezzo) {
		this.mPrezzo = mPrezzo;
	}



	public ArrayList<ElementAtleta> getmPartecipanti() {
		return mPartecipanti;
	}



	public void setmPartecipanti(ArrayList<ElementAtleta> mPartecipanti) {
		this.mPartecipanti = mPartecipanti;
	}



	public String getmTipoGara() {
		return mTipoGara;
	}



	public void setmTipoGara(String mTipoGara) {
		this.mTipoGara = mTipoGara;
	}
}
