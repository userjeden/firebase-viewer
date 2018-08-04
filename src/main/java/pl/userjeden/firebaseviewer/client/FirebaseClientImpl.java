package pl.userjeden.firebaseviewer.client;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;


public class FirebaseClientImpl implements FirebaseClient {

	private static final String READING_BRANCH = "READING";
	private static final String CONFIG_BRANCH = "CONFIGAA";
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

		System.out.println("raw data received: " + rawResponse);
		return rawResponse;
	}


	public String updateStationConfig(String payload){

		HttpClient httpclient = HttpClients.createDefault();
		String rawResponse = "";
		try {

			URIBuilder builder = new URIBuilder();
			builder.setHost(projectAddress);
			builder.setScheme(COMM_SCHEME);
			builder.setPath("/" + CONFIG_BRANCH + ".json");
			builder.setParameter("auth", projectSecret);

			StringEntity postEntity = new StringEntity(payload);

			HttpPut request = new HttpPut(builder.build());
			request.setHeader("accept", "application/json");
			request.setHeader("content-type", "application/json");
			request.setEntity(postEntity);

			HttpResponse response = httpclient.execute(request);
			HttpEntity entity = response.getEntity();

			if (entity != null) {
				rawResponse = EntityUtils.toString(entity);
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		System.out.println("raw data received: " + rawResponse);
		return rawResponse;
	}



	public String readStationReading(String station){

		HttpClient httpclient = HttpClients.createDefault();
		String rawResponse = "";
		try {

			URIBuilder builder = new URIBuilder();
			builder.setHost(projectAddress);
			builder.setScheme(COMM_SCHEME);
			builder.setPath("/" + READING_BRANCH + "/" + station + ".json");
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

		System.out.println("raw data received: " + rawResponse);
		return rawResponse;
	}


}
