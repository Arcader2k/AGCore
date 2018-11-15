package Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import Core.Core;

public class VoidSpawn
  implements Listener
{	
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		Player p = e.getPlayer();
        Location loc = p.getLocation();
 
        if (loc.getBlockY() <= 0)
        {
        	p.setFallDistance(0);
            reSpawn(p);
        }
	}
	public static void reSpawn(Player player) 
	{
	  Location loc = new Location(Bukkit.getWorld(Core.config.getString("Spawn.Location.World")),
			  Core.config.getDouble("Spawn.Location.X"), 
			  Core.config.getDouble("Spawn.Location.Y"), 
			  Core.config.getDouble("Spawn.Location.Z"), 
			  (float) Core.config.getDouble("Spawn.Location.Yaw"), 
			  (float) Core.config.getDouble("Spawn.Location.Pitch"));
	  player.teleport(loc);
	  return;
	}
}
