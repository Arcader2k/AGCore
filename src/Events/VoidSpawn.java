package Events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class VoidSpawn
  implements Listener
{
  @EventHandler
  public void onDamage(EntityDamageEvent e)
  {
    Entity p = e.getEntity();
    if (((p instanceof Player)) && 
      (p.getWorld().getName().equalsIgnoreCase("world"))) {
      if (e.getCause() == EntityDamageEvent.DamageCause.VOID)
      {
        e.setCancelled(true);
        p.teleport(p.getWorld().getSpawnLocation());
        p.sendMessage("§6It's too dark down there...");
        p.sendMessage("§6Let's go back to Spawn.");
        return;
      }
    }
    return;
  }
}
