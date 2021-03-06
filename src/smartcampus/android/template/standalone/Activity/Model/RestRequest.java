package smartcampus.android.template.standalone.Activity.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONObject;

import eu.trentorise.smartcampus.universiade.R;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.smartcampus.template.standalone.Utente;
import android.util.Log;
import eu.trentorise.smartcampus.network.JsonUtils;
import eu.trentorise.smartcampus.protocolcarrier.ProtocolCarrier;
import eu.trentorise.smartcampus.protocolcarrier.common.Constants.Method;
import eu.trentorise.smartcampus.protocolcarrier.custom.MessageRequest;
import eu.trentorise.smartcampus.protocolcarrier.custom.MessageResponse;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.ConnectionException;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.ProtocolException;
import eu.trentorise.smartcampus.protocolcarrier.exceptions.SecurityException;

class RestRequest {

	private Context mContext;

	private String mToken;
	private String juniperToken;

	public RestRequest(Context cnt) {
		mContext = cnt;
		// TO DELETE
		juniperToken = mToken = mContext.getString(R.string.AUTH_TOKEN);
		mToken = mContext.getString(R.string.AUTH_TOKEN);
	}

	public void invalidateToken() {
		mToken = null;
		juniperToken = null;
	}

	public String getToken() {
		return mToken;
	}

	public Map<String, Object> authenticate(String username, String password) {
		Map<String, Object> mReturn = new HashMap<String, Object>();
		NetworkInfo ni = ((ConnectivityManager) (mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE)))
				.getActiveNetworkInfo();

		if (ni == null || ni.getState() != NetworkInfo.State.CONNECTED) {
			mReturn.put("connectionError", true);
			return mReturn;
		}
		if (username != null && password != null) {
			mToken = mContext.getString(R.string.AUTH_TOKEN);
			mReturn.put("connectionError", false);
			juniperToken = login(username, password);
			mReturn.put("params", login(username, password));
			return mReturn;
		} else {
			mToken = mContext.getString(R.string.AUTH_TOKEN);
			mReturn.put("connectionError", false);
			juniperToken = callGETRequest(new String[] { mContext
					.getString(R.string.URL_ANONYMOUS_LOGIN) });
			//
			juniperToken = mToken;
			//
			mReturn.put("params", juniperToken);
			return mReturn;
		}
	}

	public Map<String, Object> retrieveUserData() {
		Map<String, Object> mReturn = new HashMap<String, Object>();
		NetworkInfo ni = ((ConnectivityManager) (mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE)))
				.getActiveNetworkInfo();

		if (ni == null || ni.getState() != NetworkInfo.State.CONNECTED) {
			mReturn.put("connectionError", true);
			return mReturn;
		}
		URL url;
		try {
			String path = mContext.getString(R.string.URL_BACKEND_JUNIPER)
					+ mContext.getString(R.string.URL_USER_DATA);
			url = new URL(path);

			HttpURLConnection con = null;

			if (url.getProtocol().toLowerCase().equals("https")) {
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) url
						.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				con = https;
			} else {
				con = (HttpURLConnection) url.openConnection();
			}

			// HttpsURLConnection con = (HttpsURLConnection)
			// url.openConnection();
			con.setConnectTimeout(20000);
			// con.setDoInput(true);
			// con.setDoOutput(true);

			// HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.addRequestProperty("Authorization", juniperToken);
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			// con.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = "";
			String response = "";
			while ((line = reader.readLine()) != null) {
				response = response + line;
			}
			mReturn.put("connectionError", false);
			mReturn.put("params", response);
			return mReturn;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mReturn;
	}

	public Map<String, Object> getFunzioni(Utente user) {
		Map<String, Object> mReturn = new HashMap<String, Object>();
		NetworkInfo ni = ((ConnectivityManager) (mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE)))
				.getActiveNetworkInfo();

		if (ni == null || ni.getState() != NetworkInfo.State.CONNECTED) {
			mReturn.put("connectionError", true);
			return mReturn;
		}
		URL url;
		try {
			String path = mContext.getString(R.string.URL_BACKEND) + "/utente/"
					+ user.getId() + "/funzioni";
			url = new URL(path);

			HttpURLConnection con = null;

			if (url.getProtocol().toLowerCase().equals("https")) {
				trustAllHosts();
				HttpsURLConnection https = (HttpsURLConnection) url
						.openConnection();
				https.setHostnameVerifier(DO_NOT_VERIFY);
				con = https;
			} else {
				con = (HttpURLConnection) url.openConnection();
			}
			con.addRequestProperty("Authorization", juniperToken);
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = "";
			String response = "";
			while ((line = reader.readLine()) != null) {
				response = response + line;
			}
			mReturn.put("connectionError", false);
			mReturn.put("params", response);
			return mReturn;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mReturn;
	}

	/*
	 * Params: params --> params[0] URL params[1..size] otherParams
	 */
	public Map<String, Object> restRequest(String[] params, int restType) {

		Map<String, Object> mReturn = new HashMap<String, Object>();
		NetworkInfo ni = ((ConnectivityManager) (mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE)))
				.getActiveNetworkInfo();

		if (ni == null || ni.getState() != NetworkInfo.State.CONNECTED) {
			mReturn.put("connectionError", true);
			return mReturn;
		}
		switch (restType) {
		case RestRequestType.GET:
			mReturn.put("connectionError", false);
			mReturn.put("params", callGETRequest(params));
			return mReturn;
		case RestRequestType.POST:
			mReturn.put("connectionError", false);
			mReturn.put("params", callPOSTRequest(params));
			return mReturn;
		default:
			return null;
		}
	}

	private String callGETRequest(String[] params) {
		URL url;
		try {
			String path = mContext.getString(R.string.URL_BACKEND) + params[0];
			url = new URL(path);
			Log.d("1", path);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.addRequestProperty("Authorization", juniperToken);
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = "";
			String response = "";
			while ((line = reader.readLine()) != null) {
				response = response + line;
			}
			return response;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	private String login(String username, String password) {

		HttpClient httpClient = new DefaultHttpClient();
		HttpPost requestS = new HttpPost(
				mContext.getString(R.string.URL_BACKEND_JUNIPER)
						+ mContext.getString(R.string.URL_JUNIPER_LOGIN));
		StringEntity entity;
		try {
			entity = new StringEntity("grant_type=password&client_id="
					+ mContext.getString(R.string.JUNIPER_CLIENT_ID)
					+ "&client_secret=%22%22&username=" + username
					+ "&password=" + password, HTTP.UTF_8);

			entity.setContentType(mContext.getString(R.string.OBJECT_TYPE_POST));
			requestS.setEntity(entity);
			HttpResponse response = httpClient.execute(requestS);
			String responseString = EntityUtils.toString(response.getEntity());

			if (response != null) {
				JuniperResponse res = JsonUtils.toObject(responseString,
						JuniperResponse.class);
				if (res.isLogged())
					return "Bearer " + res.getAccess_token();
				return null;
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	private String callPOSTRequest(String[] params) {
		ProtocolCarrier mProtocolCarrier = new ProtocolCarrier(mContext,
				juniperToken);

		MessageRequest request = new MessageRequest(
				mContext.getString(R.string.URL_BACKEND), params[0]);
		try {
			request.setMethod(Method.POST);
			request.setBody(params[1]);
			String body = null;

			MessageResponse response;
			response = mProtocolCarrier.invokeSync(request, juniperToken,
					juniperToken);

			if (response.getHttpStatus() == 200) {

				body = response.getBody();
			}

			return body;

		} catch (ConnectionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	// always verify the host - dont check for certificate
	final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	};

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
