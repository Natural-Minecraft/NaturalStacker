package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.enums.SpawnCause;
import id.naturalsmp.naturalstacker.api.objects.StackedEntity;
import id.naturalsmp.naturalstacker.api.objects.StackedSpawner;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.objects.WStackedSpawner;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import id.naturalsmp.naturalstacker.utils.threads.Executor;
import com.songoda.epicspawners.api.events.SpawnerSpawnEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class EpicSpawners6Hook {

    public static void register(NaturalStacker plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onSpawnerSpawn(SpawnerSpawnEvent e) {
                if (!EntityUtils.isStackable(e.getEntity()))
                    return;

                StackedEntity stackedEntity = WStackedEntity.of(e.getEntity());
                stackedEntity.setSpawnCause(SpawnCause.EPIC_SPAWNERS);

                StackedSpawner stackedSpawner = WStackedSpawner.of(e.getSpawner().getCreatureSpawner());

                //It takes 1 tick for EpicSpawners to set the metadata for the mobs.
                Executor.sync(() -> stackedEntity.runSpawnerStackAsync(stackedSpawner, null), 2L);
            }
        }, plugin);
    }

}
