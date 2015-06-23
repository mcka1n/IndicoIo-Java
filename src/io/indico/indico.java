package io.indico;

import org.apache.http.client.ClientProtocolException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import io.indico.api.Api;
import io.indico.api.ApiClient;
import io.indico.api.results.BatchIndicoResult;
import io.indico.api.results.IndicoResult;
import io.indico.api.utils.ImageUtils;

public class indico {

	private ApiClient apiClient;
	public String apiKey;
	public String cloud;
	
	public indico(String apiKey){
		this.apiKey = apiKey;
		this.initializeClients();
	}
	
	public indico(String apiKey, String privateCloud){
		this.apiKey = apiKey;
		this.cloud = privateCloud;
		
		this.initializeClients();
	}
	
	public indico(File configurationFile) throws IOException {
		Properties prop = new Properties();
		InputStream input = new FileInputStream(configurationFile);
		prop.load(input);
			
		this.apiKey = prop.getProperty("apiKey");
		this.cloud = prop.getProperty("privateCloud", null);
		
		this.initializeClients();
	}
	
	public indico() throws IOException {
		this.apiKey = System.getenv("INDICO_API_KEY");
		this.cloud = System.getenv("INDICO_CLOUD");
		
		this.initializeClients();
	}

	public void createPropertiesFile() throws IOException {
		Properties prop = new Properties();
		OutputStream output = null;
		
		output = new FileOutputStream("config.properties");
		prop.setProperty("apiKey", this.apiKey);
		if (this.cloud != null){
			prop.setProperty("privateCloud", this.cloud);
		}
		prop.store(output, null);
		
		output.close();
	}
	
	public void createPropertiesFile(String filePath) throws IOException {
		Properties prop = new Properties();
		OutputStream output = null;
		
		output = new FileOutputStream(filePath);
		prop.setProperty("apiKey", this.apiKey);
		if (this.cloud != null){
			prop.setProperty("privateCloud", this.cloud);
		}
		prop.store(output, null);
		
		output.close();
	}
	
	public IndicoResult sentiment(String data) throws UnsupportedOperationException, IOException{
		return apiClient.call(Api.Sentiment, data, null);
	}
	
	public BatchIndicoResult sentiment(List<String> data) throws ClientProtocolException, IOException{
		return apiClient.call(Api.Sentiment, data, null);
	}
	
	public BatchIndicoResult sentiment(String[] data) throws ClientProtocolException, IOException{
		return sentiment(Arrays.asList(data));
	}
	
	public IndicoResult sentimentHQ(String data) throws UnsupportedOperationException, IOException{
		return apiClient.call(Api.SentimentHQ, data, null);
	}
	
	public BatchIndicoResult sentimentHQ(List<String> data) throws ClientProtocolException, IOException{
		return apiClient.call(Api.SentimentHQ, data, null);
	}
	
	public BatchIndicoResult sentimentHQ(String[] data) throws ClientProtocolException, IOException{
		return sentimentHQ(Arrays.asList(data));
	}

	public IndicoResult political(String data)
			throws UnsupportedOperationException, IOException {
		
		return apiClient.call(Api.Political, data, null);
	}
	
	public BatchIndicoResult political(List<String> data)  throws IOException {
		
		return apiClient.call(Api.Political, data, null);
	}

	public BatchIndicoResult political(String[] data) throws IOException {
		return political(Arrays.asList(data));
	}

	public IndicoResult language(String data)
			throws UnsupportedOperationException, IOException {
		
		return apiClient.call(Api.Language, data, null);
	}
	
	public BatchIndicoResult language(List<String> data) throws IOException {
		
		return apiClient.call(Api.Language, data, null);
	}

	public BatchIndicoResult language(String[] data) throws IOException {

		return language(Arrays.asList(data));
	}
	
	public IndicoResult textTags(String data)
			throws UnsupportedOperationException, IOException {
		
		return apiClient.call(Api.TextTags, data, null);
	}
	
	public BatchIndicoResult textTags(List<String> data)
			throws ClientProtocolException, IOException {
		return apiClient.call(Api.TextTags, data, null);
	}

