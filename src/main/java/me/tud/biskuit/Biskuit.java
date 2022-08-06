package me.tud.biskuit;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Biskuit extends JavaPlugin {

    private static Biskuit instance;
    private SkriptAddon addon;

    @Override
    public void onEnable() {

        if (!Bukkit.getPluginManager().isPluginEnabled("Skript")) {
            getLogger().severe("Skript not found. Please install skript at https://github.com/SkriptLang/Skript/releases");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        instance = this;
        try {
            addon = Skript.registerAddon(this).loadClasses("me.tud.biskuit", "elements");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static Biskuit getInstance() {
        return instance;
    }

    public SkriptAddon getAddonInstance() {
        return addon;
    }
}
