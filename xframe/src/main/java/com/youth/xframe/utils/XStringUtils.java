package com.youth.xframe.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


    /*** 半角转换为全角
     *textView 排版
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号
     *
     * @param str
     * @return
     */
    public static String stringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }
}
