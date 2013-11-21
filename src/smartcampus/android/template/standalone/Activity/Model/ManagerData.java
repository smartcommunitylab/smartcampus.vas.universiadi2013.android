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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.http.util.ByteArrayBuffer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import smartcampus.android.template.standalone.Activity.ProfileBlock.CalendarSubBlock.FunzioneObj;
import smartcampus.android.template.standalone.Activity.SportBlock.SportImageConstant;
import smartcampus.android.template.standalone.IntroBlock.UserConstant;
import smartcampus.android.template.universiadi.R;
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
import android.util.Log;

public class ManagerData {

	private static ManagerData instance = null;
	public static RestRequest mRest;

	private static Context mContext;

	public static ManagerData getInstance(Context context) {
		return (instance == null) ? (instance = new ManagerData(context))
				: instance;
	}

	private ManagerData(Context context) {
		mContext = context;
		mRest = new RestRequest(context);
	}

	public static String getToken() {
		return mRest.getToken();
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

	public static Map<String, Object> saveUserInfo(Utente user) {
		try {
			JSONObject obj = new JSONObject();
			obj.put("id", user.getId());
			obj.put("uuid", user.getUuid());
			obj.put("lastname", user.getCognome());
			obj.put("firstname", user.getNome());
			obj.put("email", user.getMail());
			obj.put("mobile", user.getNumeroTelefonico());
			obj.put("afunction", user.getAmbito());
			obj.put("arole", user.getRuolo());
			obj.put("photo", user.getFoto());

			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { obj.toString(),
							mContext.getString(R.string.URL_SAVE_UTENTE) },
					RestRequestType.POST);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));
			if (!((Boolean) mMapRequest.get("connectionError"))) {
			}
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	public static Map<String, Object> getAnonymousToken() {
		return mRest.authenticate(null, null);
	}

	public static void invalidateToken() {
		mRest.invalidateToken();
	}

