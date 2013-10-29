package smartcampus.android.template.standalone.Activity.SportBlock;

import java.util.ArrayList;

import android.content.Context;

public class SportContainer {

	private static ArrayList<android.smartcampus.template.standalone.Sport> mListaSport = new ArrayList<android.smartcampus.template.standalone.Sport>();

	public static ArrayList<android.smartcampus.template.standalone.Sport> getListaSport(
			Context mContext) {
		if (mListaSport.size() == 0)
			fillLista(mContext);
		return mListaSport;
	}

	private static void fillLista(Context mContext) {
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Alpine Skiing", SportImageConstant.resourcesFromID(
						SportImageConstant.ALPINE, mContext), "Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Biathlon", SportImageConstant.resourcesFromID(
						SportImageConstant.BIATLHON, mContext), "Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Cross Country Skiing", SportImageConstant.resourcesFromID(
						SportImageConstant.CROSS, mContext), "Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Curling", SportImageConstant.resourcesFromID(
						SportImageConstant.CURLING, mContext), "Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Figure & Syncronized Skating", SportImageConstant
						.resourcesFromID(SportImageConstant.FIGURE, mContext),
				"Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Freestyle Skiing", SportImageConstant.resourcesFromID(
						SportImageConstant.FREESTYLE, mContext), "Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Ice Hockey", SportImageConstant.resourcesFromID(
						SportImageConstant.ICE, mContext), "Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Nordic Combined", SportImageConstant.resourcesFromID(
						SportImageConstant.NORDIC, mContext), "Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Short Track Speed Skating", SportImageConstant
						.resourcesFromID(SportImageConstant.SHORTTRACK,
								mContext), "Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Ski Jumping", SportImageConstant.resourcesFromID(
						SportImageConstant.SKIIJUMPING, mContext), "Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Snowboarding", SportImageConstant.resourcesFromID(
						SportImageConstant.SNOWBOARDING, mContext), "Descrizione Sport"));
		mListaSport.add(new android.smartcampus.template.standalone.Sport(
				"Speed Skating", SportImageConstant.resourcesFromID(
						SportImageConstant.SPEEDSKATING, mContext), "Descrizione Sport"));
	}

}
