package de.raidcraft.rctips.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandParser {

    private String command = null;
    private String text;
    private int commandStart = 0;

    private static final Pattern urlPattern = Pattern.compile(
            "\\s(/[a-zA-Z]+)",
            Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);

    public CommandParser(String text) {
        this.text = text;

        // Try to find URL
        Matcher matcher = urlPattern.matcher(text);
        while (matcher.find()) {
            int matchStart = matcher.start(1);
            int matchEnd = matcher.end();
            commandStart = matchStart;
            command = text.substring(matchStart, matchEnd);
        }
    }

    public boolean containsCommand() {

        return command != null;
    }


    public String getCommand() {

        return command;
    }

    public String getPreCommand() {

        if(!containsCommand()) return null;

        return text.substring(0, commandStart);
    }

    public String getPostCommand() {

        if(!containsCommand()) return null;

        return text.substring(commandStart + command.length());
    }
}
