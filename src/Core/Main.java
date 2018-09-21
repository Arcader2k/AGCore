package Core;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	private int reloadID;
	
	Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED + getConfig().getString("Menu-Name"));
	public HashMap<String, Integer> hm= new HashMap<>();
	
  public void onEnable()
  {
	instance = this;
	this.sspawn = new SetSpawn();
	
	getCommand("setspawn").setExecutor(this.sspawn);
	getCommand("spawn").setExecutor(this);
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
	  String WORLD = p.getWorld().getName();
	  if(cmd.getName().equalsIgnoreCase("servers"))
		{
		  if(sender.hasPermission("core.servers") || sender.hasPermission("core.*"))
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
							  Location loc = new Location(Bukkit.getWorld(Main.getInstance().getConfig().getString("Spawn.Location.World")),
									  Main.getInstance().getConfig().getDouble("Spawn.Location.X"), 
									  Main.getInstance().getConfig().getDouble("Spawn.Location.Y"), Main.getInstance().getConfig().getDouble("Spawn.Location.Z"), 
									  (float) Main.getInstance().getConfig().getDouble("Spawn.Location.Yaw"), 
									  (float) Main.getInstance().getConfig().getDouble("Spawn.Location.Pitch"));
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
					  if(getConfig().getStringList("Launchpad.Enabled.Worlds").contains(WORLD))
					  {
						  sender.sendMessage("§c§lLaunchpad >> §aWorld is already enabled!");
						  return true;
					  }
					  List<String> worlds = getConfig().getStringList("Launchpad.Enabled.Worlds");
					  worlds.add(WORLD);
					  getConfig().set("Launchpad.Enabled.Worlds", worlds);
					  saveConfig();
					  sender.sendMessage("§c§lLaunchpad >> §a§lEnabled!");
					  return true;
				  }
				  sender.sendMessage("§cYou are missing the 'core.pads.enable' permission.");
				  sender.sendMessage("§cPlease contact support.");
				  return true;
				  
			  }
			  if(arg[0].equalsIgnoreCase("disable"))
			  {
				  if(sender.hasPermission("core.pads.disable"))
				  {
					  if(!getConfig().getStringList("Launchpad.Enabled.Worlds").contains(WORLD))
					  {
						  sender.sendMessage("§c§lLaunchpad >> §4World has already been disabled!");
						  return true;
					  }
					  List<String> worlds = getConfig().getStringList("Launchpad.Enabled.Worlds");
					  worlds.remove(WORLD);
					  getConfig().set("Launchpad.Enabled.Worlds", worlds);
					  saveConfig();
					  sender.sendMessage("§c§lLaunchpad >> §4§lDisabled!");
					  return true;
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
				  if(sender.hasPermission("core.reload") && sender instanceof Player || sender.hasPermission("core.*"))
				  {
					  sender.sendMessage(getConfig().getString("Prefix").replace('&', '§') + (" §cReloading config.."));
					  BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					  reloadID = scheduler.scheduleSyncRepeatingTask((Plugin) this, new Runnable()
					  {
						@Override
						  public void run()
						  {
							  if(count == 0)
							  {
								  instance.reloadConfig();
								  sender.sendMessage("§aComplete");
								  stopReload();
							  }
						  }
					  }, 40L, 20L);
					  return true; 
				  }
				  sender.sendMessage("§cYou are missing the 'core.reload' permission.");
				  sender.sendMessage("§cPlease contact support.");
				  return true;
			  }
			  if(arg[0].equalsIgnoreCase("info"))
			  {
				  if ((sender.hasPermission("core.test")))
				    {
				    	sender.sendMessage("§b----------- [ §aAGCore §b]-----------");
					    sender.sendMessage("§2Active: §a§l" +
					     		getServer().getPluginManager().isPluginEnabled("AGCore"));
					    sender.sendMessage("§2Version: §a§l" + 
					      		getServer().getPluginManager().getPlugin("AGCore")
					        .getDescription().getVersion());
					    sender.sendMessage("");
					    sender.sendMessage("§2Author: §aArcader2k");
					    sender.sendMessage("§b----------------------------------------");
					    return true;
				    }
				    else
				    {
				    	sender.sendMessage("§cYou are missing the 'core.test' permission.");
				    	sender.sendMessage("§cPlease contact support.");
						return true;
				    }
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
  public void stopReload()
  {
	Bukkit.getScheduler().cancelTask(reloadID);
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
	  String world = "Spawn.Location.World";
	  String X = "Spawn.Location.X";
	  String Y = "Spawn.Location.Y";
	  String Z = "Spawn.Location.Z";
	  String Yaw = "Spawn.Location.Yaw";
	  String Pitch = "Spawn.Location.Pitch";
	  String prefix = "Prefix";
	  String server1 = "Servers.First";
	  String server2 = "Servers.Second";
	  String server3 = "Servers.Third";
	  String server4 = "Servers.Fourth";
	  String server5 = "Servers.Fifth";
	  
	  double v = 2;
	  getConfig().addDefault(prefix, "[AGCore]");
	  getConfig().addDefault("Menu-Name", "Servers");
	  
	  getConfig().addDefault(server1, "Factions");
	  getConfig().addDefault(server2, "Skyblock");
	  getConfig().addDefault(server3, "KitPVP");
	  getConfig().addDefault(server4, "Hub");
	  getConfig().addDefault(server5, "Pre-Pvp");
	  
	  getConfig().addDefault(world, "world");
	  getConfig().addDefault(X, "0");
	  getConfig().addDefault(Y, "0");
	  getConfig().addDefault(Z, "0");
	  getConfig().addDefault(Yaw, "0");
	  getConfig().addDefault(Pitch, "0");
	  
	  getConfig().addDefault("Launchpad.Velocity", v);
	  List<String> list = new ArrayList<String>();
	  list.add("world");
	  list.add("world_nether");
	  list.add("world_the_end");

	  getConfig().set("Launchpad.Enabled.Worlds", list);
	  
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
	  ArrayList<String> lore1 = new ArrayList<String>();
	  lore1.add("§5♦ Build your base to defend against the enemies!");
	  lore1.add("");
	  lore1.add("§5♦ Click here!");
	  
	  benchMeta.setLore(lore1);
	  bench.setItemMeta(benchMeta);
	  inv.setItem(0, bench);
	  
	  ItemStack bucket = new ItemStack (Material.BUCKET);
	  ItemMeta bucketMeta = bucket.getItemMeta();
	  bucketMeta.setDisplayName("§bSkyblock");
	  ArrayList<String> lore2 = new ArrayList<String>();
	  lore2.add("§5♦ Start on your own or with friends");
	  lore2.add("§5 to build up your island and compete against others");
	  lore2.add("§5 to have the biggest and baddest island!");
	  lore2.add("");
	  lore2.add("§5♦ Click here!");
	  
	  bucketMeta.setLore(lore2);
	  bucket.setItemMeta(bucketMeta);
	  inv.setItem(2, bucket);
	  
	  ItemStack sword = new ItemStack (Material.DIAMOND_SWORD);
	  ItemMeta swordMeta = sword.getItemMeta();
	  swordMeta.setDisplayName("§bKitPVP");
	  ArrayList<String> lore3 = new ArrayList<String>();
	  lore3.add("§5♦ Ready to battle head-to-head");
	  lore3.add("§5 with other players?");
	  lore3.add("");
	  lore3.add("§5♦ Click here!");
	  
	  swordMeta.setLore(lore3);
	  sword.setItemMeta(swordMeta);
	  inv.setItem(4, sword);
	  
	  ItemStack star = new ItemStack (Material.NETHER_STAR);
	  ItemMeta starMeta = star.getItemMeta();
	  starMeta.setDisplayName("§bHub");
	  ArrayList<String> lore4 = new ArrayList<String>();
	  lore4.add("§5♦ Going back to Hub?");
	  lore4.add("");
	  lore4.add("§5♦ Click here!");
	  
	  starMeta.setLore(lore4);
	  star.setItemMeta(starMeta);
	  inv.setItem(6, star);
	  
	  ItemStack isword = new ItemStack (Material.IRON_SWORD);
	  ItemMeta imeta = isword.getItemMeta();
	  imeta.setDisplayName("§bPre-PVP");
	  ArrayList<String> lore5 = new ArrayList<String>();
	  lore5.add("§5♦ Need some training?");
	  lore5.add("§5♦ Start your training today!");
	  lore5.add("");
	  lore5.add("§5♦ Click here!");
	  
	  imeta.setLore(lore5);
	  isword.setItemMeta(imeta);
	  inv.setItem(8, isword);
	  
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
		    case 0:
		   		p.closeInventory();
		   		p.sendMessage("§3Warping to " + Main.getInstance().getConfig().getString("Servers.First") + "...");
		   		teleportToServer(p, Main.getInstance().getConfig().getString("Servers.First"));
		   		break;
		   	case 2:
		   		p.closeInventory();
		   		p.sendMessage("§3Warping to " + Main.getInstance().getConfig().getString("Servers.Second") + "...");
		   		teleportToServer(p, Main.getInstance().getConfig().getString("Servers.Second"));
		   		break;
		   	case 4:
		   		p.closeInventory();
		   		p.sendMessage("§3Warping to " + Main.getInstance().getConfig().getString("Servers.Third") + "...");
		   		teleportToServer(p, Main.getInstance().getConfig().getString("Servers.Third"));
		   		break;
		   	case 6:
		   		p.closeInventory();
		   		p.sendMessage("§3Warping to " + Main.getInstance().getConfig().getString("Servers.Fourth") + "...");
		   		teleportToServer(p, Main.getInstance().getConfig().getString("Servers.Fourth"));
		  		break;
		   	case 8:
		   		p.closeInventory();
		   		p.sendMessage("§3Warping to " + Main.getInstance().getConfig().getString("Servers.Fifth") + "...");
		   		teleportToServer(p, Main.getInstance().getConfig().getString("Servers.Fifth"));
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
