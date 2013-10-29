package android.smartcampus.template.standalone;

public class Turno {

	private long data;
	private String luogo;
	private String categoria;
	private String oraInizio;
	private String oraFine;
	
	public Turno(long data, String luogo, String categoria, String oraInizio,
			String oraFine) {
		super();
		this.data = data;
		this.luogo = luogo;
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

	public String getLuogo() {
		return luogo;
	}

	public void setLuogo(String luogo) {
		this.luogo = luogo;
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
