package id.naturalsmp.naturalstacker.hooks;

import org.bukkit.entity.Entity;

import javax.annotation.Nullable;

public interface EntityTypeProvider {

    @Nullable
    String checkStackEntity(Entity entity);

}
