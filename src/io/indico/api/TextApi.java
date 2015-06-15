package io.indico.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

public interface TextApi {
	
	public Map<String, Double> call(String data) throws UnsupportedEncodingException, IOException;
	public List<Map<String, Double>> call(List<String> data) throws ClientProtocolException, IOException;

	public Double sentimentCall(String data) throws UnsupportedEncodingException, IOException;	
	public List<Double> sentimentCall(List<String> data) throws ClientProtocolException, IOException;
}
