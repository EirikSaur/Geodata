import mainApp.SupportingMethods;
import org.junit.Test;
import java.io.IOException;

import static mainApp.SupportingMethods.VALID_BASE_CURRENCY;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SupportingMethodsTest {

    public static final String NOT_VALID_CURRENCY = "AAA";
    public static final String VALID_EXCHANGE_CURRENCY_CODE = "NOK";
    public static final String VALID_DATE = "2020-05-15";
    public static final String NOT_VALID_DATE = "05-15-2020";
    public static final String VALID_AMOUNT = "5";
    public static final String NOT_VALID_AMOUNT = "notValidAmount";
    public static final String NOT_VALID_BASE_CURRENCY = "USD";

    @Test
    public void validateInputReturnsFalseIfInputIsNotInValidBaseCurrency() throws IOException {
        Boolean response = SupportingMethods.validateInput(NOT_VALID_BASE_CURRENCY+","+VALID_EXCHANGE_CURRENCY_CODE+","+VALID_AMOUNT+","+VALID_DATE);
        assertFalse(response);
    }

    @Test
    public void validateInputReturnsFalseIfInputIsNotInValidCurrencies() throws IOException {
        Boolean response = SupportingMethods.validateInput(VALID_BASE_CURRENCY+","+NOT_VALID_CURRENCY+","+VALID_AMOUNT+","+VALID_DATE);
        assertFalse(response);
    }

    @Test
    public void validateInputReturnsFalseIfAmountIsNotANumber() throws IOException {
        Boolean response = SupportingMethods.validateInput(VALID_BASE_CURRENCY+","+VALID_EXCHANGE_CURRENCY_CODE+","+NOT_VALID_AMOUNT+","+VALID_DATE);
        assertFalse(response);
    }

    @Test
    public void validateInputReturnsFalseIfDateIsNotOnTheCorrectFormat() throws IOException {
        Boolean response = SupportingMethods.validateInput(VALID_BASE_CURRENCY+","+VALID_EXCHANGE_CURRENCY_CODE+","+VALID_AMOUNT+","+NOT_VALID_DATE);
        assertFalse(response);
    }

    @Test
    public void validateInputReturnstrueIfAllInputIsCorrect() throws IOException {
        Boolean response = SupportingMethods.validateInput(VALID_BASE_CURRENCY+","+VALID_EXCHANGE_CURRENCY_CODE+","+VALID_AMOUNT+","+VALID_DATE);
        assertTrue(response);
    }

}
