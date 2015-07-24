package ch.toothwit.runner.game;

import java.util.List;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.block.Block; 
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.Location; 

import ch.toothwit.runner.main.Runner;
import ch.toothwit.runner.game.GameSettings; 

public class TriggeredBlock { 
	private static double difference = 0.31; 
	private List<Block> blocks = new ArrayList<Block>(); 
	
	public TriggeredBlock(Location location){ 
		for(int x = -1;x<=1;x++){
			for(int y = -1;y<=1;y++){ 
				Block block = location.clone().add(difference*x, 0d, difference*y).getBlock(); 
				if (GameSettings.get().getBlockMaterials().contains(block.getType())) { 
					this.blocks.add(block); 	
				} 
			}
		} 
		
		updateState(TriggerState.MARK_GREEN); 
	} 
	
	@SuppressWarnings("deprecation")
	private void updateState(final TriggerState newState){ 
		if(newState == TriggerState.MARK_GREEN){
			for(Block block : blocks){ 
				block.setType(GameSettings.countdownMaterial); 
			} 
		} 
		if(newState != TriggerState.FALL){ 
			for(Block block : blocks){ 
				block.setData(GameSettings.countdownData[newState.ordinal()]); 
			} 
			new BukkitRunnable() { 
				
				public void run() {                   
					updateState(TriggerState.values()[newState.ordinal()+1]);     
				} 
			}.runTaskLater(Runner.get(), 10L);
		} 
		else { 
			for(Block block : blocks){ 
				Material type = block.getType(); 
				block.setType(Material.AIR); 
				block.getWorld().spawnFallingBlock(block.getLocation(), type, GameSettings.countdownData[TriggerState.FALL.ordinal()]); 
			} 
		} 
	} 
} 
