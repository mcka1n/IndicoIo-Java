package io.indico.api.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.indico.api.image.FacialEmotion;
import io.indico.api.text.Language;
import io.indico.api.text.PoliticalClass;
import io.indico.api.text.TextTag;

/**
 * Created by Chris on 6/23/15.
 */
public class EnumParser {
    public static List<Map<PoliticalClass, Double>> politinum(List<Map<String, Double>> apiResponse) {
        List<Map<PoliticalClass, Double>> cleanedResponse = new ArrayList<>();
        for (Map<String, Double> entry : apiResponse) {
            cleanedResponse.add(politinum(entry));
        }
        return cleanedResponse;
    }

    public static Map<PoliticalClass, Double> politinum(Map<String, Double> apiResponse) {
        Map<PoliticalClass, Double> mappedResponse = new HashMap<>();
        for (Map.Entry<String, Double> entry : apiResponse.entrySet()) {
            mappedResponse.put(PoliticalClass.valueOf(entry.getKey()), entry.getValue());
        }
        return mappedResponse;
    }

    public static List<Map<Language, Double>> langnum(List<Map<String, Double>> apiResponse) {
        List<Map<Language, Double>> cleanedResponse = new ArrayList<Map<Language, Double>>();
        for (Map<String, Double> entry : apiResponse) {
            cleanedResponse.add(langnum(entry));
        }
        return cleanedResponse;
    }

    public static Map<Language, Double> langnum(Map<String, Double> apiResponse) {
        Map<Language, Double> mappedResponse = new HashMap<Language, Double>();
        for (Map.Entry<String, Double> entry : apiResponse.entrySet()) {
            if (entry.getKey().equals("Persian (Farsi)")) {
                mappedResponse.put(Language.valueOf("Persian"), entry.getValue());
            } else {
                mappedResponse.put(Language.valueOf(entry.getKey()), entry.getValue());
            }
        }
        return mappedResponse;
    }

    public static List<Map<TextTag, Double>> tagnum(List<Map<String, Double>> apiResponse) {
        List<Map<TextTag, Double>> cleanedResponse = new ArrayList<Map<TextTag, Double>>();
        for (Map<String, Double> entry : apiResponse) {
            cleanedResponse.add(tagnum(entry));
        }
        return cleanedResponse;
    }

    public static Map<TextTag, Double> tagnum(Map<String, Double> apiResponse) {
        Map<TextTag, Double> mappedResponse = new HashMap<TextTag, Double>();
        for (Map.Entry<String, Double> entry : apiResponse.entrySet()) {
            mappedResponse.put(TextTag.valueOf(entry.getKey()), entry.getValue());
        }
        return mappedResponse;
    }

    public static List<Map<FacialEmotion, Double>> fernum(List<Map<String, Double>> apiResponse) {
        List<Map<FacialEmotion, Double>> cleanedResponse = new ArrayList<Map<FacialEmotion, Double>>();
        for (Map<String, Double> entry : apiResponse) {
            cleanedResponse.add(fernum(entry));
        }
        return cleanedResponse;
    }

    public static Map<FacialEmotion, Double> fernum(Map<String, Double> apiResponse) {
        Map<FacialEmotion, Double> mappedResponse = new HashMap<FacialEmotion, Double>();
        for (Map.Entry<String, Double> entry : apiResponse.entrySet()) {
            mappedResponse.put(FacialEmotion.valueOf(entry.getKey()), entry.getValue());
        }
        return mappedResponse;
    }
}
