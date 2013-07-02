package smartcampus.android.template.standalone;

import android.graphics.Bitmap;

public class ElementAtleta
{
	private String mNomeAtleta;
	private String mCognomeAtleta;
	private String mEta;
	private String mNazionalita;
	private String mRisultato;
	private Bitmap mFoto;
	
	public ElementAtleta(String mNomeAtleta, String mCognomeAtleta,
			String mEta, String mNazionalita, String mRisultato, Bitmap mFoto) {
		super();
		this.mNomeAtleta = mNomeAtleta;
		this.mCognomeAtleta = mCognomeAtleta;
		this.mEta = mEta;
		this.mNazionalita = mNazionalita;
		this.mRisultato = mRisultato;
		this.mFoto = mFoto;
	}

	public String getmNomeAtleta() {
		return mNomeAtleta;
	}

	public void setmNomeAtleta(String mNomeAtleta) {
		this.mNomeAtleta = mNomeAtleta;
	}

	public String getmCognomeAtleta() {
		return mCognomeAtleta;
	}

	public void setmCognomeAtleta(String mCognomeAtleta) {
		this.mCognomeAtleta = mCognomeAtleta;
	}

	public String getmEta() {
		return mEta;
	}

	public void setmEta(String mEta) {
		this.mEta = mEta;
	}

	public String getmNazionalita() {
		return mNazionalita;
	}

	public void setmNazionalita(String mNazionalita) {
		this.mNazionalita = mNazionalita;
	}

	public String getmRisultato() {
		return mRisultato;
	}

	public void setmRisultato(String mRisultato) {
		this.mRisultato = mRisultato;
	}

	public Bitmap getmFoto() {
		return mFoto;
	}

	public void setmFoto(Bitmap mFoto) {
		this.mFoto = mFoto;
	}
}
