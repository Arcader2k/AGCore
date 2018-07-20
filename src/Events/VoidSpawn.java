package Events;

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
          reSpawn((Player) p);
      }
    }
  }
  
  public static void reSpawn(Player player) 
  {
	  player.teleport(player.getWorld().getSpawnLocation());
	  player.setFallDistance(0);
  }
}
