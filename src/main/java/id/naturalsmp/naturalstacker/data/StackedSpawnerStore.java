package id.naturalsmp.naturalstacker.data;

import id.naturalsmp.naturalstacker.api.objects.StackedSpawner;
import id.naturalsmp.naturalstacker.objects.WUnloadedStackedSpawner;
import id.naturalsmp.naturalstacker.utils.data.structures.Location2ObjectMap;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import javax.annotation.Nullable;

public class StackedSpawnerStore extends AbstractBlockDataStore<StackedSpawner, WUnloadedStackedSpawner> {

    private final Location2ObjectMap<LivingEntity> linkedEntities = new Location2ObjectMap<>();

    public void storeLinkedEntity(Location location, LivingEntity livingEntity) {
        this.linkedEntities.put(location, livingEntity);
    }

    @Nullable
    public LivingEntity getLinkedEntity(Location location) {
        return this.linkedEntities.get(location);
    }

    @Nullable
    public LivingEntity removeLinkedEntity(Location location) {
        return this.linkedEntities.remove(location);
    }

}
