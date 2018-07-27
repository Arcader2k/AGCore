package Core;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import Commands.Check;
import Commands.Servers;
import Commands.SetSpawn;
import Commands.Spawn;
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
	private Spawn spawn;
	private Servers servers;
	
	Inventory inv = Bukkit.createInventory(null, 9, ChatColor.RED + "Menu" );
	public HashMap<String, Integer> hm= new HashMap<>();
	
  public void onEnable()
  {
	instance = this;
	this.sspawn = new SetSpawn();
	
	getCommand("setspawn").setExecutor(this.sspawn);
	getCommand("spawn").setExecutor(this.spawn);
    getCommand("agtest").setExecutor(this);
    getCommand("servers").setExecutor(this.servers);
    getCommand("check").setExecutor(new Check());
    
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
			return true;
	    }
     }
    return false;
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
	  this.config.addDefault(prefix, "[AGCore]");
	  
	  this.config.addDefault(server1, "Factions");
	  this.config.addDefault(server2, "Skyblock");
	  this.config.addDefault(server3, "KitPVP");
	  this.config.addDefault(server4, "Hub");
	  
	  this.config.addDefault(world, "world");
	  this.config.addDefault(X, "0");
	  this.config.addDefault(Y, "0");
	  this.config.addDefault(Z, "0");
	  this.config.options().copyDefaults(true);
	  saveConfig();
  }
  public void onDisable() {}
  public static synchronized Main getInstance()
  {
	  return instance;
  }
}
