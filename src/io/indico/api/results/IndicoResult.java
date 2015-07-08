package io.indico.api.results;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.indico.api.Api;
import io.indico.api.image.FacialEmotion;
import io.indico.api.text.Category;
import io.indico.api.text.Language;
import io.indico.api.text.PoliticalClass;
import io.indico.api.text.TextTag;
import io.indico.api.utils.EnumParser;
import io.indico.api.utils.IndicoException;

/**
 * Created by Chris on 6/22/15.
 */
public class IndicoResult {
    Map<Api, Object> results;

    @SuppressWarnings("unchecked")
    public IndicoResult(Api api, Map<String, ?> response) throws IndicoException {
        this.results = new HashMap<>();
        if (api.getResults() == null)
            results.put(api, response.get("results"));
        else {
            Map<String, ?> responses = (Map<String, ?>) response.get("results");
            for (Api res : api.results) {
                Map<String, ?> apiResponse = (Map<String, ?>) responses.get(res.name);
                if (apiResponse == null)
                    continue;
                if (apiResponse.containsKey("error"))
                    throw new IndicoException(api.name + " failed with error " + apiResponse.get("error"));
                results.put(res, apiResponse.get("results"));
            }
        }
    }

    public Double getSentiment() throws IndicoException {
        if (!results.containsKey(Api.Sentiment))
            throw new IndicoException(Api.Sentiment.name + " was not included in the request");
        return (Double) results.get(Api.Sentiment);
    }

    public Double getSentimentHQ() throws IndicoException {
        if (!results.containsKey(Api.SentimentHQ))
            throw new IndicoException(Api.SentimentHQ.name + " was not included in the request");
        return (Double) results.get(Api.SentimentHQ);
    }

    @SuppressWarnings("unchecked")
    public Map<PoliticalClass, Double> getPolitical() throws IndicoException {
        if (!results.containsKey(Api.Political))
            throw new IndicoException(Api.Political.name + " was not included in the request");
        return EnumParser.parse(PoliticalClass.class, (Map<String, Double>) results.get(Api.Political));
    }

    @SuppressWarnings("unchecked")
    public Map<Language, Double> getLanguage() throws IndicoException {
        if (!results.containsKey(Api.Language))
            throw new IndicoException(Api.Language.name + " was not included in the request");
        return EnumParser.parse(Language.class, (Map<String, Double>) results.get(Api.Language));
    }

    @SuppressWarnings("unchecked")
    public Map<TextTag, Double> getTextTags() throws IndicoException {
        if (!results.containsKey(Api.TextTags))
            throw new IndicoException(Api.TextTags.name + " was not included in the request");
        return EnumParser.parse(TextTag.class, (Map < String, Double >)results.get(Api.TextTags));
    }

    @SuppressWarnings("unchecked")
    public Map<String, Map<Category, Double>> getNamedEntities() throws IndicoException {
        if (!results.containsKey(Api.NamedEntities))
            throw new IndicoException(Api.NamedEntities.name + " was not included in the request");

        Map<String, Map<Category, Double>> result = new HashMap<>();
        Map<String, Map<String, Object>> response = (Map<String, Map<String, Object>>) results.get(Api.NamedEntities);
        for (Map.Entry<String, Map<String, Object>> entry : response.entrySet()) {
            Map<String, Double> res = new HashMap<>();

            res.putAll((Map<String, Double>) entry.getValue().remove("categories"));
            res.put("confidence", (Double) entry.getValue().get("confidence"));
            result.put(entry.getKey(), EnumParser.parse(Category.class, res));
        }

        return result;
    }

    @SuppressWarnings("unchecked")
    public Map<FacialEmotion, Double> getFer() throws IndicoException {
        if (!results.containsKey(Api.FER))
            throw new IndicoException(Api.FER.name + " was not included in the request");
        return EnumParser.parse(FacialEmotion.class, (Map < String, Double >)results.get(Api.FER));
    }

    @SuppressWarnings("unchecked")
    public List<Double> getImageFeatures() throws IndicoException {
        if (!results.containsKey(Api.ImageFeatures))
            throw new IndicoException(Api.ImageFeatures.name + " was not included in the request");
        return (List<Double>) results.get(Api.ImageFeatures);
    }

    @SuppressWarnings("unchecked")
    public List<Double> getFacialFeatures() throws IndicoException {
        if (!results.containsKey(Api.FacialFeatures))
            throw new IndicoException(Api.FacialFeatures.name + " was not included in the request");
        return (List<Double>) results.get(Api.FacialFeatures);
    }
}
