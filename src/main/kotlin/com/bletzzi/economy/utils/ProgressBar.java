package com.bletzzi.economy.utils;

public class ProgressBar {
    public static String getBar(final double percentage, final String activeSymbol, final String inactiveSymbol, final String activeColor, final String inactiveColor, final int maxChars) {
        String bar = activeColor;

        double activeSymbolsAmount = 0;
        final double percentagePerSymbol = 100.0 / maxChars;
        for(int i = 1; i < maxChars + 1; i++) {
            if(percentage < percentagePerSymbol * i) break;
            bar += activeSymbol;
            activeSymbolsAmount++;
        }

        bar += inactiveColor;

        for(int i = 0; i < maxChars - activeSymbolsAmount; i++) {
            bar += inactiveSymbol;
        }

        return bar;
    }
}