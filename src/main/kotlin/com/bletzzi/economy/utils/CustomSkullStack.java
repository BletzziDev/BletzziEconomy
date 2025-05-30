package com.bletzzi.economy.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import org.bukkit.inventory.*;

import org.bukkit.*;
import java.util.*;
import org.bukkit.inventory.meta.*;
import java.lang.reflect.*;
import org.apache.commons.codec.binary.Base64;

public class CustomSkullStack
{
    public static ItemStack getSkull(String url) {
        url = "http://textures.minecraft.net/texture/"+url;
        final ItemStack skull = new ItemStack(Material.SKULL_ITEM, 1, (short)3);
        if (url == null || url.isEmpty()) {
            return skull;
        }
        final SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
        final GameProfile profile = new GameProfile(UUID.randomUUID(), (String)null);
        final byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        }
        catch (NoSuchFieldException | SecurityException ex15) {
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        }
        catch (IllegalArgumentException | IllegalAccessException ex16) {
        }
        skull.setItemMeta((ItemMeta)skullMeta);
        return skull;
    }
}