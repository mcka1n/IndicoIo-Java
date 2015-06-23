package io.indico.api.results;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.indico.api.Api;
import io.indico.api.image.FacialEmotion;
import io.indico.api.text.Language;
import io.indico.api.text.PoliticalClass;
import io.indico.api.text.TextTag;
import io.indico.api.utils.EnumParser;

/**
 * Created by Chris on 6/22/15.
 */
public class IndicoResult {
    Map<Api, Object> results;

    @SuppressWarnings("unchecked")
    public IndicoResult(Api api, Map<String, ?> response) {
        this.results = new HashMap<>();
        if (api.getResults() == null)
            results.put(api, response.get("results"));
        else
            for (Api res : api.results)
                results.put(res, ((Map<String, ?>) response.get(res.name)).get("results"));
    }

    public Double sentiment() {
        return (Double) results.get(Api.Sentiment);
    }

    public Double sentimentHQ() {
        return (Double) results.get(Api.SentimentHQ);
    }

    @SuppressWarnings("unchecked")
    public Map<PoliticalClass, Double> political() {
        return EnumParser.politinum((Map<String, Double>) results.get(Api.Political));
    }

    @SuppressWarnings("unchecked")
    public Map<Language, Double> language() {
        return EnumParser.langnum((Map<String, Double>) results.get(Api.Language));
    }

    @SuppressWarnings("unchecked")
    public Map<TextTag, Double> textTags() {
        return EnumParser.tagnum((Map<String, Double>) results.get(Api.TextTags));
    }

    @SuppressWarnings("unchecked")
    public Map<FacialEmotion, Double> fer() {
        return EnumParser.fernum((Map<String, Double>) results.get(Api.FER));
    }

    @SuppressWarnings("unchecked")
    public List<Double> imageFeatures() {
        return (List<Double>) results.get(Api.ImageFeatures);
    }

    @SuppressWarnings("unchecked")
    public List<Double> facialFeatures() {
        return (List<Double>) results.get(Api.FacialFeatures);
    }
}
