package Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class VoidSpawn
  implements Listener
{
  @EventHandler(priority = EventPriority.HIGH)
  public void onDamage(EntityDamageEvent e)
  {
    Entity p = e.getEntity();
    if (((p instanceof Player)) 
    		&& (p.getWorld().getName().equalsIgnoreCase("world"))) 
    {
      if (e.getCause() == DamageCause.VOID)
      {
    	  e.setCancelled(true);
    	  p.sendMessage("§3Teleporting...");
          reSpawn((Player) p);
          return;
      }
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
