package id.naturalsmp.naturalstacker.hooks;

import com.naturalsmp.common.reflection.ReflectMethod;
import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.enums.SpawnCause;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import com.kirelcodes.miniaturepets.api.events.pets.PetFinishedSpawnEvent;
import com.kirelcodes.miniaturepets.pets.Pet;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public final class MiniaturePetsHook {

    private static final ReflectMethod<LivingEntity> PET_GET_NAVIGATOR = new ReflectMethod<>(
            Pet.class, LivingEntity.class, "getNavigator");

    public static void register(NaturalStacker plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
            public void onMiniaturePetSpawn(PetFinishedSpawnEvent e) {
                LivingEntity livingEntity = getLivingEntityFromPet(e.getPet());
                if (EntityUtils.isStackable(livingEntity))
                    WStackedEntity.of(livingEntity).setSpawnCause(SpawnCause.MINIATURE_PETS);
            }
        }, plugin);
    }

    private static LivingEntity getLivingEntityFromPet(Pet pet) {
        return PET_GET_NAVIGATOR.isValid() ? PET_GET_NAVIGATOR.invoke(pet) : pet.getPet().getBaseEntity();
    }

}
