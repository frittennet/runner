package ch.toothwit.runner.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.weather.WeatherChangeEvent;

public class WeatherEventListener implements Listener { 
	
	@EventHandler 
	public void onWeatherChangeEvent(WeatherChangeEvent event){ 
		if(event.toWeatherState()){ 
			event.setCancelled(true);
			event.getWorld().setStorm(false);
		} 
	} 
}
