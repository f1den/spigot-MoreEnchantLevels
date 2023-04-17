package net.nightly.moreenchantlevels;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import org.jetbrains.annotations.NotNull;

public final class MoreEnchantLevels extends JavaPlugin {

    private static  MoreEnchantLevels instance;

    @Override
    public void onEnable() {
        instance = this;
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public static MoreEnchantLevels getInstance() {
        return instance;
    }
}
