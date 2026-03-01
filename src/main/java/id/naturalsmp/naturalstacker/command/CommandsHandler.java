package id.naturalsmp.naturalstacker.command;

import id.naturalsmp.naturalstacker.Locale;
import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.command.commands.CommandDebug;
import id.naturalsmp.naturalstacker.command.commands.CommandGive;
import id.naturalsmp.naturalstacker.command.commands.CommandInfo;
import id.naturalsmp.naturalstacker.command.commands.CommandInspect;
import id.naturalsmp.naturalstacker.command.commands.CommandKill;
import id.naturalsmp.naturalstacker.command.commands.CommandReload;
import id.naturalsmp.naturalstacker.command.commands.CommandSave;
import id.naturalsmp.naturalstacker.command.commands.CommandSettings;
import id.naturalsmp.naturalstacker.command.commands.CommandSimulate;
import id.naturalsmp.naturalstacker.command.commands.CommandStats;
import id.naturalsmp.naturalstacker.command.commands.CommandTest;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public final class CommandsHandler implements CommandExecutor, TabCompleter {

    private NaturalStackerPlugin plugin;
    private List<ICommand> subCommands = new ArrayList<>();

    public CommandsHandler(NaturalStackerPlugin plugin) {
        this.plugin = plugin;
        subCommands.add(new CommandDebug());
        subCommands.add(new CommandGive());
        subCommands.add(new CommandInfo());
        subCommands.add(new CommandInspect());
        subCommands.add(new CommandKill());
        subCommands.add(new CommandReload());
        subCommands.add(new CommandSave());
        subCommands.add(new CommandSettings());
        subCommands.add(new CommandSimulate());
        subCommands.add(new CommandStats());
        subCommands.add(new CommandTest());
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (args.length > 0) {
            for (ICommand subCommand : subCommands) {
                if (subCommand.getLabel().equalsIgnoreCase(args[0])) {
                    if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
                        Locale.NO_PERMISSION.send(sender);
                        return false;
                    }
                    if (args.length < subCommand.getMinArgs() || args.length > subCommand.getMaxArgs()) {
                        Locale.COMMAND_USAGE.send(sender, subCommand.getUsage());
                        return false;
                    }
                    subCommand.perform(plugin, sender, args);
                    return true;
                }
            }
        }

        //Checking that the player has permission to use at least one of the commands.
        for (ICommand subCommand : subCommands) {
            if (sender.hasPermission(subCommand.getPermission())) {
                //Player has permission
                Locale.HELP_COMMAND_HEADER.send(sender);

                for (ICommand cmd : subCommands) {
                    if (sender.hasPermission(subCommand.getPermission()))
                        Locale.HELP_COMMAND_LINE.send(sender, cmd.getUsage(), cmd.getDescription());
                }

                Locale.HELP_COMMAND_FOOTER.send(sender);
                return false;
            }
        }

        Locale.NO_PERMISSION.send(sender);

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (args.length > 0) {
            for (ICommand subCommand : subCommands) {
                if (subCommand.getLabel().equalsIgnoreCase(args[0])) {
                    if (subCommand.getPermission() != null && !sender.hasPermission(subCommand.getPermission())) {
                        return new ArrayList<>();
                    }
                    return subCommand.tabComplete(plugin, sender, args);
                }
            }
        }

        List<String> list = new ArrayList<>();

        for (ICommand subCommand : subCommands)
            if (subCommand.getPermission() == null || sender.hasPermission(subCommand.getPermission()))
                if (subCommand.getLabel().startsWith(args[0]))
                    list.add(subCommand.getLabel());

        return list;
    }
}
