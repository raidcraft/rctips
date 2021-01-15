package de.raidcraft.rctips.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UrlParser {

    private String url = null;
    private String text;
    private int urlStart = 0;

    private static final Pattern urlPattern = Pattern.compile(
            "(?:^|[\\W])((ht|f)tp(s?):\\/\\/|www\\.)"
                    + "(([\\w\\-]+\\.){1,}?([\\w\\-.~]+\\/?)*"
                    + "[\\p{Alnum}.,%_=?&#\\-+()\\[\\]\\*$~@!:/{};']*)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public UrlParser(String text) {
        this.text = text;

        // Try to find URL
        Matcher matcher = urlPattern.matcher(text);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            urlStart = matchStart;
            url = text.substring(matchStart, matchEnd);
        }
    }

    public boolean containsUrl() {

        return url != null;
    }


    public String getUrl() {

        return url;
    }

    public String getPreUrl() {

        if(!containsUrl()) return null;

        return text.substring(0, urlStart);
    }

    public String getPostUrl() {

        if(!containsUrl()) return null;

        return text.substring(urlStart + url.length());
    }
}
