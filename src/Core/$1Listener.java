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

public class $1Listener
  extends JavaPlugin
  implements Listener
{
	Inventory inv = Bukkit.createInventory(null, 9 * 3, ("Menu"));
	
	public static File plugin;
	public static File configFile;
	public static FileConfiguration config;
	
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

    createItem(inv, Material.WORKBENCH, "Factions", 11);
    createItem(inv, Material.DIAMOND_SWORD, "KitPVP", 13);
    createItem(inv, Material.BUCKET, "SkyBlock", 15);
    checkSlots(inv);
    
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
    	  //
      }
    }
    getConfig().options().copyDefaults(true);
    saveConfig();
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg)
  {
	if(cmd.getName().equalsIgnoreCase("agcore")) 
	{
		sender.sendMessage("§bThis plugin was created by Arcader2k");
		return true;
	}
  
    if (cmd.getName().equalsIgnoreCase("agtest"))
    {
      if ((sender instanceof Player))
      {
        sender.sendMessage(ChatColor.AQUA + "----------- [" + ChatColor.GREEN + " AGCore" + ChatColor.AQUA + " ]-----------");
        sender.sendMessage(ChatColor.DARK_GREEN + "Active?: " + ChatColor.GREEN + ChatColor.BOLD + 
        		getServer().getPluginManager().isPluginEnabled("AGCore"));
        sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + ChatColor.BOLD + 
        		getServer().getPluginManager().getPlugin("AGCore")
          .getDescription().getVersion());
        sender.sendMessage(ChatColor.AQUA + "-------------------------------");
        return true;
      }
    }
    if(cmd.getName().equalsIgnoreCase("servers"))
    {
        if(!(sender instanceof Player)) 
        {
            sender.sendMessage("You must be in-game to use this command..");
            return true;
        }
        else
        {
      	  ((Player) sender).openInventory(inv);
        }
        return true;
    }
    return false;
  }
  @EventHandler
  private void onInventoryClick(InventoryClickEvent e)
  {

      if(!(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR))
      {

          Player p = (Player) e.getWhoClicked();

          switch (e.getCurrentItem().getType()){
              case BUCKET:
            	  e.setCancelled(true);
                  p.closeInventory();
                  teleportToServer(p, getConfig().getString("Server1"));
                  break;
              case WORKBENCH:
            	  e.setCancelled(true);
                  p.closeInventory();
                  teleportToServer(p, getConfig().getString("Server2"));
                  break;
              case DIAMOND_SWORD:
            	  e.setCancelled(true);
                  p.closeInventory();
                  teleportToServer(p, getConfig().getString("Server3"));
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
      } catch (IOException e) 
      {
          e.printStackTrace();
      }
      player.sendPluginMessage(this, "BungeeCord", b.toByteArray());
  }

  private String color(String str)
  {
      return ChatColor.translateAlternateColorCodes('&', str);
  }

  private void createItem(Inventory inv, Material material, String name, int slot){
      ItemStack item = new ItemStack(material, 1);
      ItemMeta meta = item.getItemMeta();
      meta.setDisplayName(color(name));
      item.setItemMeta(meta);
      inv.setItem(slot, item);
  }

  @SuppressWarnings("deprecation")
  private void checkSlots(Inventory inv)
  {
      ItemStack space;
      space = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getDyeData());
      ItemMeta meta = space.getItemMeta();
      meta.setDisplayName(ChatColor.BLACK + "");
      space.setItemMeta(meta);
      for(int i = 0;i < inv.getSize();i++)
      {
          if(inv.getItem(i) == (null) || inv.getItem(i).getType() == Material.AIR){
              inv.setItem(i, space);
          }
      }
  }
}
