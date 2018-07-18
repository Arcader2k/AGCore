package Commands;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import Utils.API;

public class Check implements CommandExecutor
{
public boolean onCommand(CommandSender cs, Command arg1, String arg2, String[] args)
{
	if(!cs.hasPermission("check.use")) 
	{
		cs.sendMessage("§cYou do not have permission to use this!");
	    return true;
	}
	if (args.length <= 0) 
	{
	    return false;
	}
	cs.sendMessage("§cLoading...");
	  String player = args[0];
	  InetSocketAddress host = null;
	  if (Bukkit.getPlayer(player) != null) 
	  {
	    host = Bukkit.getPlayer(player).getAddress();
	  }
	  UUID uuid = API.getUUID(player);
	  cs.sendMessage("§3------------------------------------------");
	  cs.sendMessage("§aName: §7" + player);
	  
	  if (uuid == null) 
	  {
		    cs.sendMessage("§7- UUID could not be found..");
		  } else {
		    cs.sendMessage("§aUUID: §7" + uuid.toString());
		    cs.sendMessage("§aName history: §7");
		    for (String s : API.getChangedNamesAtTime(API.getPreviousNames(uuid))) 
		    {
		      cs.sendMessage("§a- §7" + s);
		      cs.sendMessage("§aIP: §7" + host);
		      cs.sendMessage("§3------------------------------------------");
		    }
		  }
          return true;
       }
    }