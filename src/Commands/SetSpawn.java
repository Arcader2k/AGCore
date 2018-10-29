package Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Core.Core;

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
				Location loc = new Location(Bukkit.getWorld(Core.config.getString("Spawn.Location.World")),
						Core.config.getDouble("Spawn.Location.X"), 
						Core.config.getDouble("Spawn.Location.Y"), Core.config.getDouble("Spawn.Location.Z"));
				Bukkit.getServer().getWorld(Core.config.getString("Spawn.Location.World")).setSpawnLocation(loc);
				  
				Core.config.set("Spawn.Location.World", p.getWorld().getName());
				Core.config.set("Spawn.Location.X", p.getLocation().getX());
				Core.config.set("Spawn.Location.Y", p.getLocation().getY());
				Core.config.set("Spawn.Location.Z", p.getLocation().getZ());
				Core.config.set("Spawn.Location.Yaw", p.getLocation().getYaw());
				Core.config.set("Spawn.Location.Pitch", p.getLocation().getPitch());
				Core.getInstance().saveConfig();
				  
				sender.sendMessage(Core.config.getString("Prefix") + " §aSpawn Set!");
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
