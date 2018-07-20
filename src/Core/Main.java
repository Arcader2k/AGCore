package Core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import Commands.Check;
import Events.$1PlayerMoveEvent;
import Events.VoidSpawn;
import Events.WorldChangeEvent;

public class Main
  extends JavaPlugin
  implements Listener
{
	public static File plugin;
	public static File configFile;
	public static FileConfiguration config;
	
	Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED + "Menu" );
	
  public void onEnable()
  {
    getCommand("agtest").setExecutor(this);
    getCommand("servers").setExecutor(this);
    getCommand("check").setExecutor(new Check());
    getCommand("agcore").setExecutor(this);
    
    Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    
    getServer().getPluginManager().registerEvents(this, this);
    getServer().getPluginManager().registerEvents(new VoidSpawn(), this);
    getServer().getPluginManager().registerEvents(new $1PlayerMoveEvent(), this);
    getServer().getPluginManager().registerEvents(new WorldChangeEvent(), this);
    
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    console.sendMessage("§b------------------------------------------");
    console.sendMessage("§3AGCore");
    console.sendMessage("§3Author: Arcader2k");
    console.sendMessage("§3Version: " +  getServer().getPluginManager().getPlugin("AGCore")
            .getDescription().getVersion());
    console.sendMessage("§b------------------------------------------");
    
    plugin = getDataFolder();
    configFile = new File(plugin, "config.yml");
    
    config = new YamlConfiguration();
    if (!plugin.exists()) 
    {
      plugin.mkdir();
    }
    if (!configFile.exists()) 
    {
      try
      {
        configFile.createNewFile();
      }
      catch (IOException e) 
      {
    	  e.printStackTrace();
      }
    }
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg)
  {
	  Player p = (Player) sender;
	  if(cmd.getName().equalsIgnoreCase("agcore")) 
		{
			sender.sendMessage("§bThis plugin was created by Arcader2k");
			return true;
		}
	  if(cmd.getName().equalsIgnoreCase("servers") && sender instanceof Player)
	  {
		  addItems();
		  p.openInventory(inv);
	  }
	  
	  if (cmd.getName().equalsIgnoreCase("agtest"))
	  {
	    if ((sender instanceof Player))
	    {
	      sender.sendMessage(ChatColor.AQUA + "----------- [" + ChatColor.GREEN + " AGCore" + ChatColor.AQUA + " ]-----------");
	      sender.sendMessage(ChatColor.DARK_GREEN + "Active: " + ChatColor.GREEN + ChatColor.BOLD + 
	      		getServer().getPluginManager().isPluginEnabled("AGCore"));
	      sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + ChatColor.BOLD + 
	      		getServer().getPluginManager().getPlugin("AGCore")
	        .getDescription().getVersion());
	      sender.sendMessage(ChatColor.AQUA + "-------------------------------");
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
	    case 7:
	   		p.closeInventory();
	   		p.sendMessage("§3Connecting to " + getConfig().getString("Server1") + "...");
	   		teleportToServer(p, getConfig().getString("Server1"));
	   		break;
	   	case 1:
	   		p.closeInventory();
	   		p.sendMessage("§3Connecting to " + getConfig().getString("Server2") + "...");
	   		teleportToServer(p, getConfig().getString("Server2"));
	   		break;
	   	case 5:
	   		p.closeInventory();
	   		p.sendMessage("§3Connecting to " + getConfig().getString("Server3") + "...");
	   		teleportToServer(p, getConfig().getString("Server3"));
	   		break;
	   	case 3:
	   		p.closeInventory();
	   		p.sendMessage("§3Connecting to " + getConfig().getString("Server4") + "...");
	   		teleportToServer(p, getConfig().getString("Server4"));
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
		player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
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
