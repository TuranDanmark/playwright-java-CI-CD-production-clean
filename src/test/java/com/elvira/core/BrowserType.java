package com.elvira.core;

public enum BrowserType {
    CHROMIUM,
    FIREFOX,
    WEBKIT;

    public static BrowserType from(String value) {
        return BrowserType.valueOf(value.toUpperCase());
    }
}