package ch.toothwit.runner.game;

import org.bukkit.entity.Player;            

public class GamePlayer {
	private Player player; 
	private int score = 0; 
	private boolean dead = false; 
	private int rank; 
	
	public GamePlayer(Player player){
		this.player = player; 
		this.player.setSaturation(20f); 
		this.player.setHealth(20f); 
	} 
	
	public Player getPlayer(){
		return player; 
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	} 
	
	public void addScore(int score) { 
		this.score += score; 
	}
	
	public void setDead(){
		dead = true; 
	} 
	
	public boolean getDead(){
		return dead; 
	} 
	
	public void setRank(int rank){
		this.rank = rank; 
	}
	
	public int getRank(){
		return rank; 
	}
} 
