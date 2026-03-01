package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.enums.EntityFlag;
import id.naturalsmp.naturalstacker.api.objects.StackedEntity;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.threads.Executor;
import me.hexedhero.pp.api.PinataSpawnEvent;
import org.bukkit.entity.Llama;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class PinataPartyHook {

    public static void register(NaturalStacker plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
            public void onPinataSpawn(PinataSpawnEvent e) {
                // The pinata is actually spawned later, therefore there must be a delay.
                Executor.sync(() -> {
                    Llama llama = e.getPinata().getEntity();
                    if (llama != null) {
                        StackedEntity stackedEntity = WStackedEntity.of(llama);
                        stackedEntity.setFlag(EntityFlag.AVOID_ONE_SHOT, true);
                    }
                }, 20L);
            }
        }, plugin);
    }

}
