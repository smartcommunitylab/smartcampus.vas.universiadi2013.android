package android.smartcampus.template.standalone;

public class Sport {
	
	private String nome;
	private byte[] foto;
	private String descrizione;
	
	public Sport(String nome, byte[] foto, String descrizione) {
		super();
		this.nome = nome;
		this.foto = foto;
		this.descrizione = descrizione;
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
}
