package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.enums.SpawnCause;
import id.naturalsmp.naturalstacker.api.objects.StackedEntity;
import io.lumine.mythic.bukkit.MythicBukkit;
import io.lumine.mythic.core.mobs.ActiveMob;
import org.bukkit.entity.LivingEntity;

@SuppressWarnings("unused")
public final class EntityNameProvider_MythicMobs5 implements EntityNameProvider {

    private final NaturalStacker plugin;

    public EntityNameProvider_MythicMobs5(NaturalStacker plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getCustomName(StackedEntity stackedEntity) {
        if (stackedEntity.getSpawnCause() != SpawnCause.MYTHIC_MOBS)
            return null;

        LivingEntity livingEntity = stackedEntity.getLivingEntity();

        ActiveMob activeMob = MythicBukkit.inst().getMobManager().getMythicMobInstance(livingEntity);

        try {
            return activeMob.getDisplayName();
        } catch (Throwable ignored) {
        }

        return plugin.getNMSEntities().getCustomName(livingEntity);
    }

}
