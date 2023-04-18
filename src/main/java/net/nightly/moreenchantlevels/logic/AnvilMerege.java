package net.nightly.moreenchantlevels.logic;

import net.nightly.moreenchantlevels.MoreEnchantLevels;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.Bukkit.getServer;

public class AnvilMerege implements Listener {
    private static AnvilMerege instance;

    public static AnvilMerege getInstance() {
        if (instance == null) {
            instance = new AnvilMerege();
        }
        return instance;
    }

    public void start() {
        getServer().getPluginManager().registerEvents(this, MoreEnchantLevels.getInstance());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPrepare(PrepareAnvilEvent e) {
        ItemStack a = e.getInventory().getItem(0);
        ItemStack b = e.getInventory().getItem(1);
        ItemStack c = e.getInventory().getItem(2);

        if (c != null && c.getItemMeta() != null && c.getItemMeta().getDisplayName() != null)
            rename(c.getItemMeta().getDisplayName(), e.getResult());

        if (a != null && b != null) {
            return;
        }

        e.setResult(new ItemStack(Material.AIR));

        if (a == null || a.getType() == Material.AIR || a.getType() == Material.ENCHANTED_BOOK) {
            return;
        }

        ItemStack result = enchant(a, b);
        if (c != null && c.getItemMeta() != null && c.getItemMeta().getDisplayName() != null) {
            rename(c.getItemMeta().getDisplayName(), result);
        }
        e.setResult(result);
    }

    public void rename(String displayName, ItemStack result) {
        ItemMeta meta = result.getItemMeta();
        if (meta == null)
            return;
        meta.setDisplayName(displayName.replaceAll("&", "§"));
        result.setItemMeta(meta);
    }

    public ItemStack enchant(ItemStack a, ItemStack b) {
        ItemStack result = new ItemStack(a);
        if (b == null || b.getType() != Material.ENCHANTED_BOOK)
            return new ItemStack(a);

        EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) b.getItemMeta();
        ItemMeta meta = result.getItemMeta();

        for (Enchantment en : bookMeta.getStoredEnchants().keySet()) {
            int level = ((Integer) bookMeta.getStoredEnchants().get(en)).intValue();
            int newLevel = Math.min(level + 1, en.getMaxLevel());
            meta.addEnchant(en, newLevel, true);
        }

        result.setItemMeta(meta);
        return result;
    }

    public boolean isCheat(ItemStack item) {
        if (item.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
            for (Enchantment E : meta.getStoredEnchants().keySet()) {
                if (((Integer) meta.getStoredEnchants().get(E)).intValue() > E.getMaxLevel())
                    return true;
            }
            return false;
        }
        for (Enchantment E : item.getEnchantments().keySet()) {
            if (((Integer) item.getEnchantments().get(E)).intValue() > E.getMaxLevel())
                return true;
        }
        return false;
    }
}