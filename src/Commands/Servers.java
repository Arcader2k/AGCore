package Commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import Core.Main;

public class Servers implements CommandExecutor 
{
	
	Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED + "Menu" );

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] arg3) 
	{
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("servers"))
		{
		  if(sender.hasPermission("core.servers"))
	      {
		     addItems();
			  p.openInventory(inv);
			}
			else
			{
			 sender.sendMessage("§cYou are misssing the 'core.servers' permission.");
			 return true;
			}
		 }
	return false;
	}
	
	public void addItems()
	  {
		  ItemStack bench = new ItemStack (Material.WORKBENCH);
		  ItemMeta benchMeta = bench.getItemMeta();
		  benchMeta.setDisplayName("§bFactions");
		  bench.setItemMeta(benchMeta);
		  inv.setItem(1, bench);
		  
		  ItemStack bucket = new ItemStack (Material.BUCKET);
		  ItemMeta bucketMeta = bucket.getItemMeta();
		  bucketMeta.setDisplayName("§bSkyblock");
		  bucket.setItemMeta(bucketMeta);
		  inv.setItem(3, bucket);
		  
		  ItemStack sword = new ItemStack (Material.DIAMOND_SWORD);
		  ItemMeta swordMeta = sword.getItemMeta();
		  swordMeta.setDisplayName("§bKitPVP");
		  sword.setItemMeta(swordMeta);
		  inv.setItem(5, sword);
		  
		  ItemStack star = new ItemStack (Material.NETHER_STAR);
		  ItemMeta starMeta = star.getItemMeta();
		  starMeta.setDisplayName("§bHub");
		  star.setItemMeta(starMeta);
		  inv.setItem(7, star);
		  
		  checkSlots(inv);
	  }
	  
	  @EventHandler
	  public void onInventoryClick(InventoryClickEvent e)
	  {
		  Player p = (Player) e.getWhoClicked();
		  
		  if(!ChatColor.stripColor(e.getInventory().getName()).equals("Menu")) 
				 return;
		  
		  if(e.getCurrentItem()==null || e.getCurrentItem().getType()==Material.AIR||!e.getCurrentItem().hasItemMeta())
		  {
			  return;
		  }
		  switch (e.getSlot())
		  {
		    case 1:
		   		p.closeInventory();
		   		p.sendMessage("§3Connecting to " + Main.getInstance().getConfig().getString("Servers.1") + "...");
		   		teleportToServer(p, Main.getInstance().getConfig().getString("Servers.1"));
		   		break;
		   	case 3:
		   		p.closeInventory();
		   		p.sendMessage("§3Connecting to " + Main.getInstance().getConfig().getString("Servers.2") + "...");
		   		teleportToServer(p, Main.getInstance().getConfig().getString("Servers.2"));
		   		break;
		   	case 5:
		   		p.closeInventory();
		   		p.sendMessage("§3Connecting to " + Main.getInstance().getConfig().getString("Servers.3") + "...");
		   		teleportToServer(p, Main.getInstance().getConfig().getString("Servers.3"));
		   		break;
		   	case 7:
		   		p.closeInventory();
		   		p.sendMessage("§3Connecting to " + Main.getInstance().getConfig().getString("Servers.4") + "...");
		   		teleportToServer(p, Main.getInstance().getConfig().getString("Servers.4"));
		  		break;
		   	default:
		   		e.setCancelled(true);
		   		break;
		  }
	    }
	  public void teleportToServer(Player player, String server)
		{
			ByteArrayOutputStream b = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(b);
			try 
			{
			    out.writeUTF("Connect");
			    out.writeUTF(server);
			} 
			catch (IOException e) 
			{
			    e.printStackTrace();
			}
			player.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
		}
	  @SuppressWarnings({ "deprecation"})
	  private void checkSlots(Inventory inv)
		{
		    ItemStack space;
		    space = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.WHITE.getDyeData());
		    ItemMeta meta = space.getItemMeta();
		    meta.setDisplayName(ChatColor.BLACK + "");
		    space.setItemMeta(meta);
		    for(int i = 0;i < inv.getSize();i++)
		    {
		        if(inv.getItem(i) == (null) || inv.getItem(i).getType() == Material.AIR)
		        {
		            inv.setItem(i, space);
		        }
		    }
		}

}
