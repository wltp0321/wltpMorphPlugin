package world.wltp.wltpmorphplugin;

import org.bukkit.plugin.java.JavaPlugin;
import world.wltp.wltpmorphplugin.commands.MorphCommand;
import world.wltp.wltpmorphplugin.commands.UnmorphCommand;

public class Main extends JavaPlugin {

    private MorphManager morphManager;

    @Override
    public void onEnable() {
        this.morphManager = new MorphManager(this);
        getCommand("morph").setExecutor(new MorphCommand(this));
        getCommand("unmorph").setExecutor(new UnmorphCommand(morphManager));
    }

    public MorphManager getMorphManager() {
        return morphManager;
    }
}
