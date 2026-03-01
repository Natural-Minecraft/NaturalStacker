package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.enums.SpawnCause;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import id.naturalsmp.naturalstacker.utils.threads.Executor;
import com.dsh105.echopet.compat.api.event.PetPreSpawnEvent;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@SuppressWarnings("unused")
public final class EchoPetHook {

    public static void register(NaturalStacker plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onEchoPetSpawn(PetPreSpawnEvent e) {
                Executor.sync(() -> {
                    Entity entity = e.getPet().getEntityPet().getBukkitEntity();
                    if (EntityUtils.isStackable(entity))
                        WStackedEntity.of(entity).setSpawnCause(SpawnCause.ECHO_PET);
                }, 1L);
            }
        }, plugin);
    }

}
