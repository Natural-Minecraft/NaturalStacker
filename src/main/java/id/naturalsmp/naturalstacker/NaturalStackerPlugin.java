package id.naturalsmp.naturalstacker;

import com.bgsoftware.common.dependencies.DependenciesManager;
import com.bgsoftware.common.nmsloader.INMSLoader;
import com.bgsoftware.common.nmsloader.NMSHandlersFactory;
import com.bgsoftware.common.nmsloader.NMSLoadException;
import com.bgsoftware.common.nmsloader.config.NMSConfiguration;
import com.bgsoftware.common.updater.Updater;
import id.naturalsmp.naturalstacker.api.NaturalStacker;
import id.naturalsmp.naturalstacker.api.NaturalStackerAPI;
import id.naturalsmp.naturalstacker.command.CommandsHandler;
import id.naturalsmp.naturalstacker.handlers.DataHandler;
import id.naturalsmp.naturalstacker.handlers.LootHandler;
import id.naturalsmp.naturalstacker.handlers.ProvidersHandler;
import id.naturalsmp.naturalstacker.handlers.SettingsHandler;
import id.naturalsmp.naturalstacker.handlers.SystemHandler;
import id.naturalsmp.naturalstacker.handlers.UpgradesHandler;
import id.naturalsmp.naturalstacker.listeners.BarrelsListener;
import id.naturalsmp.naturalstacker.listeners.BucketsListener;
import id.naturalsmp.naturalstacker.listeners.ChunksListener;
import id.naturalsmp.naturalstacker.listeners.EntitiesListener;
import id.naturalsmp.naturalstacker.listeners.ItemsListener;
import id.naturalsmp.naturalstacker.listeners.MenusListener;
import id.naturalsmp.naturalstacker.listeners.NoClaimConflictListener;
import id.naturalsmp.naturalstacker.listeners.PickupItemListener;
import id.naturalsmp.naturalstacker.listeners.PlayersListener;
import id.naturalsmp.naturalstacker.listeners.ServerTickListener;
import id.naturalsmp.naturalstacker.listeners.ShulkerOversizedPatch;
import id.naturalsmp.naturalstacker.listeners.SpawnersListener;
import id.naturalsmp.naturalstacker.listeners.StewListener;
import id.naturalsmp.naturalstacker.listeners.ToolsListener;
import id.naturalsmp.naturalstacker.listeners.WorldsListener;
import id.naturalsmp.naturalstacker.listeners.events.EventsListener;
import id.naturalsmp.naturalstacker.nms.NMSAdapter;
import id.naturalsmp.naturalstacker.nms.NMSEntities;
import id.naturalsmp.naturalstacker.nms.NMSHolograms;
import id.naturalsmp.naturalstacker.nms.NMSSpawners;
import id.naturalsmp.naturalstacker.nms.NMSWorld;
import id.naturalsmp.naturalstacker.utils.ServerVersion;
import id.naturalsmp.naturalstacker.utils.entity.EntityStorage;
import id.naturalsmp.naturalstacker.utils.entity.logic.DeathSimulation;
import id.naturalsmp.naturalstacker.utils.threads.Executor;
import id.naturalsmp.naturalstacker.utils.threads.StackService;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class NaturalStackerPlugin extends JavaPlugin implements NaturalStacker {

    private final Updater updater = new Updater(this, "naturalstacker");

    private static NaturalStackerPlugin plugin;

    private SettingsHandler settingsHandler;
    private SystemHandler systemManager;
    private UpgradesHandler upgradesHandler;
    private DataHandler dataHandler;
    private ProvidersHandler providersHandler;
    private LootHandler lootHandler;

    private NMSAdapter nmsAdapter;
    private NMSHolograms nmsHolograms;
    private NMSSpawners nmsSpawners;
    private NMSEntities nmsEntities;
    private NMSWorld nmsWorld;

    private boolean shouldEnable = true;

    public static void log(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        if (message.contains(ChatColor.COLOR_CHAR + "")) {
            Bukkit.getConsoleSender().sendMessage("[NaturalStacker] " + message);
        } else {
            plugin.getLogger().info(message);
        }
    }

    public static NaturalStackerPlugin getPlugin() {
        return plugin;
    }

    @Override
    public void onLoad() {
        plugin = this;

        DependenciesManager.inject(this);
        DeathSimulation.injectEntityDamageHandlerList();
        PickupItemListener.injectHandlerLists();

        // Setting the default locale to English will fix issues related to using upper
        // case in Turkish.
        // https://stackoverflow.com/questions/11063102/using-locales-with-javas-tolowercase-and-touppercase
        java.util.Locale.setDefault(java.util.Locale.ENGLISH);

        new Metrics(this, 4105);
        
        loadAPI();

        this.shouldEnable = loadNMSAdapter();
        if (this.shouldEnable) {
            this.nmsAdapter.loadLegacy();
        } else {
            log("&cThere was an error while loading the plugin.");
        }
    }

    @Override
    public void onDisable() {
        log("Cancelling tasks...");

        try {
            Bukkit.getScheduler().cancelAllTasks();
        } catch (Throwable ex) {
            Bukkit.getScheduler().cancelTasks(this);
        }

        log("Shutting down stacking service...");

        StackService.stop();

        if (shouldEnable) {
            log("Performing entity&items save");

            for (World world : Bukkit.getWorlds()) {
                for (Chunk chunk : world.getLoadedChunks())
                    systemManager.handleChunkUnload(chunk, SystemHandler.CHUNK_FULL_STAGE);
            }

            // We need to save the entire database
            systemManager.performCacheSave();

            Executor.stopData();

            log("Clearing database...");
            // We need to close the connection
            dataHandler.clearDatabase();
        }

        log("Stopping executor...");

        Executor.stop();

        EntityStorage.clearCache();
    }

    @Override
    public void onEnable() {
        if (!shouldEnable) {
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        log("******** ENABLE START ********");

        dataHandler = new DataHandler(this);
        systemManager = new SystemHandler(this);
        upgradesHandler = new UpgradesHandler();
        settingsHandler = new SettingsHandler(this);
        providersHandler = new ProvidersHandler(this);
        lootHandler = new LootHandler(this);

        Locale.reload();

        if (ServerVersion.isAtLeast(ServerVersion.v1_8))
            getServer().getPluginManager().registerEvents(new BarrelsListener(this), this);
        getServer().getPluginManager().registerEvents(new BucketsListener(this), this);
        getServer().getPluginManager().registerEvents(new ChunksListener(this), this);
        getServer().getPluginManager().registerEvents(new EntitiesListener(this), this);
        getServer().getPluginManager().registerEvents(new ItemsListener(this), this);
        getServer().getPluginManager().registerEvents(new MenusListener(), this);
        getServer().getPluginManager().registerEvents(new NoClaimConflictListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayersListener(this), this);
        getServer().getPluginManager().registerEvents(new ShulkerOversizedPatch(), this);
        getServer().getPluginManager().registerEvents(new SpawnersListener(this), this);
        getServer().getPluginManager().registerEvents(new StewListener(this), this);
        getServer().getPluginManager().registerEvents(new ToolsListener(this), this);
        getServer().getPluginManager().registerEvents(new WorldsListener(this), this);

        try {
            Class.forName("com.destroystokyo.paper.event.server.ServerTickEndEvent");
            getServer().getPluginManager().registerEvents(new ServerTickListener(), this);
        } catch (Throwable ignored) {
        }

        EventsListener.register(this);

        CommandsHandler commandsHandler = new CommandsHandler(this);
        getCommand("stacker").setExecutor(commandsHandler);
        getCommand("stacker").setTabCompleter(commandsHandler);

        if (updater.isOutdated()) {
            log("");
            log("A new version is available (v" + updater.getLatestVersion() + ")!");
            log("Version's description: \"" + updater.getVersionDescription() + "\"");
            log("");
        }

        log("******** ENABLE DONE ********");
    }

    private void loadAPI() {
        try {
            NaturalStackerAPI.setPluginInstance(this);
        } catch (UnsupportedOperationException ex) {
            log("Failed to set-up API - disabling plugin...");
            ex.printStackTrace();
            shouldEnable = false;
        }
    }

    private boolean loadNMSAdapter() {
        NaturalNMSConfiguration nmsConfig = new NaturalNMSConfiguration(this);

        // Detect server NMS version
        String nmsVersion = NaturalNMSConfiguration.detectNMSVersion();
        if (nmsVersion == null) {
            log("&cCould not detect NMS version from server. Is this a supported Minecraft version?");
            return false;
        }
        log("Detected NMS version: " + nmsVersion);

        // Primary approach: Direct class loading (bypasses NMSHandlersFactory resource checks)
        try {
            log("Loading NMS handlers directly for version " + nmsVersion + "...");

            this.nmsAdapter = nmsConfig.loadNMSHandlerDirect(NMSAdapter.class, nmsVersion);
            this.nmsEntities = nmsConfig.loadNMSHandlerDirect(NMSEntities.class, nmsVersion);
            this.nmsHolograms = nmsConfig.loadNMSHandlerDirect(NMSHolograms.class, nmsVersion);
            this.nmsSpawners = nmsConfig.loadNMSHandlerDirect(NMSSpawners.class, nmsVersion);
            this.nmsWorld = nmsConfig.loadNMSHandlerDirect(NMSWorld.class, nmsVersion);

            log("&aAll NMS handlers loaded successfully via direct loading!");
            return true;
        } catch (NMSLoadException directError) {
            log("&eDirect NMS loading failed: " + directError.getMessage());
            log("&eFalling back to NMSHandlersFactory...");
        }

        // Fallback: Try NMSHandlersFactory (original approach)
        try {
            INMSLoader nmsLoader = NMSHandlersFactory.createNMSLoader(this, nmsConfig);
            this.nmsAdapter = nmsLoader.loadNMSHandler(NMSAdapter.class);
            this.nmsEntities = nmsLoader.loadNMSHandler(NMSEntities.class);
            this.nmsHolograms = nmsLoader.loadNMSHandler(NMSHolograms.class);
            this.nmsSpawners = nmsLoader.loadNMSHandler(NMSSpawners.class);
            this.nmsWorld = nmsLoader.loadNMSHandler(NMSWorld.class);

            log("&aAll NMS handlers loaded successfully via NMSHandlersFactory!");
            return true;
        } catch (NMSLoadException factoryError) {
            log("&cNMSHandlersFactory also failed: " + factoryError.getMessage());
            log("&cThe plugin doesn't support your minecraft version.");
            log("&cPlease try a different version.");
            factoryError.printStackTrace();
            return false;
        }
    }



    public NMSAdapter getNMSAdapter() {
        return nmsAdapter;
    }

    public NMSHolograms getNMSHolograms() {
        return nmsHolograms;
    }

    public NMSSpawners getNMSSpawners() {
        return nmsSpawners;
    }

    public NMSEntities getNMSEntities() {
        return nmsEntities;
    }

    public NMSWorld getNMSWorld() {
        return nmsWorld;
    }

    public LootHandler getLootHandler() {
        return lootHandler;
    }

    public void setLootHandler(LootHandler lootHandler) {
        this.lootHandler = lootHandler;
    }

    public ProvidersHandler getProviders() {
        return providersHandler;
    }

    public DataHandler getDataHandler() {
        return dataHandler;
    }

    @Override
    public SystemHandler getSystemManager() {
        return systemManager;
    }

    @Override
    public UpgradesHandler getUpgradesManager() {
        return upgradesHandler;
    }

    public SettingsHandler getSettings() {
        return settingsHandler;
    }

    public void setSettings(SettingsHandler settingsHandler) {
        this.settingsHandler = settingsHandler;
    }

    public Updater getUpdater() {
        return updater;
    }

}
