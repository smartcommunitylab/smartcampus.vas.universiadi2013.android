package smartcampus.android.template.standalone;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ElementSport {

	private double[] mPosStadio;
	private ArrayList<ElementAtleta> mAtleti;
	private int mImageSport;
	private String mDescrizione;
	
	public ElementSport(double[] mPosStadio, ArrayList<ElementAtleta> mAtleti,
			int mImageSport, String mDescrizione) {
		super();
		this.mPosStadio = mPosStadio;
		this.mAtleti = mAtleti;
		this.mImageSport = mImageSport;
		this.mDescrizione = mDescrizione;
	}

	public double[] getmPosStadio() {
		return mPosStadio;
	}

	public void setmPosStadio(double[] mPosStadio) {
		this.mPosStadio = mPosStadio;
	}

	public ArrayList<ElementAtleta> getmAtleti() {
		return mAtleti;
	}

	public void setmAtleti(ArrayList<ElementAtleta> mAtleti) {
		this.mAtleti = mAtleti;
	}

	public int getmImageSport() {
		return mImageSport;
	}

	public void setmImageSport(int mImageSport) {
		this.mImageSport = mImageSport;
	}

	public String getmDescrizione() {
		return mDescrizione;
	}

	public void setmDescrizione(String mDescrizione) {
		this.mDescrizione = mDescrizione;
	}
}
