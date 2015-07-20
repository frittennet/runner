package ch.toothwit.runner.main;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import ch.toothwit.runner.events.EntityEventListener;
import ch.toothwit.runner.events.PlayerEventListener;
import ch.toothwit.runner.game.GameSettings;
import ch.toothwit.runner.main.StructureAPI;

import org.bukkit.block.Block; 
import org.bukkit.Bukkit; 

public class Runner extends JavaPlugin {
	private static Runner instance;
	private Block pos1;
	private Block pos2; 


	public Runner() {
		instance = this;
	}

	public static Runner get() {
		return instance;
	}

	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new PlayerEventListener(), this);
		getServer().getPluginManager().registerEvents(new EntityEventListener(), this);
		this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");

		try {
			StructureAPI.get().paste(StructureAPI.get().load("arena"), GameSettings.get().getSchematicLocation()); 
		} 
		catch(Exception exception){
			Bukkit.broadcastMessage("No schematic was set up !!! "); 
		} 
		
		this.getLogger().info("Enabled Runner Plugin by fredo121"); 
	}

	@Override
	public void onDisable() {
		this.getLogger().info("Disabled Runner Plugin by fredo121");
	}

	@Override 
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (command.getName().equalsIgnoreCase("runner")) {
			Player p = (Player) sender;

			if (args.length == 0) {
				displayUsage(sender);
				return true;
			} else {
				String cmd = args[0].toLowerCase();
				if (cmd.equals("addspawn")) {
					GameSettings.get().addSpawnLocation(p.getLocation());
					sender.sendMessage("Added Location"); 
				} else if (cmd.equals("addblock")) {
					Location playerLoc = p.getLocation();
					Location feetPos = playerLoc.subtract(0d, 1d, 0d);
					Material blockMaterial = playerLoc.getWorld().getBlockAt(feetPos).getType();
					if (blockMaterial != null) {
						sender.sendMessage(blockMaterial.toString() + " was added as a fallable Block Material ! ");
						GameSettings.get().addBlockMaterial(blockMaterial);
					} else {
						sender.sendMessage("Could not find a block you're standing on ");
					}
					sender.sendMessage("Added Block : " + blockMaterial.toString());
				} else if (cmd.equals("setspectatorlocation")) {
					GameSettings.get().setSpectatorLocation(p.getLocation());
					sender.sendMessage("Set Spectator location ");
				} else if (cmd.equals("setgamecountdown")) {
					GameSettings.get().setGameCountdown(Integer.parseInt(args[1])); 
					sender.sendMessage("Set game countdown to "+args[1]+" seconds");
				} else if (cmd.equals("schem")) {
					if(args[1] == null){
						sender.sendMessage("Use '/runner schem <pos1/pos2/save/location>'"); 
					}
					else{
						String subCommand = args[1]; 
						if(subCommand.equalsIgnoreCase("pos1")){
							pos1 = p.getLocation().getBlock(); 
							sender.sendMessage("Selected Pos1"); 
						}
						else if(subCommand.equalsIgnoreCase("pos2")){
							pos2 = p.getLocation().getBlock(); 
							sender.sendMessage("Selected Pos2"); 
						}
						else if(subCommand.equalsIgnoreCase("save")){
							StructureAPI.get().save("arena", StructureAPI.get().getStructure(pos1, pos2)); 
							sender.sendMessage("Saved schematic to /plugins/Runner/schematics/arena.schem"); 
						}
						else if(subCommand.equalsIgnoreCase("location")){
							GameSettings.get().setSchematicLocation(p.getLocation()); 
							sender.sendMessage("Set schematic paste location"); 
						}
						else{
							sender.sendMessage("'"+subCommand+"' is not valid. Use '/runner schem <pos1/pos2/save/location>'"); 
						}
					}
				} else { 
					displayUsage(sender, cmd);
				}
				return true;
			}
		}

		return false;

	}

	private void displayUsage(CommandSender sender) {
		displayUsage(sender, ""); 
	}

	private void displayUsage(CommandSender sender, String cmd) {
		sender.sendMessage(new String[] { "Command '" + cmd + "' doesn't exist available commands: ",
				"/runner addspawn", "/runner addblock", "/runner setGameCountdown", "/runner setSpectatorLocation" });
	}

	
}
