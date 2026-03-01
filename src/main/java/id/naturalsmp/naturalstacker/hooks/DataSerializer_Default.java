package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;
import id.naturalsmp.naturalstacker.api.objects.StackedEntity;
import id.naturalsmp.naturalstacker.api.objects.StackedItem;
import id.naturalsmp.naturalstacker.utils.threads.Executor;

public final class DataSerializer_Default implements IDataSerializer {

    private final NaturalStackerPlugin plugin;

    public DataSerializer_Default(NaturalStackerPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void saveEntity(StackedEntity stackedEntity) {
        Executor.sync(() -> plugin.getNMSAdapter().saveEntity(stackedEntity));
    }

    @Override
    public void loadEntity(StackedEntity stackedEntity) {
        Executor.sync(() -> plugin.getNMSAdapter().loadEntity(stackedEntity));
    }

    @Override
    public void saveItem(StackedItem stackedItem) {
        plugin.getNMSAdapter().saveItem(stackedItem);
    }

    @Override
    public void loadItem(StackedItem stackedItem) {
        plugin.getNMSAdapter().loadItem(stackedItem);
    }

}
