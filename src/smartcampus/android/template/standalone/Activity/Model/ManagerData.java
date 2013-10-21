package smartcampus.android.template.standalone.Activity.Model;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartcampus.android.template.standalone.Activity.SportBlock.SportImageConstant;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.smartcampus.template.standalone.Atleta;
import android.smartcampus.template.standalone.Evento;
import android.smartcampus.template.standalone.ExtendedAnswer;
import android.smartcampus.template.standalone.Meeting;
import android.smartcampus.template.standalone.POI;
import android.smartcampus.template.standalone.Sport;
import android.smartcampus.template.standalone.Ticket;
import android.smartcampus.template.standalone.Turno;
import android.smartcampus.template.standalone.Utente;
import android.telephony.TelephonyManager;
import android.text.Html;

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

	public static Map<String, Object> getAuthToken(String user, String password) {
		return mRest.authenticate(user, password);
	}

	public static Map<String, Object> readUserData() {
		try {
			Map<String, Object> mMapRequest = mRest.retrieveUserData();
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));
			if (!((Boolean) mMapRequest.get("connectionError"))) {
				JSONObject userData = new JSONObject(
						(String) mMapRequest.get("params"));
				mResult.put(
						"params",
						new Utente(userData.getString("firstname"), userData
								.getString("lastname"), userData
								.getString("afunction"), userData
								.getString("arole"), userData
								.getString("photo").getBytes("UTF-8"), userData
								.getString("mobile"), userData
								.getString("email"), Integer.toString(userData
								.getInt("id")), userData.getString("uuid")));
			}
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getAnonymousToken() {
		return mRest.authenticate(null, null);
	}

	public static void invalidateToken() {
		mRest.restRequest(new String[] { "/invalidate" }, RestRequestType.GET);
	}

	public static Map<String, Object> getEventiForData(Long data) {
		ArrayList<Evento> mListaEventi = new ArrayList<Evento>();
		try {
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { "/evento/data/" + data },
					RestRequestType.GET);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));
			if (!((Boolean) mMapRequest.get("connectionError"))) {
				if (mMapRequest.get("params") != null) {
					JSONArray arrayEventi = new JSONArray(
							(String) mMapRequest.get("params"));
					for (int i = 0; i < arrayEventi.length(); i++) {
						JSONObject obj = arrayEventi.getJSONObject(i);

						Evento evento = new Evento(null,
								obj.getString("title"),
								obj.getLong("fromTime"), Html.fromHtml(
										obj.getString("description"))
										.toString(), obj.getJSONArray(
										"location").getDouble(0), obj
										.getJSONArray("location").getDouble(1),
								"Sport 1", downloadImageFormURL(obj
										.getJSONObject("customData").getString(
												"imageUrl")));
						mListaEventi.add(evento);
					}
				}
			}
			mResult.put("params", mListaEventi);
			return mResult;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static Bitmap downloadImageFormURL(String url) {
		try {
			return BitmapFactory.decodeStream((InputStream) new URL(url)
					.getContent());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> getEventiPerSport(String sport) {
		ArrayList<Evento> mListaEventi = new ArrayList<Evento>();

		try {
			Map<String, Object> mMapRequest = mRest
					.restRequest(
							new String[] { "/evento_sport/"
									+ sport.replace(" ", "%20") },
							RestRequestType.GET);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) (mMapRequest.get("connectionError")));
			if (!(Boolean) (mMapRequest.get("connectionError"))) {
				JSONArray arrayEventi = new JSONArray(
						(String) mMapRequest.get("params"));
				for (int i = 0; i < arrayEventi.length(); i++) {
					JSONObject obj;
					obj = arrayEventi.getJSONObject(i);
					Evento evento = new Evento(null, obj.getString("title"),
							obj.getLong("fromTime"),
							obj.getString("description"), obj.getJSONObject(
									"location").getDouble("0"), obj
									.getJSONObject("location").getDouble("1"),
							"Sport 1", downloadImageFormURL(obj.getJSONObject(
									"customData").getString("imageUrl")));
					mListaEventi.add(evento);

				}
			}
			mResult.put("params", mListaEventi);
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getMeetingPerData(Long data) {
		ArrayList<Meeting> mListaEventi = new ArrayList<Meeting>();

		try {
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { "/meeting/" + data }, RestRequestType.GET);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));

			if (!((Boolean) mMapRequest.get("connectionError"))) {
				if (mMapRequest.get("params") != null) {
					JSONArray arrayEventi = new JSONArray(
							(String) mMapRequest.get("params"));
					for (int i = 0; i < arrayEventi.length(); i++) {
						JSONObject obj;
						obj = arrayEventi.getJSONObject(i);
						Meeting meeting = new Meeting(null,
								obj.getString("nome"), obj.getLong("data"),
								obj.getString("descrizione"), obj
										.getJSONObject("gps").getDouble(
												"latGPS"), obj.getJSONObject(
										"gps").getDouble("lngGPS"),
								obj.getString("ambito"), obj.getString("ruolo"));
						mListaEventi.add(meeting);
					}
				}
			}
			mResult.put("params", mListaEventi);
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getAtletiPerEvento(Evento evento) {
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

			Map<String, Object> mMapRequest = mRest
					.restRequest(
							new String[] { "/atleti_evento", params.toString() },
							RestRequestType.POST);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) (mMapRequest.get("connectionError")));

			if (!((Boolean) (mMapRequest.get("connectionError")))) {
				JSONArray arrayEventi = new JSONArray(
						(String) mMapRequest.get("params"));
				if (arrayEventi != null) {
					for (int i = 0; i < arrayEventi.length(); i++) {
						JSONObject obj;
						try {
							obj = arrayEventi.getJSONObject(i);
							Atleta atleta = new Atleta(null,
									obj.getString("nome"),
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
			}

			mResult.put("params", mListaAtleta);
			return mResult;
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getPOIForType(String type) {
		ArrayList<POI> mListaPOI = new ArrayList<POI>();
		try {
			// Map<String, Object> mMapReqeust = mRest.restRequest(
			// new String[] { "/poi/" + type.replace(" ", "%20") },
			// RestRequestType.GET);
			Map<String, Object> mMapReqeust = mRest.restRequest(
					new String[] { "/poi/party" }, RestRequestType.GET);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapReqeust.get("connectionError"));
			if (!((Boolean) mMapReqeust.get("connectionError"))) {
				JSONArray arrayPOI = new JSONArray();
				if (arrayPOI != null) {
					for (int i = 0; i < arrayPOI.length(); i++) {
						JSONObject obj = arrayPOI.getJSONObject(i);
						mListaPOI.add(new POI(null, obj.getString("title"), obj
								.getString("type").split(" - ")[1], obj
								.getJSONArray("location").getDouble(0), obj
								.getJSONArray("location").getDouble(1)));
					}
				}
			}
			mResult.put("params", mListaPOI);
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> getEventiForPOI(POI poi) {
		ArrayList<Evento> mListaEventi = new ArrayList<Evento>();
		try {
			JSONObject params = new JSONObject().put("GPS", new JSONArray()
					.put(poi.getLatGPS()).put(poi.getLngGPS()));

			Map<String, Object> mMapRequest = mRest.restRequest(new String[] {
					"/poi_evento", params.toString() }, RestRequestType.POST);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) (mMapRequest.get("connectionError")));

			if (!((Boolean) mMapRequest.get("connectionError"))) {
				JSONArray arrayEventi = new JSONArray(
						(String) mMapRequest.get("params"));
				for (int i = 0; i < arrayEventi.length(); i++) {
					JSONObject obj = arrayEventi.getJSONObject(i);
					Evento evento = new Evento(null, obj.getString("title"),
							obj.getLong("fromTime"),
							obj.getString("description"), obj.getJSONArray(
									"location").getDouble(0), obj.getJSONArray(
									"location").getDouble(1), "Sport 1",
							downloadImageFormURL(obj
									.getJSONObject("customData").getString(
											"imageUrl")));
					mListaEventi.add(evento);
				}
			}

			mResult.put("params", mListaEventi);
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getCategorieVolontari() {
		ArrayList<String> mListaCategorie = new ArrayList<String>();

		try {
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { "/volontari/categorie" },
					RestRequestType.GET);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));
			if (!(Boolean) (mMapRequest.get("connectionError"))) {
				JSONArray arrayUtenti = new JSONArray(
						(String) mMapRequest.get("params"));
				JSONArray utentiComitato = arrayUtenti.getJSONObject(0)
						.getJSONArray("items");
				for (int i = 0; i < utentiComitato.length(); i++) {
					String categoria = utentiComitato.getJSONObject(i)
							.getString("path")
							.replace("Organising Committee:", "");
					mListaCategorie.add(categoria);
				}
			}
			mResult.put("params", mListaCategorie);
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getFunzioneForUser(Utente user) {
		try {
			Map<String, Object> mMapRequest = mRest.getFunzioni(user);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));
			if (!(Boolean) (mMapRequest.get("connectionError"))) {
				ArrayList<String> listFunzioni = new ArrayList<String>();
				JSONArray arrayUtenti = new JSONArray(
						(String) mMapRequest.get("params"));
				for (int i = 0; i < arrayUtenti.length(); i++) {
					String funzione = arrayUtenti.getString(i);
					listFunzioni.add(funzione);
				}
				mResult.put("params", listFunzioni);
			}
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getSuperioriForUser(Utente user,
			String pathFunzione) {
		ArrayList<Utente> mListaSuperiori = new ArrayList<Utente>();
		try {
			Map<String, Object> mMapRequest = mRest
					.restRequest(new String[] { "/getSuperiori?ref_object="
							+ user.getId() }, RestRequestType.GET);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));
			if (!((Boolean) mMapRequest.get("connectionError"))) {
				JSONArray arraySuperiori = new JSONArray(
						(String) mMapRequest.get("params"));
				for (int i = 0; i < arraySuperiori.length(); i++) {
					JSONObject obj = arraySuperiori.getJSONObject(i);
					String[] pathTokenized = pathFunzione.split(":");
					if (arrayContains(pathTokenized,
							(String) obj.get("function"))) {
						Utente superiore = new Utente(obj.getString("label")
								.split(" ")[1], obj.getString("label").split(
								" ")[0], obj.getString("function"),
								obj.getString("arole"), new byte[1],
								obj.getString("phone"), obj.getString("email"),
								obj.getString("id"), "");
						mListaSuperiori.add(superiore);
					}
				}
			}
			mResult.put("params", mListaSuperiori);
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static boolean arrayContains(String[] array, String pattern) {
		for (int i = 0; i < array.length; i++)
			if (array[i].equalsIgnoreCase(pattern))
				return true;
		return false;
	}

	public static String getRegIDForNomeCognome(String nome, String cognome) {
		return "regID";
	}

	// public static boolean sendSMS(Ticket ticket, ArrayList<Utente> receivers)
	// {
	// try {
	// JSONObject ticketObj = new JSONObject();
	// ticketObj.put("latGPS", ticket.getLatGPS());
	// ticketObj.put("lngGPS", ticket.getLngGPS());
	// ticketObj.put("descrizione", ticket.getDescrizione());
	// ticketObj.put("categoria", ticket.getCategoria());
	// ticketObj.put("foto", ticket.getFoto());
	//
	// JSONArray receiversArray = new JSONArray();
	// for (Utente utente : receivers)
	// receiversArray.put(utente);
	//
	// return Boolean.getBoolean(mRest.restRequest(
	// new String[] { "/send_sms", ticketObj.toString(),
	// receiversArray.toString() }, RestRequestType.POST));
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	//
	// return false;
	// }

	public static Map<String, Object> sendHelpdesk(Ticket ticket,
			Context context) {
		try {
			JSONObject ticketObj = new JSONObject();
			ticketObj.put("_id", "");
			ticketObj.put("status", "send");
			ticketObj.put("ambito", ticket.getCategoria());
			ticketObj.put("foto", ticket.getFoto());
			ticketObj.put(
					"gps",
					new JSONArray().put(ticket.getLatGPS()).put(
							ticket.getLngGPS()));
			ticketObj.put("descrizione", ticket.getDescrizione());
			ticketObj.put("indirizzo", ticket.getIndirizzo());

			TelephonyManager telemamanger = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			ticketObj.put("telefono", telemamanger.getLine1Number());

			return mRest.restRequest(
					new String[] { "/ticket/send", ticketObj.toString() },
					RestRequestType.POST);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getRispostaPerDomanda(String domanda) {
		ArrayList<ExtendedAnswer> mListaRisposte = new ArrayList<ExtendedAnswer>();

		try {
			Map<String, Object> mMapRequest = mRest.restRequest(new String[] {
					"/domanda", domanda }, RestRequestType.POST);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));
			if (!((Boolean) mMapRequest.get("connectionError"))) {
				JSONArray arrayRisposte = new JSONArray(
						(String) mMapRequest.get("params"));
				for (int i = 0; i < arrayRisposte.length(); i++) {
					JSONObject obj;
					obj = arrayRisposte.getJSONObject(i);
					ExtendedAnswer extAnswer = new ExtendedAnswer(
							obj.getString("risposta"),
							obj.getString("domanda"), obj.getInt("totalTag"),
							obj.getInt("usefulTag"));
					mListaRisposte.add(extAnswer);
				}
			}
			mResult.put("params", mListaRisposte);
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getAllRisposte() {
		ArrayList<ExtendedAnswer> mListaRisposte = new ArrayList<ExtendedAnswer>();
		try {
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { "/all_domanda" }, RestRequestType.POST);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));
			if (!(Boolean) (mMapRequest.get("connectionError"))) {
				JSONArray arrayRisposte = new JSONArray(
						(String) mMapRequest.get("params"));
				for (int i = 0; i < arrayRisposte.length(); i++) {
					JSONObject obj;
					obj = arrayRisposte.getJSONObject(i);
					ExtendedAnswer extAnswer = new ExtendedAnswer(
							obj.getString("domanda"),
							obj.getString("risposta"), obj.getInt("totalTag"),
							obj.getInt("usefulTag"));
					mListaRisposte.add(extAnswer);
				}
			}
			mResult.put("params", mListaRisposte);
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getTurniForDataAndLuogoAndCategoria(
			Long date, String luogo, String categoria) {
		ArrayList<Turno> mListaTurni = new ArrayList<Turno>();
		JSONArray arrayTurni = null;
		try {

			Map<String, Object> mResult = new HashMap<String, Object>();
			Map<String, Object> mMapRequest = null;
			if (luogo != null && categoria != null) {
				mMapRequest = mRest.restRequest(
						new String[] { "/turni/" + date + "/"
								+ luogo.replace(" ", "%20") + "/"
								+ categoria.replace(" ", "%20") },
						RestRequestType.GET);
				mResult.put("connectionError",
						(Boolean) mMapRequest.get("connectionError"));
				if (!((Boolean) mMapRequest.get("connectionError")))
					arrayTurni = new JSONArray(
							(String) mMapRequest.get("params"));
			} else if (luogo != null) {
				mMapRequest = mRest.restRequest(new String[] { "/turni/" + date
						+ "/" + luogo.replace(" ", "%20") + "/\"\"" },
						RestRequestType.GET);
				mResult.put("connectionError",
						(Boolean) mMapRequest.get("connectionError"));
				if (!((Boolean) mMapRequest.get("connectionError")))
					arrayTurni = new JSONArray(
							(String) mMapRequest.get("params"));
			} else if (categoria != null) {
				mMapRequest = mRest.restRequest(new String[] { "/turni/" + date
						+ "/\"\"/" + categoria.replace(" ", "%20") },
						RestRequestType.GET);
				mResult.put("connectionError",
						(Boolean) mMapRequest.get("connectionError"));
				if (!((Boolean) mMapRequest.get("connectionError")))
					arrayTurni = new JSONArray(
							(String) mMapRequest.get("params"));
			} else {
				mMapRequest = mRest.restRequest(new String[] { "/turni/" + date
						+ "/\"\"/\"\"" }, RestRequestType.GET);
				mResult.put("connectionError",
						(Boolean) mMapRequest.get("connectionError"));
				if (!((Boolean) mMapRequest.get("connectionError")))
					arrayTurni = new JSONArray(
							(String) mMapRequest.get("params"));
			}
			if (!((Boolean) mMapRequest.get("connectionError"))) {
				for (int i = 0; i < arrayTurni.length(); i++) {
					JSONObject obj = arrayTurni.getJSONObject(i);
					Turno turno = new Turno(obj.getLong("data"),
							obj.getString("luogo"), obj.getString("categoria"),
							obj.getLong("oraInizio"), obj.getLong("oraFine"));
					mListaTurni.add(turno);
				}

				mResult.put("params", mListaTurni);
			}
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getUtentiForTurno(Turno turno) {
		ArrayList<Utente> mListaUtenti = new ArrayList<Utente>();
		JSONObject params = new JSONObject();
		try {
			params.put("data", turno.getData());
			params.put("categoria", turno.getCategoria());
			params.put("luogo", turno.getLuogo());
			params.put("oraInizio", turno.getOraInizio());
			params.put("oraFine", turno.getOraFine());

			Map<String, Object> mMapReqeust = mRest.restRequest(new String[] {
					"/users_for_turn", params.toString() },
					RestRequestType.POST);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapReqeust.get("connectionError"));

			if (!((Boolean) (mMapReqeust.get("connectionError")))) {
				JSONArray arrayUtenti = new JSONArray(
						(String) mMapReqeust.get("params"));

				for (int i = 0; i < arrayUtenti.length(); i++) {
					JSONObject obj = arrayUtenti.getJSONObject(i);
					Utente utente = new Utente(obj.getString("firstname"),
							obj.getString("lastname"),
							obj.getString("afunction"), obj.getString("arole"),
							obj.getString("photo").getBytes("UTF-8"),
							obj.getString("mobile"), obj.getString("email"),
							Integer.toString(obj.getInt("id")),
							obj.getString("uuid"));
					mListaUtenti.add(utente);
				}
			}
			mResult.put("params", mListaUtenti);
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static Map<String, Object> getSearchForFilter(String search,
			String rest) {
		ArrayList<JSONObject> mResult = new ArrayList<JSONObject>();
		try {

			Map<String, Object> mMapRequest = mRest.restRequest(new String[] {
					rest, search }, RestRequestType.POST);
			Map<String, Object> mReturn = new HashMap<String, Object>();
			mReturn.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));

			if (!((Boolean) (mMapRequest.get("connectionError")))) {
				JSONArray arraySearch = new JSONArray(
						(String) mMapRequest.get("params"));
				for (int i = 0; i < arraySearch.length(); i++) {
					mResult.add(arraySearch.getJSONObject(i));
				}
			}
			mReturn.put("params", mResult);
			return mReturn;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getSport(Context cnt) {
		ArrayList<Sport> mResult = new ArrayList<Sport>();
		try {
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { "/sport" }, RestRequestType.GET);
			Map<String, Object> mReturn = new HashMap<String, Object>();
			mReturn.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));

			if (!((Boolean) (mMapRequest.get("connectionError")))) {
				JSONArray arraySport = new JSONArray();
				for (int i = 0; i < arraySport.length(); i++) {
					JSONObject obj = arraySport.getJSONObject(i);
					Sport sport = new Sport(obj.getString("nome"),
							SportImageConstant.resourcesFromID(
									obj.getInt("foto"), cnt),
							obj.getString("descrizione"));
					mResult.add(sport);
				}
			}
			mReturn.put("params", mResult);
			return mReturn;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
