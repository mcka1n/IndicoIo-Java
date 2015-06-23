package io.indico.api;

import io.indico.api.utils.IndicoException;

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

    // IMAGE APIS
    FER("fer", true),
    ImageFeatures("imagefeatures", true),
    FacialFeatures("facialfeatures", true);

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

    static Api from(String name) throws IndicoException {
        for (Api api : Api.values()) {
            if (name.equals(api.name))
                return api;
        }

        throw new IndicoException(name + " is not a valid api name");
    }

    public Api[] getResults() {
        return results;
    }

}
