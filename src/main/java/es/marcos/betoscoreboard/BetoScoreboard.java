package es.marcos.betoscoreboard;

import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import net.milkbowl.vault.economy.Economy;

public final class BetoScoreboard extends JavaPlugin {

    //private boolean blank = true;
    private static Economy econ = null;
    ScoreboardListener listener = new ScoreboardListener(this);
    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this.listener, this);

        if(!setupEconomy()){
            System.out.println("No economy plugin found. Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    public static Economy getEconomy(){
        return econ;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
