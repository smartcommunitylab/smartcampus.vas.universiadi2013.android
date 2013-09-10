package smartcampus.android.template.standalone.Activity.Model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import smartcampus.android.template.standalone.R;
import android.content.Context;
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

	public RestRequest(Context cnt) {
		mContext = cnt;
	}

	public String authenticate(String username, String password) {
		if (username != null && password != null) {
			mToken = mContext.getString(R.string.AUTH_TOKEN);
			return (mToken = callGETRequest(new String[] { "/login/" + username
					+ "/" + password }));
		} else {
			mToken = mContext.getString(R.string.AUTH_TOKEN);
			return callGETRequest(new String[] { "/anonymus_login" });
		}
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
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.addRequestProperty("AUTH_TOKEN", mToken);
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

	private String callPOSTRequest(String[] params) {
		ProtocolCarrier mProtocolCarrier = new ProtocolCarrier(mContext,
				"test smartcampus");

		MessageRequest request = new MessageRequest(
				mContext.getString(R.string.URL_BACKEND), params[0]);

		try {
			request.setMethod(Method.POST);
			request.setBody(params[1]);
			String body = null;

			MessageResponse response;
			response = mProtocolCarrier.invokeSync(request, "test smartcampus",
					mContext.getString(R.string.AUTH_TOKEN));

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
