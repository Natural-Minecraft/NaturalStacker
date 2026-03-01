package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.api.enums.StackCheckResult;
import org.bukkit.entity.Entity;

public interface EntitySimilarityProvider {

    StackCheckResult areSimilar(Entity entity, Entity other);

}
