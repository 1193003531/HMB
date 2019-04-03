package com.youth.xframe.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 字符串工具类
 */
public class XStringUtils {
    /**
     * Return a String that only has two spaces.
     *
     * @return
     */
    public static String getTwoSpaces() {
        return "\u3000\u3000";
    }

    public static double f = 0;

    public static double m1(String numbaer) {
        try {
            f = Double.parseDouble(numbaer);
        } catch (Exception e) {
            f = 0;
        }

        BigDecimal bg = new BigDecimal(f);
        return bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * DecimalFormat转换最简便
     */
    public static String m2(String numbaer) {
        try {
            f = Double.parseDouble(numbaer);
        } catch (Exception e) {
            f = 0;
        }
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(f);
    }

    /**
     * String.format打印最简便
     */
    public static String m3(String numbaer) {
        try {
            f = Double.parseDouble(numbaer);
        } catch (Exception e) {
            f = 0;
        }
        return String.format("%.2f", f);
    }

    public static String m4(String numbaer) {
        try {
            f = Double.parseDouble(numbaer);
        } catch (Exception e) {
            f = 0;
        }
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumFractionDigits(2);
        return nf.format(f);
    }

}
