package id.naturalsmp.naturalstacker.hooks.listeners;

import id.naturalsmp.naturalstacker.api.objects.StackedEntity;

public interface IEntityDeathListener {

    void handleDeath(StackedEntity stackedEntity, Type type);

    enum Type {

        BEFORE_DEATH_EVENT,
        AFTER_DEATH_EVENT

    }

}
