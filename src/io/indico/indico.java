package io.indico;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.imageio.ImageIO;
import org.apache.commons.io.FilenameUtils;
import org.apache.http.client.ClientProtocolException;

import io.indico.api.ApiClient;
import io.indico.api.image.FacialEmotion;
import io.indico.api.image.ImageApi;
import io.indico.api.text.Language;
import io.indico.api.text.PoliticalClass;
import io.indico.api.text.TextApi;
import io.indico.api.text.TextTag;

public class indico {

	private Map<TextApi, ApiClient> textApis;
	private Map<ImageApi, ApiClient> imageApis;
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
	
	public Double sentiment(String data) throws UnsupportedOperationException, IOException{
		ApiClient handler = textApis.get(TextApi.Sentiment);
		return handler.sentimentCall(data);
	}
	
	public List<Double> sentiment(List<String> data) throws ClientProtocolException, IOException{
		ApiClient handler = textApis.get(TextApi.Sentiment);
		return handler.sentimentCall(data);
	}
	
	public List<Double> sentiment(String[] data) throws ClientProtocolException, IOException{
		ApiClient handler = textApis.get(TextApi.Sentiment);
		return handler.sentimentCall(Arrays.asList(data));
	}
	
	public Double sentimentHQ(String data) throws UnsupportedOperationException, IOException{
		ApiClient handler = textApis.get(TextApi.SentimentHQ);
		return handler.sentimentCall(data);
	}
	
	public List<Double> sentimentHQ(List<String> data) throws ClientProtocolException, IOException{
		ApiClient handler = textApis.get(TextApi.SentimentHQ);
		return handler.sentimentCall(data);
	}
	
	public List<Double> sentimentHQ(String[] data) throws ClientProtocolException, IOException{
		ApiClient handler = textApis.get(TextApi.SentimentHQ);
		return handler.sentimentCall(Arrays.asList(data));
	}

	public Map<PoliticalClass, Double> political(String data)
			throws UnsupportedOperationException, IOException {
		
		ApiClient handler = textApis.get(TextApi.Political);
		Map<String, Double> response = handler.call(data);
		return politinum(response);
	}
	
	public List<Map<PoliticalClass, Double>> political(List<String> data) 
			throws ClientProtocolException, IOException {
		
		ApiClient handler = textApis.get(TextApi.Political);
		List<Map<String, Double>> response = handler.call(data);
		return politinum(response);
	}

	public List<Map<PoliticalClass, Double>> political(String[] data)
			throws ClientProtocolException, IOException {

		ApiClient handler = textApis.get(TextApi.Political);
		List<Map<String, Double>> response = handler.call(Arrays.asList(data));
		return politinum(response);
	}
	public Map<Language, Double> language(String data)
			throws UnsupportedOperationException, IOException {
		
		ApiClient handler = textApis.get(TextApi.Language);
		Map<String, Double> response = handler.call(data);
		return langnum(response);
	}
	
	public List<Map<Language, Double>> language(List<String> data) 
			throws ClientProtocolException, IOException {
		
		ApiClient handler = textApis.get(TextApi.Language);
		List<Map<String, Double>> response = handler.call(data);
		return langnum(response);
	}

	public List<Map<Language, Double>> language(String[] data)
			throws ClientProtocolException, IOException {

		ApiClient handler = textApis.get(TextApi.Language);
		List<Map<String, Double>> response = handler.call(Arrays.asList(data));
		return langnum(response);
	}
	
	public Map<TextTag, Double> textTags(String data)
			throws UnsupportedOperationException, IOException {
		
		ApiClient handler = textApis.get(TextApi.TextTags);
		Map<String, Double> response = handler.call(data);
		return tagnum(response);
	}
	
	public List<Map<TextTag, Double>> textTags(List<String> data) 
			throws ClientProtocolException, IOException {
		
		ApiClient handler = textApis.get(TextApi.TextTags);
		List<Map<String, Double>> response = handler.call(data);
		return tagnum(response);
	}

	public List<Map<TextTag, Double>> textTags(String[] data)
			throws ClientProtocolException, IOException {

		ApiClient handler = textApis.get(TextApi.TextTags);
		List<Map<String, Double>> response = handler.call(Arrays.asList(data));
		return tagnum(response);
	}
	
	public Map<FacialEmotion, Double> fer(String filePath) 
			throws UnsupportedOperationException, IOException {
		
		return fer(convertToImage(filePath), grabType(filePath));
	}

	public Map<FacialEmotion, Double> fer(File imageFile) 
			throws UnsupportedOperationException, IOException {

		return fer(convertToImage(imageFile), grabType(imageFile));
	}

