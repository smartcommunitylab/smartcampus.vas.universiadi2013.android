package smartcampus.android.template.standalone.Activity.ProfileBlock.CalendarSubBlock;

import java.io.Serializable;

public class FunzioneObj implements Serializable {

	private String funzione;
	private String id;

	public FunzioneObj(String funzione, String id) {
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
