package Core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
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
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import Commands.Check;
import Commands.SetSpawn;
import Events.$1PlayerMoveEvent;
import Events.VoidSpawn;
import Events.WorldChangeEvent;

public class Main
  extends JavaPlugin
  implements Listener
{
	private static Main instance;
	public static File plugin;
	public static File configFile;
	public FileConfiguration config;
	private SetSpawn sspawn;
	
	private int count;
	private int taskID;
	
	Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED + getConfig().getString("Menu-Name"));
	public HashMap<String, Integer> hm= new HashMap<>();
	
  public void onEnable()
  {
	instance = this;
	this.sspawn = new SetSpawn();
	
	getCommand("setspawn").setExecutor(this.sspawn);
	getCommand("spawn").setExecutor(this);
    getCommand("agtest").setExecutor(this);
    getCommand("servers").setExecutor(this);
    getCommand("check").setExecutor(new Check());
    getCommand("lp").setExecutor(this);
    getCommand("core").setExecutor(this);
    
    Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    
    getServer().getPluginManager().registerEvents(this, this);
    getServer().getPluginManager().registerEvents(new VoidSpawn(), this);
    getServer().getPluginManager().registerEvents(new $1PlayerMoveEvent(), this);
    getServer().getPluginManager().registerEvents(new WorldChangeEvent(), this);
    
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    console.sendMessage("§b------------------------------------------");
    console.sendMessage("§3Core");
    console.sendMessage("§3Author: Arcader2k");
    console.sendMessage("§3Version: " +  getServer().getPluginManager().getPlugin("AGCore")
            .getDescription().getVersion());
    console.sendMessage("§b------------------------------------------");
    
    loadConfig();
    
  }
public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg)
  {
	  Player p = (Player) sender;
	  if (cmd.getName().equalsIgnoreCase("agtest"))
	  {
	    if ((sender.hasPermission("core.test")))
	    {
	    	sender.sendMessage("§b----------- [ §aAGCore §b]-----------");
		    sender.sendMessage("§2Active: §a§l" +
		     		getServer().getPluginManager().isPluginEnabled("AGCore"));
		    sender.sendMessage("§2Version: §a§l" + 
		      		getServer().getPluginManager().getPlugin("AGCore")
		        .getDescription().getVersion());
		    sender.sendMessage("§2Author: §aArcader2k");
		    sender.sendMessage("§b-------------------------------");
		    return true;
	    }
	    else
	    {
	    	sender.sendMessage("§cYou are missing the 'core.test' permission.");
	    	sender.sendMessage("§cPlease contact support.");
			return true;
	    }
     }
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
			 sender.sendMessage("§cPlease contact support.");
			 return true;
			}
		 }
	  
	  if(cmd.getName().equalsIgnoreCase("spawn"))
	  {
		  if(!hm.containsKey(p.getName()))
		  {
			  if(sender.hasPermission("core.spawn"))
			  {
				  this.hm.put(p.getName(), 0);
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
							  hm.remove(p.getName());
							  stopTimer();
							  return;
						  }
					  }
				  }, 60L, 20L);		  
			  }
			  else
			  {
				  sender.sendMessage("§cYou are missing the 'core.spawn' permission.");
				  sender.sendMessage("§cPlease contact support.");
				  return true;
			  }
		  }
	  }
	  if(cmd.getName().equalsIgnoreCase("lp"))
	  {
		  
		  if(arg.length == 1)
		  {
			  if(arg[0].equalsIgnoreCase("enable"))
			  {
				  if(sender.hasPermission("core.pads.enable"))
				  {
					  String world = p.getWorld().getName();
					  
					  getConfig().set("LaunchPad.Enabled.World", world);
					  saveConfig();
					  sender.sendMessage("§c§lLaunchpads >> §a§lEnabled!");
				  }
				  sender.sendMessage("§cYou are missing the 'core.pads.enable' permission.");
				  sender.sendMessage("§cPlease contact support.");
				  return true;
				  
			  }
			  if(arg[0].equalsIgnoreCase("disable"))
			  {
				  if(sender.hasPermission("core.pads.disable"))
				  {
					  getConfig().set("LaunchPad.Enabled.World", "");
					  saveConfig();
					  sender.sendMessage("§c§lLaunchpads >> §4§lDisabled!");
				  }
				  sender.sendMessage("§cYou are missing the 'core.pads.disable' permission.");
				  sender.sendMessage("§cPlease contact support.");
				  return true;
				  
			  }
		  }
		  sender.sendMessage("§cUsage: <enable/disable>");
		  return true;
	  }
	  if(cmd.getName().equalsIgnoreCase("core"))
	  {
		  if(arg.length == 1)
		  {
			  if(arg[0].equalsIgnoreCase("reload"))
			  {
				  if(sender.hasPermission("core.reload"))
				  {
					  instance.reloadConfig();
					  sender.sendMessage("§aComplete");
				  }
				  sender.sendMessage("§cYou are missing the 'core.reload' permission.");
				  sender.sendMessage("§cPlease contact support.");
				  return true;
			  }
		  }
	  }
	  return true;
  }
  
  @EventHandler
  public void onMove(PlayerMoveEvent e)
  {
	if(this.hm.containsKey(e.getPlayer().getName()))
	{
	  if(e.getFrom().getBlockX() != e.getTo().getBlockX())
	  {
		  stopTimer();
		  this.hm.remove(e.getPlayer().getName());
		  e.getPlayer().sendMessage("§cYou moved! Teleportation cancelled.");
		  return;
	  }
	 }
	}
	public void stopTimer()
	{
		Bukkit.getScheduler().cancelTask(taskID);
	}
  public void loadConfig()
  {
	  plugin = getDataFolder();
	  configFile = new File(plugin, "Config.yml");
	    
	  config = new YamlConfiguration();
	  if (!plugin.exists()) 
	  {
	    plugin.mkdir();
	  }
	  if (!configFile.exists()) 
	  {
	    try
	    {
	  	  getLogger().info("Config.yml not found, creating!");
	   	  configFile.createNewFile();
	   	  saveDefaultConfig();
	    }
	    catch (IOException e) 
	    {
	   	  e.printStackTrace();
	    }
	  }
	  String world = "Spawn.World";
	  String X = "Spawn.X";
	  String Y = "Spawn.Y";
	  String Z = "Spawn.Z";
	  String prefix = "Prefix";
	  String server1 = "Servers.1";
	  String server2 = "Servers.2";
	  String server3 = "Servers.3";
	  String server4 = "Servers.4";
	  
	  double v = 2;
	  getConfig().addDefault(prefix, "[AGCore]");
	  getConfig().addDefault("Menu-Name", "Servers");
	  
	  getConfig().addDefault(server1, "Factions");
	  getConfig().addDefault(server2, "Skyblock");
	  getConfig().addDefault(server3, "KitPVP");
	  getConfig().addDefault(server4, "Hub");
	  
	  getConfig().addDefault(world, "world");
	  getConfig().addDefault(X, "0");
	  getConfig().addDefault(Y, "0");
	  getConfig().addDefault(Z, "0");
	  
	  getConfig().addDefault("LaunchPad.Velocity", v);
	  getConfig().addDefault("LaunchPad.Enabled.World", "world");
	  
	  getConfig().options().copyDefaults(true);
	  saveConfig();
  }
  public void onDisable() 
  {}
  public static synchronized Main getInstance()
  {
	  return instance;
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
	  
	  if(!ChatColor.stripColor(e.getInventory().getName()).equals(getConfig().getString("Menu-Name")))
	  {
		  return;
	  }
	  else
	  {
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
