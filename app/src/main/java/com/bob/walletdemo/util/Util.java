package com.bob.walletdemo.util;

import java.math.BigInteger;

public class Util {

    public static String bigInteger2Hex(String bigIntegerString) {
        BigInteger bigInteger = new BigInteger(bigIntegerString, 10);
        return bigInteger.toString(16);
    }
}
