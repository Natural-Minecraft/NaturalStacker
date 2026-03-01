package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.enums.EntityFlag;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import me.jet315.minions.events.SlayerSlayEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class JetsMinionsHook {

    public static void register(NaturalStacker plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onSlayerAction(SlayerSlayEvent e) {
                e.getEntitiesToKill().removeIf(livingEntity -> EntityUtils.isStackable(livingEntity) &&
                        WStackedEntity.of(livingEntity).hasFlag(EntityFlag.DEAD_ENTITY));
            }
        }, plugin);
    }

}
