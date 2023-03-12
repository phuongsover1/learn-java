package com.semanticsquare.thrillio.entities;

import com.semanticsquare.thrillio.partners.Shareable;

public class WebLink extends Bookmark implements Shareable {
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

    @Override
    public boolean isKidFriendlyEligible() {
        if (url.contains("porn") || getTitle().contains("porn")
                || (host.contains("adult"))) {
            return false;
        }
        return true;
    }

    @Override
    public String getItemData() {
        StringBuilder builder = new StringBuilder();
        builder.append("<item>");
            builder.append("<WebLink>").append("WebLink").append("</WebLink>");
            builder.append("<title>").append(getTitle()).append("</title>");
            builder.append("<url>").append(url).append("</url>");
            builder.append("<host>").append(host).append("</host>");

        builder.append("</item>");
        return builder.toString();
    }
}
