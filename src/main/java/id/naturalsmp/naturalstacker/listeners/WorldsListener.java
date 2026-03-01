package id.naturalsmp.naturalstacker.listeners;

import id.naturalsmp.naturalstacker.NaturalStacker;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

public final class WorldsListener implements Listener {

    private final NaturalStacker plugin;

    public WorldsListener(NaturalStacker plugin) {
        this.plugin = plugin;
        for (World world : Bukkit.getWorlds())
            plugin.getNMSWorld().startEntityListen(world);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldInit(WorldInitEvent e) {
        plugin.getNMSWorld().startEntityListen(e.getWorld());
    }

}
