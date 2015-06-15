package io.indico.test;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.indico.indico;
import io.indico.api.image.FacialEmotion;
import io.indico.api.text.Language;
import io.indico.api.text.PoliticalClass;
import io.indico.api.text.TextTag;

import org.junit.Test;

public class ApiTest {
	
	@Test(expected=IllegalArgumentException.class)
	public void testBadCall() throws UnsupportedOperationException, IOException {
		indico test = new indico("notanapikey");
		test.sentiment("this is going to error!");
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNoApiKey() throws UnsupportedOperationException, IOException {
		String emptyString = null;
		indico test = new indico(emptyString);
		test.sentiment("this is going to error!");
	}
	
	@Test
	public void testConfigFile() throws IOException {
		String apiKey = "NotReallyAnApiKey";
		String cloud = "NotReallyAPrivateCloud";
		indico test = new indico(apiKey, cloud);
		test.createPropertiesFile("test.path");
		
		indico check = new indico(new File("test.path"));
		assertTrue(check.apiKey.equals(apiKey));
		assertTrue(check.cloud.equals(cloud));
		
		Files.deleteIfExists(Paths.get("test.path"));
	}
	
	@Test
	public void testSentiment() throws IOException {
		indico test = new indico(new File("config.properties"));
		assertTrue(test.sentiment("this is great!") > 0.5);
	}
	
	@Test
	public void testBatchSentimentList() throws IOException {
		indico test = new indico(new File("config.properties"));
		List<String> examples = new ArrayList<String>();
		examples.add("this is great!");
		examples.add("this is awful!");
		List<Double> results = test.sentiment(examples);
		assertTrue(results.get(0) > 0.5);
		assertTrue(results.get(1) < 0.5);
	}
	
	@Test
	public void testBatchSentimentArray() throws IOException {
		indico test = new indico(new File("config.properties"));
		List<Double> results = test.sentiment(new String[]{"this is great!", "this is awful!"});
		assertTrue(results.get(0) > 0.5);
		assertTrue(results.get(1) < 0.5);
	}
	
	@Test
	public void testSentimentHQ() throws IOException {
		indico test = new indico(new File("config.properties"));
		assertTrue(test.sentimentHQ("this is great!") > 0.5);
	}
	
	@Test
	public void testBatchSentimentHQList() throws IOException {
		indico test = new indico(new File("config.properties"));
		List<String> examples = new ArrayList<String>();
		examples.add("this is great!");
		examples.add("this is awful!");
		List<Double> results = test.sentimentHQ(examples);
		assertTrue(results.get(0) > 0.5);
		assertTrue(results.get(1) < 0.5);
	}
	
	@Test
	public void testBatchSentimentHQArray() throws IOException {
		indico test = new indico(new File("config.properties"));
		List<Double> results = test.sentimentHQ(new String[]{"this is great!", "this is awful!"});
		assertTrue(results.get(0) > 0.5);
		assertTrue(results.get(1) < 0.5);
	}
	
	@Test
	public void testPolitical() throws IOException {
		indico test = new indico(new File("config.properties"));
		assertTrue(test.political("test").size() == PoliticalClass.values().length);
	}
	
	@Test
	public void testBatchPoliticalList() throws IOException {
		indico test = new indico(new File("config.properties"));
		List<String> examples = new ArrayList<String>();
		examples.add("this is great!");
		examples.add("this is awful!");
		List<Map<PoliticalClass, Double>> results = test.political(examples);
		assertTrue(results.size() == 2);
		assertTrue(results.get(0).size() == PoliticalClass.values().length);
	}
	
	@Test
	public void testBatchPoliticalArray() throws IOException {
		indico test = new indico(new File("config.properties"));
		List<Map<PoliticalClass, Double>> results = test.political(new String[]{"this is great!", "this is awful!"});
		assertTrue(results.size() == 2);
		assertTrue(results.get(0).size() == PoliticalClass.values().length);
	}
	
	@Test
	public void testLanguage() throws IOException {
		indico test = new indico(new File("config.properties"));
		assertTrue(test.language("test").size() == Language.values().length);
	}
	
	@Test
	public void testBatchLanguageList() throws IOException {
		indico test = new indico(new File("config.properties"));
		List<String> examples = new ArrayList<String>();
		examples.add("this is great!");
		examples.add("this is awful!");
		List<Map<Language, Double>> results = test.language(examples);
		assertTrue(results.size() == 2);
		assertTrue(results.get(0).size() == Language.values().length);
	}
	
	@Test
	public void testBatchLanguageArray() throws IOException {
		indico test = new indico(new File("config.properties"));
		List<Map<Language, Double>> results = test.language(new String[]{"this is great!", "this is awful!"});
		assertTrue(results.size() == 2);
		assertTrue(results.get(0).size() == Language.values().length);
	}

	@Test
	public void testTextTags() throws IOException {
		indico test = new indico(new File("config.properties"));
		assertTrue(test.textTags("test").size() == TextTag.values().length);
	}
	
	@Test
	public void testBatchTextTagsList() throws IOException {
		indico test = new indico(new File("config.properties"));
		List<String> examples = new ArrayList<String>();
		examples.add("this is great!");
		examples.add("this is awful!");
		List<Map<TextTag, Double>> results = test.textTags(examples);
		assertTrue(results.size() == 2);
		assertTrue(results.get(0).size() == TextTag.values().length);
	}
	
	@Test
	public void testBatchTextTagArray() throws IOException {
		indico test = new indico(new File("config.properties"));
		List<Map<TextTag, Double>> results = test.textTags(new String[]{"this is great!", "this is awful!"});
		assertTrue(results.size() == 2);
		assertTrue(results.get(0).size() == TextTag.values().length);
	}
	
	@Test
	public void testFER() throws IOException {
		indico test = new indico(new File("config.properties"));
        
        Map<FacialEmotion, Double> results = test.fer("bin/lena.png");
		assertTrue(results.size() == FacialEmotion.values().length);
	}
	
	@Test
	public void testBatchFER() throws IOException {
		indico test = new indico(new File("config.properties"));
		
		List<String> lenas = new ArrayList<String>();
		lenas.add("bin/lena.png");
		lenas.add("bin/lena.png");
		
        List<Map<FacialEmotion, Double>> results = test.fer(lenas);
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
	}
	
	@Test
	public void testImageFeatures() throws IOException {
		indico test = new indico(new File("config.properties"));
        
        List<Double> results = test.imageFeatures("bin/lena.png");
		assertTrue(results.size() == 2048);
	}
	
	@Test
	public void testBatchImageFeatures() throws IOException {
		indico test = new indico(new File("config.properties"));

		List<String> lenas = new ArrayList<String>();
		lenas.add("bin/lena.png");
		lenas.add("bin/lena.png");
		
        List<List<Double>> results = test.imageFeatures(lenas);
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
	}
	
	@Test
	public void testFacialFeatures() throws IOException {
		indico test = new indico(new File("config.properties"));
        
        List<Double> results = test.facialFeatures("bin/lena.png");
		assertTrue(results.size() == 48);
	}
	
	@Test
	public void testBatchFacialFeatures() throws IOException {
		indico test = new indico(new File("config.properties"));
        
		List<String> lenas = new ArrayList<String>();
		lenas.add("bin/lena.png");
		lenas.add("bin/lena.png");
		
        List<List<Double>> results = test.facialFeatures(lenas);
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
	}
}
