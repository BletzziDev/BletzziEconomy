package com.bletzzi.economy.utils;

import com.bletzzi.core.CorePlugin;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;

public class NumberFormat {
    private static final List<String> suffixes = CorePlugin.Companion.getInstance().getConfigFile().getStringList("format.suffixes");
    private static final double minAmount = CorePlugin.Companion.getInstance().getConfigFile().getDouble("format.minValue");
    private static final boolean use = CorePlugin.Companion.getInstance().getConfigFile().getBoolean("format.use");

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