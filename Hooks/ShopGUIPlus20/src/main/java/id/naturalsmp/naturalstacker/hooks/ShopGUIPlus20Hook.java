package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import net.brcdev.shopgui.ShopGuiPlusApi;
import net.brcdev.shopgui.spawner.external.provider.ExternalSpawnerProvider;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;

@SuppressWarnings("unused")
public final class ShopGUIPlus20Hook {

    private static NaturalStackerPlugin plugin;

    public static void register(NaturalStackerPlugin plugin) {
        ShopGUIPlus20Hook.plugin = plugin;
        ShopGuiPlusApi.registerSpawnerProvider(new NaturalStackerSpawnerProvider());
        NaturalStackerPlugin.log("Found ShopGUIPlus - Hooked as SpawnerProvider!");
    }

    private static final class NaturalStackerSpawnerProvider implements ExternalSpawnerProvider {

        @Override
        public String getName() {
            return "NaturalStackerPlugin";
        }

        @Override
        public ItemStack getSpawnerItem(EntityType entityType) {
            return plugin.getProviders().getSpawnersProvider().getSpawnerItem(entityType, 1, null);
        }

        @Override
        public EntityType getSpawnerEntityType(ItemStack itemStack) {
            return plugin.getProviders().getSpawnersProvider().getSpawnerType(itemStack);
        }

    }

}
