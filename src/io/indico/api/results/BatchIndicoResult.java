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
 * Created by Chris on 6/23/15.
 */
public class BatchIndicoResult {
    Map<Api, List<?>> results;

    @SuppressWarnings("unchecked")
    public BatchIndicoResult(Api api, Map<String, ?> response) {
        this.results = new HashMap<>();
        if (api.getResults() == null)
            results.put(api, (List<?>) response.get("results"));
        else
            for (Api res : api.results)
                results.put(res, ((Map<String, List<?>>) response.get(res.name)).get("results"));
    }

    @SuppressWarnings("unchecked")
    public List<Double> sentiment() {
        return (List<Double>) results.get(Api.Sentiment);
    }

    @SuppressWarnings("unchecked")
    public List<Double> sentimentHQ() {
        return (List<Double>) results.get(Api.SentimentHQ);
    }

    @SuppressWarnings("unchecked")
    public List<Map<PoliticalClass, Double>> political() {
        return EnumParser.politinum((List<Map<String, Double>>) results.get(Api.Political));
    }

    @SuppressWarnings("unchecked")
    public List<Map<Language, Double>> language() {
        return EnumParser.langnum((List<Map<String, Double>>) results.get(Api.Language));
    }

    @SuppressWarnings("unchecked")
    public List<Map<TextTag, Double>> textTags() {
        return EnumParser.tagnum((List<Map<String, Double>>) results.get(Api.TextTags));
    }

    @SuppressWarnings("unchecked")
    public List<Map<FacialEmotion, Double>> fer() {
        return EnumParser.fernum((List<Map<String, Double>>) results.get(Api.FER));
    }

    @SuppressWarnings("unchecked")
    public List<List<Double>> imageFeatures() {
        return (List<List<Double>>) results.get(Api.ImageFeatures);
    }

    @SuppressWarnings("unchecked")
    public List<List<Double>> facialFeatures() {
        return (List<List<Double>>) results.get(Api.FacialFeatures);
    }
}

