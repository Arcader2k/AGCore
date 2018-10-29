package Core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

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
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import Commands.CoreCmd;
import Events.VoidSpawn;

public class Core extends JavaPlugin implements Listener 
{
	private int count;
	private int taskID;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public HashMap<String, Integer> hm = new HashMap();
	private File deathMessagesFolder;
	private Set<Enum<?>> damager;
	private static Core instance;
	public static File plugin;
	public static File configFile;
	public static FileConfiguration config;
	private CoreCmd ccmd;
	ConsoleCommandSender c;
	Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED + "Available Servers");
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void onEnable()
	{
		this.ccmd = new CoreCmd();
		
		this.damager = new HashSet();
	    this.damager.addAll(Arrays.asList(EntityType.values()));
	    this.damager.addAll(Arrays.asList(EntityDamageEvent.DamageCause.values()));
	    this.damager.addAll(Arrays.asList(OtherDeaths.values()));
	    this.deathMessagesFolder = new File(getDataFolder(), "Death Messages");
	    createFolder(this.deathMessagesFolder);
	    createDeathFiles();
	    createConfig();
		
		getCommand("setspawn").setExecutor(this);
		getCommand("spawn").setExecutor(this);
		getCommand("core").setExecutor(this.ccmd);
		getCommand("servers").setExecutor(this);
		
		Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new VoidSpawn(), this);
		
		c = Bukkit.getServer().getConsoleSender();
		c.sendMessage("§b----------- [ §aAGCore §b]-----------");
	    c.sendMessage("§2Active: §a§l" +
	    		this.getServer().getPluginManager().isPluginEnabled("AGCore"));
	    c.sendMessage("§2Version: §a§l" + 
	    		this.getServer().getPluginManager().getPlugin("AGCore")
	        .getDescription().getVersion());
	    c.sendMessage("");
	    c.sendMessage("§2Author: §aArcader2k");
	    c.sendMessage("§b----------------------------------------");
		
	}
	public static enum OtherDeaths{GROUND;}  
	private void createFolder(File file){if (!file.exists()){file.mkdirs();}}  
	private void createDeathFiles(){for (Enum<?> e : this.damager){createDeathFile(e);}}  
	private void createDeathFile(Enum<?> e)
	{
	   File f = new File(this.deathMessagesFolder, e.toString() + ".txt");
	   try
	   {
	     f.createNewFile();
	   }
	   catch (IOException ex)
	   {
	     getLogger().warning("Failed to create the file " + f.getName());
	     ex.printStackTrace();
	   }
	 } 
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event)
	  {
	    Player player = event.getEntity();
	    EntityDamageEvent lastDamageEvent = player.getLastDamageCause();
	    Enum<?> e = lastDamageEvent.getCause();
	    if ((lastDamageEvent instanceof EntityDamageByEntityEvent))
	    {
	      EntityDamageByEntityEvent damageEvent = (EntityDamageByEntityEvent)lastDamageEvent;
	      e = damageEvent.getDamager().getType();
	      if ((damageEvent.getDamager() instanceof Arrow))
	      {
	        Arrow arrow = (Arrow)damageEvent.getDamager();
	        if ((arrow.getShooter() instanceof LivingEntity))
	        {
	          LivingEntity livingEntity = (LivingEntity)arrow
	            .getShooter();
	          e = livingEntity.getType();
	        }
	      }
	    }
	    else if ((e == EntityDamageEvent.DamageCause.FALL) && (player.getFallDistance() <= 5.0F))
	    {
	      e = OtherDeaths.GROUND;
	    }
	    String deathMessage = getDeathMessage(event, e);
	    event.setDeathMessage(deathMessage);
	  }
	  
	private String getDeathMessage(PlayerDeathEvent event, Enum<?> e)
	{
	  File file = new File(this.deathMessagesFolder, e.toString() + ".txt");
	  String message = event.getDeathMessage();
	  if (file.exists())
	  {
	    List<String> contents = getFileContents(file);
	    message = configureDeathMessage(event, contents);
	  }
	  else{getLogger().warning("Could not find the file: " + file.getName());
	  }
	  return message;
	}  
	  @SuppressWarnings("deprecation")
	private String configureDeathMessage(PlayerDeathEvent event, List<String> contents)
	{
	  String message = event.getDeathMessage();
	  if (contents.size() > 0)
	  {
	    Random ran = new Random();
	    int selected = ran.nextInt(contents.size());
	    message = (String)contents.get(selected);
	    if (!message.equals(event.getDeathMessage()))
	    {
	      if (message.startsWith("###")) {
	        message = message.replaceFirst("###", "");
	      } else {
	        message ="" + message;
	      }
	      Player plyr = event.getEntity();
	      message = message.replace("%player%", plyr.getName());
	      if (message.contains("%killer%"))
	      {
	        Player kllr = plyr.getKiller();
	        if (kllr == null)
	        {
	          contents.remove(selected);
	          return configureDeathMessage(event, contents);
	        }
	        message = message.replace("%killer%", kllr.getName());
	        ItemStack stack = kllr.getItemInHand();
	        if (stack.getType() == Material.AIR)
	        {
	          message = message.replace("%weapon%", "Fists");
	        }
	        else
	        {
	          String item = "";
	          if ((stack.hasItemMeta()) && (stack.getItemMeta().hasDisplayName()))
	          {
	            item = stack.getItemMeta().getDisplayName();
	          }
	          else
	          {
	            String[] stringArray = stack.getType()
	              .toString().split("_");
	            for (int i = 0; i < stringArray.length; i++) {
	              item = item + stringArray[i].charAt(0) + stringArray[i].substring(1).toLowerCase() + (i == stringArray.length - 1 ? "" : 
	                " ");
	            }
	          }
	          message = message.replace("%weapon%", item);
	        }
	      }
	      message = ChatColor.translateAlternateColorCodes('&', message);
	    }
	  }
	  return message;
	}
	  
	private List<String> getFileContents(File file)
	{
	  @SuppressWarnings({ "unchecked", "rawtypes" })
	  List<String> list = new ArrayList();
	  try
	    {
	      FileInputStream fs = new FileInputStream(file);
	      BufferedReader br = new BufferedReader(new InputStreamReader(fs));
	      String lineText;
	      while ((lineText = br.readLine()) != null)
	      {
	        if (lineText.trim().length() > 0) 
	        {
	          list.add(lineText);
	        }
	      }
	      br.close();
	    }
	    catch (Exception e)
	    {
	      getLogger().warning("Failed to read the file " + file.getName());
	      e.printStackTrace();
	    }
	    return list;
	}
	public void reloadConfig()
	{
	  if(configFile == null)
	  {
		  configFile = new File(plugin, "Config.yml");
	  }
	  config = YamlConfiguration.loadConfiguration(configFile);
	}
	public boolean onCommand(CommandSender s, Command c, String a, String[] str)
	{
		Player p = (Player) s;
		if(c.getName().equalsIgnoreCase("setspawn"))
		{
			if(s.hasPermission("core.setspawn"))
			{
				Location loc = new Location(Bukkit.getWorld(Core.config.getString("Spawn.Location.World")),
						Core.config.getDouble("Spawn.Location.X"), 
						Core.config.getDouble("Spawn.Location.Y"), Core.config.getDouble("Spawn.Location.Z"));
				Bukkit.getServer().getWorld(Core.config.getString("Spawn.Location.World")).setSpawnLocation(loc);
				  
				Core.config.set("Spawn.Location.World", p.getWorld().getName());
				Core.config.set("Spawn.Location.X", p.getLocation().getX());
				Core.config.set("Spawn.Location.Y", p.getLocation().getY());
				Core.config.set("Spawn.Location.Z", p.getLocation().getZ());
				Core.config.set("Spawn.Location.Yaw", p.getLocation().getYaw());
				Core.config.set("Spawn.Location.Pitch", p.getLocation().getPitch());
				this.saveConfig();
				  
				s.sendMessage(Core.config.getString("Prefix") + " §aSpawn Set!");
				return true;
			}
			else
			{
			    s.sendMessage("§cYou are missing the 'core.setspawn' permission.");
			    s.sendMessage("§cPlease contact support.");
			    return true;
		    }  
	    }
		if(a.equalsIgnoreCase("servers"))
		{
			if(p.hasPermission("core.servers"))
			{
				addItems();
				p.openInventory(inv);
			}
		}
		if(c.getName().equalsIgnoreCase("spawn"))
		{
			 if(!this.hm.containsKey(p.getName()))
			 {
				 if(s.hasPermission("core.spawn"))
				 {
					 this.hm.put(p.getName(), Integer.valueOf(0));
					 p.sendMessage(config.getString("Prefix").replace('&', '§') + " §cTeleporting in "+"§c§l3" + " §cseconds..");
					 BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
					 this.taskID = scheduler.scheduleSyncRepeatingTask(Core.getInstance(), new Runnable()
					 {
					    @Override
						public void run()
						{
						  if(count == 0)
						  {
							 p.sendMessage("§3Teleporting...");
							 Location loc = new Location(Bukkit.getWorld(config.getString("Spawn.Location.World")),
								  config.getDouble("Spawn.Location.X"), 
								  config.getDouble("Spawn.Location.Y"), config.getDouble("Spawn.Location.Z"), 
								  (float) config.getDouble("Spawn.Location.Yaw"), 
								  (float) config.getDouble("Spawn.Location.Pitch"));
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
				  s.sendMessage("§cYou are missing the 'core.spawn' permission.");
				  s.sendMessage("§cPlease contact support.");
				  return true;
				 }
			 }
		}
		return false;
		
	}
	public void stopTimer()
	{
		Bukkit.getScheduler().cancelTask(taskID);
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
	public void createConfig()
	{
		plugin = getDataFolder();
		configFile = new File(getDataFolder(), "Config.yml");
		
		config = new YamlConfiguration();
		if(!plugin.exists())
		{
			plugin.mkdir();
		}
		if(!configFile.exists())
		{
			try
			{
				getLogger().info("Config.yml not found.. Creating one for you!");
				configFile.createNewFile();
				this.saveDefaultConfig();
			}
			catch(IOException e)
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
		config.addDefault(X, "100");
		config.addDefault(Y, "100");
		config.addDefault(Z, "100");
		config.addDefault(Yaw, "100");
		config.addDefault(Pitch, "100");
		 
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
	    this.saveConfig();
	}
	public void saveConfig()
	{
		try
		{
			config.save(configFile);
		}
		catch(IOException e)
		{
			Bukkit.getServer().getConsoleSender()
			.sendMessage(ChatColor.GREEN + "Could not save config.yml");
		}
	}
	public void onDisable() {}
	public void addItems()
	{
		  ItemStack bench = new ItemStack (Material.WORKBENCH);
		  ItemMeta benchMeta = bench.getItemMeta();
		  benchMeta.setDisplayName("§b" + config.getString("Servers.First"));
		  ArrayList<String> lore1 = new ArrayList<String>();
		  lore1.add("§5♦ Build your base to defend against the enemies!");
		  lore1.add("");
		  lore1.add("§5♦ Click here!");
		  
		  benchMeta.setLore(lore1);
		  bench.setItemMeta(benchMeta);
		  inv.setItem(0, bench);
		  
		  ItemStack bucket = new ItemStack (Material.BUCKET);
		  ItemMeta bucketMeta = bucket.getItemMeta();
		  bucketMeta.setDisplayName("§b" + config.getString("Servers.Second"));
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
		  swordMeta.setDisplayName("§b" + config.getString("Servers.Third"));
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
		  starMeta.setDisplayName("§b" + config.getString("Servers.Fourth"));
		  ArrayList<String> lore4 = new ArrayList<String>();
		  lore4.add("§5♦ Going back to Hub?");
		  lore4.add("");
		  lore4.add("§5♦ Click here!");
		  
		  starMeta.setLore(lore4);
		  star.setItemMeta(starMeta);
		  inv.setItem(6, star);
		  
		  ItemStack isword = new ItemStack (Material.IRON_SWORD);
		  ItemMeta imeta = isword.getItemMeta();
		  imeta.setDisplayName("§b" + config.getString("Servers.Fifth"));
		  ArrayList<String> lore5 = new ArrayList<String>();
		  lore5.add("§5♦ Need some training?");
		  lore5.add("§5♦ Start your training today!");
		  lore5.add("");
		  lore5.add("§5♦ Click here!");
		  
		  imeta.setLore(lore5);
		  isword.setItemMeta(imeta);
		  inv.setItem(8, isword);
		  
		  checkSlots(this.inv);
	  }
	  @EventHandler
	  public void onInventoryClick(InventoryClickEvent e)
	  {
		  Player p = (Player) e.getWhoClicked();
		  
		  if(!ChatColor.stripColor(e.getInventory().getName()).equals("Available Servers"))
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
			   		p.sendMessage("§3Warping to " + config.getString("Servers.First") + "...");
			   		teleportToServer(p, config.getString("Servers.First"));
			   		break;
			   	case 2:
			   		p.closeInventory();
			   		p.sendMessage("§3Warping to " + config.getString("Servers.Second") + "...");
			   		teleportToServer(p, config.getString("Servers.Second"));
			   		break;
			   	case 4:
			   		p.closeInventory();
			   		p.sendMessage("§3Warping to " + config.getString("Servers.Third") + "...");
			   		teleportToServer(p, config.getString("Servers.Third"));
			   		break;
			   	case 6:
			   		p.closeInventory();
			   		p.sendMessage("§3Warping to " + config.getString("Servers.Fourth") + "...");
			   		teleportToServer(p, config.getString("Servers.Fourth"));
			  		break;
			   	case 8:
			   		p.closeInventory();
			   		p.sendMessage("§3Warping to " + config.getString("Servers.Fifth") + "...");
			   		teleportToServer(p, config.getString("Servers.Fifth"));
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
	  public static Core getInstance() {return instance;}
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
