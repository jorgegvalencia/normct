package ctgov;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CTManager {

	private static final String BASEPATH = "data/trials/";

	public static void downloadTrial(String nctid) {
		// path to store the new file
		String filePath = BASEPATH + nctid + ".xml";
		try {

			// build the request url
			String requestURL = "https://clinicaltrials.gov/show/" + nctid + "?displayxml=true";
			URL url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/xml");

			// send http request
			if (conn.getResponseCode() == 200){ // if successful request
				File trial = new File(filePath);
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream()), "utf-8"));
				BufferedWriter bw = new BufferedWriter(new FileWriter(trial));
				String line;
				while ((line = br.readLine()) != null) {
					bw.write(line); bw.newLine();
				}
				bw.close();
			}
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void downloadTrials(SearchOptions options){
		try {
			// build the request url
			String requestURL = options.getRequestURL();
			System.out.println(requestURL);
			URL url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "text/xml, application/zip");

			// send http request
			if (conn.getResponseCode() == 200){ // if successful request
				InputStream in = conn.getInputStream();
				FileOutputStream out = new FileOutputStream("download.zip");
				byte[] b = new byte[4096];
				int count;
				while ((count = in.read(b)) >= 0) {
					out.write(b, 0, count);
				}
				out.flush();
				out.close();
				in.close();
			}
			conn.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
