package io.indico.api;

import java.util.HashMap;

/**
 * Created by Chris on 6/22/15.
 */
public enum Api {
    // TEXT APIS
    Sentiment("sentiment"),
    SentimentHQ("sentimenthq"),
    Political("political"),
    Language("language"),
    TextTags("texttags"),
    NamedEntities("namedentities"),
    Keywords("keywords"),
    MultiText("apis", Sentiment, SentimentHQ, Political, Language, TextTags, Keywords, NamedEntities),

    // IMAGE APIS
    FER("fer", true, 48),
    ImageFeatures("imagefeatures", true, 64),
    FacialFeatures("facialfeatures", true, 64),
    ContentFiltering("contentfiltering", true, -1),
    MultiImage("apis", true, 48, FER, ImageFeatures, FacialFeatures, ContentFiltering);

    public String name;
    public String type;
    public int size;
    public boolean isImageApi;
    public Api[] results;

    Api(String name) {
        this(name, false, 0);
    }

    Api(String name, boolean isImageApi, int size) {
        this.isImageApi = isImageApi;
        this.name = name;
        this.type = isImageApi ? "image" : "text";
        this.size = size;
    }

    Api(String name, Api... apis) {
        this(name, false, 0, apis);
    }

    Api(String name, boolean isImageApi, int size, Api... apis) {
        this(name, isImageApi, size);
        this.results = apis;
    }

    public Api[] getResults() {
        return results;
    }

    public boolean isImage() {
        return isImageApi;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public int getSize(HashMap<String, Object> params) {
        if (this == Api.FER
            && params != null
            && params.containsKey("detect")
            && params.get("detect") == true)
                return -1;
        return size;
    }
}