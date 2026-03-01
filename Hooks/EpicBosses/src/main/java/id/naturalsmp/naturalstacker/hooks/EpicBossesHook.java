package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.api.enums.SpawnCause;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import com.songoda.epicbosses.events.BossSkillEvent;
import com.songoda.epicbosses.events.BossSpawnEvent;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@SuppressWarnings("unused")
public final class EpicBossesHook {

    public static void register(NaturalStackerPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onBossSpawn(BossSpawnEvent e) {
                LivingEntity livingEntity = e.getActiveBossHolder().getLivingEntity();
                if (EntityUtils.isStackable(livingEntity))
                    WStackedEntity.of(livingEntity).setSpawnCause(SpawnCause.EPIC_BOSSES);
            }

            @EventHandler
            public void onBossSkill(BossSkillEvent e) {
                if (!e.getSkill().getDisplayName().equals("Minions"))
                    return;

                e.getActiveBossHolder().getActiveMinionHolderMap().values().forEach(activeMinionHolder ->
                        activeMinionHolder.getLivingEntityMap().keySet().forEach(position -> {
                            LivingEntity livingEntity = activeMinionHolder.getLivingEntity(position);
                            if (EntityUtils.isStackable(livingEntity))
                                WStackedEntity.of(livingEntity).setSpawnCause(SpawnCause.EPIC_BOSSES_MINION);
                        }));
            }
        }, plugin);
    }

}
