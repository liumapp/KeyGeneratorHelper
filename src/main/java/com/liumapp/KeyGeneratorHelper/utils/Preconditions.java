package com.liumapp.KeyGeneratorHelper.utils;

/**
 * Created by liumapp on 10/12/17.
 * E-mail:liumapp.com@gmail.com
 * home-page:http://www.liumapp.com
 */
public class Preconditions {

    private Preconditions() {
        // prevent instance
    }

    public static void checkState(boolean expression, String message) {
        if (!expression) {
            throw new IllegalStateException(message);
        }
    }

    public static void checkArgument(boolean expression, String message) {
        if (!expression) {
            throw new IllegalArgumentException(message);
        }
    }

}
