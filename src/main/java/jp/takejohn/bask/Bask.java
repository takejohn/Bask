package jp.takejohn.bask;

import ch.njol.skript.Skript;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bask extends JavaPlugin {

    @Override
    public void onEnable() {
        Skript.registerAddon(this);
    }

}
