package nestJava;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.json.JSONException;

public class Nest {
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				createAndShowGui();
			}
		});
	}

	private static void createAndShowGui() {
		JFrame nestFrame = new JFrame("Nest");
		nestFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JButton nestTxButton = new JButton("Send text to NEST");
		nestTxButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Nest.executePost(null, null);
			}
		});

		JPanel nestPanel = new JPanel();
		nestPanel.add(nestTxButton);

		nestFrame.add(nestPanel);
		nestFrame.pack();
		nestFrame.setVisible(true);
	}

	public static String executePost(String targetURL, String urlParameters)
	{
		try {
			openOauth2Connection();			
		} catch (IOException | JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return(null);
	}

	static void openAccessToken() throws Exception {
		//Create connection
		String nestURLString = "https://api.home.nest.com/oauth2/access_token";
		URL nestURL = new URL(nestURLString);

		System.out.println(nestURL.getUserInfo());
		HttpURLConnection connection = (HttpURLConnection)nestURL.openConnection();
		connection.setRequestMethod("POST");
		//			connection.setRequestProperty("Content-Type",contentType);
		connection.setRequestProperty("client_id", Secrets.client_id);
		connection.setRequestProperty("client_secret", Secrets.client_secret);

		final String urlParameters = 
				"?code=AUTH_CODE"
						+ "&client_id=" + Secrets.client_id 
						+ "&client_secret=" + Secrets.client_secret
						+ "&grant_type=authorization_code";
		
		connection.setDoOutput(true);

		handleServerResponse(nestURLString, connection, urlParameters);
	}

	private static void openOauth2Connection() throws IOException, JSONException {
		String nestURLString = "https://home.nest.com/login/oauth2";			
		URL nestURL = new URL(nestURLString);

		System.out.println(nestURL.getUserInfo());
		HttpURLConnection connection = (HttpURLConnection)nestURL.openConnection();
		connection.setRequestMethod("POST");
		//		connection.setRequestProperty("Content-Type",contentType);
		connection.setRequestProperty("client_id", Secrets.client_id);
		connection.setRequestProperty("client_secret", Secrets.client_secret);

		String urlParameters = 
				"?client_id=" + Secrets.client_id
				+ "&state=STATE";

		connection.setDoOutput(true);

//		handleServerResponse(nestURLString, connection, urlParameters);
	}

	private static void handleServerResponse(String nestURLString, HttpURLConnection connection, String urlParameters)
			throws IOException, JSONException {
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();

//		InputStream in = new BufferedInputStream(connection.getInputStream());
//		String result = readStream(in);
//		JSONObject object = new JSONObject(result);

		int responseCode = connection.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + nestURLString);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);

		BufferedReader br = new BufferedReader(
				new InputStreamReader(connection.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = br.readLine()) != null) {
			response.append(inputLine + "\n");
		}
//		in.close();

		//print result
		System.out.println(response.toString());
	}

	private static String readStream(InputStream stream) throws IOException {
		final int BUFFER_SIZE = 4096;

		final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		final byte[] buffer = new byte[BUFFER_SIZE];
		int read;
		while ((read = stream.read(buffer, 0, buffer.length)) != -1) {
			outStream.write(buffer, 0, read);
		}
		final byte[] data = outStream.toByteArray();
		return new String(data);
	}

	//	private void okhttp() {
	//		MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
	//		RequestBody body = RequestBody.create(mediaType, "code=AUTH_CODE&client_id=CLIENT_ID&client_secret=CLIENT_SECRET&grant_type=authorization_code");
	//		Request request = new Request.Builder()
	//				.url("https://api.home.nest.com/oauth2/access_token")
	//				.post(body)
	//				.build();
	//
	//		Response response = client.newCall(request).execute();
	//	}
}



