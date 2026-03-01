package id.naturalsmp.naturalstacker.command;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface ICommand {

    String getLabel();

    String getUsage();

    String getPermission();

    String getDescription();

    int getMinArgs();

    int getMaxArgs();

    void perform(NaturalStackerPlugin plugin, CommandSender sender, String[] args);

    List<String> tabComplete(NaturalStackerPlugin plugin, CommandSender sender, String[] args);

}
