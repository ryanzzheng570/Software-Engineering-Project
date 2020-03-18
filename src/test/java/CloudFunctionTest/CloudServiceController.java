package CloudFunctionTest;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CloudServiceController {

    // List all our Cloud Function URLs here
    public static final String addItem = "https://us-central1-engineeringlabproject.cloudfunctions.net/addItem";
    public static final String addItemToStore = "https://us-central1-engineeringlabproject.cloudfunctions.net/addItemToStore";
    public static final String addShop = "https://us-central1-engineeringlabproject.cloudfunctions.net/addShop";
    public static final String addTag = "https://us-central1-engineeringlabproject.cloudfunctions.net/addTag";
    public static final String changeShopName = "https://us-central1-engineeringlabproject.cloudfunctions.net/changeShopName";
    public static final String purchaseItems = "https://us-central1-engineeringlabproject.cloudfunctions.net/purchaseItems";
    public static final String removeItem = "https://us-central1-engineeringlabproject.cloudfunctions.net/removeItem";
    public static final String removeTag = "https://us-central1-engineeringlabproject.cloudfunctions.net/removeTag";

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    private void close() throws IOException {
        httpClient.close();
    }

    public String sendPost(String url, HashMap<String, String> data) throws Exception {
        String returnValue = "";
        HttpPost post = new HttpPost(url);

        List<NameValuePair> urlParameters = new ArrayList<>();
        data.forEach((key, value) -> urlParameters.add(new BasicNameValuePair(key, value)));
        post.setEntity(new UrlEncodedFormEntity(urlParameters));

        try (CloseableHttpClient httpClient = HttpClients.createDefault();
             CloseableHttpResponse response = httpClient.execute(post)) {
            returnValue = EntityUtils.toString(response.getEntity());

        } catch (Exception e) {
            System.out.println("Sorry, the HTTP request failed.\n");
            e.printStackTrace();
        } finally {
            close();
            return returnValue;
        }

    }

}
