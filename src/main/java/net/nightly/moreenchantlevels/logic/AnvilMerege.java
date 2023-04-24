package net.nightly.moreenchantlevels.logic;


import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class AnvilMerege implements Listener {


    private static AnvilMerege instance;
    HashMap<String, String> latestMessage = new HashMap<String, String>();

    public static AnvilMerege getInstance() {
        if (instance == null) {
            instance = new AnvilMerege();
        }
        return instance;
    }


    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPrepare(PrepareAnvilEvent e) {
        ItemStack a = e.getInventory().getItem(0);
        ItemStack b = e.getInventory().getItem(1);
        ItemStack c = new ItemStack(Material.AIR);
        if (a == null && b == null && c == null) {
            return;
        }
        if (b == null || a == null) {
            return;
        }

        //ITEM + ITEM
        if (a.getType() != Material.ENCHANTED_BOOK && b.getType() != Material.ENCHANTED_BOOK) {
            if (a.getType() == b.getType()) {
                c = enchantItem(a, b, e);
            }
        }
        // BOOK + BOOK
        else if (a.getType() == Material.ENCHANTED_BOOK && b.getType() == Material.ENCHANTED_BOOK) {
            c = enchantBook(a, b, e);
        } else if (b.getType() == Material.ENCHANTED_BOOK && a.getType() != Material.ENCHANTED_BOOK) {
            //TODO: BOOK + ITEM
            c = comboEnchant(a, b, e);
        }
        e.setResult(c);
    }

    private ItemStack enchantBook(ItemStack a, ItemStack b, PrepareAnvilEvent e) {

        ItemStack c = new ItemStack(a.getType());
        Player p = (Player) e.getView().getPlayer();
        EnchantmentStorageMeta aMeta = (EnchantmentStorageMeta) (a.getItemMeta());
        EnchantmentStorageMeta bMeta = (EnchantmentStorageMeta) b.getItemMeta();

        Map<Enchantment, Integer> aEnchants = aMeta.getStoredEnchants();
        Map<Enchantment, Integer> bEnchants = bMeta.getStoredEnchants();
        if (aEnchants.isEmpty()) {
            aEnchants = a.getEnchantments();
        }
        if (bEnchants.isEmpty()) {
            bEnchants = b.getEnchantments();
        }

        int reqLvl = 0;

        for (Enchantment i : aEnchants.keySet()) {
            for (Enchantment j : bEnchants.keySet()) {
                int levelA = Integer.parseInt(aEnchants.get(i).toString());
                int levelB = Integer.parseInt(bEnchants.get(j).toString());

                if (i == j) {
                    if (levelA == levelB) {
//                        //todo: Limitation of ench level
//                        // validateEnchantment();
                        e.getInventory().setMaximumRepairCost(0);
                        if (levelA > 5) {

                            reqLvl += (int) Math.ceil(levelA * 20.5);
                        } else {
                            reqLvl += (int) Math.ceil(levelA * 7.5);
                        }
                        c.addUnsafeEnchantment(i, levelA + 1);
                    } else {
                        reqLvl += (int) (Math.max(levelA, levelB) * 1.5);
                        c.addUnsafeEnchantment(i, Math.max(levelA, levelB));
                    }
                } else if (i != j) {
                    if (!i.conflictsWith(j)) {

                        reqLvl += (int) (levelB * 4.5);
                        c.addUnsafeEnchantment(i, levelA);
                        c.addUnsafeEnchantment(j, levelB);
                    } else {
                        //TODO: если конфликтует либо оставить чар только с 1 без потери xp либо заменить вторым + 4*lvl xp

                        reqLvl += (int) (levelB * 4.5);
                        c.addUnsafeEnchantment(j, levelB);
                    }
                }
            }
        }

        if (e.getInventory().getRenameText() != c.getItemMeta().getDisplayName()) {
            c = rename(c, e.getInventory().getRenameText());
            reqLvl += 2;
        }

        if (reqLvl > 40) {
            if (p.getLevel() >= reqLvl) {
                pMsg(p, "Enchantment cost: §b§o" + reqLvl + " §rlvl");
                e.getInventory().setRepairCost(reqLvl);
                return c;
            } else {
                pMsg(p, "You need: §4§I" + reqLvl + " §rlvl");
                return null;
            }
        } else {
            e.getInventory().setRepairCost(reqLvl);
            return c;
        }
    }


    private ItemStack enchantItem(ItemStack a, ItemStack b, PrepareAnvilEvent e) {
        ItemStack c = new ItemStack(a.getType());
        Player p = (Player) e.getView().getPlayer();
        Map<Enchantment, Integer> ItemEnchantA = a.getItemMeta().getEnchants();
        Map<Enchantment, Integer> ItemEnchantB = b.getItemMeta().getEnchants();
        int reqLvl = 0;
        for (Enchantment i : ItemEnchantA.keySet()) {
            for (Enchantment j : ItemEnchantB.keySet()) {
                int levelA = Integer.parseInt(ItemEnchantA.get(i).toString());
                int levelB = Integer.parseInt(ItemEnchantB.get(j).toString());
                if (i == j) {
                    if (levelA == levelB) {
                        //todo: Limitation of ench level
                        // validateEnchantment();
                        e.getInventory().setMaximumRepairCost(0);
                        if (levelA > 6) {
                            reqLvl += (int) Math.ceil(levelA * 20.5);
                        } else {
                            reqLvl += (int) Math.ceil(levelA * 7.5);
                        }
                        c.addUnsafeEnchantment(i, levelA + 1);
                    } else {
                        reqLvl += (int) (Math.max(levelA, levelB) * 1.5);
                        c.addUnsafeEnchantment(i, Math.max(levelA, levelB));
                    }
                } else if (i != j) {
                    if (!i.conflictsWith(j)) {
                        reqLvl += (int) (levelB * 4.5);
                        c.addUnsafeEnchantment(i, levelA);
                        c.addUnsafeEnchantment(j, levelB);
                    } else {
                        //TODO: если конфликтует либо оставить чар только с 1 без потери xp либо заменить вторым + 4*lvl xp
                        reqLvl += (int) (levelB * 4.5);
                        c.addUnsafeEnchantment(j, levelB);
                    }
                }
            }
        }


        if (e.getInventory().getRenameText() != c.getItemMeta().getDisplayName()) {
            c = rename(c, e.getInventory().getRenameText());
            reqLvl += 2;
        }
        if (reqLvl > 40) {
            if (p.getLevel() >= reqLvl) {
                pMsg(p, "Enchantment cost: §b§o" + reqLvl + " §rlvl");
                e.getInventory().setRepairCost(reqLvl);
                return c;
            } else {
                pMsg(p, "You need: §4§I" + reqLvl + " §rlvl");
                return null;
            }
        } else {
            return c;
        }
    }

    private boolean validateEnchant(Enchantment ench) {

        return false;
    }

    private void pMsg(Player p, String msg) {
        if (latestMessage.get(p.getName()) != null) {
            if (!Objects.equals(latestMessage.get(p.getName()), msg)) {
                p.sendMessage(msg);
                latestMessage.put(p.getName(), msg);
            }
        } else {
            latestMessage.put(p.getName(), msg);
            p.sendMessage(msg);
        }
    }

    private ItemStack rename(ItemStack c, String name) {
        name = name.replace("&", "§");
        ItemMeta cMeta = c.getItemMeta();
        cMeta.setDisplayName(name);
        c.setItemMeta(cMeta);
        return c;
    }

    private ItemStack comboEnchant(ItemStack a, ItemStack b, PrepareAnvilEvent e) {
        ItemStack c = new ItemStack(a.getType());
        Player p = (Player) e.getView().getPlayer();
        Map<Enchantment, Integer> ItemEnchantA = a.getItemMeta().getEnchants();
        Map<Enchantment, Integer> ItemEnchantB = b.getItemMeta().getEnchants();
        HashMap<Enchantment, Integer> Result = new HashMap<Enchantment, Integer>();
        //FIX BOOK BUG
        if (ItemEnchantB.isEmpty()) {
            EnchantmentStorageMeta bMeta = (EnchantmentStorageMeta) b.getItemMeta();
            ItemEnchantB = bMeta.getStoredEnchants();
        }
        int reqLvl = 0;
        if (!ItemEnchantA.isEmpty()) {
            for (Enchantment i : ItemEnchantA.keySet()) {
                for (Enchantment j : ItemEnchantB.keySet()) {
                    int levelA = Integer.parseInt(ItemEnchantA.get(i).toString());
                    int levelB = Integer.parseInt(ItemEnchantB.get(j).toString());

                    if (i == j) {
                        if (levelA == levelB) {
                            //todo: Limitation of ench level
                            // validateEnchantment();
                            e.getInventory().setMaximumRepairCost(0);
                            if (levelA > 5) {
                                reqLvl += (int) Math.ceil(levelA * 20.5);
                            } else {
                                reqLvl += (int) Math.ceil(levelA * 7.5);
                            }
                            if (Result.get(i) < levelA + 1) {
                                Result.put(i, levelA + 1);
                            }

                        } else {
                            reqLvl += (int) (Math.max(levelA, levelB) * 1.5);
                            if (Result.get(i) < Math.max(levelA, levelB)) {
                                Result.put(i, Math.max(levelA, levelB));
                            }


                        }
                    } else if (i != j) {
                        if (!i.conflictsWith(j)) {
                            reqLvl += (int) (levelB * 4.5);
                            if (Result.get(i) < levelA) {
                                Result.put(i, levelA);
                            }
                            if (Result.get(j) < levelB) {
                                Result.put(j, levelB);
                            }
                        } else {
                            //TODO: если конфликтует либо оставить чар только с 1 без потери xp либо заменить вторым + 4*lvl xp
                            reqLvl += (int) (levelB * 4.5);
                            if (Result.get(j) < levelB) {
                                Result.put(j, levelB);
                            }
                        }
                    }
                }
            }
        } else {
            for (Enchantment j : ItemEnchantB.keySet()) {
                int levelB = Integer.parseInt(ItemEnchantB.get(j).toString());
                if (levelB > 5) {
                    reqLvl += (int) Math.ceil(levelB * 20.5);
                } else {
                    reqLvl += (int) Math.ceil(levelB * 7.5);
                }
                if (Result.get(j) < levelB) {
                    Result.put(j, levelB);
                }
            }
        }

        if (e.getInventory().getRenameText() != c.getItemMeta().getDisplayName()) {
            c = rename(c, e.getInventory().getRenameText());
            reqLvl += 2;
        }
        if (reqLvl > 40) {
            if (p.getLevel() >= reqLvl) {
                pMsg(p, "Enchantment cost: §b§o" + reqLvl + " §rlvl");
                e.getInventory().setRepairCost(reqLvl);
                return c;
            } else {
                pMsg(p, "You need: §4§I" + reqLvl + " §rlvl");
                return null;
            }
        } else {
            e.getInventory().setRepairCost(reqLvl);
            return c;
        }
    }

}


