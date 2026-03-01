package id.naturalsmp.naturalstacker.hooks;

import id.naturalsmp.naturalstacker.api.objects.StackedEntity;

import javax.annotation.Nullable;

public interface EntityNameProvider {

    @Nullable
    String getCustomName(StackedEntity stackedEntity);

}
