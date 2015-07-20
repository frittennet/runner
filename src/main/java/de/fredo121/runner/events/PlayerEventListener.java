package de.fredo121.runner.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent; 
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import de.fredo121.runner.game.Game;
import de.fredo121.runner.game.GamePlayer;
import de.fredo121.runner.game.GameSettings;
import de.fredo121.runner.game.GameState;
import de.fredo121.runner.game.TriggeredBlock;

public class PlayerEventListener implements Listener { 
	@EventHandler (priority=EventPriority.LOW)
	public void onPlayerQuitEvent(PlayerQuitEvent event){
		Game.get().removeGamePlayer(event.getPlayer()); 
	}
	
	@EventHandler
	public void onEntityDamageEvent(EntityDamageEvent event) {
		if (event.getEntityType() == EntityType.PLAYER) { 
			GamePlayer gamePlayer = Game.get().getGamePlayer((Player)event.getEntity()); 
			GameState gameState = Game.get().getGameState();
			
			if (!gamePlayer.getDead() && (gameState == GameState.RUNNING || gameState == GameState.PREPARATION)) { 
				if(gamePlayer.getPlayer().getHealth() - event.getDamage() <= 0){ 
					Game.get().onPlayerDie(gamePlayer); 
					event.setCancelled(true); 
				} 
			} 
			else{ 
				event.setCancelled(true); 
			} 
		} 
	}

	@EventHandler
	public void onPlayerMoveEvent(PlayerMoveEvent event) {
		if (Game.get().getGameState() == GameState.RUNNING) {
			Location blockLocation = event.getPlayer().getLocation().subtract(0d, 1d, 0d);
			Block blockUnderPlayer = blockLocation.getBlock();
			if (blockUnderPlayer.getMetadata(TriggeredBlock.metaname).isEmpty()
					&& GameSettings.get().getBlockMaterials().contains(blockUnderPlayer.getType())) {
				new TriggeredBlock(blockUnderPlayer);
			}
		}
	}                 
} 
