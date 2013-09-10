//package smartcampus.android.template.standalone.Utilities;
//
//import java.io.BufferedReader;
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.ObjectOutput;
//import java.io.ObjectOutputStream;
//import java.io.OutputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import smartcampus.android.template.standalone.R;
//
//import android.os.AsyncTask;
//import android.util.Log;
//
//public class MapRoute extends AsyncTask<String, Integer, JSONObject> {
//
//	/*
//	 * params: ["lat","lng","AUTH_TOKEN"]
//	 */
//	@Override
//	protected JSONObject doInBackground(String... params) {
//		// TODO Auto-generated method stub
//		URL url;
//		try {
//			//Create and serialize JSONObject
//			JSONObject singleJourney = new JSONObject();
//			
//			ByteArrayOutputStream bos = new ByteArrayOutputStream();
//			ObjectOutput out = null;
//			try {
//			  out = new ObjectOutputStream(bos);  
//			  byte[] yourBytes = bos.toByteArray();
//			} finally {
//			  out.close();
//			  bos.close();
//			}
//			String path = "http://api.openstreetmap.org/api/0.6/map?bbox="
//					+ params[1] + "," + params[0] + ","
//					+ Double.toString(maxLng) + "," + Double.toString(maxLat);
//			Log.i("Path", path);
//			url = new URL(path);
//			HttpURLConnection con = (HttpURLConnection) url.openConnection();
//			//Set type request
//			con.setRequestMethod("POST");
//			con.setDoInput(true);
//			con.setDoOutput(true);
//			//Set AUTH_TOKEN
//			con.addRequestProperty("AUTH_TOKEN", params[2]);
//			//Set Body
//			OutputStream output = null;
//			try {
//			     output = con.getOutputStream();
//			     output.write(query.getBytes(charset));
//			} finally {
//			     if (output != null) try { output.close(); } catch (IOException logOrIgnore) {}
//			}
//			InputStream response = connection.getInputStream();
//			BufferedReader reader = new BufferedReader(new InputStreamReader(
//					con.getInputStream()));
//			String line = "";
//			String response = "";
//			while ((line = reader.readLine()) != null) {
//				response = response + line;
//			}
//			return new JSONObject(response);
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		return null;
//	}
//
// }
