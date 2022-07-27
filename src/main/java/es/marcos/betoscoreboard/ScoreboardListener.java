package es.marcos.betoscoreboard;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scoreboard.*;
import java.util.HashMap;
import java.util.UUID;

public class ScoreboardListener implements Listener {

    BetoScoreboard instance;
    private HashMap<UUID, GameMode> playerGamemode = new HashMap<>();

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
            Score online = objective.getScore(ChatColor.WHITE.toString() + ChatColor.BOLD + "Conectados");
            Score areIn = objective.getScore(ChatColor.WHITE.toString() + ChatColor.BOLD + "Estas en");

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

            Team gameMode = scoreboard.registerNewTeam("gamemode");
            gameMode.addEntry(ChatColor.RED.toString());
            switch (e.getPlayer().getGameMode()){
                case CREATIVE:
                    gameMode.setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + "CREATIVO");
                    break;
                case SURVIVAL:
                    gameMode.setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + "SUPERVIVENCIA"); break;
                case SPECTATOR:
                    gameMode.setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + "ESPECTADOR"); break;
                case ADVENTURE:
                    gameMode.setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + "AVENTURA"); break;
                default:
                    break;
            }
            playerGamemode.put(e.getPlayer().getUniqueId(), e.getPlayer().getGameMode());


            Score currentGamemode = objective.getScore(ChatColor.RED.toString());
            Score playersOnline = objective.getScore(ChatColor.BLACK.toString());
            Score moneyBalance = objective.getScore(ChatColor.WHITE.toString());


            name.setScore(11);
            stripes.setScore(10);
            space.setScore(9);
            cash.setScore(8);
            moneyBalance.setScore(7);
            space2.setScore(6);
            areIn.setScore(5);
            currentGamemode.setScore(4);
            space.setScore(3);
            online.setScore(2);
            playersOnline.setScore(1);

            player.setScoreboard(scoreboard);


            BukkitScheduler scheduler = Bukkit.getScheduler();
            scheduler.runTaskTimer(instance, () -> {
                for(Player p : Bukkit.getOnlinePlayers()){
                    updateScoreboard(p);
                }
            }, 600, 600);
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

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent e){
        Player player = e.getPlayer();

        playerGamemode.put(player.getUniqueId(), e.getNewGameMode());

        Scoreboard scoreboard = player.getScoreboard();
        switch (playerGamemode.get(e.getPlayer().getUniqueId())){
            case CREATIVE:
                scoreboard.getTeam("gamemode").setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + "CREATIVO");
                break;
            case SURVIVAL:
                scoreboard.getTeam("gamemode").setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + "SUPERVIVENCIA"); break;
            case SPECTATOR:
                scoreboard.getTeam("gamemode").setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + "ESPECTADOR"); break;
            case ADVENTURE:
                scoreboard.getTeam("gamemode").setPrefix(ChatColor.GOLD.toString() + ChatColor.BOLD + "AVENTURA"); break;
            default:
                break;
        }
    }
}
