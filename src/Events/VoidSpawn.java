package Events;

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
    if (((p instanceof Player)) && 
      (p.getWorld().getName().equalsIgnoreCase("world"))) 
    {
      if (e.getCause() == DamageCause.VOID)
      {
    	e.setCancelled(true);
    	p.sendMessage("§3Teleporting...");
        reSpawn(p);
      }
    }
  }
  
  public static void reSpawn(Entity p) 
  {
    Double X = p.getWorld().getSpawnLocation().getX();
    Double Y = p.getWorld().getSpawnLocation().getY();
    Double Z = p.getWorld().getSpawnLocation().getZ();
    Location loc = new Location(p.getWorld(), X, Y, Z);
    p.teleport(loc);
  }
}
