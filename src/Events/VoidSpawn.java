package Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

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
		  Location loc = new Location(Bukkit.getWorld(Bukkit.getServer().getPluginManager().getPlugin("AGCore").getConfig().getString("Spawn.World")),
				  Bukkit.getServer().getPluginManager().getPlugin("AGCore").getConfig().getDouble("Spawn.X"), 
				  Bukkit.getServer().getPluginManager().getPlugin("AGCore").getConfig().getDouble("Spawn.Y"), 
				  Bukkit.getServer().getPluginManager().getPlugin("AGCore").getConfig().getDouble("Spawn.Z"), 
				  (float) Bukkit.getServer().getPluginManager().getPlugin("AGCore").getConfig().getDouble("Spawn.Yaw"), 
				  (float) Bukkit.getServer().getPluginManager().getPlugin("AGCore").getConfig().getDouble("Spawn.Pitch"));
		  player.teleport(loc);
	  }
}
