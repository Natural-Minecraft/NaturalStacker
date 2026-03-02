package id.naturalsmp.naturalstacker;

import com.bgsoftware.common.nmsloader.config.NMSConfiguration;
import com.bgsoftware.common.nmsloader.NMSLoadException;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * Custom NMS loading system that bypasses the NMSHandlersFactory entirely.
 * 
 * The original NMSLoader library has a hardcoded com.bgsoftware package check
 * and a resource path check (classLoader.getResource(path)) that fails because
 * jar directory entries may be missing after repackaging. This class directly
 * loads the NMS implementation classes by their known fully-qualified names.
 */
public class NaturalNMSConfiguration extends NMSConfiguration {

    private final JavaPlugin plugin;
    private final File cacheFolder;

    public NaturalNMSConfiguration(JavaPlugin plugin) {
        this.plugin = plugin;
        this.cacheFolder = new File(plugin.getDataFolder(), ".cache");
        if (!this.cacheFolder.exists()) {
            this.cacheFolder.mkdirs();
        }
    }

    @Override
    public String getNMSResourcePathForVersion(String nmsVersionName) {
        return String.format("id/naturalsmp/naturalstacker/nms/%s", nmsVersionName);
    }

    @Override
    public String getPackagePathForNMSHandler(String nmsVersionName, String handlerName) {
        return String.format("id.naturalsmp.naturalstacker.nms.%s.%sImpl", nmsVersionName, handlerName);
    }

    @Override
    public File getCacheFolder() {
        return this.cacheFolder;
    }

    /**
     * Detect the NMS version string from the server's data version.
     * Uses reflection to call getDataVersion() since it may not exist at compile time.
     */
    public static String detectNMSVersion() {
        try {
            // Use reflection to get data version (not available in old Spigot API)
            Object unsafeValues = Bukkit.class.getMethod("getUnsafe").invoke(null);
            Method getDataVersion = unsafeValues.getClass().getMethod("getDataVersion");
            int dataVersion = (int) getDataVersion.invoke(unsafeValues);


            int[][] versionMap = {
                {2730, -1},   // v1_17 start marker
                {2974, 0},    // gap
                {2975, -1},   // v1_18 start marker
                {3336, 0},    // gap
                {3337, -1},   // v1_19 start marker
                {3699, 0},    // gap
                {3700, -1},   // v1_20_3 start marker
                {3839, -1},   // v1_20_4
                {3955, -1},   // v1_21
                {4082, -1},   // v1_21_3
                {4189, -1},   // v1_21_4
                {4325, -1},   // v1_21_5
                {4440, -1},   // v1_21_7
                {4556, -1},   // v1_21_9
                {4671, -1},   // v1_21_10
            };

            String[] versionNames = {
                "v1_17", null, "v1_18", null, "v1_19", null,
                "v1_20_3", "v1_20_4", "v1_21", "v1_21_3", "v1_21_4",
                "v1_21_5", "v1_21_7", "v1_21_9", "v1_21_10"
            };

            for (int i = 0; i < versionMap.length; i++) {
                if (dataVersion <= versionMap[i][0]) {
                    return versionNames[i];
                }
            }

            // If data version is higher than known, try the latest
            return "v1_21_10";
        } catch (Throwable error) {
            return null;
        }
    }

    /**
     * Directly load an NMS handler class, bypassing NMSHandlersFactory and its resource checks.
     * This mimics what BaseNMSLoader.loadNMSClass does.
     */
    @SuppressWarnings("unchecked")
    public <T> T loadNMSHandlerDirect(Class<T> nmsClass, String nmsVersion) throws NMSLoadException {
        String className = String.format("id.naturalsmp.naturalstacker.nms.%s.%sImpl",
                nmsVersion, nmsClass.getSimpleName());

        try {
            Class<?> implClass = Class.forName(className);

            // Try constructor with plugin parameter first (like BaseNMSLoader)
            try {
                Constructor<?> constructor = implClass.getConstructor(plugin.getClass());
                return (T) constructor.newInstance(plugin);
            } catch (NoSuchMethodException e) {
                // Fall back to no-arg constructor
                return (T) implClass.newInstance();
            }
        } catch (ClassNotFoundException error) {
            throw new NMSLoadException("NMS class not found: " + className +
                    ". Make sure the NMS module for " + nmsVersion + " is compiled and included.", error);
        } catch (Throwable error) {
            throw new NMSLoadException("Failed to instantiate NMS handler: " + className, error);
        }
    }
}
