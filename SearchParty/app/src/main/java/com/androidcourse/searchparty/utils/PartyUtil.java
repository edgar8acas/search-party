package com.androidcourse.searchparty.utils;

import com.androidcourse.searchparty.data.Party;

import java.util.Date;
import java.util.Random;

public class PartyUtil {

    public static final String[] names = {
            "Dog",
            "Cat",
            "Purse",
            "Boy",
            "Treasure",
            "Girl",
            "Clue",
            "Place"
    };
    public static final String[] invitationCodes = {
            "AB2N",
            "JN1K",
            "4NQM",
            "LA3Z",
            "Q9ZP",
            "LLQ0",
            "Q01Q"
    };

    public static Party getRandom() {
        Random random = new Random();
        return new Party(
                getRandomString(names, random),
                getRandomString(invitationCodes, random),
                new Date()
        );
    }

    private static String getRandomString(String[] array, Random random) {
        int ind = random.nextInt(array.length);
        return array[ind];
    }
}
