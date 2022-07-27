package es.marcos.betoscoreboard;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;

public class ScoreboardListener implements Listener {

    BetoScoreboard instance;

    //This instance is required so that we can access to config.yml and disable scoreboard if wanted
    ScoreboardListener(BetoScoreboard instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (instance.getConfig().getBoolean("enable-scoreboard")) {
            Economy economy = BetoScoreboard.getEconomy();
            Player player = e.getPlayer();
            Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective objective =
                    scoreboard.registerNewObjective("BetoLand", "dummy");
            objective.setDisplaySlot(DisplaySlot.SIDEBAR);
            objective.setDisplayName(ChatColor.GREEN.toString() + ChatColor.BOLD + "BetoLand #1");
            Score name = objective.getScore(ChatColor.WHITE.toString() + ChatColor.BOLD + e.getPlayer().getPlayerListName());
            Score space = objective.getScore(" ");
            Score space2 = objective.getScore("  ");
            Score space3 = objective.getScore("   ");
            Score cash = objective.getScore(ChatColor.WHITE.toString() + ChatColor.BOLD + "ORO");
            Score stripes = objective.getScore(ChatColor.GOLD.toString() + ChatColor.BOLD + "==============");
            Score online = objective.getScore(ChatColor.WHITE.toString() + ChatColor.BOLD + "CONECTADOS");

            //Dynamic real time data requires creating a team
            //Datos dinámicos en tiempo real requieren crear un Team
            Team onlinePlayerCounter = scoreboard.registerNewTeam("onlinecounter");
            onlinePlayerCounter.addEntry(ChatColor.BLACK.toString());
            if (Bukkit.getOnlinePlayers().size() == 0) {
                onlinePlayerCounter.setPrefix("0");
            } else {
                onlinePlayerCounter.setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + Bukkit.getOnlinePlayers().size());
            }

            Team cashBalance = scoreboard.registerNewTeam("cashbalance");
            cashBalance.addEntry(ChatColor.WHITE.toString());
            if (economy.getBalance(e.getPlayer()) == 0){
                cashBalance.setPrefix("0");
            } else {
                cashBalance.setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + economy.getBalance(e.getPlayer()));
            }
            Score playersOnline = objective.getScore(ChatColor.BLACK.toString());
            Score moneyBalance = objective.getScore(ChatColor.WHITE.toString());


            name.setScore(8);
            stripes.setScore(7);
            space.setScore(6);
            cash.setScore(5);
            moneyBalance.setScore(4);
            space2.setScore(3);
            online.setScore(2);
            playersOnline.setScore(1);

            player.setScoreboard(scoreboard);

            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.runTaskTimer(instance, () -> {
                for(Player p : Bukkit.getOnlinePlayers()){
                    updateScoreboard(p);
                }
            }, 1200, 1200);
        }
    }

    public void updateScoreboard(Player player){
        Scoreboard scoreboard = player.getScoreboard();
        Economy economy = BetoScoreboard.getEconomy();

        if(Bukkit.getOnlinePlayers().size() == 0){
            scoreboard.getTeam("onlinecounter").setPrefix("0");
        } else{
            scoreboard.getTeam("onlinecounter").setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + Bukkit.getOnlinePlayers().size());
        }

        scoreboard.getTeam("cashbalance").setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + economy.getBalance(player));
    }

}
