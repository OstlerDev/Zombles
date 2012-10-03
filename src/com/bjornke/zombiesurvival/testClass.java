package com.bjornke.zombiesurvival;

import java.util.List;
import java.util.Map;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class testClass extends main
  implements Listener
{
  private int pay;

@EventHandler
  public void onZombieDamage(EntityDamageEvent event)
  {
    Entity ent = event.getEntity();
    if ((!this.state.isEmpty()) && (!this.zombies.isEmpty()) && (this.zombies.get(Integer.valueOf(ent.getEntityId())) != null) && 
      ((ent instanceof Zombie)) && 
      (((Integer)this.state.get(this.zombies.get(Integer.valueOf(ent.getEntityId())))).intValue() > 1))
      if ((ent.getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
        EntityDamageByEntityEvent entitykiller = (EntityDamageByEntityEvent)ent.getLastDamageCause();
        if (!(entitykiller.getDamager() instanceof Player))
          event.setCancelled(true);
      }
      else if (((Integer)this.gameperks.get(this.zombies.get(Integer.valueOf(ent.getEntityId())))).intValue() == 0) {
        event.setCancelled(true);
      }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void onZombieDeath(EntityDeathEvent event)
  {
    Entity ent = event.getEntity();
    if ((!this.state.isEmpty()) && (!this.zombies.isEmpty()) && (this.zombies.get(Integer.valueOf(ent.getEntityId())) != null) && 
      (((Integer)this.state.get(this.zombies.get(Integer.valueOf(ent.getEntityId())))).intValue() > 1) && 
      ((ent instanceof Zombie))) {
      if ((ent.getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
        EntityDamageByEntityEvent entitykiller = (EntityDamageByEntityEvent)ent.getLastDamageCause();
        if ((entitykiller.getDamager() instanceof Player)) {
          Player player = (Player)entitykiller.getDamager();
          if (playerGame(player).equalsIgnoreCase((String)this.zombies.get(Integer.valueOf(ent.getEntityId())))) {
            event.getDrops().clear();
            if (chance()) {
              event.getDrops().add(new ItemStack(randomItem(), 1));
            }
            String map = playerGame(player);
            int kills = ((Integer)this.playerskills.get(player.getName())).intValue();
            kills++;
            this.playerskills.put(player.getName(), Integer.valueOf(kills));
            this.secondkills.put(map, Integer.valueOf(((Integer)this.secondkills.get(map)).intValue() + 1));
            if (this.points) {
              int score = ((Integer)this.playerscore.get(player.getName())).intValue();
              int newscore = score + 10;
              String name = player.getName();
              this.playerscore.put(player.getName(), Integer.valueOf(newscore));
              player.setDisplayName("[" + Integer.toString(newscore) + "]" + name);
              player.sendMessage(ChatColor.GREEN + "10 Points for Zombie Kill!");
            } else {
              econ.depositPlayer(player.getName(), this.pay);
              player.sendMessage(ChatColor.GREEN + "You have been deposited " + Integer.toString(this.pay) + " for a kill!");
            }
            this.zslayed.put(map, Integer.valueOf(((Integer)this.zslayed.get(map)).intValue() + 1));
            if (((Integer)this.zslayed.get(map)).intValue() >= ((Integer)this.wave.get(map)).intValue() * 10) {
              NewWave(map);
            }
          }
        }
      }
      if (this.zombies.containsKey(Integer.valueOf(ent.getEntityId()))) {
        this.zcount.put(this.zombies.get(Integer.valueOf(ent.getEntityId())), Integer.valueOf(((Integer)this.zcount.get(this.zombies.get(Integer.valueOf(ent.getEntityId())))).intValue() - 1));
        this.zombies.remove(Integer.valueOf(ent.getEntityId()));
      }
    }
  }
}