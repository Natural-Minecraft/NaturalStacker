package id.naturalsmp.naturalstacker.api.objects;

import org.bukkit.inventory.ItemStack;

public interface UnloadedStackedBarrel extends UnloadedStackedObject {

    /**
     * Get the block inside the item as an item-stack.
     *
     * @param amount the amount of the item-stack.
     */
    ItemStack getBarrelItem(int amount);

}
