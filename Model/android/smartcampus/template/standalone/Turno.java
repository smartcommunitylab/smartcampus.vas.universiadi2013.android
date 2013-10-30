package android.smartcampus.template.standalone;

import java.util.ArrayList;

public class Turno {

	private long data;
	private ArrayList<Utente> volontari;
	private String categoria;
	private String oraInizio;
	private String oraFine;
	
	public Turno(long data, ArrayList<Utente> volontari, String categoria,
			String oraInizio, String oraFine) {
		super();
		this.data = data;
		this.volontari = volontari;
		this.categoria = categoria;
		this.oraInizio = oraInizio;
		this.oraFine = oraFine;
	}

	public long getData() {
		return data;
	}

	public void setData(long data) {
		this.data = data;
	}

	public ArrayList<Utente> getVolontari() {
		return volontari;
	}

	public void setVolontari(ArrayList<Utente> volontari) {
		this.volontari = volontari;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getOraInizio() {
		return oraInizio;
	}

	public void setOraInizio(String oraInizio) {
		this.oraInizio = oraInizio;
	}

	public String getOraFine() {
		return oraFine;
	}

	public void setOraFine(String oraFine) {
		this.oraFine = oraFine;
	}
}
