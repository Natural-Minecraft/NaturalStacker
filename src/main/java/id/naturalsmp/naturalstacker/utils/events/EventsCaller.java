package id.naturalsmp.naturalstacker.utils.events;

import id.naturalsmp.naturalstacker.api.events.BarrelDropEvent;
import id.naturalsmp.naturalstacker.api.events.BarrelPlaceEvent;
import id.naturalsmp.naturalstacker.api.events.BarrelPlaceInventoryEvent;
import id.naturalsmp.naturalstacker.api.events.BarrelStackEvent;
import id.naturalsmp.naturalstacker.api.events.BarrelUnstackEvent;
import id.naturalsmp.naturalstacker.api.events.DuplicateSpawnEvent;
import id.naturalsmp.naturalstacker.api.events.EntityStackEvent;
import id.naturalsmp.naturalstacker.api.events.EntityUnstackEvent;
import id.naturalsmp.naturalstacker.api.events.ItemStackEvent;
import id.naturalsmp.naturalstacker.api.events.SpawnerDropEvent;
import id.naturalsmp.naturalstacker.api.events.SpawnerPlaceEvent;
import id.naturalsmp.naturalstacker.api.events.SpawnerPlaceInventoryEvent;
import id.naturalsmp.naturalstacker.api.events.SpawnerStackEvent;
import id.naturalsmp.naturalstacker.api.events.SpawnerStackedEntitySpawnEvent;
import id.naturalsmp.naturalstacker.api.events.SpawnerUnstackEvent;
import id.naturalsmp.naturalstacker.api.events.SpawnerUpgradeEvent;
import id.naturalsmp.naturalstacker.api.objects.StackedBarrel;
import id.naturalsmp.naturalstacker.api.objects.StackedEntity;
import id.naturalsmp.naturalstacker.api.objects.StackedItem;
import id.naturalsmp.naturalstacker.api.objects.StackedSpawner;
import id.naturalsmp.naturalstacker.api.upgrades.SpawnerUpgrade;
import id.naturalsmp.naturalstacker.utils.pair.Pair;
import org.bukkit.Bukkit;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public final class EventsCaller {

    private EventsCaller() {

    }

    public static ItemStack callBarrelDropEvent(StackedBarrel stackedBarrel, Player player, int amount) {
        BarrelDropEvent barrelDropEvent = new BarrelDropEvent(stackedBarrel, player, stackedBarrel.getBarrelItem(amount));
        Bukkit.getPluginManager().callEvent(barrelDropEvent);
        return barrelDropEvent.getItemStack();
    }

    public static boolean callBarrelPlaceEvent(Player player, StackedBarrel stackedBarrel, ItemStack inHand) {
        BarrelPlaceEvent barrelPlaceEvent = new BarrelPlaceEvent(player, stackedBarrel, inHand);
        Bukkit.getPluginManager().callEvent(barrelPlaceEvent);
        return !barrelPlaceEvent.isCancelled();
    }

    public static boolean callBarrelPlaceInventoryEvent(Player player, StackedBarrel stackedBarrel, int increaseAmount) {
        BarrelPlaceInventoryEvent barrelPlaceInventoryEvent = new BarrelPlaceInventoryEvent(player, stackedBarrel, increaseAmount);
        Bukkit.getPluginManager().callEvent(barrelPlaceInventoryEvent);
        return !barrelPlaceInventoryEvent.isCancelled();
    }

    public static boolean callBarrelStackEvent(StackedBarrel barrel, StackedBarrel target) {
        BarrelStackEvent barrelStackEvent = new BarrelStackEvent(barrel, target);
        Bukkit.getPluginManager().callEvent(barrelStackEvent);
        return !barrelStackEvent.isCancelled();
    }

    public static boolean callBarrelUnstackEvent(StackedBarrel barrel, Entity unstackSource, int unstackAmount) {
        BarrelUnstackEvent barrelUnstackEvent = new BarrelUnstackEvent(barrel, unstackSource, unstackAmount);
        Bukkit.getPluginManager().callEvent(barrelUnstackEvent);
        return !barrelUnstackEvent.isCancelled();
    }

    public static void callDuplicateSpawnEvent(StackedEntity stackedEntity, StackedEntity duplicate) {
        DuplicateSpawnEvent duplicateSpawnEvent = new DuplicateSpawnEvent(stackedEntity, duplicate);
        Bukkit.getPluginManager().callEvent(duplicateSpawnEvent);
    }

    public static boolean callEntityStackEvent(StackedEntity entity, StackedEntity target) {
        EntityStackEvent entityStackEvent = new EntityStackEvent(entity, target);
        Bukkit.getPluginManager().callEvent(entityStackEvent);
        return !entityStackEvent.isCancelled();
    }

    public static Pair<Boolean, Integer> callEntityUnstackEvent(StackedEntity entity, Entity unstackSource, int unstackAmount) {
        EntityUnstackEvent entityUnstackEvent = new EntityUnstackEvent(entity, unstackSource, unstackAmount);
        Bukkit.getPluginManager().callEvent(entityUnstackEvent);
        return new Pair<>(!entityUnstackEvent.isCancelled(), entityUnstackEvent.getAmount());
    }

    public static boolean callItemStackEvent(StackedItem item, StackedItem target) {
        ItemStackEvent itemStackEvent = new ItemStackEvent(item, target);
        Bukkit.getPluginManager().callEvent(itemStackEvent);
        return !itemStackEvent.isCancelled();
    }

    public static ItemStack callSpawnerDropEvent(StackedSpawner stackedSpawner, Player player, int amount) {
        SpawnerDropEvent spawnerDropEvent = new SpawnerDropEvent(stackedSpawner, player, stackedSpawner.getDropItem(amount));
        Bukkit.getPluginManager().callEvent(spawnerDropEvent);
        return spawnerDropEvent.getItemStack();
    }

    public static boolean callSpawnerPlaceEvent(Player player, StackedSpawner stackedSpawner, ItemStack inHand) {
        SpawnerPlaceEvent spawnerPlaceEvent = new SpawnerPlaceEvent(player, stackedSpawner, inHand);
        Bukkit.getPluginManager().callEvent(spawnerPlaceEvent);
        return !spawnerPlaceEvent.isCancelled();
    }

    public static boolean callSpawnerPlaceInventoryEvent(Player player, StackedSpawner stackedSpawner, int increaseAmount) {
        SpawnerPlaceInventoryEvent spawnerPlaceInventoryEvent = new SpawnerPlaceInventoryEvent(player, stackedSpawner, increaseAmount);
        Bukkit.getPluginManager().callEvent(spawnerPlaceInventoryEvent);
        return !spawnerPlaceInventoryEvent.isCancelled();
    }

    public static boolean callSpawnerStackEvent(StackedSpawner spawner, StackedSpawner target) {
        SpawnerStackEvent spawnerStackEvent = new SpawnerStackEvent(spawner, target);
        Bukkit.getPluginManager().callEvent(spawnerStackEvent);
        return !spawnerStackEvent.isCancelled();
    }

    public static boolean callSpawnerUnstackEvent(StackedSpawner spawner, Entity unstackSource, int unstackAmount) {
        SpawnerUnstackEvent spawnerUnstackEvent = new SpawnerUnstackEvent(spawner, unstackSource, unstackAmount);
        Bukkit.getPluginManager().callEvent(spawnerUnstackEvent);
        return !spawnerUnstackEvent.isCancelled();
    }

    public static void callSpawnerUpgradeEvent(StackedSpawner stackedSpawner, SpawnerUpgrade spawnerUpgrade, Player who) {
        SpawnerUpgradeEvent spawnerUpgradeEvent = new SpawnerUpgradeEvent(stackedSpawner, spawnerUpgrade, who);
        Bukkit.getPluginManager().callEvent(spawnerUpgradeEvent);
    }

    public static boolean callSpawnerStackedEntitySpawnEvent(CreatureSpawner creatureSpawner) {
        SpawnerStackedEntitySpawnEvent spawnerStackedEntitySpawnEvent = new SpawnerStackedEntitySpawnEvent(creatureSpawner);
        Bukkit.getPluginManager().callEvent(spawnerStackedEntitySpawnEvent);
        return spawnerStackedEntitySpawnEvent.shouldBeStacked();
    }

}
