package com.semacticsquare.exceptions;

public class APIFormatChangeException extends Exception {
    private String response;
    private String elementName;
    private String partner;

    public APIFormatChangeException(String response, String elementName, String partner, Throwable cause) {
        super("Response: " + response + ", Element: " + elementName + ", Partner: " + partner, cause);
        this.response = response;
        this.elementName = elementName;
        this.partner = partner;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getElementName() {
        return elementName;
    }

    public void setElementName(String elementName) {
        this.elementName = elementName;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }
}
