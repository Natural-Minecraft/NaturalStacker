package id.naturalsmp.naturalstacker.hooks;

import com.naturalsmp.superiorskyblock.api.SuperiorSkyblockAPI;
import com.naturalsmp.superiorskyblock.api.island.Island;
import com.naturalsmp.superiorskyblock.api.island.IslandFlag;
import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.events.EntityStackEvent;
import id.naturalsmp.naturalstacker.api.events.SpawnerStackedEntitySpawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class SuperiorSkyblockHook {

    private static IslandFlag ENTITIES_STACKING;
    private static boolean registered = false;

    public static void register(NaturalStacker plugin) {
        if (!plugin.getSettings().superiorSkyblockHook)
            return;

        if (registered)
            return;

        try {
            ENTITIES_STACKING = IslandFlag.getByName("ENTITIES_STACKING");
        } catch (NullPointerException error) {
            IslandFlag.register("ENTITIES_STACKING");
            try {
                ENTITIES_STACKING = IslandFlag.getByName("ENTITIES_STACKING");
            } catch (NullPointerException error2) {
                NaturalStacker.log("&cCouldn't register a custom island-flag into SuperiorSkyblock - open an issue on github.");
                return;
            }
        }

        assert ENTITIES_STACKING != null;

        SuperiorSkyblockAPI.getSuperiorSkyblock().getMenus().updateSettings(ENTITIES_STACKING);
        plugin.getServer().getPluginManager().registerEvents(new EntityListener(), plugin);

        registered = true;
    }

    private static class EntityListener implements Listener {

        @EventHandler
        public void onEntityStack(EntityStackEvent e) {
            Island island = SuperiorSkyblockAPI.getIslandAt(e.getTarget().getLocation());
            if (island != null && !island.hasSettingsEnabled(ENTITIES_STACKING))
                e.setCancelled(true);
        }

        @EventHandler
        public void onEntityStackedSpawn(SpawnerStackedEntitySpawnEvent e) {
            Island island = SuperiorSkyblockAPI.getIslandAt(e.getSpawner().getLocation());
            if (island != null && !island.hasSettingsEnabled(ENTITIES_STACKING))
                e.setShouldBeStacked(false);
        }

    }

}
