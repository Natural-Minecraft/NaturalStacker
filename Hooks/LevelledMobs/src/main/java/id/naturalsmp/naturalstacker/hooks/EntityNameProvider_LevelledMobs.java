package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.api.objects.StackedEntity;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

@SuppressWarnings("unused")
public final class EntityNameProvider_LevelledMobs implements EntityNameProvider {


    private final NaturalStackerPlugin plugin;
    private final NamespacedKey isLevelledKey;

    public EntityNameProvider_LevelledMobs(NaturalStackerPlugin plugin) {
        this.plugin = plugin;
        Plugin levelledMobs = Bukkit.getPluginManager().getPlugin("LevelledMobs");
        assert levelledMobs != null;
        isLevelledKey = new NamespacedKey(levelledMobs, "isLevelled");
    }

    @Override
    public String getCustomName(StackedEntity stackedEntity) {
        LivingEntity livingEntity = stackedEntity.getLivingEntity();
        return isLevelledMob(livingEntity) ? plugin.getNMSEntities().getCustomName(livingEntity) : null;
    }

    private boolean isLevelledMob(LivingEntity livingEntity) {
        PersistentDataContainer dataContainer = livingEntity.getPersistentDataContainer();
        return dataContainer.has(isLevelledKey, PersistentDataType.STRING);
    }

}
