package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.enums.SpawnCause;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import net.splodgebox.elitebosses.events.BossSpawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class EliteBossesHook {

    public static void register(NaturalStacker plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onBossSpawn(BossSpawnEvent e) {
                if (EntityUtils.isStackable(e.getBoss()))
                    WStackedEntity.of(e.getBoss()).setSpawnCause(SpawnCause.ELITE_BOSSES);
            }
        }, plugin);
    }

}
