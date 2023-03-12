package com.semanticsquare.thrillio.constants;

public enum UserType {

    USER("user"),
    EDITOR("editor"),
    CHIEF_EDITOR("chief editor");

    private UserType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }
}
