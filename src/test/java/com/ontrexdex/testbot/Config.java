package com.ontrexdex.testbot;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {

    private static final Dotenv env = Dotenv.load();

    public static String get(String key){
        return env.get(key.toUpperCase());
    }
}
