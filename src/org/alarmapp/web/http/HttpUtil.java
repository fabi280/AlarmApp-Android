package org.alarmapp.web.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.alarmapp.util.Ensure;
import org.alarmapp.util.LogEx;
import org.alarmapp.web.WebException;

public class HttpUtil {

	private static final String TEMPORARY_NOT_AVAILABLE = "Die Ressource ist temporär nicht verfügbar. HTTP-Statuscode:";
	private static final String URL_ERROR = "Die Url hat kein gültiges Format. URL: ";
	private static final String HTTP_POST_ERROR = "Fehler beim Senden von Daten an die URL ";
	private static final String HTTP_GET_ERROR = "Fehler beim Abrufen der URL ";
	private static final String TEMPORAL_SERVER_ERROR = "Der Web-Service ist momentan nicht verfügbar. Versuchen Sie es später erneut";

	private static final String UNKNOWN_HTTP_STATUS_ERROR = "Der HTTP-Status-Code ist unbekannt:";
	private static final String RESSOURCE_DOES_NOT_EXIST_ERROR = "Die angeforderte Ressource existiert nicht.";
	private static final String NETWORK_ERROR = "Der Webserver ist nicht erreichbar.";
	private static final String INTERNAL_SERVER_ERROR = "Probelem beim Verarbeiten der Anfrage durch den Web-Server.";
	private static final String SLOW_NETWORK_SERVER_ERROR = "Verbindung zum Server zu langsam";

	private static String read(InputStream stream) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(stream));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		while ((line = br.readLine()) != null) {
			stringBuilder.append(line);
		}
		return stringBuilder.toString();
	}

	private static void setHeaders(HttpURLConnection connection,
			HashMap<String, String> headers) {
		if (headers == null)
			return;

		for (Map.Entry<String, String> header : headers.entrySet()) {
			connection.setRequestProperty(header.getKey(), header.getValue());
		}
	}

	private static String get(URL url, HashMap<String, String> headers)
			throws WebException {
		try {
			Ensure.notNull(url);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("GET");
			http.setReadTimeout(10000);

			setHeaders(http, headers);

			return getResponse(http);

		} catch (IOException e) {
			throw new WebException(HTTP_GET_ERROR + url, e);
		}
	}

	private static String getResponse(HttpURLConnection http)
			throws WebException, IOException {
		int statusCode = http.getResponseCode();

		LogEx.verbose(http.getURL() + " returned: Status " + statusCode);

		if (statusCode == 302) // Temporary unavailable
		{

			LogEx.verbose("Response is " + http.getHeaderField("Location"));
			throw new WebException(TEMPORARY_NOT_AVAILABLE + statusCode);
		} else if (statusCode == 404) // Ressource not found
		{
			throw new WebException(RESSOURCE_DOES_NOT_EXIST_ERROR);
		} else if (statusCode == -1) // no valid response code.
		{
			throw new WebException(false, NETWORK_ERROR);
		} else if (statusCode == 500) // Internal Server Error
		{
			String error = formatErrorMessage(read(http.getErrorStream()));
			LogEx.exception(error);

			throw new WebException(INTERNAL_SERVER_ERROR);
		} else if (statusCode == 503) // Service temporary unavailable
		{
			LogEx.warning("Web Service returned " + read(http.getErrorStream()));
			throw new WebException(false, TEMPORAL_SERVER_ERROR);
		} else if (statusCode == 504) // Slow network
		{
			LogEx.warning("Web Service returned " + read(http.getErrorStream()));
			throw new WebException(false, SLOW_NETWORK_SERVER_ERROR);
		} else if (statusCode >= 200 && statusCode < 300) {
			return read(http.getInputStream());
		} else if (statusCode == 409) {
			LogEx.info("Data is already present. Do nothing!");
			return read(http.getInputStream());
		} else {
			throw new WebException(UNKNOWN_HTTP_STATUS_ERROR + statusCode);
		}
	}

	private static String formatErrorMessage(String read) {
		int bodyBegin = read.indexOf("<body");
		int bodyEnd = read.indexOf("</body>");
		if (bodyBegin > -1)
			return read.substring(bodyBegin);
		return read;
	}

	private static String post(URL url, HashMap<String, String> data,
			HashMap<String, String> headers) throws WebException {
		Ensure.notNull(url);

		try {
			Ensure.notNull(url);
			HttpURLConnection http = (HttpURLConnection) url.openConnection();
			http.setRequestMethod("POST");
			http.setReadTimeout(10000);
			http.setDoOutput(true);
			http.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			setHeaders(http, headers);

			OutputStreamWriter streamToSend = new OutputStreamWriter(
					http.getOutputStream(), "UTF8");

			streamToSend.write(exportPostData(data));
			streamToSend.flush();
			streamToSend.close();

			return getResponse(http);

		} catch (IOException e) {
			LogEx.exception(e);
			throw new WebException(HTTP_POST_ERROR + url, e);
		}
	}

	public static String request(String url, HashMap<String, String> data,
			HashMap<String, String> headers) throws WebException {
		Ensure.notNull(url);
		try {
			URL uri = new URL(url);

			if (isNullOrEmpty(data))
				return get(uri, headers);
			else
				return post(uri, data, headers);

		} catch (MalformedURLException e) {
			throw new WebException(URL_ERROR + url, e);
		}
	}

	private static boolean isNullOrEmpty(HashMap<String, String> data) {
		return data == null || data.isEmpty();
	}

	private static String exportPostData(HashMap<String, String> data) {
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, String> entry : data.entrySet())
			builder.append(URLEncoder.encode(entry.getKey()) + "="
					+ URLEncoder.encode(entry.getValue()) + "&");

		return builder.toString();

	}
}
