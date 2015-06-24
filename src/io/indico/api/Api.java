package io.indico.api;

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
    MultiText("apis", Sentiment, SentimentHQ, Political, Language, TextTags),

    // IMAGE APIS
    FER("fer", true),
    ImageFeatures("imagefeatures", true),
    FacialFeatures("facialfeatures", true),
    MultiImage("apis", FER, ImageFeatures, FacialFeatures);

    public String name;
    public boolean isImageApi;
    public Api[] results;

    Api(String name) {
        this(name, false);
    }

    Api(String name, boolean isImageApi) {
        this.isImageApi = isImageApi;
        this.name = name;
    }

    Api(String name, Api... apis) {
        this(name, false, apis);
    }

    Api(String name, boolean isImageApi, Api... apis) {
        this(name, isImageApi);
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
}
