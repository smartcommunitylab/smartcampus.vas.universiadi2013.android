package android.smartcampus.template.standalone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;

import android.smartcampus.template.standalone.Evento;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table EVENTO.
*/
public class EventoDao extends AbstractDao<Evento, Long> {

    public static final String TABLENAME = "EVENTO";

    /**
     * Properties of entity Evento.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property ID = new Property(0, Long.class, "ID", true, "ID");
        public final static Property Nome = new Property(1, String.class, "nome", false, "NOME");
        public final static Property Data = new Property(2, String.class, "data", false, "DATA");
        public final static Property Ora = new Property(3, String.class, "ora", false, "ORA");
        public final static Property Immagine = new Property(4, byte[].class, "immagine", false, "IMMAGINE");
        public final static Property LatGPS = new Property(5, Double.class, "latGPS", false, "LAT_GPS");
        public final static Property LngGPS = new Property(6, Double.class, "lngGPS", false, "LNG_GPS");
        public final static Property Indirizzo = new Property(7, String.class, "indirizzo", false, "INDIRIZZO");
        public final static Property Descrizione = new Property(8, String.class, "descrizione", false, "DESCRIZIONE");
        public final static Property Ruolo = new Property(9, Integer.class, "ruolo", false, "RUOLO");
        public final static Property Ambito = new Property(10, String.class, "ambito", false, "AMBITO");
        public final static Property TipoSport = new Property(11, String.class, "tipoSport", false, "TIPO_SPORT");
    };


    public EventoDao(DaoConfig config) {
        super(config);
    }
    
    public EventoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'EVENTO' (" + //
                "'ID' INTEGER PRIMARY KEY ," + // 0: ID
                "'NOME' TEXT," + // 1: nome
                "'DATA' TEXT," + // 2: data
                "'ORA' TEXT," + // 3: ora
                "'IMMAGINE' BLOB," + // 4: immagine
                "'LAT_GPS' REAL," + // 5: latGPS
                "'LNG_GPS' REAL," + // 6: lngGPS
                "'INDIRIZZO' TEXT," + // 7: indirizzo
                "'DESCRIZIONE' TEXT," + // 8: descrizione
                "'RUOLO' INTEGER," + // 9: ruolo
                "'AMBITO' TEXT," + // 10: ambito
                "'TIPO_SPORT' TEXT);"); // 11: tipoSport
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'EVENTO'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Evento entity) {
        stmt.clearBindings();
 
        Long ID = entity.getID();
        if (ID != null) {
            stmt.bindLong(1, ID);
        }
 
        String nome = entity.getNome();
        if (nome != null) {
            stmt.bindString(2, nome);
        }
 
        String data = entity.getData();
        if (data != null) {
            stmt.bindString(3, data);
        }
 
        String ora = entity.getOra();
        if (ora != null) {
            stmt.bindString(4, ora);
        }
 
        byte[] immagine = entity.getImmagine();
        if (immagine != null) {
            stmt.bindBlob(5, immagine);
        }
 
        Double latGPS = entity.getLatGPS();
        if (latGPS != null) {
            stmt.bindDouble(6, latGPS);
        }
 
        Double lngGPS = entity.getLngGPS();
        if (lngGPS != null) {
            stmt.bindDouble(7, lngGPS);
        }
 
        String indirizzo = entity.getIndirizzo();
        if (indirizzo != null) {
            stmt.bindString(8, indirizzo);
        }
 
        String descrizione = entity.getDescrizione();
        if (descrizione != null) {
            stmt.bindString(9, descrizione);
        }
 
        Integer ruolo = entity.getRuolo();
        if (ruolo != null) {
            stmt.bindLong(10, ruolo);
        }
 
        String ambito = entity.getAmbito();
        if (ambito != null) {
            stmt.bindString(11, ambito);
        }
 
        String tipoSport = entity.getTipoSport();
        if (tipoSport != null) {
            stmt.bindString(12, tipoSport);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Evento readEntity(Cursor cursor, int offset) {
        Evento entity = new Evento( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // ID
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nome
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // data
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ora
            cursor.isNull(offset + 4) ? null : cursor.getBlob(offset + 4), // immagine
            cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5), // latGPS
            cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6), // lngGPS
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // indirizzo
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // descrizione
            cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9), // ruolo
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // ambito
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11) // tipoSport
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Evento entity, int offset) {
        entity.setID(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setNome(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setData(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setOra(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setImmagine(cursor.isNull(offset + 4) ? null : cursor.getBlob(offset + 4));
        entity.setLatGPS(cursor.isNull(offset + 5) ? null : cursor.getDouble(offset + 5));
        entity.setLngGPS(cursor.isNull(offset + 6) ? null : cursor.getDouble(offset + 6));
        entity.setIndirizzo(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setDescrizione(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setRuolo(cursor.isNull(offset + 9) ? null : cursor.getInt(offset + 9));
        entity.setAmbito(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setTipoSport(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Evento entity, long rowId) {
        entity.setID(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Evento entity) {
        if(entity != null) {
            return entity.getID();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
