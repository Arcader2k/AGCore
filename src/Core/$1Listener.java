package Core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import Events.$1PlayerMoveEvent;
import Events.VoidSpawn;
import Events.WorldChangeEvent;

public class $1Listener
  extends JavaPlugin
  implements Listener
{
  public void onEnable()
  {
    getCommand("agtest").setExecutor(this);
    
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(this, this);
    pm.registerEvents(new VoidSpawn(), this);
    pm.registerEvents(new $1PlayerMoveEvent(), this);
    pm.registerEvents(new WorldChangeEvent(), this);
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg)
  {
    if (cmd.getName().equalsIgnoreCase("agtest"))
    {
      if ((sender instanceof Player))
      {
        sender.sendMessage(ChatColor.DARK_GREEN + "-==========[" + ChatColor.GREEN + " AGCore" + ChatColor.DARK_GREEN + " ]==========-");
        sender.sendMessage(ChatColor.DARK_GREEN + "Active?: " + ChatColor.GREEN + ChatColor.BOLD + getServer().getPluginManager().isPluginEnabled("AGCore"));
        sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + ChatColor.BOLD + getServer().getPluginManager().getPlugin("AGCore")
          .getDescription().getVersion());
        return true;
      }
      if(sender == getServer().getConsoleSender())
      {
    	  sender.sendMessage(ChatColor.DARK_GREEN + "-==========[" + ChatColor.GREEN + " AGCore" + ChatColor.DARK_GREEN + " ]==========-");
          sender.sendMessage(ChatColor.DARK_GREEN + "Active?: " + ChatColor.GREEN + ChatColor.BOLD + getServer().getPluginManager().isPluginEnabled("AGCore"));
          sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + ChatColor.BOLD + getServer().getPluginManager().getPlugin("AGCore")
            .getDescription().getVersion());  
      }
      return false;
    }
    return false;
  }
}
