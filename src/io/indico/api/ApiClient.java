package io.indico.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

public class ApiClient implements TextApi, ImageApi{

	private static HttpClient httpClient = HttpClients.createDefault();
	public String baseUrl;
	public String name;
	public String baseEndpoint;
	public String batchEndpoint;
	
	public ApiClient(String api, String apiKey) {
		if (apiKey == null){
			throw new IllegalArgumentException("Api Key cannot be null");
		}
		this.baseUrl = "https://apiv2.indico.io";
		name = api;
		baseEndpoint = this.baseUrl + "/" + api.toLowerCase() + "?key=" + apiKey;
		batchEndpoint = this.baseUrl + "/" + api.toLowerCase() + "/batch" + "?key=" + apiKey;
	}
	
	public ApiClient(String api, String apiKey, String privateCloud) {
		if (apiKey == null){
			throw new IllegalArgumentException("Private Cloud cannot be null");
		}
		this.baseUrl = "https://" + privateCloud + ".indico.domains";
		this.name = api;
		this.baseEndpoint = this.baseUrl + "/" + api.toLowerCase() + "?key=" + apiKey;
		this.batchEndpoint = this.baseUrl + "/" + api.toLowerCase() + "/batch" + "?key=" + apiKey;
 	}
	
	@Override
	public Double sentimentCall(String data) 
			throws UnsupportedOperationException, IOException {
		
		Map<String, ?> apiResponse = baseCall(data);
	    return (Double) apiResponse.get("results");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Double> call(String data)
			throws UnsupportedOperationException, IOException {
		
		Map<String, ?> apiResponse = baseCall(data);
		return (Map<String, Double>) apiResponse.get("results");
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public List<Double> sentimentCall(List<String> data)
			throws ClientProtocolException, IOException {
		
		Map<String, List<?>> apiResponse = baseCall(data);
		return (List<Double>) apiResponse.get("results");
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Double>> call(List<String> data) 
			throws ClientProtocolException, IOException {
		
		Map<String, List<?>> apiResponse = baseCall(data);
		return (List<Map<String, Double>>) apiResponse.get("results");
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Double> imageCall(BufferedImage image, String type) 
			throws UnsupportedOperationException, IOException {
        
        Map<String, ?> apiResponse = baseCall(encodeImage(image, type));
		return (Map<String, Double>) apiResponse.get("results");
	}

	@Override
	public List<Map<String, Double>> imageCall(List<BufferedImage> images, String type) throws UnsupportedOperationException, IOException{
		List<Map<String, Double>> response = new ArrayList<Map<String, Double>>();
		for (BufferedImage image: images){
			response.add(imageCall(image, type));
		}
		return response;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Double> featureCall(BufferedImage image, String type) 
			throws UnsupportedOperationException, IOException {
        Map<String, ?> apiResponse = baseCall(encodeImage(image, type));
		return (List<Double>) apiResponse.get("results");
	}

	@Override
	public List<List<Double>> featureCall(List<BufferedImage> images, String type)
			throws UnsupportedOperationException, IOException {
		List<List<Double>> response = new ArrayList<List<Double>>();
		for (BufferedImage image: images){
			response.add(featureCall(image, type));
		}
		return response;
	}
	
	@SuppressWarnings("unchecked")
	private Map<String, ?> baseCall(String data) throws UnsupportedOperationException, IOException{
		HttpPost basePost = new HttpPost(baseEndpoint);
		
		List<NameValuePair> params = new ArrayList<NameValuePair>(1);
		params.add(new BasicNameValuePair("data", data));
		basePost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		
		basePost.addHeader("client-lib", "java");
		basePost.addHeader("client-lib", "2.1");
		
		HttpResponse response = httpClient.execute(basePost);
		HttpEntity entity = response.getEntity();

		@SuppressWarnings("rawtypes")
		Map<String, ?> apiResponse = new HashMap();
		if (entity != null) {
		    InputStream responseStream = entity.getContent();
		    try {
		    	String responseString = IOUtils.toString(responseStream, "UTF-8"); 
		    	apiResponse = new Gson().fromJson(responseString, Map.class);
		    	if (apiResponse.containsKey("error")) {
		    		throw new IllegalArgumentException((String) apiResponse.get("error"));
		    	}
		    } finally {
		        responseStream.close();
		    }
		}
		return apiResponse;

	}
	
	@SuppressWarnings("unchecked")
	private Map<String, List<?>> baseCall(List<String> data) throws ClientProtocolException, IOException{
		HttpPost basePost = new HttpPost(batchEndpoint);
		
		Map<String, List<String>> rawParams = new HashMap<String, List<String>>();
		rawParams.put("data", data);
		
		StringEntity params = new StringEntity(new Gson().toJson(rawParams));
        basePost.setEntity(params);

		basePost.addHeader("content-type", "application/x-www-form-urlencoded");
		basePost.addHeader("client-lib", "java");
		basePost.addHeader("client-lib", "2.1");
		
		HttpResponse response = httpClient.execute(basePost);
		HttpEntity entity = response.getEntity();
	
		Map<String, List<?>> apiResponse = new HashMap<String, List<?>>();
		if (entity != null) {
		    InputStream responseStream = entity.getContent();
		    Reader reader = new InputStreamReader(responseStream, "UTF-8");
		    try {
		    	apiResponse = new Gson().fromJson(reader, Map.class);
		    } finally {
		        responseStream.close();
		    }
		}
		return apiResponse;
	}
	
	private String encodeImage(BufferedImage image, String type){
		String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.encodeBase64String(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return imageString;
	}
}
