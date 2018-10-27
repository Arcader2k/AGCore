package Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Core.Main;

public class SetSpawn implements CommandExecutor
{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] arg3) 
	{
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("setspawn"))
		  {
			  if(sender.hasPermission("core.setspawn"))
			  {
				  Location loc = new Location(Bukkit.getWorld(Main.config.getString("Spawn.Location.World")),
						  Main.config.getDouble("Spawn.Location.X"), 
						  Main.config.getDouble("Spawn.Location.Y"), Main.config.getDouble("Spawn.Location.Z"));
				  Bukkit.getServer().getWorld(Main.config.getString("Spawn.Location.World")).setSpawnLocation(loc);
				  
				  Main.config.set("Spawn.Location.World", p.getWorld().getName());
				  Main.config.set("Spawn.Location.X", p.getLocation().getX());
				  Main.config.set("Spawn.Location.Y", p.getLocation().getY());
				  Main.config.set("Spawn.Location.Z", p.getLocation().getZ());
				  Main.config.set("Spawn.Location.Yaw", p.getLocation().getYaw());
				  Main.config.set("Spawn.Location.Pitch", p.getLocation().getPitch());
				  Main.getInstance().saveConfig();
				  
				  sender.sendMessage(Main.config.getString("Prefix") + " §aSpawn Set!");
				  return true;
			  }
			  else
			  {
				  sender.sendMessage("§cYou are missing the 'core.setspawn' permission.");
				  sender.sendMessage("§cPlease contact support.");
				  return true;
			  }  
		  }
		return false;
	}

}
