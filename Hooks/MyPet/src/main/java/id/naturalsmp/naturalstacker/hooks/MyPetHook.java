package id.naturalsmp.naturalstacker.hooks;

import com.naturalsmp.common.reflection.ReflectMethod;
import id.naturalsmp.naturalstacker.NaturalStacker;
import id.naturalsmp.naturalstacker.api.enums.SpawnCause;
import id.naturalsmp.naturalstacker.objects.WStackedEntity;
import id.naturalsmp.naturalstacker.utils.entity.EntityUtils;
import id.naturalsmp.naturalstacker.utils.threads.Executor;
import de.Keyle.MyPet.api.entity.MyPetBukkitEntity;
import de.Keyle.MyPet.api.event.MyPetCallEvent;
import de.Keyle.MyPet.entity.MyPet;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@SuppressWarnings("unused")
public final class MyPetHook {

    private static final ReflectMethod<Optional<MyPetBukkitEntity>> GET_ENTITY_METHOD =
            new ReflectMethod<>(MyPet.class, Optional.class, "getEntity");

    public static void register(NaturalStacker plugin) {
        plugin.getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onMyPetSpawn(MyPetCallEvent e) {
                if (!(e.getMyPet() instanceof MyPet))
                    return;

                MyPet myPet = (MyPet) e.getMyPet();

                Executor.sync(() -> {
                    Optional<MyPetBukkitEntity> entityOptional = GET_ENTITY_METHOD.isValid() ?
                            GET_ENTITY_METHOD.invoke(myPet) : myPet.getEntity();

                    entityOptional.ifPresent(entity -> {
                        if (EntityUtils.isStackable(entity))
                            WStackedEntity.of(entity).setSpawnCause(SpawnCause.MY_PET);
                    });
                }, 1L);
            }
        }, plugin);
    }

}
