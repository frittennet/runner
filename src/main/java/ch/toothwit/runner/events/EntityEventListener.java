package ch.toothwit.runner.events; 

import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import ch.toothwit.runner.game.Game;
import ch.toothwit.runner.game.GamePlayer;

import org.bukkit.event.EventHandler;
import org.bukkit.entity.EntityType;

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