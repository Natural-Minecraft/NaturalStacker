package id.naturalsmp.naturalstacker.command.commands;

import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.command.ICommand;
import id.naturalsmp.naturalstacker.menu.ConfigEditorMenu;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public final class CommandSettings implements ICommand {

    @Override
    public String getLabel() {
        return "settings";
    }

    @Override
    public String getUsage() {
        return "stacker settings";
    }

    @Override
    public String getPermission() {
        return "naturalstacker.settings";
    }

    @Override
    public String getDescription() {
        return "Open settings editor.";
    }

    @Override
    public int getMinArgs() {
        return 1;
    }

    @Override
    public int getMaxArgs() {
        return 1;
    }

    @Override
    public void perform(NaturalStacker plugin, CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can perform this command.");
            return;
        }

        ConfigEditorMenu.open((Player) sender);
    }

    @Override
    public List<String> tabComplete(NaturalStacker plugin, CommandSender sender, String[] args) {
        return null;
    }
}
