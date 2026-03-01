package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.enums.SpawnCause;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import me.badbones69.crazyenchantments.api.managers.AllyManager;
import me.badbones69.crazyenchantments.api.objects.AllyMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

import javax.annotation.Nullable;

public final class CrazyEnchantmentsHook {

    private static boolean registered = false;

    public static void register(NaturalStacker plugin) {
        if (registered)
            return;

        plugin.getServer().getPluginManager().registerEvents(new AllyListener(plugin), plugin);

        registered = true;
    }

    @Nullable
    private static AllyMob.AllyType getAllyType(EntityType entityType) {
        for (AllyMob.AllyType allyType : AllyMob.AllyType.values()) {
            if (allyType.getEntityType() == entityType) {
                return allyType;
            }
        }

        return null;
    }

    private static class AllyListener implements Listener {

        private final NaturalStacker plugin;

        AllyListener(NaturalStacker plugin) {
            this.plugin = plugin;
        }

        @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
        private void onAllySpawn(CreatureSpawnEvent e) {
            if (!EntityUtils.isStackable(e.getEntity()))
                return;

            AllyMob.AllyType allyType = getAllyType(e.getEntityType());
            if (allyType == null)
                return;

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (AllyManager.getInstance().isAllyMob(e.getEntity())) {
                    WStackedEntity.of(e.getEntity()).setSpawnCause(SpawnCause.CRAZY_ENCHANTMENTS);
                }
            }, 1L);
        }

    }


}
