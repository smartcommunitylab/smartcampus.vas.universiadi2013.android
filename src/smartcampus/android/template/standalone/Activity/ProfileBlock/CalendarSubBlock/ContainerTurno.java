package smartcampus.android.template.standalone.Activity.ProfileBlock.CalendarSubBlock;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.smartcampus.template.standalone.Turno;
import android.util.Log;

public class ContainerTurno {

	private static ArrayList<Turno> mListaTurni = new ArrayList<Turno>();

	public static Map<String, Object> getTurniWithAmbitoELuogo(Long date,
			String luogo, String ambito) {
		//fillListTurni();
		ArrayList<Turno> mReturn = new ArrayList<Turno>();
		for (Turno turno : mListaTurni) {
			SimpleDateFormat dateFormatter = new SimpleDateFormat("dd.MM.yyyy",
					Locale.getDefault());
			String dateTurno = dateFormatter.format(turno.getData());
			String dateFilter = dateFormatter.format(date);
			// Log.i("DateTurno", dateTurno);
			// Log.i("DateFilter", dateFilter);
			if (ambito == null && luogo == null) {
				if (dateTurno.equalsIgnoreCase(dateFilter))
					mReturn.add(turno);

			} else if (ambito == null) {
				if (dateTurno.equalsIgnoreCase(dateFilter))
//						&& turno.getLuogo().equalsIgnoreCase(luogo))
					mReturn.add(turno);

			} else if (luogo == null) {
				if (dateTurno.equalsIgnoreCase(dateFilter)
						&& turno.getCategoria().equalsIgnoreCase(ambito))
					mReturn.add(turno);

			} else {
				if (dateTurno.equalsIgnoreCase(dateFilter)
						&& turno.getCategoria().equalsIgnoreCase(ambito)
						)
					mReturn.add(turno);
			}
		}

		Map<String, Object> mResult = new HashMap<String, Object>();
		mResult.put("connectionError", false);
		mResult.put("params", mReturn);
		return mResult;
	}

//	private static void fillListTurni() {
//		Calendar day = Calendar.getInstance();
//		for (int i = 0; i < 100; i++) {
//			// SimpleDateFormat dateFormatter = new
//			// SimpleDateFormat("dd.MM.yyyy",
//			// Locale.getDefault());
//			// Log.i("Today", dateFormatter.format(day.getTime()));
//			mListaTurni.add(new Turno(day.getTimeInMillis(), "Luogo 1",
//					"Ambito 1", "08:30", "10:30"));
//			mListaTurni.add(new Turno(day.getTimeInMillis(), "Luogo 1",
//					"Ambito 1", "14:30", "18:00"));
//			mListaTurni.add(new Turno(day.getTimeInMillis(), "Luogo 1",
//					"Ambito 2", "09:30", "10:30"));
//			mListaTurni.add(new Turno(day.getTimeInMillis(), "Luogo 1",
//					"Ambito 2", "14:30", "16:00"));
//			mListaTurni.add(new Turno(day.getTimeInMillis(), "Luogo 2",
//					"Ambito 1", "08:00", "10:30"));
//			mListaTurni.add(new Turno(day.getTimeInMillis(), "Luogo 2",
//					"Ambito 1", "14:30", "18:00"));
//			mListaTurni.add(new Turno(day.getTimeInMillis(), "Luogo 3",
//					"Ambito 3", "08:00", "18:00"));
//
//			day.setTimeInMillis(day.getTimeInMillis() + (3600 * 1000 * 24));
//		}
//	}

}
