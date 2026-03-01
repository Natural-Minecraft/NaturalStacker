package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.EntitiesLoadEvent;
import org.bukkit.event.world.EntitiesUnloadEvent;

import static id.naturalsmp.naturalstacker.handlers.SystemHandler.ENTITIES_STAGE;

public class PaperChunksHook implements Listener {

    public static void register(NaturalStackerPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new PaperChunksHook(plugin), plugin);
    }

    private final NaturalStackerPlugin plugin;

    public PaperChunksHook(NaturalStackerPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntitiesLoad(EntitiesLoadEvent e) {
        plugin.getSystemManager().handleChunkLoad(e.getChunk(), ENTITIES_STAGE, e.getEntities());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntitiesUnload(EntitiesUnloadEvent e) {
        plugin.getSystemManager().handleChunkUnload(e.getChunk(), ENTITIES_STAGE, e.getEntities());
    }

}
