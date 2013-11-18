package android.smartcampus.template.standalone;

import java.io.Serializable;

public class Sport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String nome;
	private byte[] foto;
	private String descrizione;
	private String atleti;
	private String specialita;

	public Sport(String nome, byte[] foto, String descrizione, String atleti,
			String specialita) {
		super();
		this.nome = nome;
		this.foto = foto;
		this.descrizione = descrizione;
		this.atleti = atleti;
		this.specialita = specialita;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getAtleti() {
		return atleti;
	}

	public void setAtleti(String atleti) {
		this.atleti = atleti;
	}

	public String getSpecialita() {
		return specialita;
	}

	public void setSpecialita(String specialita) {
		this.specialita = specialita;
	}
}
