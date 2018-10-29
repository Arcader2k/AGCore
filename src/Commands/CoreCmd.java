package Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;

import Core.Core;

public class CoreCmd implements CommandExecutor 
{
	private int count;
	private int reloadID;
	
	public void stopReload()
	{
	    Bukkit.getScheduler().cancelTask(reloadID);
	}

	@Override
	public boolean onCommand(CommandSender s, Command c, String st, String[] a) {
		@SuppressWarnings("unused")
		Player p = (Player) s;
		if(c.getName().equalsIgnoreCase("core"))
		  {
			  if(a.length == 1)
			  {
				  if(a[0].equalsIgnoreCase("reload"))
				  {
					  if(s.hasPermission("core.reload") && s instanceof Player || s.hasPermission("core.*"))
					  {
						  s.sendMessage(Core.config.getString("Prefix").replace('&', '§') + (" §cReloading config.."));
						  BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
						  reloadID = scheduler.scheduleSyncRepeatingTask(Core.getInstance(), new Runnable()
						  {
							@Override
							  public void run()
							  {
								  if(count == 0)
								  {
									  Core.getInstance().reloadConfig();
									  s.sendMessage("§aComplete");
									  stopReload();
								  }
							  }
						  }, 40L, 20L);
						  return true; 
					  }
					  s.sendMessage("§cYou are missing the 'core.reload' permission.");
					  s.sendMessage("§cPlease contact support.");
					  return true;
				  }
			  }
		  }
		return true;
	}
}
