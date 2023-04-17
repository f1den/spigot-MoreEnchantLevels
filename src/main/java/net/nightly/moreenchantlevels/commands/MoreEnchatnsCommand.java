package net.nightly.moreenchantlevels.commands;

import net.nightly.moreenchantlevels.MoreEnchantLevels;
import net.nightly.moreenchantlevels.commands.AbstractCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MoreEnchatnsCommand extends AbstractCommand {

    public MoreEnchatnsCommand() {
        super("moreenchantslevel");
    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if(args.length == 0){
            sender.sendMessage("Reload plugin: /" + label + " reload");
            return;
        }

        if(args[0].equalsIgnoreCase("reload")){
            if(!sender.hasPermission("moreenchantslevel.reload")){
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
}
