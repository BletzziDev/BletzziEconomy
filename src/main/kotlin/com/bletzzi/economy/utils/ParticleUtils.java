package com.bletzzi.economy.utils;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ParticleUtils {
    public static void create(final Player player, final EnumParticle particle, final Location location, final int count, final float offsetX, final float offsetY, final float offsetZ, final float speed) {
        final PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                particle, true,
                (float) location.getX(), (float) location.getY(), (float) location.getZ(),
                offsetX, offsetY, offsetZ, speed, count
        );

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }
}