package Commands;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import Core.Core;
import net.md_5.bungee.api.ChatColor;

public class LaunchPads implements CommandExecutor 
{

	@Override
	public boolean onCommand(CommandSender s, Command c, String str, String[] a) 
	{
		Player p = (Player) s;
		if(c.getName().equals("lp"))
		{
			if(a.length <=0)
			{
				if(a[0].equals("enable"))
				{
					if(s.hasPermission("core.launchpads.use"))
					{
						List<String> disabled = Core.config.getStringList("Launchpad.Disabled.Worlds");
						disabled.remove(p.getWorld().getName());
						List<String> enabled = Core.config.getStringList("Launchpad.Enabled.Worlds");
						enabled.add(p.getWorld().getName());
						return true;
					}
					s.sendMessage(ChatColor.RED + 
							"You are missing the 'core.launchpads.use' permission.");
					s.sendMessage(ChatColor.RED + 
							"Please contact support.");
					return true;
				}
				if(a[0].equals("disable"))
				{
					if(s.hasPermission("core.launchpads.use"))
					{
						List<String> disabled = Core.config.getStringList("Launchpad.Enabled.Worlds");
						disabled.remove(p.getWorld().getName());
						List<String> enabled = Core.config.getStringList("Launchpad.Disabled.Worlds");
						enabled.add(p.getWorld().getName());
					}
					s.sendMessage(ChatColor.RED + 
							"You are missing the 'core.launchpads.use' permission.");
					s.sendMessage(ChatColor.RED + 
							"Please contact support.");
					return true;
				}
			}
		}
		return false;
	}
}
