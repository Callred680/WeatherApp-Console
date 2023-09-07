import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class WeatherAPI {

    private static final String Key = getKey(); // Establish API key for user
    private static final String URI = "http://api.weatherapi.com/v1";   // Base URI for being able to select desired information when requested
    private static final String currentMethod = "/current.xml?key=";

    private String zipcode = "&q=", lang;

    public HttpRequest request = null;
    public HttpResponse<String> response = null;

    // intializes class with default values for getting weather
    WeatherAPI(String zipcode, String lang){
        this.zipcode += zipcode;
        this.lang = lang;
    };

    // Fetches data for current weather
    public void GetCurrentData(){
        try {
            request = HttpRequest.newBuilder()
                .uri(new URI(URI+currentMethod+Key+zipcode+"&aqi=no"))  // Sets the HttpRequest's request URI (Uniform Resource Identifier, char sequence to identify which logical or physical resource)
                .header("Authorization", Key)   // .GET() is the default 
                .build();
            try {
                response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                if(response == null){
                    System.out.println("ERROR! Request time out...");
                }else{
                    displayData(response.body());                    
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Function for displaying each header returned in .json file depedent on request method called
    public void displayData(String data){
        System.out.println(data);
    }

    // Serves to read api key from .txt file to be used for authorization in api calls [rather than hard coding]
    private static String getKey(){
        try (BufferedReader read = new BufferedReader(new FileReader("apiKey.txt"))) {
            return read.readLine();
        } catch (IOException e) {
            System.out.println("Wrong file!");
            e.printStackTrace();
        }
        return "0";
    }
}
