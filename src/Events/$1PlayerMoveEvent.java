package Events;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.util.Vector;

import Core.Main;

public class $1PlayerMoveEvent
  implements Listener
{
  @SuppressWarnings("deprecation")
  @EventHandler
  public void onLaunchMove(PlayerMoveEvent event)
  {
    Player player = event.getPlayer();
    Location playerLoc = player.getLocation();
	int ID = playerLoc.getWorld().getBlockAt(playerLoc).getRelative(0, -1, 0).getTypeId();
    if (ID == 165)
    {
      player.setVelocity(player.getLocation().getDirection().multiply(5));
      player.setVelocity(new Vector(player.getVelocity().getX(), 1.0D, player.getVelocity().getZ()));
      player.playEffect(player.getLocation(), Effect.EXPLOSION_HUGE, 1);
      player.playSound(player.getLocation(), Sound.ENTITY_LIGHTNING_IMPACT, 1.0F, 1.0F);
    }
  }
}
