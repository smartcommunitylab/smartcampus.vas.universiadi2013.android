package smartcampus.android.template.standalone.Activity.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import smartcampus.android.template.universiadi.R;
import android.content.Context;
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
import eu.trentorise.smartcampus.storage.Utils;

class RestRequest {

	private Context mContext;

	private String mToken;
	private String juniperToken;

	public RestRequest(Context cnt) {
		mContext = cnt;
	}

	public String authenticate(String username, String password) {
		if (username != null && password != null) {
			mToken = mContext.getString(R.string.AUTH_TOKEN);
			return (juniperToken = login(username, password));
		} else {
			mToken = mContext.getString(R.string.AUTH_TOKEN);
			return callGETRequest(new String[] { "/anonymus_login" });
		}
	}

	public String retrieveUserData() {
		URL url;
		try {
			String path = mContext.getString(R.string.URL_BACKEND_JUNIPER)
					+ "/getUserData";
			url = new URL(path);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.addRequestProperty("Authorization",
					mContext.getString(R.string.JUNIPER_CLIENT_ID));
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
		return null;
	}

	public String getFunzioni(Utente user) {
		URL url;
		try {
			String path = mContext.getString(R.string.URL_BACKEND_JUNIPER)
					+ "/utente/" + user.getId() + "/funzioni";
			url = new URL(path);
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
		return null;
	}

	/*
	 * Params: params --> params[0] URL params[1..size] otherParams
	 */
	public String restRequest(String[] params, int restType) {
		switch (restType) {
		case RestRequestType.GET:
			return callGETRequest(params);
		case RestRequestType.POST:
			return callPOSTRequest(params);
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
		return null;
	}

	private String login(String username, String password) {
		ProtocolCarrier mProtocolCarrie = new ProtocolCarrier(this.mContext,
				mToken);

		MessageRequest request = new MessageRequest(
				mContext.getString(R.string.URL_BACKEND_JUNIPER),
				mContext.getString(R.string.URL_JUNIPER_LOGIN));
		request.setBody("grant_type=password&client_id="
				+ mContext.getString(R.string.JUNIPER_CLIENT_ID)
				+ "&client_secret=%22%22&username=" + username + "&password="
				+ password);
		request.setMethod(Method.POST);
		request.setContentType(" application/x-www-form-urlencoded");

		MessageResponse response = null;
		try {
			response = mProtocolCarrie.invokeSync(request, mToken, mToken);
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

		JuniperResponse res = JsonUtils.toObject(response.getBody(),
				JuniperResponse.class);
		if (res.isLogged()) {
			return res.getAccess_token();
		} else {
			return null;
		}

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
		return null;
	}

}
