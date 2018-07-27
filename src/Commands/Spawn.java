package Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

import Core.Main;

public class Spawn implements CommandExecutor
{
	
	private int count;
	private int taskID;

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] arg3) 
	{
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("spawn"))
		  {
			  if(!Main.getInstance().hm.containsKey(p.getName()))
			  {
				  if(sender.hasPermission("core.spawn"))
				  {
					  Main.getInstance().hm.put(p.getName(), 0);
					  p.sendMessage(Main.getInstance().getConfig().getString("Prefix").replace('&', '§') + "§cTeleporting in "+"§c§l3" + " §cseconds..");
					  BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					  taskID = scheduler.scheduleSyncRepeatingTask((Plugin) this, new Runnable()
					  {
						@Override
						  public void run()
						  {
							  if(count == 0)
							  {
								  p.sendMessage("§3Teleporting...");
								  Location loc = new Location(Bukkit.getWorld(Main.getInstance().getConfig().getString("Spawn.World")),
										  Main.getInstance().getConfig().getDouble("Spawn.X"), 
										  Main.getInstance().getConfig().getDouble("Spawn.Y"), Main.getInstance().getConfig().getDouble("Spawn.Z"), 
										  (float) Main.getInstance().getConfig().getDouble("Spawn.Yaw"), 
										  (float) Main.getInstance().getConfig().getDouble("Spawn.Pitch"));
								  p.teleport(loc);
								  Main.getInstance().hm.remove(p.getName());
								  stopTimer();
								  return;
							  }
						  }
					  }, 60L, 20L);		  
				  }
				  else
				  {
					  sender.sendMessage("§cYou are missing the 'core.spawn' permission.");
					  return true;
				  }
			  }
		  }
		return false;
	}
	@EventHandler
	public void onMove(PlayerMoveEvent e)
	{
		if(Main.getInstance().hm.containsKey(e.getPlayer().getName()))
		  {
			  if(e.getFrom().getBlockX() != e.getTo().getBlockX())
			  {
				  stopTimer();
				  Main.getInstance().hm.remove(e.getPlayer().getName());
				  e.getPlayer().sendMessage("§cYou moved! Teleportation cancelled.");
				  return;
			  }
		  }
	}
	public void stopTimer()
	{
		Bukkit.getScheduler().cancelTask(taskID);
	}
}
