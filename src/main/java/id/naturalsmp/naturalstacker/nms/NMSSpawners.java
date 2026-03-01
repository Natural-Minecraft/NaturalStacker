package id.naturalsmp.naturalstacker.nms;

import id.naturalsmp.naturalstacker.api.objects.StackedSpawner;
import id.naturalsmp.naturalstacker.api.upgrades.SpawnerUpgrade;
import id.naturalsmp.naturalstacker.utils.spawners.SpawnerCachedData;
import id.naturalsmp.naturalstacker.utils.spawners.SyncedCreatureSpawner;
import org.bukkit.Chunk;
import org.bukkit.block.CreatureSpawner;

public interface NMSSpawners {

    void updateStackedSpawners(Chunk chunk);

    void updateStackedSpawner(StackedSpawner stackedSpawner);

    void registerSpawnConditions();

    SyncedCreatureSpawner createSyncedSpawner(CreatureSpawner creatureSpawner);

    void updateSpawner(CreatureSpawner creatureSpawner, SpawnerUpgrade spawnerUpgrade);

    SpawnerCachedData readData(CreatureSpawner creatureSpawner);

}
