package android.smartcampus.template.standalone;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.DaoConfig;
import de.greenrobot.dao.Property;

import android.smartcampus.template.standalone.Utente;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/**
 * DAO for table UTENTE.
 */
public class UtenteDao extends AbstractDao<Utente, Void> {

	public static final String TABLENAME = "UTENTE";

	/**
	 * Properties of entity Utente.<br/>
	 * Can be used for QueryBuilder and for referencing column names.
	 */
	public static class Properties {
		public final static Property Nome = new Property(0, String.class,
				"nome", false, "NOME");
		public final static Property Cognome = new Property(1, String.class,
				"cognome", false, "COGNOME");
		public final static Property Ruolo = new Property(2, String.class,
				"ruolo", false, "RUOLO");
		public final static Property Ambito = new Property(3, String.class,
				"ambito", false, "AMBITO");
		public final static Property Foto = new Property(4, byte[].class,
				"foto", false, "FOTO");
		public final static Property NumeroTelefonico = new Property(5,
				String.class, "numeroTelefonico", false, "NUMERO_TELEFONICO");
	};

	public UtenteDao(DaoConfig config) {
		super(config);
	}

	public UtenteDao(DaoConfig config, DaoSession daoSession) {
		super(config, daoSession);
	}

	/** Creates the underlying database table. */
	public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
		String constraint = ifNotExists ? "IF NOT EXISTS " : "";
		db.execSQL("CREATE TABLE " + constraint + "'UTENTE' (" + //
				"'NOME' TEXT," + // 0: nome
				"'COGNOME' TEXT," + // 1: cognome
				"'RUOLO' TEXT," + // 2: ruolo
				"'AMBITO' TEXT," + // 3: ambito
				"'FOTO' BLOB," + // 4: foto
				"'NUMERO_TELEFONICO' TEXT);"); // 5: numeroTelefonico
	}

	/** Drops the underlying database table. */
	public static void dropTable(SQLiteDatabase db, boolean ifExists) {
		String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "")
				+ "'UTENTE'";
		db.execSQL(sql);
	}

	/** @inheritdoc */
	@Override
	protected void bindValues(SQLiteStatement stmt, Utente entity) {
		stmt.clearBindings();

		String nome = entity.getNome();
		if (nome != null) {
			stmt.bindString(1, nome);
		}

		String cognome = entity.getCognome();
		if (cognome != null) {
			stmt.bindString(2, cognome);
		}

		String ruolo = entity.getRuolo();
		if (ruolo != null) {
			stmt.bindString(3, ruolo);
		}

		String ambito = entity.getAmbito();
		if (ambito != null) {
			stmt.bindString(4, ambito);
		}

		byte[] foto = entity.getFoto();
		if (foto != null) {
			stmt.bindBlob(5, foto);
		}

		String numeroTelefonico = entity.getNumeroTelefonico();
		if (numeroTelefonico != null) {
			stmt.bindString(6, numeroTelefonico);
		}
	}

	/** @inheritdoc */
	@Override
	public Void readKey(Cursor cursor, int offset) {
		return null;
	}

	/** @inheritdoc */
	@Override
	public Utente readEntity(Cursor cursor, int offset) {
		Utente entity = new Utente(
				//
				cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // nome
				cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // cognome
				cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // ruolo
				cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // ambito
				cursor.isNull(offset + 4) ? null : cursor.getBlob(offset + 4), // foto
				cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // numeroTelefonico
				cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // mail
				cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // id
				cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8) // uuid
		);
		return entity;
	}

	/** @inheritdoc */
	@Override
	public void readEntity(Cursor cursor, Utente entity, int offset) {
		entity.setNome(cursor.isNull(offset + 0) ? null : cursor
				.getString(offset + 0));
		entity.setCognome(cursor.isNull(offset + 1) ? null : cursor
				.getString(offset + 1));
		entity.setRuolo(cursor.isNull(offset + 2) ? null : cursor
				.getString(offset + 2));
		entity.setAmbito(cursor.isNull(offset + 3) ? null : cursor
				.getString(offset + 3));
		entity.setFoto(cursor.isNull(offset + 4) ? null : cursor
				.getBlob(offset + 4));
		entity.setNumeroTelefonico(cursor.isNull(offset + 5) ? null : cursor
				.getString(offset + 5));
	}

	/** @inheritdoc */
	@Override
	protected Void updateKeyAfterInsert(Utente entity, long rowId) {
		// Unsupported or missing PK type
		return null;
	}

	/** @inheritdoc */
	@Override
	public Void getKey(Utente entity) {
		return null;
	}

	/** @inheritdoc */
	@Override
	protected boolean isEntityUpdateable() {
		return true;
	}

}