	public static Map<String, Object> getEventiForData(Long data) {
		ArrayList<Evento> mListaEventi = new ArrayList<Evento>();
		try {
			Log.i("Data", Long.toString(data));
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { mContext
							.getString(R.string.URL_EVENTO_PER_DATA) + data },
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
						String title = null;
						String description = null;
						if (Locale.getDefault().toString()
								.equalsIgnoreCase("it_IT")) {
							description = (obj.getJSONObject("customData")
									.getJSONObject("description").has("IT")) ? obj
									.getJSONObject("customData")
									.getJSONObject("description")
									.getString("IT")
									: obj.getJSONObject("customData")
											.getJSONObject("description")
											.getString("EN");
							title = (obj.getJSONObject("customData")
									.getJSONObject("title").has("IT")) ? obj
									.getJSONObject("customData")
									.getJSONObject("title").getString("IT")
									: obj.getJSONObject("customData")
											.getJSONObject("title")
											.getString("EN");
						} else {
							description = (obj.getJSONObject("customData")
									.getJSONObject("description").has("EN")) ? obj
									.getJSONObject("customData")
									.getJSONObject("description")
									.getString("EN")
									: obj.getJSONObject("customData")
											.getJSONObject("description")
											.getString("IT");
							title = (obj.getJSONObject("customData")
									.getJSONObject("title").has("EN")) ? obj
									.getJSONObject("customData")
									.getJSONObject("title").getString("EN")
									: obj.getJSONObject("customData")
											.getJSONObject("title")
											.getString("IT");
						}
						Evento evento = new Evento(
								null,
								title,
								obj.getLong("fromTime"),
								obj.getLong("fromTime"),
								obj.getLong("toTime"),
								Html.fromHtml(description).toString(),
								(!obj.isNull("location")) ? obj.getJSONArray(
										"location").getDouble(0) : 0,
								(!obj.isNull("location")) ? obj.getJSONArray(
										"location").getDouble(1) : 0,
								obj.getJSONObject("customData").getString(
										"category"),
								(!obj.getJSONObject("customData")
										.getString("imageUrl").equals("")) ? downloadImageFormURL(obj
										.getJSONObject("customData").getString(
												"imageUrl")) : new byte[1]);
						mListaEventi.add(evento);
					}
				}
			}
			Collections.sort(mListaEventi, new Comparator<Evento>() {
				@Override
				public int compare(Evento s1, Evento s2) {
					if (s1.getData() < s2.getData())
						return -1;
					else if (s1.getData() > s2.getData())
						return 1;
					return 0;
				}
			});
			mResult.put("params", mListaEventi);
			return mResult;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

	private static byte[] downloadImageFormURL(String url) {
		try {
			Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(
					url).getContent());
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
			return stream.toByteArray();
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
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { mContext
							.getString(R.string.URL_EVENTO_PER_SPORT)
							+ sport.replace(" ", "%20") }, RestRequestType.GET);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) (mMapRequest.get("connectionError")));
			if (!(Boolean) (mMapRequest.get("connectionError"))) {
				JSONArray arrayEventi = new JSONArray(
						(String) mMapRequest.get("params"));
				for (int i = 0; i < arrayEventi.length(); i++) {
					JSONObject obj = arrayEventi.getJSONObject(i);
					String title = null;
					String description = null;
					if (Locale.getDefault().getDisplayLanguage()
							.equalsIgnoreCase("it_IT")) {
						description = (obj.getJSONObject("customData")
								.getJSONObject("description").has("IT")) ? obj
								.getJSONObject("customData")
								.getJSONObject("description").getString("IT")
								: obj.getJSONObject("customData")
										.getJSONObject("description")
										.getString("EN");
						title = (obj.getJSONObject("customData").getJSONObject(
								"title").has("IT")) ? obj
								.getJSONObject("customData")
								.getJSONObject("title").getString("IT") : obj
								.getJSONObject("customData")
								.getJSONObject("title").getString("EN");
					} else {
						description = (obj.getJSONObject("customData")
								.getJSONObject("description").has("EN")) ? obj
								.getJSONObject("customData")
								.getJSONObject("description").getString("EN")
								: obj.getJSONObject("customData")
										.getJSONObject("description")
										.getString("IT");
						title = (obj.getJSONObject("customData").getJSONObject(
								"title").has("EN")) ? obj
								.getJSONObject("customData")
								.getJSONObject("title").getString("EN") : obj
								.getJSONObject("customData")
								.getJSONObject("title").getString("IT");
					}
					Evento evento = new Evento(
							null,
							title,
							obj.getLong("fromTime"),
							obj.getLong("fromTime"),
							obj.getLong("toTime"),
							Html.fromHtml(description).toString(),
							(!obj.isNull("location")) ? obj.getJSONArray(
									"location").getDouble(0) : 0,
							(!obj.isNull("location")) ? obj.getJSONArray(
									"location").getDouble(1) : 0,
							obj.getJSONObject("customData").getString(
									"category"),
							(!obj.getJSONObject("customData")
									.getString("imageUrl").equals("")) ? downloadImageFormURL(obj
									.getJSONObject("customData").getString(
											"imageUrl")) : new byte[1]);
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

	public static Map<String, Object> getMeetingPerData(Long data,
			FunzioneObj funzione) {
		ArrayList<Turno> mListaEventi = new ArrayList<Turno>();

		try {
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { mContext.getString(R.string.URL_MY_AGENDA)
							+ data + "/" + funzione.getId() },
					RestRequestType.GET);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapRequest.get("connectionError"));

			if (!((Boolean) mMapRequest.get("connectionError"))) {
				if (mMapRequest.get("params") != null) {
					JSONArray arrayEventi = new JSONArray(
							(String) mMapRequest.get("params"));
					if (arrayEventi != null) {
						SimpleDateFormat dateFormatter = new SimpleDateFormat(
								"HH:mm", Locale.getDefault());
						for (int i = 0; i < arrayEventi.length(); i++) {
							JSONObject obj = arrayEventi.getJSONObject(i);
							Turno agenda = new Turno(obj.getLong("start"),
									null, funzione.getFunzione(),
									dateFormatter.format(obj.getLong("start")),
									dateFormatter.format(obj.getLong("end")));
							mListaEventi.add(agenda);
						}
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

			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] {
							mContext.getString(R.string.URL_ATLETI_PER_EVENTO),
							params.toString() }, RestRequestType.POST);
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
			Map<String, Object> mMapReqeust = mRest.restRequest(
					new String[] { mContext
							.getString(R.string.URL_POI_PER_TIPO)
							+ type.replace(" ", "%20") }, RestRequestType.GET);
			// Map<String, Object> mMapReqeust = mRest.restRequest(
			// new String[] { mContext
			// .getString(R.string.URL_POI_PER_TIPO) },
			// RestRequestType.GET);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) mMapReqeust.get("connectionError"));
			if (!((Boolean) mMapReqeust.get("connectionError"))) {
				JSONArray arrayPOI = new JSONArray(
						(String) mMapReqeust.get("params"));
				if (arrayPOI != null) {
					for (int i = 0; i < arrayPOI.length(); i++) {
						JSONObject obj = arrayPOI.getJSONObject(i);
						String description = null;

						if (obj.getJSONObject("customData")
								.getJSONObject("description").length() != 0) {
							if (Locale.getDefault().toString()
									.equalsIgnoreCase("it_IT")) {
								description = (obj.getJSONObject("customData")
										.getJSONObject("description").has("IT")) ? obj
										.getJSONObject("customData")
										.getJSONObject("description")
										.getString("IT")
										: obj.getJSONObject("customData")
												.getJSONObject("description")
												.getString("EN");
							} else {
								description = (obj.getJSONObject("customData")
										.getJSONObject("description").has("EN")) ? obj
										.getJSONObject("customData")
										.getJSONObject("description")
										.getString("EN")
										: obj.getJSONObject("customData")
												.getJSONObject("description")
												.getString("IT");
							}
						}
						mListaPOI.add(new POI(null, obj.getString("title"), obj
								.getString("type"), description, obj
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

			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] {
							mContext.getString(R.string.URL_EVENTO_PER_POI),
							params.toString() }, RestRequestType.POST);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) (mMapRequest.get("connectionError")));

			if (!((Boolean) mMapRequest.get("connectionError"))) {
				JSONArray arrayEventi = new JSONArray(
						(String) mMapRequest.get("params"));
				for (int i = 0; i < arrayEventi.length(); i++) {
					JSONObject obj = arrayEventi.getJSONObject(i);
					Evento evento = new Evento(
							null,
							obj.getString("title"),
							obj.getLong("fromTime"),
							obj.getLong("fromTime"),
							obj.getLong("toTime"),
							Html.fromHtml(obj.getString("description"))
									.toString(),
							(!obj.isNull("location")) ? obj.getJSONArray(
									"location").getDouble(0) : 0,
							(!obj.isNull("location")) ? obj.getJSONArray(
									"location").getDouble(1) : 0,
							obj.getJSONObject("customData").getString(
									"category"),
							(!obj.getJSONObject("customData")
									.getString("imageUrl").equals("")) ? downloadImageFormURL(obj
									.getJSONObject("customData").getString(
											"imageUrl")) : new byte[1]);
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
		ArrayList<FunzioneObj> mListaCategorie = new ArrayList<FunzioneObj>();

		try {
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { mContext
							.getString(R.string.URL_CATEGORIE_VOLONTARI) },
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
					int id = utentiComitato.getJSONObject(i).getInt("id");
					FunzioneObj funzione = new FunzioneObj(categoria, id);
					mListaCategorie.add(funzione);
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
				ArrayList<FunzioneObj> listFunzioni = new ArrayList<FunzioneObj>();
				JSONArray arrayUtenti = new JSONArray(
						(String) mMapRequest.get("params"));
				for (int i = 0; i < arrayUtenti.length(); i++) {
					String categoria = arrayUtenti.getJSONObject(i)
							.getString("path")
							.replace("Organising Committee:", "");
					int id = arrayUtenti.getJSONObject(i).getInt("id");
					FunzioneObj funzione = new FunzioneObj(categoria, id);
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
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { "/utente/" + user.getId() + "/superiori" },
					RestRequestType.GET);
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
					new String[] {
							mContext.getString(R.string.URL_SEND_TICKET),
							ticketObj.toString() }, RestRequestType.POST);
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
					mContext.getString(R.string.URL_DOMANDA), domanda },
					RestRequestType.POST);
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
			Map<String, Object> mMapRequest = mRest
					.restRequest(new String[] { mContext
							.getString(R.string.URL_ALL_DOMANDA) },
							RestRequestType.POST);
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

	public static Map<String, Object> getTurniForDataAndFunzione(Long dateFrom,
			Long dateTo, String personale, FunzioneObj funzione) {
		ArrayList<Turno> mListaTurni = new ArrayList<Turno>();
		JSONArray arrayTurni = new JSONArray();
		Map<String, Object> mResult = new HashMap<String, Object>();
		Map<String, Object> mMapRequest = null;
		Long date = dateFrom;
		while (date <= dateTo) {
			if (personale.equalsIgnoreCase("Turni personale")) {
				mMapRequest = mRest.restRequest(
						new String[] { mContext.getString(R.string.URL_TURNI)
								+ date + "/" + funzione.getId() + "/"
								+ UserConstant.getUser().getId() },
						RestRequestType.GET);
				mResult.put("connectionError",
						(Boolean) mMapRequest.get("connectionError"));
				try {
					if (!((Boolean) mMapRequest.get("connectionError"))
							&& new JSONArray(mMapRequest.get("params")
									.toString()).length() != 0)
						arrayTurni.put((String) mMapRequest.get("params"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				mMapRequest = mRest.restRequest(
						new String[] { mContext.getString(R.string.URL_TURNI)
								+ date + "/" + funzione.getId() },
						RestRequestType.GET);
				mResult.put("connectionError",
						(Boolean) mMapRequest.get("connectionError"));
				try {
					if (!((Boolean) mMapRequest.get("connectionError"))
							&& new JSONArray(mMapRequest.get("params")
									.toString()).length() != 0)
						arrayTurni.put((String) mMapRequest.get("params"));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			date = date + (3600 * 1000 * 24);
		}

		try {
			if (arrayTurni.length() != 0) {
				if (!((Boolean) mMapRequest.get("connectionError"))) {
					for (int i = 0; i < arrayTurni.length(); i++) {
						JSONObject obj;
						obj = arrayTurni.getJSONObject(i);

						SimpleDateFormat dateFormatter = new SimpleDateFormat(
								"HH:mm", Locale.getDefault());
						JSONArray arrayVolontari = new JSONArray(
								obj.getString("volontari"));
						ArrayList<Utente> mListaVolontari = new ArrayList<Utente>();
						for (int j = 0; j < arrayVolontari.length(); j++) {
							JSONObject objUser = arrayVolontari
									.getJSONObject(j);
							mListaVolontari.add(new Utente(objUser.getString(
									"label").split(" ")[1], objUser.getString(
									"label").split(" ")[0], null, null,
									new byte[1], null, null, objUser
											.getString("id"), null));
						}
						Turno turno = new Turno(obj.getLong("start"),
								mListaVolontari, funzione.getFunzione(),
								dateFormatter.format(obj.getLong("start")),
								dateFormatter.format(obj.getLong("end")));
						mListaTurni.add(turno);
					}

				}
			}
			mResult.put("params", mListaTurni);
			return mResult;
		} catch (JSONException e) {
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

	public static Map<String, Object> getSport() {
		ArrayList<Sport> mListaSport = new ArrayList<Sport>();

		try {
			Map<String, Object> mMapRequest = mRest.restRequest(
					new String[] { mContext.getString(R.string.URL_SPORT) },
					RestRequestType.GET);
			Map<String, Object> mResult = new HashMap<String, Object>();
			mResult.put("connectionError",
					(Boolean) (mMapRequest.get("connectionError")));
			if (!(Boolean) (mMapRequest.get("connectionError"))) {
				if (mMapRequest.get("params") != null) {
					JSONArray arrayEventi = new JSONArray(
							(String) mMapRequest.get("params"));
					for (int i = 0; i < arrayEventi.length(); i++) {
						JSONObject obj = arrayEventi.getJSONObject(i);
						JSONArray poi = obj.getJSONArray("geolocations");
						ArrayList<POI> mPOICorrelati = new ArrayList<POI>();
						for (int j = 0; j < poi.length(); j++) {
							JSONObject poiObj = poi.getJSONObject(j);
							mPOICorrelati.add(new POI(null, poiObj
									.getString("title"), null, null, poiObj
									.getJSONArray("GPS").getDouble(0), poiObj
									.getJSONArray("GPS").getDouble(1)));
						}
						Sport sport = new Sport(obj.getString("nome"),
								SportImageConstant.resourcesFromID(
										obj.getInt("foto"), mContext),
								obj.getString("descrizione"),
								obj.getString("atleti"),
								obj.getString("specialita"), mPOICorrelati);
						mListaSport.add(sport);
					}
				}
			}
			mResult.put("params", mListaSport);
			return mResult;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
}
