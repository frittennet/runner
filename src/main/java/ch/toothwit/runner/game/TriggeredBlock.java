package ch.toothwit.runner.game;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import ch.toothwit.runner.main.Runner;

public class TriggeredBlock { 
	public static String metaname; 
	
	private Block block; 
	
	public TriggeredBlock(Block block){
		this.block = block; 
		
		updateState(TriggerState.MARK_GREEN); 
	} 
	
	@SuppressWarnings("deprecation")
	private void updateState(final TriggerState newState){ 
		if(newState == TriggerState.MARK_GREEN){
			block.setType(GameSettings.countdownMaterial); 
			block.setMetadata(TriggeredBlock.metaname, new FixedMetadataValue(Runner.get(), true));
		} 
		if(newState != TriggerState.FALL){ 
			this.block.setData(GameSettings.countdownData[newState.ordinal()]); 
			new BukkitRunnable() {
				
				public void run() {                   
					updateState(TriggerState.values()[newState.ordinal()+1]);     
				} 
			}.runTaskLater(Runner.get(), 10L);
		} 
		else { 
			Material type = this.block.getType(); 
			this.block.setType(Material.AIR); 
			this.block.getWorld().spawnFallingBlock(this.block.getLocation(), type, GameSettings.countdownData[TriggerState.FALL.ordinal()]); 
			// TODO spawn falling block entity at position 
		} 
	} 
} 
