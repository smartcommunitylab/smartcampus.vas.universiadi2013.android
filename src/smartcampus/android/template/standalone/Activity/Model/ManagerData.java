package smartcampus.android.template.standalone.Activity.Model;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartcampus.android.template.standalone.Activity.SportBlock.SportImageConstant;
import android.content.Context;
import android.smartcampus.template.standalone.Atleta;
import android.smartcampus.template.standalone.Evento;
import android.smartcampus.template.standalone.ExtendedAnswer;
import android.smartcampus.template.standalone.Meeting;
import android.smartcampus.template.standalone.POI;
import android.smartcampus.template.standalone.Sport;
import android.smartcampus.template.standalone.Ticket;
import android.smartcampus.template.standalone.Turno;
import android.smartcampus.template.standalone.Utente;

public class ManagerData {

	private static ManagerData instance = null;
	private static RestRequest mRest;

	public static ManagerData getInstance(Context context) {
		return (instance == null) ? (instance = new ManagerData(context))
				: instance;
	}

	private ManagerData(Context context) {
		mRest = new RestRequest(context);
	}

	public static String getAuthToken(String user, String password) {
		return mRest.authenticate(user, password);
	}

	public static Utente readUserData() {
		try {
			JSONObject userData = new JSONObject(mRest.restRequest(
					new String[] { "/read_user_data" }, RestRequestType.GET));
			return new Utente(userData.getString("nome"),
					userData.getString("cognome"), userData.getString("ruolo"),
					userData.getString("ambito"), userData.getString("foto")
							.getBytes("UTF-8"),
					userData.getString("numeroTelefonico"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static String getAnonymousToken() {
		return mRest.authenticate(null, null);
	}

	public static void invalidateToken() {
		mRest.restRequest(new String[] { "/invalidate" }, RestRequestType.GET);
	}

	public static ArrayList<Evento> getEventiForData(Long data) {
		ArrayList<Evento> mListaEventi = new ArrayList<Evento>();
		try {
			JSONArray arrayEventi = new JSONArray(mRest.restRequest(
					new String[] { "/evento_data/" + data },
					RestRequestType.GET));
			if (arrayEventi != null) {
				for (int i = 0; i < arrayEventi.length(); i++) {
					JSONObject obj;
					obj = arrayEventi.getJSONObject(i);
					Evento evento = new Evento(null, obj.getString("nome"),
							obj.getLong("data"), obj.getString("descrizione"),
							obj.getJSONObject("gps").getDouble("latGPS"), obj
									.getJSONObject("gps").getDouble("lngGPS"),
							obj.getString("tipoSport"));
					mListaEventi.add(evento);
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mListaEventi;
	}

	public static ArrayList<Evento> getEventiPerSport(String sport) {
		ArrayList<Evento> mListaEventi = new ArrayList<Evento>();

		try {
			JSONArray arrayEventi = new JSONArray(
					mRest.restRequest(
							new String[] { "/evento_sport/"
									+ sport.replace(" ", "%20") },
							RestRequestType.GET));
			for (int i = 0; i < arrayEventi.length(); i++) {
				JSONObject obj;
				obj = arrayEventi.getJSONObject(i);
				Evento evento = new Evento(null, obj.getString("nome"),
						obj.getLong("data"), obj.getString("descrizione"), obj
								.getJSONObject("gps").getDouble("latGPS"), obj
								.getJSONObject("gps").getDouble("lngGPS"),
						obj.getString("tipoSport"));
				mListaEventi.add(evento);

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mListaEventi;
	}

	public static ArrayList<Meeting> getMeetingPerData(Long data) {
		ArrayList<Meeting> mListaEventi = new ArrayList<Meeting>();

		try {
			JSONArray arrayEventi = new JSONArray(mRest.restRequest(
					new String[] { "/meeting/" + data }, RestRequestType.GET));
			for (int i = 0; i < arrayEventi.length(); i++) {
				JSONObject obj;
				obj = arrayEventi.getJSONObject(i);
				Meeting meeting = new Meeting(null, obj.getString("nome"),
						obj.getLong("data"), obj.getString("descrizione"), obj
								.getJSONObject("gps").getDouble("latGPS"), obj
								.getJSONObject("gps").getDouble("lngGPS"),
						obj.getString("ambito"), obj.getString("ruolo"));
				mListaEventi.add(meeting);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mListaEventi;
	}

	public static ArrayList<Atleta> getAtletiPerEvento(Evento evento) {
		ArrayList<Atleta> mListaAtleta = new ArrayList<Atleta>();
		JSONObject params = new JSONObject();
		try {
			params.put("nome", evento.getNome());
			params.put("data", evento.getData());
			params.put("descrizione", evento.getDescrizione());

			JSONObject gps = new JSONObject();
			gps.put("latGPS", evento.getLatGPS());
			gps.put("lngGPS", evento.getLngGPS());
			params.put("gps", gps);
			params.put("tipoSport", evento.getTipoSport());

			JSONArray arrayEventi = new JSONArray(mRest.restRequest(
					new String[] { "/atleti_evento", params.toString() },
					RestRequestType.POST));
			if (arrayEventi != null) {
				for (int i = 0; i < arrayEventi.length(); i++) {
					JSONObject obj;
					try {
						obj = arrayEventi.getJSONObject(i);
						Atleta atleta = new Atleta(null, obj.getString("nome"),
								obj.getString("cognome"),
								obj.getString("nazionalita"),
								obj.getString("disciplina"), obj.getString(
										"foto").getBytes("UTF-8"));
						mListaAtleta.add(atleta);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return mListaAtleta;
	}

	public static ArrayList<POI> getPOIForType(String type) {
		ArrayList<POI> mListaPOI = new ArrayList<POI>();
		try {
			JSONArray arrayPOI = new JSONArray(mRest.restRequest(
					new String[] { "/poi/" + type.replace(" ", "%20") },
					RestRequestType.GET));
			if (arrayPOI != null) {
				for (int i = 0; i < arrayPOI.length(); i++) {
					JSONObject obj;
					obj = arrayPOI.getJSONObject(i);
					mListaPOI.add(new POI(null, obj.getString("nome"), obj
							.getString("categoria"), obj.getJSONObject("gps")
							.getDouble("latGPS"), obj.getJSONObject("gps")
							.getDouble("lngGPS")));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mListaPOI;
	}

	public static ArrayList<Evento> getEventiForPOI(POI poi) {
		ArrayList<Evento> mListaEventi = new ArrayList<Evento>();
		try {

			JSONObject params = new JSONObject().put("latGPS", poi.getLatGPS())
					.put("lngGPS", poi.getLngGPS());

			JSONArray arrayEventi = new JSONArray(mRest.restRequest(
					new String[] { "/poi_evento", params.toString() },
					RestRequestType.POST));
			for (int i = 0; i < arrayEventi.length(); i++) {
				JSONObject obj = arrayEventi.getJSONObject(i);
				Evento evento = new Evento(null, obj.getString("nome"),
						obj.getLong("data"), obj.getString("descrizione"), obj
								.getJSONObject("gps").getDouble("latGPS"), obj
								.getJSONObject("gps").getDouble("lngGPS"),
						obj.getString("tipoSport"));
				mListaEventi.add(evento);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mListaEventi;
	}

	public static ArrayList<String> getCategorieVolontari() {
		ArrayList<String> mListaCategorie = new ArrayList<String>();

		try {
			JSONArray arrayUtenti = new JSONArray(mRest.restRequest(
					new String[] { "/categoria_volontari" },
					RestRequestType.GET));
			for (int i = 0; i < arrayUtenti.length(); i++) {
				mListaCategorie.add(arrayUtenti.getString(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mListaCategorie;
	}

	public static ArrayList<Utente> getUserForCategoriaAndAmbito(String ambito,
			String ruolo) {
		ArrayList<Utente> mListaUtenti = new ArrayList<Utente>();
		JSONArray arrayUtenti = null;

		try {
			if (ambito == null)
				arrayUtenti = new JSONArray(mRest.restRequest(
						new String[] { "/utenti/\"\"/"
								+ ruolo.replace(" ", "%20") },
						RestRequestType.GET));
			else if (ruolo == null)
				arrayUtenti = new JSONArray(mRest.restRequest(
						new String[] { "/utenti/" + ambito.replace(" ", "%20")
								+ "/\"\"" }, RestRequestType.GET));
			else
				arrayUtenti = new JSONArray(mRest.restRequest(
						new String[] { "/utenti/" + ambito.replace(" ", "%20")
								+ "/" + ruolo.replace(" ", "%20") },
						RestRequestType.GET));

			for (int i = 0; i < arrayUtenti.length(); i++) {
				JSONObject obj = arrayUtenti.getJSONObject(i);
				Utente utente = new Utente(obj.getString("nome"),
						obj.getString("cognome"), obj.getString("ruolo"),
						obj.getString("ambito"), obj.getString("foto")
								.getBytes("UTF-8"),
						obj.getString("numeroTelefonico"));
				mListaUtenti.add(utente);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mListaUtenti;
	}

	public static String getRegIDForNomeCognome(String nome, String cognome) {
		return "regID";
	}

	public static boolean sendSMS(Ticket ticket, ArrayList<Utente> receivers) {
		try {
			JSONObject ticketObj = new JSONObject();
			ticketObj.put("latGPS", ticket.getLatGPS());
			ticketObj.put("lngGPS", ticket.getLngGPS());
			ticketObj.put("descrizione", ticket.getDescrizione());
			ticketObj.put("categoria", ticket.getCategoria());
			ticketObj.put("foto", ticket.getFoto());

			JSONArray receiversArray = new JSONArray();
			for (Utente utente : receivers)
				receiversArray.put(utente);

			return Boolean.getBoolean(mRest.restRequest(
					new String[] { "/send_sms", ticketObj.toString(),
							receiversArray.toString() }, RestRequestType.POST));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static boolean sendHelpdesk(Ticket ticket) {
		try {
			JSONObject ticketObj = new JSONObject();
			ticketObj.put("latGPS", ticket.getLatGPS());
			ticketObj.put("lngGPS", ticket.getLngGPS());
			ticketObj.put("descrizione", ticket.getDescrizione());
			ticketObj.put("categoria", ticket.getCategoria());
			ticketObj.put("foto", ticket.getFoto());

			return Boolean.getBoolean(mRest.restRequest(new String[] {
					"/send_helpdesk", ticketObj.toString() },
					RestRequestType.POST));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return false;
	}

	public static ArrayList<ExtendedAnswer> getRispostaPerDomanda(String domanda) {
		ArrayList<ExtendedAnswer> mListaRisposte = new ArrayList<ExtendedAnswer>();

		try {
			JSONArray arrayRisposte = new JSONArray(mRest.restRequest(
					new String[] { "/domanda", domanda }, RestRequestType.POST));
			for (int i = 0; i < arrayRisposte.length(); i++) {
				JSONObject obj;
				obj = arrayRisposte.getJSONObject(i);
				ExtendedAnswer extAnswer = new ExtendedAnswer(
						obj.getString("risposta"), obj.getString("domanda"),
						obj.getInt("totalTag"), obj.getInt("usefulTag"));
				mListaRisposte.add(extAnswer);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mListaRisposte;
	}

	public static ArrayList<ExtendedAnswer> getAllRisposte() {
		ArrayList<ExtendedAnswer> mListaRisposte = new ArrayList<ExtendedAnswer>();
		try {
			JSONArray arrayRisposte = new JSONArray(mRest.restRequest(
					new String[] { "/all_domanda" }, RestRequestType.POST));
			for (int i = 0; i < arrayRisposte.length(); i++) {
				JSONObject obj;
				obj = arrayRisposte.getJSONObject(i);
				ExtendedAnswer extAnswer = new ExtendedAnswer(
						obj.getString("domanda"), obj.getString("risposta"),
						obj.getInt("totalTag"), obj.getInt("usefulTag"));
				mListaRisposte.add(extAnswer);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mListaRisposte;
	}

	public static ArrayList<Turno> getTurniForDataAndLuogoAndCategoria(
			Long date, String luogo, String categoria) {
		ArrayList<Turno> mListaTurni = new ArrayList<Turno>();
		JSONArray arrayTurni = null;
		try {

			if (luogo != null && categoria != null)
				arrayTurni = new JSONArray(mRest.restRequest(
						new String[] { "/turni/" + date + "/"
								+ luogo.replace(" ", "%20") + "/"
								+ categoria.replace(" ", "%20") },
						RestRequestType.GET));
			else if (luogo != null)
				arrayTurni = new JSONArray(mRest.restRequest(
						new String[] { "/turni/" + date + "/"
								+ luogo.replace(" ", "%20") + "/\"\"" },
						RestRequestType.GET));
			else if (categoria != null)
				arrayTurni = new JSONArray(mRest.restRequest(
						new String[] { "/turni/" + date + "/\"\"/"
								+ categoria.replace(" ", "%20") },
						RestRequestType.GET));
			else
				arrayTurni = new JSONArray(mRest.restRequest(
						new String[] { "/turni/" + date + "/\"\"/\"\"" },
						RestRequestType.GET));
			for (int i = 0; i < arrayTurni.length(); i++) {
				JSONObject obj = arrayTurni.getJSONObject(i);
				Turno turno = new Turno(obj.getLong("data"),
						obj.getString("luogo"), obj.getString("categoria"),
						obj.getLong("oraInizio"), obj.getLong("oraFine"));
				mListaTurni.add(turno);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mListaTurni;
	}

	public static ArrayList<Utente> getUtentiForTurno(Turno turno) {
		ArrayList<Utente> mListaUtenti = new ArrayList<Utente>();
		JSONObject params = new JSONObject();
		try {
			params.put("data", turno.getData());
			params.put("categoria", turno.getCategoria());
			params.put("luogo", turno.getLuogo());
			params.put("oraInizio", turno.getOraInizio());
			params.put("oraFine", turno.getOraFine());

			JSONArray arrayUtenti = new JSONArray(mRest.restRequest(
					new String[] { "/users_for_turn", params.toString() },
					RestRequestType.POST));

			for (int i = 0; i < arrayUtenti.length(); i++) {
				JSONObject obj = arrayUtenti.getJSONObject(i);
				Utente utente = new Utente(obj.getString("nome"),
						obj.getString("cognome"), obj.getString("ruolo"),
						obj.getString("ambito"), obj.getString("foto")
								.getBytes("UTF-8"),
						obj.getString("numeroTelefonico"));
				mListaUtenti.add(utente);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mListaUtenti;
	}

	public static ArrayList<JSONObject> getSearchForFilter(String search,
			String rest) {
		ArrayList<JSONObject> mResult = new ArrayList<JSONObject>();
		try {
			JSONArray arraySearch = new JSONArray(mRest.restRequest(
					new String[] { rest, search }, RestRequestType.POST));
			for (int i = 0; i < arraySearch.length(); i++) {
				mResult.add(arraySearch.getJSONObject(i));
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mResult;
	}

	public static ArrayList<Sport> getSport(Context cnt) {
		ArrayList<Sport> mResult = new ArrayList<Sport>();
		try {
			JSONArray arraySport = new JSONArray(mRest.restRequest(
					new String[] { "/sport" }, RestRequestType.GET));
			for (int i = 0; i < arraySport.length(); i++) {
				JSONObject obj = arraySport.getJSONObject(i);
				Sport sport = new Sport(obj.getString("nome"),
						SportImageConstant.resourcesFromID(obj.getInt("foto"),
								cnt), obj.getString("descrizione"));
				mResult.add(sport);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mResult;
	}
}
