package de.fredo121.runner.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit; 

import de.fredo121.lobby.main.LobbyAPI;
import de.fredo121.lobby.main.LobbyEventHandler;
import de.fredo121.runner.main.Runner;
import de.fredo121.runner.main.Util;

public class Game implements LobbyEventHandler {
	private GameState gameState = GameState.LOBBY;
	private static Game instance; 
	private List<GamePlayer> ingamePlayers = new ArrayList<GamePlayer>(); 
	private List<GamePlayer> playerDeathOrder = new ArrayList<GamePlayer>(); 
	
	public Game(){
		LobbyAPI.subscribe(this); 
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public static Game get(){
		if(instance == null){
			instance = new Game(); 
		}
		return instance; 
	}

	public void onPlayerDie(GamePlayer gamePlayer){ 
		playerDeathOrder.add(gamePlayer);  
		gamePlayer.setDead(); 
		gamePlayer.getPlayer().setGameMode(GameMode.SPECTATOR); 
		gamePlayer.getPlayer().teleport(GameSettings.get().getSpectatorLocation()); 
		if(getAliveGamePlayers().size() <= 1){ 
			playerDeathOrder.add(getAliveGamePlayers().get(0)); 
			finishGame(); 
		}
	}
	
	public List<GamePlayer> getDeadGamePlayers(){
		List<GamePlayer> list = new ArrayList<GamePlayer>(); 
		for(GamePlayer gamePlayer : this.ingamePlayers){ 
			if(gamePlayer.getDead()){
				list.add(gamePlayer); 
			}
		} 
		return list; 
	}
	
	public List<GamePlayer> getAliveGamePlayers(){
		List<GamePlayer> list = new ArrayList<GamePlayer>(); 
		for(GamePlayer gamePlayer : this.ingamePlayers){ 
			if(!gamePlayer.getDead()){
				list.add(gamePlayer); 
			}
		} 
		return list; 
	}
	
	public void removeGamePlayer(Player player){ 
		GamePlayer gamePlayer = null; 
		for(GamePlayer p : ingamePlayers){
			if(p.getPlayer() == player){
				gamePlayer = p; 
			} 
		} 
		if(gamePlayer != null){
			ingamePlayers.remove(gamePlayer); 
		}
	}
	
	public GamePlayer getGamePlayer(Player player){
		for(GamePlayer p : ingamePlayers){
			if(p.getPlayer() == player){
				return p; 
			} 
		} 
		return null; 
	}
	
	private void spawnPlayers(){
		Location[] spawnLocations = GameSettings.get().getSpawnLocations().toArray(new Location[GameSettings.get().getSpawnLocations().size()]); 
		int n=0; 
		for(GamePlayer p : ingamePlayers){ 
			p.getPlayer().teleport(spawnLocations[n%spawnLocations.length]); 
			n++; 
		}
	} 
	
	public void StartGame(List<Player> lobbyPlayers) { 
		for(Player p : lobbyPlayers){
			ingamePlayers.add(new GamePlayer(p)); 
		}
		
		gameState = GameState.PREPARATION; 
		spawnPlayers(); 
		Bukkit.broadcastMessage(ChatColor.GOLD+"Preparing "+ChatColor.RED+GameSettings.get().getGameCountdown()+ChatColor.GOLD+" seconds. ");  
		Util.displayCountdown(GameSettings.get().getGameCountdown(), GameSettings.get().getGameCountdown(), "Prepare: "+ChatColor.RED); 
		new BukkitRunnable() {
			
			public void run() {
				endPrepare(); 
			}
		}.runTaskLater(Runner.get(), GameSettings.get().getGameCountdown()*20L); 
	} 
	
	private void endPrepare(){
		gameState = GameState.RUNNING; 
		Bukkit.broadcastMessage(ChatColor.GOLD+"Game started!!!"); 
	} 
	
	private void finishGame(){ 
		gameState = GameState.FINISHED; 
		int n=playerDeathOrder.size(); 
		Bukkit.broadcastMessage(ChatColor.GOLD+"============="+ChatColor.RED+"Ranking"+ChatColor.GOLD+"============");
		for(GamePlayer gamePlayer : playerDeathOrder){
			Bukkit.broadcastMessage(ChatColor.RED+"        "+n+""+ChatColor.GOLD+". "+gamePlayer.getPlayer().getName());
			n--; 
		}
		Bukkit.broadcastMessage(ChatColor.GOLD+"================================"); 
	} 
} 
