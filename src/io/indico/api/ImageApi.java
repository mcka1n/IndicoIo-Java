package io.indico.api;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ImageApi {

	public Map<String, Double> imageCall(BufferedImage image, String type) throws UnsupportedOperationException, IOException;
	public List<Map<String, Double>> imageCall(List<BufferedImage> images, String type) throws UnsupportedOperationException, IOException;

	public List<Double> featureCall(BufferedImage image, String type) throws UnsupportedOperationException, IOException;
	public List<List<Double>> featureCall(List<BufferedImage> images, String type) throws UnsupportedOperationException, IOException;
}
