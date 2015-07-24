package ch.toothwit.runner.events; 

import ch.toothwit.runner.game.Game; 
import ch.toothwit.runner.game.GameState;
import org.bukkit.event.Listener; 
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerEventListener implements Listener { 
    @EventHandler 
	public void onServerListPing(ServerListPingEvent event) {
	    switch(Game.get().getGameState()){
	        case RUNNING:
	        case PREPARATION: 
	        case FINISHED: 
	            event.setMotd(ChatColor.AQUA+"[Laufend]");
	            break; 
	        case LOBBY: 
	            event.setMotd(ChatColor.GREEN+"[Lobby]");
	            break; 
	        case STOPPED: 
	            event.setMotd(ChatColor.DARK_RED+"[Gestoppt]"); 
	            break; 
	    } 
	}
}