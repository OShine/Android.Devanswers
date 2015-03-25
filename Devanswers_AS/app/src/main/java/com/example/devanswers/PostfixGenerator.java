package com.example.devanswers;

import java.util.Random;

/**
 * Created by O'Shine on 26.03.2015.
 */
public class PostfixGenerator {

    private static final char[] _symbols =
            {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z',
                    'A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z',
                            '1','2','3','4','5','6','7','8','9','0'};

    public static String Generete() {

        String result = "";
        Random random = new Random();

        for(int i = 0; i < 2; i++) {
            int index = random.nextInt(_symbols.length);
            result += _symbols[index];
        }

        return result;
    }

}
