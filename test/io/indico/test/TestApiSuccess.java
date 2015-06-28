package io.indico.test;

import org.junit.Test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import io.indico.api.Api;
import io.indico.api.image.FacialEmotion;
import io.indico.api.results.BatchIndicoResult;
import io.indico.api.results.IndicoResult;
import io.indico.api.text.Language;
import io.indico.api.text.PoliticalClass;
import io.indico.api.text.TextTag;
import io.indico.api.utils.IndicoException;
import io.indico.Indico;

import static org.junit.Assert.assertTrue;

public class TestApiSuccess {

    @Test
    public void testConfigFile() throws IOException {
        String apiKey = "NotReallyAnApiKey";
        String cloud = "NotReallyAPrivateCloud";
        Indico test = new Indico(apiKey, cloud);
        test.createPropertiesFile("test.path");

        Indico check = new Indico(new File("test.path"));
        assertTrue(check.apiKey.equals(apiKey));
        assertTrue(check.cloud.equals(cloud));

        Files.deleteIfExists(Paths.get("test.path"));
    }

    @Test
    public void testSentiment() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        assertTrue(test.sentiment.predict("this is great!").getSentiment() > 0.5);
    }

    @Test
    public void testBatchSentimentList() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        List<String> examples = new ArrayList<String>();
        examples.add("this is great!");
        examples.add("this is awful!");
        List<Double> results = test.sentiment.predict(examples).getSentiment();
        assertTrue(results.get(0) > 0.5);
        assertTrue(results.get(1) < 0.5);
    }

    @Test
    public void testBatchSentimentArray() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        List<Double> results = test.sentiment.predict(new String[]{"this is great!", "this is awful!"}).getSentiment();
        assertTrue(results.get(0) > 0.5);
        assertTrue(results.get(1) < 0.5);
    }

    @Test
    public void testSentimentHQ() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        assertTrue(test.sentimentHQ.predict("this is great!").getSentimentHQ() > 0.5);
    }

    @Test
    public void testBatchSentimentHQList() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        List<String> examples = new ArrayList<String>();
        examples.add("this is great!");
        examples.add("this is awful!");
        List<Double> results = test.sentimentHQ.predict(examples).getSentimentHQ();
        assertTrue(results.get(0) > 0.5);
        assertTrue(results.get(1) < 0.5);
    }

    @Test
    public void testBatchSentimentHQArray() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        List<Double> results = test.sentimentHQ.predict(new String[]{"this is great!", "this is awful!"}).getSentimentHQ();
        assertTrue(results.get(0) > 0.5);
        assertTrue(results.get(1) < 0.5);
    }

    @Test
    public void testPolitical() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        assertTrue(test.political.predict("test").getPolitical().size() == PoliticalClass.values().length);
    }

    @Test
    public void testBatchPoliticalList() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        List<String> examples = new ArrayList<String>();
        examples.add("this is great!");
        examples.add("this is awful!");
        List<Map<PoliticalClass, Double>> results = test.political.predict(examples).getPolitical();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == PoliticalClass.values().length);
    }

    @Test
    public void testBatchPoliticalArray() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        List<Map<PoliticalClass, Double>> results = test.political.predict(new String[]{"this is great!", "this is awful!"}).getPolitical();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == PoliticalClass.values().length);
    }

    @Test
    public void testLanguage() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        assertTrue(test.language.predict("test").getLanguage().size() == Language.values().length);
    }

    @Test
    public void testBatchLanguageList() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        List<String> examples = new ArrayList<String>();
        examples.add("this is great!");
        examples.add("this is awful!");
        List<Map<Language, Double>> results = test.language.predict(examples).getLanguage();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == Language.values().length);
    }

    @Test
    public void testBatchLanguageArray() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        List<Map<Language, Double>> results = test.language.predict(new String[]{"this is great!", "this is awful!"}).getLanguage();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == Language.values().length);
    }

    @Test
    public void testTextTags() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        assertTrue(test.textTags.predict("test").getTextTags().size() == TextTag.values().length);
    }

    @Test
    public void testBatchTextTagsList() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        List<String> examples = new ArrayList<String>();
        examples.add("this is great!");
        examples.add("this is awful!");
        List<Map<TextTag, Double>> results = test.textTags.predict(examples).getTextTags();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == TextTag.values().length);
    }

    @Test
    public void testBatchTextTagArray() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        List<Map<TextTag, Double>> results = test.textTags.predict(new String[]{"this is great!", "this is awful!"}).getTextTags();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == TextTag.values().length);
    }

    @Test
    public void testFERString() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        Map<FacialEmotion, Double> results = test.fer.predict("bin/lena.png").getFer();
        assertTrue(results.size() == FacialEmotion.values().length);
    }
    
    @Test
    public void testFERFile() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        Map<FacialEmotion, Double> results = test.fer.predict(new File("bin/lena.png")).getFer();
        assertTrue(results.size() == FacialEmotion.values().length);
    }
    
    @Test
    public void testFERImage() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));
        
        BufferedImage check = ImageIO.read(new File("bin/lena.png"));

        Map<FacialEmotion, Double> results = test.fer.predict(check, "png").getFer();
        assertTrue(results.size() == FacialEmotion.values().length);
    }

    @Test
    public void testBatchFERList() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        List<String> lenas = new ArrayList<String>();
        lenas.add("bin/lena.png");
        lenas.add("bin/lena.png");

        List<Map<FacialEmotion, Double>> results = test.fer.predict(lenas).getFer();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
    }
    
    @Test
    public void testBatchFERStringArray() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        String[] lenas = {"bin/lena.png", "bin/lena.png"};

        List<Map<FacialEmotion, Double>> results = test.fer.predict(lenas).getFer();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
    }
    
    @Test
    public void testBatchFERFileArray() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        File[] lenas = {new File("bin/lena.png"), new File("bin/lena.png")};

        List<Map<FacialEmotion, Double>> results = test.fer.predict(lenas).getFer();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
    }

    @Test
    public void testImageFeatures() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        List<Double> results = test.imageFeatures.predict("bin/lena.png").getImageFeatures();
        assertTrue(results.size() == 2048);
    }

    @Test
    public void testBatchImageFeatures() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        List<String> lenas = new ArrayList<String>();
        lenas.add("bin/lena.png");
        lenas.add("bin/lena.png");

        List<List<Double>> results = test.imageFeatures.predict(lenas).getImageFeatures();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
    }

    @Test
    public void testFacialFeatures() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        List<Double> results = test.facialFeatures.predict("bin/lena.png").getFacialFeatures();
        assertTrue(results.size() == 48);
    }

    @Test
    public void testBatchFacialFeatures() throws IOException, IndicoException {
        Indico test = new Indico(new File("config.properties"));

        List<String> lenas = new ArrayList<String>();
        lenas.add("bin/lena.png");
        lenas.add("bin/lena.png");

        List<List<Double>> results = test.facialFeatures.predict(lenas).getFacialFeatures();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
    }

    @Test
    public void testPredictText() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String example = "this is great!";
        IndicoResult result = test.text.predict(example, new HashMap<String, Object>() {
        
			private static final long serialVersionUID = 1215210703571708645L;

		{
            put("apis", new Api[] { Api.Sentiment, Api.Language });
        }});

        assertTrue(result.getSentiment() > .5);
        assertTrue(result.getLanguage().size() == Language.values().length);
    }

    @Test
    public void testPredictImageString() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String example = "bin/lena.png";

        IndicoResult result = test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 6393826713020433012L;

		{
            put("apis", new Api[] { Api.FER, Api.FacialFeatures });
        }});

        assertTrue(result.getFacialFeatures().size() == 48);
        assertTrue(result.getFer().size() == FacialEmotion.values().length);
    }

    @Test
    public void testPredictImageFile() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        File example = new File("bin/lena.png");

        IndicoResult result = test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 6393826713020433012L;

		{
            put("apis", new Api[] { Api.FER, Api.FacialFeatures });
        }});

        assertTrue(result.getFacialFeatures().size() == 48);
        assertTrue(result.getFer().size() == FacialEmotion.values().length);
    }

    @Test
    public void testBatchPredictText() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"this is great!", "this is terrible!"};

        BatchIndicoResult result = test.text.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 4143333107148067384L;

		{
            put("apis", new Api[] { Api.Sentiment, Api.Language });
        }});

        assertTrue(result.getSentiment().get(0) > .5);
        assertTrue(result.getSentiment().get(1) < .5);
        assertTrue(result.getLanguage().size() == 2);
        assertTrue(result.getLanguage().get(0).size() == Language.values().length);
    }

    @Test
    public void testPredictBatchImageString() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        String[] example = {"bin/lena.png", "bin/lena.png"};

        BatchIndicoResult result = test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 4325122495271807552L;

		{
            put("apis", new Api[] { Api.FER, Api.FacialFeatures });
        }});

        assertTrue(result.getFacialFeatures().get(0).size() == 48);
        assertTrue(result.getFer().size() == 2);
        assertTrue(result.getFacialFeatures().get(0).equals(result.getFacialFeatures().get(1)));
    }
    
    @Test
    public void testPredictBatchImageFile() throws IndicoException, IOException {
        Indico test = new Indico(new File("config.properties"));

        File[] example = {new File("bin/lena.png"), new File("bin/lena.png")};

        BatchIndicoResult result = test.image.predict(example, new HashMap<String, Object>() {

			private static final long serialVersionUID = 4325122495271807552L;

		{
            put("apis", new Api[] { Api.FER, Api.FacialFeatures });
        }});

        assertTrue(result.getFacialFeatures().get(0).size() == 48);
        assertTrue(result.getFer().size() == 2);
        assertTrue(result.getFacialFeatures().get(0).equals(result.getFacialFeatures().get(1)));
    }
}
