package mainApp;

import org.apache.commons.lang.math.NumberUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static mainApp.Main.AMOUNT;
import static mainApp.Main.BASE_CURRENCY;
import static mainApp.Main.DATE;
import static mainApp.Main.EXCHANGE_CURRENCY;
import static mainApp.Main.TO_FEW_INPUTS_MESSAGE;

public class SupportingMethods {

    public static int STANDARD_CURRENCY_LENGTH = 3;
    public static String VALID_CURRENCIES_RESOURCE = "validCurrencies.txt";
    public static String GET_REQUEST = "GET";
    public static String VALID_BASE_CURRENCY  = "EUR";
    public static String INVALID_BASE_INPUT_MESSAGE = "The base input has to be EUR.";
    public static String INVALID_EXCHANGE_CURRENCY = "%s is not a valid currency code.";
    public static String INVALID_AMOUNT = "%s is not a valid number.";
    public static String INVALID_DATE_FORMAT = "%s is an invalid date format. Date should be on format YYYY-MM-DD";

    private static String API_KEY = "56e7da9b75d87c324ecb164f376d2a21";
    private static String URL_TEMPLATE = "http://data.fixer.io/api/%s?access_key=%s&base=%s&symbols=%s";
    private static HttpURLConnection connection;


    public static double calculateValue(Double amount, Double exchangeModifier) {
        return amount*exchangeModifier;
    }

    public static Map<String, String> splitInputIntoParameterMap(String input) {
        String[] inputList = input.split(",");
        Map<String, String> map = new HashMap<>();
        map.put(BASE_CURRENCY, inputList[0]);
        map.put(EXCHANGE_CURRENCY, inputList[1]);
        map.put(AMOUNT, inputList[2]);
        map.put(DATE, inputList.length == 4 ? inputList[3] : "latest");
        return map;
    }

    public static boolean validateInput(String input) throws IOException {
        String[] inputList = input.split(",");
        if(inputList.length < 3) {
            System.out.println(TO_FEW_INPUTS_MESSAGE);
            return false;
        }
        if(!inputList[0].equals(VALID_BASE_CURRENCY)) {
            System.out.println(INVALID_BASE_INPUT_MESSAGE);
            return false;
        }
        if(!isAValidCurrency(inputList[1])) {
            System.out.println(String.format(INVALID_EXCHANGE_CURRENCY, inputList[1]));
            return false;
        }
        if(!validAmount(inputList[2])) {
            System.out.println(String.format(INVALID_AMOUNT, inputList[2]));
            return false;
        }
        if(inputList.length == 3) {
            return true;
        }
        if(!validDateFormat(inputList[3])) {
            System.out.println(String.format(INVALID_DATE_FORMAT, inputList[3]));
            return false;
        }
        return true;
    }


    public static HttpURLConnection setupConnection(String exchangeRateDate,String base, String symbols) throws IOException {
        try {
            System.out.println(String.format(URL_TEMPLATE, exchangeRateDate, API_KEY, base, symbols));
            URL url = new URL(String.format(URL_TEMPLATE, exchangeRateDate, API_KEY, base, symbols));
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(GET_REQUEST);
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);
            return connection;
        } catch(MalformedURLException e) {
            throw new MalformedURLException(e.getMessage());
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }

    }

    private static String readFromResources(String resource) throws IOException {
        StringBuffer resourceString = new StringBuffer();
        String resourceLine;
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
        InputStream is = classloader.getResourceAsStream(resource);
        InputStreamReader isReader = new InputStreamReader(is);
        BufferedReader reader = new BufferedReader(isReader);
        while((resourceLine = reader.readLine()) != null) {
            resourceString.append(resourceLine + ", ");
        }
        return resourceString.toString();
    }

    private static boolean isAValidCurrency(String currency) throws IOException {
        if(currency.length() != STANDARD_CURRENCY_LENGTH) { return false; }
        String validCurrencies = readFromResources(VALID_CURRENCIES_RESOURCE);
        return validCurrencies.contains(currency);
    }

    private static boolean validDateFormat(String input) {
        return Pattern.matches("^\\d{4}\\-(0[1-9]|1[012])\\-(0[1-9]|[12][0-9]|3[01])$", input);
    }

    private static boolean validAmount(String input) {
        return NumberUtils.isNumber(input);
    }

}
