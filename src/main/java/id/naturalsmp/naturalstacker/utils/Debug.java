package id.naturalsmp.naturalstacker.utils;

import id.naturalsmp.naturalstacker.NaturalStackerPlugin;

import java.util.logging.Level;

public class Debug {

    private static final NaturalStackerPlugin plugin = NaturalStackerPlugin.getPlugin();

    private Debug() {

    }

    public static void debug(String clazz, String method, String message) {
        plugin.getLogger().log(Level.INFO, clazz + "::" + method + " " + message);
    }

}
