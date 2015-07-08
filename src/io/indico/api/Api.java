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
    NamedEntities("namedentities"),
    MultiText("apis", Sentiment, SentimentHQ, Political, Language, TextTags),

    // IMAGE APIS
    FER("fer", true, 48),
    ImageFeatures("imagefeatures", true, 64),
    FacialFeatures("facialfeatures", true, 64),
    MultiImage("apis", true, 48, FER, ImageFeatures, FacialFeatures);

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

    public int getImageSize() {
        return size;
    }
}
