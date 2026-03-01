package id.naturalsmp.naturalstacker.data;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.api.objects.StackedItem;
import id.naturalsmp.naturalstacker.objects.WStackedItem;
import id.naturalsmp.naturalstacker.utils.data.DataSerializer;

import java.util.UUID;

public class StackedItemStore extends AbstractEntityDataStore<StackedItem> {

    private static final NaturalStacker plugin = NaturalStackerPlugin.getPlugin();

    @Override
    protected boolean loadUnloadedInternal(UUID uuid, StackedItem stackedItem) {
        boolean unloadedFromDb = super.loadUnloadedInternal(uuid, stackedItem);
        if (unloadedFromDb)
            return true;

        try {
            ((WStackedItem) stackedItem).setSaveData(false);
            loadUnloadedFromCache(stackedItem);
        } finally {
            ((WStackedItem) stackedItem).setSaveData(true);
        }

        return true;
    }

    private void loadUnloadedFromCache(StackedItem stackedItem) {
        String cachedData = DataSerializer.deserializeData(stackedItem.getCustomName());
        if (!cachedData.isEmpty()) {
            try {
                stackedItem.setStackAmount(Integer.parseInt(cachedData), false);
            } catch (Exception ignored) {
            }

            stackedItem.setCustomName(DataSerializer.stripData(stackedItem.getCustomName()));
        } else {
            plugin.getSystemManager().getDataSerializer().loadItem(stackedItem);
        }

        // We want to update the item's size if it's above max stack size.
        // We do it here so item will not be saved.
        if (stackedItem.getStackAmount() > stackedItem.getItemStack().getMaxStackSize())
            stackedItem.setStackAmount(stackedItem.getStackAmount(), false);
    }

    public static class Unloaded implements AbstractEntityDataStore.Unloaded<StackedItem> {

        private final int stackAmount;

        public Unloaded(int stackAmount) {
            this.stackAmount = stackAmount;
        }

        @Override
        public void load(StackedItem object) {
            object.setStackAmount(this.stackAmount, false);
        }

    }

}
