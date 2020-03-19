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
    public static final String addShop = "https://us-central1-engineeringlabproject.cloudfunctions.net/addShopTest";
    public static final String deleteShop = "https://us-central1-engineeringlabproject.cloudfunctions.net/deleteShopTest";
    public static final String changeShopName = "https://us-central1-engineeringlabproject.cloudfunctions.net/changeShopNameTest";
    public static final String addTagToShop = "https://us-central1-engineeringlabproject.cloudfunctions.net/addTagToShopTest";
    public static final String removeTagFromShop = "https://us-central1-engineeringlabproject.cloudfunctions.net/removeTagFromShopTest";
    public static final String addItemToShop = "https://us-central1-engineeringlabproject.cloudfunctions.net/addItemToShopTest";
    public static final String removeItemFromShop = "https://us-central1-engineeringlabproject.cloudfunctions.net/removeItemFromShopTest";
    public static final String purchaseItemFromShop = "https://us-central1-engineeringlabproject.cloudfunctions.net/purchaseItemFromShopTest";
    public static final String createMerchant = "https://us-central1-engineeringlabproject.cloudfunctions.net/createMerchantTest";
    public static final String createCustomer = "https://us-central1-engineeringlabproject.cloudfunctions.net/createCustomerTest";

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
