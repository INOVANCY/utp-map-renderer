package fr.inovancy.main;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class UTPMapRenderer extends JavaPlugin {

    private static UTPMapRenderer instance;

    public static synchronized UTPMapRenderer getInstance(){
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        Bukkit.getLogger().info("UTPMapRender started without encountering any errors!");
        getCommand("utprenderer").setExecutor(new Command());
        saveConfig();
    }

    @Override
    public void onDisable() {
        Bukkit.getLogger().info("UTPMapRender has been successfully deactivated.");
    }

    public WorldEditPlugin getWorldEdit() {
        Plugin p = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit");
        if (p instanceof WorldEditPlugin)
            return (WorldEditPlugin)p;
        return null;
    }
}
