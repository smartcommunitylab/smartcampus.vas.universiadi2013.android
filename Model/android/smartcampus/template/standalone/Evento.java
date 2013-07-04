package android.smartcampus.template.standalone;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table EVENTO.
 */
public class Evento {

    private Long ID;
    private String nome;
    private String data;
    private byte[] immagine;
    private Double latGPS;
    private Double lngGPS;
    private String indirizzo;
    private String descrizione;
    private Integer ruolo;
    private String ambito;
    private String tipoSport;

    public Evento() {
    }

    public Evento(Long ID) {
        this.ID = ID;
    }

    public Evento(Long ID, String nome, String data, byte[] immagine, Double latGPS, Double lngGPS, String indirizzo, String descrizione, Integer ruolo, String ambito, String tipoSport) {
        this.ID = ID;
        this.nome = nome;
        this.data = data;
        this.immagine = immagine;
        this.latGPS = latGPS;
        this.lngGPS = lngGPS;
        this.indirizzo = indirizzo;
        this.descrizione = descrizione;
        this.ruolo = ruolo;
        this.ambito = ambito;
        this.tipoSport = tipoSport;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public byte[] getImmagine() {
        return immagine;
    }

    public void setImmagine(byte[] immagine) {
        this.immagine = immagine;
    }

    public Double getLatGPS() {
        return latGPS;
    }

    public void setLatGPS(Double latGPS) {
        this.latGPS = latGPS;
    }

    public Double getLngGPS() {
        return lngGPS;
    }

    public void setLngGPS(Double lngGPS) {
        this.lngGPS = lngGPS;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public Integer getRuolo() {
        return ruolo;
    }

    public void setRuolo(Integer ruolo) {
        this.ruolo = ruolo;
    }

    public String getAmbito() {
        return ambito;
    }

    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    public String getTipoSport() {
        return tipoSport;
    }

    public void setTipoSport(String tipoSport) {
        this.tipoSport = tipoSport;
    }

}
