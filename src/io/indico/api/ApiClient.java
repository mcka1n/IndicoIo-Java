package io.indico.api;

import com.google.gson.Gson;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import io.indico.api.results.BatchIndicoResult;
import io.indico.api.results.IndicoResult;

public class ApiClient {
    private final static String PUBLIC_BASE_URL = "https://apiv2.indico.io";

    private static HttpClient httpClient = HttpClients.createDefault();
    public String baseUrl;
    public String baseEndpoint;
    public String batchEndpoint;


    public ApiClient(String apiKey) {
        this(PUBLIC_BASE_URL, apiKey, null);
    }

    private ApiClient(String baseUrl, String apiKey, String privateCloud) {
        if (apiKey == null) {
            throw new IllegalArgumentException("Private Cloud cannot be null");
        }

        this.baseUrl = privateCloud == null ?
                baseUrl : "https://" + privateCloud + ".indico.domains";
        this.baseEndpoint = this.baseUrl + "/%1$2s?key=" + apiKey;
        this.batchEndpoint = this.baseUrl + "/%1$2s/batch" + "?key=" + apiKey;
    }

    public ApiClient(String apiKey, String privateCloud) {
        this(PUBLIC_BASE_URL, apiKey, privateCloud);
    }

    public IndicoResult call(Api api, BufferedImage data, String type, Map<String, Object> extraParams)
            throws UnsupportedOperationException, IOException {
        return call(api, encodeImage(data, type), extraParams);
    }

    @SuppressWarnings("unchecked")
    public IndicoResult call(Api api, String data, Map<String, Object> extraParams)
            throws UnsupportedOperationException, IOException {

        Map<String, ?> apiResponse = baseCall(api.name, data, extraParams);
        return new IndicoResult(api, apiResponse);
    }

    private String encodeImage(BufferedImage image, String type) {
        String imageString = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try {
            ImageIO.write(image, type, bos);
            byte[] imageBytes = bos.toByteArray();

            imageString = Base64.encodeBase64String(imageBytes);

            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return imageString;
    }

    @SuppressWarnings("unchecked")
    private Map<String, ?> baseCall(String api, String data, Map<String, Object> extraParams) throws UnsupportedOperationException, IOException {
        HttpPost basePost = new HttpPost(String.format(baseEndpoint, api));

        List<NameValuePair> params = new ArrayList<>(1);
        params.add(new BasicNameValuePair("data", data));
        basePost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

        basePost.addHeader("client-lib", "java");
        basePost.addHeader("client-lib", "1.4");

        HttpResponse response = httpClient.execute(basePost);
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

    public BatchIndicoResult call(Api api, List<BufferedImage> data, String type, Map<String, Object> extraParams)
            throws UnsupportedOperationException, IOException {
        List<String> batchedData = new ArrayList<>(data.size());
        for (BufferedImage image : data)
            batchedData.add(encodeImage(image, type));
        return call(api, batchedData, extraParams);
    }

    public BatchIndicoResult call(Api api, List<String> data, Map<String, Object> extraParams)
            throws UnsupportedOperationException, IOException {

        Map<String, List<?>> apiResponse = baseCall(api.name, data, extraParams);
        return new BatchIndicoResult(api, apiResponse);
    }

    @SuppressWarnings("unchecked")
    private Map<String, List<?>> baseCall(String api, List<String> data, Map<String, Object> extraParams) throws IOException {
        HttpPost basePost = new HttpPost(String.format(batchEndpoint, api));

        Map<String, Object> rawParams = extraParams != null ? extraParams : new HashMap<String, Object>();
        rawParams.put("data", data);

        StringEntity params = new StringEntity(new Gson().toJson(rawParams));
        basePost.setEntity(params);

        basePost.addHeader("content-type", "application/x-www-form-urlencoded");
        basePost.addHeader("client-lib", "java");
        basePost.addHeader("client-lib", "1.4");

        HttpResponse response = httpClient.execute(basePost);
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

    public BatchIndicoResult call(Api api, Map<BufferedImage, String> data, Map<String, Object> extraParams)
            throws UnsupportedOperationException, IOException {
        List<String> batchedData = new ArrayList<>(data.size());
        for (Map.Entry<BufferedImage, String> entry : data.entrySet())
            batchedData.add(encodeImage(entry.getKey(), entry.getValue()));

        return call(api, batchedData, extraParams);
    }
}
