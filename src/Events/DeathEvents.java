package Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import org.bukkit.event.entity.PlayerDeathEvent;

import Core.Main;

public class DeathEvents implements Listener 
{
	
	@EventHandler
	public void entityDeath(EntityDamageByEntityEvent ev)
	{
		Player victim = (Player) ev.getEntity();
		Player killer = (Player) ev.getDamager();
		if(victim instanceof Entity)
		{
			Bukkit.broadcastMessage(victim + " was killed by " + killer);
		}
		if(ev.getCause() == DamageCause.PROJECTILE)
		{
			Bukkit.broadcastMessage(victim + " was shot by " + killer);
		}
	}
	
	@EventHandler
	public void onDeath(PlayerDeathEvent e) 
	{
		Player player = e.getEntity();
		if(player instanceof Player )
		{
			
			//Player Drowned
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.DROWNING)
			{
				e.setDeathMessage(Main.messages.getString("Death.Messages.Drowning").replace('&', '§'));
			}
			
			//Player Exploded
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION)
			{
				e.setDeathMessage(Main.messages.getString("Death.Messages.Block_Explosion").replace('&', '§'));
			}
			
			//Player Fell
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALL)
			{
				String e1 = Main.messages.getString("Death.Messages.Fall").replace('&', '§');
				String f = e1.replace("%player%", player.getDisplayName());
				e.setDeathMessage(f);
				return;
			}
			
			//Player Crushed
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FALLING_BLOCK)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Crushed").replace('&', '§'));
			}
			
			//Player Burned
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.FIRE)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Burned").replace('&', '§'));
			}
			
			//Player Burned
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LAVA)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Lava").replace('&', '§'));
			}
			
			//Player struck by Lightning
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.LIGHTNING)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Lightning").replace('&', '§'));
			}
			
			//Player died by Magic
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.MAGIC)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Magic").replace('&', '§'));
			}
			
			//Player poisoned
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.POISON)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Poison").replace('&', '§'));
			}
			
			//Player struck by projectile
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.PROJECTILE)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Projectile").replace('&', '§'));
			}
			
			//Player Starved
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.STARVATION)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Starvation").replace('&', '§'));
			}
			
			//Player Suicide
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.SUICIDE)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Suicide").replace('&', '§'));
			}
			
			//Player died by Thorns
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.THORNS)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Thorns").replace('&', '§'));
			}
			
			//Player died by Wither
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.WITHER)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Wither").replace('&', '§'));
			}
			
			//Player Died by Entity
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Attacked").replace('&', '§'));
			}
			
			//Player Exploded by Entity
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION)
			{
				Bukkit.broadcastMessage(
						Main.messages.getString(" Death.Messages.Entity-Exploded").replace('&', '§'));
			}
			
			//Player Melted
			if(e.getEntity().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.MELTING)
			{
				Bukkit.broadcastMessage( 
						Main.messages.getString(" Death.Messages.Melted").replace('&', '§'));
			}
		}
		else
		{
			return;
		}
	}

}