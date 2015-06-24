package io.indico.api;

import com.google.gson.Gson;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.indico.api.results.BatchIndicoResult;
import io.indico.api.results.IndicoResult;
import io.indico.api.utils.ImageUtils;
import io.indico.api.utils.IndicoException;

public class ApiClient {
    private final static String PUBLIC_BASE_URL = "https://apiv2.indico.io";

    private static HttpClient httpClient = HttpClients.createDefault();
    public String baseUrl;
    public String baseEndpoint;
    public String batchEndpoint;

    private ApiClient(String baseUrl, String apiKey, String privateCloud) {
        if (apiKey == null) {
            throw new IllegalArgumentException("Private Cloud cannot be null");
        }

        this.baseUrl = privateCloud == null ?
                baseUrl : "https://" + privateCloud + ".indico.domains";
        this.baseEndpoint = this.baseUrl + "/%1$2s?key=" + apiKey;
        this.batchEndpoint = this.baseUrl + "/%1$2s/batch" + "?key=" + apiKey;
    }

    public ApiClient(String apiKey) {
        this(PUBLIC_BASE_URL, apiKey, null);
    }

    public ApiClient(String apiKey, String privateCloud) {
        this(PUBLIC_BASE_URL, apiKey, privateCloud);
    }

    public IndicoResult call(Api api, BufferedImage data, String type, Map<String, Object> extraParams)
            throws UnsupportedOperationException, IOException, IndicoException {
        return call(api, ImageUtils.encodeImage(data, type), extraParams);
    }

    @SuppressWarnings("unchecked")
    public IndicoResult call(Api api, String data, Map<String, Object> extraParams)
            throws UnsupportedOperationException, IOException, IndicoException {

        Map<String, ?> apiResponse = baseCall(api.name, data, extraParams);
        return new IndicoResult(api, apiResponse);
    }

    public BatchIndicoResult call(Api api, List<BufferedImage> data, String type, Map<String, Object> extraParams)
            throws UnsupportedOperationException, IOException, IndicoException {
        List<String> batchedData = new ArrayList<>(data.size());
        for (BufferedImage image : data)
            batchedData.add(ImageUtils.encodeImage(image, type));
        return call(api, batchedData, extraParams);
    }

    public BatchIndicoResult call(Api api, List<String> data, Map<String, Object> extraParams)
            throws UnsupportedOperationException, IOException, IndicoException {

        Map<String, List<?>> apiResponse = baseCall(api.name, data, extraParams);
        return new BatchIndicoResult(api, apiResponse);
    }

    public BatchIndicoResult call(Api api, Map<BufferedImage, String> data, Map<String, Object> extraParams)
            throws UnsupportedOperationException, IOException, IndicoException {
        List<String> batchedData = new ArrayList<>(data.size());
        for (Map.Entry<BufferedImage, String> entry : data.entrySet())
            batchedData.add(ImageUtils.encodeImage(entry.getKey(), entry.getValue()));

        return call(api, batchedData, extraParams);
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> baseCall(String api, String data, Map<String, Object> extraParams) throws UnsupportedOperationException, IOException {
        HttpResponse response = httpClient.execute(getBasePost(api, data, extraParams, false));
        HttpEntity entity = response.getEntity();

        @SuppressWarnings("rawtypes")
        Map<String, ?> apiResponse = new HashMap();
        if (entity != null) {
            InputStream responseStream = entity.getContent();
            try {
                String responseString = IOUtils.toString(responseStream, "UTF-8");
                apiResponse = new Gson().fromJson(responseString, Map.class);
                if (apiResponse.containsKey("error")) {
                    throw new IllegalArgumentException((String) apiResponse.get("error"));
                }
            } finally {
                responseStream.close();
            }
        }
        return apiResponse;

    }

    @SuppressWarnings("unchecked")
    private Map<String, List<?>> baseCall(String api, List<String> data, Map<String, Object> extraParams) throws IOException {
        HttpResponse response = httpClient.execute(getBasePost(api, data, extraParams, true));
        HttpEntity entity = response.getEntity();

        Map<String, List<?>> apiResponse = new HashMap<>();
        if (entity != null) {
            InputStream responseStream = entity.getContent();
            Reader reader = new InputStreamReader(responseStream, "UTF-8");
            try {
                apiResponse = new Gson().fromJson(reader, Map.class);
            } finally {
                responseStream.close();
            }
        }
        return apiResponse;
    }

    private HttpPost getBasePost(String api, Object data, Map<String, Object> extraParams, boolean batch) throws UnsupportedEncodingException {
        String url = String.format(batch ? batchEndpoint : baseEndpoint, api);
        if (Objects.equals(api, Api.MultiImage.name)) {
            StringBuilder builder = new StringBuilder(url);
            builder.append("&apis=");
            for (Api each : (Api[]) extraParams.get("apis")) {
                builder.append(each.toString()).append(",");
            }

            extraParams.remove("apis");
            url = builder.substring(0, builder.length() - 1);
        }

        HttpPost basePost = new HttpPost(url);

        Map<String, Object> rawParams = new HashMap<>();
        if (extraParams != null && !extraParams.isEmpty())
            rawParams.putAll(extraParams);
        rawParams.put("data", data);

        String entity = new Gson().toJson(rawParams);
        StringEntity params = new StringEntity(entity);
        params.setContentType("application/json");
        basePost.setEntity(params);

        basePost.addHeader("content-type", "application/json");
        basePost.addHeader("client-lib", "java");
        basePost.addHeader("client-lib", "1.4");

        return basePost;
    }
}
