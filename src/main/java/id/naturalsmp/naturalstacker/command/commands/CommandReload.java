package id.naturalsmp.naturalstacker.command.commands;

import id.naturalsmp.naturalstacker.Locale;
import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.command.ICommand;
import id.naturalsmp.naturalstacker.handlers.LootHandler;
import id.naturalsmp.naturalstacker.handlers.SettingsHandler;
import id.naturalsmp.naturalstacker.tasks.ItemsMerger;
import id.naturalsmp.naturalstacker.tasks.KillTask;
import id.naturalsmp.naturalstacker.tasks.StackTask;
import id.naturalsmp.naturalstacker.utils.threads.Executor;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public final class CommandReload implements ICommand {

    @Override
    public String getLabel() {
        return "reload";
    }

    @Override
    public String getUsage() {
        return "stacker reload";
    }

    @Override
    public String getPermission() {
        return "naturalstacker.reload";
    }

    @Override
    public String getDescription() {
        return "Reload the settings and the language files.";
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
        Executor.async(() -> {
            SettingsHandler.reload();
            LootHandler.reload();
            Locale.reload();
            KillTask.start();
            StackTask.start();
            ItemsMerger.start();
            Locale.RELOAD_SUCCESS.send(sender);
        });
    }

    @Override
    public List<String> tabComplete(NaturalStacker plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }
}
