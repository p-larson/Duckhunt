package com.wowserman.tools;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class TitlesReflection {
	public static void sendPacket(Player player, Object packet) {
        try {
                Object handle = player.getClass().getMethod("getHandle").invoke(player);
                Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
                playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        }
       
        catch (Exception e) {
                e.printStackTrace();
        }
	}

	public static Class<?> getNMSClass(String name) {
        // org.bukkit.craftbukkit.v1_8_R3...
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
                return Class.forName("net.minecraft.server." + version + "." + name);
        }
       
        catch (ClassNotFoundException e) {
        	return null;
        }
	}
	
	public static void sendTitle(Player player, String message) {
		message = "{\"text\":\"" + message + "\",\"color\":\"gold\"}";
		try {
            Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
            Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, message);
           
            Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(enumTitle, chat, 20, 40, 20);
           
            sendPacket(player, packet);
		} catch (Exception exception) {

		}
	}
	
	
	public static void sendSubTitle(Player player, String message) {
		message = "{\"text\":\"" + message + "\",\"color\":\"gold\"}";
		
		try {
			Object enumSubTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
			Object chat = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, message);
			
			Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle").getConstructor(getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], getNMSClass("IChatBaseComponent"), int.class, int.class, int.class);
            Object packet = titleConstructor.newInstance(enumSubTitle, chat, 20, 40, 20);
            
            sendPacket(player, packet);
		}
		catch (Exception exception) {

		}
		
	}
	
	public static void clearTitle(Player player) {
		TitlesReflection.sendTitle(player, "");
		TitlesReflection.sendSubTitle(player, "");
	}
	
}
