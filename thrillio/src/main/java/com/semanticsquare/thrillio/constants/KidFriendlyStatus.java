package com.semanticsquare.thrillio.constants;

public enum KidFriendlyStatus {

    UNKNOWN("unknown"),
    APPROVED("approved"),
    REJECTED("rejected");

    private KidFriendlyStatus(String state) {
        this.state = state;
    }

    private String state;

    public String getState() {
        return state;
    }
}
