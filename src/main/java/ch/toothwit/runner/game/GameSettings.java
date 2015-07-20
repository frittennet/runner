package ch.toothwit.runner.game;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material; 
import org.bukkit.configuration.file.FileConfiguration;

import ch.toothwit.runner.main.Runner;

public class GameSettings {
	private static GameSettings instance; 
	private FileConfiguration config; 
	
	
	// Settings 
	private List<Location> spawnLocations = new ArrayList<Location>(); 
	private List<Material> blockMaterials = new ArrayList<Material>(); 
	private int gameCountdown;
	private Location spectatorLocation; 
	private Location schematicLocation; 
	
	// static Settings 
	public static Material countdownMaterial = Material.STAINED_CLAY; 
	public static byte[] countdownData = new byte[]{ 5 , 4 , 14 , 14 }; 
	
	public static GameSettings get(){
		if(instance == null){
			instance = new GameSettings(); 
		}
		return instance; 
	} 
	
	public GameSettings(){ 
		Runner.get().saveDefaultConfig(); 
		config = Runner.get().getConfig(); 
		
		reloadConfig(); 
		
	}
	
	public void saveConfig(){ 
		setLocation("game.spectatorLocation", spectatorLocation); 
		setLocation("game.schematicLocation", schematicLocation); 
		setLocationList("game.spawnLocations", spawnLocations); 
		setMaterialList("game.blockTypes", blockMaterials); 
		
		config.set("game.countdown", gameCountdown); 
		
		File gameConfig = new File(Runner.get().getDataFolder()+"/"+"config.yml"); 
		try {
			config.save(gameConfig);
		} catch (IOException e) {
			Bukkit.getLogger().warning("Could not save config"); 
		} 
	} 
	
	public void reloadConfig(){
		Runner.get().reloadConfig(); 
		config = Runner.get().getConfig(); 
		
		spectatorLocation = getLocation("game.spectatorLocation"); 
		schematicLocation = getLocation("game.schematicLocation"); 
		spawnLocations = getLocationList("game.spawnLocations"); 
		blockMaterials = getMaterialList("game.blockTypes"); 
		gameCountdown = config.getInt("game.countdown"); 
	} 
	
	public void setLocationList(String path, List<Location> locations){
		List<String> locs = new ArrayList<String>();
		for(Location loc : locations){
		    locs.add(loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ());
		}
		config.set(path, locs); 
	}
	
	public List<Location> getLocationList(String path){ 
		List<String> locstrings = config.getStringList(path);
		List<Location> locs = new ArrayList<Location>();
		for(String s : locstrings){
		    locs.add(new Location(Bukkit.getWorld(s.split(" ")[0]), Double.parseDouble(s.split(" ")[1]), Double.parseDouble(s.split(" ")[2]), Double.parseDouble(s.split(" ")[3])));
		}
		return locs; 
	}
	
	public void setMaterialList(String path, List<Material> materials){
		List<String> mats = new ArrayList<String>();
		for(Material mat : materials){
			mats.add(mat.toString()); 
		}
		config.set(path, mats); 
	}
	
	public List<Material> getMaterialList(String path){
		List<String> matStrings = config.getStringList(path);
		List<Material> mats = new ArrayList<Material>();
		for(String s : matStrings){
			mats.add(Material.valueOf(s));
		}
		return mats; 
	}
	
	public void setLocation(String path, Location loc){ 
		if(loc == null){ 
			loc = new Location(Bukkit.getWorlds().get(0), 0d, 100d, 0d); 
		}
		config.set(path, loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ()); 
	}
	
	public Location getLocation(String path){ 
		String s = config.getString(path); 
		return new Location(Bukkit.getWorld(s.split(" ")[0]), Double.parseDouble(s.split(" ")[1]), Double.parseDouble(s.split(" ")[2]), Double.parseDouble(s.split(" ")[3])); 
	} 
	
	
	
	// get methods 
	
	public List<Location> getSpawnLocations(){
		return spawnLocations; 
	}
	
	public List<Material> getBlockMaterials(){
		return blockMaterials; 
	}
	
	public int getGameCountdown() {
		return gameCountdown;
	} 

	public Location getSpectatorLocation() {
		return spectatorLocation; 
	}
	
	public Location getSchematicLocation(){
		return schematicLocation; 
	} 
	
	// set / add methods 
	public void addSpawnLocation(Location location){
		this.spawnLocations.add(location); 
		saveConfig(); 
	}

	public void addBlockMaterial(Material material){
		this.blockMaterials.add(material); 
		saveConfig(); 
	} 

	public void setGameCountdown(int gameCountdown) {
		this.gameCountdown = gameCountdown; 
		saveConfig(); 
	}

	public void setSpectatorLocation(Location location) {
		spectatorLocation = location; 
		saveConfig(); 
	} 
	
	public void setSchematicLocation(Location location){
		schematicLocation = location; 
		saveConfig(); 
	}
}
