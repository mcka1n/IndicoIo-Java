package io.indico.api.results;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
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
        if (!results.containsKey(Api.Sentiment))
            throw new IndicoException(Api.Sentiment.name + " was not included in the request");
        return (List<Double>) results.get(Api.Sentiment);
    }

    @SuppressWarnings("unchecked")
    public List<Double> getSentimentHQ() throws IndicoException {
        if (!results.containsKey(Api.SentimentHQ))
            throw new IndicoException(Api.SentimentHQ.name + " was not included in the request");
        return (List<Double>) results.get(Api.SentimentHQ);
    }

    @SuppressWarnings("unchecked")
    public List<Map<PoliticalClass, Double>> getPolitical() throws IndicoException {
        if (!results.containsKey(Api.Political))
            throw new IndicoException(Api.Political.name + " was not included in the request");
        return EnumParser.politinum((List<Map<String, Double>>) results.get(Api.Political));
    }

    @SuppressWarnings("unchecked")
    public List<Map<Language, Double>> getLanguage() throws IndicoException {
        if (!results.containsKey(Api.Language))
            throw new IndicoException(Api.Language.name + " was not included in the request");
        return EnumParser.langnum((List<Map<String, Double>>) results.get(Api.Language));
    }

    @SuppressWarnings("unchecked")
    public List<Map<TextTag, Double>> getTextTags() throws IndicoException {
        if (!results.containsKey(Api.TextTags))
            throw new IndicoException(Api.TextTags.name + " was not included in the request");
        return EnumParser.tagnum((List<Map<String, Double>>) results.get(Api.TextTags));
    }

    @SuppressWarnings("unchecked")
    public List<Map<FacialEmotion, Double>> getFer() throws IndicoException {
        if (!results.containsKey(Api.FER))
            throw new IndicoException(Api.FER.name + " was not included in the request");
        return EnumParser.fernum((List<Map<String, Double>>) results.get(Api.FER));
    }

    @SuppressWarnings("unchecked")
    public List<Map<Point, Map<FacialEmotion, Double>>> getLocalizedFer() throws IndicoException {
        List<Map<Point, Map<FacialEmotion, Double>>> ret = new ArrayList<>();

        if (!results.containsKey(Api.FER))
            throw new IndicoException(Api.FER.name +  " was not included in the request");
        try {
            List<List<Map<String, Object>>> result = (List<List<Map<String, Object>>>) results.get(Api.FER);
            for (List<Map<String, Object>> res : result) {
                Map<Point, Map<FacialEmotion, Double>> parsed = new HashMap<>();
                for (Map<String, Object> each : res) {
                    int[] point = (int[]) each.get("location");
                    parsed.put(new Point(point[0], point[1]), EnumParser.fernum((Map<String, Double>) each.get("emotions")));
                }
                ret.add(parsed);
            }
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

        return ret;
    }

    @SuppressWarnings("unchecked")
    public List<List<Double>> getImageFeatures() throws IndicoException {
        if (!results.containsKey(Api.ImageFeatures))
            throw new IndicoException(Api.ImageFeatures.name + " was not included in the request");
        return (List<List<Double>>) results.get(Api.ImageFeatures);
    }

    @SuppressWarnings("unchecked")
    public List<List<Double>> getFacialFeatures() throws IndicoException {
        if (!results.containsKey(Api.FacialFeatures))
            throw new IndicoException(Api.FacialFeatures.name + " was not included in the request");
        return (List<List<Double>>) results.get(Api.FacialFeatures);
    }
}

