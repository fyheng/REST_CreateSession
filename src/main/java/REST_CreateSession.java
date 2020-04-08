package main.java;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class REST_CreateSession {
    public static void main(String[] args) {
        //CONFIG PARAMETERS:
        //BEGIN------------CONFIG PARAMETERS BELOW TO YOUR ENVIRONMENT---------------------------------------
        String baseRestURL = "https://env-73627.customer.cloud.microstrategy.com/MicroStrategyLibrary"";
        String cubeName = "MyCube";
        String username = "steve";
        String password = "";
        String projectID = "B7CA92F04B9FAE8D941C3E9B7E0CD754";
        String updatePolicy = "add";
        //END------------CONFIG PARAMETERS BELOW TO YOUR ENVIRONMENT---------------------------------------

        //Create HTTPClient - Used to make Request to API
        HttpClient httpClient = null;
        CookieStore httpCookieStore = new BasicCookieStore();
        HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
        httpClient = builder.build();

        //Create Session using /api/auth/login
        String authToken = createAuthToken(baseRestURL, httpClient, username, password);
        System.out.println("Auth Token: " + authToken);

        if(authToken == null){
            System.out.println("Error: Unable to generate authToken - check to see if server is running");
            return;
        }
    }

    //Creates an AuthToken
    public static String createAuthToken(String baseRestURL, HttpClient httpClient, String username, String password){
        String APIPath = "/api/auth/login";
        String completeRestURL = baseRestURL + APIPath;
        System.out.println("REST API URL: " + completeRestURL);

        // Define the server endpoint to send the HTTP request to
        URL serverUrl;
        try {
            serverUrl = new URL(completeRestURL);

            HttpPost httpRequest = new HttpPost(completeRestURL);
            httpRequest.setHeader("Content-Type", "application/json");
            httpRequest.setHeader("Accept", "application/json");
            StringEntity body =new StringEntity("{\"username\": \""+username+"\",\"password\": \""+password+"\",\"loginMode\": 1,\"applicationType\": 35}");
            httpRequest.setEntity(body);

            HttpResponse response = httpClient.execute(httpRequest);

            Header[] headers = (Header[]) response.getAllHeaders();
            for (int i = 0; i < headers.length; i++){
                Header header = headers[i];
                //System.out.println(header.getName() + " : " + header.getValue());
                if (header.getName().equalsIgnoreCase("X-MSTR-AuthToken")){
                    return header.getValue();
                }
            }

            return null;

        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
