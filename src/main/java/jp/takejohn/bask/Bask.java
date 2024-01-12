package jp.takejohn.bask;

import ch.njol.skript.Skript;
import jp.takejohn.bask.elements.ClassRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bask extends JavaPlugin {

    private static Bask instance;

    @Override
    public void onEnable() {
        setInstance(this);
        Skript.registerAddon(this);
        ClassRegistry.load();
    }

    private static void setInstance(Bask instance) {
        Bask.instance = instance;
    }

    public static Bask getInstance() {
        return instance;
    }

}
