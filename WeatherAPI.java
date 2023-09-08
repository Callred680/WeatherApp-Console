import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class WeatherAPI {

    private static final String Key = getKey(); // Establish API key for user
    private static final String BaseURL = "http://api.weatherapi.com/v1";   // Base URI for being able to select desired information when requested

    public HttpRequest request = null;
    public HttpResponse<String> response = null;

    private String Language, AQI, TempUnit, SpeedUnit; 
    // intializes class with default values for getting weather
    WeatherAPI(String Language, String AQI, String TempUnit, String SpeedUnit){
        this.Language = Language;
        this.AQI = AQI;
        this.TempUnit = TempUnit;
        this.SpeedUnit = SpeedUnit;
        
    };

    // Fetches data for current weather data
    public void GetData(Weather.SearchMethod search, ArrayList<String> params){
        try{
            String addParams = "";
            for(String p : params){
                addParams += p;
            }
            URI uri = new URI(BaseURL + search.getMethod() + Key + addParams + "&lang=" + Language + "&aqi=" + AQI);
            request = HttpRequest.newBuilder()
                .uri(uri)  // Sets the HttpRequest's request URI (Uniform Resource Identifier, char sequence to identify which logical or physical resource)
                .header("Authorization", Key)   // .GET() is the default 
                .build();
            response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if(response.statusCode() != 200){
                System.out.println("ERROR! Request time out...");
            }else{
                displayData(readData( response.body() ), search);   // Reads xml string into document which is then displayed
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    // Function for processing data received from request call (XML file)
    private Document readData(String XML) throws SAXException, IOException, ParserConfigurationException{
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(XML)));

        // Puts all text nodes underneath a node (neither adjacent Text nodes nor empty ones)
        document.getDocumentElement().normalize();

        return document;
    }

    // Function for displaying information for the inputted location as well as respective information for inputted search method
    private void displayData(Document doc, Weather.SearchMethod search){
        Element root = doc.getDocumentElement();
        System.out.println(root.getNodeName());

        // Showing weather for one location only so do not need for loop
        NodeList list = doc.getElementsByTagName("location");
        Element node = (Element) list.item(0);  // Receives first item in list (only one item since its location)
        System.out.println("==========================================");
        System.out.printf("City : %s%n", node.getElementsByTagName("name").item(0).getTextContent());
        System.out.printf("Region : %s%n", node.getElementsByTagName("region").item(0).getTextContent());
        System.out.printf("Country : %s%n", node.getElementsByTagName("country").item(0).getTextContent());
        System.out.printf("Latitude : %s\t  Longitude : %s%n", node.getElementsByTagName("lat").item(0).getTextContent(), node.getElementsByTagName("lon").item(0).getTextContent());
        System.out.printf("Timezone : %s%n", node.getElementsByTagName("tz_id").item(0).getTextContent());
        System.out.printf("Local Time : %s%n", node.getElementsByTagName("localtime").item(0).getTextContent());

        switch(search){
            case CURRENT:
                list = doc.getElementsByTagName("current");
                node = (Element)list.item(0);
                System.out.println("==========================================");
                System.out.printf("Last Updated : %s%n", node.getElementsByTagName("last_updated").item(0).getTextContent());
                System.out.printf("Temperature : %s\u00B0%s%n", node.getElementsByTagName((TempUnit.equals("F") ? "temp_f" : "temp_c")).item(0).getTextContent(), TempUnit); // Embedded if statement for selecting which parameter to display (Celsius or Fahrenheit)
                System.out.printf("Feels Like : %s\u00B0%s%n", node.getElementsByTagName((TempUnit.equals("F") ? "feelslike_f" : "feelslike_c")).item(0).getTextContent(), TempUnit); // Embedded if statement for selecting which parameter to display (Celsius or Fahrenheit)
                System.out.printf("UV Index : %s%n", node.getElementsByTagName("uv").item(0).getTextContent());
                System.out.printf("Wind Speed : %s%n", node.getElementsByTagName((SpeedUnit.equals("MPH") ? "wind_mph" : "feelslike_c")).item(0).getTextContent()+" "+SpeedUnit); // Embedded if statement for selecting which parameter to display (MPH or KPH)
                System.out.printf("Humidity : %s%n", node.getElementsByTagName("humidity").item(0).getTextContent());
                break;
            case FORECAST:
                break;
            case FUTURE:
                break;
            case HISTORY:
                break;
            case MARINE:
                break;
            case ASTRONOMY:
                break;
            case IP:
                break;
            case TIMEZONE:
                break;
            case SEARCH:
                break;
            default:
        }
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

    public void setLanguage(String lang){
        this.Language = lang;
    }
    public void setAQI(String AQI){
        this.AQI = AQI;
    }
    public void setTempUnit(String Unit){
        this.TempUnit = Unit;
    }
    public void setSpeedUnit(String Unit){
        this.SpeedUnit = Unit;
    }
}
