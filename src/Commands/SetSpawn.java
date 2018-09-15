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
				  Location loc = new Location(Bukkit.getWorld(Main.getInstance().getConfig().getString("Spawn.World")),
						  Main.getInstance().getConfig().getDouble("Spawn.X"), 
						  Main.getInstance().getConfig().getDouble("Spawn.Y"), Main.getInstance().getConfig().getDouble("Spawn.Z"));
				  Main.getInstance().getServer().getWorld(Main.getInstance().getConfig().getString("Spawn.World")).setSpawnLocation(loc);
				  
				  Main.getInstance().getConfig().set("Spawn.World", p.getWorld().getName());
				  Main.getInstance().getConfig().set("Spawn.X", p.getLocation().getX());
				  Main.getInstance().getConfig().set("Spawn.Y", p.getLocation().getY());
				  Main.getInstance().getConfig().set("Spawn.Z", p.getLocation().getZ());
				  Main.getInstance().getConfig().set("Spawn.Yaw", p.getLocation().getYaw());
				  Main.getInstance().getConfig().set("Spawn.Pitch", p.getLocation().getPitch());
				  Main.getInstance().saveConfig();
				  
				  p.sendMessage(Main.getInstance().getConfig().getString("Prefix ").replace('&', '§') + "§aSpawn Set!");
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
