package com.bletzzi.economy.utils;

public class FormattedTime {
    public static String formatTime(double seconds) {
        if (seconds < 0) {
            return "error";
        }
        int days = (int) (seconds / (3600 * 24));
        int remainingSeconds = (int) (seconds % (3600 * 24));

        int hours = remainingSeconds / 3600;
        int minutes = (remainingSeconds % 3600) / 60;
        remainingSeconds = remainingSeconds % 60;

        return String.format("%dd %dh, %dm, %ds", days, hours, minutes, remainingSeconds);
    }
    public static int getHours(double seconds) {
        if (seconds < 0) {
            return -1;
        }
        return (int) seconds / 3600;
    }
    public static int getMinutes(double seconds) {
        if (seconds < 0) {
            return -1;
        }
        return (int) (seconds % 3600) / 60;
    }
    public static String formatTimeShort(double seconds) {
        if (seconds < 0) {
            return "error";
        }

        if (seconds < 60) {
            return String.format("%.0f segundos", seconds);
        } else if (seconds < 3600) {
            return String.format("%.0f minutos", seconds / 60);
        } else if (seconds < 86400) {
            double hours = (seconds / 3600);
            double minutes = (seconds % 3600) / 60;
            return minutes < 1 ? String.format("%.0f horas", seconds / 3600) : String.format("%.0f horas e %.0f minutos", Math.floor(hours), minutes);
        } else if (seconds < 2592000) { // 30 dias = 2592000 segundos
            return String.format("%.0f dias", seconds / 86400);
        } else if (seconds < 31536000) { // 365 dias = 31536000 segundos
            return String.format("%.0f meses", seconds / 2592000);
        } else {
            return String.format("%.0f anos", seconds / 31536000);
        }
    }
}