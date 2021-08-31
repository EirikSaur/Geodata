package com.saur.currencyexchangeservice;

import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;

public class SupportingMethods {

    public static int STANDARD_CURRENCY_LENGTH = 3;
    public static int BASE_CURRENCY_INDEX = 0;
    public static int RESULT_CURRENCY_INDEX = 1;
    public static int BASE_AMOUNT_INDEX = 2;
    public static String VALID_CURRENCIES_RESOURCE = "validCurrencies.txt";
    public static String GET_REQUEST = "GET";
    public static String VALID_BASE_CURRENCY  = "EUR";
    public static String INVALID_BASE_INPUT_MESSAGE = "The base input has to be EUR.";
    public static String INVALID_EXCHANGE_CURRENCY = "%s is not a valid currency code.";
    public static String INVALID_AMOUNT = "%s is not a valid number.";
    public static String INVALID_DATE_FORMAT = "%s is an invalid date format. Date should be on format YYYY-MM-DD";
    public static String DATE_IS_IN_THE_FUTURE = "It is not possible to use dates that are in the future.";

    private static String API_KEY = "56e7da9b75d87c324ecb164f376d2a21";
    private static String URL_TEMPLATE = "http://data.fixer.io/api/%s?access_key=%s&base=%s&symbols=%s";
    private static HttpURLConnection connection;


    public static double calculateValue(Double amount, Double exchangeModifier) {
        return amount*exchangeModifier;
    }

    public static String validateInput(Optional<String> date, String... input) throws IOException, ParseException {
        if(!input[BASE_CURRENCY_INDEX].equals(VALID_BASE_CURRENCY)) {
            return INVALID_BASE_INPUT_MESSAGE;
        }
        if(!isAValidCurrency(input[RESULT_CURRENCY_INDEX])) {
            return String.format(INVALID_EXCHANGE_CURRENCY, input[RESULT_CURRENCY_INDEX]);
        }
        if(!validAmount(input[BASE_AMOUNT_INDEX])) {
            return String.format(INVALID_AMOUNT, input[BASE_AMOUNT_INDEX]);
        }
        if(!date.isPresent()) {
            return "";
        }
        if(!validDateFormat(date.get())) {
            return String.format(INVALID_DATE_FORMAT, date.get());
        }
        if(dateIsInTheFuture(date.get())) {
            return DATE_IS_IN_THE_FUTURE;
        }
        return "";
    }

    public static HttpURLConnection setupConnection(Optional<String> exchangeRateDate,String base, String result) throws IOException {
        try {
            URL url = new URL(String.format(URL_TEMPLATE, exchangeRateDate.orElse("latest"), API_KEY, base, result));
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

    public static Double queryConnectionForExchangeModifier(HttpURLConnection connection, String result) throws IOException {
        String line;
        StringBuffer content = new StringBuffer();
        InputStream response = connection.getInputStream();
        InputStreamReader responseReader = new InputStreamReader(response);
        BufferedReader contentReader = new BufferedReader(responseReader);
        while((line = contentReader.readLine()) != null) {
            content.append(line);
        }
        JSONObject jsonObject = new JSONObject(content.toString());
        return jsonObject.getJSONObject("rates").getDouble(result);
    }

    public static Date getTodaysDateOrInputDateIfPresent(Optional<String> date) throws ParseException {
        Date dateObject;
        if(date.isPresent()) {
            dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(date.get());
        } else {
            dateObject = new Date();
        }
        return dateObject;
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

    private static boolean dateIsInTheFuture(String input) throws ParseException {
        Date dateObject = new SimpleDateFormat("yyyy-MM-dd").parse(input);
        Date today = new Date();
        return dateObject.after(today) ;
    }

    private static boolean validAmount(String input) {
        return NumberUtils.isNumber(input);
    }

}
