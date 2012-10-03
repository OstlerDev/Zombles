package com.bjornke.zombiesurvival;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
//import java.util.Locale.Builder; //TODO: Look into this!
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UnknownFormatConversionException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.minecraft.server.EntityHuman;
import net.minecraft.server.EntityLiving;
import net.minecraft.server.EntityWolf;
import net.minecraft.server.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.PathfinderGoalSelector;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.entity.CraftWolf;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Wool;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class main extends JavaPlugin
  implements Runnable, Listener
{
  public main instance;
  public Random random = new Random();
  //TODO: see if these are being used
  public Map<String, String> playermap = new HashMap();
  public Map<String, Integer> playerscore = new HashMap();
  public Map<String, Integer> playerskills = new HashMap();
  public Map<String, Integer> secondkills = new HashMap();
  public int onlinep = 0;
  public Map<String, Integer> pcount = new HashMap();
  public Map<String, Integer> state = new HashMap();
  public Map<String, Integer> zcount = new HashMap();
  public Map<String, Integer> wave = new HashMap();
  public Map<String, Integer> zslayed = new HashMap();
  public Map<String, Integer> maxzombies = new HashMap();
  public Map<String, Integer> maxwaves = new HashMap();
  public Map<String, Integer> maxplayers = new HashMap();
  public Map<String, Integer> gameperks = new HashMap();
  public Map<String, Integer> justleftgame = new HashMap();
  public Map<String, Location> playerloconjoin = new HashMap();
  public Map<String, Boolean> skellywave = new HashMap();
  public Map<String, Boolean> wolfwave = new HashMap();

  public Map<String, Integer> easycreate = new HashMap();
  public Map<String, String> ecname = new HashMap();
  public Map<String, Integer> ecpcount = new HashMap();
  public Map<String, Integer> ecwcount = new HashMap();
  public Map<String, Integer> eczcount = new HashMap();

  public List<String> twomintimer = new ArrayList();
  public int startpcount;
  public int commandwave = 0;
  public String commandMap = "default";
  public Map<String, Integer> perkcount = new HashMap();
  public String VERSION = "R-2.3";
  public boolean outofdate = false;
  public boolean antigreif = false;
  public boolean itemsatjoin = false;
  public boolean automated = false;
  public boolean infectmat = false;
  public boolean spectateallow = false;
  public boolean perpnight = false;
  public boolean emptyaccount = true;
  public boolean forcespawn = false;
  public boolean respawn = true;
  public boolean allhurt = true;
  public boolean forceclear = false;
  public boolean donatorperks = false;
  public boolean invsave = true;
  public boolean spawncontrol = false;
  public double seekspeed = 0.23D;
  public double fastseekspeed = 0.4D;
  public boolean wolfs = true;
  public boolean healnewwave = true;
  public String joinmessage = "Welcome to the server!";
  public List<String> joincommand = new ArrayList();
  public List<String> leavecommand = new ArrayList();
  public int cooldown = 120;
  public int vspoke = 0;
  public int dropchance = 0;
  public int runnerchance = 10;
  public int effectchance = 20;
  public int skellywavechance = 0;
  public int wait = 20;
  public int bitelength = 10;
  public int doorfindradius = 6;
  public int leavetimer = 120;
  public double deathloss = 0.0D;
  public double diffmulti = 0.1D;
  public double damagemulti = 1.0D;
  public Map<String, Integer> cooldowncount = new HashMap();

  public Map<String, Location> loc = new HashMap(10);
  public Map<String, Location> loc2 = new HashMap(10);
  public Map<String, Location> lightloc = new HashMap(10);
  public List<Integer> joinitems = new ArrayList(5);
  public List<Integer> joinarmor = new ArrayList(5);
  public List<Integer> drops = new ArrayList(5);
  public List<Integer> boxitems = new ArrayList(10);
  public Map<String, Map<Location, Integer>> spawnPoints = new HashMap();
  public Map<Location, String> roundFire = new HashMap();
  public Map<String, Map<Block, BlockState>> changedBlocks = new HashMap();
  public Map<String, Map<Block, BlockState>> placedBlocks = new HashMap();
  public Map<String, Map<Location, Integer>> doors = new HashMap();
  public Map<String, Map<Block, BlockState>> doriginals = new HashMap();
  public List<Integer> blockbreak = new ArrayList();
  public List<Integer> blockplace = new ArrayList();
  public Map<Location, String> specialblocks = new HashMap();
  public Map<String, ItemStack[]> deaddrops = new HashMap();
  public Map<String, ItemStack[]> inv = new HashMap();
  public Map<Integer, String> zombies = new HashMap(200);
  public Map<Integer, String> fastzombies = new HashMap(5);
  public Map<Integer, Map<String, Integer>> zombiescore = new HashMap();
  public Map<String, String> dead = new ConcurrentHashMap();
  public Map<String, Location> deathPoints = new HashMap();
  public Map<String, Boolean> perkend = new HashMap();
  public Map<String, String> Maps = new HashMap(10);
  public Set<Location> Signs = new HashSet();
  public Map<String, Integer> votemap = new HashMap(2);
  public Set<String> voted = new HashSet();
  public String vmap = null;
  public Map<String, Integer> wavemax = new HashMap();

  public Map<String, Integer> add = new HashMap(2);
  public Map<String, Integer> remove = new HashMap(2);
  int task;
  int task2;
  public static Economy econ = null;
  public static Chat chat = null;
  public boolean points = true;
  private FileConfiguration customConfig = null;
  private File customConfigFile = null;
  private static Field targetSelector;

  public static List<String> downedPlayers = new ArrayList();
  
  public void onEnable() {
    this.instance = this;
    try {
      saveDefaultConfig();
      registerEvents();
      LoadConfig();
      this.task = scheduleTask(this, 20L, 20L);
      if (setupEconomy()) {
        this.points = false;
        getLogger().info("Using Vault for Economy Access!");
      }
      if (this.automated) {
        getLogger().info("Running in automation mode!");
        autoStart();
      }
      Version();
      reloadPlayers();
      QuickUpdate();
      checkMobs();
      AsynchTasks();
      perpNight();
      dealDamage();
      if (!this.points) {
        this.points = getConfig().getBoolean("force-points");
      }
      getLogger().info("ZombieSurvival Enabled!");
      try {
        metrics metrics = new metrics(this);
        metrics.start();
      } catch (IOException e) {
        getLogger().info("Metrics failed to start");
      }
      try {
        targetSelector = EntityLiving.class.getDeclaredField("targetSelector");
        if (targetSelector == null) {
          getLogger().warning("Unable to use Java Reflection to gain access to wolf behavior. No wolves allowed.");
          this.wolfs = false;
        } else {
          targetSelector.setAccessible(true);
        }
      } catch (Exception e) {
        getLogger().warning("Unable to use Java Reflection to gain access to wolf behavior. No wolves allowed.");
        this.wolfs = false;
      }
    } catch (Exception e) {
      getLogger().severe("ZombieSurvival failed to start correctly! Disabling!");
      Bukkit.getPluginManager().disablePlugin(this);
    }
  }

  public void onDisable() {
    for (String map : this.Maps.keySet()) {
      GamesOver(map, Boolean.valueOf(true));
    }
    getLogger().info("ZombieSurvival Disabled!");
  }
  private void registerEvents() {
    PluginManager pm = Bukkit.getPluginManager();
    pm.registerEvents(this, this);
  }

  public void LoadConfig() {
    List<String> worldnames = new ArrayList();
    for (World world2 : Bukkit.getWorlds()) {
      worldnames.add(world2.getName());
    }
    for (String w : worldnames) {
      for (String s : getCustomConfig().getStringList(w + ".maps")) {
        this.Maps.put(s, w);
      }
      for (String str2 : getCustomConfig().getStringList(w + ".signs")) {
        this.Signs.add(parseToLoc(str2));
      }
    }
    saveSigns();
    for (String str : this.Maps.keySet()) {
      this.doors.put(str, new HashMap());
      this.spawnPoints.put(str, new HashMap());
      for (String str2 : getCustomConfig().getStringList((String)this.Maps.get(str) + "." + str + ".zombiespawns")) {
        parseToSpawn(str, str2);
      }
      for (String str2 : getCustomConfig().getStringList((String)this.Maps.get(str) + "." + str + ".roundfire")) {
        this.roundFire.put(parseToLoc(str2), str);
      }
      for (String str2 : getCustomConfig().getStringList((String)this.Maps.get(str) + "." + str + ".specialblocks")) {
        this.specialblocks.put(parseToLoc(str2), str);
      }
      for (String str2 : getCustomConfig().getStringList((String)this.Maps.get(str) + "." + str + ".doors")) {
        parseToDoor(str, str2);
      }
      this.deathPoints.put(str, parseToLoc(getCustomConfig().getString((String)this.Maps.get(str) + "." + str + ".waiting")));
      this.loc.put(str, parseToLoc(getCustomConfig().getString((String)this.Maps.get(str) + "." + str + ".lobby")));
      this.loc2.put(str, parseToLoc(getCustomConfig().getString((String)this.Maps.get(str) + "." + str + ".spectate")));
      this.lightloc.put(str, parseToLoc(getCustomConfig().getString((String)this.Maps.get(str) + "." + str + ".lightning")));
      this.maxzombies.put(str, Integer.valueOf(getCustomConfig().getInt((String)this.Maps.get(str) + "." + str + ".maxzombies")));
      this.maxplayers.put(str, Integer.valueOf(getCustomConfig().getInt((String)this.Maps.get(str) + "." + str + ".maxplayers")));
      this.maxwaves.put(str, Integer.valueOf(getCustomConfig().getInt((String)this.Maps.get(str) + "." + str + ".maxwaves")));
      this.state.put(str, Integer.valueOf(1));
      this.pcount.put(str, Integer.valueOf(0));
      this.gameperks.put(str, Integer.valueOf(0));
      this.perkcount.put(str, Integer.valueOf(0));
      this.changedBlocks.put(str, new HashMap());
      this.placedBlocks.put(str, new HashMap());
      this.perkend.put(str, Boolean.valueOf(true));
      this.wave.put(str, Integer.valueOf(0));
      this.zcount.put(str, Integer.valueOf(0));
    }
    this.blockbreak = getConfig().getIntegerList("allowbreak");
    this.blockplace = getConfig().getIntegerList("allowplace");
    this.startpcount = getConfig().getInt("start-player-count");
    this.antigreif = getConfig().getBoolean("auto-anti-grief");
    this.joinitems = getConfig().getIntegerList("join-items");
    this.joinarmor = getConfig().getIntegerList("armor-items");
    this.itemsatjoin = getConfig().getBoolean("items-at-join");
    this.automated = getConfig().getBoolean("automated");
    this.joinmessage = getConfig().getString("auto-join-message");
    this.cooldown = getConfig().getInt("auto-cooldown");
    this.deathloss = getConfig().getDouble("death-loss-percent");
    this.dropchance = getConfig().getInt("drop-chance");
    this.drops = getConfig().getIntegerList("drop-items");
    this.diffmulti = getConfig().getDouble("health-multi");
    this.damagemulti = getConfig().getDouble("damage-multi");
    this.infectmat = getConfig().getBoolean("infect-mat");
    this.spectateallow = getConfig().getBoolean("allow-spectate");
    this.wait = (getConfig().getInt("wave-wait") * 20);
    this.perpnight = getConfig().getBoolean("perp-night");
    this.runnerchance = getConfig().getInt("runner-chance");
    this.effectchance = getConfig().getInt("effect-chance");
    this.emptyaccount = getConfig().getBoolean("empty-account");
    this.forcespawn = getConfig().getBoolean("force-spawn");
    this.seekspeed = getConfig().getDouble("seek-speed");
    this.fastseekspeed = getConfig().getDouble("fast-seek-speed");
    this.bitelength = (getConfig().getInt("bite-effect-length") * 20);
    this.doorfindradius = getConfig().getInt("buy-door-find-radius");
    this.boxitems = getConfig().getIntegerList("mysterybox-items");
    this.respawn = getConfig().getBoolean("death-non-human-respawn");
    this.allhurt = getConfig().getBoolean("all-hurt");
    this.leavetimer = getConfig().getInt("leave-timer");
    this.forceclear = getConfig().getBoolean("inventory-clear-join");
    this.donatorperks = getConfig().getBoolean("use-donator-perks");
    this.invsave = getConfig().getBoolean("inventory-save");
    this.healnewwave = getConfig().getBoolean("heal-player-new-wave");
    this.skellywavechance = getConfig().getInt("skelly-wave-chance");
    this.wolfs = getConfig().getBoolean("wolves-every-10th-wave");
    this.joincommand = getConfig().getStringList("join-commands");
    this.leavecommand = getConfig().getStringList("leave-commands");
    if ((this.Maps.size() < 3) && (this.automated)) {
      this.automated = false;
      getLogger().info("You need atleast 3 maps for automated mode! Back to normal mode!");
    }
    PopulateDoriginals();
    saveCustomConfig();
  }
  @EventHandler
  public void CommandListener(PlayerCommandPreprocessEvent e) { String message = e.getMessage().substring(1);
    String[] command = message.split(" ");
    if (this.joincommand.contains(command[0])) {
      if (command.length > 1)
        e.getPlayer().performCommand("bsapj " + command[1]);
      else {
        e.getPlayer().performCommand("bsapj ");
      }
      e.setCancelled(true);
    } else if (this.leavecommand.contains(command[0])) {
      e.getPlayer().performCommand("bsapl");
      e.setCancelled(true);
    } }

  public void reload() {
    for (String str : this.Maps.keySet()) {
      GamesOver(str, Boolean.valueOf(true));
    }
    this.loc.clear();
    this.loc2.clear();
    this.lightloc.clear();
    this.maxzombies.clear();
    this.maxwaves.clear();
    this.blockbreak.clear();
    this.blockplace.clear();
    this.spawnPoints.clear();
    this.roundFire.clear();
    this.doors.clear();
    this.add.clear();
    this.remove.clear();
    this.pcount.clear();
    this.playermap.clear();
    this.playerscore.clear();
    this.playerskills.clear();
    this.changedBlocks.clear();
    this.placedBlocks.clear();
    this.zombies.clear();
    this.zslayed.clear();
    this.wave.clear();
    this.state.clear();
    this.gameperks.clear();
    this.perkcount.clear();
    this.commandwave = 0;
    this.commandMap = "";
    this.automated = false;
    this.dead.clear();
    this.onlinep = 0;
    reloadConfig();
    reloadCustomConfig();
    LoadConfig();
    reloadPlayers();
  }
  public void run() {
    for (String map : this.Maps.keySet()) {
      World world = Bukkit.getWorld((String)this.Maps.get(map));
      int s = ((Integer)this.state.get(map)).intValue();
      if ((s == 2) && (((Integer)this.zcount.get(map)).intValue() < ((Integer)this.wavemax.get(map)).intValue()) && (!this.spawncontrol))
      {
        Entity ent;
        if (((Boolean)this.skellywave.get(map)).booleanValue()) {
          ent = world.spawnEntity(spawnloc(map), EntityType.SKELETON);
        } else if (((Boolean)this.wolfwave.get(map)).booleanValue()) {
          ent = world.spawnEntity(spawnloc(map), EntityType.WOLF);
          Wolf w = (Wolf)ent;
          w.setAngry(true);
        } else {
          ent = world.spawnEntity(spawnloc(map), EntityType.ZOMBIE);
        }
        LivingEntity lent = (LivingEntity)ent;
        if (this.diffmulti != 0.0D) {
          int health = (int)(((Integer)this.wave.get(map)).intValue() * this.diffmulti);
          if ((health > 17) && (!((Boolean)this.wolfwave.get(map)).booleanValue()))
            health = 17;
          else {
            health = 5;
          }
          lent.setHealth(3 + health);
        }
        if (this.runnerchance != 0) {
          int go = this.random.nextInt(this.runnerchance);
          if (go == 1) {
            this.fastzombies.put(Integer.valueOf(ent.getEntityId()), map);
          }
        }
        this.zombies.put(Integer.valueOf(ent.getEntityId()), map);
        this.zcount.put(map, Integer.valueOf(((Integer)this.zcount.get(map)).intValue() + 1));
      }
      if ((s == 2) && (((Integer)this.secondkills.get(map)).intValue() > ((Integer)this.pcount.get(map)).intValue()) && (((Integer)this.gameperks.get(map)).intValue() == 0)) {
        NewPerk(map);
        this.perkend.put(map, Boolean.valueOf(false));
      }
      this.secondkills.put(map, Integer.valueOf(0));
      if ((((Integer)this.gameperks.get(map)).intValue() > 0) && (((Integer)this.perkcount.get(map)).intValue() < 60)) {
        this.perkcount.put(map, Integer.valueOf(((Integer)this.perkcount.get(map)).intValue() + 1));
      } else {
        if (!((Boolean)this.perkend.get(map)).booleanValue()) {
          for (String str : playersInMap(map)) {
            Player p = Bukkit.getPlayer(str);
            if (p != null) {
              p.sendMessage(ChatColor.GRAY + "PERK HAS ENDED");
            }
          }
          this.perkend.put(map, Boolean.valueOf(true));
        }
        this.gameperks.put(map, Integer.valueOf(0));
        this.perkcount.put(map, Integer.valueOf(0));
      }
      if ((this.state.get(this.vmap) != null) && (this.cooldowncount.get(this.vmap) != null))
        if ((((Integer)this.state.get(this.vmap)).intValue() == 1) && (this.automated) && (((Integer)this.cooldowncount.get(this.vmap)).intValue() < this.cooldown)) {
          this.cooldowncount.put(this.vmap, Integer.valueOf(((Integer)this.cooldowncount.get(this.vmap)).intValue() + 1));
        } else if ((((Integer)this.state.get(this.vmap)).intValue() == 1) && (this.automated) && (((Integer)this.cooldowncount.get(this.vmap)).intValue() == this.cooldown)) {
          Player[] list = Bukkit.getOnlinePlayers();
          for (Player p : list) {
            this.playermap.put(p.getName(), this.vmap);
            this.playerscore.put(p.getName(), Integer.valueOf(0));
          }
          Games(this.vmap, Boolean.valueOf(false));
          this.cooldowncount.put(this.vmap, Integer.valueOf(0));
          this.voted.clear();
          this.votemap.clear();
        }
    }
  }

  public void Games(String map, Boolean force)
  {
    World world = Bukkit.getWorld((String)this.Maps.get(map));
    String health;
    String zombmax;
    if ((((Integer)this.pcount.get(map)).intValue() >= this.startpcount) || (force.booleanValue())) {
      this.state.put(map, Integer.valueOf(2));
      this.wave.put(map, Integer.valueOf(1));
      this.zslayed.put(map, Integer.valueOf(0));
      this.zcount.put(map, Integer.valueOf(0));
      this.secondkills.put(map, Integer.valueOf(0));
      this.skellywave.put(map, Boolean.valueOf(false));
      this.wolfwave.put(map, Boolean.valueOf(false));
      maxfinder(map);
      health = Integer.toString(4 + (int)(((Integer)this.wave.get(map)).intValue() * this.diffmulti));
      zombmax = Integer.toString(((Integer)this.wavemax.get(map)).intValue());
      if (this.lightloc.get(map) != null) {
        world.strikeLightningEffect((Location)this.lightloc.get(map));
      }
      getLogger().info("Starting game: " + map);
      for (String mp : playersInMap(map)) {
        Player p = Bukkit.getPlayer(mp);
        if (p != null) {
          p.teleport((Location)this.loc.get(map));
          p.setAllowFlight(false);
          p.setFlying(false);
          p.setGameMode(GameMode.SURVIVAL);
          p.setHealth(20);
          p.setFoodLevel(20);
          this.dead.remove(p.getName());
          joinItems(p);
          p.sendMessage(ChatColor.AQUA + "Welcome to Minecraft Zombie Survival!");
          p.sendMessage(ChatColor.AQUA + "Beginning WAVE 1");
          p.sendMessage(ChatColor.DARK_RED + zombmax + ChatColor.GRAY + " zombies with " + ChatColor.DARK_RED + health + ChatColor.GRAY + " health!");
        }
      }
    } else {
      if (this.automated) {
        this.vspoke = 0;
        this.cooldowncount.clear();
        this.voted.clear();
        this.votemap.clear();
      }
      for (String mp : playersInMap(map)) {
        Player p = Bukkit.getPlayer(mp);
        if (p != null)
          p.sendMessage(ChatColor.RED + "Not enough players to start game!");
      }
    }
  }

  public void placeInGame(Player p, String map, boolean wave)
  {
    unhidePlayer(p);
    this.playerscore.put(p.getName(), Integer.valueOf(0));
    p.teleport((Location)this.loc.get(map));
    p.setAllowFlight(false);
    p.setFlying(false);
    p.setGameMode(GameMode.SURVIVAL);
    p.setHealth(20);
    p.setFoodLevel(20);
    joinItems(p);
    if (!wave)
      p.sendMessage(ChatColor.AQUA + "Welcome to Minecraft Zombie Survival!");
  }

  public void GamesOver(String map, Boolean force)
  {
    World world = Bukkit.getWorld((String)this.Maps.get(map));
    if ((onlinepcount(map) < 1) || (force.booleanValue())) {
      resetBlocks(map);
      clearDrops(map);
      resetDoors(map);
      this.state.put(map, Integer.valueOf(1));
      this.wave.put(map, Integer.valueOf(0));
      this.pcount.put(map, Integer.valueOf(0));
      this.vspoke = 0;
      this.wavemax.put(map, null);
      getLogger().info("Ending game: " + map);
      List templist = getEnts(world);
      Entity went;
      for (Iterator it = templist.iterator(); it.hasNext(); ) { went = (Entity)it.next();
        for (it = this.zombies.keySet().iterator(); it.hasNext(); ) {
          int id = ((Integer)it.next()).intValue();
          if ((((String)this.zombies.get(Integer.valueOf(id))).equalsIgnoreCase(map)) && (went.getEntityId() == id)) {
            went.remove();
            it.remove();
          }
        }
      }
      Iterator it;
      for (String mp : playersInMap(map)) {
        this.playermap.remove(mp);
        this.playerscore.put(mp, Integer.valueOf(0));
        this.playerskills.remove(mp);
        this.dead.remove(mp);
        Player p = Bukkit.getPlayer(mp);
        if (p != null) {
          p.teleport((Location)this.playerloconjoin.get(mp));
          this.playerloconjoin.remove(mp);
          p.setAllowFlight(false);
          p.setFlying(false);
          p.setGameMode(GameMode.SURVIVAL);
          p.setHealth(20);
          p.setFoodLevel(20);
          if ((this.invsave) && (this.inv.containsKey(mp))) {
            p.getInventory().setContents((ItemStack[])this.inv.get(mp));
            p.updateInventory();
            this.inv.remove(mp);
          }
          unhidePlayer(p);
          p.setDisplayName(p.getName());
          p.sendMessage(ChatColor.AQUA + "Game has ended! Back to spawn!");
          if ((this.emptyaccount) && (!this.points)) {
            double removem = econ.getBalance(mp);
            econ.withdrawPlayer(p.getName(), removem);
          }
        }
      }
      if (this.automated)
        autoStart();
    }
  }

  public Location spawnloc(String game)
  {
    List PossibleSpawns = new ArrayList();
    if ((!this.spawnPoints.isEmpty()) && (this.spawnPoints.get(game) != null))
      for (Location possible : (this.spawnPoints.get(game)).keySet()) //TODO: broken?
        if (((Integer)((Map)this.spawnPoints.get(game)).get(possible)).intValue() <= ((Integer)this.wave.get(game)).intValue())
          PossibleSpawns.add(possible);
    Location spawnloc;
    //Location spawnloc;
    if ((!PossibleSpawns.isEmpty()) && (PossibleSpawns != null))
      spawnloc = (Location)PossibleSpawns.get(this.random.nextInt(PossibleSpawns.size()));
    else {
      spawnloc = (Location)this.loc.get(game);
    }
    return spawnloc;
  }

  public Location parseToLoc(String str) throws NumberFormatException {
    if (str == null) {
      return null;
    }
    String[] strs = str.split(" ");
    double xl = Double.parseDouble(strs[0]);
    double yl = Double.parseDouble(strs[1]);
    double zl = Double.parseDouble(strs[2]);
    World worldl = Bukkit.getServer().getWorld(strs[3]);
    Location parsedLoc = new Location(worldl, xl, yl, zl);
    return parsedLoc;
  }
  public void parseToDoor(String str, String str2) throws NumberFormatException {
    if (str == null) {
      return;
    }
    String[] strs = str2.split(" ");
    double xc = Double.parseDouble(strs[0]);
    double yc = Double.parseDouble(strs[1]);
    double zc = Double.parseDouble(strs[2]);
    World worldc = Bukkit.getServer().getWorld(strs[4]);
    int wavenumber = Integer.parseInt(strs[3]);
    Location save = new Location(worldc, xc, yc, zc);
    ((Map)this.doors.get(str)).put(save, Integer.valueOf(wavenumber));
  }
  public void parseToSpawn(String str, String str2) throws NumberFormatException {
    if (str == null) {
      return;
    }
    String[] strs = str2.split(" ");
    double xc = Double.parseDouble(strs[0]);
    double yc = Double.parseDouble(strs[1]);
    double zc = Double.parseDouble(strs[2]);
    World worldc = Bukkit.getServer().getWorld(strs[4]);
    int wavenumber = Integer.parseInt(strs[3]);
    Location save = new Location(worldc, xc, yc, zc);
    ((Map)this.spawnPoints.get(str)).put(save, Integer.valueOf(wavenumber));
  }
  public String parseToStr(Location parseloc) {
    return String.format("%.2f %.2f %.2f %s", new Object[] { Double.valueOf(parseloc.getX()), Double.valueOf(parseloc.getY()), Double.valueOf(parseloc.getZ()), parseloc.getWorld().getName() });
  }
  public String parseToDoorStr(Location parseloc, Integer wavenumber) {
    return String.format("%.2f %.2f %.2f %d %s", new Object[] { Double.valueOf(parseloc.getX()), Double.valueOf(parseloc.getY()), Double.valueOf(parseloc.getZ()), wavenumber, parseloc.getWorld().getName() });
  }
  public int scheduleTask(Runnable runnable, long initial, long delay) {
    return Bukkit.getScheduler().scheduleSyncRepeatingTask(this.instance, runnable, initial, delay);
  }

  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    if (cmd.getName().equalsIgnoreCase("zsdebug")) {
      if ((sender instanceof Player)) {
        Player player = (Player)sender;
        zsDebug(1, player);
        return true;
      }
      zsDebug(0, null);
      return true;
    }
    Player player;
    if ((sender instanceof Player)) {
      player = (Player)sender;

      if (cmd.getName().equalsIgnoreCase("zsa-spawn")) {
        if (args.length != 2) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.add.put(player.getName(), Integer.valueOf(3));
        this.commandMap = args[0];
        this.commandwave = Integer.parseInt(args[1]);
        this.remove.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to add as a spawnpoint. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zsr-spawn")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.remove.put(player.getName(), Integer.valueOf(3));
        this.add.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to remove as spawnpoint. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zs-start")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        Bukkit.broadcastMessage(ChatColor.AQUA + "Admin has started the game!");
        Games(args[0], Boolean.valueOf(true));
      } else if (cmd.getName().equalsIgnoreCase("zs-end")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        GamesOver(args[0], Boolean.valueOf(true));
        player.sendMessage(ChatColor.GOLD + "You have ended the games early!");
      } else if (cmd.getName().equalsIgnoreCase("zs-create")) {
        if (args.length != 4)
          return false;
        try
        {
          this.commandMap = args[0];
          this.add.put(player.getName(), Integer.valueOf(0));
          this.remove.remove(player.getName());
          getCustomConfig().set(player.getWorld().getName() + "." + args[0] + ".maxzombies", Integer.valueOf(Integer.parseInt(args[1])));
          getCustomConfig().set(player.getWorld().getName() + "." + args[0] + ".maxplayers", Integer.valueOf(Integer.parseInt(args[2])));
          getCustomConfig().set(player.getWorld().getName() + "." + args[0] + ".maxwaves", Integer.valueOf(Integer.parseInt(args[3])));
          this.maxzombies.put(args[0], Integer.valueOf(Integer.parseInt(args[1])));
          this.maxplayers.put(args[0], Integer.valueOf(Integer.parseInt(args[2])));
          this.maxwaves.put(args[0], Integer.valueOf(Integer.parseInt(args[3])));
          this.pcount.put(args[0], Integer.valueOf(0));
          this.state.put(args[0], Integer.valueOf(1));
          this.Maps.put(args[0], player.getWorld().getName());
          this.gameperks.put(args[0], Integer.valueOf(0));
          this.perkcount.put(args[0], Integer.valueOf(0));
          this.perkend.put(args[0], Boolean.valueOf(false));
          this.wave.put(args[0], Integer.valueOf(0));
          this.changedBlocks.put(args[0], new HashMap());
          this.placedBlocks.put(args[0], new HashMap());
          List gamemaps = new ArrayList();
          for (String s : this.Maps.keySet()) {
            if (((String)this.Maps.get(s)).equalsIgnoreCase(player.getWorld().getName())) {
              gamemaps.add(s);
            }
          }
          getCustomConfig().set((String)this.Maps.get(args[0]) + ".maps", gamemaps);
          saveCustomConfig();
        } catch (UnknownFormatConversionException ex) {
          player.sendMessage(ChatColor.RED + "Try again, bad arguments!");
          return false;
        }
        player.sendMessage(ChatColor.GREEN + "Left click a block to set as player spawnpoint for the game. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zs-remove")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }

        getCustomConfig().set((String)this.Maps.get(args[0]) + ".maps", this.Maps);
        getCustomConfig().set((String)this.Maps.get(args[0]) + "." + args[0], null);
        this.Maps.remove(args[0]);
        saveCustomConfig();
        reload();
        player.sendMessage(ChatColor.GREEN + "You have removed the entire game: " + args[0]);
      } else if (cmd.getName().equalsIgnoreCase("zsa-fire")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.add.put(player.getName(), Integer.valueOf(4));
        this.remove.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Click to add blocks to be set on fire (Netherrack) every 10th round");
      } else if (cmd.getName().equalsIgnoreCase("zsr-fire")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.remove.put(player.getName(), Integer.valueOf(4));
        this.add.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Click to remove blocks to be set on fire (Netherrack) every 10th round");
      } else if (cmd.getName().equalsIgnoreCase("zsa-spectate")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.add.put(player.getName(), Integer.valueOf(1));
        this.remove.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to set as spectator spawnpoint. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zsr-spectate")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.remove.put(player.getName(), Integer.valueOf(1));
        this.add.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to remove as spectator spawnpoint. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zsa-waiting")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.add.put(player.getName(), Integer.valueOf(7));
        this.remove.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to set as waiting lobby for game. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zsr-waiting")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.remove.put(player.getName(), Integer.valueOf(7));
        this.add.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to remove as waiting lobby for game. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zsa-lightning")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.add.put(player.getName(), Integer.valueOf(2));
        this.remove.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to set as lightning strike location. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zsr-lightning")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.remove.put(player.getName(), Integer.valueOf(2));
        this.add.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to remove as lightning strike location. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zsa-door")) {
        if (args.length != 2) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.add.put(player.getName(), Integer.valueOf(5));
        this.commandwave = Integer.parseInt(args[1]);
        this.remove.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to set as a door. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zsr-door")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.remove.put(player.getName(), Integer.valueOf(5));
        this.add.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to as a door. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zs-reload")) {
        reload();
        player.sendMessage("Reloaded ZombieSurvival!");
      } else if (cmd.getName().equalsIgnoreCase("zsa-special")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.add.put(player.getName(), Integer.valueOf(6));
        this.remove.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to set as a special action block. Must be wool. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zsr-special")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        this.commandMap = args[0];
        this.remove.put(player.getName(), Integer.valueOf(6));
        this.add.remove(player.getName());
        player.sendMessage(ChatColor.GREEN + "Left click a block to as a special action block. Right click to escape.");
      } else if (cmd.getName().equalsIgnoreCase("zs-version")) {
        Version();
        if (this.outofdate) {
          player.sendMessage(ChatColor.GOLD + "Version: " + this.VERSION);
          player.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.RED + "OUT OF DATE! UPDATE AVAILABLE");
        } else {
          player.sendMessage(ChatColor.GREEN + "Version: " + this.VERSION);
        }
      } else if (cmd.getName().equalsIgnoreCase("zstp")) {
        if (args.length != 1) {
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        player.teleport((Location)this.loc.get(args[0]));
      } else if (cmd.getName().equalsIgnoreCase("zs-c")) {
        this.easycreate.put(player.getName(), Integer.valueOf(0));
        player.sendMessage(ChatColor.GREEN + "Please type the name of the game you wish to create!");
      }
      if (cmd.getName().equalsIgnoreCase("stats")) {
        if ((args.length < 1) && 
          (this.state.get(playerGame(player)) != null) && 
          (((Integer)this.state.get(playerGame(player))).intValue() > 1)) {
          info(player, player);
        }

        if (args.length > 0) {
          Player p = Bukkit.getPlayer(args[0]);
          if ((p != null) && (inGame(p)))
            info(p, player);
          else {
            player.sendMessage(ChatColor.RED + "Error - player is either not in a game or is not online!");
          }
        }
        player.sendMessage("Number of players online: " + Integer.toString(this.onlinep));
      } else if (cmd.getName().equalsIgnoreCase("whisper")) {
        Player other = Bukkit.getServer().getPlayer(args[0]);
        if (other == null) {
          return false;
        }
        String send = " MESSAGE:";
        for (int i = 1; i < args.length; i++) {
          send = send + " " + args[i];
        }
        other.sendMessage("From: " + player.getName() + send);
        player.sendMessage("Sent To: " + other.getName() + "Message:" + send);
      } else if (cmd.getName().equalsIgnoreCase("zshelp")) {
        player.sendMessage("Commands: stats, whisper, join, leave, zshelp");
        if (player.isOp())
          player.sendMessage("OP-Commands: zs-create, zs-remove, zsa-spawn, zsr-spawn, zsa-door, zsr-door, zsa-spectate, zsr-spectate, zsa-lightning, zsr-lightning, zsa-fire, zsr-fire, zsa-special, zsr-special, zsa-waiting, zsr-waiting, zs-reload, zs-version, zstp");
      }
      else if ((cmd.getName().equalsIgnoreCase("bsapj")) && (!this.automated)) {
        if (args.length < 1) {
          String message = "Games Available " + this.Maps.keySet().toString();
          player.sendMessage(ChatColor.BLUE + message);
          return false;
        }
        if (!this.Maps.containsKey(args[0])) {
          player.sendMessage("Could not find game specified");
          return false;
        }
        if (this.justleftgame.containsKey(player.getName())) {
          player.sendMessage("You just left a game! You must wait " + Integer.toString(this.leavetimer - ((Integer)this.justleftgame.get(player.getName())).intValue()) + " seconds to try again!");
          return true;
        }
        if ((numberInMap(args[0]) < ((Integer)this.maxplayers.get(args[0])).intValue()) && (!inGame(player)) && (!this.dead.containsKey(player.getName())) && (((Integer)this.playerscore.get(player.getName())).intValue() >= 0)) {
          Bukkit.broadcastMessage(ChatColor.GOLD + "[ZombieSurvival] " + ChatColor.GREEN + player.getName() + " just joined " + args[0] + "!");
          this.playermap.put(player.getName(), args[0]);
          this.playerscore.put(player.getName(), Integer.valueOf(0));
          this.playerskills.put(player.getName(), Integer.valueOf(0));
          this.pcount.put(args[0], Integer.valueOf(((Integer)this.pcount.get(args[0])).intValue() + 1));
          this.playerloconjoin.put(player.getName(), player.getLocation());
          if (((!player.hasPermission("zs.donator")) || (!this.donatorperks)) && (this.invsave)) {
            this.inv.put(player.getName(), player.getInventory().getContents());
          }
          if (((Integer)this.state.get(args[0])).intValue() < 2)
            Games(args[0], Boolean.valueOf(false));
          else if (((Integer)this.state.get(args[0])).intValue() > 1) {
            placeInGame(player, args[0], false);
          }
          player.sendMessage("You have joined the zombie survival game!");
        } else {
          player.sendMessage("Please try again later!");
        }
      } else if ((cmd.getName().equalsIgnoreCase("bsapl")) && (inGame(player)) && (!this.automated)) {
        String game = playerGame(player);
        this.justleftgame.put(player.getName(), Integer.valueOf(0));
        if (!this.dead.containsKey(player.getName())) {
          int tempcount = ((Integer)this.pcount.get(game)).intValue();
          this.pcount.put(game, Integer.valueOf(tempcount - 1));
        }
        if (((!player.hasPermission("zs.donator")) && (!player.hasPermission("zs.bypass"))) || (!this.donatorperks)) {
          player.getInventory().clear();
          player.getInventory().setArmorContents(null);
          player.updateInventory();
        }
        if ((player.hasPermission("zs.bypass")) && (this.donatorperks) && 
          (player.getInventory().contains(Material.DIAMOND_SWORD))) {
          ItemStack[] temp = player.getInventory().getContents();
          List temp2 = new ArrayList();
          for (ItemStack stack : temp) {
            if ((stack != null) && (stack.getType() == Material.DIAMOND_SWORD)) {
              temp2.add(stack);
              break;
            }
          }
          ItemStack[] set = (ItemStack[])(ItemStack[])temp2.toArray(new ItemStack[temp2.size()]);
          player.getInventory().clear();
          player.getInventory().setArmorContents(null);
          player.getInventory().setContents(set);
          player.updateInventory();
        }

        player.teleport((Location)this.playerloconjoin.get(player.getName()));
        this.playerloconjoin.remove(player.getName());
        player.sendMessage("You have left the zombie survival game!");
        this.playermap.remove(player.getName());
        this.playerscore.put(player.getName(), Integer.valueOf(0));
        this.playerskills.remove(player.getName());
        this.dead.remove(player.getName());
        if ((this.emptyaccount) && (!this.points)) {
          double removem = econ.getBalance(player.getName());
          econ.withdrawPlayer(player.getName(), removem);
        }
        if ((this.invsave) && (this.inv.containsKey(player.getName()))) {
          player.getInventory().setContents((ItemStack[])this.inv.get(player.getName()));
          player.updateInventory();
          this.inv.remove(player.getName());
        }
        GamesOver(game, Boolean.valueOf(false));
        String name = player.getName();
        player.setDisplayName(name);
      } else if ((cmd.getName().equalsIgnoreCase("vote")) && (this.automated)) {
        if (args.length != 1) {
          return false;
        }
        if (this.voted.contains(player.getName())) {
          player.sendMessage(ChatColor.GOLD + "You already voted!");
          return true;
        }
        for (String options : this.votemap.keySet())
          if (args[0].equalsIgnoreCase(options)) {
            int tally = ((Integer)this.votemap.get(options)).intValue();
            tally++;
            this.votemap.put(options, Integer.valueOf(tally));
            this.voted.add(player.getName());
            player.sendMessage(ChatColor.GREEN + "You voted for: " + options);
          } else if (!this.voted.contains(player.getName())) {
            player.sendMessage(ChatColor.RED + "Did not match your vote to available options!");
          }
      }
    }
    else {
      sender.sendMessage("Must be player!");
      return false;
    }

    return true;
  }
  public void info(Player p, Player s) {
    String str3 = rPlayers(playerGame(p));
    s.sendMessage(ChatColor.GREEN + "Kills: " + ChatColor.DARK_RED + Integer.toString(((Integer)this.playerskills.get(p.getName())).intValue()));
    s.sendMessage(ChatColor.GREEN + "Wave: " + ChatColor.DARK_RED + Integer.toString(((Integer)this.wave.get(playerGame(p))).intValue()));
    if (!this.points) {
      String score = String.format("%.1f", new Object[] { Double.valueOf(econ.getBalance(p.getName())) });
      s.sendMessage(ChatColor.GREEN + "Balance: " + ChatColor.DARK_RED + score);
    } else {
      s.sendMessage(ChatColor.GREEN + "Points: " + ChatColor.DARK_RED + Integer.toString(((Integer)this.playerscore.get(p.getName())).intValue()));
    }
    s.sendMessage(ChatColor.GREEN + "Remaining Players: " + ChatColor.DARK_RED + str3);
  }
  public String rPlayers(String map) {
    String string = "";
    for (String p : playersInMap(map)) {
      if (!this.dead.containsKey(p)) {
        string = string.concat(p) + " ";
      }
    }
    return string;
  }
  public void NewWave(final String map) {
    maxfinder(map);
    this.wave.put(map, Integer.valueOf(((Integer)this.wave.get(map)).intValue() + 1));
    if (this.skellywavechance != 0) {
      int go = this.random.nextInt(this.skellywavechance);
      if ((go == 1) && (((Integer)this.wave.get(map)).intValue() != 1))
        this.skellywave.put(map, Boolean.valueOf(true));
      else {
        this.skellywave.put(map, Boolean.valueOf(false));
      }
    }
    if (Integer.toString(((Integer)this.wave.get(map)).intValue()).endsWith("0")) {
      for (Location blg : this.roundFire.keySet()) {
        if (((String)this.roundFire.get(blg)).equalsIgnoreCase(map)) {
          blg.getBlock().getRelative(BlockFace.UP).setType(Material.FIRE);
        }
      }
      //TODO: Add giants and pig zombies here!
      if (this.wolfs) {
        this.wolfwave.put(map, Boolean.valueOf(true));
        this.skellywave.put(map, Boolean.valueOf(false));
      }
    } else {
      this.wolfwave.put(map, Boolean.valueOf(false));
    }
    if (((Integer)this.wave.get(map)).intValue() > ((Integer)this.maxwaves.get(map)).intValue()) {
      GamesOver(map, Boolean.valueOf(true));
      return;
    }
    String health = "20";
    if (this.diffmulti != 0.0D) {
      int h = (int)(((Integer)this.wave.get(map)).intValue() * this.diffmulti);
      if ((h > 17) && (!((Boolean)this.wolfwave.get(map)).booleanValue()))
        h = 17;
      else {
        h = 5;
      }
      health = Integer.toString(3 + h);
    }
    String zombmax = annouceMax(map);
    if (((Integer)this.wave.get(map)).intValue() != 1) {
      Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
      {
        public void run() {
          main.this.maxfinder(map);
          main.this.zcount.put(map, Integer.valueOf(0));
          main.this.zslayed.put(map, Integer.valueOf(0));
        }
      }
      , this.wait);
    }

    Boolean doorsopen = Boolean.valueOf(false);
    for (String player : playersInMap(map)) {
      Player p = Bukkit.getPlayer(player);
      if (p != null) {
        p.sendMessage(ChatColor.GREEN + "WAVE: " + ChatColor.DARK_RED + Integer.toString(((Integer)this.wave.get(map)).intValue()) + ChatColor.GRAY + " starts in " + ChatColor.DARK_RED + Integer.toString(this.wait / 20) + ChatColor.GRAY + " seconds!");
        if (((Boolean)this.skellywave.get(map)).booleanValue()) {
          p.sendMessage(ChatColor.GRAY + "The zombies have called in the " + ChatColor.DARK_RED + "skellies!" + ChatColor.GRAY + " Prepare for the slaughter!");
          p.sendMessage(ChatColor.DARK_RED + zombmax + ChatColor.GRAY + " skellies with " + ChatColor.DARK_RED + health + ChatColor.GRAY + " health!");
        } else if (((Boolean)this.wolfwave.get(map)).booleanValue()) {
          p.sendMessage(ChatColor.GRAY + "The zombies have called in the " + ChatColor.DARK_RED + "wolves!" + ChatColor.GRAY + " Prepare for the slaughter!");
          p.sendMessage(ChatColor.DARK_RED + zombmax + ChatColor.GRAY + " wolves with " + ChatColor.DARK_RED + health + ChatColor.GRAY + " health!");
        } else {
          p.sendMessage(ChatColor.DARK_RED + zombmax + ChatColor.GRAY + " zombies with " + ChatColor.DARK_RED + health + ChatColor.GRAY + " health!");
        }
      }
    }
    for (Iterator it = this.dead.keySet().iterator(); it.hasNext(); ) {
      String player = (String)it.next();
      if (((String)this.dead.get(player)).equalsIgnoreCase(map)) {
        Player p = Bukkit.getPlayer(player);
        if (p != null) {
          placeInGame(p, (String)this.dead.get(player), true);
          this.pcount.put(map, Integer.valueOf(((Integer)this.pcount.get(map)).intValue() + 1));
          it.remove();
        }
      }
    }
    for (Location blc : (this.doors.get(map)).keySet()) {
      if (((Map)this.doors.get(map)).get(blc) == this.wave.get(map)) {
        Block door = blc.getBlock();
        door.setType(Material.AIR);
        doorsopen = Boolean.valueOf(true);
      }
    }
    for (String player : playersInMap(map)) {
      Player p = Bukkit.getPlayer(player);
      if (p != null) {
        if (this.healnewwave) {
          p.setHealth(20);
          p.setFoodLevel(20);
        }
        if (doorsopen.booleanValue())
          p.sendMessage(ChatColor.BLUE + "Doors have opened!");
      }
    }
  }

  public void NewPerk(String map) {
    int randomperk = this.random.nextInt(6) + 1;
    switch (randomperk) {
    case 1:
      this.gameperks.put(map, Integer.valueOf(1));
      for (String pl : playersInMap(map)) {
        Player p = Bukkit.getPlayer(pl);
        if (p != null) {
          p.sendMessage(ChatColor.DARK_PURPLE + "GODMODE PERK ENABLED");
        }
      }
      break;
    case 2:
      this.gameperks.put(map, Integer.valueOf(2));
      for (String pl : playersInMap(map)) {
        Player p = Bukkit.getPlayer(pl);
        if (p != null) {
          p.sendMessage(ChatColor.DARK_PURPLE + "INSTANT-KILL PERK ENABLED");
        }
      }
      break;
    case 3:
      this.gameperks.put(map, Integer.valueOf(3));
      for (String pl : playersInMap(map)) {
        Player p = Bukkit.getPlayer(pl);
        if (p != null) {
          p.sendMessage(ChatColor.DARK_PURPLE + "FIRE PERK ENABLED");
        }
      }
      break;
    case 4:
      this.gameperks.put(map, Integer.valueOf(4));
      for (String pl : playersInMap(map)) {
        Player p = Bukkit.getPlayer(pl);
        if (p != null) {
          p.sendMessage(ChatColor.DARK_PURPLE + "BONUS XP PERK ENABLED");
        }
      }
      break;
    case 5:
      this.gameperks.put(map, Integer.valueOf(6));
      for (String pl : playersInMap(map)) {
        Player p = Bukkit.getPlayer(pl);
        if (p != null)
          p.sendMessage(ChatColor.DARK_PURPLE + "IRON FIST PERK ENABLED");
      }
    }
  }

  public void NewReward(Player player)
  {
  }

  public void resetBlocks(String map) {
    World world = Bukkit.getWorld((String)this.Maps.get(map));
    List<Location> fire = new ArrayList();
    if ((!this.roundFire.isEmpty()) && (this.roundFire != null)) {
      for (Location str : this.roundFire.keySet()) {
        if (((String)this.roundFire.get(str)).equalsIgnoreCase(map)) {
          fire.add(str);
        }
      }
      for (Location b : fire) {
        b.getBlock().getRelative(BlockFace.UP).setType(Material.AIR);
      }
      fire.clear();
    }
    if ((!this.changedBlocks.isEmpty()) && (this.changedBlocks.get(map) != null)) {
      for (Block b : (this.changedBlocks.get(map)).keySet()) {
        BlockState bstate = (BlockState)((Map)this.changedBlocks.get(map)).get(b);
        b.setTypeIdAndData(bstate.getTypeId(), bstate.getRawData(), false);
      }
      ((Map)this.changedBlocks.get(map)).clear();
    }
    if ((!this.placedBlocks.isEmpty()) && (this.placedBlocks.get(map) != null)) {
      for (Block p : (this.placedBlocks.get(map)).keySet()) {
        p.setType(Material.AIR);
      }
      ((Map)this.placedBlocks.get(map)).clear();
    }
  }

  public void clearDrops(String map) {
    World world = Bukkit.getWorld((String)this.Maps.get(map));
    List<Entity> templist = getEnts(world);
    for (Entity e : templist)
      if ((e instanceof Item))
        e.remove();
  }

  public void hidePlayer(Player player)
  {
    if (player != null) {
      Player[] list = Bukkit.getOnlinePlayers();
      for (Player p : list) {
        p.hidePlayer(player);
        if (!inGame(player))
          player.hidePlayer(p);
      }
    }
  }

  public void unhidePlayer(Player player) {
    if (player != null) {
      Player[] list = Bukkit.getOnlinePlayers();
      for (Player p : list)
        p.showPlayer(player);
    }
  }

  public void addLobby(Location lobbyloc, String map)
  {
    lobbyloc.add(0.5D, 1.0D, 0.5D);
    this.loc.put(map, lobbyloc);
    String savelobbyloc = parseToStr(lobbyloc);
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".lobby", savelobbyloc);
    saveCustomConfig();
  }
  public void removeLobby(Location lobbyloc, String map) {
    lobbyloc.add(0.5D, 1.0D, 0.5D);
    if ((this.loc.containsKey(map)) && (((Location)this.loc.get(map)).equals(lobbyloc))) {
      this.loc.remove(map);
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".lobby", null);
    saveCustomConfig();
  }
  public void addFire(Location fireblock, String map) {
    this.roundFire.put(fireblock, map);
    List adeath = new ArrayList();
    for (Location str : this.roundFire.keySet()) {
      if (((String)this.roundFire.get(str)).equalsIgnoreCase(map)) {
        adeath.add(parseToStr(str));
      }
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".roundfire", adeath);
    saveCustomConfig();
  }
  public void removeFire(Location fireblock, String map) {
    this.roundFire.remove(fireblock);
    List adeath = new ArrayList();
    for (Location str : this.roundFire.keySet()) {
      if (((String)this.roundFire.get(str)).equalsIgnoreCase(map)) {
        adeath.add(parseToStr(str));
      }
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".roundfire", adeath);
    saveCustomConfig();
  }
  public void addSpecial(Location specialblock, String map) {
    this.specialblocks.put(specialblock, map);
    List adeath = new ArrayList();
    for (Location str : this.specialblocks.keySet()) {
      if (((String)this.specialblocks.get(str)).equalsIgnoreCase(map)) {
        adeath.add(parseToStr(str));
      }
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".specialblocks", adeath);
    saveCustomConfig();
  }
  public void removeSpecial(Location specialblock, String map) {
    this.specialblocks.remove(specialblock);
    List adeath = new ArrayList();
    for (Location str : this.specialblocks.keySet()) {
      if (((String)this.specialblocks.get(str)).equalsIgnoreCase(map)) {
        adeath.add(parseToStr(str));
      }
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".specialblocks", adeath);
    saveCustomConfig();
  }
  public void addSpawn(Location spawnblock, int wavenumber, String map) {
    if (this.spawnPoints.get(map) == null) {
      this.spawnPoints.put(map, new HashMap());
    }
    spawnblock.add(0.5D, 1.0D, 0.5D);
    Location testloc = spawnblock;
    ((Map)this.spawnPoints.get(map)).put(testloc, Integer.valueOf(wavenumber));
    List aspawn = new ArrayList();
    for (Location spawnloc : (this.spawnPoints.get(map)).keySet()) {
      aspawn.add(parseToDoorStr(spawnloc, (Integer)((Map)this.spawnPoints.get(map)).get(spawnloc)));
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".zombiespawns", aspawn);
    saveCustomConfig();
  }
  public void removeSpawn(Location spawnblock, String map) {
    ((Map)this.spawnPoints.get(map)).remove(spawnblock);
    List aspawn = new ArrayList();
    for (Location spawnloc : (this.spawnPoints.get(map)).keySet()) {
      aspawn.add(parseToDoorStr(spawnloc, (Integer)((Map)this.spawnPoints.get(map)).get(spawnloc)));
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".zombiespawns", aspawn);
    saveCustomConfig();
  }
  public void addDoor(Location doorblock, int wavenumber, String map) {
    if (this.doors.get(map) == null) {
      this.doors.put(map, new HashMap());
    }
    if (this.doriginals.get(map) == null) {
      this.doriginals.put(map, new HashMap());
    }
    ((Map)this.doors.get(map)).put(doorblock, Integer.valueOf(wavenumber));
    Block b = doorblock.getBlock();
    ((Map)this.doriginals.get(map)).put(b, b.getState());
    List aspawn = new ArrayList();
    for (Location spawnloc : (this.doors.get(map)).keySet()) {
      aspawn.add(parseToDoorStr(spawnloc, (Integer)((Map)this.doors.get(map)).get(spawnloc)));
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".doors", aspawn);
    saveCustomConfig();
  }
  public void removeDoor(Location doorblock, int wavenumber, String map) {
    ((Map)this.doors.get(map)).remove(doorblock);
    List aspawn = new ArrayList();
    for (Location spawnloc : (this.doors.get(map)).keySet()) {
      aspawn.add(parseToDoorStr(spawnloc, (Integer)((Map)this.doors.get(map)).get(spawnloc)));
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".doors", aspawn);
    saveCustomConfig();
  }
  public void addSpec(Location specloc, String map) {
    specloc.add(0.5D, 1.0D, 0.5D);
    this.loc2.put(map, specloc);
    String savelobbyloc = parseToStr(specloc);
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".spectate", savelobbyloc);
    saveCustomConfig();
  }
  public void removeSpec(Location specloc, String map) {
    specloc.add(0.5D, 1.0D, 0.5D);
    if ((this.loc2.containsKey(map)) && (((Location)this.loc2.get(map)).equals(specloc))) {
      this.loc2.remove(map);
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".spectate", null);
    saveCustomConfig();
  }
  public void addLight(Location lightloca, String map) {
    lightloca.add(0.5D, 1.0D, 0.5D);
    this.lightloc.put(map, lightloca);
    String savelobbyloc = parseToStr(lightloca);
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".lightning", savelobbyloc);
    saveCustomConfig();
  }
  public void removeLight(Location lightloca, String map) {
    lightloca.add(0.5D, 1.0D, 0.5D);
    if ((this.lightloc.containsKey(map)) && (((Location)this.lightloc.get(map)).equals(lightloca))) {
      this.lightloc.remove(map);
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".lightning", null);
    saveCustomConfig();
  }
  public void addDeath(Location deathloc, String map) {
    deathloc.add(0.5D, 1.0D, 0.5D);
    this.deathPoints.put(map, deathloc);
    String savelobbyloc = parseToStr(deathloc);
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".waiting", savelobbyloc);
    saveCustomConfig();
  }
  public void removeDeath(Location deathloc, String map) {
    deathloc.add(0.5D, 1.0D, 0.5D);
    if ((this.deathPoints.containsKey(map)) && (((Location)this.deathPoints.get(map)).equals(deathloc))) {
      this.deathPoints.remove(map);
    }
    getCustomConfig().set((String)this.Maps.get(map) + "." + map + ".waiting", null);
    saveCustomConfig();
  }
  @EventHandler
  public void onOpAction(PlayerInteractEvent event) {
    int OPadd = -1;
    int OPremove = -1;
    Block block = event.getClickedBlock();
    Player player = event.getPlayer();
    if (player.isOp())
    {
      if (this.add.containsKey(player.getName())) {
        OPadd = ((Integer)this.add.get(player.getName())).intValue();
      }
      if (this.remove.containsKey(player.getName())) {
        OPremove = ((Integer)this.remove.get(player.getName())).intValue();
      }
      switch (OPadd) {
      case 0:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          addLobby(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Added a lobby!");
        } else if (((Integer)this.easycreate.get(player.getName())).intValue() == 4) {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GREEN + "Now let's add our first zombie spawn points for wave 1 :)");
          player.performCommand("zsa-spawn " + this.commandMap + " 1");
        } else {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 1:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          addSpec(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Added a spectator spawn!");
        } else {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 2:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          addLight(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Added a lightning spot!");
        } else {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 3:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          addSpawn(block.getLocation(), this.commandwave, this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Added a zombie spawn!");
        } else if (((Integer)this.easycreate.get(player.getName())).intValue() == 4) {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GREEN + "We're done! You may want to customize it more with additonal commands!");
          this.easycreate.remove(player.getName());
        } else {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 4:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          addFire(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Added a fire block!");
        } else {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 5:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          addDoor(block.getLocation(), this.commandwave, this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Added a door!");
        } else {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 6:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          if (block.getTypeId() == 35) {
            addSpecial(block.getLocation(), this.commandMap);
            player.sendMessage(ChatColor.GREEN + "Added special action block!");
          } else {
            player.sendMessage(ChatColor.RED + "Not a wool block!");
          }
        } else {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 7:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          addDeath(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Added a waiting area!");
        } else {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
      }

      switch (OPremove) {
      case 0:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          removeLobby(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Removed a lobby!");
        } else {
          this.remove.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 1:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          removeSpec(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Removed a spectator spawn!");
        } else {
          this.remove.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 2:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          removeLight(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Removed a lightning strike spot!");
        } else {
          this.remove.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 3:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          removeSpawn(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Removed a zombie spawn!");
        } else {
          this.remove.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 4:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          removeFire(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Removed a fire block!");
        } else {
          this.remove.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 5:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          removeDoor(block.getLocation(), this.commandwave, this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Removed a door!");
        } else {
          this.remove.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 6:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          if (block.getTypeId() == 35) {
            removeSpecial(block.getLocation(), this.commandMap);
            player.sendMessage(ChatColor.GREEN + "Removed special action block!");
          } else {
            player.sendMessage(ChatColor.RED + "Not a wool block!");
          }
        } else {
          this.remove.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
        break;
      case 7:
        if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
          removeDeath(block.getLocation(), this.commandMap);
          player.sendMessage(ChatColor.GREEN + "Removed a waiting area!");
        } else {
          this.add.remove(player.getName());
          player.sendMessage(ChatColor.GOLD + "Back to normal mode!");
        }
      }
    }
  }

  @EventHandler
  public void onPlayerInteract(PlayerInteractEvent event) {
    Player player = event.getPlayer();
    if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) && (player.hasPermission("zs.signs"))) {
      Block block = event.getClickedBlock();
      if ((block.getState() instanceof Sign)) {
        Sign sign = (Sign)block.getState();
        String[] lines = sign.getLines();
        if ((!this.state.isEmpty()) && (inGame(player)) && 
          (((Integer)this.state.get(playerGame(player))).intValue() > 1)) {
          if (lines.length < 1) {
            return;
          }
          if ((lines[0].equalsIgnoreCase("§9zombie")) && (!lines[1].contains("heal")) && (!lines[1].contains("leave"))) {
            String[] temp = lines[3].split(":");
            int cost = 0;
            int item = 0;
            int amount = 1;
            try {
              amount = Integer.parseInt(temp[1]);
              cost = Integer.parseInt(lines[2]);
              item = Integer.parseInt(temp[0]);
            } catch (Exception e) {
              player.sendMessage("Misconfigured sign, please tell admin!");
            }
            if ((!this.points) && (econ.getBalance(player.getName()) >= cost)) {
              player.getInventory().addItem(new ItemStack[] { new ItemStack(item, amount) });
              player.updateInventory();
              econ.withdrawPlayer(player.getName(), cost);
              player.sendMessage(ChatColor.GREEN + "You bought this item for " + Integer.toString(cost) + " dollars");
            } else if (((Integer)this.playerscore.get(player.getName())).intValue() >= cost) {
              player.getInventory().addItem(new ItemStack[] { new ItemStack(item, 1) });
              player.updateInventory();
              this.playerscore.put(player.getName(), Integer.valueOf(((Integer)this.playerscore.get(player.getName())).intValue() - cost));
              String name = player.getName();
              player.setDisplayName("[" + Integer.toString(((Integer)this.playerscore.get(player.getName())).intValue()) + "]" + name);
              player.sendMessage(ChatColor.GREEN + "You bought this item for " + Integer.toString(cost) + " points");
            } else {
              player.sendMessage("Not enough money to purchase!");
            }
          }
          if ((lines[0].equalsIgnoreCase("§9zombie")) && (lines[1].contains("heal"))) {
            int cost2heal = 50;
            try {
              cost2heal = Integer.parseInt(lines[2]);
            } catch (Exception e) {
              player.sendMessage("Misconfigured sign, please tell admin!");
            }
            if ((!this.points) && (econ.getBalance(player.getName()) >= cost2heal)) {
              econ.withdrawPlayer(player.getName(), cost2heal);
              player.setHealth(20);
              player.setFoodLevel(20);
              player.sendMessage(ChatColor.GREEN + "You have been healed for " + Integer.toString(cost2heal) + " dollars");
            } else if (((Integer)this.playerscore.get(player.getName())).intValue() >= cost2heal) {
              player.setHealth(20);
              player.setFoodLevel(20);
              this.playerscore.put(player.getName(), Integer.valueOf(((Integer)this.playerscore.get(player.getName())).intValue() - cost2heal));
              String name = player.getName();
              player.setDisplayName("[" + Integer.toString(((Integer)this.playerscore.get(player.getName())).intValue()) + "]" + name);
              player.sendMessage(ChatColor.GREEN + "You have been healed for " + Integer.toString(cost2heal) + " points");
            } else {
              player.sendMessage("Not enough money to purchase!");
            }
          }
          if ((lines[0].equalsIgnoreCase("§9zombie")) && (lines[1].contains("leave"))) {
            player.performCommand("bsapl");
          }
          if (lines[0].equalsIgnoreCase("§9zombie door")) {
            int cost2open = 100;
            try {
              cost2open = Integer.parseInt(lines[2]);
            } catch (Exception e) {
              player.sendMessage("Misconfigured sign, please tell admin!");
            }
            if ((!this.points) && (econ.getBalance(player.getName()) >= cost2open))
              try {
                econ.withdrawPlayer(player.getName(), cost2open);
                openDoors(sign.getLocation(), lines[1]);
                player.sendMessage(ChatColor.GREEN + "You have opened these doors for " + Integer.toString(cost2open) + " dollars!");
              } catch (Exception e) {
                player.sendMessage("Misconfigured sign, please tell admin!");
              }
            else if (((Integer)this.playerscore.get(player.getName())).intValue() >= cost2open)
              try {
                this.playerscore.put(player.getName(), Integer.valueOf(((Integer)this.playerscore.get(player.getName())).intValue() - cost2open));
                String name = player.getName();
                player.setDisplayName("[" + Integer.toString(((Integer)this.playerscore.get(player.getName())).intValue()) + "]" + name);
                openDoors(sign.getLocation(), lines[1]);
                player.sendMessage(ChatColor.GREEN + "You have opened these doors for " + Integer.toString(cost2open) + " points!");
              } catch (Exception e) {
                player.sendMessage("Misconfigured sign, please tell admin!");
              }
            else {
              player.sendMessage("Not enough money to purchase!");
            }
          }
        }

        if ((lines[0].equalsIgnoreCase("zombie stats")) && (!lines[1].contains("zombies"))) {
          if (this.justleftgame.containsKey(player.getName())) {
            player.sendMessage("You just left a game! You must wait " + Integer.toString(this.leavetimer - ((Integer)this.justleftgame.get(player.getName())).intValue()) + " seconds to try again!");
            return;
          }
          try {
        	  //TODO: Is this where I can set team joins?
            String joinmap = lines[1];
            if ((numberInMap(joinmap) < ((Integer)this.maxplayers.get(joinmap)).intValue()) && (!inGame(player)) && (!this.dead.containsKey(player.getName()))) {
              Bukkit.broadcastMessage(ChatColor.GOLD + "[ZombieSurvival] " + ChatColor.GREEN + player.getName() + " just joined " + joinmap + "!");
              this.playermap.put(player.getName(), joinmap);
              this.pcount.put(joinmap, Integer.valueOf(((Integer)this.pcount.get(joinmap)).intValue() + 1));
              this.playerscore.put(player.getName(), Integer.valueOf(0));
              this.playerskills.put(player.getName(), Integer.valueOf(0));
              this.playerloconjoin.put(player.getName(), player.getLocation());
              if (((!player.hasPermission("zs.donator")) || (!this.donatorperks)) && (this.invsave)) {
                this.inv.put(player.getName(), player.getInventory().getContents());
              }
              player.sendMessage("You have joined the zombie survival game!");
              if (((Integer)this.state.get(joinmap)).intValue() < 2)
                Games(joinmap, Boolean.valueOf(false));
              else if (((Integer)this.state.get(joinmap)).intValue() > 1)
                placeInGame(player, joinmap, false);
            }
          }
          catch (Exception e) {
            player.sendMessage("Misconfigured sign, please tell admin!");
          }
        }
      }
      if ((block.getState() instanceof Chest)) {
        Chest chest = (Chest)block.getState();
        if ((inGame(player)) && 
          (!mysteryBox(block.getLocation(), chest, player)))
          event.setCancelled(true);
      }
    }
  }

  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    List spblocks = new ArrayList();
    Player player = event.getPlayer();
    if ((!this.state.isEmpty()) && (inGame(player)) && 
      (((Integer)this.state.get(playerGame(player))).intValue() > 1)) {
      Location location = new Location(player.getWorld(), player.getLocation().getX(), player.getLocation().getY() - 1.0D, player.getLocation().getZ());
      Block block = location.getBlock();
      for (Location str : this.specialblocks.keySet()) {
        if (((String)this.specialblocks.get(str)).equalsIgnoreCase(playerGame(player))) {
          spblocks.add(str);
        }
      }
      if ((block.getTypeId() == 35) && (spblocks.contains(block.getLocation()))) {
        Wool wool = new Wool(block.getType(), block.getData());
        if (wool.getColor() == DyeColor.YELLOW) {
          player.setVelocity(player.getLocation().getDirection().multiply(2));
        }
        if ((wool.getColor() == DyeColor.BLACK) && (!player.isDead())) {
          player.setHealth(0);
        }
        if (wool.getColor() == DyeColor.GREEN) {
          player.setVelocity(player.getLocation().getDirection().multiply(0.5D).setY(0));
        }
        if (wool.getColor() == DyeColor.BLUE) {
          player.setVelocity(player.getLocation().getDirection().setY(1));
        }
        if ((wool.getColor() == DyeColor.ORANGE) && (player.getFireTicks() == 0)) {
          player.setFireTicks(200);
        }
        if (wool.getColor() == DyeColor.LIGHT_BLUE) {
          player.setVelocity(player.getLocation().getDirection().setY(2));
        }
        if (wool.getColor() == DyeColor.SILVER) {
          hidePlayer(player);
        }
        if (wool.getColor() == DyeColor.GRAY) {
          unhidePlayer(player);
        }
      }

    }

    spblocks.clear();
  }
  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) { Block block = event.getBlock();
    Player player = event.getPlayer();
    if (this.Signs.contains(block.getLocation())) {
      this.Signs.remove(block.getLocation());
      saveSigns();
    }
    if ((!this.state.isEmpty()) && (inGame(player))) {
      String map = playerGame(player);
      if (((Integer)this.state.get(map)).intValue() > 1) {
        if ((this.blockbreak.contains(Integer.valueOf(block.getTypeId()))) && (!((Map)this.changedBlocks.get(map)).containsKey(block))) {
          ((Map)this.changedBlocks.get(map)).put(block, block.getState());
        } else {
          event.setCancelled(true);
          player.sendMessage(ChatColor.RED + "Not allowed!");
        }
      }
      if ((((Integer)this.state.get(map)).intValue() == 1) && (this.antigreif) && (!player.isOp()))
        event.setCancelled(true);
    } }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    Block block = event.getBlock();
    Player player = event.getPlayer();
    if ((!this.state.isEmpty()) && (inGame(player))) {
      String map = playerGame(player);
      if (((Integer)this.state.get(map)).intValue() > 1) {
        if ((this.blockplace.contains(Integer.valueOf(block.getTypeId()))) && (!((Map)this.placedBlocks.get(map)).containsKey(block))) {
          ((Map)this.placedBlocks.get(map)).put(block, block.getState());
        } else {
          event.setCancelled(true);
          player.sendMessage(ChatColor.RED + "Not allowed!");
        }
      }
      if ((((Integer)this.state.get(map)).intValue() == 1) && (this.antigreif) && (!player.isOp()))
        event.setCancelled(true); 
    }
  }

  @EventHandler
  public void onBlockIgnite(BlockIgniteEvent event) {
    Block block = event.getBlock();
    Player player = event.getPlayer();
    if ((!this.state.isEmpty()) && (inGame(player))) {
      String map = playerGame(player);
      if (((Integer)this.state.get(map)).intValue() > 1) {
        if ((this.blockbreak.contains(Integer.valueOf(block.getTypeId()))) && (!((Map)this.changedBlocks.get(map)).containsKey(block)))
          ((Map)this.changedBlocks.get(map)).put(block, block.getState());
        else {
          event.setCancelled(true);
        }
      }
      if ((((Integer)this.state.get(map)).intValue() == 1) && (this.antigreif) && (!player.isOp()))
        event.setCancelled(true); 
    }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void onDamagePvP(EntityDamageByEntityEvent event) {
    Entity ent = event.getEntity();
    Entity dEnt = event.getDamager();
    int entid = ent.getEntityId();
    int dEntid = dEnt.getEntityId();
    Player player = null;

    if (((ent instanceof Player)) && ((dEnt instanceof Player))) {
      player = (Player)ent;
      Player damager = (Player)dEnt;
      String map = playerGame(player);
      if ((!this.state.isEmpty()) && (inGame(player))) {
        if ((((Integer)this.state.get(map)).intValue() > 1) && 
          (playerGame(player).equalsIgnoreCase(playerGame(damager)))) {
          if (this.dead.containsKey(damager.getName())) {
            event.setCancelled(true);
          }
          if (this.points) {
            int dscore = ((Integer)this.playerscore.get(damager.getName())).intValue();
            int dnewscore = dscore - event.getDamage();
            this.playerscore.put(damager.getName(), Integer.valueOf(dnewscore));
            damager.sendMessage(ChatColor.RED + "Your score has been deducted for attacking a fellow survivor!");
            String dname = damager.getName();
            damager.setDisplayName("[" + dnewscore + "]" + dname);
          } else {
            econ.withdrawPlayer(damager.getName(), event.getDamage());
            damager.sendMessage(ChatColor.RED + "You have been deducted " + Integer.toString(event.getDamage()) + " for attacking another survivor!");
          }
        }

        if ((((Integer)this.state.get(map)).intValue() == 1) && (this.antigreif)) {
          event.setCancelled(true);
        }
      }
    }
    if ((this.zombies.containsKey(Integer.valueOf(entid))) && (!(dEnt instanceof Player)) && (!(dEnt instanceof Projectile))) {
      event.setCancelled(true);
    }
    if ((this.zombies.containsKey(Integer.valueOf(entid))) && (((dEnt instanceof Player)) || ((dEnt instanceof Arrow)))) {
      if ((dEnt instanceof Arrow)) {
        Arrow arrow = (Arrow)dEnt;
        if ((arrow.getShooter() instanceof Player))
          player = (Player)arrow.getShooter();
      }
      else {
        player = (Player)dEnt;
      }
      if ((inGame(player)) && (this.zombies.containsKey(Integer.valueOf(entid)))) {
        String map = playerGame(player);
        if (this.dead.containsKey(player.getName())) {
          event.setCancelled(true);
        }
        if (((String)this.zombies.get(Integer.valueOf(entid))).equalsIgnoreCase(map)) {
          if (this.zombiescore.get(Integer.valueOf(entid)) == null) {
            Map temp = new HashMap();
            temp.put(player.getName(), Integer.valueOf(event.getDamage()));
            this.zombiescore.put(Integer.valueOf(entid), temp);
          } else if (((Map)this.zombiescore.get(Integer.valueOf(entid))).get(player.getName()) == null) {
            ((Map)this.zombiescore.get(Integer.valueOf(entid))).put(player.getName(), Integer.valueOf(event.getDamage()));
          } else {
            int olddamg = ((Integer)((Map)this.zombiescore.get(Integer.valueOf(entid))).get(player.getName())).intValue();
            int damg = olddamg + event.getDamage();
            ((Map)this.zombiescore.get(Integer.valueOf(entid))).put(player.getName(), Integer.valueOf(damg));
          }
        }
        if ((this.donatorperks) && (player.hasPermission("zs.donator"))) {
          int ordmg = event.getDamage() + 4;
          event.setDamage(ordmg);
        }
        switch (((Integer)this.gameperks.get(map)).intValue()) {
        case 2:
          event.setDamage(100);
          break;
        case 3:
          event.getEntity().setFireTicks(60);
          break;
        case 5:
          ent.setVelocity(player.getLocation().getDirection().multiply(3));
        case 4:
        }
      }
    }
    if (((ent instanceof Player)) && (this.zombies.containsKey(Integer.valueOf(dEntid)))) {
      final Player p = (Player)ent;
      if (inGame(p)) {
        String map = playerGame(p);
        if (p.getHealth() - ((Integer)this.wave.get(map)).intValue() * this.damagemulti < 2) p.setHealth(1);
        if((p.getHealth() < 2) && !isPlayerDown(p.getDisplayName())){
      	  p.setNoDamageTicks(200);
      	  PotionEffect slowness = new PotionEffect(PotionEffectType.SLOW, 200, 5);
      	  p.addPotionEffect(slowness, true);
      	  //TODO: Add runable to kill the player if not revived!
      	  event.setCancelled(true);
      	  final String playerName = p.getDisplayName();
      	  downedPlayers.add(playerName);
      	  p.sendMessage(ChatColor.RED + "You are Down! Quick! Get a player to help you up!");
      	  
      	  this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {

      		   public void run() {
      		       if (main.isPlayerDown(playerName)){
      		    	   p.setHealth(0);
      		    	   main.downedPlayers.remove(playerName);
      		    	   p.sendMessage(ChatColor.RED + "You have died while down!");
      		       }
      		   }
      		}, 200L);
      	  
      	  return;
        }
        if (((Integer)this.gameperks.get(map)).intValue() == 1) {
          event.setCancelled(true);
        }
        if (this.damagemulti != 0.0D) {
          event.setDamage(2 + (int)(((Integer)this.wave.get(map)).intValue() * this.damagemulti));
        }
        if (this.effectchance != 0) {
          int doeffect = this.random.nextInt(this.effectchance) + 1;
          if (doeffect == 1) {
            PotionEffect potion1 = new PotionEffect(PotionEffectType.BLINDNESS, this.bitelength, 1);
            PotionEffect potion2 = new PotionEffect(PotionEffectType.CONFUSION, this.bitelength, 1);
            PotionEffect potion3 = new PotionEffect(PotionEffectType.SLOW, this.bitelength, 1);
            PotionEffect potion4 = new PotionEffect(PotionEffectType.WEAKNESS, this.bitelength, 1);
            PotionEffect potion5 = new PotionEffect(PotionEffectType.POISON, this.bitelength / 3, 1);
            PotionEffect potion6 = new PotionEffect(PotionEffectType.HUNGER, this.bitelength, 1);
            List potions = new ArrayList();
            potions.add(potion1);
            potions.add(potion2);
            potions.add(potion3);
            potions.add(potion4);
            potions.add(potion5);
            potions.add(potion6);
            int potionget = this.random.nextInt(6);
            p.addPotionEffect((PotionEffect)potions.get(potionget));
          }
        }
      }
    }
  }

  @EventHandler
  public void onZombieDamage(EntityDamageEvent event) {
    Entity ent = event.getEntity();
    if (ent instanceof Player){
    	Player p = (Player)ent;
    	if (this.isPlayerDown(p.getDisplayName())) event.setCancelled(true);
    }
    if ((!this.state.isEmpty()) && (!this.zombies.isEmpty()) && (this.zombies.get(Integer.valueOf(ent.getEntityId())) != null) && 
      (this.zombies.containsKey(Integer.valueOf(ent.getEntityId()))) && 
      (((Integer)this.state.get(this.zombies.get(Integer.valueOf(ent.getEntityId())))).intValue() > 1) && 
      (!(event instanceof EntityDamageByEntityEvent)) && (!this.allhurt))
      event.setCancelled(true);
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void onZombieDeath(EntityDeathEvent event)
  {
    Entity ent = event.getEntity();
    if ((!this.state.isEmpty()) && (!this.zombies.isEmpty()) && (this.zombies.get(Integer.valueOf(ent.getEntityId())) != null) && 
      (((Integer)this.state.get(this.zombies.get(Integer.valueOf(ent.getEntityId())))).intValue() > 1))
    {
      String map = (String)this.zombies.get(Integer.valueOf(ent.getEntityId()));
      event.getDrops().clear();
      PayPlayers(ent.getEntityId());
      EntityDamageEvent.DamageCause deathreason = ent.getLastDamageCause().getCause();
      if (chance()) {
        event.getDrops().add(new ItemStack(randomItem(), 1));
      }
      if (((Integer)this.gameperks.get(map)).intValue() == 4) {
        event.setDroppedExp(25);
      }
      if (this.fastzombies.containsKey(Integer.valueOf(ent.getEntityId()))) {
        this.fastzombies.remove(Integer.valueOf(ent.getEntityId()));
      }
      if ((this.respawn) && (deathreason != EntityDamageEvent.DamageCause.ENTITY_ATTACK) && (deathreason != EntityDamageEvent.DamageCause.PROJECTILE))
        this.zcount.put(map, Integer.valueOf(((Integer)this.zcount.get(map)).intValue() - 1));
      else {
        this.zslayed.put(map, Integer.valueOf(((Integer)this.zslayed.get(map)).intValue() + 1));
      }
      this.zombies.remove(Integer.valueOf(ent.getEntityId()));
      if (((Integer)this.zslayed.get(map)).intValue() >= ((Integer)this.wavemax.get(map)).intValue()) {
        NewWave(map);
      }

      if ((ent.getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
        EntityDamageByEntityEvent entitykiller = (EntityDamageByEntityEvent)ent.getLastDamageCause();
        if (((entitykiller.getDamager() instanceof Player)) || ((entitykiller.getDamager() instanceof Arrow))) {
          Player player = null;
          if ((entitykiller.getDamager() instanceof Arrow)) {
            Arrow arrow = (Arrow)entitykiller.getDamager();
            if ((arrow.getShooter() instanceof Player))
              player = (Player)arrow.getShooter();
          }
          else {
            player = (Player)entitykiller.getDamager();
          }
          try {
            if (playerGame(player).equalsIgnoreCase(map)) {
              int kills = ((Integer)this.playerskills.get(player.getName())).intValue();
              kills++;
              this.playerskills.put(player.getName(), Integer.valueOf(kills));
              this.secondkills.put(map, Integer.valueOf(((Integer)this.secondkills.get(map)).intValue() + 1));
            }
          } catch (Exception e) {
            if (player != null) {
              player.sendMessage("Naughty boy, back to spawn!");
              player.teleport(player.getWorld().getSpawnLocation());
              this.playermap.remove(player.getName());
              this.playerscore.put(player.getName(), Integer.valueOf(0));
              this.playerskills.remove(player.getName());
              this.dead.remove(player.getName());
              if ((this.emptyaccount) && (!this.points)) {
                double removem = econ.getBalance(player.getName());
                econ.withdrawPlayer(player.getName(), removem);
              }
              String name = player.getName();
              player.setDisplayName(name);
            }
          }
        }
      }
    }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPlayerDeath(PlayerDeathEvent event) {
    Player killed = event.getEntity();
    if ((!this.state.isEmpty()) && (inGame(killed))) {
      String map = playerGame(killed);
      getLogger().info(killed.getName() + " has died! Was in game: " + map);
      if (killed.hasPermission("zs.donator")) {
        this.deaddrops.put(killed.getName(), killed.getInventory().getContents());
      } else if ((killed.hasPermission("zs.bypass")) && 
        (killed.getInventory().contains(Material.DIAMOND_SWORD))) {
        ItemStack[] temp = killed.getInventory().getContents();
        List temp2 = new ArrayList();
        for (ItemStack stack : temp) {
          if ((stack != null) && (stack.getType() == Material.DIAMOND_SWORD)) {
            temp2.add(stack);
            break;
          }
        }
        ItemStack[] set = (ItemStack[])(ItemStack[])temp2.toArray(new ItemStack[temp2.size()]);
        this.deaddrops.put(killed.getName(), set);
      }

      event.getDrops().clear();
      this.playerscore.put(killed.getName(), Integer.valueOf(0));
      this.pcount.put(map, Integer.valueOf(((Integer)this.pcount.get(map)).intValue() - 1));
      this.dead.put(killed.getName(), map);
      GamesOver(map, Boolean.valueOf(false));
      if ((!this.points) && (this.deathloss > 0.0D)) {
        double original = econ.getBalance(killed.getName());
        double withdraw = original * this.deathloss;
        econ.withdrawPlayer(killed.getName(), withdraw);
      }
      if ((killed.getLastDamageCause() instanceof EntityDamageByEntityEvent)) {
        EntityDamageByEntityEvent killer = (EntityDamageByEntityEvent)killed.getLastDamageCause();
        if ((killer.getDamager() instanceof Zombie)) {
          killed.sendMessage(ChatColor.DARK_PURPLE + "You Died Nobly!");
        }
        if ((killer.getDamager() instanceof Player))
          killed.sendMessage(ChatColor.DARK_PURPLE + "You Died By Treason!");
      }
      else {
        killed.sendMessage(ChatColor.DARK_PURPLE + "You Died!");
      }
    }
  }

  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPlayerRespawn(PlayerRespawnEvent event) {
    final Player player = event.getPlayer();
    final String name = player.getName();
    if (this.deaddrops.containsKey(name)) {
      player.getInventory().setContents((ItemStack[])this.deaddrops.get(name));
      player.updateInventory();
      this.deaddrops.remove(name);
    }
    Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
    {
      public void run() {
        if ((!main.this.state.isEmpty()) && (main.this.inGame(player))) {
          String map = main.this.playerGame(player);
          if ((((Integer)main.this.state.get(map)).intValue() > 1) && 
            (main.this.dead.containsKey(name)))
            if ((main.this.loc2.get(map) != null) && (main.this.spectateallow)) {
              player.teleport((Location)main.this.loc2.get(map));
              player.setAllowFlight(true);
              player.setFlying(true);
              main.this.hidePlayer(player);
              player.getInventory().clear();
              player.getInventory().setArmorContents(null);
            } else if ((main.this.deathPoints.get(map) != null) && (!main.this.spectateallow)) {
              player.teleport((Location)main.this.deathPoints.get(map));
            } else {
              player.teleport(player.getWorld().getSpawnLocation());
              player.sendMessage("No waiting lobby defined and spectating disabled. Back to spawn!");
            }
        }
      } } );
  }

  @EventHandler
  public void onSignChange(SignChangeEvent event) {
    String[] lines = event.getLines();
    if ((lines[0].equalsIgnoreCase("zombie")) && (!lines[1].isEmpty())) {
      Player player = event.getPlayer();
      event.setLine(0, "§9zombie");
      if (player.isOp())
        player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
      else
        event.setLine(0, "MUST BE OP");
    }
    else if (lines[0].equalsIgnoreCase("zombie")) {
      event.setLine(0, "BAD SIGN");
    }
    if ((lines[0].equalsIgnoreCase("zombie stats")) && ((this.Maps.containsKey(lines[1])) || (this.Maps.containsKey(lines[2])))) {
      Player player = event.getPlayer();
      event.setLine(0, "zombie stats");
      if (player.isOp()) {
        Location sloc = event.getBlock().getLocation();
        this.Signs.add(sloc);
        saveSigns();
        player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
      } else {
        event.setLine(0, "MUST BE OP");
      }
    } else if (lines[0].equalsIgnoreCase("zombie stats")) {
      event.setLine(0, "BAD SIGN");
    }
    if ((lines[0].equalsIgnoreCase("zombie door")) && (!lines[2].isEmpty()) && (this.Maps.containsKey(lines[1]))) {
      Player player = event.getPlayer();
      event.setLine(0, "§9zombie door");
      if (player.isOp())
        player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
      else
        event.setLine(0, "MUST BE OP");
    }
    else if (lines[0].equalsIgnoreCase("zombie door")) {
      event.setLine(0, "BAD SIGN");
    }
    if ((lines[0].equalsIgnoreCase("zombie box")) && (!lines[1].isEmpty())) {
      Player player = event.getPlayer();
      event.setLine(0, "§9zombie box");
      if (player.isOp())
        player.sendMessage(ChatColor.GREEN + "You have created a new ZombieSurvival sign!");
      else
        event.setLine(0, "MUST BE OP");
    }
    else if (lines[0].equalsIgnoreCase("zombie box")) {
      event.setLine(0, "BAD SIGN");
    }
  }
  @EventHandler
  public void onPlayerLogin(PlayerLoginEvent e) {
    spawnController();
  }
  @EventHandler(priority=EventPriority.HIGHEST)
  public void onPlayerJoin(PlayerJoinEvent event) { spawnController();
    this.onlinep += 1;
    Player player = event.getPlayer();
    String name = player.getName();
    if (!inGame(player)) {
      if ((this.invsave) && (this.inv.containsKey(name))) {
        player.getInventory().setContents((ItemStack[])this.inv.get(name));
        player.updateInventory();
        this.inv.remove(name);
      }
      this.playerscore.put(name, Integer.valueOf(0));
      player.setHealth(20);
      player.setFoodLevel(20);
      unhidePlayer(player);
      player.setDisplayName(name);
      if (this.forcespawn) {
        player.teleport(player.getWorld().getSpawnLocation());
      }
    }
    if ((this.forceclear) && (!this.twomintimer.contains(name))) {
      player.getInventory().clear();
      player.getInventory().setArmorContents(null);
    }
    if (inGame(player)) {
      String game = playerGame(player);
      if ((this.state.get(game) != null) && (((Integer)this.state.get(game)).intValue() < 2)) {
        player.getInventory().clear();
        player.getInventory().setArmorContents(null);
        player.teleport(player.getWorld().getSpawnLocation());
        this.playermap.remove(name);
        this.playerscore.put(name, Integer.valueOf(0));
        this.playerskills.remove(name);
        this.dead.remove(name);
        if ((this.emptyaccount) && (!this.points)) {
          double removem = econ.getBalance(name);
          econ.withdrawPlayer(name, removem);
        }
        player.setDisplayName(name);
      }
    }
    if (player.isOp()) {
      Version();
      if (this.outofdate) {
        player.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.RED + "OUT OF DATE! UPDATE AVAILABLE");
      }
    }
    if (this.automated) {
      List options = new ArrayList(2);
      player.sendMessage(ChatColor.GREEN + this.joinmessage);
      for (String str : this.votemap.keySet()) {
        options.add(str);
      }
      player.sendMessage(ChatColor.BLUE + "Voting Time!");
      player.sendMessage(ChatColor.AQUA + "Your options are: " + ChatColor.DARK_GREEN + (String)options.get(0) + ChatColor.AQUA + " and " + ChatColor.DARK_GREEN + (String)options.get(1));
      player.sendMessage(ChatColor.GREEN + "To vote type /vote <option name>    EXAMPLE: /vote default");
    }
    if ((this.antigreif) && (!this.automated))
      player.sendMessage(ChatColor.GREEN + "Right click a sign or type /join to join a ZombieSurvival game!"); }

  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    this.onlinep -= 1;
    final Player player = event.getPlayer();
    final String name = player.getName();
    if (inGame(player)) {
      this.twomintimer.add(name);
      final String map = playerGame(player);
      if (onlinepcount(map) > 1) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
        {
          public void run() {
            if (!player.isOnline()) {
              if ((!main.this.points) && (main.this.emptyaccount)) {
                double original = main.econ.getBalance(name);
                main.econ.withdrawPlayer(name, original);
              } else if ((!main.this.points) && (main.this.deathloss > 0.0D)) {
                double original = main.econ.getBalance(name);
                double withdraw = original * main.this.deathloss;
                main.econ.withdrawPlayer(name, withdraw);
              }
              if (!main.this.dead.containsKey(name)) {
                int tempcount = ((Integer)main.this.pcount.get(map)).intValue();
                main.this.pcount.put(map, Integer.valueOf(tempcount - 1));
              }
              main.this.playermap.remove(name);
              main.this.playerscore.remove(name);
              main.this.twomintimer.remove(name);
              main.this.dead.remove(name);
              main.this.GamesOver(map, Boolean.valueOf(false));
            }
          }
        }
        , 1200L);
      }
      else
      {
        if ((!this.points) && (this.emptyaccount)) {
          double original = econ.getBalance(name);
          econ.withdrawPlayer(name, original);
        } else if ((!this.points) && (this.deathloss > 0.0D)) {
          double original = econ.getBalance(name);
          double withdraw = original * this.deathloss;
          econ.withdrawPlayer(name, withdraw);
        }
        if (!this.dead.containsKey(name)) {
          int tempcount = ((Integer)this.pcount.get(map)).intValue();
          this.pcount.put(map, Integer.valueOf(tempcount - 1));
        }
        this.playermap.remove(name);
        this.playerscore.remove(name);
        this.twomintimer.remove(name);
        this.dead.remove(player.getName());
        GamesOver(map, Boolean.valueOf(false));
      }
    }
  }

  @EventHandler
  public void onPlayerKick(PlayerKickEvent event) {
    this.onlinep -= 1;
    final Player player = event.getPlayer();
    final String name = player.getName();
    if (inGame(player)) {
      this.twomintimer.add(name);
      final String map = playerGame(player);
      if (onlinepcount(map) > 1) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new Runnable()
        {
          public void run() {
            if (!player.isOnline()) {
              if ((!main.this.points) && (main.this.emptyaccount)) {
                double original = main.econ.getBalance(name);
                main.econ.withdrawPlayer(name, original);
              } else if ((!main.this.points) && (main.this.deathloss > 0.0D)) {
                double original = main.econ.getBalance(name);
                double withdraw = original * main.this.deathloss;
                main.econ.withdrawPlayer(name, withdraw);
              }
              if (!main.this.dead.containsKey(name)) {
                int tempcount = ((Integer)main.this.pcount.get(map)).intValue();
                main.this.pcount.put(map, Integer.valueOf(tempcount - 1));
              }
              main.this.playermap.remove(name);
              main.this.playerscore.remove(name);
              main.this.twomintimer.remove(name);
              main.this.dead.remove(name);
              main.this.GamesOver(map, Boolean.valueOf(false));
            }
          }
        }
        , 200L);
      }
      else
      {
        if ((!this.points) && (this.emptyaccount)) {
          double original = econ.getBalance(name);
          econ.withdrawPlayer(name, original);
        } else if ((!this.points) && (this.deathloss > 0.0D)) {
          double original = econ.getBalance(name);
          double withdraw = original * this.deathloss;
          econ.withdrawPlayer(name, withdraw);
        }
        if (!this.dead.containsKey(name)) {
          int tempcount = ((Integer)this.pcount.get(map)).intValue();
          this.pcount.put(map, Integer.valueOf(tempcount - 1));
        }
        this.playermap.remove(name);
        this.playerscore.remove(name);
        this.twomintimer.remove(name);
        this.dead.remove(player.getName());
        GamesOver(map, Boolean.valueOf(false));
      }
    }
  }

  @EventHandler
  public void onPlayerPickup(PlayerPickupItemEvent event) {
    Player player = event.getPlayer();
    if (this.dead.containsKey(player.getName()))
      event.setCancelled(true);
  }

  public void joinItems(Player player) {
    if ((player.hasPermission("zs.bypass")) && (this.donatorperks)) {
      if (player.getInventory().contains(Material.DIAMOND_SWORD)) {
        ItemStack[] temp = player.getInventory().getContents();
        List temp2 = new ArrayList();
        for (ItemStack stack : temp) {
          if ((stack != null) && (stack.getType() == Material.DIAMOND_SWORD)) {
            temp2.add(stack);
            break;
          }
        }
        ItemStack[] set = (ItemStack[])(ItemStack[])temp2.toArray(new ItemStack[temp2.size()]);
        player.getInventory().setContents(set);
        player.updateInventory();
      }
    } else if ((!player.hasPermission("zs.donator")) || (!this.donatorperks)) {
      player.getInventory().clear();
      player.getInventory().setArmorContents(null);
    }
    if ((!this.joinitems.isEmpty()) && (this.itemsatjoin)) {
      for (Integer item : this.joinarmor) {
        player.getInventory().addItem(new ItemStack[] { new ItemStack(item.intValue(), 1) });
        player.updateInventory();
      }
      for (Integer item : this.joinitems) {
        player.getInventory().addItem(new ItemStack[] { new ItemStack(item.intValue(), 1) });
        player.updateInventory();
      }
      player.sendMessage(ChatColor.GREEN + "Check Inventory for Items!");
    }
  }

  public void Version() {
    try {
      URL url = new URL("https://dl.dropbox.com/u/34805710/zsinfo.txt");
      BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
      String str;
      while ((str = in.readLine()) != null) {
        if (str.equals(this.VERSION)) {
          getLogger().info("Version is current!"); continue;
        }
        //Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.RED + "OUT OF DATE! UPDATE AVAILABLE");
        //Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.RED + "Newest Version: " + str + " Your Version: " + this.VERSION);
        this.outofdate = true;
      }
    } catch (MalformedURLException e) {
    } catch (IOException e) {
    }
  }

  private boolean setupEconomy() {
    if (getServer().getPluginManager().getPlugin("Vault") == null) {
      return false;
    }
    RegisteredServiceProvider rsp = getServer().getServicesManager().getRegistration(Economy.class);
    if (rsp == null)
    {
      return false;
    }
    econ = (Economy)rsp.getProvider();
    return econ != null;
  }

  public String playerGame(Player p) {
    if (p == null) {
      return null;
    }
    if (this.playermap.containsKey(p.getName())) {
      String map = (String)this.playermap.get(p.getName());
      return map;
    }
    return null;
  }
  public boolean inGame(Player p) {
    if (p == null) {
      return false;
    }

    return this.playermap.containsKey(p.getName());
  }

  public List<String> playersInMap(String map)
  {
    if (map == null) {
      return null;
    }
    List mapplayers = new ArrayList();
    for (String p : this.playermap.keySet()) {
      if (((String)this.playermap.get(p)).equalsIgnoreCase(map)) {
        mapplayers.add(p);
      }
    }
    return mapplayers;
  }
  public void autoStart() {
    Player[] players = Bukkit.getOnlinePlayers();
    List options = new ArrayList();
    for (int i = 0; i < 3; i++) {
      int rand = this.random.nextInt(this.Maps.size() - 1);
      this.votemap.put(this.Maps.get(Integer.valueOf(rand)), Integer.valueOf(0));
    }
    for (String str : this.votemap.keySet()) {
      options.add(str);
    }
    for (Player p : players)
      if (p != null) {
        p.sendMessage(ChatColor.BLUE + "Voting Time!");
        p.sendMessage(ChatColor.AQUA + "Your options are: " + ChatColor.DARK_GREEN + (String)options.get(0) + ChatColor.AQUA + " and " + ChatColor.DARK_GREEN + (String)options.get(1));
        p.sendMessage(ChatColor.GREEN + "To vote type /vote <option name>    EXAMPLE: /vote default");
      }
  }

  public String votedMap() {
    String result = null;
    List votecount = new ArrayList(2);
    for (String str : this.votemap.keySet()) {
      votecount.add(this.votemap.get(str));
    }
    int max = Math.max(((Integer)votecount.get(0)).intValue(), ((Integer)votecount.get(1)).intValue());
    for (String str : this.votemap.keySet()) {
      if (((Integer)this.votemap.get(str)).intValue() == max) {
        result = str;
        return result;
      }
    }
    return result;
  }
  public int randomItem() {
    if (this.dropchance != 0) {
      int rand = this.random.nextInt(this.drops.size());
      int item = ((Integer)this.drops.get(rand)).intValue();
      return item;
    }
    return 0;
  }
  public boolean chance() {
    if (this.dropchance != 0) {
      List temp = new ArrayList();
      for (int i = 0; i < this.dropchance; i++) {
        temp.add(Integer.valueOf(i));
      }
      int rand = this.random.nextInt(temp.size());
      int result = ((Integer)temp.get(rand)).intValue();
      if (result == 1) {
        return true;
      }
    }
    return false;
  }
  public void maxfinder(String map) {
    if (((Integer)this.maxzombies.get(map)).intValue() < 10) {
      this.wavemax.put(map, Integer.valueOf((int)(((Integer)this.maxzombies.get(map)).intValue() * ((Integer)this.wave.get(map)).intValue() * 0.5D)));
    }
    if ((((Integer)this.maxzombies.get(map)).intValue() >= 10) && (((Integer)this.maxzombies.get(map)).intValue() <= 50)) {
      this.wavemax.put(map, Integer.valueOf((int)(((Integer)this.maxzombies.get(map)).intValue() * ((Integer)this.wave.get(map)).intValue() * 0.1D)));
    }
    if ((((Integer)this.maxzombies.get(map)).intValue() >= 51) && (((Integer)this.maxzombies.get(map)).intValue() <= 100)) {
      this.wavemax.put(map, Integer.valueOf((int)(((Integer)this.maxzombies.get(map)).intValue() * ((Integer)this.wave.get(map)).intValue() * 0.08D)));
    }
    if ((((Integer)this.maxzombies.get(map)).intValue() >= 101) && (((Integer)this.maxzombies.get(map)).intValue() <= 200)) {
      this.wavemax.put(map, Integer.valueOf((int)(((Integer)this.maxzombies.get(map)).intValue() * ((Integer)this.wave.get(map)).intValue() * 0.05D)));
    }
    if (((Integer)this.maxzombies.get(map)).intValue() >= 201) {
      this.wavemax.put(map, Integer.valueOf((int)(((Integer)this.maxzombies.get(map)).intValue() * ((Integer)this.wave.get(map)).intValue() * 0.04D)));
    }
    if (((Integer)this.wavemax.get(map)).intValue() < 1)
      this.wavemax.put(map, Integer.valueOf(1));
  }

  public String annouceMax(String map) {
    int max = 0;
    if (((Integer)this.maxzombies.get(map)).intValue() < 10) {
      max = (int)(((Integer)this.maxzombies.get(map)).intValue() * ((Integer)this.wave.get(map)).intValue() * 0.5D);
    }
    if ((((Integer)this.maxzombies.get(map)).intValue() >= 10) && (((Integer)this.maxzombies.get(map)).intValue() <= 50)) {
      max = (int)(((Integer)this.maxzombies.get(map)).intValue() * ((Integer)this.wave.get(map)).intValue() * 0.1D);
    }
    if ((((Integer)this.maxzombies.get(map)).intValue() >= 51) && (((Integer)this.maxzombies.get(map)).intValue() <= 100)) {
      max = (int)(((Integer)this.maxzombies.get(map)).intValue() * ((Integer)this.wave.get(map)).intValue() * 0.08D);
    }
    if ((((Integer)this.maxzombies.get(map)).intValue() >= 101) && (((Integer)this.maxzombies.get(map)).intValue() <= 200)) {
      max = (int)(((Integer)this.maxzombies.get(map)).intValue() * ((Integer)this.wave.get(map)).intValue() * 0.05D);
    }
    if (((Integer)this.maxzombies.get(map)).intValue() >= 201) {
      max = (int)(((Integer)this.maxzombies.get(map)).intValue() * ((Integer)this.wave.get(map)).intValue() * 0.04D);
    }
    String string = Integer.toString(max);
    return string;
  }
  public void SignUpdater() {
    for (Iterator it = this.Signs.iterator(); it.hasNext(); ) {
      Location sloc = (Location)it.next();
      Block block = sloc.getBlock();
      if ((block.getState() instanceof Sign)) {
        Sign sign = (Sign)block.getState();
        String[] lines = sign.getLines();
        if (lines[1].contains("zombies")) {
          String map = lines[2];
          if ((this.wave.get(map) != null) && (this.Maps.containsKey(map)) && (this.zcount.get(map) != null) && (((Integer)this.state.get(map)).intValue() > 1)) {
            sign.setLine(1, "" + Integer.toString(((Integer)this.zcount.get(map)).intValue() - ((Integer)this.zslayed.get(map)).intValue()) + " zombies");
            sign.setLine(3, "Wave: " + Integer.toString(((Integer)this.wave.get(map)).intValue()));
            sign.update();
          } else {
            sign.setLine(1, "no zombies in");
            sign.setLine(3, "Not Started");
            sign.update();
          }
          if (!this.Maps.containsKey(map)) {
            block.setTypeId(0);
            it.remove();
            saveSigns();
          }
        } else if (this.Maps.containsKey(lines[1])) {
          sign.setLine(2, "Players: " + Integer.toString(numberInMap(lines[1])) + "/" + Integer.toString(((Integer)this.maxplayers.get(lines[1])).intValue()));
          sign.setLine(3, "Wave: " + Integer.toString(((Integer)this.wave.get(lines[1])).intValue()));
          sign.update();
        } else {
          block.setTypeId(0);
          it.remove();
          saveSigns();
        }
      }
    }
  }

  public void saveSigns() {
    List temp = new ArrayList();
    for (Location strloc : this.Signs) {
      temp.add(parseToStr(strloc));
      getCustomConfig().set(strloc.getWorld().getName() + ".signs", null);
      getCustomConfig().set(strloc.getWorld().getName() + ".signs", temp);
    }
  }

  public void reloadPlayers() {
    Player[] list = Bukkit.getOnlinePlayers();
    for (Player p : list) {
      this.playerscore.put(p.getName(), Integer.valueOf(0));
      this.playerskills.put(p.getName(), Integer.valueOf(0));
      this.onlinep += 1;
    }
  }

  public void openDoors(Location middle, String map) {
    World world = Bukkit.getWorld((String)this.Maps.get(map));
    for (int x = middle.getBlockX() - this.doorfindradius; x <= middle.getBlockX() + this.doorfindradius; x++)
      for (int y = middle.getBlockY() - this.doorfindradius; y <= middle.getBlockY() + this.doorfindradius; y++)
        for (int z = middle.getBlockZ() - this.doorfindradius; z <= middle.getBlockZ() + this.doorfindradius; z++) {
          Location temp = new Location(world, x, y, z);
          if (((Map)this.doors.get(map)).containsKey(temp)) {
            Block block = temp.getBlock();
            if (block.getType() != Material.AIR) {
              ((Map)this.changedBlocks.get(map)).put(block, block.getState());
              block.setType(Material.AIR);
            }
          }
        }
  }

  public void QuickUpdate()
  {
    this.task2 = getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
      public void run() {
    	  
        for (Iterator i = main.this.Maps.keySet().iterator(); i.hasNext(); ) { String map = (String)i.next();
          World world = Bukkit.getWorld((String)main.this.Maps.get(map));
          if ((((Integer)main.this.state.get(map)).intValue() > 1) && (world != null)) {
            ArrayList<Location> plist = new ArrayList();
            for (String pl : main.this.playersInMap(map)) {
              Player p = Bukkit.getPlayer(pl);
              if ((!main.this.dead.containsKey(pl)) && (p != null) && (!p.isDead())) {
                plist.add(p.getLocation());
              }
            }
            List<Entity> templist = main.this.getEnts(world);
            for (Entity went : templist) {
              int id = went.getEntityId();
              if ((main.this.zombies.containsKey(Integer.valueOf(id))) && (((String)main.this.zombies.get(Integer.valueOf(id))).equalsIgnoreCase(map))) {
                Location myLocation = went.getLocation();
                Location closest = (Location)main.this.loc.get(map);
                double closestDist;
                if (plist.size() > 0) {
                  closest = (Location)plist.get(0);
                  closestDist = closest.distanceSquared(myLocation);
                  for (Location locs : plist) {
                    if (locs.distanceSquared(myLocation) < closestDist) {
                      closestDist = locs.distanceSquared(myLocation);
                      closest = locs;
                    }
                  }
                }
                LivingEntity mon = (LivingEntity)went;
                if (mon.getType() == EntityType.WOLF) {
                  Wolf w = (Wolf)mon;
                  main.this.updateTarget(map, w);
                }
                Location TravelTo = main.this.getSegment(mon, closest);
                if ((main.this.fastzombies.containsKey(Integer.valueOf(went.getEntityId()))) && (((String)main.this.fastzombies.get(Integer.valueOf(went.getEntityId()))).equalsIgnoreCase(map)))
                  main.this.livingEntityMoveTo(mon, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), (float)main.this.fastseekspeed);
                else
                  main.this.livingEntityMoveTo(mon, TravelTo.getX(), TravelTo.getY(), TravelTo.getZ(), (float)main.this.seekspeed);
              }
            }
          }
        }
        String map;
        List plist;
      }
    }
    , 4L, 4L);
  }

  public void updateTarget(String map, Wolf w)
  {
    if ((targetSelector != null) && ((w instanceof CraftWolf))) {
      CraftWolf cw = (CraftWolf)w;
      EntityWolf ew = cw.getHandle();
      PathfinderGoalSelector s = null;
      try {
        s = (PathfinderGoalSelector)targetSelector.get(ew);
      } catch (Exception e) {
      }
      if (s != null) {
        s.a(4, new PathfinderGoalNearestAttackableTarget(ew, EntityHuman.class, 16.0F, 0, true));
      }
    }
    w.setAngry(true);
  }
  public boolean livingEntityMoveTo(LivingEntity livingEntity, double x, double y, double z, float speed) {
    return ((CraftLivingEntity)livingEntity).getHandle().getNavigation().a(x, y, z, speed);
  }
  private Location getSegment(LivingEntity mob, Location tar) {
    if ((Math.abs(tar.getX() - mob.getLocation().getX()) < 10.0D) && (Math.abs(tar.getZ() - mob.getLocation().getZ()) < 10.0D))
    {
      return tar;
    }
    return trigdis(mob.getLocation(), tar);
  }
  private Location trigdis(Location o, Location t) {
    double xdis = 9.0D;
    double zdis = 9.0D;
    if (t.getX() < o.getX()) {
      xdis = -9.0D;
    }
    if (Math.abs(o.getX() - t.getX()) < 9.0D) {
      xdis = -(o.getX() - t.getX());
    }
    if (t.getZ() < o.getZ()) {
      zdis = -9.0D;
    }
    if (Math.abs(o.getZ() - t.getZ()) < 9.0D) {
      zdis = -(o.getZ() - t.getZ());
    }
    return new Location(o.getWorld(), o.getX() + xdis, o.getY(), o.getZ() + zdis);
  }
  public void PayPlayers(int zombid) {
    try {
      for (String player : (this.zombiescore.get(Integer.valueOf(zombid))).keySet()) {
        Player p = Bukkit.getPlayer(player);
        if (p != null) {
          int newscore = 0;
          if (this.points) {
            newscore = ((Integer)((Map)this.zombiescore.get(Integer.valueOf(zombid))).get(player)).intValue() / 2 + ((Integer)this.playerscore.get(player)).intValue();
            this.playerscore.put(player, Integer.valueOf(newscore));
          } else if (!this.points) {
            newscore = ((Integer)((Map)this.zombiescore.get(Integer.valueOf(zombid))).get(player)).intValue() / 2;
            econ.depositPlayer(player, newscore);
          }
          if (p != null)
            if (this.points) {
              p.sendMessage(ChatColor.GREEN + "You now have " + ChatColor.DARK_RED + Integer.toString(newscore) + ChatColor.GREEN + " points! GAINED: " + ChatColor.DARK_RED + Integer.toString(((Integer)((Map)this.zombiescore.get(Integer.valueOf(zombid))).get(player)).intValue() / 2));
            } else if (!this.points) {
              String score = String.format("%.1f", new Object[] { Double.valueOf(econ.getBalance(player)) });
              p.sendMessage(ChatColor.GREEN + "You now have " + ChatColor.DARK_RED + score + ChatColor.GREEN + " dollars! GAINED: " + ChatColor.DARK_RED + Integer.toString(((Integer)((Map)this.zombiescore.get(Integer.valueOf(zombid))).get(player)).intValue() / 2));
            }
        }
      }
    } catch (Exception e) {
    }
  }

  public void saveCustomConfig() {
    if ((this.customConfig == null) || (this.customConfigFile == null))
      return;
    try
    {
      getCustomConfig().save(this.customConfigFile);
    } catch (IOException ex) {
      getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
    }
  }

  public FileConfiguration getCustomConfig() {
    if (this.customConfig == null) {
      reloadCustomConfig();
    }
    return this.customConfig;
  }
  public void reloadCustomConfig() {
    if (this.customConfigFile == null) {
      this.customConfigFile = new File(getDataFolder(), "games.yml");
    }
    this.customConfig = YamlConfiguration.loadConfiguration(this.customConfigFile);

    InputStream defConfigStream = getResource("games.yml");
    if (defConfigStream != null) {
      YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
      this.customConfig.setDefaults(defConfig);
    }
  }

  public boolean mysteryBox(Location middle, Chest chest, Player p) {
    World world = middle.getWorld();
    for (int x = middle.getBlockX() - 2; x <= middle.getBlockX() + 2; x++) {
      for (int y = middle.getBlockY() - 2; y <= middle.getBlockY() + 2; y++) {
        for (int z = middle.getBlockZ() - 2; z <= middle.getBlockZ() + 2; z++) {
          Location temp = new Location(world, x, y, z);
          Block sign = temp.getBlock();
          if ((sign.getState() instanceof Sign)) {
            Sign actual = (Sign)sign.getState();
            String[] lines = actual.getLines();
            try {
              int cost = Integer.parseInt(lines[1]);
              List viewers = chest.getBlockInventory().getViewers();
              if (viewers.size() > 0) {
                p.sendMessage("Try again later. Only one person at a time!");
                return false;
              }
              if (lines[0].equalsIgnoreCase("§9zombie box")) {
                if ((this.points) && (((Integer)this.playerscore.get(p.getName())).intValue() >= cost)) {
                  this.playerscore.put(p.getName(), Integer.valueOf(((Integer)this.playerscore.get(p.getName())).intValue() - cost));
                  chest.getBlockInventory().clear();
                  int ritem = this.random.nextInt(this.boxitems.size());
                  int item = ((Integer)this.boxitems.get(ritem)).intValue();
                  chest.getBlockInventory().setItem(this.random.nextInt(27), new ItemStack(item, 1));
                  chest.update();
                  String name = p.getName();
                  p.setDisplayName("[" + Integer.toString(((Integer)this.playerscore.get(p.getName())).intValue()) + "]" + name);
                  p.sendMessage(ChatColor.GREEN + "You have purchased this " + ChatColor.DARK_RED + "Mysterybox " + ChatColor.GREEN + "for " + Integer.toString(cost) + " points!");
                  return true;
                }if ((!this.points) && (econ.getBalance(p.getName()) >= cost)) {
                  econ.withdrawPlayer(p.getName(), cost);
                  chest.getBlockInventory().clear();
                  int ritem = this.random.nextInt(this.boxitems.size() - 1);
                  int item = ((Integer)this.boxitems.get(ritem)).intValue();
                  chest.getBlockInventory().setItem(this.random.nextInt(27), new ItemStack(item, 1));
                  chest.update();
                  p.sendMessage(ChatColor.GREEN + "You have purchased this " + ChatColor.DARK_RED + "Mysterybox " + ChatColor.GREEN + "for " + Integer.toString(cost) + " dollars!");
                  return true;
                }
                p.sendMessage("Not enough money to purchase!");
                return false;
              }
            }
            catch (Exception e) {
              return true;
            }
          }
        }
      }
    }
    return true;
  }
  public void checkMobs() {
    getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable()
    {
      public void run()
      {
        Iterator i$;
        if (!main.this.zombies.isEmpty())
          for (i$ = main.this.Maps.keySet().iterator(); i$.hasNext(); ) {String map = (String)i$.next();
            if (((Integer)main.this.state.get(map)).intValue() > 1) {
              World world = Bukkit.getWorld((String)main.this.Maps.get(map));
              List<Entity> templis = main.this.getEnts(world);
              ArrayList tempID = new ArrayList();
              for (Entity id : templis) {
                int entid = id.getEntityId();
                tempID.add(Integer.valueOf(entid));
              }
              List<Integer> IDs = new ArrayList();
              for (Integer i : main.this.zombies.keySet()) {
                if (((String)main.this.zombies.get(i)).equalsIgnoreCase(map)) {
                  IDs.add(i);
                }
              }
              for (Integer i2 : IDs)
                if (!tempID.contains(i2)) {
                  main.this.zslayed.put(map, Integer.valueOf(((Integer)main.this.zslayed.get(map)).intValue() + 1));
                  main.this.zombies.remove(i2);
                }
            }
          }
        String map;
        List tempID;
      }
    }
    , 200L, 200L);
  }

  public void AsynchTasks()
  {
    getServer().getScheduler().scheduleAsyncRepeatingTask(this, new Runnable() {
      public void run() {
        main.this.SignUpdater();
        if ((main.this.automated) && (main.this.vspoke < 1)) {
          main.this.vmap = ((String)main.this.Maps.get(Integer.valueOf(main.this.random.nextInt(main.this.Maps.size() - 1))));
          main.this.cooldowncount.put(main.this.vmap, Integer.valueOf(0));
          main.this.autoStart();
          main.this.vspoke += 1;
        }
        if ((main.this.automated) && (main.this.vspoke != main.this.cooldown)) {
          main.this.vspoke += 1;
        } else if ((main.this.automated) && (main.this.vspoke == main.this.cooldown) && (!main.this.votedMap().isEmpty())) {
          main.this.vmap = main.this.votedMap();
          main.this.cooldowncount.put(main.this.vmap, Integer.valueOf(main.this.cooldown));
          main.this.vspoke += 1;
        } else if ((main.this.automated) && (!main.this.votedMap().isEmpty())) {
          main.this.vspoke = 0;
          main.this.cooldowncount.clear();
        }
        for (Iterator it = main.this.justleftgame.keySet().iterator(); it.hasNext(); ) {
          String string = (String)it.next();
          int orig = ((Integer)main.this.justleftgame.get(string)).intValue();
          if (orig < main.this.leavetimer) {
            orig++;
            main.this.justleftgame.put(string, Integer.valueOf(orig));
          } else {
            it.remove();
          }
        }
      }
    }
    , 20L, 20L);
  }

  public void perpNight()
  {
    getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
      public void run() {
        String t = "";
        for (String s : main.this.Maps.keySet()) {
          String b = (String)main.this.Maps.get(s);
          if (!b.equalsIgnoreCase(t)) {
            t = b;
            World world = Bukkit.getWorld(b);
            if (main.this.perpnight)
              world.setTime(13000L);
          }
        }
      }
    }
    , 5L, 8000L);
  }

  public void dealDamage()
  {
    getServer().getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
      public void run() {
        Player[] list = Bukkit.getOnlinePlayers();
        for (Player p : list) {
          if (((p.getLocation().getBlock().getTypeId() != 8) && (p.getLocation().getBlock().getTypeId() != 9) && (p.getLocation().getBlock().getTypeId() != 30)) || 
            (!main.this.inGame(p)) || (!main.this.infectmat)) continue;
          p.damage(2);
        }
      }
    }
    , 20L, 20L);
  }

  public int onlinepcount(String map)
  {
    int i = 0;
    for (String mp : playersInMap(map)) {
      Player p = Bukkit.getPlayer(mp);
      if ((p != null) && (!this.dead.containsKey(mp))) {
        i++;
      }
    }
    return i;
  }
  public int numberInMap(String map) {
    int i = 0;
    for (String mp : playersInMap(map)) {
      i++;
    }
    return i;
  }
  public void zsDebug(int i, Player p) {
    if (i == 0) {
      Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "-----------Diagnostics Started-----------");
      for (String map : this.Maps.keySet()) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " state: " + Integer.toString(((Integer)this.state.get(map)).intValue()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " onlinepcount: " + Integer.toString(onlinepcount(map)));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " pcount: " + Integer.toString(((Integer)this.pcount.get(map)).intValue()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " numberinmap: " + Integer.toString(numberInMap(map)));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " wave: " + Integer.toString(((Integer)this.wave.get(map)).intValue()));
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " zcount: " + Integer.toString(((Integer)this.zcount.get(map)).intValue()));
      }
      Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Number of Signs: " + Integer.toString(this.Signs.size()));
      Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Number of Maps: " + Integer.toString(this.Maps.size()));
      Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Number of Zombies: " + Integer.toString(this.zombies.size()));
      Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "------------Diagnostics Ended------------");
    }
    if ((i == 1) && (p != null)) {
      p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "-----------Diagnostics Started-----------");
      for (String map : this.Maps.keySet()) {
        p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " state: " + Integer.toString(((Integer)this.state.get(map)).intValue()));
        p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " onlinepcount: " + Integer.toString(onlinepcount(map)));
        p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " pcount: " + Integer.toString(((Integer)this.pcount.get(map)).intValue()));
        p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " numberinmap: " + Integer.toString(numberInMap(map)));
        p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " wave: " + Integer.toString(((Integer)this.wave.get(map)).intValue()));
        p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Game: " + map + " zcount: " + Integer.toString(((Integer)this.zcount.get(map)).intValue()));
      }
      p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Number of Signs: " + Integer.toString(this.Signs.size()));
      p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Number of Maps: " + Integer.toString(this.Maps.size()));
      p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "Number of Zombies: " + Integer.toString(this.zombies.size()));
      p.sendMessage(ChatColor.GREEN + "[ZombieSurvival] " + ChatColor.WHITE + "------------Diagnostics Ended------------");
    }
  }

  public synchronized List getEnts(World world) {
    return world.getEntities();
  }
  public void PopulateDoriginals() {
    for (Iterator i$ = this.Maps.keySet().iterator(); i$.hasNext(); ) {String m = (String)i$.next();
      this.doriginals.put(m, new HashMap());
      for (Location l : (this.doors.get(m)).keySet()) {
        Block b = l.getBlock();
        ((Map)this.doriginals.get(m)).put(b, b.getState());
      } } String m;
  }

  public void resetDoors(String m) {
    try {
      for (Block b : (this.doriginals.get(m)).keySet()) {
        BlockState bstate = (BlockState)((Map)this.doriginals.get(m)).get(b);
        b.setTypeIdAndData(bstate.getTypeId(), bstate.getRawData(), false);
      }
    } catch (Exception e) {
      getLogger().info(m + " Did not have any doors to reset!");
    }
  }
  @EventHandler
  public void onChat(AsyncPlayerChatEvent e) {
    String p = e.getPlayer().getName();
    String m = e.getMessage();
    Player pp = e.getPlayer();
    if (this.easycreate.containsKey(p)) {
      int step = ((Integer)this.easycreate.get(p)).intValue();
      switch (step) {
      case 0:
        this.ecname.put(p, m);
        pp.sendMessage(ChatColor.GREEN + "Please type the max players for " + (String)this.ecname.get(p));
        this.easycreate.put(p, Integer.valueOf(1));
        e.setCancelled(true);
        break;
      case 1:
        try {
          this.ecpcount.put(p, Integer.valueOf(Integer.parseInt(m)));
          this.eczcount.put(p, Integer.valueOf(calcMaxZ(Integer.parseInt(m))));
          pp.sendMessage(ChatColor.GREEN + "Please type the max waves for " + (String)this.ecname.get(p));
          this.easycreate.put(p, Integer.valueOf(2));
        } catch (Exception ec) {
          pp.sendMessage(ChatColor.RED + "Try again, could not parse number!");
        }
        e.setCancelled(true);
        break;
      case 2:
        try {
          this.ecwcount.put(p, Integer.valueOf(Integer.parseInt(m)));
          this.easycreate.put(p, Integer.valueOf(3));
          pp.sendMessage(ChatColor.GOLD + "Creating game for " + (String)this.ecname.get(p) + "!" + ChatColor.DARK_RED + " Is this correct??" + ChatColor.GOLD + " players: " + Integer.toString(((Integer)this.ecpcount.get(p)).intValue()) + " waves: " + Integer.toString(((Integer)this.ecwcount.get(p)).intValue()));
          pp.sendMessage(ChatColor.GREEN + "Please type y or n (yes or no)");
        } catch (Exception ex) {
          pp.sendMessage(ChatColor.RED + "Try again, could not parse number!");
        }
        e.setCancelled(true);
        break;
      case 3:
        if (m.contains("y")) {
          pp.performCommand("zs-create " + (String)this.ecname.get(p) + " " + Integer.toString(((Integer)this.eczcount.get(p)).intValue()) + " " + Integer.toString(((Integer)this.ecpcount.get(p)).intValue()) + " " + Integer.toString(((Integer)this.ecwcount.get(p)).intValue()));
          this.easycreate.put(p, Integer.valueOf(4));
        } else {
          pp.sendMessage(ChatColor.GREEN + "Cancelling!");
          this.easycreate.remove(p);
          this.ecpcount.remove(p);
          this.eczcount.remove(p);
          this.ecwcount.remove(p);
          this.ecname.remove(p);
        }
        e.setCancelled(true);
      }
    }
  }

  public int calcMaxZ(int pmax) {
    if (pmax <= 5)
      return pmax * 12;
    if ((pmax > 5) && (pmax <= 10))
      return pmax * 10;
    if ((pmax > 10) && (pmax <= 15)) {
      return pmax * 9;
    }
    return pmax * 10;
  }

  public void spawnController() {

    this.spawncontrol = true;
    getServer().getScheduler().scheduleAsyncDelayedTask(this, new Runnable() {
      public void run() {
        main.this.spawncontrol = false;
      }
    }
    , 20L);
  }

  public static boolean isPlayerDown(String playerName){
	  for (String p : downedPlayers){
		  if (p.equalsIgnoreCase(playerName)) return true;
	  }
	  return false;
  }

  //TODO: Add heal
  @EventHandler
  public void onHeal(PlayerInteractEntityEvent event)
  {
    if (((event.getRightClicked() instanceof Player)) && (event.getPlayer().getItemInHand().getType() == Material.BONE)) {
      Player e = (Player)event.getRightClicked();
      Player p = event.getPlayer();
      if (isPlayerDown(e.getDisplayName())){
    	  e.setNoDamageTicks(0);
      e.setHealth(20);
      downedPlayers.remove(e.getDisplayName());
      if (p.getItemInHand().getAmount() > 1)
        p.setItemInHand(new ItemStack(Material.BONE, p.getItemInHand().getAmount() - 1));
      else {
        p.setItemInHand(new ItemStack(Material.AIR, 0));
      }
    }
    }
  }

}