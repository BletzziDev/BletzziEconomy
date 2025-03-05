package com.bletzzi.economy.utils;

import org.bukkit.configuration.ConfigurationSection;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class NumberFormat {
    private static List<String> suffixes;
    private static double minAmount;
    private static boolean use;

    public static void setup(ConfigurationSection section) {
        suffixes = section.getStringList("suffixes");
        minAmount = section.getDouble("minValue");
        use = section.getBoolean("use");
    }

    public static String format(double number) {
        if(!use || number <= minAmount) {
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(new Locale("pt", "BR"));
            DecimalFormat decimalFormat = new DecimalFormat("#,##0.00", symbols);
            return decimalFormat.format(number);
        }

        int suffixIndex = 0;
        while (number >= 1_000 && suffixIndex < suffixes.size() - 1) {
            number /= 1_000;
            suffixIndex++;
        }

        return formatNumber(number) + suffixes.get(suffixIndex);
    }

    private static String formatNumber(double number) {
        DecimalFormat decimalFormat = new DecimalFormat("0.#");
        return decimalFormat.format(number);
    }
}