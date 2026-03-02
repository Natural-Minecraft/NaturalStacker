package id.naturalsmp.naturalstacker;

import com.bgsoftware.common.nmsloader.config.NMSConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Custom NMSConfiguration that bypasses the com.bgsoftware package check
 * in the original PluginNMSConfiguration.
 *
 * After relocation (com.bgsoftware -> id.naturalsmp), the format strings
 * in PluginNMSConfiguration become "id/naturalsmp/%s/nms/%s" and
 * "id.naturalsmp.%s.nms.%s.%s", so we use "naturalstacker" as the
 * plugin package segment to construct correct paths.
 */
public class NaturalNMSConfiguration extends NMSConfiguration {

    private final File cacheFolder;

    public NaturalNMSConfiguration(JavaPlugin plugin) {
        this.cacheFolder = new File(plugin.getDataFolder(), ".cache");
        if (!this.cacheFolder.exists()) {
            this.cacheFolder.mkdirs();
        }
    }

    @Override
    public String getNMSResourcePathForVersion(String nmsVersionName) {
        // After relocation, classes are at id/naturalsmp/naturalstacker/nms/{version}/
        return String.format("id/naturalsmp/naturalstacker/nms/%s", nmsVersionName);
    }

    @Override
    public String getPackagePathForNMSHandler(String nmsVersionName, String handlerName) {
        // After relocation, class names are id.naturalsmp.naturalstacker.nms.{version}.{handler}Impl
        return String.format("id.naturalsmp.naturalstacker.nms.%s.%sImpl", nmsVersionName, handlerName);
    }

    @Override
    public File getCacheFolder() {
        return this.cacheFolder;
    }
}
