package de.fredo121.runner.events; 

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent; 

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.entity.EntityType; 

import de.fredo121.runner.game.Game;
import de.fredo121.runner.game.GamePlayer;
import de.fredo121.runner.game.GameSettings;
import de.fredo121.runner.game.GameState;
import de.fredo121.runner.game.TriggeredBlock;

public class EntityEventListener implements Listener {
    @EventHandler
    public void onBlockChange(EntityChangeBlockEvent event){ 
        if(event.getEntityType() == EntityType.FALLING_BLOCK){ 
            for(GamePlayer p : Game.get().getAliveGamePlayers()){ 
                if(event.getEntity().getLocation().distance(p.getPlayer().getLocation()) < 1){
                    p.getPlayer().damage(2, event.getEntity()); 
                }
            }
            
            event.setCancelled(true); 
        } 
    }
}