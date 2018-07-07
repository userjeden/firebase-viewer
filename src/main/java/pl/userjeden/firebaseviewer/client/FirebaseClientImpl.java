package pl.userjeden.firebaseviewer.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import pl.userjeden.firebaseviewer.model.StationSettingsDTO;


public class FirebaseClientImpl implements FirebaseClient {

	private static final String READING_BRANCH = "READINGS";
	private static final String CONFIG_BRANCH = "CONFIG";
	private static final String COMM_SCHEME = "https";

	private String projectAddress;
	private String projectSecret;


	public FirebaseClientImpl(String address, String secret){
		this.projectAddress = address;
		this.projectSecret = secret;
	}


	public String readStationsConfig(){

		HttpClient httpclient = HttpClients.createDefault();
		String rawResponse = "";
		try {

			URIBuilder builder = new URIBuilder();
			builder.setHost(projectAddress);
			builder.setScheme(COMM_SCHEME);
			builder.setPath("/" + CONFIG_BRANCH + ".json");
			builder.setParameter("auth", projectSecret);
			builder.setParameter("orderBy", "\"$key\"");

			HttpGet request = new HttpGet(builder.build());
			request.setHeader("accept", "application/json");
			request.setHeader("content-type", "application/json");

			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				rawResponse = EntityUtils.toString(entity);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println("data received: " + rawResponse);
		return rawResponse;
	}


	public String updateStationConfig(String station, StationSettingsDTO settings){
		return "";
	}



	public String readStationReading(String station){

		HttpClient httpclient = HttpClients.createDefault();
		String rawResponse = "";
		try {

			URIBuilder builder = new URIBuilder();
			builder.setScheme(COMM_SCHEME);
			builder.setHost(projectAddress);
			builder.setPath("/" + CONFIG_BRANCH + "/" + station + ".json");
			builder.setParameter("orderBy", "\"$key\"");
//			builder.setParameter("print", "pretty");

			HttpGet request = new HttpGet(builder.build());
			request.setHeader("accept", "application/json");
			request.setHeader("content-type", "application/json");

			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				rawResponse = EntityUtils.toString(entity);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println("\n******* NEXT DATA *******");
		System.out.println("Dane z serwera: " + rawResponse);

		return rawResponse;
	}


}
