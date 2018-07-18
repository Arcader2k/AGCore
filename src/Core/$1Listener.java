package Core;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import Commands.Check;
import Events.$1PlayerMoveEvent;
import Events.VoidSpawn;
import Events.WorldChangeEvent;

public class $1Listener
  extends JavaPlugin
  implements Listener
{
	Inventory inv = Bukkit.createInventory(null, 9 * 3, (color("&2Available Servers")));
	
  public void onEnable()
  {
    getCommand("agtest").setExecutor(this);
    getCommand("servers").setExecutor(this);
    getCommand("check").setExecutor(new Check());
    
    PluginManager pm = Bukkit.getServer().getPluginManager();
    pm.registerEvents(this, this);
    pm.registerEvents(new VoidSpawn(), this);
    pm.registerEvents(new $1PlayerMoveEvent(), this);
    pm.registerEvents(new WorldChangeEvent(), this);
    
    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();

    console.sendMessage(ChatColor.GOLD + "AGCore");
    console.sendMessage(ChatColor.GOLD + "Author: Arcader2k");
    console.sendMessage(ChatColor.GOLD + "Version: " +  getServer().getPluginManager().getPlugin("AGCore")
            .getDescription().getVersion());

    this.getServer().getPluginManager().registerEvents(this, this);
    createItem(inv, Material.WORKBENCH, "Factions", 11);
    createItem(inv, Material.DIAMOND_SWORD, "KitPVP", 13);
    createItem(inv, Material.BUCKET, "SkyBlock", 15);
    checkSlots(inv);
  }
  
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] arg)
  {
    if (cmd.getName().equalsIgnoreCase("agtest"))
    {
      if ((sender instanceof Player))
      {
        sender.sendMessage(ChatColor.DARK_GREEN + "-==========[" + ChatColor.GREEN + " AGCore" + ChatColor.DARK_GREEN + " ]==========-");
        sender.sendMessage(ChatColor.DARK_GREEN + "Active?: " + ChatColor.GREEN + ChatColor.BOLD + getServer().getPluginManager().isPluginEnabled("AGCore"));
        sender.sendMessage(ChatColor.DARK_GREEN + "Version: " + ChatColor.GREEN + ChatColor.BOLD + getServer().getPluginManager().getPlugin("AGCore")
          .getDescription().getVersion());
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
  private void onInventoryClick(InventoryClickEvent e){
      if(!ChatColor.stripColor(e.getInventory().getName()).equals("Available Servers")) 
      	return;

      if(!(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR)){

          Player p = (Player) e.getWhoClicked();
          Location loc = p.getLocation();
          loc.add(0, 1, 0);

          switch (e.getCurrentItem().getType()){
              case BUCKET:
                  e.setCancelled(true);
                  p.closeInventory();
                  p.sendMessage(color("&2&lThis is a test.."));
                  break;
              case WORKBENCH:
                  e.setCancelled(true);
                  p.closeInventory();
                  p.sendMessage(color("&2&lThis is a test.."));
                  break;
              case DIAMOND_SWORD:
                  e.setCancelled(true);
                  p.closeInventory();
                  p.sendMessage(color("&2&lThis is a test.."));
                  break;
              default:
                  e.setCancelled(true);
                  break;
          }
      }
  }

  private String color(String str){
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
  private void checkSlots(Inventory inv){
      ItemStack space;
      space = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.GRAY.getDyeData());
      ItemMeta meta = space.getItemMeta();
      meta.setDisplayName(ChatColor.BLACK + "");
      space.setItemMeta(meta);
      for(int i = 0;i < inv.getSize();i++){
          if(inv.getItem(i) == (null) || inv.getItem(i).getType() == Material.AIR){
              inv.setItem(i, space);
          }
      }
  }
}