	public Map<FacialEmotion, Double> fer(BufferedImage image, String type)
			throws UnsupportedOperationException, IOException {

		ApiClient handler = imageApis.get(ImageApi.FER);
		Map<String, Double> response = handler.imageCall(image, type);
		return fernum(response);
	}
	
	public List<Map<FacialEmotion, Double>> fer(List<BufferedImage> images, String type) throws UnsupportedOperationException, IOException{
		ApiClient handler = imageApis.get(ImageApi.FER);
		return fernum(handler.imageCall(images, type));
	}
	
	public List<Map<FacialEmotion, Double>> fer(List<?> images) throws UnsupportedOperationException, IOException {
		return fer(convertToImage(images), grabType(images));
	}
	
	public List<Map<FacialEmotion, Double>> fer(String[] images) throws UnsupportedOperationException, IOException{
		return fer(Arrays.asList(images));
	}
	
	public List<Map<FacialEmotion, Double>> fer(File[] images) throws UnsupportedOperationException, IOException{
		return fer(Arrays.asList(images));
	}
	
	public List<Double> imageFeatures(String filePath) throws UnsupportedOperationException, IOException {
		return imageFeatures(convertToImage(filePath), grabType(filePath));
	}
	
	public List<Double> imageFeatures(File imageFile) throws UnsupportedOperationException, IOException {
		return imageFeatures(convertToImage(imageFile), grabType(imageFile));
	}
	
	public List<Double> imageFeatures(BufferedImage image, String type) throws UnsupportedOperationException, IOException {
		ApiClient handler = imageApis.get(ImageApi.ImageFeatures);
		return  handler.featureCall(image, type);
	}
	
	public List<List<Double>> imageFeatures(List<?> images) throws IOException {		
		return imageFeatures(convertToImage(images), grabType(images));
	}
	
	public List<List<Double>> imageFeatures(List<BufferedImage> images, String type) throws UnsupportedOperationException, IOException {
		ApiClient handler = imageApis.get(ImageApi.ImageFeatures);
		return handler.featureCall(images, type);
	}
	
	public List<List<Double>> imageFeatures(String[] images) throws IOException {
		return imageFeatures(Arrays.asList(images));
	}
	
	public List<List<Double>> imageFeatures(File[] images) throws IOException {
		return imageFeatures(Arrays.asList(images));
	}
	
	public List<Double> facialFeatures(String filePath) throws UnsupportedOperationException, IOException {
		return facialFeatures(convertToImage(filePath), grabType(filePath));
	}
	
	public List<Double> facialFeatures(File imageFile) throws UnsupportedOperationException, IOException {
		return facialFeatures(convertToImage(imageFile), grabType(imageFile));
	}
	
	public List<Double> facialFeatures(BufferedImage image, String type) throws UnsupportedOperationException, IOException {
		ApiClient handler = imageApis.get(ImageApi.FacialFeatures);
		return  handler.featureCall(image, type);
	}
	
	public List<List<Double>> facialFeatures(List<?> images) throws UnsupportedOperationException, IOException {
		return facialFeatures(convertToImage(images), grabType(images));
	}
	
	public List<List<Double>> facialFeatures(List<BufferedImage> images, String type) throws UnsupportedOperationException, IOException {
		ApiClient handler = imageApis.get(ImageApi.FacialFeatures);
		return handler.featureCall(images, type);
	}
	
	public List<List<Double>> facialFeatures(String[] images) throws UnsupportedOperationException, IOException {
		return facialFeatures(Arrays.asList(images));
	}
	
	public List<List<Double>> facialFeatures(File[] images) throws UnsupportedOperationException, IOException {
		return facialFeatures(Arrays.asList(images));
	}

	private Map<PoliticalClass, Double> politinum(Map<String, Double> apiResponse){
		Map<PoliticalClass, Double> mappedResponse = new HashMap<PoliticalClass, Double>();
		for (Map.Entry<String, Double> entry: apiResponse.entrySet()){
			mappedResponse.put(PoliticalClass.valueOf(entry.getKey()), entry.getValue());
		}
		return mappedResponse;
	}
	
	private List<Map<PoliticalClass, Double>> politinum(List<Map<String, Double>> apiResponse){
		List<Map<PoliticalClass, Double>> cleanedResponse = new ArrayList<Map<PoliticalClass, Double>>();
		for (Map<String, Double> entry: apiResponse){
			cleanedResponse.add(politinum(entry));
		}
		return cleanedResponse;
	}
	
