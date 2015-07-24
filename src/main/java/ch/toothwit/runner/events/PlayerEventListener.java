package ch.toothwit.runner.events;

import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ch.toothwit.runner.game.Game;
import ch.toothwit.runner.game.GamePlayer;
import ch.toothwit.runner.game.GameSettings;
import ch.toothwit.runner.game.GameState;
import ch.toothwit.runner.game.TriggeredBlock;

public class PlayerEventListener implements Listener { 
	@EventHandler (priority=EventPriority.LOW)
	public void onPlayerQuitEvent(PlayerQuitEvent event){
		Game.get().removeGamePlayer(event.getPlayer()); 
	}
	
	@EventHandler 
	public void onPlayerJoinEvent(PlayerJoinEvent event){
		if(Game.get().getGameState() != GameState.LOBBY){ 
			Player player = event.getPlayer(); 
			player.setGameMode(GameMode.SPECTATOR); 
			player.teleport(GameSettings.get().getSpectatorLocation()); 
		}
	} 
	
	@EventHandler
	public void onFoodLevelChangeEvent(FoodLevelChangeEvent event){           
		event.setCancelled(true);
	} 
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) { 
			GamePlayer gamePlayer = Game.get().getGamePlayer((Player)event.getEntity()); 
			GameState gameState = Game.get().getGameState(); 
			
			if(gamePlayer != null && (event.getCause() == EntityDamageEvent.DamageCause.VOID || (gamePlayer.getPlayer().getHealth() - event.getDamage() <= 0))){ 
				if ((gameState == GameState.RUNNING || gameState == GameState.PREPARATION)) { 
					Game.get().onPlayerDie(gamePlayer); 
					event.setCancelled(true); 
				} 
				else {
					event.setCancelled(true); 	
				} 
			} 
		} 
	} 
	
	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		if (Game.get().getGameState() == GameState.RUNNING) {
			new TriggeredBlock(event.getPlayer().getLocation().subtract(0d, 1d, 0d)); 
		}
	}                
} 