	public BatchIndicoResult textTags(String[] data)
			throws ClientProtocolException, IOException {
		return textTags(Arrays.asList(data));
	}
	
	public IndicoResult fer(String filePath)
			throws UnsupportedOperationException, IOException {
		
		return fer(ImageUtils.convertToImage(filePath), ImageUtils.grabType(filePath));
	}

	public IndicoResult fer(File imageFile)
			throws UnsupportedOperationException, IOException {

		return fer(ImageUtils.convertToImage(imageFile), ImageUtils.grabType(imageFile));
	}

	public IndicoResult fer(BufferedImage image, String type)
			throws UnsupportedOperationException, IOException {

		return apiClient.call(Api.FER, image, type, null);
	}
	
	public BatchIndicoResult fer(List<BufferedImage> images, String type) throws UnsupportedOperationException, IOException{
		return apiClient.call(Api.FER, images, type, null);
	}
	
	public BatchIndicoResult fer(List<?> images) throws UnsupportedOperationException, IOException {
		return fer(ImageUtils.convertToImage(images), ImageUtils.grabType(images));
	}
	
	public BatchIndicoResult fer(String[] images) throws UnsupportedOperationException, IOException{
		return fer(Arrays.asList(images));
	}
	
	public BatchIndicoResult fer(File[] images) throws UnsupportedOperationException, IOException{
		return fer(Arrays.asList(images));
	}
	
	public IndicoResult imageFeatures(String filePath) throws UnsupportedOperationException, IOException {
		return imageFeatures(ImageUtils.convertToImage(filePath), ImageUtils.grabType(filePath));
	}
	
	public IndicoResult imageFeatures(File imageFile) throws UnsupportedOperationException, IOException {
		return imageFeatures(ImageUtils.convertToImage(imageFile), ImageUtils.grabType(imageFile));
	}
	
	public IndicoResult imageFeatures(BufferedImage image, String type) throws UnsupportedOperationException, IOException {
		return apiClient.call(Api.ImageFeatures, image, type, null);
	}

	public BatchIndicoResult imageFeatures(List<BufferedImage> images, String type) throws UnsupportedOperationException, IOException {
		return apiClient.call(Api.ImageFeatures, images, type, null);
	}

	public BatchIndicoResult imageFeatures(List<?> images) throws IOException {
		return imageFeatures(ImageUtils.convertToImage(images), ImageUtils.grabType(images));
	}

	public BatchIndicoResult imageFeatures(String[] images) throws IOException {
		return imageFeatures(Arrays.asList(images));
	}
	
	public BatchIndicoResult imageFeatures(File[] images) throws IOException {
		return imageFeatures(Arrays.asList(images));
	}
	
	public IndicoResult facialFeatures(String filePath) throws UnsupportedOperationException, IOException {
		return facialFeatures(ImageUtils.convertToImage(filePath), ImageUtils.grabType(filePath));
	}
	
	public IndicoResult facialFeatures(File imageFile) throws UnsupportedOperationException, IOException {
		return facialFeatures(ImageUtils.convertToImage(imageFile), ImageUtils.grabType(imageFile));
	}
	
	public IndicoResult facialFeatures(BufferedImage image, String type) throws UnsupportedOperationException, IOException {
		return apiClient.call(Api.FacialFeatures, image, type, null);
	}
	
	public BatchIndicoResult facialFeatures(List<?> images) throws UnsupportedOperationException, IOException {
		return facialFeatures(ImageUtils.convertToImage(images), ImageUtils.grabType(images));
	}
	
	public BatchIndicoResult facialFeatures(List<BufferedImage> images, String type) throws UnsupportedOperationException, IOException {
		return apiClient.call(Api.FacialFeatures, images, type, null);
	}
	
	public BatchIndicoResult facialFeatures(String[] images) throws UnsupportedOperationException, IOException {
		return facialFeatures(Arrays.asList(images));
	}
	
	public BatchIndicoResult facialFeatures(File[] images) throws UnsupportedOperationException, IOException {
		return facialFeatures(Arrays.asList(images));
	}

	private void initializeClients() {
		this.apiClient = new ApiClient(this.apiKey, this.cloud);
	}
}
