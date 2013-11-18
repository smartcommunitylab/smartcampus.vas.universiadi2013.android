package smartcampus.android.template.standalone.Activity.ProfileBlock.CalendarSubBlock;

import java.io.Serializable;

public class FunzioneObj implements Serializable {

	private String funzione;
	private int id;

	public FunzioneObj(String funzione, int id) {
		super();
		this.funzione = funzione;
		this.id = id;
	}

	public String getFunzione() {
		return funzione;
	}

	public void setFunzione(String funzione) {
		this.funzione = funzione;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}
