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

import Commands.SetSpawn;
import Events.$1PlayerMoveEvent;
import Events.DeathEvents;
import Events.VoidSpawn;
import Events.WorldChangeEvent;

public class Main
  extends JavaPlugin implements Listener 
  {
	private static Main instance;
	public static FileConfiguration config;
	public static FileConfiguration messages;
	public File cFile;
	public File dFile;
	
	private int count;
	private int taskID;
	private int reloadID;
	
	Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED + getConfig().getString("Menu-name"));
	public HashMap<String, Integer> hm= new HashMap<>();
	
    public void onEnable()
    {
	  this.getCommand("setspawn").setExecutor(new SetSpawn());
	  this.getCommand("spawn").setExecutor(this);
	  this.getCommand("servers").setExecutor(this);
	  this.getCommand("lp").setExecutor(this);
	  this.getCommand("core").setExecutor(this);
	  this.getCommand("disablefly").setExecutor(this);
	  this.getCommand("enablefly").setExecutor(this);
    
      Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    
      this.getServer().getPluginManager().registerEvents(this, this);
      this.getServer().getPluginManager().registerEvents(new VoidSpawn(), this);
      this.getServer().getPluginManager().registerEvents(new $1PlayerMoveEvent(), this);
      this.getServer().getPluginManager().registerEvents(new WorldChangeEvent(), this);
      this.getServer().getPluginManager().registerEvents(new DeathEvents(), this);
    
      ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

      console.sendMessage("§b----------- [ §aAGCore §b]-----------");
      console.sendMessage("§2Active: §a§l" +
    		this.getServer().getPluginManager().isPluginEnabled("AGCore"));
      console.sendMessage("§2Version: §a§l" + 
    		this.getServer().getPluginManager().getPlugin("AGCore")
        .getDescription().getVersion());
      console.sendMessage("");
      console.sendMessage("§2Author: §aArcader2k");
      console.sendMessage("§b----------------------------------------");
      setupConfig();
      setupMessages();
  }
  
  public void setupConfig()
  {
	 if(!this.getDataFolder().exists())
	 {
		 this.getDataFolder().mkdir();
	 }
		
	 cFile = new File(this.getDataFolder(), "config.yml");
		
	 if(!cFile.exists())
	 {
		 try
		 {
			 cFile.createNewFile();
		 } catch(IOException e)
		 {
			 Bukkit.getServer().getConsoleSender()
			 .sendMessage(ChatColor.RED + "Could not load config.yml");
		 }	
	 }
	 config = YamlConfiguration.loadConfiguration(cFile);
	 Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "config.yml has been loaded!");
		
	 String world = "Spawn.Location.World";
	 String X = "Spawn.Location.X";
	 String Y = "Spawn.Location.Y";
	 String Z = "Spawn.Location.Z";
	 String Yaw = "Spawn.Location.Yaw";
	 String Pitch = "Spawn.Location.Pitch";
	 String prefix = "Prefix";
	 String Menu = "Menu-name";
	 String server1 = "Servers.First";
	 String server2 = "Servers.Second";
	 String server3 = "Servers.Third";
	 String server4 = "Servers.Fourth";
	 String server5 = "Servers.Fifth";
		  
	 double v = 2;
	 config.addDefault(prefix, "[AGCore]");
	 config.addDefault(Menu, "Server-List");
		  
	 config.addDefault(server1, "Factions");
	 config.addDefault(server2, "Skyblock");
	 config.addDefault(server3, "KitPVP");
	 config.addDefault(server4, "Hub");
	 config.addDefault(server5, "Pre-Pvp");
		
	 config.addDefault(world, "world");
	 config.addDefault(X, "0");
	 config.addDefault(Y, "0");
	 config.addDefault(Z, "0");
	 config.addDefault(Yaw, "0");
	 config.addDefault(Pitch, "0");
	 
	 config.addDefault("Launchpad.Velocity", v);
	 List<String> list = new ArrayList<String>();
	 list.add("world");
	 list.add("world_nether");
	 list.add("world_the_end"); 

	 config.set("Launchpad.Enabled.Worlds", list);
		  
	 List<String> enableFly = new ArrayList<String>();
	 enableFly.add("world");

	 config.set("Arena.Fly.Enabled.Worlds", enableFly);
		  
	 List<String> disableFly = new ArrayList<String>();
	 disableFly.add("Disable");
	 disableFly.add("Worlds");
	 disableFly.add("Here");

	 config.set("Arena.Fly.Disabled.Worlds", disableFly);
	 
	 config.options().copyDefaults(true);
	 saveConfig();
	}
	public void setupMessages()
	{
		dFile = new File(this.getDataFolder(), "death_messages.yml");
		
		if(!dFile.exists())
		{
			try
			{
				dFile.createNewFile();
			}
			catch(IOException e)
			{
				Bukkit.getServer().getConsoleSender()
				.sendMessage(ChatColor.RED + "Could not create death_messages.yml");
			}
		}
		messages = YamlConfiguration.loadConfiguration(dFile);
		Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "death_messages.yml has been loaded!");
		
		String drowning = "%player% drowned!";
		String block_explosion = "%player% exploded";
		String fall = "%player% fell to their death";
		String falling_block = "%player% was crushed";
		String fire = "%player% played with fire";
		String lava = "%player% tried to swim in lava";
		String lightning = "%player% was struck by lightning";
		String magic = "%player% played with dark magic";
		String poison = "%player% was poisoned";
		String projectile = "%player% was shot";
		String starvation = "%player% starved to death";
		String suicide = "%player% took their life";
		String thorns = "%player% tried to hug a cactus";
		String wither = "%player% died by wither";
		String entity_attack = "%player% died";
		String entity_explosion = "%player% died";
		String melted = "%player% melted";
		
		messages.addDefault("Death.Messages.Drowning", drowning);
		messages.addDefault("Death.Messages.Block_Explosion", block_explosion);
		messages.addDefault("Death.Messages.Fall", fall);
		messages.addDefault("Death.Messages.Falling_Block", falling_block);
		messages.addDefault("Death.Messages.Fire", fire);
		messages.addDefault("Death.Messages.Lava", lava);
		messages.addDefault("Death.Messages.Lightning", lightning);
		messages.addDefault("Death.Messages.Magic", magic);
		messages.addDefault("Death.Messages.Poison", poison);
		messages.addDefault("Death.Messages.Projectile", projectile);
		messages.addDefault("Death.Messages.Starvation", starvation);
		messages.addDefault("Death.Messages.Suicide", suicide);
		messages.addDefault("Death.Messages.Thorns", thorns);
		messages.addDefault("Death.Messages.Wither", wither);
		messages.addDefault("Death.Messages.Entity_Attack", entity_attack);
		messages.addDefault("Death.Messages.Entity_Explosion", entity_explosion);
		messages.addDefault("Death.Messages.Melted", melted);
		
		messages.options().copyDefaults(true);
        saveMessages();
		
	}
	public void saveConfig()
	{
		try
		{
			config.save(cFile);
		}
		catch(IOException e)
		{
			Bukkit.getServer().getConsoleSender()
			.sendMessage(ChatColor.GREEN + "Could not save config.yml");
		}
	}
	public void saveMessages()
	{
		try
		{
			messages.save(dFile);
		}
		catch(IOException e)
		{
			Bukkit.getServer().getConsoleSender()
			.sendMessage(ChatColor.GREEN + "Could not save death_messages.yml");
		}
	}
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg)
  {
	  Player p = (Player) sender;
	  String WORLD = p.getWorld().getName();
	  if(cmd.getName().equalsIgnoreCase("servers"))
	  {
		if(sender.hasPermission("core.servers") || sender.hasPermission("core.*"))
	    {
			setupMenu();
			p.openInventory(inv);
		}
		else
		{
		    sender.sendMessage("§cYou are misssing the 'core.servers' permission.");
			sender.sendMessage("§cPlease contact support.");
			return true;
		}
	  }
	  if(cmd.getName().equalsIgnoreCase("disablefly"))
	  {
		  if(sender.hasPermission("core.abilities.fly.disable"))
		  {
			  
			  if(this.getConfig().getStringList("Arena.Fly.Disabled.Worlds").contains(WORLD))
			  {
				  sender.sendMessage("§2§lFly >> §aFly has already been disabled for this world.");
				  return true;
			  }
			  List<String> disabledWorld = this.getConfig().getStringList("Arena.Fly.Disabled.Worlds");
			  disabledWorld.add(WORLD);
			  this.getConfig().set("Arena.Fly.Disabled.Worlds", disabledWorld);
			  this.saveConfig();
			  
			  List<String> enabledWorld = this.getConfig().getStringList("Arena.Fly.Enabled.Worlds");
			  enabledWorld.remove(WORLD);
			  this.getConfig().set("Arena.Fly.Enabled.Worlds", enabledWorld);
			  this.saveConfig();
			  
			  sender.sendMessage("§2§lFly >> §a§lDisabled!");
			  return true;
		  }
		  sender.sendMessage("§cYou are missing the 'core.abilities.fly' permission.");
		  sender.sendMessage("§cPlease contact support.");
		  return true;
	  }
	  if(cmd.getName().equalsIgnoreCase("enablefly"))
	  {
		  if(sender.hasPermission("core.abilities.fly.enable"))
		  { 
			  if(this.getConfig().getStringList("Arena.Fly.Enabled.Worlds").contains(WORLD))
			  {
				  sender.sendMessage("§2§lFly >> §aFly has already been enabled for this world.");
				  return true;
			  }
			  List<String> disabledWorld = this.getConfig().getStringList("Arena.Fly.Disabled.Worlds");
			  disabledWorld.remove(WORLD);
			  this.getConfig().set("Arena.Fly.Disabled.Worlds", disabledWorld);
			  this.saveConfig();
			  
			  List<String> enabledWorld = this.getConfig().getStringList("Arena.Fly.Enabled.Worlds");
			  enabledWorld.add(WORLD);
			  this.getConfig().set("Arena.Fly.Enabled.Worlds", enabledWorld);
			  this.saveConfig();
			  
			  sender.sendMessage("§2§lFly >> §a§lEnabled!");
			  return true;
		  }
		  sender.sendMessage("§cYou are missing the 'core.abilities.fly' permission.");
		  sender.sendMessage("§cPlease contact support.");
		  return true;
	  }
	  if(cmd.getName().equalsIgnoreCase("spawn"))
	  {
		  if(!hm.containsKey(p.getName()))
		  {
			  if(sender.hasPermission("core.spawn"))
			  {
				  this.hm.put(p.getName(), 0);
				  p.sendMessage(this.getConfig().getString("Prefix").replace('&', '§') + "§cTeleporting in "+"§c§l3" + " §cseconds..");
				  BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
				  taskID = scheduler.scheduleSyncRepeatingTask((Plugin) this, new Runnable()
				  {
					@Override
					  public void run()
					  {
						  if(count == 0)
						  {
							  p.sendMessage("§3Teleporting...");
							  Location loc = new Location(Bukkit.getWorld(getConfig().getString("Spawn.Location.World")),
									  getConfig().getDouble("Spawn.Location.X"), 
									  getConfig().getDouble("Spawn.Location.Y"), getConfig().getDouble("Spawn.Location.Z"), 
									  (float) getConfig().getDouble("Spawn.Location.Yaw"), 
									  (float) getConfig().getDouble("Spawn.Location.Pitch"));
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
					  if(this.getConfig().getStringList("Launchpad.Enabled.Worlds").contains(WORLD))
					  {
						  sender.sendMessage("§c§lLaunchpad >> §aWorld is already enabled!");
						  return true;
					  }
					  List<String> worlds = this.getConfig().getStringList("Launchpad.Enabled.Worlds");
					  worlds.add(WORLD);
					  this.getConfig().set("Launchpad.Enabled.Worlds", worlds);
					  this.saveConfig();
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
					  if(!this.getConfig().getStringList("Launchpad.Enabled.Worlds").contains(WORLD))
					  {
						  sender.sendMessage("§c§lLaunchpad >> §4World has already been disabled!");
						  return true;
					  }
					  List<String> worlds = this.getConfig().getStringList("Launchpad.Enabled.Worlds");
					  worlds.remove(WORLD);
					  this.getConfig().set("Launchpad.Enabled.Worlds", worlds);
					  this.saveConfig();
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
					  sender.sendMessage(this.getConfig().getString("Prefix").replace('&', '§') + (" §cReloading config.."));
					  BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					  reloadID = scheduler.scheduleSyncRepeatingTask((Plugin) this, new Runnable()
					  {
						@Override
						  public void run()
						  {
							  if(count == 0)
							  {
								  reloadConfig();
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
					    		this.getServer().getPluginManager().isPluginEnabled("AGCore"));
					    sender.sendMessage("§2Version: §a§l" + 
					    		this.getServer().getPluginManager().getPlugin("AGCore")
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
  public void reloadConfig()
  {
	  if(cFile == null || dFile == null)
	  {
		  cFile = new File(getDataFolder(), "config.yml");
		  dFile = new File(getDataFolder(), "death_messages.yml");
	  }
	  config = YamlConfiguration.loadConfiguration(cFile);
	  messages = YamlConfiguration.loadConfiguration(dFile);
  }
  
  @EventHandler
  public void onMove(PlayerMoveEvent e)
  {
	if(this.hm.containsKey(e.getPlayer().getName()))
	{
	  if(e.getFrom().getBlockX() != e.getTo().getBlockX())
	  {
		  this.stopTimer();
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
  public void onDisable() 
  {}
  
  public void setupMenu()
  {
	  ItemStack bench = new ItemStack (Material.WORKBENCH);
	  ItemMeta benchMeta = bench.getItemMeta();
	  benchMeta.setDisplayName("§b" + this.getConfig().getString("Servers.First"));
	  ArrayList<String> lore1 = new ArrayList<String>();
	  lore1.add("§5♦ Build your base to defend against the enemies!");
	  lore1.add("");
	  lore1.add("§5♦ Click here!");
	  
	  benchMeta.setLore(lore1);
	  bench.setItemMeta(benchMeta);
	  inv.setItem(0, bench);
	  
	  ItemStack bucket = new ItemStack (Material.BUCKET);
	  ItemMeta bucketMeta = bucket.getItemMeta();
	  bucketMeta.setDisplayName("§b" + this.getConfig().getString("Servers.Second"));
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
	  swordMeta.setDisplayName("§b" + this.getConfig().getString("Servers.Third"));
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
	  starMeta.setDisplayName("§b" + this.getConfig().getString("Servers.Fourth"));
	  ArrayList<String> lore4 = new ArrayList<String>();
	  lore4.add("§5♦ Going back to Hub?");
	  lore4.add("");
	  lore4.add("§5♦ Click here!");
	  
	  starMeta.setLore(lore4);
	  star.setItemMeta(starMeta);
	  inv.setItem(6, star);
	  
	  ItemStack isword = new ItemStack (Material.IRON_SWORD);
	  ItemMeta imeta = isword.getItemMeta();
	  imeta.setDisplayName("§b" + this.getConfig().getString("Servers.Fifth"));
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
	  
	  if(!ChatColor.stripColor(e.getInventory().getName()).equals(getConfig().getString("Menu-name")))
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
		   		p.sendMessage("§3Warping to " + this.getConfig().getString("Servers.First") + "...");
		   		teleportToServer(p, this.getConfig().getString("Servers.First"));
		   		break;
		   	case 2:
		   		p.closeInventory();
		   		p.sendMessage("§3Warping to " + this.getConfig().getString("Servers.Second") + "...");
		   		teleportToServer(p, this.getConfig().getString("Servers.Second"));
		   		break;
		   	case 4:
		   		p.closeInventory();
		   		p.sendMessage("§3Warping to " + this.getConfig().getString("Servers.Third") + "...");
		   		teleportToServer(p, getConfig().getString("Servers.Third"));
		   		break;
		   	case 6:
		   		p.closeInventory();
		   		p.sendMessage("§3Warping to " + getConfig().getString("Servers.Fourth") + "...");
		   		teleportToServer(p, getConfig().getString("Servers.Fourth"));
		  		break;
		   	case 8:
		   		p.closeInventory();
		   		p.sendMessage("§3Warping to " + getConfig().getString("Servers.Fifth") + "...");
		   		teleportToServer(p, getConfig().getString("Servers.Fifth"));
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
	player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
  }
  public static Main getInstance() 
  {
	  return instance;
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