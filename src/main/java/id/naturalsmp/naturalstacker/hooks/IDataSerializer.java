package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.api.objects.StackedEntity;
import id.naturalsmp.naturalstacker.api.objects.StackedItem;

public interface IDataSerializer {

    void saveEntity(StackedEntity stackedEntity);

    void loadEntity(StackedEntity stackedEntity);

    void saveItem(StackedItem stackedItem);

    void loadItem(StackedItem stackedItem);

}
