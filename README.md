# IndicoIo-Java

A wrapper for the [indico API](http://indico.io).
The indico API is free to use, and no training data is required.

Full Documentation + API Keys + Setup
----------------
For API key registration and setup and docs, checkout our [quickstart guide](https://indico.io/docs).

Supported APIs:
------------

- Positive/Negative Sentiment Analysis
- Political Sentiment Analysis
- Keywords Analysis
- Named Entities Recognition
- Image Feature Extraction
- Image Recognition
- Content Filtering Analysis
- Facial Emotion Recognition
- Facial Feature Extraction
- Language Detection
- Text Topic Tagging

Example:
------------
```java
import io.indico.Indico;
import io.indico.api.IndicoResult;
import io.indico.api.BatchIndicoResult;

// single example
Indico indico = new Indico('YOUR_API_KEY');
IndicoResult single = indico.sentiment.predict(
    "indico is so easy to use!"
);
Double result = single.getSentiment();
System.out.println(result);

// batch example
String[] example = {
    "indico is so easy to use!", 
    "everything is awesome!"
};
BatchIndicoResult multiple = indico.sentiment.predict(example);
List<Double> results = multiple.getSentiment();
System.out.println(results);
```
