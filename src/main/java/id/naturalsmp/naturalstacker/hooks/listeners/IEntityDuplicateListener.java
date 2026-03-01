package id.naturalsmp.naturalstacker.hooks.listeners;

import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;

public interface IEntityDuplicateListener {

    @Nullable
    LivingEntity duplicateEntity(LivingEntity entity);

}
