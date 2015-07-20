package ch.toothwit.runner.main; 

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.Plugin;

import ch.toothwit.runner.main.Runner;
 
public class StructureAPI {
    private static StructureAPI instance; 
    Plugin plugin;
 
    public StructureAPI(Plugin pl){
        this.plugin = pl;
    } 
    
    public static StructureAPI get(){
        if(instance == null){
            instance = new StructureAPI(Runner.get()); 
        }
        return instance; 
    }
 
    /**
    * Get all blocks between two points and return a 3d array
    */
 
    public String[][][] getStructure(Block block, Block block2){
        int minX, minZ, minY;
        int maxX, maxZ, maxY;
 
 
        //Couldv'e used Math.min()/Math.max(), but that didn't occur to me until after I finished this :D
        minX = block.getX() < block2.getX() ? block.getX() : block2.getX();
        minZ = block.getZ() < block2.getZ() ? block.getZ() : block2.getZ();
        minY = block.getY() < block2.getY() ? block.getY() : block2.getY();
 
        maxX = block.getX() > block2.getX() ? block.getX() : block2.getX();
        maxZ = block.getZ() > block2.getZ() ? block.getZ() : block2.getZ();
        maxY = block.getY() > block2.getY() ? block.getY() : block2.getY();
 
        String[][][] blocks = new String[maxX-minX+1][maxY-minY+1][maxZ-minZ+1];
 
        for(int x = minX; x <= maxX; x++){
 
            for(int y = minY; y <= maxY; y++){
 
                for(int z = minZ; z <= maxZ; z++){
 
                    Block b = block.getWorld().getBlockAt(x, y, z);
                    blocks[x-minX][y-minY][z-minZ] = Integer.toString(b.getTypeId())+":"+Byte.toString(b.getData());
                }
            }
        }
 
        return blocks;
 
    }
 
 
    /**
    * Pastes a structure to a desired location
    */
 
    public void paste(String[][][] blocks, Location l){
        for(int x = 0; x < blocks.length; x++){
 
            for(int y = 0; y < blocks[x].length; y++){
 
                for(int z = 0; z < blocks[x][y].length; z++){
                    Location neww = l.clone();
                    neww.add(x, y, z);
                    Block b = neww.getBlock();
                    String[] split = blocks[x][y][z].split(":"); 
                    if(blocks[x][y][z] != "0:0"){
                        b.setTypeId(Integer.parseInt(split[0])); 
                        b.setData(Byte.parseByte(split[1])); 
                        b.getState().update(true);
                    }
                }
 
            }
        }
    }
 
    /**
    * Save a structure with a desired name
    */
 
    public void save(String name, String[][][] b){
        ObjectOutputStream oos = null;
        FileOutputStream fout = null;
 
        File f = new File(plugin.getDataFolder() + "/schematics/"+ name + ".schem");
        File dir = new File(plugin.getDataFolder() + "/schematics");
 
        try {
                dir.mkdirs();
                f.createNewFile();
        } catch (IOException e1) {
                e1.printStackTrace();
            }
 
        try{
               fout = new FileOutputStream(f);
               oos = new ObjectOutputStream(fout);
               oos.writeObject(b);
        } catch (Exception e) {
               e.printStackTrace();
        }finally {
               if(oos  != null){
                   try {
                        oos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        }
    }
 
    /**
    * Load structure from file
    */
 
    public String[][][] load(String name){
 
        File f = new File(plugin.getDataFolder() + "/schematics/"+ name + ".schem");
 
        String[][][] loaded = new String[0][0][0];
 
        try {
            FileInputStream streamIn = new FileInputStream(f);
           ObjectInputStream objectinputstream = new ObjectInputStream(streamIn);
           loaded = (String[][][])objectinputstream.readObject();
 
           objectinputstream.close();
 
       } catch (Exception e) {
 
           e.printStackTrace();
    }
 
        return loaded;
    }
 
 
}