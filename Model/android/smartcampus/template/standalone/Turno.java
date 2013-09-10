package android.smartcampus.template.standalone;

public class Turno {

	private long data;
	private String luogo;
	private String categoria;
	private long oraInizio;
	private long oraFine;

	public Turno(long data, String luogo, String categoria, long oraInizio, long oraFine) {
		super();
		this.data = data;
		this.luogo = luogo;
		this.oraInizio = oraInizio;
		this.oraFine = oraFine;
		this.categoria = categoria;
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

	public long getOraInizio() {
		return oraInizio;
	}

	public void setOraInizio(long oraInizio) {
		this.oraInizio = oraInizio;
	}

	public long getOraFine() {
		return oraFine;
	}

	public void setOraFine(long oraFine) {
		this.oraFine = oraFine;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}
}
