package id.naturalsmp.naturalstacker.command.commands;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.command.ICommand;
import id.naturalsmp.naturalstacker.objects.WStackedSpawner;
import id.naturalsmp.naturalstacker.utils.legacy.Materials;
import com.google.common.collect.Sets;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public final class CommandDebug implements ICommand {

    @Override
    public String getLabel() {
        return "debug";
    }

    @Override
    public String getUsage() {
        return "stacker debug";
    }

    @Override
    public String getPermission() {
        return "naturalstacker.debug";
    }

    @Override
    public String getDescription() {
        return "Toggle debug mode for a spawner.";
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
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can perform this command.");
            return;
        }

        Player pl = (Player) sender;

        Block targetBlock = pl.getTargetBlock(getMaterials("AIR", "WATER", "STATIONARY_WATER"), 10);

        if (targetBlock == null || targetBlock.getType() != Materials.SPAWNER.toBukkitType()) {
            sender.sendMessage(ChatColor.RED + "You must look directly at a spawner.");
            return;
        }

        WStackedSpawner stackedSpawner = (WStackedSpawner) WStackedSpawner.of(targetBlock);

        if(stackedSpawner.isDebug()) {
            stackedSpawner.setDebug(false);
            sender.sendMessage("" + ChatColor.GOLD + ChatColor.BOLD + "NaturalStackerPlugin " + ChatColor.GRAY + "Toggled debug mode " + ChatColor.RED + "OFF" + ChatColor.GRAY + ".");
        } else {
            stackedSpawner.setDebug(true);
            sender.sendMessage("" + ChatColor.GOLD + ChatColor.BOLD + "NaturalStackerPlugin " + ChatColor.GRAY + "Toggled debug mode " + ChatColor.GREEN + "ON" + ChatColor.GRAY + ".");
        }
    }

    @Override
    public List<String> tabComplete(NaturalStackerPlugin plugin, CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    private Set<Material> getMaterials(String... materials) {
        Set<Material> materialsSet = Sets.newHashSet();

        for (String material : materials) {
            try {
                materialsSet.add(Material.valueOf(material));
            } catch (IllegalArgumentException ignored) {
            }
        }

        return materialsSet;
    }

}
