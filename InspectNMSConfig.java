import com.bgsoftware.common.nmsloader.config.NMSConfiguration;
import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class InspectNMSConfig {
    public static void main(String[] args) {
        System.out.println("Methods:");
        for (Method m : NMSConfiguration.class.getDeclaredMethods()) {
            System.out.println(m.getName() + " (" + m.getReturnType().getSimpleName() + ")");
        }
        System.out.println("\nFields:");
        for (Field f : NMSConfiguration.class.getDeclaredFields()) {
            System.out.println(f.getName() + " (" + f.getType().getSimpleName() + ")");
        }
        
        try {
            Class<?> inner = Class.forName("com.bgsoftware.common.nmsloader.config.NMSConfiguration$PluginNMSConfiguration");
            System.out.println("\nInner Class Fields:");
            for (Field f : inner.getDeclaredFields()) {
                System.out.println(f.getName() + " (" + f.getType().getSimpleName() + ")");
            }
        } catch (Exception e) {}
    }
}
