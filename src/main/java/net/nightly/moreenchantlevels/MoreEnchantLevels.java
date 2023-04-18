package net.nightly.moreenchantlevels;

import net.nightly.moreenchantlevels.logic.AnvilMerege;
import org.bukkit.plugin.java.JavaPlugin;
import net.nightly.moreenchantlevels.commands.MoreEnchantsCommand;


public final class MoreEnchantLevels extends JavaPlugin {

    private static MoreEnchantLevels instance;

    @Override
    public void onEnable() {
        instance = this;
        new MoreEnchantsCommand();
        getServer().getPluginManager().registerEvents(AnvilMerege.getInstance(), this); // Register AnvilMerge as event listener
    }


    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MoreEnchantLevels getInstance() {
        return instance;
    }
}