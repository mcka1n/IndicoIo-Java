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
import io.indico.api.utils.IndicoException;

/**
 * Created by Chris on 6/23/15.
 */
public class BatchIndicoResult {
    Map<Api, List<?>> results;

    @SuppressWarnings("unchecked")
    public BatchIndicoResult(Api api, Map<String, ?> response) throws IndicoException {
        this.results = new HashMap<>();
        if (api.getResults() == null)
            results.put(api, (List<?>) response.get("results"));
        else {
            if (response.containsKey("error")) {
                throw new IndicoException(api.name + " failed with error " + response.get("error"));
            }
            Map<String, ?> responses = (Map<String, ?>) response.get("results");
            for (Api res : api.results) {
                if (!responses.containsKey(res.name))
                    continue;
                Map<String, ?> apiResponse = (Map<String, ?>) responses.get(res.name);
                if (apiResponse.containsKey("error"))
                    throw new IndicoException(res.name + " failed with error " + apiResponse.get("error"));
                results.put(res, (List<?>) apiResponse.get("results"));
            }
        }
    }

    @SuppressWarnings("unchecked")
    public List<Double> getSentiment() throws IndicoException {
        return (List<Double>) get(Api.Sentiment);
    }

    @SuppressWarnings("unchecked")
    public List<Double> getSentimentHQ() throws IndicoException {
        return (List<Double>) get(Api.SentimentHQ);
    }

    @SuppressWarnings("unchecked")
    public List<Map<PoliticalClass, Double>> getPolitical() throws IndicoException {
        return EnumParser.politinum((List<Map<String, Double>>) get(Api.Political));
    }

    @SuppressWarnings("unchecked")
    public List<Map<Language, Double>> getLanguage() throws IndicoException {
        return EnumParser.langnum((List<Map<String, Double>>) get(Api.Language));
    }

    @SuppressWarnings("unchecked")
    public List<Map<TextTag, Double>> getTextTags() throws IndicoException {
        return EnumParser.tagnum((List<Map<String, Double>>) get(Api.TextTags));
    }

    @SuppressWarnings("unchecked")
    public List<Map<FacialEmotion, Double>> getFer() throws IndicoException {
        return EnumParser.fernum((List<Map<String, Double>>) get(Api.FER));
    }

    @SuppressWarnings("unchecked")
    public List<List<Double>> getImageFeatures() throws IndicoException {
        return (List<List<Double>>) get(Api.ImageFeatures);
    }

    @SuppressWarnings("unchecked")
    public List<List<Double>> getFacialFeatures() throws IndicoException {
        return (List<List<Double>>) get(Api.FacialFeatures);
    }

    @SuppressWarnings("unchecked")
    public List<Map<String, Double>> getKeywords() throws IndicoException {
        return (List<Map<String, Double>>) get(Api.Keywords);
    }

    @SuppressWarnings("unchecked")
    public List<Double> getNudityDetection() throws IndicoException {
        return (List<Double>) get(Api.NudityDetection);
    }

    private List<?> get(Api name) throws IndicoException{
        if (!results.containsKey(name))
            throw new IndicoException(name.name + " was not included in the request");
        return results.get(name);
    }
}

