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
        message = id.naturalsmp.naturalstacker.utils.GeneralUtils.color(message);
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

    // All known NMS versions, latest first, for fallback scanning
    private static final String[] KNOWN_NMS_VERSIONS = {
        "v1_21_10", "v1_21_9", "v1_21_7", "v1_21_5", "v1_21_4", "v1_21_3",
        "v1_21", "v1_20_4", "v1_20_3", "v1_19", "v1_18", "v1_17",
        "v1_16_R3", "v1_12_R1", "v1_8_R3", "v1_7_R4"
    };

    private boolean loadNMSAdapter() {
        NaturalNMSConfiguration nmsConfig = new NaturalNMSConfiguration(this);

        // Detect server NMS version
        String detectedVersion = NaturalNMSConfiguration.detectNMSVersion();
        log("Detected NMS version: " + (detectedVersion != null ? detectedVersion : "unknown"));

        // Build list of versions to try: detected version first, then all others
        java.util.List<String> versionsToTry = new java.util.ArrayList<>();
        if (detectedVersion != null) {
            versionsToTry.add(detectedVersion);
        }
        for (String v : KNOWN_NMS_VERSIONS) {
            if (!v.equals(detectedVersion)) {
                versionsToTry.add(v);
            }
        }

        // Try each version via direct class loading
        for (String nmsVersion : versionsToTry) {
            try {
                // Quick check: does the main adapter class exist?
                Class.forName("id.naturalsmp.naturalstacker.nms." + nmsVersion + ".NMSAdapterImpl");

                log("Found NMS module for version " + nmsVersion + ", loading handlers...");

                this.nmsAdapter = nmsConfig.loadNMSHandlerDirect(NMSAdapter.class, nmsVersion);
                this.nmsEntities = nmsConfig.loadNMSHandlerDirect(NMSEntities.class, nmsVersion);
                this.nmsHolograms = nmsConfig.loadNMSHandlerDirect(NMSHolograms.class, nmsVersion);
                this.nmsSpawners = nmsConfig.loadNMSHandlerDirect(NMSSpawners.class, nmsVersion);
                this.nmsWorld = nmsConfig.loadNMSHandlerDirect(NMSWorld.class, nmsVersion);

                log("&aAll NMS handlers loaded successfully for version " + nmsVersion + "!");
                return true;
            } catch (ClassNotFoundException ignored) {
                // This version's NMS module is not available, try next
            } catch (Throwable error) {
                log("&eNMS version " + nmsVersion + " found but failed to load: " + error.getMessage());
                error.printStackTrace();
            }
        }

        log("&cNo compatible NMS module found. Tried versions: " + versionsToTry);
        log("&cThe plugin doesn't support your minecraft version.");
        return false;
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
