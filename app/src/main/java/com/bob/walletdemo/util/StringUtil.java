package com.bob.walletdemo.util;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@SuppressWarnings("unused")
public class StringUtil {

    /**
     * 转换成符合 decimal 的数值
     */

    public static String toDecimal(int decimal, BigInteger integer) {
//		String substring = str.substring(str.length() - decimal);
        StringBuilder sbf = new StringBuilder("1");
        for (int i = 0; i < decimal; i++) {
            sbf.append("0");
        }
        return new BigDecimal(integer).divide(new BigDecimal(sbf.toString()), 18, RoundingMode.DOWN).toPlainString();
    }

    public static String getAddressFromWalletJsonFileName(String s) {
        if (TextUtils.isEmpty(s)) return "";

        int begin = s.lastIndexOf("--") + 2;
        int end = s.lastIndexOf(".");
        return s.substring(begin, end);
    }


}
