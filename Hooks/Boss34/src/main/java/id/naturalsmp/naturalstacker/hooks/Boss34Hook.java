package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.api.enums.SpawnCause;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import id.naturalsmp.naturalstacker.utils.threads.Executor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.mineacademy.boss.api.event.BossSpawnEvent;

@SuppressWarnings("unused")
public final class Boss34Hook {

    public static void register(NaturalStackerPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onBossSpawn(BossSpawnEvent e) {
                if (EntityUtils.isStackable(e.getEntity()))
                    Executor.sync(() -> WStackedEntity.of(e.getEntity()).setSpawnCause(SpawnCause.BOSS), 2L);
            }
        }, plugin);
    }

    private static class NewBossListener implements Listener {

        @EventHandler
        public void onBossSpawn(org.mineacademy.boss.api.event.BossSpawnEvent e) {
            if (EntityUtils.isStackable(e.getEntity()))
                Executor.sync(() -> WStackedEntity.of(e.getEntity()).setSpawnCause(SpawnCause.BOSS), 2L);
        }

    }

}
