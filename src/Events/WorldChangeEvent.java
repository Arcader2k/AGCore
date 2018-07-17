package Events;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;

public class WorldChangeEvent
  implements Listener
{
  @EventHandler
  public void onWorldChange(PlayerChangedWorldEvent event)
  {
    Player p = event.getPlayer();
    if (p.getWorld().getName().equalsIgnoreCase("Warzone"))
    {
      if (!p.isFlying())
      {
        p.setFlying(false);
        p.setAllowFlight(false);
        p.sendMessage(ChatColor.GREEN + "You entered" + ChatColor.DARK_RED + 
          ChatColor.BOLD + " Warzone" + ChatColor.GREEN + "! " + 
          ChatColor.RED + "Flying Disabled!");
      }
      if (p.isFlying())
      {
        p.setFlying(false);
        p.setAllowFlight(false);
        p.sendMessage(ChatColor.GREEN + "You entered" + ChatColor.DARK_RED + 
          ChatColor.BOLD + " Warzone" + ChatColor.GREEN + "! " + 
          ChatColor.RED + "Flying Disabled!");
      }
      if (p.getGameMode() == GameMode.CREATIVE)
      {
        p.setGameMode(GameMode.SURVIVAL);
        p.setFlying(false);
        p.setAllowFlight(false);
      }
      if (p.getGameMode() == GameMode.ADVENTURE)
      {
        p.setGameMode(GameMode.SURVIVAL);
        p.setFlying(false);
        p.setAllowFlight(false);
      }
      if (p.getGameMode() == GameMode.SPECTATOR)
      {
        p.setGameMode(GameMode.SURVIVAL);
        p.setFlying(false);
        p.setAllowFlight(false);
      }
      if (p.isOp())
      {
        if (p.isFlying())
        {
          p.setFlying(false);
          p.setAllowFlight(false);
          p.sendMessage(ChatColor.GREEN + "You entered" + ChatColor.DARK_RED + 
            ChatColor.BOLD + " Warzone" + ChatColor.GREEN + "! " + 
            ChatColor.RED + "Flying Disabled!");
        }
        if (p.getGameMode() == GameMode.CREATIVE)
        {
          p.setGameMode(GameMode.SURVIVAL);
          p.setFlying(false);
          p.setAllowFlight(false);
        }
        if (p.getGameMode() == GameMode.ADVENTURE)
        {
          p.setGameMode(GameMode.SURVIVAL);
          p.setFlying(false);
          p.setAllowFlight(false);
        }
        if (p.getGameMode() == GameMode.SPECTATOR)
        {
          p.setGameMode(GameMode.SURVIVAL);
          p.setFlying(false);
          p.setAllowFlight(false);
        }
      }
      if (p.getWorld().getName().equalsIgnoreCase("arena1"))
      {
        if (!p.isFlying())
        {
          p.setFlying(false);
          p.setAllowFlight(false);
          p.sendMessage(ChatColor.BLUE + "[Duels]" + 
            ChatColor.DARK_RED + ChatColor.BOLD + " Entered Arena: " + 
            ChatColor.RED + "Flying Disabled!");
        }
        if (p.isFlying())
        {
          p.setFlying(false);
          p.setAllowFlight(false);
          p.sendMessage(ChatColor.BLUE + "[Duels]" + 
            ChatColor.DARK_RED + ChatColor.BOLD + " Entered Arena: " + 
            ChatColor.RED + "Flying Disabled!");
        }
        if (p.getGameMode() == GameMode.CREATIVE)
        {
          p.setGameMode(GameMode.SURVIVAL);
          p.setFlying(false);
          p.setAllowFlight(false);
        }
        if (p.getGameMode() == GameMode.ADVENTURE)
        {
          p.setGameMode(GameMode.SURVIVAL);
          p.setFlying(false);
          p.setAllowFlight(false);
        }
        if (p.getGameMode() == GameMode.SPECTATOR)
        {
          p.setGameMode(GameMode.SURVIVAL);
          p.setFlying(false);
          p.setAllowFlight(false);
        }
        if (p.isOp())
        {
          if (p.isFlying())
          {
            p.setFlying(false);
            p.setAllowFlight(false);
            p.sendMessage(ChatColor.BLUE + "[Duels]" + 
              ChatColor.DARK_RED + ChatColor.BOLD + " Entered Arena: " + 
              ChatColor.RED + "Flying Disabled!");
          }
          if (p.getGameMode() == GameMode.CREATIVE)
          {
            p.setGameMode(GameMode.SURVIVAL);
            p.setFlying(false);
            p.setAllowFlight(false);
          }
          if (p.getGameMode() == GameMode.ADVENTURE)
          {
            p.setGameMode(GameMode.SURVIVAL);
            p.setFlying(false);
            p.setAllowFlight(false);
          }
          if (p.getGameMode() == GameMode.SPECTATOR)
          {
            p.setGameMode(GameMode.SURVIVAL);
            p.setFlying(false);
            p.setAllowFlight(false);
          }
        }
      }
    }
  }
}
