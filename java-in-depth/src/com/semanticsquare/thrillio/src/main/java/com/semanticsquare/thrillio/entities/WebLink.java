package com.semanticsquare.thrillio.entities;

public class WebLink extends Bookmark{
    private String url;
    private String host;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("WebLink{");
        sb.append("url='").append(url).append('\'');
        sb.append(", host='").append(host).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }
}
