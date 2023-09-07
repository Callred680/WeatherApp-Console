import java.util.Scanner;

public class Weather{

    public static void main(String[] args){
        Scanner input = new Scanner(System.in);

        System.out.print("Enter zipcode: ");
        String zipcode = input.nextLine();
        System.out.print("Enter language: ");
        String lang = input.nextLine();

        WeatherAPI GetWeather = new WeatherAPI(zipcode, lang);
        GetWeather.GetCurrentData();
        input.close();

    }
}