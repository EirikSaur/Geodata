package com.saur.currencyexchangeservice.controller;

import com.saur.currencyexchangeservice.SupportingMethods;
import com.saur.currencyexchangeservice.pojo.ErrorObject;
import com.saur.currencyexchangeservice.pojo.ExchangeObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.Optional;

@RestController
public class CurrencyExchangeController {

    public static final String API_V1_PATH = "/api/v1";
    public static final String DEFAULT_WELCOME_MESSAGE = "Welcome to the exchange service. ";
    public static final String INSTRUCTIONS_MESSAGE = "To use the API go to "
            + API_V1_PATH + "/exchange and supply input parameters for 'base', 'result', 'amount' and 'date'(optional).";

    @RequestMapping("/")
    public String defaultResponse() {
        return DEFAULT_WELCOME_MESSAGE + INSTRUCTIONS_MESSAGE;
    }


    @RequestMapping(API_V1_PATH+"/exchange/")
    public Object exchangeCurrencies(@RequestParam(name = "base") String base,
                                         @RequestParam(name = "result") String result,
                                         @RequestParam(name = "amount") String amount,
                                         @RequestParam(name = "date", required = false) String date) {
        try{
            String validateMessage = SupportingMethods.validateInput(Optional.ofNullable(date), base, result, amount);
            if(!validateMessage.isEmpty()) {
                return new ErrorObject(validateMessage);
            }

            Double exchangeModifier = SupportingMethods.queryConnectionForExchangeModifier(SupportingMethods.setupConnection(Optional.ofNullable(date), base, result), result);
            Double resultAmount = SupportingMethods.calculateValue(Double.valueOf(amount), exchangeModifier);

            return new ExchangeObject(base,
                    result,
                    Double.valueOf(amount),
                    resultAmount,
                    SupportingMethods.getTodaysDateOrInputDateIfPresent(Optional.ofNullable(date))
            );
        } catch (IOException | ParseException e) {
            return new ErrorObject(e.getMessage());
        }
    }
}
