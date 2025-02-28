package com.bletzzi.economy.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerializer {
    public static String serialize(Location location) {
        return String.format("%s,%s,%s,%s,%s,%s",
                location.getWorld().getName(),
                String.valueOf(location.getX()),
                String.valueOf(location.getY()),
                String.valueOf(location.getZ()),
                String.valueOf(location.getYaw()),
                String.valueOf(location.getPitch()));
    }
    public static Location unserialize(String string) {
        if(string == null || string.isEmpty() || string.equalsIgnoreCase("none")) return null;
        String[] parts = string.split(",");
        return new Location(
                Bukkit.getWorld(parts[0]),
                Double.parseDouble(parts[1]),
                Double.parseDouble(parts[2]),
                Double.parseDouble(parts[3]),
                Float.parseFloat(parts[4]),
                Float.parseFloat(parts[5])
        );
    }
}