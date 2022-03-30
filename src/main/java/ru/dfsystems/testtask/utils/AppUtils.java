package ru.dfsystems.testtask.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.util.StringUtils;

import java.util.Optional;

@UtilityClass
public class AppUtils {
    public static String toUnicode(String queryString) {
        return StringEscapeUtils.escapeJava(queryString).replaceAll("\\\\u", "u");
    }

    public static Optional<String> fromUnicode(String unicodeString) {
            return Optional.ofNullable(unicodeString)
                    .map(it->StringEscapeUtils.unescapeJava(unicodeString.replaceAll("u", "\\\\u")));
    }
}
