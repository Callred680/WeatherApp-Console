import java.util.ArrayList;
import java.util.Scanner;

public class Weather{
    // Each enum type corresponds to one specifc API call that can be made
    enum SearchMethod{
        CURRENT("/current.xml?key="),    // For obtaining realtime weather
        FORECAST("/forecast.xml?key="),   // For obtaining hourly weather for next 3 days (dependent on price level, up to 14 days)
        FUTURE("/future.xml?key="),     // For obtaining hourly weather for day atleast 14 days in the future   (unavailable with current price plan)
        HISTORY("/history.xml?key="),    // For obtaining weather from the past 7 days (at most)
        MARINE("/marine.xml?key="),     // For obtaining realtime marine weather
        ASTRONOMY("/astronomy.xml?key="),  // For obtaining realtime astornomy info
        IP("/ip.xml?key="),         // For obtaining up to date information based on IP address
        TIMEZONE("/timezone.xml?key="),   // For obtaining time zone information for given input value
        SEARCH("/search.xml?key=");     // For obtaining weather with incomplete/partial location input

        // Constructor for creating enum with values (specific api method call)
        private String method;  // Will be set to api method call for completing URI builder
        private SearchMethod(String method){
            this.method = method;
        }
        // Allowing main program to retrieve specifc method name
        public String getMethod(){
            return this.method;
        }
    }

    /*
     *  THIS PROGRAM ASSUMES ONLY VALID INPUT IS GIVEN
     *      - ADDITIONAL CODE FOR INPUT VALIDATION TBD 
     */
    static Scanner input = new Scanner(System.in);
    public static void main(String[] args){
        WeatherAPI GetWeather = new WeatherAPI("en", "no", "F", "MPH");   // Create WeatherAPI object with english as default language

        DisplayMenu();
        String selection = input.nextLine();
        selection = selection.toUpperCase();    // Case Sensitive so eliminate potential input error for selection choice

        while(!selection.equals("-1") && !selection.equals("EXIT")){
            // Send API request with inputted parameters from user and selection choice for correct API call method
            if(!selection.equals("SETTINGS")){    // Represents settings
                GetWeather.GetData(SearchMethod.valueOf(selection), getParameters(SearchMethod.CURRENT));
                System.out.println();
            }else{
                Settings(GetWeather);
                System.out.println("==========================================");
            }
            DisplayMenu();
            selection = input.nextLine().toUpperCase();
        }

        input.close();
    }

    private static void DisplayMenu(){
        System.out.println("Welcome! Please select one of the options below (Type out selection EX: current)\n");
        SearchMethod[] methods = SearchMethod.values();
        for(int i = 0; i < methods.length; i++){
            System.out.println((i+1) + ". " + methods[i]);  // Displays all possible methods (EX: 1. CURRENT)
        }
        System.out.print(0 + ". SETTINGS \n===> ");
    }

    private static void Settings(WeatherAPI WeatherAPISettings){
        System.out.println("Select which option to change (Enter number)\n==========================\n");
        System.out.println("1. Language\n2. AQI\n3. Temperature units\n4. Speed Units\n5. Exit\n0. Back");

        switch(input.nextLine()){
            case "1":
                System.out.print("Enter desired language (language code) => "); // Lower case for api call parameter use
                WeatherAPISettings.setLanguage(input.nextLine());
                break;
            case "2":
                System.out.print("AQI included, yes or no? => ");
                WeatherAPISettings.setLanguage(input.nextLine().toLowerCase()); // Lower case for api call parameter use
                break;
            case "3":
                System.out.print("Fahrenheit(F) or Celsius(C) => ");
                WeatherAPISettings.setLanguage(input.nextLine().toUpperCase()); // Upper case for when being displayed
                break;
            case "4":
                System.out.print("Miles Per Hour(MPH) or Kilometers Per Hour(KPH) => ");
                WeatherAPISettings.setLanguage(input.nextLine().toUpperCase()); // Upper case for when being displayed
                break;
            case "5":
                input.close();
                System.exit(0); // Exit choice was selected so end program
                break;
            case "0":
                // For "going back" to menu
                break;
        }
    }


    private static ArrayList<String> getParameters(SearchMethod search){
        ArrayList<String> params = new ArrayList<String>();
        
        // Obtain location to check weather (appended with correct formatting for making API call)
        System.out.print("Please enter location: ");
        params.add("&q=" + input.nextLine());

        // If API call is one of these methods, additional paramter requests are needed from user
        switch(search){
            case FORECAST:
                System.out.print("Please enter number of days for forecast(1 to 14): ");
                params.add("&days=" + input.nextLine());
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
        return params;
    }
}