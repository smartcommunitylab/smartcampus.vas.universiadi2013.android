package smartcampus.android.template.standalone.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

public class MapTextRoute extends AsyncTask<String, Integer, JSONObject> {

	/*
	 * params: ["srcLat-srcLon","destLat-destLon"]
	 */
	@Override
	protected JSONObject doInBackground(String... params) {
		// TODO Auto-generated method stub
		URL url;
		try {
			String[] srcGPSTokenized = params[0].split("-");
			String[] destGPSTokenized = params[1].split("-");
			String path = "http://maps.googleapis.com/maps/api/directions/json?origin="
					+ srcGPSTokenized[0]
					+ ","
					+ srcGPSTokenized[1]
					+ "&destination="
					+ destGPSTokenized[0]
					+ ","
					+ destGPSTokenized[1] + "&sensor=false&language=it";
			Log.i("Path", path);
			url = new URL(path);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					con.getInputStream()));
			String line = "";
			String response = "";
			while ((line = reader.readLine()) != null) {
				response = response + line;
			}
			return new JSONObject(response);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// @Override
	// protected void onPostExecute(JSONArray result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// try {
	// for (int i = 0; i < result.length(); i++) {
	// JSONObject item = result.getJSONObject(i);
	// Log.i("Marker #" + item.getString("id"),
	// Double.toString(item.getDouble("lat")) + " - "
	// + Double.toString(item.getDouble("lon")));
	// }
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

}
