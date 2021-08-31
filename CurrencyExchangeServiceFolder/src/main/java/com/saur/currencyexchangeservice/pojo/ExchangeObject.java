package com.saur.currencyexchangeservice.pojo;

import java.util.Date;

public class ExchangeObject {

    private String baseCurrency;
    private String resultCurrency;
    private Double baseAmount;
    private Double resultAmount;
    private Date date;

    public ExchangeObject(String baseCurrency, String resultCurrency, Double baseAmount, Double resultAmount, Date date) {
        this.baseCurrency = baseCurrency;
        this.resultCurrency = resultCurrency;
        this.baseAmount = baseAmount;
        this.resultAmount = resultAmount;
        this.date = date;
    }

    public String getBaseCurrency() {
        return baseCurrency;
    }

    public void setBaseCurrency(String baseCurrency) {
        this.baseCurrency = baseCurrency;
    }

    public String getResultCurrency() {
        return resultCurrency;
    }

    public void setResultCurrency(String resultCurrency) {
        this.resultCurrency = resultCurrency;
    }

    public Double getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(Double baseAmount) {
        this.baseAmount = baseAmount;
    }

    public Double getResultAmount() {
        return resultAmount;
    }

    public void setResultAmount(Double resultAmount) {
        this.resultAmount = resultAmount;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

}
