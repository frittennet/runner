package ch.toothwit.runner.main;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class Util {
	public static void SendToBungeeServer(final String server, final Player player) { 
		new BukkitRunnable() { 
			
			public void run() {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
                DataOutputStream out = new DataOutputStream(b); 

                try {
                    out.writeUTF("Connect");
                    out.writeUTF(server);
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }

                if (b != null) {
                    player.sendPluginMessage(Runner.get(), "BungeeCord", b.toByteArray());
                }
            }
        }.runTaskLater(Runner.get(), 20L); 
	} 
	
	public static void displayCountdown(final int currentSeconds, final int startSeconds, final String message){ 
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			player.setLevel(currentSeconds); 
		}
		new BukkitRunnable() {
			
			public void run() {
				displayCountdown(currentSeconds-1, startSeconds, message);  
			}
		}.runTaskLater(Runner.get(), 20L);
	} 
	
	public static void hideCountdown(){
		for(Player player : Bukkit.getServer().getOnlinePlayers()){
			player.setLevel(0); 
		}
	} 
}
