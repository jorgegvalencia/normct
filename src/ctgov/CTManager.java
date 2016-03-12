package ctgov;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import main.Environment;

public class CTManager {

	private static final SimpleDateFormat DATEFORMAT = new SimpleDateFormat("yyyy-MM-dd_HH_mm_ss");

	/**
	 * Downloads the specified trial
	 * @param nctid
	 */
	public static void downloadTrial(String nctid) {
		// path to store the new file
		String filePath = Environment.TRIALS_PATH + nctid + ".xml";
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

	public static void downloadTrials(String[] trials){
		for(String trial: trials){
			downloadTrial(trial);
		}
	}

	public static void downloadTrials(SearchOptions options){
		try {
			// build the request url
			String requestURL = options.getRequestURL();
			URL url = new URL(requestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/zip");

			// send http request
			if (conn.getResponseCode() == 200){ // if successful request
				InputStream in = conn.getInputStream();
				FileOutputStream out = new FileOutputStream("search_result.zip");
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
			
			// Extract the zip file
			String now = DATEFORMAT.format(new Date());//2014_08_06-15_59_48
			unzip("search_result.zip",Environment.TRIALS_PATH); //+now+"_"+options.getTopic()
			
			// Delete the zip file
			Files.deleteIfExists(new File("search_result.zip").toPath());
			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setDownloadPath(String path){
		Environment.TRIALS_PATH = path;
	}
	
	/**
	 * Extracts a zip file specified by the zipFilePath to a directory specified by
	 * destDirectory (will be created if does not exists)
	 * @param src
	 * @param dest
	 * @throws IOException
	 */
	private static void unzip(String src, String dest) throws IOException{
		File destDir = new File(dest);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(src));
		ZipEntry entry = zipIn.getNextEntry();
		// iterates over entries in the zip file
		while (entry != null) {
			String filePath = dest + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				// if the entry is a file, extracts it
				extractFile(zipIn, filePath);
			} else {
				// if the entry is a directory, make the directory
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}
}
