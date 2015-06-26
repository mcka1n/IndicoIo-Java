package io.indico.api;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.indico.api.results.BatchIndicoResult;
import io.indico.api.results.IndicoResult;
import io.indico.api.utils.IndicoException;

/**
 * Created by Chris on 6/26/15.
 */
public class TextApi extends ApiClient{
    Api api;

    public TextApi(Api api, String apiKey) {
        super(apiKey);
        this.api = api;
    }

    public TextApi(Api api, String apiKey, String privateCloud) {
        super(apiKey, privateCloud);
        this.api = api;
    }

    public IndicoResult predict(String data) throws UnsupportedOperationException, IOException, IndicoException {
        return call(api, data, null);
    }

    public BatchIndicoResult predict(List<String> data) throws IOException, IndicoException {
        return call(api, data, null);
    }

    public BatchIndicoResult predict(String[] data) throws IOException, IndicoException {
        return predict(Arrays.asList(data), null);
    }

    public IndicoResult predict(String data, HashMap<String, Object> params) throws UnsupportedOperationException, IOException, IndicoException {
        return call(api, data, params);
    }

    public BatchIndicoResult predict(List<String> data, HashMap<String, Object> params) throws IOException, IndicoException {
        return call(api, data, params);
    }

    public BatchIndicoResult predict(String[] data, HashMap<String, Object> params) throws IOException, IndicoException {
        return predict(Arrays.asList(data), params);
    }

}
