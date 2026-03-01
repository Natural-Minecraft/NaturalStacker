package id.naturalsmp.naturalstacker.utils.spawners;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import org.bukkit.block.CreatureSpawner;

public interface SyncedCreatureSpawner extends CreatureSpawner {

    NaturalStacker plugin = NaturalStackerPlugin.getPlugin();

    static SyncedCreatureSpawner of(CreatureSpawner creatureSpawner) {
        return creatureSpawner instanceof SyncedCreatureSpawner ? (SyncedCreatureSpawner) creatureSpawner :
                plugin.getNMSSpawners().createSyncedSpawner(creatureSpawner);
    }

}
