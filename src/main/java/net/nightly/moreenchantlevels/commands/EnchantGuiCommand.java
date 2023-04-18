package net.nightly.moreenchantlevels.commands;

import com.google.common.collect.Lists;
import net.nightly.moreenchantlevels.MoreEnchantLevels;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class EnchantGuiCommand extends AbstractCommand {

    public EnchantGuiCommand() { super("moreenchantlevels"); }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if(args.length == 0){
            sender.sendMessage("IDK: /" + label + " enchant");
            return;
        }

        if(args[0].equalsIgnoreCase("enchant")){
            if(!sender.hasPermission("moreenchantlevels.enchant")){
                sender.sendMessage(ChatColor.RED + "You don't have permission to do that");
                return;
            }
            MoreEnchantLevels.getInstance().reloadConfig();
            sender.sendMessage(ChatColor.GREEN + "Plugin reloaded");
            return;
        }
        sender.sendMessage(ChatColor.RED + "Unknown command:  " + args[0]);
        sender.sendMessage(ChatColor.RED + "Type /" + label + " for help");
    }

    @Override
    public List<String> complete(CommandSender sender, String[] args) {
        if(args.length == 1) return Lists.newArrayList("enchant");
        return Lists.newArrayList();
    }
}
