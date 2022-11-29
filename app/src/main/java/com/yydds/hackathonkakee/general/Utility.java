package com.yydds.hackathonkakee.general;

public class Utility {
    public static String generateID() {
        StringBuilder randAlp = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            randAlp.append((char)('a' + (int)(Math.random() * 26)));
        }
        randAlp.replace(0, 0, String.valueOf(System.currentTimeMillis()));
        return randAlp.toString();
    }
}
