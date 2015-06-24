package io.indico.test;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.indico.api.Api;
import io.indico.api.image.FacialEmotion;
import io.indico.api.results.BatchIndicoResult;
import io.indico.api.results.IndicoResult;
import io.indico.api.text.Language;
import io.indico.api.text.PoliticalClass;
import io.indico.api.text.TextTag;
import io.indico.api.utils.IndicoException;
import io.indico.indico;

import static org.junit.Assert.assertTrue;

public class ApiTest {

    @Test(expected = IllegalArgumentException.class)
    public void testBadCall() throws UnsupportedOperationException, IOException, IndicoException {
        indico test = new indico("notanapikey");
        test.sentiment("this is going to error!");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNoApiKey() throws UnsupportedOperationException, IOException, IndicoException {
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
    public void testSentiment() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        assertTrue(test.sentiment("this is great!").getSentiment() > 0.5);
    }

    @Test
    public void testBatchSentimentList() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        List<String> examples = new ArrayList<String>();
        examples.add("this is great!");
        examples.add("this is awful!");
        List<Double> results = test.sentiment(examples).getSentiment();
        assertTrue(results.get(0) > 0.5);
        assertTrue(results.get(1) < 0.5);
    }

    @Test
    public void testBatchSentimentArray() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        List<Double> results = test.sentiment(new String[]{"this is great!", "this is awful!"}).getSentiment();
        assertTrue(results.get(0) > 0.5);
        assertTrue(results.get(1) < 0.5);
    }

    @Test
    public void testSentimentHQ() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        assertTrue(test.sentimentHQ("this is great!").getSentimentHQ() > 0.5);
    }

    @Test
    public void testBatchSentimentHQList() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        List<String> examples = new ArrayList<String>();
        examples.add("this is great!");
        examples.add("this is awful!");
        List<Double> results = test.sentimentHQ(examples).getSentimentHQ();
        assertTrue(results.get(0) > 0.5);
        assertTrue(results.get(1) < 0.5);
    }

    @Test
    public void testBatchSentimentHQArray() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        List<Double> results = test.sentimentHQ(new String[]{"this is great!", "this is awful!"}).getSentimentHQ();
        assertTrue(results.get(0) > 0.5);
        assertTrue(results.get(1) < 0.5);
    }

    @Test
    public void testPolitical() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        assertTrue(test.political("test").getPolitical().size() == PoliticalClass.values().length);
    }

    @Test
    public void testBatchPoliticalList() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        List<String> examples = new ArrayList<String>();
        examples.add("this is great!");
        examples.add("this is awful!");
        List<Map<PoliticalClass, Double>> results = test.political(examples).getPolitical();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == PoliticalClass.values().length);
    }

    @Test
    public void testBatchPoliticalArray() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        List<Map<PoliticalClass, Double>> results = test.political(new String[]{"this is great!", "this is awful!"}).getPolitical();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == PoliticalClass.values().length);
    }

    @Test
    public void testLanguage() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        assertTrue(test.language("test").getLanguage().size() == Language.values().length);
    }

    @Test
    public void testBatchLanguageList() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        List<String> examples = new ArrayList<String>();
        examples.add("this is great!");
        examples.add("this is awful!");
        List<Map<Language, Double>> results = test.language(examples).getLanguage();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == Language.values().length);
    }

    @Test
    public void testBatchLanguageArray() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        List<Map<Language, Double>> results = test.language(new String[]{"this is great!", "this is awful!"}).getLanguage();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == Language.values().length);
    }

    @Test
    public void testTextTags() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        assertTrue(test.textTags("test").getTextTags().size() == TextTag.values().length);
    }

    @Test
    public void testBatchTextTagsList() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        List<String> examples = new ArrayList<String>();
        examples.add("this is great!");
        examples.add("this is awful!");
        List<Map<TextTag, Double>> results = test.textTags(examples).getTextTags();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == TextTag.values().length);
    }

    @Test
    public void testBatchTextTagArray() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));
        List<Map<TextTag, Double>> results = test.textTags(new String[]{"this is great!", "this is awful!"}).getTextTags();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).size() == TextTag.values().length);
    }

    @Test
    public void testFER() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));

        Map<FacialEmotion, Double> results = test.fer("bin/lena.png").getFer();
        assertTrue(results.size() == FacialEmotion.values().length);
    }

    @Test
    public void testBatchFER() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));

        List<String> lenas = new ArrayList<String>();
        lenas.add("bin/lena.png");
        lenas.add("bin/lena.png");

        List<Map<FacialEmotion, Double>> results = test.fer(lenas).getFer();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
    }

    @Test
    public void testImageFeatures() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));

        List<Double> results = test.imageFeatures("bin/lena.png").getImageFeatures();
        assertTrue(results.size() == 2048);
    }

    @Test
    public void testBatchImageFeatures() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));

        List<String> lenas = new ArrayList<String>();
        lenas.add("bin/lena.png");
        lenas.add("bin/lena.png");

        List<List<Double>> results = test.imageFeatures(lenas).getImageFeatures();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
    }

    @Test
    public void testFacialFeatures() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));

        List<Double> results = test.facialFeatures("bin/lena.png").getFacialFeatures();
        assertTrue(results.size() == 48);
    }

    @Test
    public void testBatchFacialFeatures() throws IOException, IndicoException {
        indico test = new indico(new File("config.properties"));

        List<String> lenas = new ArrayList<String>();
        lenas.add("bin/lena.png");
        lenas.add("bin/lena.png");

        List<List<Double>> results = test.facialFeatures(lenas).getFacialFeatures();
        assertTrue(results.size() == 2);
        assertTrue(results.get(0).equals(results.get(1)));
    }

    @Test
    public void testPredictText() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String example = "this is great!";
        Api[] apis = {Api.Sentiment, Api.Language};

        IndicoResult result = test.predictText(example, apis);
        assertTrue(result.getSentiment() > .5);
        assertTrue(result.getLanguage().size() == Language.values().length);
    }

    @Test(expected = IndicoException.class)
    public void testBadEmptyApisPredictText() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String example = "this is going to break!";
        Api[] apis = {};

        test.predictText(example, apis);
    }

    @Test(expected = IndicoException.class)
    public void testBadResultsPredictText() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String example = "this is going to break!";
        Api[] apis = {Api.Sentiment, Api.SentimentHQ};

        IndicoResult result = test.predictText(example, apis);
        assertTrue(result.getLanguage() == null);
    }

    @Test
    public void testPredictImage() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String example = "bin/lena.png";
        Api[] apis = {Api.FER, Api.FacialFeatures};

        IndicoResult result = test.predictImage(example, apis);
        assertTrue(result.getFacialFeatures().size() == 48);
        assertTrue(result.getFer().size() == FacialEmotion.values().length);
    }

    @Test(expected = IndicoException.class)
    public void testBadEmptyApisPredictImage() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String example = "bin/lena.png";
        Api[] apis = {};

        test.predictImage(example, apis);
    }

    @Test(expected = IndicoException.class)
    public void testBadApisPredictImage() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String example = "bin/lena.png";
        Api[] apis = {Api.Sentiment};

        test.predictImage(example, apis);
    }

    @Test(expected = IndicoException.class)
    public void testBadResultsPredictImage() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String example = "bin/lena.png";
        Api[] apis = {Api.FER, Api.FacialFeatures};

        IndicoResult result = test.predictImage(example, apis);
        assertTrue(result.getImageFeatures() == null);
    }


    @Test
    public void testBatchPredictText() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String[] example = {"this is great!", "this is terrible!"};
        Api[] apis = {Api.Sentiment, Api.Language};

        BatchIndicoResult result = test.predictText(example, apis);
        assertTrue(result.getSentiment().get(0) > .5);
        assertTrue(result.getSentiment().get(1) < .5);
        assertTrue(result.getLanguage().size() == 2);
        assertTrue(result.getLanguage().get(0).size() == Language.values().length);
    }

    @Test(expected = IndicoException.class)
    public void testBadEmptyApisBatchPredictText() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String[] example = {"this is going to break!", "This is not going to break"};
        Api[] apis = {};

        test.predictText(example, apis);
    }

    @Test(expected = IndicoException.class)
    public void testBadResultsBatchPredictText() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String[] example = {"this is going to break!", "errr"};
        Api[] apis = {Api.Sentiment, Api.SentimentHQ};

        BatchIndicoResult result = test.predictText(example, apis);
        assertTrue(result.getLanguage() == null);
    }

    @Test
    public void testPredictBatchImage() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String[] example = {"bin/lena.png", "bin/lena.png"};
        Api[] apis = {Api.FER, Api.FacialFeatures};

        BatchIndicoResult result = test.predictImage(example, apis);
        assertTrue(result.getFacialFeatures().get(0).size() == 48);
        assertTrue(result.getFer().size() == 2);
        assertTrue(result.getFacialFeatures().get(0).equals(result.getFacialFeatures().get(1)));
    }

    @Test(expected = IndicoException.class)
    public void testBadEmptyApisBatchPredictImage() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String[] example = {"bin/lena.png", "bin/lena.png"};
        Api[] apis = {};

        test.predictImage(example, apis);
    }

    @Test(expected = IndicoException.class)
    public void testBadApisBatchPredictImage() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String[] example = {"bin/lena.png", "bin/lena.png"};
        Api[] apis = {Api.Sentiment};

        test.predictImage(example, apis);
    }

    @Test(expected = IndicoException.class)
    public void testBadResultsBatchPredictImage() throws IndicoException, IOException {
        indico test = new indico(new File("config.properties"));

        String[] example = {"bin/lena.png", "bin/lena.png"};
        Api[] apis = {Api.FER, Api.FacialFeatures};

        BatchIndicoResult result = test.predictImage(example, apis);
        assertTrue(result.getImageFeatures() == null);
    }
}
