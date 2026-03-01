package id.naturalsmp.naturalstacker.command.commands;

import id.naturalsmp.naturalstacker.Locale;
import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.command.ICommand;
import org.bukkit.command.CommandSender;

import java.util.List;

public final class CommandStats implements ICommand {

    @Override
    public String getLabel() {
        return "stats";
    }

    @Override
    public String getUsage() {
        return "stacker stats";
    }

    @Override
    public String getPermission() {
        return "naturalstacker.stats";
    }

    @Override
    public String getDescription() {
        return "See all the cached stats of NaturalStacker.";
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
    public void perform(NaturalStackerPlugin plugin, CommandSender sender, String[] args) {
        int entitiesAmount = plugin.getDataHandler().stackedEntityStore.size();
        int unloadedEntitiesAmount = plugin.getDataHandler().stackedEntityStore.sizeUnloaded();
        int itemsAmount = plugin.getDataHandler().stackedItemStore.size();
        int unloadedItemsAmount = plugin.getDataHandler().stackedItemStore.sizeUnloaded();
        int spawnersAmount = plugin.getDataHandler().stackedSpawnerStore.size();
        int barrelsAmount = plugin.getDataHandler().stackedBarrelStore.size();
        int spawnersUnloadedAmount = plugin.getDataHandler().stackedSpawnerStore.sizeUnloaded();
        int barrelsUnloadedAmount = plugin.getDataHandler().stackedBarrelStore.sizeUnloaded();

        String message = "&eNaturalStacker Stats:" +
                "\n&e - Stacked Entities: (Loaded: " + entitiesAmount + ", Unloaded: " + unloadedEntitiesAmount + ")" +
                "\n&e - Stacked Items: (Loaded: " + itemsAmount + ", Unloaded: " + unloadedItemsAmount + ")" +
                "\n&e - Stacked Spawners: (Loaded: " + spawnersAmount + ", Unloaded: " + spawnersUnloadedAmount + ")" +
                "\n&e - Stacked Barrels: (Loaded: " + barrelsAmount + ", Unloaded: " + barrelsUnloadedAmount + ")";

        Locale.sendMessage(sender, message);
    }

    @Override
    public List<String> tabComplete(NaturalStackerPlugin plugin, CommandSender sender, String[] args) {
        return null;
    }
}
