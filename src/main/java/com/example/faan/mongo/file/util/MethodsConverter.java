package com.example.faan.mongo.file.util;

import org.apache.commons.lang3.StringUtils;

public class MethodsConverter {

    public static String normalizeFileName(String fileName) {
        return StringUtils.stripAccents(fileName)
                .replaceAll("[^a-zA-Z0-9._-]", "");
    }
}
