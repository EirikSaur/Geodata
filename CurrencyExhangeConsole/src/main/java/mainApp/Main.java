package mainApp;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Locale;
import java.util.Map;

public class Main {

    public static String WELCOME_MESSAGE = "Welcome to the currency calculator!";
    public static String INSTRUCTIONS_MESSAGE = "Enter [BaseCurrency],[ExchangeCurrency],[Amount],[Date(Optional)]." + System.lineSeparator()
            + "End the program by entering 'Exit'.";
    public static String TO_FEW_INPUTS_MESSAGE = "You need to at least enter [BaseCurrency],[ExchangeCurrency],[Amount]";
    public static String EXCHANGE_CURRENCY = "ExchangeCurrency";
    public static String BASE_CURRENCY = "BaseCurrency";
    public static String AMOUNT = "Amount";
    public static String DATE = "Date";

    private static StringBuffer content = new StringBuffer();
    private static String line;


    public static void main(String args[]) throws IOException {
        {
            System.out.println(WELCOME_MESSAGE);
            while (true) {
                System.out.println(INSTRUCTIONS_MESSAGE);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(System.in));

                String input = reader.readLine().toUpperCase(Locale.ROOT);
                if (input.equals("EXIT")) { break; }
                if (!SupportingMethods.validateInput(input)) {
                    continue;
                }
                Map<String, String> inputMap = SupportingMethods.splitInputIntoParameterMap(input);
                HttpURLConnection connection = SupportingMethods.setupConnection(inputMap.get(DATE),inputMap.get(BASE_CURRENCY), inputMap.get(EXCHANGE_CURRENCY));
                InputStream response = connection.getInputStream();
                InputStreamReader responseReader = new InputStreamReader(response);
                BufferedReader contentReader = new BufferedReader(responseReader);
                while((line = contentReader.readLine()) != null) {
                    content.append(line);
                }

                JSONObject jsonObject = new JSONObject(content.toString());
                Double exchangeModifier = jsonObject.getJSONObject("rates").getDouble(inputMap.get(EXCHANGE_CURRENCY));
                System.out.println(SupportingMethods.calculateValue(Double.valueOf(inputMap.get(AMOUNT)),exchangeModifier));
                content = new StringBuffer();
            }

        }
    }
}
