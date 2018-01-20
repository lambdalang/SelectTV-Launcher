package com.selecttvlauncher.RPC;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

/**
 * Implementation of JSON-RPC over HTTP/POST
 */
public class JSONRPCHttpClient extends JSONRPCClient {
	private String serviceUri;

	public JSONRPCHttpClient(String uri) {
		this.serviceUri = uri;
	}

	protected JSONObject doJSONRequest(JSONObject jsonRequest) throws JSONRPCException {
		URL url;

		try {
			long e = System.currentTimeMillis();
			String strresponse="";
			try {
				url = new URL(this.serviceUri);

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setReadTimeout(15000);
				conn.setConnectTimeout(15000);
				conn.setRequestMethod("POST");
				conn.setDoInput(true);
				conn.setDoOutput(true);
				conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

				OutputStream os = conn.getOutputStream();
				BufferedWriter writer = new BufferedWriter(
						new OutputStreamWriter(os, "UTF-8"));
				Log.d("json-rpc", "params:" + jsonRequest.toString());
				writer.write(jsonRequest.toString());

				writer.flush();
				writer.close();
				os.close();
				int responseCode=conn.getResponseCode();

				if (responseCode == HttpsURLConnection.HTTP_OK) {
					String line;
					BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
					/*while ((line=br.readLine()) != null) {
						strresponse+=line;
					}*/
					Log.d("json-rpc", "reading::::");
					StringBuilder sb = new StringBuilder();
					while ((line=br.readLine()) != null) {
						sb.append(line);
					}
					strresponse=sb.toString();
				}
				else {
					strresponse="";

				}
			} catch (Exception ee) {
				ee.printStackTrace();
			}


			e = System.currentTimeMillis() - e;
			Log.d("json-rpc", "Request time :" + e);
			String responseString = strresponse.trim();
			Log.d("json-rpc", "responseString:::" + responseString);
			JSONObject jsonResponse = new JSONObject(responseString);
			if(jsonResponse.has("error")) {
				Object jsonError = jsonResponse.get("error");
				if(!jsonError.equals((Object)null)) {
					throw new JSONRPCException(jsonResponse.get("error"));
				} else {
					return jsonResponse;
				}
			} else {
				return jsonResponse;
			}
		}  catch (JSONException var13) {
			throw new JSONRPCException("Invalid JSON response", var13);
		}
	}

	private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException{
		StringBuilder result = new StringBuilder();
		boolean first = true;
		for(Map.Entry<String, String> entry : params.entrySet()){
			if (first)
				first = false;
			else
				result.append("&");

			result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
			result.append("=");
			result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
		}

		return result.toString();
	}
}