	private Map<Language, Double> langnum(Map<String, Double> apiResponse){
		Map<Language, Double> mappedResponse = new HashMap<Language, Double>();
		for (Map.Entry<String, Double> entry: apiResponse.entrySet()){
			if (entry.getKey().equals("Persian (Farsi)")) {
				mappedResponse.put(Language.valueOf("Persian"), entry.getValue());
			} else {
				mappedResponse.put(Language.valueOf(entry.getKey()), entry.getValue());
			}
		}
		return mappedResponse;
	}
	
	private List<Map<Language, Double>> langnum(List<Map<String, Double>> apiResponse){
		List<Map<Language, Double>> cleanedResponse = new ArrayList<Map<Language, Double>>();
		for (Map<String, Double> entry: apiResponse){
			cleanedResponse.add(langnum(entry));
		}
		return cleanedResponse;
	}
	
	private Map<TextTag, Double> tagnum(Map<String, Double> apiResponse){
		Map<TextTag, Double> mappedResponse = new HashMap<TextTag, Double>();
		for (Map.Entry<String, Double> entry: apiResponse.entrySet()){
			mappedResponse.put(TextTag.valueOf(entry.getKey()), entry.getValue());
		}
		return mappedResponse;
	}
	
	private List<Map<TextTag, Double>> tagnum(List<Map<String, Double>> apiResponse){
		List<Map<TextTag, Double>> cleanedResponse = new ArrayList<Map<TextTag, Double>>();
		for (Map<String, Double> entry: apiResponse){
			cleanedResponse.add(tagnum(entry));
		}
		return cleanedResponse;
	}
	
	private Map<FacialEmotion, Double> fernum(Map<String, Double> apiResponse){
		Map<FacialEmotion, Double> mappedResponse = new HashMap<FacialEmotion, Double>();
		for (Map.Entry<String, Double> entry: apiResponse.entrySet()){
			mappedResponse.put(FacialEmotion.valueOf(entry.getKey()), entry.getValue());
		}
		return mappedResponse;
	}
	
	private List<Map<FacialEmotion, Double>> fernum(List<Map<String, Double>> apiResponse){
		List<Map<FacialEmotion, Double>> cleanedResponse = new ArrayList<Map<FacialEmotion, Double>>();
		for (Map<String, Double> entry: apiResponse){
			cleanedResponse.add(fernum(entry));
		}
		return cleanedResponse;
	}

	private BufferedImage convertToImage(String filePath) throws IOException {
		return convertToImage(new File(filePath));
	}
	
	private BufferedImage convertToImage(File imageFile) throws IOException {
		return ImageIO.read(imageFile);
	}
	
	private List<BufferedImage> convertToImage(List<?> images) throws IOException {
		List<BufferedImage> convertedInput = new ArrayList<BufferedImage>();
		for (Object entry: images){
			if (entry instanceof File){
				convertedInput.add(convertToImage((File) entry));
			} else if (entry instanceof String){
				convertedInput.add(convertToImage((String) entry));
			} else {
				throw new IllegalArgumentException(
					"imageCall method only supports lists of Files and lists of Strings"
				);
			}
		}
		return convertedInput;
	}
	
	private String grabType(String filePath) throws IOException {
		return grabType(new File(filePath));
	}

	private String grabType(File imageFile) throws IOException {
		return FilenameUtils.getExtension(imageFile.getName());
	}
	
	private String grabType(List<?> images) {
		String type;
		Object entry = images.get(0);
		
		if (entry instanceof File){
			type = FilenameUtils.getExtension(((File) entry).getName());
		} else if (entry instanceof String){
			type = FilenameUtils.getExtension((String) entry);
		} else {
			throw new IllegalArgumentException(
				"imageCall method only supports lists of Files and lists of Strings"
			);
		}
		
		return type;
	}
	
	private void initializeClients() {
		this.textApis = new HashMap<TextApi, ApiClient>();
		for (TextApi ta: TextApi.values()){
			if (this.cloud == null){
				this.textApis.put(ta, new ApiClient(ta.toString(), this.apiKey));
			} else {
				this.textApis.put(ta, new ApiClient(ta.toString(), this.apiKey, this.cloud));
			}
		}
		
		this.imageApis = new HashMap<ImageApi, ApiClient>();
		for (ImageApi ia: ImageApi.values()) {
			if (this.cloud == null){
				this.imageApis.put(ia, new ApiClient(ia.toString(), this.apiKey));
			} else {
				this.imageApis.put(ia, new ApiClient(ia.toString(), this.apiKey, this.cloud));
			}
		}
	}

	public static void main(String[] args) throws IOException{
		indico test = new indico("de00119c789e444fc607d7b686b39f93");
		test.createPropertiesFile();
	}
}
