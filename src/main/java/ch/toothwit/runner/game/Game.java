package ch.toothwit.runner.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Bukkit; 

import ch.toothwit.lobby.main.LobbyAPI;
import ch.toothwit.lobby.main.LobbyEventHandler;
import ch.toothwit.runner.main.Runner;
import ch.toothwit.runner.main.Util;
import ch.toothwit.runner.main.StructureAPI; 
import ch.toothwit.runner.game.GameSettings; 


public class Game implements LobbyEventHandler {
	private GameState gameState; 
	private static Game instance; 
	private List<GamePlayer> ingamePlayers; 
	private List<GamePlayer> playerDeathOrder; 
	
	public void reload(){ 
		this.gameState = GameState.LOBBY;  
		this.ingamePlayers = new ArrayList<GamePlayer>(); 
		this.playerDeathOrder = new ArrayList<GamePlayer>(); 
		Bukkit.getLogger().info("Runner Spiel neu gestartet"); 
		
		try {
			StructureAPI.get().paste(StructureAPI.get().load("arena"), GameSettings.get().getSchematicLocation()); 
		} 
		catch(Exception exception){
			Bukkit.broadcastMessage("Keine Schematic vorhanden!!! "); 
		} 
	}
	
	public Game(){
		this.reload(); 
		LobbyAPI.subscribe(this); 
	}
	
	public GameState getGameState() {
		return this.gameState;
	}
	
	public void setGameState(GameState gameState){
		this.gameState = gameState; 
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
		GamePlayer gamePlayer = getGamePlayer(player); 
		if(gamePlayer != null){
			ingamePlayers.remove(gamePlayer); 
			onPlayerDie(gamePlayer); 
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
		Bukkit.broadcastMessage(ChatColor.GOLD+"Vorbereitung "+ChatColor.RED+GameSettings.get().getGameCountdown()+ChatColor.GOLD+" seconds. ");  
		Util.displayCountdown(GameSettings.get().getGameCountdown(), GameSettings.get().getGameCountdown(), "Prepare "+ChatColor.RED); 
		new BukkitRunnable() {
			
			public void run() {
				endPrepare(); 
			}
		}.runTaskLater(Runner.get(), GameSettings.get().getGameCountdown()*20L); 
	} 
	
	private void endPrepare(){
		gameState = GameState.RUNNING; 
		Bukkit.broadcastMessage(ChatColor.GOLD+"Spiel gestartet!!!"); 
		for(GamePlayer gamePlayer : this.getAliveGamePlayers()){
			new TriggeredBlock(gamePlayer.getPlayer().getLocation().subtract(0d, 1d, 0d)); 
		}
	} 
	
	private void finishGame(){ 
		gameState = GameState.FINISHED; 
		int n=playerDeathOrder.size(); 
		Bukkit.broadcastMessage(ChatColor.GOLD+"============="+ChatColor.RED+"Platzierung"+ChatColor.GOLD+"============");
		for(GamePlayer gamePlayer : playerDeathOrder){
			Bukkit.broadcastMessage(ChatColor.RED+"          "+n+""+ChatColor.GOLD+". "+gamePlayer.getPlayer().getName());
			n--; 
		}
		Bukkit.broadcastMessage(ChatColor.GOLD+"===================================="); 
		Bukkit.broadcastMessage(ChatColor.GOLD+"Wird zur\u00fcck zu Lobby gesendet in "+ChatColor.RED+""+5+ChatColor.GOLD+" Sekunden. ");  
		Util.displayCountdown(5, 5, "Zur\u00fcck zur Lobby senden..."+ChatColor.RED); 
		new BukkitRunnable() {
			
			public void run() {
				sendPlayersToLobby(); 
			} 
		}.runTaskLater(Runner.get(), 5*20L); 
	} 
	
	private void sendPlayersToLobby(){
		for(Player p : Bukkit.getOnlinePlayers()){
			Util.SendToBungeeServer(LobbyAPI.getBungeeLobbyServer(), p); 
		} 
		
		LobbyAPI.reload(); 
		this.reload(); 
	}
} 
