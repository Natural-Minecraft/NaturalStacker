package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.enums.SpawnCause;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.bukkit.events.MythicMobSpawnEvent;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class MythicMobsHook5 {

    public static void register(NaturalStacker plugin) {
        plugin.getServer().getPluginManager().registerEvents(new MythicMobsListener(), plugin);
        plugin.getProviders().registerEntityDuplicateListener(MythicMobsHook5::tryDuplicate);
    }

    private static LivingEntity tryDuplicate(LivingEntity livingEntity) {
        if (WStackedEntity.of(livingEntity).getSpawnCause() == SpawnCause.MYTHIC_MOBS) {
            ActiveMob activeMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(livingEntity);
            ActiveMob duplicate = MythicBukkit.inst().getMobManager().spawnMob(activeMob.getType().getInternalName(), livingEntity.getLocation());
            return (LivingEntity) duplicate.getEntity().getBukkitEntity();
        }

        return null;
    }

    public static final class MythicMobsListener implements Listener {

        @EventHandler
        public void onMythicMobSpawn(MythicMobSpawnEvent e) {
            if (EntityUtils.isStackable(e.getEntity()))
                WStackedEntity.of(e.getEntity()).setSpawnCause(SpawnCause.MYTHIC_MOBS);
        }

    }

}
