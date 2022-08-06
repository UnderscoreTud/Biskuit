package me.tud.biskuit.util;

import me.tud.biskuit.Biskuit;
import org.bukkit.ChatColor;

public class Util {

    public static void log(Object object) {
        Biskuit.getInstance().getLogger().info(colored(object + ""));
    }

    public static String colored(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

}
