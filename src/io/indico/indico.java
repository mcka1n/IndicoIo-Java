package io.indico;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import io.indico.api.Api;
import io.indico.api.ImageApi;
import io.indico.api.TextApi;

public class indico {

    public TextApi sentiment, sentimentHQ, political, language, textTags, text;
    public ImageApi fer, facialFeatures, imageFeatures, image;
    public String apiKey;
    public String cloud;

    public indico(String apiKey) {
        this.apiKey = apiKey;
        this.initializeClients();
    }

    public indico(String apiKey, String privateCloud) {
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
        if (this.cloud != null) {
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
        if (this.cloud != null) {
            prop.setProperty("privateCloud", this.cloud);
        }
        prop.store(output, null);

        output.close();
    }

    private void initializeClients() {
        this.sentiment = new TextApi(Api.Sentiment, this.apiKey, this.cloud);
        this.sentimentHQ = new TextApi(Api.SentimentHQ, this.apiKey, this.cloud);
        this.political = new TextApi(Api.Political, this.apiKey, this.cloud);
        this.language = new TextApi(Api.Language, this.apiKey, this.cloud);
        this.textTags = new TextApi(Api.TextTags, this.apiKey, this.cloud);
        this.text = new TextApi(Api.MultiText, this.apiKey, this.cloud);

        this.fer = new ImageApi(Api.FER, this.apiKey, this.cloud);
        this.facialFeatures = new ImageApi(Api.FacialFeatures, this.apiKey, this.cloud);
        this.imageFeatures = new ImageApi(Api.ImageFeatures, this.apiKey, this.cloud);
        this.image = new ImageApi(Api.MultiImage, this.apiKey, this.cloud);
    }
}
