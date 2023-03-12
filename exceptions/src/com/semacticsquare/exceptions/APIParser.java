package com.semacticsquare.exceptions;

class APIParser {
    static void parseSendResponseCode(String response, String data, String partner) throws APIFormatChangeException{
        int responseCode = -1;
        try {
            String startTag = "<code>";
            String endTag = "</code>";

            if (response.contains(startTag)) {
                int beginCodeIndex = response.indexOf(startTag) + startTag.length();
                int endCodeIndex = response.lastIndexOf(endTag);
                String code = response.substring(beginCodeIndex, endCodeIndex);

                System.out.println("Code: " + code);
                responseCode = Integer.parseInt(code);

            }

        } catch (NumberFormatException e) {
            throw new APIFormatChangeException(response,data, partner, e);
        }
    }

}
