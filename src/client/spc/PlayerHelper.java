package net.minecraft.src;
// TODO: Thread scripting
// TODO: Add schedule command
// TODO: Add [enable|disable] to all toggle commands
// TODO: XRAY - Requires render hooks
// TODO: Startup command on new world
// TODO: Repeat command (ms)
// TODO: /endercrystal command
// TODO: F3 entity viewer
// TODO: Update light/chunk command
// TODO HARD: guigive - Creative inventory
// TODO HARD: guicraft - Crafting table
// TODO: NEED RENDER HOOK: (RenderLiving.renderLivingLabel) xrayentity [enable|disable] - View all entities on the map through walls
// TODO: xrayblock [enable|disable] - Makes blocks in the xray list invisible (or mostly transparent) when this mod is enabled
// TODO: stronghold [detect|teleport] - Makes the player rotate in the direction of the stronghold and tells them how far away it is ~north/south,east/west,up/down blocks , or teleports the player there
// TODO: biome [current|list|change [biomename]] - Allow player to change biomes
// TODO: Allow bindings to have multiple states... press once - cmd1, press twice - cmd2, press again - cmd3, press again - cmd1 (or 4...)
// TODO: Moddamage command doesn't let you see minecart chest contents - or destroy them
// TODO: Add mushroom and vine growing to the grow command
// TODO: spawnportal (end portal)
// TODO: /harvest - harvests nearby things, ie: wheat, pumpkins, grass, etc
// TODO HARD: /morph <MOB> - Changes the player into another mob type
// TODO: /sneak [enable|disable]
// DONE: /reach [+|-]
// DONE: /xp level X
// TODO: slowdown/speedup ticks
// TODO: /bring list
// TODO: /killall list
// TODO: rename /destroy to /invdelete
// TODO: heal mobs (radius/pointing at)
// TODO: /sign edit - should edit individual lines of text and keep others
// TODO: heal entities around you
// TODO: Attach commands to blocks, when stepped on/hit/activated it executes the command
// TODO: Power a block nearby
// TODO: ghast fireball
// TODO: weather disable isnt reset using /reset
// TODO: add effect to potion bottle, /i 373:DAMAGE
// TODO: Make /spawner non-case sensitive
// DONE: Fixed WE copy/paste of tile entities
// TODO: /dispenser [fill ITEM|clear]
// TODO: /server mode - forces every command to begin with / otherwise treated as chat
// TODO: /music loop
// TODO: Leaves tree decay

//import com.sijobe.console.GuiConsole;

import java.awt.Desktop;
import java.awt.Frame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import paulscode.sound.SoundSystem;

import net.minecraft.client.Minecraft;

/**
 * This class is the core Single Player Commands class. It handles all input and controls all
 * output
 * 
 * @author simo_415 Copyright (C) 2010-2012 simo_415 - (http://bit.ly/spcmod)
 * 
 *         This file is part of Single Player Commands.
 * 
 *         Single Player Commands is free software: you can redistribute it and/or modify it under
 *         the terms of the GNU Lesser General Public License as published by the Free Software
 *         Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 *         Single Player Commands is distributed in the hope that it will be useful, but WITHOUT ANY
 *         WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *         PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 *         You should have received a copy of the GNU Lesser General Public License along with
 *         Single Player Commands. If not, see <http://www.gnu.org/licenses/>.
 */
public class PlayerHelper {

   public static boolean DEBUG = false;

   public Minecraft mc;
   public EntityPlayerSPSPC ep;
   public HashMap<String, String> commands;

   private HashMap<String, Class<?>> entitylist;
   private HashMap<Integer, Class<?>> entityidlist;
   private List<Class<?>> spawnignore;
   private double prevx;
   private double prevy;
   private double prevz;
   public boolean instant;
   public boolean falldamage;
   public boolean prevfalldamage;
   public boolean waterdamage;
   public boolean prevwaterdamage;
   public boolean damage;
   public boolean prevdamage;
   public boolean prevfiredamage;
   public boolean flying;
   public boolean prevflying;
   public boolean noClip;
   public boolean prevnoclip;
   public boolean dropitems; 
   public boolean isinvisible; 
   public boolean mobsfrozen; 
   public boolean mobdamage; 
   public boolean norepair; 
   public boolean infiniteitems; 
   public boolean keepitems;
   public boolean instantkill; 
   public boolean watermovement;
   public String sessionusername;
   public double gravity;
   public double speed;
   public int timeschedule[];
   public int lastrift;
   public float reachdistance;
   public boolean opened;
   public boolean disablecommands;
   public boolean leftbutton;
   public boolean rightbutton;
   public long lastleftcall;
   public long lastrightcall;
   public boolean ismultiplayer;
   public Vector<Integer> keyboard;
   public Settings bound;
   public boolean light;
   public boolean instantplant;
   public boolean instantgrow;
   public String startup;
   public String engine;
   public boolean output;
   public boolean monsterspawn;
   public boolean animalspawn;
   private SoundSystem sound;
   public double prevspeed;
   public double prevgravity;
   public double superpunch;
   public Settings alias;
   public boolean toggledropgive;
   public int sizewidth;
   public int sizeheight;
   public boolean moveplayer;
   public boolean movecamera;
   public float freezecamyaw;
   public float freezecampitch;
   public int timespeed;
   public boolean updateson;
   public char textcolornormal;
   public char textcolorerror;
   public String textcolorrandom;
   //public boolean sfly;
   public String lastcommand;
   public boolean limitstack;
   public float walked;
   public String flymode;
   public boolean ladderMode;
   public boolean sprinting;
   public double playerSize;
   public boolean criticalHit;
   public boolean disableHunger;
   public boolean creeperExplosion;
   public volatile Settings itemsbound;
   public List<Integer> mouse;
   public boolean weather;
   //public static Vector<SPCPlugin> CORE;
   //public static HashMap<String,Object[]> COMMANDS;

   // TODO: Player Helper and dependencies need to be removed
   public static PlayerHelper PH;
   public static final HashMap<String, String[]> CMDS;
   public static Vector<String> ITEMNAMES;
   public static SPCPluginManager PLUGIN_MANAGER;
   public static File MODDIR = new File(Minecraft.getMinecraftDir(), "mods/sppcommands/");
   public static Object HAS_STARTED_UP = null;
   public static String VALID_COLOURS = "0123456789abcdef";
   public static int MAGICNUMBER = -8383847;

   public static Method PLUGIN_HANDLECOMMAND;
   public static Method PLUGIN_HANDLELEFTCLICK;
   public static Method PLUGIN_HANDLERIGHTCLICK;
   public static Method PLUGIN_HANDLELEFTBUTTONDOWN;
   public static Method PLUGIN_HANDLERIGHTBUTTONDOWN;
   public static Method PLUGIN_ATUPDATE;
   public static Method PLUGIN_SETHELPER;
   public static Method PLUGIN_HANDLECUIEVENT;

   public static String ERRMSG_PARAM = "Invalid number of parameters.";
   public static String ERRMSG_PARSE = "Could not parse input.";
   public static String ERRMSG_NOTSET = "WorldEdit points not set.";
   public static String ERRMSG_OSNOTSUPPORTED = "Your operating system does not support this function.";
   public static String ERRMSG_NPCNOTFOUND = "The specified NPC was not found.";

   public static HashMap<String, double[]> WAYPOINTS;
   public static int ITEM_MAX_DAMAGE[];
   public static InventoryPlayer INV_BEFORE_DEATH;
   //TODO: Write this
   //public static List<Class<SPCPlugin>> CORE_CLASSES;

   public static ClientCommandManager manager;

   static {
      try {
         PLUGIN_HANDLECOMMAND = SPCPlugin.class.getDeclaredMethod("handleCommand", String[].class);
         PLUGIN_HANDLELEFTCLICK = SPCPlugin.class.getDeclaredMethod("handleLeftClick", SPCObjectHit.class);
         PLUGIN_HANDLERIGHTCLICK = SPCPlugin.class.getDeclaredMethod("handleRightClick", SPCObjectHit.class);
         PLUGIN_HANDLELEFTBUTTONDOWN = SPCPlugin.class.getDeclaredMethod("handleLeftButtonDown", SPCObjectHit.class);
         PLUGIN_HANDLERIGHTBUTTONDOWN = SPCPlugin.class.getDeclaredMethod("handleRightButtonDown", SPCObjectHit.class);
         PLUGIN_ATUPDATE = SPCPlugin.class.getDeclaredMethod("atUpdate");
         PLUGIN_SETHELPER = SPCPlugin.class.getDeclaredMethod("setPlayerHelper", PlayerHelper.class);
         PLUGIN_HANDLECUIEVENT = SPCPlugin.class.getDeclaredMethod("handleCUIEvent", String.class, String[].class);
      } catch (Throwable t) {
         PlayerHelper.printStackTrace(t);
      }
   }

   static {
      CMDS = new HashMap<String, String[]>();
      CMDS.put("=", new String[] { "Calculator command", "NUMBER OPERATOR NUMBER", "32 * 17 * 4 + 7" });
      CMDS.put("achievement", new String[] { "Achievement related commands", "<list|unlock>", "unlock" });
      CMDS.put("alias", new String[] { "Create an alias for a command", "<list|<ALIAS> {VALUE}>", "example msg hello world" });
      CMDS.put("ascend", new String[] { "Ascends the player to the platform above", "", "" });
      //1.2 broken CMDS.put("atlantis", new String[] { "Makes your world like Atlantis, the legendary underwater city.", "", "" });
      CMDS.put("biome", new String[] { "Gets the current biome name", "", "" });
      CMDS.put("bind", new String[] { "Binds a keyboard key to a command", "<KEYNAME> <COMMAND>", "e /cannon 5" });
      CMDS.put("bindid", new String[] { "Binds a command to a keyboard key using the key id", "<ID> <COMMAND> {ARGS}", "22 /cannon 5" });
      CMDS.put("binditem", new String[] { "Binds a mouse button to a command when the specified item is in hand", "<MOUSEBUTTON {COMMAND}>", "left /cannon 5" });
      CMDS.put("bring", new String[] { "Brings the specified entity to you.", "[ENTITY]", "item" });
      CMDS.put("calc", new String[] { "Calculator command", "NUMBER OPERATOR NUMBER", "32 * 17 * 4 + 7" });
      CMDS.put("cannon", new String[] { "Shoots TNT in the direction you are pointing, at the specified strength", "[strength]", "10" });
      CMDS.put("chest", new String[] { "Gives you access to chests.", "<drop|get|fill|swap|clear>", "get" });
//       CMDS.put("clear", new String[] { "Clears the chat console.", "", "" });
      CMDS.put("clearchat", new String[] { "Clears the chat console.", "", "" });
      CMDS.put("clearwater", new String[] { "Toggles water clarity on/off", "", "" });
      CMDS.put("climb", new String[] { "Toggles climbing mode on/off", "[enable|disable]", "enable" });
      CMDS.put("clone", new String[] { "Clones the NPC which you are looking at", "[QUANTITY]", "10" });
      CMDS.put("config", new String[] { "Allows you to set the global configuration file", "<setglobal [reset]>", "setglobal" });
      //CMDS.put("confuse", new String[] { "Confuses nearby mobs", "[DISTANCE]", "64" });
      CMDS.put("confusesuicide", new String[] { "Confuses nearby mobs to attack each other", "", "" });
      CMDS.put("clouds", new String[] { "Provides control on whether clouds are shown or not", "[enable|disable]", "disable" });
      //CMDS.put("credits", new String[] { "Shows the end-of-game credits", "", "" });
      CMDS.put("creeper", new String[] { "Allows you to turn off creeper explosions", "<explosion [enable|disable]>", "explosion disable" });
      CMDS.put("criticalhit", new String[] { "Turns critical hits always on", "[enable|disable]", "enable" });
      CMDS.put("cyclepainting", new String[] { "Cycles through the painting which you are pointing at", "", "" });
      CMDS.put("damage", new String[] { "Turns damage on/off", "", "" });
      CMDS.put("defuse", new String[] { "Defuses nearby TNT which has been primed", "[all]", "all" });
      CMDS.put("descend", new String[] { "Descends the player to the next platform below", "", "" });
      CMDS.put("destroy", new String[] { "Destroys the selected itemstack.", "[all]", "" });
      CMDS.put("diff", new String[] { "Sets the difficulty of the game. Valid values 0-3", "<VALUE>", "3" });
//       CMDS.put("difficulty", new String[] { "Sets the difficulty of the game. Valid values 0-3", "<VALUE>", "3" });
      CMDS.put("drops", new String[] { "Turn block drops on or off.", "", "" });
      CMDS.put("dupe", new String[] { "Duplicates the selected itemstack.", "[all]", "all" });
      CMDS.put("duplicate", new String[] { "Duplicates the selected itemstack.", "[all]", "all" });
      CMDS.put("dropstore", new String[] { "Transfers everything in your inventory into a chest that it creates next to you.", "", "" });
      CMDS.put("effect", new String[] { "Configures potion effects on the player", "<list|remove TYPE|add TYPE [DURATION] [STRENGTH]", "add 1 100 1" });
      CMDS.put("enchant", new String[] { "Enchants the currently selected item", "<list|remove|add TYPE [LEVEL]>", "add protection 10" });
      CMDS.put("enderman", new String[] { "Enderman command to enable/disable block pickup", "pickup [enable|disable]", "pickup disable" });
      CMDS.put("explode", new String[] { "Creates an explosion at the players current location.", "<SIZE>", "10" });
      CMDS.put("ext", new String[] { "Extinguishes fires nearby", "[all]", "all" });
      CMDS.put("exterminate", new String[] { "Blows up the NPC which you are looking at", "[SIZE]", "10" });
      CMDS.put("extinguish", new String[] { "Extinguishes fires nearby", "[all]", "all" });
      CMDS.put("falldamage", new String[] { "Turns fall damage onoff", "", "" });
      CMDS.put("feed", new String[] { "Feeds the player the specified number of half points", "<HEAL>", "10" });
      CMDS.put("firedamage", new String[] { "Turns fire damage onoff", "", "" });
      CMDS.put("flammable", new String[] { "Sets the specified block at the flammability level", "<BLOCK> [CATCH] [SPREAD]", "stone 10 100" });
      CMDS.put("fly", new String[] { "Allows the player to fly. See /flymode for alternative flying modes", "[SPEED]", "5" });
      CMDS.put("flymode", new String[] { "Allows the player to fly using a standard fly mechanism.", "<standard|dynamic|minecraft|reset>", "standard" });
      CMDS.put("fog", new String[] { "Changes the render distance", "[tiny|small|normal|far]", "normal" });
      CMDS.put("freecam", new String[] { "Allows you to free cam arond the map", "", ""});
      CMDS.put("freeze", new String[] { "Toggles mobs to be frozen.", "", "" });
      CMDS.put("freezecam", new String[] { "Allows you to freeze the camera where it currently is", "", ""});
      CMDS.put("gamemode2", new String[] { "Changes the game mode", "[0|1|2|survival|creative|adventure]", "creative" });
//       CMDS.put("gamemode", new String[] { "Changes the game mode", "[0|1|2|survival|creative|adventure]", "creative" });
//       CMDS.put("give", new String[] { "Gives player item, if quantity isn't specified maximum amount of that item", "<ITEMCODE|ITEMNAME> [QUANTITY] [DAMAGE]", "1" });
      CMDS.put("goto", new String[] { "Goto a waypoint", "<NAME>", "example" });
      CMDS.put("grow", new String[] { "Grows all saplings and wheat on the map.", "[all]", "" });
      CMDS.put("h", new String[] { "Brings up a help message", "[COMMAND]", "give" });
      CMDS.put("hardcore", new String[] { "Configures the world to be in hardcore mode or not", "[enable|disable]", "enable" });
      CMDS.put("heal", new String[] { "Heals a player the specified number of points", "<HEALTH>", "10" });
      CMDS.put("health", new String[] { "Sets the health of a player to pre-defined figures", "<MIN|MAX|INFINITE>", "max" });
//       CMDS.put("help", new String[] { "Brings up a help message", "[COMMAND]", "give" });
      CMDS.put("helmet", new String[] { "Specifies the helmet the player wears", "[ITEM] [QTY] [DAMAGE]", "10 1 1" });
      CMDS.put("home", new String[] { "Teleport to spawn point", "", "" });
      CMDS.put("hunger", new String[] { "Sets the players hunger level to pre-defined figures", "[empty|full|infinite|enable|disable]", "infinite" });
      CMDS.put("i", new String[] { "Gives player item, if quantity isn't specified maximum amount of that item", "<ITEMCODE|ITEMNAME> [QUANTITY] [DAMAGE]", "1" });
      CMDS.put("ignite", new String[] { "Lights the block that the player is pointing at on fire", "", "" });
      CMDS.put("infiniteitems", new String[] { "Gives the player infinite items.", "", "" });
      CMDS.put("instantkill", new String[] { "Allows the player to kill enemies in one hit.", "", "" });
      CMDS.put("instantmine", new String[] { "Turns instant mining on/off", "", "" });
      CMDS.put("instantplant", new String[] { "Instantly plants saplings", "[grow]", "grow" });
      CMDS.put("invrotate", new String[] { "Rotates the inventory around line by line or item by item", "[[line|item] [left|right]]", "item" });
      CMDS.put("invstore", new String[] { "Allows you to load and save your current inventory", "<load NAME|save NAME|list>", "save mining" });
      CMDS.put("item", new String[] { "Gives player item, if quantity isn't specified maximum amount of that item", "<ITEMCODE|ITEMNAME> [QUANTITY] [DAMAGE]", "1" });
      CMDS.put("itemdamage", new String[] { "Toggles item damage on and off", "", "" });
      CMDS.put("itemname", new String[] { "Discover the itemname and ID of your currently selected item.", "", "" });
      CMDS.put("itemstack", new String[] { "Gives the player the specified number of itemstacks", "<ID> [QTY] [DAMAGE]", "1 10 0" });
      CMDS.put("jump", new String[] { "Moves you from where you are to where your mouse is pointing", "", "" });
      CMDS.put("keepitems", new String[] { "Allows the player to keep items on death.", "", "" });
      CMDS.put("kill2", new String[] { "Kills the current player", "", "" });
      CMDS.put("killall", new String[] { "Kills all of the specified mob type", "<MOBTYPE>", "" });
      CMDS.put("killnpc", new String[] { "Kills all living creatures around the player.", "[monster|animal|all]", "all" });
      CMDS.put("l", new String[] { "Lists all the waypoints currently configured.", "", "" });
      CMDS.put("light", new String[] { "Lights up the map.", "", "" });
      CMDS.put("listwaypoints", new String[] { "Lists all the waypoints currently configured.", "", "" });
      CMDS.put("longerlegs", new String[] { "Makes your legs longer so you can walk up 1 block high", "", "" });
      CMDS.put("macro", new String[] { "Loads and runs a file containing commands.", "<create <FILE>|edit <FILE>|list|folder|dir|delete <FILE>|FILENAME {ARGS}>", "miner arg1 arg2" });
      CMDS.put("maxstack", new String[] { "Changes the maximum stack size of the Item.", "[ITEMNAME|ITEMID] [QTY]", "stone 128" });
      CMDS.put("mobdamage", new String[] { "Toggles damage on and off", "", "" });
      CMDS.put("moveplayer", new String[] { "Moves the player in the specified direction.", "<DISTANCE> <DIRECTION>", "100 north" });
      CMDS.put("msg", new String[] { "This commands adds a message to the console.", "<MESSAGE>", "Hello world" });
      CMDS.put("music", new String[] { "Music configuration. Send a request to start music or set the volume.", "[play|pause|stop|skip|next|VOLUME]", "50" });
      CMDS.put("noclip", new String[] { "Allows the player to noclip", "", "" });
      CMDS.put("output", new String[] { "Toggles SPC output on/off", "", "" });
      CMDS.put("p", new String[] { "Gives current player position", "", "" });
      CMDS.put("phelp", new String[] { "Provides help for plugins", "[COMMAND]", "example" });
      CMDS.put("pick", new String[] { "Gets a maximum stack of the block which the player is pointing at", "[QUANTITY]", "64" });
      CMDS.put("platform", new String[] { "Puts a one block glass platform beneath the players position", "", "" });
      CMDS.put("plugin", new String[] { "Provides plugin information and useful utilities", "<list|enable [MODNAME]|disable [MODNAME]>", "disable WorldEdit" });
      CMDS.put("pos", new String[] { "Gives current player position", "", "" });
      CMDS.put("ralias", new String[] { "Removes the specified alias", "<ALIAS|all>", "example" });
      CMDS.put("reach", new String[] { "Sets the reach distance of the player.", "<DISTANCE|+|->", "10" });
      CMDS.put("rem", new String[] { "Removes the specified waypoint", "<NAME>", "example" });
      CMDS.put("removedrops", new String[] { "This command removes item drops from the world.", "[all]", "all" });
      CMDS.put("rename", new String[] { "Renames a specified command name to another name.", "<COMMAND> <NEWCOMMAND>", "goto warp" });
      CMDS.put("repair", new String[] { "Repairs the currently selected item, or all.", "[all]", "all" });
      CMDS.put("refill", new String[] { "Re-stocks your items in your inventory to the maximum ammount", "[all]", "all" });
      CMDS.put("repeat", new String[] {"Repeat the last command used","",""});
      // TODO
      // CMDS.put("repeat", new String[] { "Runs a command a repeated number of times",
      // "<TIME_SECONDS> <COUNT> <COMMAND> {COMMANDPARAMS}", "1 -1 killnpc all" });
      CMDS.put("reset", new String[] { "Resets the settings to default", "", "" });
      CMDS.put("resize", new String[] { "Resizes the Minecraft window the size you want it", "[1080p|720p|480p|setdefault [WIDTH HEIGHT]|<WIDTH HEIGHT>]", "800 600" });
      CMDS.put("reskin", new String[] { "Reskins the NPC which you are pointing at to the specified skin", "", "" });
      CMDS.put("return", new String[] { "Moves the player to the last position before teleport", "", "" });
      CMDS.put("ride", new String[] { "Rides the entity you are pointing at", "", "" });
      CMDS.put("s", new String[] { "Mark a waypoint on the world", "<NAME>", "example" });
      CMDS.put("sc", new String[] { "Runs the specified script", "<FILENAME>", "test.js" });
      CMDS.put("search", new String[] { "Allows you to search for items using a name", "<SEARCHTERM>", "pick" });
      CMDS.put("set", new String[] { "Mark a waypoint on the world", "<NAME>", "example" });
      CMDS.put("setjump", new String[] { "Sets the height that you jump", "<HEIGHT|reset>", "3" });
      CMDS.put("setspawn", new String[] { "Sets the players spawn at their current location, or the one specified.", "[<X> <Y> <Z>]", "0 66 0" });
      CMDS.put("setspeed", new String[] { "Sets the speed that the player moves", "<SPEED|reset>", "3" });
      CMDS.put("sign", new String[] { "Allows placing and editing of signs without a GUI", "<add|edit> [\"LINE1\"] [\"LINE2\"] [\"LINE3\"] [\"LINE4\"]", "add \"Hello\" \"World\" \"   This\" \"is a test\"" });
      //TODO: CMDS.put("size", new String[] { "Sets the player size", "<SIZE|reset>", "1" });
      CMDS.put("skin", new String[] { "Allows the user to change their skin to any valid player's skin.", "<PLAYERNAME|reset>", "trunksbomb" });
      CMDS.put("slippery", new String[] { "Makes the specified block slippery", "<BLOCK> [SLIPPERYNESS]", "grass 1.5" });
      CMDS.put("spawn", new String[] { "Spawns the specified creature.", "<CREATURENAME> [QTY]", "zombie 10" });
      CMDS.put("spawner", new String[] { "Changes the mob spawner the player is pointing at", "<TYPE>", "Creeper" });
      CMDS.put("spawncontrol", new String[] { "Allows you to configure a list of spawnable creatures", "<all|animals|monsters>"/*|[disable|enable <MOBNAME>]>"*/, "all" });
      CMDS.put("spawnportal", new String[] { "Spawns a portal nearby the player", "", "" });
      CMDS.put("spawnstack", new String[] { "Spawns the specified creature, multiple creatures will result in a stack.", "{CREATURENAME}", "spider creeper" });
      CMDS.put("sprinting", new String[] { "Turns sprinting on/off", "[enable|disable]", "enable" });
      CMDS.put("stackcombine", new String[] { "Combines all stacks in your inventory of the same type", "", "" });
      CMDS.put("stacklimit", new String[] { "Turn the itemstack limit on/off. Note: does not save stacksize above 128", "[on|off]", "off" });
      CMDS.put("startup", new String[] { "Sets the command to run on startup","<COMMAND> {ARGS}", "macro startup" });
      CMDS.put("superheat", new String[] { "Superheats all the specified item in your inventory", "[ITEMID|ITEMNAME|all]", "stone" });
      CMDS.put("superpunch", new String[] { "Hit that NPC with a punch like no other", "[DISTANCE|reset]", "20" });
      CMDS.put("t", new String[] { "Teleport to X Y Z coordinates.", "<X> <Y> <Z>", "0 66 0" });
      CMDS.put("tele", new String[] { "Teleport to X Y Z coordinates.", "<X> <Y> <Z>", "0 66 0" });
      CMDS.put("textcolor", new String[] { "Configure the text which SPC outputs", "<<normal|error> <0-f|random>>|setrandom VALIDCOLORS|reset", "normal 9" });
//       CMDS.put("time", new String[] { "Set and get the time within minecraft.", "[day|night|[set|get [minute|hour|day [TIME]]]|speed <SPEED>]", "set hour 16" });
      CMDS.put("time2", new String[] { "Set and get the time within minecraft.", "[day|night|[set|get [minute|hour|day [TIME]]]|speed <SPEED>]", "set hour 16" });
      CMDS.put("timeschedule", new String[] { "Sets a time schedule which minecraft time will follow", "<TIME1> <TIME2>", "0:00 12:00" });
      CMDS.put("unbind", new String[] { "Unbinds a key which has been bound to a command", "<KEYNAME|all>", "e" });
      CMDS.put("unbindid", new String[] { "Unbinds a command from a keyboard key using the key id", "<ID>", "22" });
      CMDS.put("unbinditem", new String[] { "Unbinds the currently selected items bindings, or all bindings", "[all]", "all" });
      CMDS.put("update", new String[] { "Configures the update warnings", "<enable|disable|check>", "enable" });
      CMDS.put("useportal", new String[] { "Instantly transfers you to the specified dimension", "<normal|nether|end|PORTAL>", "-1" });
      CMDS.put("waterdamage", new String[] { "Turns water damage on/off", "", "" });
      CMDS.put("watermovement", new String[] { "Turns water and lava slowdown and current effects off.", "", "" });
//       CMDS.put("weather", new String[] { "Commands to toggle various weather on/off", "[rain|lightning|thunder|sun|enable|disable]", "rain" });
      CMDS.put("weather2", new String[] { "Commands to toggle various weather on/off", "[rain|lightning|thunder|sun|enable|disable]", "rain" });
      CMDS.put("world", new String[] { "Various world related commands.", "<load FILENAME|save|new FILENAME [SEED]|seed [SEED]|exit|backup|name>", "load World10" });
//       CMDS.put("xp", new String[] { "XP (player experience) related commands", "<add QTY|get|set XP|level LEVEL>", "add 100" });
      CMDS.put("xp2", new String[] { "XP (player experience) related commands", "<add QTY|get|set XP|level LEVEL>", "add 100" });
      //CMDS.put("worldinfo", new String[] { "Allows direct editing of the WorldInfo file.", "<key> <value>", "seed 123" });

      //CMDS.put("zoom", new String[] { "Zooms in or out of the region the player is facing", "[ZOOM] [YAW] [PITCH]", "10 1 1" });
//       CMDS.put("test", new String[] { "Test command ", "", "" });
      //CMDS.put("test2", new String[] { "Test command ", "", "" });
      System.out.println("Commands: " + CMDS.size());
   }

   public PlayerHelper(Minecraft mc, EntityPlayerSPSPC ep) {
      this.mc = mc;
      this.ep = ep;
      PlayerHelper.PH = this;
      ismultiplayer = mc.isMultiplayerWorld();
      //setupLogging();
      initialise();
      createDirs();
      WAYPOINTS = new HashMap<String, double[]>();
      /*if (CORE == null) {
         CORE = new Vector<SPCPlugin>();
         COMMANDS = new HashMap<String,Object[]>();
         loadCore();
      }*/
      loadConfig();
      loadSettings();
      loadBindings();
      loadAlias();
      populateItemNames();
      if (ITEM_MAX_DAMAGE == null) {
         ITEM_MAX_DAMAGE = new int[Item.itemsList.length];
         for (int i = 0; i < ITEM_MAX_DAMAGE.length; i++) {
            ITEM_MAX_DAMAGE[i] = 1;
         }
      }
      setup();
      if (INV_BEFORE_DEATH == null) {
         INV_BEFORE_DEATH = new InventoryPlayer(null);
      }

      if (PLUGIN_MANAGER == null) {
         PLUGIN_MANAGER = new SPCPluginManager(this);
         PLUGIN_MANAGER.loadPlugins();
      }
      PLUGIN_MANAGER.callPluginMethods(PLUGIN_SETHELPER, this);
   }

   /**
    * Attempts to load the core SPC classes
    */
   /*public void loadCore() {
      //TODO: Create new core class loader which extends the plugin loader

      // Load core classes into Vector
      try { CORE.add(new SPCcInventory()); } catch (Exception e) { return; }

      // Load commands of core classes into hashmap
      Iterator<SPCPlugin> i = CORE.iterator();
      while (i.hasNext()) {
         Map<String,Object[]> cmds = SPCPluginManager.loadCommands(i.next());
         if (cmds != null) {
            COMMANDS.putAll(cmds);
         }
      }
   }*/

   /*public void setupLogging() {
      try {
         PrintStream out = new PrintStream(new FileOutputStream(new File(MODDIR,"log.txt")));
         System.setOut(out);
      } catch (Exception e) {
      }
   }*/

   /**
    * Adds the specified file to the classpath dynamically
    * 
    * @param file - The file to add to the classpath
    * @return True if the file was added, false otherwise.
    */
   public static boolean addToClasspath(File file) {
      if (!file.exists()) {
         return false;
      }
      URLClassLoader sysloader = (URLClassLoader) ClassLoader.getSystemClassLoader();
      Class<URLClassLoader> sysclass = URLClassLoader.class;
      try {
         Method method = sysclass.getDeclaredMethod("addURL", new Class[] { URL.class });
         method.setAccessible(true);
         method.invoke(sysloader, new Object[] { file.toURI().toURL() });
      } catch (Throwable t) {
         t.printStackTrace();
         return false;
      }
      return true;
   }

   /**
    * Gets the directory where the current world exists.
    * 
    * @return The path to the current directory
    */
   public File getWorldDir() {
      ISaveHandler sh = ((ISaveHandler) this.mc.theWorld.saveHandler);
      if (sh instanceof SaveHandler) {
         return ((SaveHandler) sh).getSaveDirectory();
      } else {
         return new File("");
      }
   }

   /**
    * Creates any necessary directory structure when SPC is started
    * 
    * @return True if the directories were successfully created, false otherwise
    */
   public boolean createDirs() {
      try {
         if (!MODDIR.exists()) {
            MODDIR.mkdirs();
         }
         File temp = new File(MODDIR, "macros/");
         if (!temp.exists()) {
            temp.mkdirs();
         }
         temp = new File(MODDIR, "saves/");
         if (!temp.exists()) {
            temp.mkdirs();
         }
      } catch (Exception e) {
         return false;
      }
      return true;
   }

   /**
    * Initialises the mod to default settings.
    */
   public void initialise() {
      noClip = false;
      flying = false;
      damage = true;
      waterdamage = true;
      ep.isImmuneToFire = false;
      falldamage = true;
      speed = 1.0D;
      gravity = 1.0D;
      timeschedule = null;
      reachdistance = 4F;
      opened = false;
      prevx = -1;
      prevy = -1;
      prevz = -1;
      instant = false;
      prevfalldamage = true;
      prevwaterdamage = true;
      prevdamage = true;
      prevfiredamage = true;
      prevflying = false;
      prevnoclip = false;
      dropitems = true; 
      mobsfrozen = false; 
      mobdamage = true; 
      keepitems = false; 
      instantkill = false; 
      watermovement = true; 
      sessionusername = "";
      keyboard = new Vector<Integer>();
      light = false;
      instantgrow = false;
      instantplant = false;
      startup = "";
      engine = "JavaScript";
      output = true;
      monsterspawn = true;
      animalspawn = true;
      prevspeed = 1.0D;
      prevgravity = 1.0D;
      superpunch = -1;
      ep.stepHeight = 0.5F;
      Block.blocksList[0] = null;
      toggledropgive = true;
      moveplayer = true;
      movecamera = true;
      mc.renderViewEntity = ep;
      freezecampitch = 0F;
      freezecamyaw = 0F;
      timespeed = 0;
      updateson = true;
      textcolornormal = 'f';
      textcolorerror = '4';
      textcolorrandom = VALID_COLOURS;
      //sfly = false;
      limitstack = false;
      flymode = "dynamic";
      ladderMode = false;
      sprinting = false;
      criticalHit = false;
      disableHunger = false;
      creeperExplosion = true;
      mouse = new Vector<Integer>();
      weather = true;
   }

   /**
    * Sets up any necessary configuration
    */
   public void setup() {
      if (ismultiplayer) {
         return;
      }
      toggleLight(light, false);
      setItemMaxDamage();

      if (keepitems && INV_BEFORE_DEATH != null) {
         for (int j = 0; j < INV_BEFORE_DEATH.mainInventory.length; j++) {
            ep.inventory.mainInventory[j] = INV_BEFORE_DEATH.mainInventory[j];
         }
         for (int j = 0; j < INV_BEFORE_DEATH.armorInventory.length; j++) {
            ep.inventory.armorInventory[j] = INV_BEFORE_DEATH.armorInventory[j];
         }
      }
      mc.overrideMobSpawning = !animalspawn || !monsterspawn;
      mc.theWorld.setAllowedSpawnTypes(monsterspawn, animalspawn);
      //mc.playerController = new SPCPlayerControllerSP(mc, this);
      // this.processCommand(startup == null ? "" : startup);
   }

   public void loadAlias() {
      loadAlias(MODDIR);
   }

   public void loadAlias(File f) {
      alias = new Settings(new File(f, "alias.properties"));
   }

   public void loadBindings() {
      loadBindings(MODDIR);
   }

   /**
    * Loads the key bindings from file
    * 
    * @param f - The directory to load the bindings from
    */
   public void loadBindings(File f) {
      bound = new Settings(new File(f, "bindings.properties"));
      itemsbound = new Settings(new File(f, "itembindings.properties"));
   }

   public void loadConfig() {
      loadConfig(MODDIR);
   }

   /**
    * Loads the command configuration file into memory. This file contains the command names mapped
    * to their new names. After adding them to memory they are then saved back to file.
    * 
    * @param f The configuration file to load and save to
    */
   public void loadConfig(File f) {
      commands = new HashMap<String, String>();
      File settings = new File(f, "sppcommands.properties");
      Settings p = new Settings(settings);

      Iterator<String> i = CMDS.keySet().iterator();
      Settings save = new Settings(settings);
      while (i.hasNext()) {
         String key = i.next();
         String newkey = p.getString(key, key);
         commands.put(newkey, key);
         save.set(key, newkey);
      }

      if (!save.save("Single Player Commands - Rename command names: COMMANDNAME=NEWCOMMANDNAME")) {
         sendDebug("ERROR: A problem occurred saving the command names to file.");
      }
   }

   public void populateItemNames() {
      // Check if previously loaded (expensive method to complete..)
      if (ITEMNAMES != null) {
         return;
      }

      // Load Item Names
      ITEMNAMES = new Vector<String>();
      for (int i = 0; i < Item.itemsList.length; i++) {
         if (Item.itemsList[i] == null) {
            ITEMNAMES.add(null);
         } else {
            ITEMNAMES.add(StringTranslate.getInstance().translateNamedKey((Item.itemsList[i].getItemName())).toString().trim().toLowerCase());
         }
      }

      // Load properties file and look for renames
      File config = new File(MODDIR, "itemnames.properties");
      Settings p = new Settings(config, false);
      if (config.exists() && p.load()) {
         Iterator<Object> i = p.keySet().iterator();
         while (i.hasNext()) {
            try {
               String key = (String) i.next();
               int id = Integer.parseInt(key);
               ITEMNAMES.setElementAt(p.getProperty(key), id);
            } catch (Exception e) {
            }
         }
      }

      // Save item ID-Name pairs back to file
      writeItemNamesToFile(config);
   }

   /**
    * Writes item name and ID pairs to the file specified
    * 
    * @param file the file to write the item names to
    */
   public void writeItemNamesToFile(File file) {
      Iterator<String> i = ITEMNAMES.iterator();
      Settings p = new Settings(file, false);
      int id = 0;
      String temp = null;
      while (i.hasNext()) {
         if ((temp = i.next()) != null) {
            p.set("" + id, temp);
         }
         id++;
      }

      if (!p.save("Single Player Commands - Rename item names: ITEMID=ITEMNAME")) {
         sendDebug("ERROR: Unable to write item names to file.");
      }
   }

   /**
    * Loads the game settings from file at the default location
    */
   public void loadSettings() {
      File dir = getWorldDir();
      if (!(new File(dir, "spc.settings")).exists()) {
         dir = MODDIR;
      }
      loadSettings(dir);
   }

   /**
    * Loads the game settings from file at the specified location
    * 
    * @param f - The directory to loads the game settings from
    */
   public void loadSettings(File f) {
      if (ismultiplayer) {
         initialise();
         return;
      }
      Settings s = new Settings(new File(f, "spc.settings"));
      instant = s.getBoolean("instant", false);
      prevx = s.getDouble("previousx", 0);
      prevy = s.getDouble("previousy", 0);
      prevz = s.getDouble("previousz", 0);
      gravity = s.getDouble("gravity", 1);
      speed = s.getDouble("speed", 1);
      falldamage = s.getBoolean("falldamage", true);
      waterdamage = s.getBoolean("waterdamage", true);
      ep.isImmuneToFire = s.getBoolean("firedamage", false);
      damage = s.getBoolean("damage", true);
      lastrift = s.getInteger("lastrift", -1);
      noClip = s.getBoolean("noclip", false);
      flying = s.getBoolean("fly", false);
      reachdistance = s.getFloat("reachdistance", 4);
      disablecommands = s.getBoolean("disablecommands", false);
      dropitems = s.getBoolean("harvestitems", true);
      mobsfrozen = s.getBoolean("mobsfrozen", false);
      mobdamage = s.getBoolean("mobdamage", true);
      norepair = s.getBoolean("norepair", false);
      infiniteitems = s.getBoolean("infiniteitems", false);
      keepitems = s.getBoolean("keepitems", false);
      instantkill = s.getBoolean("instantkill", false);
      instantgrow = s.getBoolean("instantgrow", false);
      instantplant = s.getBoolean("instantplant", false);
      watermovement = s.getBoolean("watermovement", true);
      startup = s.getString("startup", "");
      engine = s.getString("engine", "JavaScript");
      output = s.getBoolean("output", true);
      animalspawn = s.getBoolean("animalspawn", true);
      monsterspawn = s.getBoolean("monsterspawn", true);
      prevspeed = s.getDouble("prevspeed", 1.0D);
      prevgravity = s.getDouble("prevgravity", 1.0D);
      superpunch = s.getDouble("superpunch", -1);
      ep.stepHeight = s.getFloat("stepheight", ep.stepHeight);
      toggledropgive = s.getBoolean("toggledropgive", true);
      sizewidth = s.getInteger("sizewidth", mc.mcCanvas.getParent().getWidth());
      sizeheight = s.getInteger("sizeheight", mc.mcCanvas.getParent().getHeight());
      timespeed = s.getInteger("timespeed",0);
      updateson = s.getBoolean("updateson",true);
      textcolornormal = s.getCharacter("textcolornormal", 'f');
      textcolorerror = s.getCharacter("textcolorerror", '4');
      textcolorrandom = s.getString("textcolorrandom", VALID_COLOURS);
      //sfly = s.getBoolean("sfly", false);
      flymode = s.getString("flymode","dynamic");
      lastcommand = s.getString("lastcommand", "");
      limitstack = s.getBoolean("limitstack", false);
      ladderMode = s.getBoolean("laddermode", false);
      sprinting = s.getBoolean("sprinting", false);
      criticalHit = s.getBoolean("criticalhit",false);
      disableHunger = s.getBoolean("disablehunger", false);
      creeperExplosion = s.getBoolean("creeperexplosion", true);
      weather = s.getBoolean("weather", true);
      if (lastrift > -1) {
         timeschedule = new int[4];
         for (int i = 0; i < timeschedule.length; i++) {
            timeschedule[i] = s.getInteger("timeschedule" + i, 0);
         }
      }
   }

   /**
    * Saves the game settings to the default location
    */
   public void saveSettings() {
      saveSettings(getWorldDir());
   }

   /**
    * Saves the game settings to the specified location
    * 
    * @param f - The location to store the game settings
    */
   public void saveSettings(File f) {
      Settings s = new Settings(new File(f, "spc.settings"));
      s.set("instant", instant);
      s.set("instant", instant);
      s.set("previousx", prevx);
      s.set("previousy", prevy);
      s.set("previousz", prevz);
      s.set("gravity", gravity);
      s.set("speed", speed);
      s.set("falldamage", falldamage);
      s.set("waterdamage", waterdamage);
      s.set("firedamage", ep.isImmuneToFire);
      s.set("damage", damage);
      s.set("lastrift", lastrift);
      s.set("noclip", noClip);
      s.set("fly", flying);
      s.set("reachdistance", reachdistance);
      s.set("disablecommands", disablecommands);
      s.set("mobdamage", mobdamage);
      s.set("mobsfrozen", mobsfrozen);
      s.set("harvestitems", dropitems);
      s.set("norepair", norepair);
      s.set("infiniteitems", infiniteitems);
      s.set("keepitems", keepitems);
      s.set("instantkill", instantkill);
      s.set("instantplant", instantplant);
      s.set("instantgrow", instantgrow);
      s.set("watermovement", watermovement);
      s.set("startup", startup);
      s.set("engine", engine);
      s.set("output", output);
      s.set("animalspawn", animalspawn);
      s.set("monsterspawn", monsterspawn);
      s.set("prevspeed", prevspeed);
      s.set("prevgravity", prevgravity);
      s.set("superpunch", superpunch);
      s.set("stepheight", ep.stepHeight);
      s.set("toggledropgive", toggledropgive);
      s.set("sizeheight",sizeheight);
      s.set("sizewidth",sizewidth);
      s.set("timespeed", timespeed);
      s.set("updateson",updateson);
      s.set("textcolornormal",textcolornormal);
      s.set("textcolorerror",textcolorerror);
      s.set("textcolorrandom",textcolorrandom);
      //s.set("sfly",sfly);
      s.set("flymode",flymode);
      s.set("lastcommand",lastcommand);
      s.set("limitstack",limitstack);
      s.set("laddermode", ladderMode);
      s.set("sprinting",sprinting);
      s.set("criticalhit",criticalHit);
      s.set("disablehunger", disableHunger);
      s.set("creeperexplosion", creeperExplosion);
      s.set("weather", weather);
      if (timeschedule != null) {
         for (int i = 0; i < 4; i++) {
            s.set("timeschedule" + i, timeschedule[i]);
         }
      }
      s.save("Single Player Commands - Settings");
   }

   /**
    * Reads the saved waypoints from file into the game
    * 
    * @param f The file which the waypoints are file
    */
   public void readWaypointsFromNBT(File f) {
      File wp = new File(f, "waypoints.dat");
      if (!wp.exists()) {
         return;
      }
      WAYPOINTS.clear();
      NBTTagCompound nbttagcompound1;
      try {
         nbttagcompound1 = CompressedStreamTools.readCompressed(new FileInputStream(wp));
      } catch (Exception e) {
         return;
      }
      NBTTagList nbttaglist = nbttagcompound1.getTagList("waypoints");
      for (int i = 0; i < nbttaglist.tagCount(); i++) {
         NBTTagCompound nbttagcompound = (NBTTagCompound) nbttaglist.tagAt(i);
         String s = nbttagcompound.getString("Name");
         double d = nbttagcompound.getDouble("X");
         double d1 = nbttagcompound.getDouble("Y");
         double d2 = nbttagcompound.getDouble("Z");
         WAYPOINTS.put(s, new double[] { d, d1, d2 });
      }
   }

   /**
    * Writes the waypoints which are in memory to the specified file
    * 
    * @param f The file which is written to
    */
   public void writeWaypointsToNBT(File f) {
      if (WAYPOINTS.size() == 0) {
         return;
      }
      NBTTagCompound nbttagcompound;
      NBTTagList nbttaglist = new NBTTagList();
      for (Iterator<?> iterator = WAYPOINTS.keySet().iterator(); iterator.hasNext(); nbttaglist.appendTag(nbttagcompound)) {
         nbttagcompound = new NBTTagCompound();
         String s = (String) iterator.next();
         nbttagcompound.setString("Name", s);
         nbttagcompound.setDouble("X", ((double[]) WAYPOINTS.get(s))[0]);
         nbttagcompound.setDouble("Y", ((double[]) WAYPOINTS.get(s))[1]);
         nbttagcompound.setDouble("Z", ((double[]) WAYPOINTS.get(s))[2]);
      }
      NBTTagCompound nbttagcompound1 = new NBTTagCompound();
      nbttagcompound1.setTag("waypoints", nbttaglist);
      File wpnew = new File(f, "waypoints.dat_new");
      File wpold = new File(f, "waypoints.dat_old");
      File wp = new File(f, "waypoints.dat");
      try {
         CompressedStreamTools.writeCompressed(nbttagcompound1, new FileOutputStream(wpnew));
         if (wpold.exists()) {
            wpold.delete();
         }
         wp.renameTo(wpold);
         if (wp.exists()) {
            wp.delete();
         }
         wpnew.renameTo(wp);
         if (wpnew.exists()) {
            wpnew.delete();
         }
      } catch (Exception e) {
      }
   }

   /**
    * Gets the list of spawnable creature names
    * 
    * @return A map containing a Name - Class relationship of spawnable NPCs
    */
   public Map<String, Class<?>> getEntityList() {
      if (entitylist == null) {
         setEntityLists();
      }
      return entitylist;
   }

   /**
    * Gets the list of spawnable creature ids
    * 
    * @return A map containing a ID - Class relationship of spawnable NPCs
    */
   public Map<Integer, Class<?>> getEntityIdList() {
      if (entityidlist == null) {
         setEntityLists();
      }
      return entityidlist;
   }

   /**
    * ** Should only need to be called once **
    * 
    * Gets the private spawnlist in the EntityList class
    */
   private void setEntityLists() {
      try {
         Field field[] = EntityList.class.getDeclaredFields();
         for (int i = 0; i < field.length; i++) {
            if (entitylist != null && entityidlist != null) {
               break;
            }
            if (!field[i].isAccessible()) {
               field[i].setAccessible(true);
            } else {
               continue;
            }
            if (!Modifier.isStatic(field[i].getModifiers())) {
               continue;
            }

            Object o = field[i].get(null);
            if (!(o instanceof Map)) {
               continue;
            }

            Map<?, Class<?>> temp = (Map<?, Class<?>>) o;
            try {
               if (temp.keySet().iterator().next() instanceof String) {
                  entitylist = (HashMap<String, Class<?>>) temp;
               } else if (temp.keySet().iterator().next() instanceof Integer) {
                  entityidlist = (HashMap<Integer, Class<?>>) temp;
               }
            } catch (Exception e) {
               continue;
            }
         }
      } catch (Exception e) {
         entitylist = new HashMap<String, Class<?>>();
         entityidlist = new HashMap<Integer, Class<?>>();
         return;
      }
      spawnignore = new Vector<Class<?>>();
      for (Iterator<Class<?>> i = entitylist.values().iterator(); i.hasNext();) {
         Class<?> temp = i.next();
         if (!EntityLiving.class.isAssignableFrom(temp)) {
            spawnignore.add(temp);
         }
      }
      spawnignore.add(EntityLiving.class);
   }

   /**
    * Returns the String name of an Entity based on Entity id
    * 
    * @param i The Entity ID
    * @return The Entity name
    */
   public String getEntityName(Integer i) {
      try {
         Object o = getEntityIdList().get(i);
         if (!(o instanceof Class)) {
            return null;
         }
         Class<?> c = (Class<?>)o;
         if (c == null || spawnignore.contains(c)) {
            return null;
         }
         for (Iterator<String> it = getEntityList().keySet().iterator(); it.hasNext();) {
            String tmp = it.next();
            if (getEntityList().get(tmp) == c) {
               return tmp;
            }
         }
      } catch (Exception e) {
      }
      return null;
   }

   /**
    * Returns the first class associated with the String name (ignores case). Returns null if the
    * entity was not found
    * 
    * @param name The name of the entity
    * @return Class that the entity belongs to
    */
   public Class<?> getEntity(String name) {
      Class<?> creature = null;
      for (Iterator<String> t = getEntityList().keySet().iterator(); t.hasNext();) {
         String key = t.next();
         if (name.equalsIgnoreCase(key)) {
            creature = (Class<?>) getEntityList().get(key);
            break;
         }
      }
      return creature;
   }

   public Class<?> getEntity2(String name) {
      Class<?> creature = null;
      for (Iterator<String> t = getEntityList().keySet().iterator(); t.hasNext();) {
         String key = t.next();
         if (name.equals(key)) {
            creature = (Class<?>) getEntityList().get(key);
            break;
         }
      }
      return creature;
   }

   /**
    * Sets the current position of the player into the previous instance variables. This method is
    * used before a player teleports that way the return command will work correctly
    */
   public void setCurrentPosition() {
      prevx = ep.posX;
      prevy = ep.posY;
      prevz = ep.posZ;
   }

   /*
    * Discovers whether the specified command is an inbuilt command or not
    * 
    * @param command - The command which is to be checked
    * 
    * @return true is returned when the command is in-built into SPC
    */
   /*
    * public boolean isInbuiltCommand(String command) { if (commands.get(command.toLowerCase()) !=
    * null) { return true; } else if (command.startsWith(".")) { return true; } else { return false;
    * } }
    */

   /**
    * This function is the master command processor, works out which function to call and handles
    * any unexpected exception.
    * 
    * @param s The user input String
    */
   public boolean processCommand(String str) {
      if (ismultiplayer || str == null || str.equalsIgnoreCase("")) {
         return false;
      }
      if (disablecommands) {
         sendMessage("Nice try, but you disabled your commands in this world.");
         return false;
      }
      try {
         boolean slash = false;
         boolean tempoutput = false;
         boolean prevoutput = output;
         String s = convertInput(str);
         sendDebug(s);
         if (s.startsWith("@")) {
            s = s.substring(1);
            tempoutput = true;
            output = false;
         }
         if (s.startsWith("/")) {
            s = s.substring(1);
            slash = true;
         }

         boolean run = false;
         String split[] = s.trim().split(" ");
         split[0] = commands.get(split[0].toLowerCase());
         if (split[0] == null || !split[0].equalsIgnoreCase("repeat")) {
            lastcommand = str;
         }
         if (split[0] != null) {
            processCommands(s);
            run = true;
         }

         split = s.trim().split(" ");
         //run = PLUGIN_MANAGER.handleCommand(split,COMMANDS) ? true : run;

         if (slash) {
            split[0] = "/" + split[0];
         }

         run = PLUGIN_MANAGER.callPluginMethods(PLUGIN_HANDLECOMMAND, (Object) split) ? true : run;
         if (!run){
            run = PLUGIN_MANAGER.handleCommand(split);
         }

         if (!run) {
            sendError("Command not found - " + split[0]);
            return false;
         }
         if (tempoutput) {
            output = prevoutput;
            saveSettings();
         }
      } catch (Throwable t) {
         sendError("UNHANDLED COMMANDS EXCEPTION - " + t.getMessage());
         t.printStackTrace();
         PrintWriter p = null;
         try {
            File f = new File(MODDIR, "spcexception-" + System.currentTimeMillis() + ".log");
            p = new PrintWriter(new FileOutputStream(f));
            t.printStackTrace(p);
            p.println();
            p.println("Command = " + str);
            p.println("Version = " + EntityPlayerSPSPC.SPCVERSION.getVersion());
            p.println("Time = " + System.currentTimeMillis());
            String plugins = "";
            Iterator<SPCPlugin> pg = PLUGIN_MANAGER.getPlugins().iterator();
            while (pg.hasNext()) {
               SPCPlugin temp = pg.next();
               plugins += temp.getName() + " | " + temp.getVersion() + "\n";
            }
            p.println("---Plugins---\n" + plugins);
            sendError("Error log written at: " + f.getAbsolutePath());
         } catch (Exception ex) {
            sendError("Could not write error log.");
         } finally {
            if (p != null) {
               p.close();
            }
         }
         return false;
      }
      return true;
   }

   /**
    * Compares two itemstacks to check for the same type
    * @param i - Itemstack 1
    * @param j - Itemstack 2
    * @return True if they are of the exact same type, false otherwise
    */
   public boolean isItemEqual(ItemStack i, ItemStack j) {
      if (i == null || j == null || i.getItem() == null || j.getItem() == null) {
         return false;
      }

      try {
         if (i.getItem().getItemName().equals(j.getItem().getItemName())) {
            return i.isItemEqual(j);
         }
      } catch (Exception e) {}
      return false;
   }

   /**
    * Adds an itemstack to the players inventory
    * @param i - The Itemstack to add to the inventory
    * @return true is returned when the itemstack was successfully added to the inventory
    */
   public boolean addItemStackToInv(ItemStack i) {
      if (i == null) {
         return true;
      }
      if (limitstack) {
         return ep.inventory.addItemStackToInventory(i);
      }
      int last = -1;
      for (int j = 0; j < ep.inventory.mainInventory.length; j++) {
         ItemStack slot = ep.inventory.mainInventory[j];
         if (slot != null) {
            if (isItemEqual(slot,i)) {
               slot.stackSize += i.stackSize;
               return true;
            }
         } else {
            if (last == -1) {
               last = j;
            }
         }
      }
      if (last != -1) {
         ep.inventory.mainInventory[last] = i;
         return true;
      }
      return false;
   }

   /**
    * This command is called when an item needs to be given to the player
    * 
    * @param i the itemstack to give to the player
    */
   public void givePlayerItem(ItemStack i) {
      givePlayerItem(i,toggledropgive);
   }

   /**
    * This command is called when an item needs to be given to the player
    * 
    * @param i the itemstack to give to the player
    */
   public void givePlayerItem(ItemStack i, boolean dropgive) {
      if (i == null) return;
      if (dropgive && addItemStackToInv(i)) {
         return;
      }
      EntityItem e = new EntityItem(mc.theWorld, ep.posX, (ep.posY - 0.30000001192092896D) + (double) ep.getEyeHeight(), ep.posZ, i);
      float f1 = 0.3F;
      e.motionX = -MathHelper.sin((ep.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((ep.rotationPitch / 180F) * 3.141593F) * f1;
      e.motionZ = MathHelper.cos((ep.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((ep.rotationPitch / 180F) * 3.141593F) * f1;
      e.motionY = -MathHelper.sin((ep.rotationPitch / 180F) * 3.141593F) * f1 + 0.1F;
      f1 = 0.02F;
      Random random = new Random();
      float f3 = random.nextFloat() * 3.141593F * 2.0F;
      f1 *= random.nextFloat();
      e.motionX += Math.cos(f3) * (double) f1;
      e.motionY += (random.nextFloat() - random.nextFloat()) * 0.1F;
      e.motionZ += Math.sin(f3) * (double) f1;
      if (!dropitems) {
         e.age = -6000;
      }
      e.delayBeforeCanPickup = 40;
      mc.theWorld.spawnEntityInWorld(e);
   }

   public void givePlayerItemNaturally(ItemStack i) {
      /*if (!dropitems) {
         return;
      }*/
      givePlayerItem(i,false);
   }

   /**
    * This function processes the commands from the chat console. It is called from the sp player
    * class.
    * 
    * @param s the user input to process.
    * @throws Exception if an unexpected, unhandled exception occurs
    */
   public void processCommands(String s) throws Exception {
      String split[] = s.trim().split(" ");
      split[0] = commands.get(split[0].toLowerCase());
      if (split[0] == null) {
         sendError("Command does not exist.");
         return;
      }

      /*
       * Item command - gives a player the specified item and quantity
       */
      if (split[0].equalsIgnoreCase("item") || split[0].equalsIgnoreCase("i")/* || split[0].equalsIgnoreCase("give")*/) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         String args[] = new String[split.length - 1];
         for (int i = 0; i < args.length; i++) {
            args[i] = split[i + 1];
         }
         ItemStack is = getItemStack(args);
         if (is != null) {
            givePlayerItem(is);
         } else {
            sendError("Could not find specified item.");
         }

         /*
          * Teleport command - teleports the player to the specified coordinates
          */
      } else if (split[0].equalsIgnoreCase("tele") || split[0].equalsIgnoreCase("t")) {
         if (split.length < 4) {
            sendError(ERRMSG_PARAM);
            return;
         }
         try {
            double x = Double.parseDouble(split[1]);
            double y = Double.parseDouble(split[2]);
            double z = Double.parseDouble(split[3]);
            setCurrentPosition();
            ep.setPosition(x, y, z);
         } catch (Exception e) {
            sendError(ERRMSG_PARSE);
         }

         /*
          * Position command - outputs the position of the player
          */
      } else if (split[0].equalsIgnoreCase("pos") || split[0].equalsIgnoreCase("p")) {
         sendMessage("Current Position: " + positionAsString());

         /*
          * Setspawn command - sets the spawn point of the player
          */
      } else if (split[0].equalsIgnoreCase("setspawn")) {
         int x = mc.theWorld.worldInfo.getSpawnX();
         int y = mc.theWorld.worldInfo.getSpawnY();
         int z = mc.theWorld.worldInfo.getSpawnZ();
         if (split.length == 1) {
            x = (int) ep.posX;
            y = (int) ep.posY;
            z = (int) ep.posZ;
         } else if (split.length == 4) {
            try {
               x = (int) Double.parseDouble(split[1]);
               y = (int) Double.parseDouble(split[2]);
               z = (int) Double.parseDouble(split[3]);
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
         } else {
            sendError(ERRMSG_PARAM);
            return;
         }
         mc.theWorld.worldInfo.setSpawnPosition(x, y, z);

         sendMessage("Spawn set at (" + x + "," + y + "," + z + ")");

         /*
          * Set command - sets a waypoint on the map
          */
      } else if (split[0].equalsIgnoreCase("set") || split[0].equalsIgnoreCase("s")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         String a = s.substring(4).trim();
         WAYPOINTS.put(a, new double[] { ep.posX, ep.posY, ep.posZ });
         writeWaypointsToNBT(getWorldDir());
         sendMessage("Waypoint \"" + a + "\" set at: " + positionAsString());

         /*
          * Goto command - Teleports the player to the specified waypoint
          */
      } else if (split[0].equalsIgnoreCase("goto")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         String a = s.substring(5).trim();
         if (WAYPOINTS.containsKey(a)) {
            double pos[] = WAYPOINTS.get(a);
            String from = positionAsString();
            setCurrentPosition();
            ep.setPosition(pos[0], pos[1], pos[2]);
            sendMessage("Moved from: " + from + " to: " + positionAsString());
         } else {
            sendError("Could not find specified waypoint.");
         }

         /*
          * Rem command - removes a waypoint from the save
          */
      } else if (split[0].equalsIgnoreCase("rem")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         String a = s.substring(4).trim();
         if (WAYPOINTS.containsKey(a)) {
            WAYPOINTS.remove(a);
            writeWaypointsToNBT(getWorldDir());
            sendMessage("Waypoint \"" + a + "\" removed.");
         } else {
            sendError("Could not find specified waypoint.");
         }

         /*
          * Home command - teleports the player to spawn
          */
      } else if (split[0].equalsIgnoreCase("home")) {
         setCurrentPosition();
         ep.setLocationAndAngles((double) mc.theWorld.worldInfo.getSpawnX() + 0.5D, (double) mc.theWorld.worldInfo.getSpawnY() + 1.0D, (double) mc.theWorld.worldInfo.getSpawnZ() + 0.5D, 0.0F, 0.0F);
         ep.preparePlayerToSpawn();

         /*
          * Kill command - kills the player
          */
      } else if (split[0].equalsIgnoreCase("kill2")) {
         ep.attackEntityFrom(DamageSource.causePlayerDamage(ep), Integer.MAX_VALUE);

         /*
          * List waypoints - lists all the waypoints on the current map
          */
      } else if (split[0].equalsIgnoreCase("listwaypoints") || split[0].equalsIgnoreCase("l")) {
         int count = WAYPOINTS.size();
         if (count == 0) {
            sendMessage("No waypoints found.");
            return;
         }
         Iterator<String> keys = WAYPOINTS.keySet().iterator();
         String lines = "";
         while (keys.hasNext()) {
            lines += keys.next() + ", ";
         }
         sendMessage("Waypoints (" + count + "): ");
         sendMessage(lines);

         /*
          * Clear command - clears the console of all messages
          */
      } else if (split[0].equalsIgnoreCase(/*"clear"*/"clearchat")) {
         mc.ingameGUI.getChatGUI().func_73761_a();

         /*
          * Time command - allows setting and getting of the minecraft time
          */
      } else if (split[0].equalsIgnoreCase(/*"time"*/"time2")) {
         if ((s.trim().length() == 4) || (s.trim().length() == 8 && s.trim().startsWith("time get")))
            printCurrentTime();
         if (split.length > 2) {
            int type = -1;
            if (split[1].equalsIgnoreCase("speed")) {
               int speed = 0;
               try {
                  speed = Integer.parseInt(split[2]);
                  timespeed = speed - 1;
                  sendMessage("The speed of time set to " + split[2] + "x normal.");
               } catch (Exception e) {
                  if (split[2].equalsIgnoreCase("reset")) {
                     timespeed = 0;
                     sendMessage("The speed of time was reset to default.");
                  }
               }
               saveSettings();
               return;
            }
            if (split[2].equalsIgnoreCase("day")) {
               type = 2;
            } else if (split[2].equalsIgnoreCase("hour")) {
               type = 1;
            } else if (split[2].equalsIgnoreCase("minute")) {
               type = 0;
            }
            if (type == -1) {
               sendError("Invalid time command: " + split[2]);
               return;
            }

            int d = getTime()[2];
            int m = getTime()[0];
            int h = getTime()[1];
            if (split[1].equalsIgnoreCase("get")) {
               sendMessage(split[2].toUpperCase() + ": " + getTime()[type]);
            } else if (split[1].equalsIgnoreCase("set") && split.length > 3) {
               int val = -1;
               try {
                  val = Integer.parseInt(split[3]);
               } catch (Exception e) {
                  sendError(ERRMSG_PARSE);
                  return;
               }
               if (val < 0) {
                  return;
               }
               if (type == 0) {
                  val = (int) (((val % 60) / 60.0) * 1000);
                  ((WorldSSP)mc.theWorld).commandSetTime((d * 24000) + (h * 1000) + val, true);
               } else if (type == 1) {
                  val = (val % 24) * 1000;
                  ((WorldSSP)mc.theWorld).commandSetTime((long) ((d * 24000) + val + ((m / 60.0) * 1000)), true);
               } else if (type == 2) {
                  val = val * 24000;
                  ((WorldSSP)mc.theWorld).commandSetTime((long) (val + (h * 1000) + ((m / 60.0) * 1000)), true);
               } else {
                  sendError("Invalid time command: " + split[2]);
                  return;
               }
               printCurrentTime();
            }
         } else if (split.length == 2) {
            int d = getTime()[2];
            if (split[1].equalsIgnoreCase("day")) {
               ((WorldSSP)mc.theWorld).commandSetTime((long) ((d + 1) * 24000), true);
               printCurrentTime();
            } else if (split[1].equalsIgnoreCase("night")) {
               ((WorldSSP)mc.theWorld).commandSetTime((long) ((d * 24000) + 13000), true);
               printCurrentTime();
            }
         }

         /*
          * Health command - allows you to set your health to predefined values
          */
      } else if (split[0].equalsIgnoreCase("health")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("max")) {
            ep.health = 20;
         } else if (split[1].equalsIgnoreCase("min")) {
            ep.health = 1;
         } else if (split[1].equalsIgnoreCase("infinite") || split[1].equalsIgnoreCase("inf")) {
            ep.health = Short.MAX_VALUE;
         } else {
            sendError("Invalid health command: " + split[1]);
            return;
         }
         sendMessage("Health set at " + split[1] + " (" + ep.health + ")");

         /*
          * Heal command - allows you to heal your player the specified number of half hearts.
          */
      } else if (split[0].equalsIgnoreCase("heal")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         try {
            ep.heal(Integer.parseInt(split[1]));
         } catch (Exception e) {
            sendError(ERRMSG_PARSE);
            return;
         }
         sendMessage("Player healed");

         /*
          * Spawnstack command - allows monsters to be spawned in a stack
          */
      } else if (split[0].equalsIgnoreCase("spawnstack")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("list")) {
            String npcs = "";
            for (Iterator<Integer> i = getEntityIdList().keySet().iterator(); i.hasNext();) {
               Integer val = i.next();
               String tmp = getEntityName(val);
               if (tmp == null) {
                  continue;
               }

               npcs += tmp + " (" + val + "), ";
            }
            sendMessage(npcs);
            return;
         }
         EntityLiving es = null;
         EntityLiving et = null;
         int i = 1;
         String error = "";
         do {
            try {
               Class<?> creature = null;
               if (split[i].equalsIgnoreCase("random") || split[i].equalsIgnoreCase("r")) {
                  Object n[] = getEntityIdList().values().toArray();
                  creature = (Class<?>) n[(new Random()).nextInt(n.length)];
               } else {
                  try {
                     Integer j = new Integer(split[i]);
                     creature = (Class<?>) getEntityIdList().get(j);
                  } catch (Exception e) {
                     creature = getEntity(split[i]);
                  }
               }
               if (creature == null || spawnignore.contains(creature)) {
                  if (creature == null) {
                     error += split[i] + ", ";
                  }
                  continue;
               }
               es = (EntityLiving) creature.getConstructor(new Class[] { World.class }).newInstance(new Object[] { mc.theWorld });
            } catch (Exception e) {
               continue;
            }
            MovingObjectPosition m = ep.rayTrace(reachdistance, 1.0F);
            if (m != null) {
               es.setLocationAndAngles(m.blockX, m.blockY + 1, m.blockZ, -ep.rotationYaw, 0F);
            } else {
               es.setLocationAndAngles(ep.posX + 3, ep.posY, ep.posZ + 3, ep.rotationYaw, 0F);
            }
            mc.theWorld.spawnEntityInWorld(es);
            if (et != null) {
               es.mountEntity(et);
            }
            et = es;
         } while (i++ < split.length - 1);
         if (!error.equalsIgnoreCase("")) {
            sendError("Could not find: " + error);
         }

         /*
          * Spawn - Spawns the specified creature the specified number of times
          */
      } else if (split[0].equalsIgnoreCase("spawn")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("list")) {
            String npcs = "";
            for (Iterator<Integer> i = getEntityIdList().keySet().iterator(); i.hasNext();) {
               Integer val = i.next();
               String tmp = getEntityName(val);
               if (tmp == null) {
                  continue;
               }

               npcs += tmp + " (" + val + "), ";
            }
            sendMessage(npcs);
            return;
         }

         Class<?> creature = null;

         int qty = 1;
         if (split.length > 2) {
            try {
               qty = Integer.parseInt(split[2]);
            } catch (Exception e) {
               qty = 1;
            }
         }

         try {
            Integer j = new Integer(split[1]);
            creature = (Class<?>) getEntityIdList().get(j);
         } catch (Exception e) {
            creature = getEntity(split[1]);
         }

         if (spawnignore.contains(creature) || creature == null && !(split[1].equalsIgnoreCase("random") || split[1].equalsIgnoreCase("r"))) {
            sendError(ERRMSG_NPCNOTFOUND);
            return;
         }

         try {
            Random r = new Random();

            for (int i = 0; i < qty; i++) {
               if (split[1].equalsIgnoreCase("random") || split[1].equalsIgnoreCase("r")) {
                  Object n[] = getEntityIdList().values().toArray();
                  int rand = (new Random()).nextInt(n.length);
                  creature = (Class<?>) n[rand];
               }
               if (creature == null || spawnignore.contains(creature)) {
                  i--;
                  continue;
               }
               EntityLiving es = (EntityLiving) creature.getConstructor(new Class[] { World.class }).newInstance(new Object[] { mc.theWorld });
               MovingObjectPosition m = ep.rayTrace(reachdistance, 1.0F);
               if (m != null) {
                  es.setLocationAndAngles(m.blockX, m.blockY + 1, m.blockZ, -ep.rotationYaw, 0F);
               } else {
                  es.setLocationAndAngles(ep.posX + r.nextInt(5), ep.posY, ep.posZ + r.nextInt(5), ep.rotationYaw, 0F);
               }
               mc.theWorld.spawnEntityInWorld(es);
            }
         } catch (Exception e) {
            return;
         }

         /*
          * Music command - Allows you to set the music volume and request a song to play
          */
      } else if (split[0].equalsIgnoreCase("music")) {
         if (split.length < 2) {
            playRandomMusic();
         } else if (split[1].equalsIgnoreCase("next") || split[1].equalsIgnoreCase("skip")) {
            SoundSystem sound = getSoundSystem();
            if (sound != null) {
               if (sound.playing("BgMusic")) {
                  sound.stop("BgMusic");
               }
               if (sound.playing("streaming")) {
                  sound.stop("streaming");
               }
            }
            playRandomMusic();
         } else if (split[1].equalsIgnoreCase("pause")) {
            SoundSystem sound = getSoundSystem();
            if (sound != null) {
               if (sound.playing("BgMusic")) {
                  sound.pause("BgMusic");
               }
               if (sound.playing("streaming")) {
                  sound.pause("streaming");
               }
            }
         } else if (split[1].equalsIgnoreCase("play")) {
            SoundSystem sound = getSoundSystem();
            if (sound != null) {
               if (!sound.playing("BgMusic")) {
                  sound.play("BgMusic");
               }
               if (!sound.playing("BgMusic")) {
                  playRandomMusic();
               }
            } else {
               playRandomMusic();
            }
         } else if (split[1].equalsIgnoreCase("stop")) {
            SoundSystem sound = getSoundSystem();
            if (sound != null) {
               if (sound.playing("BgMusic")) {
                  sound.stop("BgMusic");
               }
               if (sound.playing("streaming")) {
                  sound.stop("streaming");
               }
            }
         } else {
            try {
               int volume = Integer.parseInt(split[1]);
               if (volume < 0) {
                  volume = 0;
               } else if (volume > 100) {
                  volume = 100;
               }
               mc.gameSettings.musicVolume = volume / 100.0F;
               mc.sndManager.onSoundOptionsChanged();
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
         }

         /*
          * Difficulty command - allows the game difficulty to be set
          */
      } else if (/*split[0].equalsIgnoreCase("difficulty") || */split[0].equalsIgnoreCase("diff")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         try {
            int diff = Integer.parseInt(split[1]);
            if (diff < 0) {
               diff = 0;
            } else if (diff > 3) {
               diff = 3;
            }
            mc.gameSettings.difficulty = diff;
         } catch (Exception e) {
            sendError(ERRMSG_PARSE);
            return;
         }

         /*
          * Kill NPC command - allows you to kill npcs
          */
      } else if (split[0].equalsIgnoreCase("killnpc")) {
         int distance = 16;
         int type = 0;
         String message = "Nearby NPCs are now dead.";
         if (split.length > 1) {
            if (split[1].equalsIgnoreCase("all")) {
               distance = 1024;
               message = "All NPCs are now dead.";
            } else if (split[1].equalsIgnoreCase("animal")) {
               type = 1;
               distance = 1024;
               message = "All animals are now dead.";
            } else if (split[1].equalsIgnoreCase("monster")) {
               type = 2;
               distance = 1024;
               message = "All monsters are now dead.";
            }
         }
         List<Entity> npcs = mc.theWorld.getEntitiesWithinAABBExcludingEntity(ep, AxisAlignedBB.getBoundingBox(ep.posX - distance, ep.posY - distance, ep.posZ - distance, ep.posX + distance, ep.posY + distance, ep.posZ + distance));
         for (int i = 0; i < npcs.size(); i++) {
            Entity e = npcs.get(i);
            if (type == 0 && e instanceof EntityLiving) {
               ((EntityLiving) e).damageEntity(DamageSource.causePlayerDamage(ep), Integer.MAX_VALUE);
            } else if (type == 1 && e instanceof EntityAnimal) {
               ((EntityLiving) e).damageEntity(DamageSource.causePlayerDamage(ep),Integer.MAX_VALUE);
            } else if (type == 2 && e instanceof IMob) {
               ((EntityLiving) e).damageEntity(DamageSource.causePlayerDamage(ep),Integer.MAX_VALUE);
            }
         }
         sendMessage(message);

         /*
          * Ascend/Descend command - puts the player on the platform above
          */
      } else if (split[0].equalsIgnoreCase("ascend") || split[0].equalsIgnoreCase("descend")) {
         boolean flag = false;
         boolean up = true;
         if (split[0].equalsIgnoreCase("descend")) {
            up = false;
         }
         double x = ep.posX;
         double y = ep.posY;
         double z = ep.posZ;
         setCurrentPosition();
         if (up && ep.posY < 0) {
            ep.posY = 0;
         } else if (!up && ep.posY > 260) {
            ep.posY = 130;
         }
         do {
            if (ep.posY < 0 || ep.posY > 260) {
               ep.setPosition(x, y, z);
               break;
            }
            ep.setPosition(ep.posX, ep.posY, ep.posZ);
            if (mc.theWorld.getCollidingBoundingBoxes(ep, ep.boundingBox).size() == 0) {
               if (up && flag) {
                  break;
               } else if (!up && flag) {
                  ep.setPosition(ep.posX, --ep.posY, ep.posZ);
                  if (mc.theWorld.getCollidingBoundingBoxes(ep, ep.boundingBox).size() != 0) {
                     ep.setPosition(ep.posX, ++ep.posY, ep.posZ);
                     break;
                  }
                  ep.posY++;
               }
            } else {
               flag = true;
            }
            if (up) {
               ep.posY++;
            } else {
               ep.posY--;
            }
         } while (true);
         ep.motionX = ep.motionY = ep.motionZ = 0.0D;
         ep.rotationPitch = 0.0F;

         /*
          * Repair command - repairs the players items
          */
      } else if (split[0].equalsIgnoreCase("repair")) {
         if (split.length > 1 && split[1].equalsIgnoreCase("all")) {
            for (int i = 0; i < ep.inventory.mainInventory.length; i++) {
               ItemStack tmp = ep.inventory.mainInventory[i];
               if (tmp == null) {
                  continue;
               }
               tmp.damageItem(-tmp.getItemDamage(), null);
               // ep.inventory.mainInventory[i].damageItem(-ep.inventory.mainInventory[i].getItemDamageForDisplay());
            }
            for (int i = 0; i < ep.inventory.armorInventory.length; i++) {
               ItemStack tmp = ep.inventory.armorInventory[i];
               if (tmp == null) {
                  continue;
               }
               tmp.damageItem(-tmp.getItemDamage(), null);
               // ep.inventory.armorInventory[i].damageItem(-ep.inventory.armorInventory[i].getItemDamageForDisplay());
            }
            return;
         }
         ItemStack tmp = ep.inventory.getCurrentItem();
         if (tmp != null) {
            tmp.damageItem(-tmp.getItemDamage(), null);
            // ep.inventory.getCurrentItem().damageItem(-ep.inventory.getCurrentItem().getItemDamageForDisplay());
         }

         /*
          * Duplicate command - duplicates the players items
          */
      } else if (split[0].equalsIgnoreCase("duplicate") || split[0].equalsIgnoreCase("dupe")) {
         if (split.length > 1 && split[1].equalsIgnoreCase("all")) {
            for (int i = 0; i < ep.inventory.mainInventory.length; i++) {
               if (ep.inventory.mainInventory[i] == null) {
                  continue;
               }
               givePlayerItem(ep.inventory.mainInventory[i].copy());
            }
            for (int i = 0; i < ep.inventory.armorInventory.length; i++) {
               if (ep.inventory.armorInventory[i] == null) {
                  continue;
               }
               givePlayerItem(ep.inventory.armorInventory[i].copy());
            }
            return;
         }
         if (ep.inventory.getCurrentItem() != null) {
            if (split.length > 1) {
               try {
                  int qty = Integer.parseInt(split[1]);
                  qty = qty > 256 ? 256 : (qty < 0 ? 0 : qty);
                  for (int i = 0; i < qty; i++) {
                     givePlayerItem(ep.inventory.getCurrentItem().copy());
                  }
               } catch (Exception e) {
                  sendError(ERRMSG_PARSE);
               }
               return;
            }
            givePlayerItem(ep.inventory.getCurrentItem().copy());
         }

         /*
          * Destroy command - destroys a players inventory
          */
      } else if (split[0].equalsIgnoreCase("destroy")) {
         if (split.length > 1 && split[1].equalsIgnoreCase("all")) {
            destroyInventory();
            return;
         }
         ep.inventory.mainInventory[ep.inventory.currentItem] = null;
         
         /*
          * Itemstack command
          */
      } else if (split[0].equalsIgnoreCase("itemstack")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         String args[] = new String[split.length - 1];
         for (int i = 0; i < args.length; i++) {
            args[i] = split[i + 1];
         }
         ItemStack is = getItemStack(args);
         if (is != null) {
            int qty = is.stackSize;
            is.stackSize = is.getMaxStackSize();
            for (int i = 0; i < qty; i++) {
               givePlayerItem(new ItemStack(is.getItem(), is.getMaxStackSize(), is.getItemDamageForDisplay()));
            }
         } else {
            sendError("Could not find specified item.");
         }

         /*
          * Defuse command - defuses all primed TNT
          */
      } else if (split[0].equalsIgnoreCase("defuse")) {
         int distance = 16;
         if (split.length > 1 && split[1].equalsIgnoreCase("all")) {
            distance = 1024;
         }
         List<Entity> tnt = mc.theWorld.getEntitiesWithinAABBExcludingEntity(ep, AxisAlignedBB.getBoundingBox(ep.posX - distance, ep.posY - distance, ep.posZ - distance, ep.posX + distance, ep.posY + distance, ep.posZ + distance));
         for (int i = 0; i < tnt.size(); i++) {
            Entity e = tnt.get(i);
            if (e instanceof EntityTNTPrimed) {
               EntityItem entityitem = new EntityItem(mc.theWorld, e.posX, e.posY, e.posZ, new ItemStack(Item.itemsList[46], 1));
               mc.theWorld.setEntityDead(e);
               mc.theWorld.spawnEntityInWorld(entityitem);
            }
         }

         /*
          * Jump command - moves the player to where the mouse is pointing
          */
      } else if (split[0].equalsIgnoreCase("jump")) {
         try {
            MovingObjectPosition mop = ep.rayTrace(1024D, 1F);
            int xcoord = MathHelper.floor_double(mop.hitVec.xCoord);
            int ycoord = MathHelper.floor_double(mop.hitVec.yCoord);
            int zcoord = MathHelper.floor_double(mop.hitVec.zCoord);
            int b1 = mc.theWorld.getBlockId(xcoord, ycoord, zcoord);
            int b2 = mc.theWorld.getBlockId(xcoord, ycoord + 1, zcoord);

            if (mop.hitVec.xCoord % xcoord == 0 && ep.posX > mop.hitVec.xCoord)
               xcoord--;
            else if (mop.hitVec.zCoord % zcoord == 0 && ep.posZ > mop.hitVec.zCoord)
               zcoord--;

            while (ycoord < 256) {
               if (!mc.theWorld.isAirBlock(xcoord, ycoord, zcoord) || !mc.theWorld.isAirBlock(xcoord, ycoord + 1, zcoord))
                  ycoord++;
               else
                  break;
            }
            if (mc.theWorld.isAirBlock(MathHelper.floor_double(ep.posX), MathHelper.floor_double(ep.posY) - 1, MathHelper.floor_double(ep.posZ))) {
               while (ycoord > 0) {
                  if (mc.theWorld.isAirBlock(xcoord, ycoord - 1, zcoord))
                     ycoord--;
                  else
                     break;
               }
            }
            double offsetX = 0.5D;
            double offsetZ = 0.5D;

            ep.setPosition(xcoord + offsetX, ycoord + ep.height, zcoord + offsetZ);
         } catch (Exception e) {
            sendError("Unknown problem");
            return;
         }

         /*
          * Return command - returns the player to their previous position before teleporting
          */
      } else if (split[0].equalsIgnoreCase("return")) {
         if (prevy <= 0) {
            return;
         }
         double i = prevx;
         double j = prevy;
         double k = prevz;
         setCurrentPosition();
         ep.setPosition(i, j, k);
         saveSettings(getWorldDir());

         /*
          * Instantmine - allows the player to break blocks instantly
          */
      } else if (split[0].equalsIgnoreCase("instantmine")) {
         instant = !instant;
         sendMessage("Instant mine now " + (instant ? "on" : "off"));
         saveSettings();

         /*
          * Setjump - sets the players jump height
          */
      } else if (split[0].equalsIgnoreCase("setjump")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("reset")) {
            gravity = 1D;
            falldamage = true;
         } else {
            try {
               double grav = Double.parseDouble(split[1]);
               gravity = grav > 0D ? grav : 1D;
               prevgravity = gravity;
               falldamage = gravity <= 1D;
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
         }
         sendMessage("Player jump set at: " + gravity);
         saveSettings();

         /*
          * Setspeed - sets the players moving speed
          */
      } else if (split[0].equalsIgnoreCase("setspeed")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("reset")) {
            speed = 1F;
         } else {
            try {
               float speed = Float.parseFloat(split[1]);
               this.speed = speed > 0F ? speed : 1F;
               this.prevspeed = this.speed;
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
         }
         sendMessage("Player speed set at: " + speed);
         saveSettings();

         /*
          * Falldamage - toggles falldamage on/off
          */
      } else if (split[0].equalsIgnoreCase("falldamage")) {
         falldamage = !falldamage;
         sendMessage("Fall damage now " + (falldamage ? "on" : "off"));
         saveSettings();

         /*
          * Waterdamage - toggles waterdamage on/off
          */
      } else if (split[0].equalsIgnoreCase("waterdamage")) {
         waterdamage = !waterdamage;
         sendMessage("Water damage now " + (waterdamage ? "on" : "off"));
         saveSettings();

         /*
          * Firedamage - toggles firedamage on/off
          */
      } else if (split[0].equalsIgnoreCase("firedamage")) {
         ep.isImmuneToFire = !ep.isImmuneToFire;
         sendMessage("Fire damage now " + (ep.isImmuneToFire ? "off" : "on"));
         saveSettings();

         /*
          * Damage - toggles damage on/off
          */
      } else if (split[0].equalsIgnoreCase("damage")) {
         damage = !damage;
         sendMessage("Damage now " + (damage ? "on" : "off"));
         saveSettings();

         /*
          * Extinguish - removes fire from the world
          */
      } else if (split[0].equalsIgnoreCase("ext") || split[0].equalsIgnoreCase("extinguish")) {
         int distance = 16;
         if (split.length > 1) {
            if (split[1].equalsIgnoreCase("all")) {
               distance = 128;
            }
         }
         int x = MathHelper.floor_double(ep.posX);
         int y = MathHelper.floor_double(ep.posY);
         int z = MathHelper.floor_double(ep.posZ);
         for (int i = 0; i < distance; i++) {
            for (int j = 0; j < distance; j++) {
               if (y - j < 0 || y + j > 256) {
                  continue;
               }
               for (int k = 0; k < distance; k++) {
                  if (mc.theWorld.getBlockId(x + i, y + j, z + k) == Block.fire.blockID) {
                     mc.theWorld.setBlockWithNotify(x + i, y + j, z + k, 0);
                  }
                  if (mc.theWorld.getBlockId(x - i, y + j, z + k) == Block.fire.blockID) {
                     mc.theWorld.setBlockWithNotify(x - i, y + j, z + k, 0);
                  }
                  if (mc.theWorld.getBlockId(x - i, y + j, z - k) == Block.fire.blockID) {
                     mc.theWorld.setBlockWithNotify(x - i, y + j, z - k, 0);
                  }
                  if (mc.theWorld.getBlockId(x + i, y + j, z - k) == Block.fire.blockID) {
                     mc.theWorld.setBlockWithNotify(x + i, y + j, z - k, 0);
                  }
                  if (mc.theWorld.getBlockId(x + i, y - j, z + k) == Block.fire.blockID) {
                     mc.theWorld.setBlockWithNotify(x + i, y - j, z + k, 0);
                  }
                  if (mc.theWorld.getBlockId(x - i, y - j, z + k) == Block.fire.blockID) {
                     mc.theWorld.setBlockWithNotify(x - i, y - j, z + k, 0);
                  }
                  if (mc.theWorld.getBlockId(x - i, y - j, z - k) == Block.fire.blockID) {
                     mc.theWorld.setBlockWithNotify(x - i, y - j, z - k, 0);
                  }
                  if (mc.theWorld.getBlockId(x + i, y - j, z - k) == Block.fire.blockID) {
                     mc.theWorld.setBlockWithNotify(x + i, y - j, z - k, 0);
                  }
               }
            }
         }
         ep.fireResistance = 0;
         //ep.fire = 0;
         sendMessage("Fire extinguished");

         /*
          * Explode - makes the player explode
          */
      } else if (split[0].equalsIgnoreCase("explode")) {
         float size = 4F;
         if (split.length > 1) {
            try {
               size = Integer.parseInt(split[1]);
            } catch (Exception e) {
               size = 4F;
            }
         }
         mc.theWorld.createExplosion(ep, ep.posX, ep.posY, ep.posZ, size, true);

         /*
          * Timeschedule - sets a timeschedule which minecraft runs on
          */
      } else if (split[0].equalsIgnoreCase("timeschedule")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("reset")) {
            timeschedule = null;
            lastrift = -1;
            sendMessage("Timeschedule reset");
            saveSettings();
            return;
         }
         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }
         timeschedule = new int[4];
         try {
            timeschedule[2] = Integer.parseInt(split[1].split(":")[0]);
            timeschedule[3] = Integer.parseInt(split[1].split(":")[1]);
            timeschedule[0] = Integer.parseInt(split[2].split(":")[0]);
            timeschedule[1] = Integer.parseInt(split[2].split(":")[1]);
            timeschedule[2] = timeschedule[2] < 0 ? 0 : (timeschedule[2] > 23 ? 23 : timeschedule[2]);
            timeschedule[3] = timeschedule[3] < 0 ? 0 : (timeschedule[3] > 59 ? 59 : timeschedule[3]);
            timeschedule[0] = timeschedule[0] < 0 ? 0 : (timeschedule[0] > 23 ? 23 : timeschedule[0]);
            timeschedule[1] = timeschedule[1] < 0 ? 0 : (timeschedule[1] > 59 ? 59 : timeschedule[1]);
            lastrift = -1;
         } catch (Exception e) {
            sendError(ERRMSG_PARSE);
            return;
         }
         sendMessage("Timeschedule set. From: " + split[1] + " To: " + split[2]);
         saveSettings();

         /*
          * Search - searches for the given string against items
          */
      } else if (split[0].equalsIgnoreCase("search")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         String results = "";
         for (int i = 0; i < ITEMNAMES.size(); i++) {
            String temp;
            if ((temp = (String) ITEMNAMES.elementAt(i)) != null) {
               results = temp.indexOf(split[1].trim().toLowerCase()) != -1 ? results + " " + temp + "(" + i + ")" : results;
            }
         }
         if (results.equalsIgnoreCase("")) {
            sendMessage("No results found");
         } else {
            sendMessage(results);
         }

         /*
          * Msg - Sends a message onto the screen
          */
      } else if (split[0].equalsIgnoreCase("msg")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         String message = "";
         for (int i = 1; i < split.length; i++) {
            message += split[i] + " ";
         }
         sendMessage(message.trim());

         /*
          * Grow - grows all plants/trees
          */
      } else if (split[0].equalsIgnoreCase("grow")) {
         int distance = 16;
         if (split.length > 1) {
            if (split[1].equalsIgnoreCase("all")) {
               distance = 128;
            }
         }
         int x = MathHelper.floor_double(ep.posX);
         int y = MathHelper.floor_double(ep.posY);
         int z = MathHelper.floor_double(ep.posZ);
         WorldGenerator sml = new WorldGenTrees(true);
         WorldGenerator big = new WorldGenBigTree(true);
         WorldGenerator wgt = sml;
         Random r = new Random();
         for (int i = 0; i < distance; i++) {
            for (int j = 0; j < distance; j++) {
               if (y - j < 0 || y + j > 256) {
                  continue;
               }
               for (int k = 0; k < distance; k++) {
                  if (r.nextInt(10) == 0) {
                     wgt = big;
                  } else {
                     wgt = sml;
                  }
                  growPlant(x + i, y + j, z + k, r, wgt);
                  growPlant(x - i, y + j, z + k, r, wgt);
                  growPlant(x - i, y + j, z - k, r, wgt);
                  growPlant(x + i, y + j, z - k, r, wgt);
                  growPlant(x + i, y - j, z + k, r, wgt);
                  growPlant(x - i, y - j, z + k, r, wgt);
                  growPlant(x - i, y - j, z - k, r, wgt);
                  growPlant(x + i, y - j, z - k, r, wgt);
               }
            }
         }
         sendMessage("Plants have now grown.");

         /*
          * Itemname provides you with the itemname and ID of the current item
          */
      } else if (split[0].equalsIgnoreCase("itemname")) {
         if (ep.inventory.mainInventory[ep.inventory.currentItem] == null) {
            sendMessage("No item currently selected.");
            return;
         }
         int id = ep.inventory.getCurrentItem().itemID;
         String name = (String) ITEMNAMES.elementAt(id);
         name = name == null ? "Unknown" : name;
         String damage = !ep.inventory.getCurrentItem().isItemStackDamageable() && ep.inventory.getCurrentItem().getItemDamage() > 0 ? ":" + ep.inventory.getCurrentItem().getItemDamage() : "";
         sendMessage(name + " (" + id + damage + ")");

         /*
          * Moves the player to the nether from the current location
          */
      } else if (split[0].equalsIgnoreCase("useportal")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         try {
            mc.usePortal(Integer.parseInt(split[1]));
            return;
         } catch (Exception e) {}

         if (split[1].equalsIgnoreCase("normal")) {
            mc.usePortal(0);
         } else if (split[1].equalsIgnoreCase("nether")) {
            mc.usePortal(-1);
         } else if (split[1].equalsIgnoreCase("end")) {
            mc.usePortal(1);
         } else {
            sendError(ERRMSG_PARSE);
         }

         /*
          * Replenishes the players inventory
          */
      } else if (split[0].equalsIgnoreCase("refill")) {
         if (split.length > 1 && split[1].equalsIgnoreCase("all")) {
            for (int i = 0; i < ep.inventory.mainInventory.length; i++) {
               if (ep.inventory.mainInventory[i] == null) {
                  continue;
               }
               ep.inventory.mainInventory[i].stackSize = ep.inventory.mainInventory[i].getMaxStackSize();
            }
            for (int i = 0; i < ep.inventory.armorInventory.length; i++) {
               if (ep.inventory.armorInventory[i] == null) {
                  continue;
               }
               ep.inventory.armorInventory[i].stackSize = ep.inventory.armorInventory[i].getMaxStackSize();
            }
            return;
         }
         if (ep.inventory.mainInventory[ep.inventory.currentItem] != null) {
            ep.inventory.mainInventory[ep.inventory.currentItem].stackSize = ep.inventory.mainInventory[ep.inventory.currentItem].getMaxStackSize();
         }

         /*
          * Moves all the players items from the inventory into a chest it creates next to the
          * player
          */
      } else if (split[0].equalsIgnoreCase("dropstore")) {
         mc.theWorld.setBlock((int) ep.posX + 1, (int) ep.posY - 1, (int) ep.posZ, Block.chest.blockID);
         mc.theWorld.setBlockWithNotify((int) ep.posX + 1, (int) ep.posY - 1, (int) ep.posZ + 1, Block.chest.blockID);
         InventoryLargeChest inv = new InventoryLargeChest("Large chest", (TileEntityChest) mc.theWorld.getBlockTileEntity((int) ep.posX + 1, (int) ep.posY - 1, (int) ep.posZ + 1), (TileEntityChest) mc.theWorld.getBlockTileEntity((int) ep.posX + 1, (int) ep.posY - 1, (int) ep.posZ));
         int count = 0;
         for (int i = 0; i < ep.inventory.mainInventory.length; i++) {
            inv.setInventorySlotContents(count++, ep.inventory.mainInventory[i]);
            ep.inventory.mainInventory[i] = null;
         }
         for (int i = 0; i < ep.inventory.armorInventory.length; i++) {
            inv.setInventorySlotContents(count++, ep.inventory.armorInventory[i]);
            ep.inventory.armorInventory[i] = null;
         }

         /*
          * Removes item drops from the map
          */
      } else if (split[0].equalsIgnoreCase("removedrops")) {
         int distance = 16;
         if (split.length > 1 && split[1].equalsIgnoreCase("all")) {
            distance = 1024;
         }
         List<Entity> npcs = mc.theWorld.getEntitiesWithinAABBExcludingEntity(ep, AxisAlignedBB.getBoundingBox(ep.posX - distance, ep.posY - distance, ep.posZ - distance, ep.posX + distance, ep.posY + distance, ep.posZ + distance));
         for (int i = 0; i < npcs.size(); i++) {
            Entity e = npcs.get(i);
            if (e instanceof EntityItem) {
               ((EntityItem) e).setDead();
            }
         }

         /*
          * Turns fly mode on
          */
      } else if (split[0].equalsIgnoreCase("fly")) {
         double speed = -1;
         boolean setprev = true;
         ep.fallDistance = 0;
         if (!noClip) {
            if (split.length == 2) {
               try {
                  double flyspeed = Double.parseDouble(split[1]);
                  speed = flyspeed;
               } catch (Exception e) {
                  sendError(ERRMSG_PARSE);
                  return;
               }
               if (speed != this.speed) {
                  if (flying) {
                     setprev = false;
                  }
                  flying = true;
                  sendMessage("Flying is enabled at speed " + speed);
               } else {
                  flying = false;
                  sendMessage("Flying now turned off");
               }
            } else {
               flying = !flying;
               sendMessage("Flying now turned " + (flying ? "on" : "off"));
            }
            if (flying) {
               if (setprev) {
                  prevspeed = this.speed;
                  prevgravity = this.gravity;
                  prevfalldamage = falldamage;
               }
               if (speed > 0) {
                  this.speed = speed;
                  this.gravity = speed;
               }
               falldamage = false;
            } else {
               falldamage = prevfalldamage;
               this.speed = prevspeed;
               this.gravity = prevgravity;
            }
            saveSettings();
         }

         /*
          * Turns noclip mode on
          */
      } else if (split[0].equalsIgnoreCase("noclip")) {
         noClip = !noClip;
         noclip(noClip);
         /*try {
            Block list[] = new Block[Block.blocksList.length];
            for (int i = 0; i < list.length; i++) {
               list[i] = Block.blocksList[i];
            }
            Field f = Block.class.getDeclaredField("blocksList");
            this.setFinal(f, null, null);
            Block.blocksList[1] = null;
            Block.blocksList[1] = new SPCBlock(1,Material.ground);
            //f.set(null,list);

         } catch (Exception e) {
            e.printStackTrace();
         }*/
         sendMessage("Noclip now turned " + (noClip ? "on" : "off"));
         saveSettings();

         /*
          * Sets the player reach distance
          */
      } else if (split[0].equalsIgnoreCase("reach")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         try {
            if (split[1].equalsIgnoreCase("+")) {
               reachdistance++;
            } else if (split[1].equalsIgnoreCase("-")) {
               reachdistance--;
            } else {
               reachdistance = Float.parseFloat(split[1]);
            }
            if (reachdistance < 0) {
               reachdistance = 0;
            }
            sendMessage("Reach distance set at " + reachdistance);
         } catch (Exception e) {
            if (split[1].equalsIgnoreCase("reset")) {
               reachdistance = 4F;
               return;
            }
            sendError(ERRMSG_PARSE);
         }
         saveSettings();

         /*
          * Resets the settings to default
          */
      } else if (split[0].equalsIgnoreCase("reset")) {
         initialise();
         saveSettings();
         sendMessage("Settings reset to default.");

         /*
          * Runs a macro
          */
      } else if (split[0].equalsIgnoreCase("macro") || split[0].equalsIgnoreCase("sc")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         File macrodir = new File(MODDIR, "macros/");
         if (!macrodir.exists() || !macrodir.isDirectory()) {
            sendError("Macro directory could not be found.");
            return;
         }

         if (split[1].equalsIgnoreCase("create") || split[1].equalsIgnoreCase("edit") || split[1].equalsIgnoreCase("delete") || split[1].equalsIgnoreCase("folder") || split[1].equalsIgnoreCase("list") || split[1].equalsIgnoreCase("dir") || split[1].equalsIgnoreCase("engine")) {
            if (split[1].equalsIgnoreCase("list")) {

               String list = "";
               for (File macro : macrodir.listFiles()) {
                  String temp = "";
                  if ((temp = macro.getName()).endsWith(".txt")) {
                     list += temp.substring(0, temp.length() - 4) + ", ";
                  }
               }
               if (!list.equalsIgnoreCase("")) {
                  sendMessage("Installed Macros:");
                  sendMessage(list);
               } else {
                  sendMessage("No macros found.");
               }
               return;
            } else if (split[1].equalsIgnoreCase("folder") || split[1].equalsIgnoreCase("dir")) {
               if (Desktop.isDesktopSupported()) {
                  try {
                     Desktop d = Desktop.getDesktop();
                     d.open(macrodir);
                  } catch (Exception e) {
                     sendError(ERRMSG_OSNOTSUPPORTED);
                  }
               } else {
                  sendError(ERRMSG_OSNOTSUPPORTED);
               }
               return;
            } else if (split.length < 3) {
               sendError(ERRMSG_PARAM);
               return;
            }
            String filename = join(split, 2, split.length);
            File macro = new File(macrodir, filename + ".txt");
            if (split[1].equalsIgnoreCase("delete")) {
               if (macro.exists() && macro.isFile()) {
                  try {
                     macro.delete();
                     sendMessage("Macro successfully deleted");
                  } catch (Exception e) {
                     sendError("Could not delete specified macro " + filename);
                  }
               } else {
                  sendError("Could not delete specified macro " + filename);
               }
               return;
            } else if (split[1].equalsIgnoreCase("engine")) {
               ScriptEngineManager engineMgr = new ScriptEngineManager();
               ScriptEngine engine = engineMgr.getEngineByName(filename);
               if (engine == null) {
                  sendError("Specified language engine could not be loaded.");
                  return;
               }
               this.engine = filename;
               saveSettings();
               sendMessage("Script engine changed to " + filename);
               return;
            }
            if (!macro.exists()) {
               try {
                  macro.createNewFile();
               } catch (Exception e) {
                  sendError("Could not open the macro for editing");
               }
            }
            if (Desktop.isDesktopSupported()) {
               try {
                  Desktop d = Desktop.getDesktop();
                  d.edit(macro);
               } catch (Exception e) {
                  sendError(ERRMSG_OSNOTSUPPORTED);
               }
            } else {
               sendError(ERRMSG_OSNOTSUPPORTED);
            }
            return;
         }

         if (split[0].equalsIgnoreCase("sc")) {
            File thefile = new File(macrodir, split[1]);
            if (!thefile.exists()) {
               thefile = new File(split[1]);
            }
            String args[] = new String[split.length - 1];
            for (int i = 1; i < split.length; i++) {
               args[i-1] = split[i];
            }
            ScriptEngineManager engineMgr = new ScriptEngineManager();
            ScriptEngine engine = engineMgr.getEngineByName(this.engine);
            engine.put("args", args);
            engine.put("player", ep);
            engine.put("helper", this);
            engine.put("world", mc.theWorld);
            engine.put("minecraft", mc);
            InputStream is = new FileInputStream(thefile);
            try {
               Reader reader = new InputStreamReader(is);
               engine.eval(reader);
            } catch (Exception ex) {
               ex.printStackTrace();
            }
         } else {
            File thefile = new File(macrodir, split[1] + ".txt");
            if (!thefile.exists()) {
               thefile = new File(split[1]);
            }

            if (!thefile.exists() || !thefile.isFile()) {
               sendError("Could not find specified file.");
               return;
            }

            split[1] = thefile.getAbsolutePath();
            BufferedReader br = new BufferedReader(new FileReader(thefile));

            String line = null;
            while ((line = br.readLine()) != null) {
               // Adds arguments to the line
               for (int i = 0; i < split.length - 1; i++) {
                  line = line.replaceAll("\\$_" + i, split[i + 1]);
               }

               // Remove all unspecified arguments from line
               line = line.replaceAll("\\$_[0-9]+", "");

               // Executes the line
               ep.sendChatMessage(line);
            }
         }
         lastcommand = join(split, 0, split.length);
         sendMessage("Macro finished processing.");

         /*
          * Sets the maximum stack size of an item
          */
      } else if (split[0].equalsIgnoreCase("maxstack")) {
         if (split.length < 2) {
            Item i = ep.inventory.getCurrentItem().getItem();
            if (i == null) {
               return;
            }
            i.maxStackSize = 64;
            sendMessage("Current items stack size changed to 64");
            return;
         }
         String args[] = new String[split.length - 1];
         for (int j = 0; j < args.length; j++) {
            args[j] = split[j + 1];
         }
         ItemStack i = getItemStack(args);
         if (i == null) {
            if (split[1].equalsIgnoreCase("all")) {
               int size = 64;
               if (split.length > 2) {
                  try {
                     size = Integer.parseInt(split[2]);
                  } catch (Exception e) {
                     size = 64;
                  }
               }
               size = size > 64 || size < 1 ? 64 : size;
               for (int j = 0; j < Item.itemsList.length; j++) {
                  if (Item.itemsList[j] != null) {
                     Item.itemsList[j].maxStackSize = size;
                  }
               }
               sendMessage("All items MAX stack size set at " + size);
               return;
            }
            sendError(ERRMSG_PARSE);
            return;
         }
         int size = i.getItem().maxStackSize == i.stackSize ? 64 : (i.stackSize < 0 || i.stackSize > 64) ? 64 : i.stackSize;
         i.getItem().maxStackSize = size;
         sendMessage("Items stack size changed to " + size);

         /*
          * Provides a number of generic world related commands
          */
      } else if (split[0].equalsIgnoreCase("world")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("load")) {
            if (split.length < 3) {
               sendError(ERRMSG_PARAM);
               return;
            }
            File parent = new File(Minecraft.getMinecraftDir(), "saves");
            File child = new File(parent, split[2]);
            if (!child.exists()) {
               parent = new File(split[2]);
               child = parent;
               parent = parent.getParentFile();
            }
            if (parent == null || child == null) {
               sendError("Could not find the specified save.");
               return;
            }

            String message = child.getAbsolutePath();
            // World w = new World(new SaveHandler(parent, child.getName(),false), child.getName(),
            // (new Random()).nextLong());
            // mc.changeWorld2(new World(parent,child.getName()),"Changing World to " + message);
            // public WorldSettings(long l, int i, boolean flag, boolean flag1, EnumWorldType enumworldtype)
            ISaveHandler savehandler = mc.getSaveLoader().getSaveLoader(child.getName(), false);
            Object o = mc.worldClass.getDeclaredConstructor(new Class[]{ISaveHandler.class, String.class, WorldSettings.class, Profiler.class}).newInstance(
                       new Object[]{savehandler, child.getName(), new WorldSettings(new Random().nextLong(), EnumGameType.SURVIVAL, true, false, WorldType.DEFAULT), mc.mcProfiler});
            mc.changeWorld2((WorldSSP)o, "Changing World to " + message);
//             mc.changeWorld2(new WorldSSP(new SaveHandler(parent, child.getName(), false), child.getName(), new WorldSettings(new Random().nextLong(), EnumGameType.SURVIVAL, true, false, WorldType.DEFAULT), mc.mcProfiler), "Changing World to " + message);
         } else if (split[1].equalsIgnoreCase("save") || split[1].equalsIgnoreCase("backup")) {
            LoadingScreenRenderer l = new LoadingScreenRenderer(mc);
            l.resetProgressAndMessage("Please wait... Saving level");
            ((WorldSSP)mc.theWorld).saveWorld(true, l);
            if (split[1].equalsIgnoreCase("backup")) {
               l = new LoadingScreenRenderer(mc);
               l.resetProgressAndMessage("Please wait... World is being backed up");
               String worldname = mc.theWorld.worldInfo.getWorldName();
               SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");
               String time = format.format(new Date());
               copyDirectory(getWorldDir(), new File(Minecraft.getMinecraftDir(), "backup/" + worldname + "/" + time), l);
            }
         } else if (split[1].equalsIgnoreCase("exit")) {
            mc.displayGuiScreen(new GuiMainMenu());
            mc.theWorld = null;
            mc.changeWorld(null, "", null);
         } else if (split[1].equalsIgnoreCase("new")) { // TODO: Add ability to specify world type (ie: default or flat)
            long seed = 0;
            boolean sd = false;
            if (split.length > 3) {
               try {
                  seed = Long.parseLong(split[3]);
                  sd = true;
               } catch (Exception e) {
                  sendError(ERRMSG_PARSE);
                  return;
               }
            }
            File parent = new File(Minecraft.getMinecraftDir(), "saves");
            String name = split.length > 2 ? split[2] : "" + System.currentTimeMillis();
            File child = new File(parent, name);
            if (child.exists()) {
               sendError("Cannot create new world right now, the World already exists.");
               return;
            }
            WorldSSP world = null;
            ISaveHandler savehandler = mc.getSaveLoader().getSaveLoader(child.getName(), false);
            if (sd) {
              Object o = mc.worldClass.getDeclaredConstructor(new Class[]{ISaveHandler.class, String.class, WorldSettings.class, Profiler.class}).newInstance(
                         new Object[]{savehandler, child.getName(), new WorldSettings(seed,EnumGameType.SURVIVAL,true,false,WorldType.DEFAULT), mc.mcProfiler});
              world = (WorldSSP)o;
//                world = new WorldSSP(   new SaveHandler(parent, child.getName(), false), child.getName(), new WorldSettings(seed,EnumGameType.SURVIVAL,true,false,WorldType.DEFAULT), mc.mcProfiler);
            } else {
              Object o = mc.worldClass.getDeclaredConstructor(new Class[]{ISaveHandler.class, String.class, WorldSettings.class, Profiler.class}).newInstance(
                         new Object[]{savehandler, child.getName(), new WorldSettings((new Random()).nextLong(),EnumGameType.SURVIVAL,true,false,WorldType.DEFAULT), mc.mcProfiler});
              world = (WorldSSP)o;
//                world = new WorldSSP(new SaveHandler(parent, child.getName(), false), child.getName(), new WorldSettings((new Random()).nextLong(),EnumGameType.SURVIVAL,true,false,WorldType.DEFAULT), mc.mcProfiler);
            }

            mc.changeWorld2(world, "Creating a new world at: " + child.getAbsolutePath());
         } else if (split[1].equalsIgnoreCase("list")) {
            File parent = new File(Minecraft.getMinecraftDir(), "saves");
            File list[] = parent.listFiles();
            String saves = null;
            for (int i = 0; i < list.length; i++) {
               if (list[i].isDirectory()) {
                  if (saves == null) {
                     saves = list[i].getName();
                  } else {
                     saves += ", " + list[i].getName();
                  }
               }
            }
            sendMessage("World Saves:");
            sendMessage(saves);
         } else if (split[1].equalsIgnoreCase("seed")) {
            if (split.length == 2) {
               sendMessage("The current world seed is " + mc.theWorld.worldInfo.getSeed());
               return;
            }
            long seed = -1;
            try {
               seed = Long.parseLong(split[2]);
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }

            NBTTagCompound n = mc.theWorld.worldInfo.getNBTTagCompound();
            n.setLong("RandomSeed", seed);
            mc.theWorld.worldInfo = new WorldInfo(n);
            sendMessage("World seed set to " + seed);

         } else if (split[1].equalsIgnoreCase("name")) {
            if (split.length > 2) {
               String name = join(split, 2, split.length);
               mc.theWorld.worldInfo.setWorldName(name);
            }
            sendMessage("Your current world's name is " + mc.theWorld.worldInfo.getWorldName());
         } else {
            sendError("Unknown world command: " + split[1]);
         }

         /*
          * Binds a command to a key
          */
      } else if (split[0].equalsIgnoreCase("bind") || split[0].equalsIgnoreCase("bindid")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         if (split[1].equalsIgnoreCase("list")) {
            sendBindList();
            return;
         }

         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }
         int keycode = -1;
         if (split[0].equalsIgnoreCase("bind")) {
            try {
               keycode = Keyboard.getKeyIndex(split[1].toUpperCase());
               if (keycode == Keyboard.KEY_NONE) {
                  keycode = -1;
               }
               // keycode = Integer.parseInt(split[1]);
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
         } else {
            try {
               keycode = Integer.parseInt(split[1]);
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
         }
         if (keycode < 0) {
            sendError("Invalid keycode.");
            return;
         }
         String command = split[2];
         for (int i = 3; i < split.length; i++) {
            command += " " + split[i];
         }
         bound.set("" + keycode, command);
         if (bound.save(new File(MODDIR, "bindings.properties"), "Single Player Commands - Key Bindings")) {
            sendMessage("Key " + split[1] + " successfully bound to specified command.");
         } else {
            sendError("Unable to save bindings to file. Key binding will work until world closes.");
         }

         /*
          * Unbinds a command from a key
          */
      } else if (split[0].equalsIgnoreCase("unbind") || split[0].equalsIgnoreCase("unbindid")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         if (split[1].equalsIgnoreCase("list")) {
            sendBindList();
            return;
         }

         if (split[0].equalsIgnoreCase("all")) {
            bound.clear();
            if (bound.save(new File(MODDIR, "bindings.properties"), "Single Player Commands - Key Bindings")) {
               sendMessage("All keys were successfully unbound.");
            } else {
               sendError("Unable to save bindings to file. Key bindings have been removed until world closes.");
            }
            return;
         }

         int keycode = -1;
         if (split[0].equalsIgnoreCase("unbind")) {
            try {
               keycode = Keyboard.getKeyIndex(split[1].toUpperCase());
               if (keycode == Keyboard.KEY_NONE) {
                  keycode = -1;
               }
               // keycode = Integer.parseInt(split[1]);
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
         } else {
            try {
               keycode = Integer.parseInt(split[1]);
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
         }
         if (keycode < 0) {
            sendError("Invalid keycode.");
            return;
         }
         Object temp = bound.remove(keycode + "");
         if (temp == null) {
            sendError("Could not find a binding for key " + split[1]);
            return;
         }
         if (bound.save(new File(MODDIR, "bindings.properties"), "Single Player Commands - Key Bindings")) {
            sendMessage("Key " + split[1] + " was successfully unbound.");
         } else {
            sendError("Unable to save bindings to file. Key binding has been removed until world closes.");
         }

         /*
          * Renames a command from one name to another
          */
      } else if (split[0].equalsIgnoreCase("rename")) {
         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (!commands.containsKey(split[1])) {
            sendError("Could not find specified command.");
            return;
         }
         String orig = commands.get(split[1]);
         commands.remove(split[1]);
         commands.put(split[2], orig);
         manager.unregisterCommand(split[1]);
         manager.registerCommand(createWrapper(split[2]));
         sendMessage("Command " + split[1] + " renamed to " + split[2] + ".");

         /*
          * Superheats the current item or all items in your inventory
          */
      } else if (split[0].equalsIgnoreCase("superheat")) {
         Field f[] = FurnaceRecipes.class.getDeclaredFields();
         Map<Integer, ItemStack> smelt = null;
         for (int i = 0; i < f.length; i++) {
            f[i].setAccessible(true);
            Object o = f[i].get(FurnaceRecipes.smelting());
            if (o instanceof Map) {
               smelt = (Map<Integer, ItemStack>) o;
               break;
            }
         }
         if (smelt == null) {
            sendError("Could not retrieve smelting list.");
            return;
         }

         boolean all = false;
         if (split.length >= 2 && split[1].equalsIgnoreCase("all")) {
            all = true;
         }
         int length = all ? ep.inventory.mainInventory.length : 1;
         int start = all ? 0 : ep.inventory.currentItem;

         for (int i = start; i < start + length; i++) {
            if (ep.inventory.mainInventory[i] == null) {
               continue;
            }
            int key = ep.inventory.mainInventory[i].itemID;
            if (smelt.containsKey(key) && smelt.get(key) != null) {
               int amt = ep.inventory.mainInventory[i].stackSize;
               ItemStack temp = smelt.get(key);
               int id = temp.itemID;
               int damage = temp.getItemDamage();
               ItemStack item = new ItemStack(id, amt, damage);
               ep.inventory.mainInventory[i] = item;
            }
         }

         /*
          * Sets lights always on - ie: no shadows (should make game faster)
          */
      } else if (split[0].equalsIgnoreCase("light")) {
         light = !light;
         toggleLight(light, true);

         sendMessage("Light now turned " + (light ? "on" : "off") + ".");
         saveSettings();

         /*
          * Drops - toggles item drops on or off
          */
      } else if (split[0].equalsIgnoreCase("drops")) {
         dropitems = !dropitems;
         sendMessage("Blocks " + (dropitems ? "now" : "will not") + " drop items.");
         saveSettings();

         /*
          * Sets whether or not the user is visible.
          */
      } else if (split[0].equalsIgnoreCase("invisible")) {

         isinvisible = !isinvisible;
         sendMessage("Toggling invisibility: " + isinvisible);
         saveSettings();

         /*
          * Freeze - freezes all NPCs in place
          */
      } else if (split[0].equalsIgnoreCase("freeze")) {
         mobsfrozen = !mobsfrozen;
         sendMessage("All mobs are " + (mobsfrozen ? "now" : "no longer") + " frozen");
         saveSettings();

         /*
          * Mobdamage - Turns damage on and off
          */
      } else if (split[0].equalsIgnoreCase("mobdamage")) {
         mobdamage = !mobdamage;
         sendMessage("Mob damage is now " + (mobdamage ? "on" : "off"));
         saveSettings();

         /*
          * Norepair - Items require no repair while command is on (currently not working)
          */
      } else if (split[0].equalsIgnoreCase("itemdamage")) {

         norepair = !norepair;
         for (int i = 0; i < Item.itemsList.length; i++) {
            if (Item.itemsList[i] instanceof Item) {
               if (norepair) {
                  //System.out.println("Max damage is now: -1");
                  if (Item.itemsList[i].isDamageable()) {
                     Item.itemsList[i].setMaxDamage(MAGICNUMBER);
                  }
                  // Item.itemsList[i].maxDamage = -1;
               } else {
                  // System.out.println("[" + i + "]  Max damage is reset to: " +
                  // ITEM_MAX_DAMAGE[i]);
                  if (Item.itemsList[i].getMaxDamage() == MAGICNUMBER) {
                     Item.itemsList[i].setMaxDamage(ITEM_MAX_DAMAGE[i]);
                  }
                  // Item.itemsList[i].maxDamage = ITEM_MAX_DAMAGE[i];
               }
            }
         }
         sendMessage("Items now " + (norepair ? "don't " : "") + "require repair.");
         saveSettings();

         /*
          * infiniteitems - Gives an infinite supply of items
          */
      } else if (split[0].equalsIgnoreCase("infiniteitems")) {
         infiniteitems = !infiniteitems;

         sendMessage("Items are " + (infiniteitems ? "now" : "no longer") + " infinite.");
         saveSettings();

         /*
          * Keepitems - allows player to keep items upon death.
          */
      } else if (split[0].equalsIgnoreCase("keepitems")) {
         keepitems = !keepitems;
         sendMessage("Items will" + (keepitems ? " be" : " not be") + " kept on death.");
         saveSettings();

         /*
          * Instantkill - Allows the player to instantly kill entities.
          */
      } else if (split[0].equalsIgnoreCase("instantkill")) {
         instantkill = !instantkill;
         saveSettings();
         sendMessage("Instant kill is turned " + (instantkill ? "on." : "off."));

         /*
          * Instantplant (instantgrow) - Instantly plants and grows saplings
          * 
          * @TODO - Reeds, cactus, wheat and flowers
          */
      } else if (split[0].equalsIgnoreCase("instantplant")) {
         instantgrow = false;
         if (split.length > 1 && split[1].equalsIgnoreCase("grow")) {
            instantgrow = true;
         }
         if (!instantgrow) {
            instantplant = !instantplant;
         } else {
            instantplant = true;
         }
         sendMessage("Instant plant turned " + (instantplant ? "on" : "off") + ", instant grow is " + (instantgrow ? "on" : "off"));

         /*
          * Plugin - Adds some commands which allow you to see plugins and turn them on and off
          */
      } else if (split[0].equalsIgnoreCase("plugin")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         if (split.length == 2) {
            if (split[1].equalsIgnoreCase("list") || split[1].equalsIgnoreCase("dlist")) {
               String list = "";
               Iterator<SPCPlugin> i = null;
               String message = "";
               if (split[1].equalsIgnoreCase("dlist")) {
                  i = PLUGIN_MANAGER.getDisabledPlugins().iterator();
                  message = "Disabled plugins:";
               } else {
                  i = PLUGIN_MANAGER.getPlugins().iterator();
                  message = "Loaded Plugins: ";
               }
               while (i.hasNext()) {
                  list += i.next().getName() + ", ";
               }
               if (list.equalsIgnoreCase("")) {
                  sendMessage("No plugins found.");
               } else {
                  sendMessage(message + list);
               }
            } else if (split[1].equalsIgnoreCase("enable")) {
               PLUGIN_MANAGER.setEnabled(true);
               sendMessage("Plugins have been enabled.");
            } else if (split[1].equalsIgnoreCase("disable")) {
               PLUGIN_MANAGER.setEnabled(false);
               sendMessage("Plugins have been disabled.");
            }
            return;
         } else if (split.length >= 3) {
            if (split[1].equalsIgnoreCase("disable") || split[1].equalsIgnoreCase("enable")) {
               boolean enable = true;
               if (split[1].equalsIgnoreCase("disable")) {
                  enable = false;
               }
               String name = split[2];
               for (int i = 3; i < split.length; i++) {
                  name += " " + split[i];
               }

               SPCPlugin plugins[] = PLUGIN_MANAGER.getPlugin(name);
               if (plugins.length == 0) {
                  sendError("Plugin with name \"" + name + "\" could not be found.");
                  return;
               }
               boolean flag = false;
               for (SPCPlugin plugin : plugins) {
                  if (enable) {
                     if (PLUGIN_MANAGER.enablePlugin(plugin)) {
                        flag = true;
                     }
                  } else {
                     if (PLUGIN_MANAGER.disablePlugin(plugin)) {
                        flag = true;
                     }
                  }
               }
               if (flag) {
                  sendMessage("Plugin with name \"" + name + "\" was " + (enable ? "enabled." : "disabled."));
               } else {
                  sendError("Plugin with name \"" + name + "\" could not be " + (enable ? "enabled." : "disabled."));
               }
            }
         }

         /*
          * Water movement -
          */
      } else if (split[0].equalsIgnoreCase("watermovement")) {
         watermovement = !watermovement;
         sendMessage("Liquid slowdown and current effects are " + (watermovement ? "on." : "off."));
         saveSettings();

         /*
          * Confuse -
          */
      } else if (split[0].equalsIgnoreCase("confuse")) {
         //confuseMobs();

         /*
          * Skin - Changes the players skin
          */
      } else if (split[0].equalsIgnoreCase("skin")) {

         if (split.length > 1) {
            if (split[1].equalsIgnoreCase("reset")) {
               ep.skinUrl = (new StringBuilder()).append("http://s3.amazonaws.com/MinecraftSkins/").append(sessionusername).append(".png").toString();
               return;
            }
            String username = "";
            for (int i = 1; i < split.length; i++) {
               username += split[i];
               if (i + 1 < split.length) {
                  username += " ";
               }
            }
            username.trim();
            changeSkin(ep, username);
         }

         /*
          * Scheduler - repeats a command the specified number of times
          */
         /*} else if (split[0].equalsIgnoreCase("scheduler")) {
         if (split.length < 4) {
            sendError(ERRMSG_PARAM);
            return;
         }

         int seconds = -1;
         int repeat = -1;
         String command;

         try {
            seconds = Integer.parseInt(split[1]);
            repeat = Integer.parseInt(split[2]);
            command = split[3];
            for (int i = 4; i < split.length; i++) {
               command += " " + split[i];
            }
         } catch (Exception e) {
            sendError(ERRMSG_PARSE);
            return;
         }

         int id = 0;

         sendMessage("Added repeating command. ID = " + id);*/

         /*
          * Runs the specified startup command - best used with /macro
          */
      } else if (split[0].equalsIgnoreCase("startup")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         String startup = split[1];
         for (int i = 2; i < split.length; i++) {
            startup += " " + split[i];
         }
         this.startup = startup;
         saveSettings();
         sendMessage("Startup command was successfully set.");

         /*
          * When run will mount you on the entity you are pointing at
          */
      } else if (split[0].equalsIgnoreCase("ride")) {
         if (ep.ridingEntity != null) {
            ep.mountEntity(null);
            return;
         }
         // MovingObjectPosition m = ep.rayTrace(reachdistance, 1.0F);
         MovingObjectPosition m = mc.objectMouseOver;
         if (m == null) {
            sendError(ERRMSG_NPCNOTFOUND);
            return;
         }
         Entity e = m.entityHit;
         if (e != null && e instanceof EntityLiving) {
            ep.mountEntity(e);
            sendMessage("Mounted the entity. Type /ride again to unmount");
         } else {
            sendError(ERRMSG_NPCNOTFOUND);
         }

         /*
          * Blows the NPC up which you are looking at
          */
      } else if (split[0].equalsIgnoreCase("exterminate")) {
         int size = 4;
         // MovingObjectPosition m = ep.rayTrace(reachdistance, 1.0F);
         MovingObjectPosition m = mc.objectMouseOver;
         if (m == null) {
            sendError(ERRMSG_NPCNOTFOUND);
            return;
         }
         if (split.length > 1) {
            try {
               size = Integer.parseInt(split[1]);
            } catch (Exception e) {
               size = 4;
            }
         }
         if (size < 0) {
            size = 4;
         }
         Entity e = m.entityHit;
         if (e != null && e instanceof EntityLiving) {
            mc.theWorld.createExplosion(ep, e.posX, e.posY, e.posZ, size, true);
            if (size > 3) {
               sendMessage("BOOM!");
            } else {
               sendMessage("puff");
            }
         } else {
            sendError(ERRMSG_NPCNOTFOUND);
         }

         /*
          * Shoots lit TNT in the direction you are facing
          */
      } else if (split[0].equalsIgnoreCase("cannon")) {
         EntityTNTPrimed tnt = new EntityTNTPrimed(mc.theWorld);
         tnt.setLocationAndAngles(ep.posX, ep.posY, ep.posZ, ep.rotationYaw, ep.rotationPitch);
         tnt.fuse = 40;
         tnt.motionX = -MathHelper.sin((tnt.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((tnt.rotationPitch / 180F) * 3.141593F);
         tnt.motionZ = MathHelper.cos((tnt.rotationYaw / 180F) * 3.141593F) * MathHelper.cos((tnt.rotationPitch / 180F) * 3.141593F);
         tnt.motionY = -MathHelper.sin((tnt.rotationPitch / 180F) * 3.141593F);

         double multiplier = 1;
         if (split.length > 1) {
            try {
               multiplier = Double.parseDouble(split[1]);
            } catch (Exception e) {
               multiplier = 1;
            }
         }

         tnt.motionX *= multiplier;
         tnt.motionY *= multiplier;
         tnt.motionZ *= multiplier;

         mc.theWorld.spawnEntityInWorld(tnt);

         /*
          * Toggles SPC output on/off
          */
      } else if (split[0].equalsIgnoreCase("output")) {
         output = !output;
         saveSettings();
         sendMessage("Output now turned " + (output ? "on" : "off"));

         /*
          * Contains configuration commands
          */
      } else if (split[0].equalsIgnoreCase("config")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("setglobal")) {
            File globalsettings = new File(MODDIR, "spc.settings");
            if (split.length > 2 && split[2].equalsIgnoreCase("reset")) {
               try {
                  globalsettings.delete();
                  sendMessage("Global settings reset to default.");
               } catch (Exception e) {
                  sendError("Unable to reset global settings.");
               }
               return;
            }
            File worldsettings = new File(getWorldDir(), "spc.settings");
            if (copyFile(worldsettings, globalsettings)) {
               sendMessage("Global configuration successfully set as current configuration.");
            } else {
               sendMessage("Unable to set global configuration.");
            }
         }

         /*
          * Gives you access to chests. Syntax - <drop|get|fill|swap>
          */
      } else if (split[0].equalsIgnoreCase("chest")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         int x1, y1, z1, x2, y2, z2;
         MovingObjectPosition target = ep.rayTrace(reachdistance, 1.0F);
         if (target == null) {
            sendError("No block within range.");
            return;
         }
         x1 = target.blockX;
         y1 = target.blockY;
         z1 = target.blockZ;
         x2 = target.blockX + 1;
         y2 = target.blockY;
         z2 = target.blockZ;

         if (split[1].equalsIgnoreCase("drop")) {
            y1 += 1;
            y2 += 1;
            mc.theWorld.setBlock(x1, y1, z1, Block.chest.blockID);
            mc.theWorld.setBlockWithNotify(x2, y2, z2, Block.chest.blockID);
         } else if (split[1].equalsIgnoreCase("fill") || split[1].equalsIgnoreCase("get") || split[1].equalsIgnoreCase("swap") || split[1].equalsIgnoreCase("clear")) {
            if (mc.theWorld.getBlockId(x1, y1, z1) == Block.chest.blockID) {
               if (mc.theWorld.getBlockId(x2, y2, z2) == Block.chest.blockID) {
               } else if (mc.theWorld.getBlockId(x1 - 1, y1, z1) == Block.chest.blockID) {
                  x2 = x1 - 1;
               } else if (mc.theWorld.getBlockId(x1, y1, z1 + 1) == Block.chest.blockID) {
                  x2 = x1;
                  z2 = z1 + 1;
               } else if (mc.theWorld.getBlockId(x1, y1, z1 - 1) == Block.chest.blockID) {
                  x2 = x1;
                  z2 = z1 - 1;
               } else {
                  y2 = -1;
               }
            } else {
               sendError("Chest not found. You need to point at a chest to fill.");
               return;
            }
         }

         IInventory inv = null;
         if (y2 > -1) {
            inv = new InventoryLargeChest("Large chest", (TileEntityChest) mc.theWorld.getBlockTileEntity(x1, y1, z1), (TileEntityChest) mc.theWorld.getBlockTileEntity(x2, y2, z2));
         } else {
            inv = (TileEntityChest) mc.theWorld.getBlockTileEntity(x1, y1, z1);
         }

         if (split[1].equalsIgnoreCase("drop") || split[1].equalsIgnoreCase("fill")) {
            transferInventory(ep.inventory, inv);
         } else if (split[1].equalsIgnoreCase("get")) {
            transferInventory(inv, ep.inventory);
         } else if (split[1].equalsIgnoreCase("clear")) {
            transferInventory(inv, null);
         } else if (split[1].equalsIgnoreCase("swap")) {
            InventoryPlayer p = new InventoryPlayer(ep);
            for (int i = 0; i < p.getSizeInventory(); i++) {
               p.setInventorySlotContents(i, ep.inventory.getStackInSlot(i));
               ep.inventory.setInventorySlotContents(i, null);
            }
            transferInventory(inv, ep.inventory);
            transferInventory(p, inv);
         }

         /*
          * Provides the biome name
          */
      } else if (split[0].equalsIgnoreCase("biome")) {
         //BiomeGenBase mobspawnerbase = mc.theWorld.getWorldChunkManager().getBiomeGenAt((int) ep.posX, (int) ep.posZ);
         int j0 = MathHelper.floor_double(mc.thePlayer.posX);
         int j2 = MathHelper.floor_double(mc.thePlayer.posZ);
         Chunk chunk = mc.theWorld.getChunkFromBlockCoords(j0, j2);
         sendMessage("Current Biome: " + chunk.getBiomeGenForWorldCoords(j0 & 0xf, j2 & 0xf, mc.theWorld.getWorldChunkManager()).biomeName);

         /*
          * Creates a glass block platform beneath the player
          */
      } else if (split[0].equalsIgnoreCase("platform")) {
         int x = MathHelper.floor_double(ep.posX);
         int y = MathHelper.floor_double(ep.posY);
         int z = MathHelper.floor_double(ep.posZ);
         mc.theWorld.setBlockWithNotify(x, y - 2, z, Block.glass.blockID);

         /*
          * Gives you control over what will spawn
          */
      } else if (split[0].equalsIgnoreCase("spawncontrol")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         if (split.length == 2) {
            if (split[1].equalsIgnoreCase("all")) {
               animalspawn = !animalspawn;
               monsterspawn = animalspawn;
            } else if (split[1].equalsIgnoreCase("monster") || split[1].equalsIgnoreCase("agressive")) {
               monsterspawn = !monsterspawn;
            } else if (split[1].equalsIgnoreCase("animal") || split[1].equalsIgnoreCase("friendly")) {
               animalspawn = !animalspawn;
            }
            mc.overrideMobSpawning = !animalspawn || !monsterspawn;
            mc.theWorld.setAllowedSpawnTypes(monsterspawn, animalspawn);
            saveSettings();
            sendMessage("Friendly mobs will " + (animalspawn ? "now spawn" : "not spawn") + ". Agressive mobs will " + (monsterspawn ? "now spawn" : "not spawn"));
            return;
         }
         /*boolean disable = true;
         if (split[1].equalsIgnoreCase("enable")) {
            disable = false;
         } else if (split[1].equalsIgnoreCase("disable")) {
            disable = true;
         } else {
            sendMessage("Invalid spawn control parameter.");
            return;
         }

         if (!toggleMob(split[2],disable)) {
            sendError("Could not configure the specified mob " + split[2]);
         } else {
            sendMessage("Successfully " + split[1] + "d the " + split[2] + " mob for spawning.");
         }*/

         /*
          * Reskins an NPC
          */
      } else if (split[0].equalsIgnoreCase("reskin")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         MovingObjectPosition m = mc.objectMouseOver;
         if (m == null || m.entityHit == null) {
            sendError("No entity found.");
            return;
         }
         if (m.entityHit instanceof EntityLiving) {
            ((EntityLiving) m.entityHit).texture = split[1];
         }

         sendMessage("Reskined to " + split[1]);

      } else if (split[0].equalsIgnoreCase("helmet")) {
         String as1[] = new String[split.length - 1];
         System.arraycopy(split, 1, as1, 0, split.length - 1);
         ItemStack itemstack = getItemStack(as1);
         if (itemstack == null) {
            sendError("Invalid item.");
            return;
         } else {
            mc.thePlayer.inventory.armorInventory[3] = itemstack;
            return;
         }

         /*
          * Makes the specified block slippery
          */
      } else if (split[0].equalsIgnoreCase("slippery")) {
         float f = 0.6F;
         if (split.length != 2 && split.length != 3) {
            sendError("Invalid syntax: expected: blockname [slipperiness]");
            return;
         }
         if (split.length == 3) {
            try {
               f = Float.parseFloat(split[2]);
            } catch (NumberFormatException numberformatexception) {
               sendError("Invalid number format: expected: blockname [slipperiness]");
               return;
            }
         }
         int i = getBlockID(split[1]);
         if (i == -1 || Block.blocksList[i] == null) {
            sendError("Invalid block.");
            return;
         } else {
            Block.blocksList[i].slipperiness = f;
            sendMessage(String.format("%s.slipperiness = %.2f", new Object[] { split[1], Float.valueOf(f) }));
            return;
         }

         /*
          * Gives the player "longer legs"
          */
      } else if (split[0].equalsIgnoreCase("longerlegs")) {
         String s1;
         if (ep.stepHeight == 1.0F) {
            ep.stepHeight = 0.5F;
            s1 = "off";
         } else {
            ep.stepHeight = 1.0F;
            s1 = "on";
         }
         sendMessage("Longer legs " + s1);

         /*
          * Toggles Atlantis mode on/off
          */
      } else if (split[0].equalsIgnoreCase("atlantis")) {
         String s1;
         if (Block.blocksList[0] != null) {
            Block.blocksList[0] = null;
            s1 = "off";
         } else {
            Block.blocksList[0] = Block.blocksList[8];
            s1 = "on";
         }
         sendMessage("Atlantis mode: " + s1);

         /*
          * Configures the weather
          */
      } else if (split[0].equalsIgnoreCase(/*"weather"*/"weather2")) {
         if (split.length < 2) {
            weather = !weather;
            sendMessage("Weather was " + (weather ? "enabled." : "disabled."));
            return;
         }
         WorldInfo wi = mc.theWorld.worldInfo;
         if (split[1].equalsIgnoreCase("rain")) {
            String s1;
            if (wi.isRaining()) {
               s1 = "off";
               wi.setRaining(false);
            } else {
               s1 = "on";
               wi.setRaining(true);
            }
            sendMessage("Rain has been turned " + s1);
         } else if (split[1].equalsIgnoreCase("thunder")) {
            String s1;
            if (wi.isThundering()) {
               s1 = "off";
               wi.setThundering(false);
               wi.setRaining(false);
            } else {
               s1 = "on";
               wi.setThundering(true);
               wi.setRaining(true);
            }
            sendMessage("Thunder has been turned " + s1);
         } else if (split[1].equalsIgnoreCase("lightning")) {
            EntityLightningBolt e = null;
            SPCObjectHit soh = new SPCObjectHit(ep.rayTrace(128, 1.0F));
            if (soh.blocky == -1) {
               return;
            }
            e = new EntityLightningBolt(mc.theWorld, soh.blockx, soh.blocky, soh.blockz);
            mc.theWorld.addWeatherEffect(e);
         } else if (split[1].equalsIgnoreCase("sun")) {
            wi.setThundering(false);
            wi.setRaining(false);
         } else if (split[1].equalsIgnoreCase("enable")) {
            weather = true;
            sendMessage("Weather was " + (weather ? "enabled." : "disabled."));
         } else if (split[1].equalsIgnoreCase("disable")) {
            weather = false;
            sendMessage("Weather was " + (weather ? "enabled." : "disabled."));
         } else {
            sendError("Unknown weather command: " + split[1]);
         }

         /*
          * Configures the zoom
          */
         /*      } else if (split[0].equalsIgnoreCase("zoom")) {
         double d = 1.0D;
         double d1 = 0.0D;
         double d2 = 0.0D;
         if (split.length == 2) {
            try {
               d = Double.parseDouble(split[1]);
               d1 = 0.0D;
               d2 = 0.0D;
            } catch (NumberFormatException numberformatexception) {
               sendError("Invalid number format.");
               return;
            }
         } else if (split.length == 4) {
            try {
               d = Double.parseDouble(split[1]);
               d1 = Double.parseDouble(split[2]);
               d2 = Double.parseDouble(split[3]);
            } catch (NumberFormatException numberformatexception) {
               sendError("Invalid number format.");
               return;
            }
         }
         mc.entityRenderer.func_21152_a(d, d1, d2);
         sendMessage(String.format("Zoom: %.1f %.1f %.1f", new Object[] { Double.valueOf(d), Double.valueOf(d1), Double.valueOf(d2) }));
          */
         /*
          * Spawns a portal neaby the player
          */
      } else if (split[0].equalsIgnoreCase("spawnportal")) {
         (new TeleporterSP()).createPortal(mc.theWorld, mc.thePlayer);
         sendMessage("Spawned a portal");

         /*
          * Clones the mob you are looking at the specified number of times
          */
      } else if (split[0].equalsIgnoreCase("clone")) {
         int i = 1;
         if (split.length > 1) {
            try {
               i = Integer.parseInt(split[1]);
            } catch (NumberFormatException numberformatexception) {
               sendError("Invalid count: " + split[1]);
               return;
            }
         }
         if (mc.objectMouseOver == null || mc.objectMouseOver.entityHit == null) {
            sendError(ERRMSG_NPCNOTFOUND);
            return;
         }
         Entity entity = mc.objectMouseOver.entityHit;
         for (int j = 0; j < i; j++) {
            Entity entity1 = null;
            try {
               entity1 = (Entity) entity.getClass().getConstructor(new Class[] { World.class }).newInstance(new Object[] { mc.theWorld });
            } catch (Throwable throwable) {
               sendError("An error occurred cloning the mob");
               return;
            }
            entity1.setPosition(entity.posX, entity.posY, entity.posZ);
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            entity.writeToNBT(nbttagcompound);
            entity1.readFromNBT(nbttagcompound);
            mc.theWorld.spawnEntityInWorld(entity1);
         }

         sendMessage("Cloned " + i + " " + EntityList.getEntityString(entity));

      } else if (split[0].equalsIgnoreCase("killall")) {
         Class class1 = Entity.class;
         if (split.length >= 2) {
            class1 = getEntity(split[1]);
            if (class1 == null) {
               sendError("Invalid mob: " + split[1]);
               return;
            }
         }
         List list = getAllMobs(class1, -1D);
         Entity entity;
         for (Iterator iterator = list.iterator(); iterator.hasNext(); entity.setDead()) {
            entity = (Entity) iterator.next();
         }

         sendMessage("Killed " + list.size() + " " + (split.length <= 1 ? "entities" : split[1]));

         /*
          * Makes the specified block flammable
          */
      } else if (split[0].equalsIgnoreCase("flammable")) {
         int i = 0;
         int j = 0;
         if (split.length != 2 && split.length != 4) {
            sendError("Invalid format. Expected: blockname spread catch");
            sendDebug(Integer.toString(split.length));
            return;
         }
         int k = getBlockID(split[1]);
         if (k == -1) {
            sendError("Unknown block: " + split[1]);
            return;
         }
         if (split.length == 4) {
            try {
               i = Integer.parseInt(split[2]);
               j = Integer.parseInt(split[3]);
            } catch (NumberFormatException numberformatexception) {
               sendError("Invalid format. Expected: blockname spread catch");
               sendDebug(Integer.toString(split.length));
               return;
            }
         }
         try {
            Method m[] = BlockFire.class.getDeclaredMethods();
            Method flamable = null;
            for (Method method : m) {
               method.setAccessible(true);
               Type t[] = method.getGenericParameterTypes();
               if (t.length == 3) {
                  if (t[0].equals(Integer.TYPE) && t[1].equals(Integer.TYPE) && t[2].equals(Integer.TYPE)) {
                     flamable = method;
                     break;
                  }
               }
            }
            if (flamable == null) {
               throw new Exception("Could not find method.");
            }
            flamable.setAccessible(true);
            flamable.invoke((BlockFire) Block.blocksList[51], new Object[] { Integer.valueOf(k), Integer.valueOf(i), Integer.valueOf(j) });
         } catch (Throwable throwable) {
            sendError("Could not invoke method");
            printStackTrace(throwable);
            return;
         }
         sendMessage(String.format("Set flammability of %d to %d %d", new Object[] { Integer.valueOf(k), Integer.valueOf(i), Integer.valueOf(j) }));

         /*
          * Makes water crystal clear
          */
      } else if (split[0].equalsIgnoreCase("clearwater")) {
         String s1;
         if (Block.lightOpacity[8] == 0) {
            Block.lightOpacity[8] = Block.lightOpacity[9] = 3;
            s1 = "off";
         } else {
            Block.lightOpacity[8] = Block.lightOpacity[9] = 0;
            s1 = "on";
         }
         sendMessage("Clear water: " + s1);

         /*
          * Hit that NPC with a punch like no other. Does the same amount of damage but sends them
          * flying!
          */
      } else if (split[0].equalsIgnoreCase("superpunch")) {
         double distance = superpunch == -1 ? 4 : -1;
         String level = "4";
         if (split.length == 2) {
            try {
               distance = Double.parseDouble(split[1]);
               level = split[1];
            } catch (Exception e) {
               if (split[1].equalsIgnoreCase("reset")) {
                  distance = -1;
               }
            }
         }
         if (distance <= 1) {
            superpunch = -1;
         } else {
            // Using whole numbers seems overpowered - divide by 10 for smaller numbers
            superpunch = (distance / 10) + 1;
         }
         saveSettings();
         sendMessage("Superpunch " + (superpunch <= 1.0 ? "is disabled" : "is set at " + level));

         /*
          * Confuses mobs so that they attack each other
          */
      } else if (split[0].equalsIgnoreCase("confusesuicide")) {
         double d = 10D;
         if (split.length >= 2) {
            try {
               d = Double.parseDouble(split[1]);
            } catch (NumberFormatException numberformatexception) {
               sendError("Invalid distance: " + split[1]);
               return;
            }
         }
         double d1 = d * d;
         List list = getAllMobs(EntityCreature.class, d * d);
         EntityCreature entitycreature;
         for (Iterator iterator = list.iterator(); iterator.hasNext(); confuse(entitycreature, entitycreature)) {
            Entity entity = (Entity) iterator.next();
            entitycreature = (EntityCreature) entity;
         }

         sendMessage(String.format("Confused %d within radius %.1f ", new Object[] { Integer.valueOf(list.size()), Double.valueOf(d) }));

         /*
          * Confuses mobs so that they attack each other
          */
      } else if (split[0].equalsIgnoreCase("confuse")) {
         double d = 10D;
         if (split.length >= 2) {
            try {
               d = Double.parseDouble(split[1]);
            } catch (NumberFormatException numberformatexception) {
               sendError("Invalid distance: " + split[1]);
               return;
            }
         }
         double d1 = d * d;
         List list = getAllMobs(EntityCreature.class, d * d);
         if (list.size() < 2) {
            sendError("Not enough mobs around.");
            return;
         }
         EntityCreature entitycreature = null;
         EntityCreature entitycreature1 = null;
         for (Iterator iterator = list.iterator(); iterator.hasNext();) {
            Entity entity = (Entity) iterator.next();
            EntityCreature entitycreature2 = (EntityCreature) entity;
            if (entitycreature1 == null) {
               entitycreature = entitycreature2;
               entitycreature1 = entitycreature2;
            } else {
               confuse(entitycreature1, entitycreature2);
               entitycreature1 = entitycreature2;
            }
         }

         if (entitycreature1 != null && entitycreature != entitycreature1) {
            confuse(entitycreature1, entitycreature);
         }
         sendMessage(String.format("Confused %d within radius %.1f ", new Object[] { Integer.valueOf(list.size()), Double.valueOf(d) }));

         /*
          * Cycles the painting you are looking at with another one
          */
      } else if (split[0].equalsIgnoreCase("cyclepainting")) {
         int inc = mc.thePlayer.isSneaking() ? -1 : 1;

         if (mc.objectMouseOver == null || !(mc.objectMouseOver.entityHit instanceof EntityPainting) || mc.objectMouseOver.entityHit.isDead) {
            sendError("No painting under cursor.");
            return;
         }

         EntityPainting pic = (EntityPainting) mc.objectMouseOver.entityHit;

         EnumArt oldart = pic.art;
         int olddirection = pic.hangingDirection;
         int current = 0;

         List<EnumArt> eligible = new ArrayList<EnumArt>();
         EnumArt[] all = EnumArt.values();

         for (int i = 0; i < all.length; ++i) {
            EnumArt art = all[i];
            pic.art = art;
            pic.setDirection(olddirection);

            if (pic.onValidSurface()) {
               eligible.add(art);

               if (oldart == art) {
                  current = i;
               }
            }
         }

         if (eligible.size() <= 1) {
            pic.art = oldart;
            pic.setDirection(olddirection);

            sendError("No other painting choices available.");
            return;
         }

         int newart = inc == -1 ? (current == 0 ? eligible.size() - 1 : current - 1) : (current == eligible.size() - 1 ? 0 : current + 1);

         pic.art = eligible.get(newart);
         pic.setDirection(olddirection);

         /*
          * Brings the player the specified entities
          */
      } else if (split[0].equalsIgnoreCase("bring")) {
         int i = -1;
         Class class1 = Entity.class;
         if (split.length >= 2) {
            class1 = getClass(split[1]);
            if (class1 == null) {
               sendError("Invalid mob: " + split[1]);
               return;
            }
            if (split.length >= 3) {
               try {
                  i = Integer.parseInt(split[2]);
               } catch (NumberFormatException numberformatexception) {
                  sendError("Invalid count: " + split[2]);
                  return;
               }
            }
         }
         Vec3 vec3d = mc.thePlayer.getLook(1.0F);
         double d = 5D;
         double d1 = mc.thePlayer.posX + vec3d.xCoord * d;
         double d2 = mc.thePlayer.posY + vec3d.yCoord * d;
         double d3 = mc.thePlayer.posZ + vec3d.zCoord * d;
         List list = getAllMobs(class1, -1D);
         if (i == -1) {
            i = list.size();
         }
         for (int j = 0; j < i; j++) {
            ((Entity) list.get(j)).setPosition(d1, d2, d3);
         }

         sendMessage("Brought " + i + " " + (split.length <= 1 ? "entities" : split[1]));

         /*
          * Plugin based help
          */
      } else if (split[0].equalsIgnoreCase("phelp")) {
         if (split.length == 1) {
            String commands[] = PLUGIN_MANAGER.getCommands();
            String list = "";
            Arrays.sort(commands);
            for (String command : commands) {
               list += command + ", ";
            }
            if (list.equalsIgnoreCase("")) {
               sendMessage("No plugin commands found.");
               return;
            }
            list = list.substring(0, list.length() - 2);
            sendMessage("Plugin Commands:");
            sendMessage(list);
         } else {
            String help[] = PLUGIN_MANAGER.getHelp(split[1]);
            if (help == null) {
               return;
            }
            helpMessage(help[0],split[1] + " " + help[1], split[1] + " " + help[2]);
         }

         /*
          * Achievement command - to be implemented when achievements are officially introduced
          */
      } else if (split[0].equalsIgnoreCase("achievement")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         // ep.yOffset = 2.0F;
         if (split[1].equalsIgnoreCase("list")) {
            Iterator<Achievement> i = AchievementList.achievementList.iterator();
            String list = "";
            while (i.hasNext()) {
               list += i.next().getDescription() + ", ";
            }
            if (list.equalsIgnoreCase("")) {
               sendError("No achievements found.");
            } else {
               sendMessage("Achievements: ");
               sendMessage(list);
            }
         } else if (split[1].equalsIgnoreCase("unlock")) {
            Iterator<Achievement> i = AchievementList.achievementList.iterator();
            while (i.hasNext()) {
               ep.triggerAchievement(i.next());
            }
            sendMessage("All achievements unlocked, cheater.");
         } /*else if (split[1].equalsIgnoreCase("lock")) {
            Iterator<Achievement> i = AchievementList.field_27388_e.iterator();
            while (i.hasNext()) {
               ep.addStat(i.next(),0);
            }
            sendMessage("Action Completed.");
         }*/

         /*
          * Adds an alias into the mod
          */
      } else if (split[0].equalsIgnoreCase("alias")) {
         if (split.length == 2 && split[1].equalsIgnoreCase("list")) {
            String list = "";
            Iterator i = alias.keySet().iterator();
            while (i.hasNext()) {
               list += ", " + i.next();
            }
            list = list.startsWith(", ") ? list.substring(2) : list.trim();
            sendMessage("All aliases:");
            sendMessage(list.equalsIgnoreCase("") ? "None found" : list);
            return;
         } else if (split.length < 3 && split[1].equalsIgnoreCase("all")) {
            sendError(ERRMSG_PARAM);
            return;
         }
         String value = join(split,2,split.length);
         addAlias(split[1],value);
         sendMessage("Alias \"" + split[1] + "\" successfully assigned to: " + value);

         /*
          * Removes an alias from the list
          */
      } else if (split[0].equalsIgnoreCase("ralias")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("all")) {
            Iterator i = alias.keySet().iterator();
            while (i.hasNext()) {
               manager.unregisterCommand(((String)i.next()));
            }
            alias.clear();
            alias.save();
            sendMessage("All aliases removed");
         } else if (alias.containsKey(split[1])) {
            alias.remove(split[1]);
            alias.save();
            manager.unregisterCommand(split[1]);
            sendMessage("Alias \"" + split[1] + "\" successfully removed");
         } else {
            sendError("Could not find specified alias \"" + split[1] + "\"");
         }

         /*
          * Resizes the Minecraft window appropriately.
          */
      } else if (split[0].equalsIgnoreCase("resize")) {
         //[1080p|720p|480p|setdefault [WIDTH HEIGHT]|<WIDTH HEIGHT>]
         //System.out.println(mc.mcCanvas.getParent().getWidth() + " " + mc.mcCanvas.getParent().getHeight());
         Frame f = Frame.getFrames()[0];
         if (split.length == 1) {
            setWindowSize(sizewidth,sizeheight);
         } else if (split[1].equalsIgnoreCase("1080p")) {
            setWindowSize(1920,1080);
         } else if (split[1].equalsIgnoreCase("720p")) {
            setWindowSize(1280,720);
         } else if (split[1].equalsIgnoreCase("480p")) {
            setWindowSize(640,480);
         } else if (split[1].equalsIgnoreCase("setdefault")) {
            if (split.length == 4) {
               int h = -1, w = -1;
               try {
                  w = Integer.parseInt(split[2]);
                  h = Integer.parseInt(split[3]);
               } catch (Exception e) {
                  sendError(ERRMSG_PARSE);
                  return;
               }
               sizewidth = w;
               sizeheight = h;
            } else {
               sizewidth = f.getWidth();
               sizeheight = f.getHeight();
            }
            saveSettings();
            sendMessage("Default screen resolution set to " + sizewidth + "x" + sizeheight);
            return;
         } else if (split.length == 3) {
            int h = -1, w = -1;
            try {
               w = Integer.parseInt(split[1]);
               h = Integer.parseInt(split[2]);
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
            setWindowSize(w,h);
         }
         f.setLocation(0, 0);
         sendMessage("Screensize set to " + f.getWidth() + "x" + f.getHeight());

         /*
          * Allows you to move the player in the direction the number of blocks you want
          */
      } else if (split[0].equalsIgnoreCase("moveplayer")) {
         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }
         double distance = 0;
         try {
            distance = Double.parseDouble(split[1]);
         } catch (Exception e) {
            sendError(ERRMSG_PARSE);
            return;
         }
         setCurrentPosition();
         Boolean ontop = null;
         if (split[2].startsWith("+")) {
            ontop = true;
            split[2] = split[2].substring(1);
         } else if (split[2].startsWith("-")) {
            ontop = false;
            split[2] = split[2].substring(1);
         }
         if (split[2].startsWith("n")) {
            ep.setPosition(ep.posX - distance, ep.posY, ep.posZ);
         } else if (split[2].startsWith("s")) {
            ep.setPosition(ep.posX + distance, ep.posY, ep.posZ);
         } else if (split[2].startsWith("e")) {
            ep.setPosition(ep.posX, ep.posY, ep.posZ - distance);
         } else if (split[2].startsWith("w")) {
            ep.setPosition(ep.posX, ep.posY, ep.posZ + distance);
         } else if (split[2].startsWith("u")) {
            ep.setPosition(ep.posX, ep.posY + distance, ep.posZ);
         } else if (split[2].startsWith("d")) {
            ep.setPosition(ep.posX, ep.posY - distance, ep.posZ);
         } else {
            sendMessage(ERRMSG_PARSE);
            return;
         }

         if (ontop != null) {
            if (ontop) {
               processCommands("ascend");
            } else {
               processCommands("descend");
            }
         }

         /*
          * ALlows the user to free cam around the map
          */
      } else if (split[0].equalsIgnoreCase("freecam")) {
         if (mc.renderViewEntity instanceof SPCEntityCamera) {
            mc.renderViewEntity = ep;
            flying = prevflying;
            speed = prevspeed;
            gravity = prevgravity;
            //falldamage = prevfalldamage;
            noClip = prevnoclip;
            noclip(prevnoclip);
            movecamera = false;
            moveplayer = true;
         } else {
            prevflying = flying;
            flying = true;
            prevspeed = speed;
            prevgravity = gravity;
            /*prevfalldamage = falldamage;
            falldamage = false;*/
            speed = 5;
            gravity = 5;
            prevnoclip = noClip;
            if (noClip) {
               noClip = false;
            }
            ep.motionX = 0;
            ep.motionY = 0;
            ep.motionZ = 0;
            mc.renderViewEntity = new SPCEntityCamera(mc, mc.theWorld, mc.session, ep.dimension);
            mc.renderViewEntity.setPositionAndRotation(ep.posX, ep.posY, ep.posZ, ep.rotationYaw, ep.rotationPitch);
            movecamera = true;
            moveplayer = false;
         }

         /*
          * Freezes the camera where it currently is
          */
      } else if (split[0].equalsIgnoreCase("freezecam")) {
         if (movecamera == true) {
            if (!(mc.renderViewEntity instanceof SPCEntityCamera)) {
               SPCEntityCamera cam = new SPCEntityCamera(mc, mc.theWorld, mc.session, ep.dimension);
               freezecamyaw = ep.rotationYaw;
               freezecampitch = ep.rotationPitch;
               cam.setPositionAndRotation(ep.posX, ep.posY, ep.posZ, freezecamyaw, freezecampitch);
               cam.setCamera(0, 0, 0,freezecamyaw, freezecampitch);
               mc.renderViewEntity = cam;
               //mc.renderViewEntity = new SPCEntityCamera(mc.theWorld);
               //mc.renderViewEntity.setPositionAndRotation(ep.posX, ep.posY, ep.posZ, ep.rotationYaw, ep.rotationPitch);
            }
            movecamera = false;
            moveplayer = true;
         } else {
            if ((mc.renderViewEntity instanceof SPCEntityCamera)) {
               mc.renderViewEntity.kill();
               mc.renderViewEntity = ep;
            }
            movecamera = true;
            moveplayer = true;
         }

         /*
          * Update - allows you to turn notifications on/off
          */
      } else if (split[0].equalsIgnoreCase("update")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("enable")) {
            updateson = true;
         } else if (split[1].equalsIgnoreCase("disable")) {
            updateson = false;
         } else if (split[1].equalsIgnoreCase("check")) {
            try {
               Method m = PlayerHelper.class.getDeclaredMethod("checkUpdateAvailable", Vector.class);
               (new SPCCheckVersion(new SPCVersion[]{EntityPlayerSPSPC.SPCVERSION},EntityPlayerSPSPC.MCVERSION,m,this)).start();
            } catch (Throwable t) {
               PlayerHelper.printStackTrace(t);
            }
            return;
         } else {
            sendMessage("Invalid update command.");
            return;
         }
         sendMessage("Updates now " + split[1] + ".");

         /*
          * Allows the text colour to be changed to a user specified colour
          */
      } else if (split[0].equalsIgnoreCase("textcolor")) {
         String message = "";
         if (split.length == 2 && split[1].equalsIgnoreCase("reset")) {
            textcolornormal = 'f';
            textcolorerror = '4';
            textcolorrandom = VALID_COLOURS;
            message = "Text colors were reset to default.";
         } else if (split.length != 3) {
            sendError(ERRMSG_PARAM);
            return;
         } else {
            char color = split[2].charAt(0);
            message = " text set to " + color;
            if (split[1].equalsIgnoreCase("normal")) {
               textcolornormal = color;
               message = "Normal" + message;
            } else if (split[1].equalsIgnoreCase("error")) {
               textcolorerror = color;
               message = "Error" + message;
            } else if (split[1].equalsIgnoreCase("setrandom")) {
               textcolorrandom = split[2];
               message = "Random colors set to " + textcolorrandom;
            } else {
               sendError(ERRMSG_PARSE);
               return;
            }
         }
         sendMessage(message);
         saveSettings();

         /*
          * Sfly - turns standard fly mode on
          */
      } else if (split[0].equalsIgnoreCase("flymode")) {
         if (split.length != 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("standard")) {
            flymode = "standard";
            //sfly = !sfly;
         } else if (split[1].equalsIgnoreCase("dynamic") || split[1].equalsIgnoreCase("reset")) {
            flymode = "dynamic";
         } else if (split[1].equalsIgnoreCase("minecraft")) {
            flymode = "minecraft";
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }
         sendMessage(flymode + " fly mode enabled. Use /fly to turn fly on/off");
         saveSettings();

         /*
          * Repeats the last used command
          */
      } else if (split[0].equalsIgnoreCase("repeat")) {
         if (lastcommand == null) {
            sendError("Previous command cannot be repeated.");
            return;
         }
         ep.sendChatMessage(lastcommand);

      } else if (split[0].equalsIgnoreCase("stacklimit")) {
         if (split.length >= 2) {
            if (split[1].equalsIgnoreCase("on")) {
               limitstack = true;
            } else if (split[1].equalsIgnoreCase("off")) {
               limitstack = false;
            } else {
               sendError(ERRMSG_PARAM);
               return;
            }
         } else {
            limitstack = !limitstack;
         }
         sendMessage("Stack size limited turned " + (limitstack ? "on" : "off"));

         /*
          * Combines all of the same type of stack in the inventory into one
          */
      } else if (split[0].equalsIgnoreCase("stackcombine")) {
         for (int i = 0; i < ep.inventory.mainInventory.length; i++) {
            ItemStack sloti = ep.inventory.mainInventory[i];
            if (sloti == null) {
               continue;
            }
            for (int j = i + 1; j < ep.inventory.mainInventory.length; j++) {
               ItemStack slotj = ep.inventory.mainInventory[j];
               if (slotj == null) {
                  continue;
               }
               if (isItemEqual(sloti,slotj)) {
                  sloti.stackSize += slotj.stackSize;
                  ep.inventory.mainInventory[j] = null;
               }
            }
         }

         /*
          * Sets the current gamemode (survival or creative)
          */
      } else if (split[0].equalsIgnoreCase(/*"gamemode"*/"gamemode2")) {
         EnumGameType mode = null;
         if (split.length < 2) {
            mode = (mc.theWorld.worldInfo.getGameType() == EnumGameType.SURVIVAL || mc.theWorld.worldInfo.getGameType() == EnumGameType.ADVENTURE) ? EnumGameType.CREATIVE : EnumGameType.SURVIVAL;
         } else if (split[1].equalsIgnoreCase("survival") || split[1].equalsIgnoreCase("0")) {
            mode = EnumGameType.CREATIVE;
         } else if (split[1].equalsIgnoreCase("creative") || split[1].equalsIgnoreCase("1")) {
            mode = EnumGameType.SURVIVAL;
         } else if (split[1].equalsIgnoreCase("adventure") || split[1].equalsIgnoreCase("2")) {
            mode = EnumGameType.ADVENTURE;
         }

         if (mode == null) {
            sendError(ERRMSG_PARSE);
            return;
         }

         mc.setController(mode);
         mc.setGameMode(mode);

         mc.theWorld.getWorldInfo().setGameType(mode);

         sendMessage("Game mode changed to " + mode.getName());

         /*
          * 
          */
         /*} else if (split[0].equalsIgnoreCase("worldinfo")) {
         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }

         ArrayList players = new ArrayList();
         players.add(ep);
         NBTTagCompound nbt = mc.theWorld.worldInfo.getNBTTagCompoundWithPlayer(players);
         nbt.setString(split[1],split[2]);

         WorldInfo w = new WorldInfo(nbt);
         mc.theWorld.worldInfo = w;

         sendMessage("WorldInfo setting " + split[1] + " set as " + split[2]);*/

         /*
          * Sets the players hunger level appropriately
          */
      } else if (split[0].equalsIgnoreCase("hunger")) {
         int food = 0;
         if (split.length == 1) {
            disableHunger = !disableHunger;
            saveSettings();
            sendMessage("Hunger was " + (disableHunger ? "disabled" : "enabled"));
            return;
         } else if (split[1].equalsIgnoreCase("empty")) {
            food = 0;
         } else if (split[1].equalsIgnoreCase("full")) {
            food = 20;
         } else if (split[1].equalsIgnoreCase("inf") || split[1].equalsIgnoreCase("infinite")) {
            food = Integer.MAX_VALUE;
         } else if (split[1].equalsIgnoreCase("enable")) {
            disableHunger = false;
            saveSettings();
            sendMessage("Hunger was enabled");
            return;
         } else if (split[1].equalsIgnoreCase("disable")) {
            disableHunger = true;
            saveSettings();
            sendMessage("Hunger was disabled");
            return;
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }
         ep.foodStats.setFoodLevel(food); 
         sendMessage("Hunger level set to " + split[1] + "(" + food + ")");

         /*
          * Feeds the player the specified ammount
          */
      } else if (split[0].equalsIgnoreCase("feed")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         int food = 0;
         try {
            food = Integer.parseInt(split[1]);
         } catch (Exception e) {
            sendError(ERRMSG_PARSE);
            return;
         }
         ep.foodStats.setFoodLevel(ep.foodStats.getFoodLevel() + food);
         sendMessage("Food level set to " + ep.foodStats.getFoodLevel());

         /*
          * Toggles hardcore mode on/off
          */
      } else if (split[0].equalsIgnoreCase("hardcore")) {
         NBTTagCompound n = mc.theWorld.worldInfo.getNBTTagCompound();
         if (split.length < 2) {
            n.setBoolean("hardcore", !mc.theWorld.worldInfo.isHardcoreModeEnabled());
         } else if (split[1].equalsIgnoreCase("enable")) {
            n.setBoolean("hardcore", true);
         } else if (split[1].equalsIgnoreCase("disable")) {
            n.setBoolean("hardcore", false);
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }

         mc.theWorld.worldInfo = new WorldInfo(n);
         if (mc.theWorld.worldInfo.isHardcoreModeEnabled()) {
            sendMessage("Hardcore mode enabled! Don't die!");
         } else {
            sendMessage("Hardcore mode disabled, chicken.");
         }

         /*
          * Allows the player to control their xp
          */
      } else if (split[0].equalsIgnoreCase(/*"xp"*/"xp2")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         if (split[1].equalsIgnoreCase("get")) {
            int percentage = (int)(ep.experience * 100);
            sendMessage("Current player XP is " + ep.experienceTotal + " at level " + ep.experienceLevel + ", " + percentage + "% to next level.");
            return;
         }

         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }
         int value = 0;
         try {
            value = Integer.parseInt(split[2]);
         } catch (Exception e) {
            sendError(ERRMSG_PARSE);
            return;
         }
         if (split[1].equalsIgnoreCase("add")) {
            int alt = value;
            while (alt > 0) {
               alt -= ep.xpBarCap();
               ep.addExperience(alt < 0 ? ep.xpBarCap() + alt : ep.xpBarCap());
            }
            sendMessage(value + " xp was added to your experience");
         } else if (split[1].equalsIgnoreCase("set")) {
            ep.experience = 0;
            ep.experienceTotal = 0;
            ep.experienceLevel = 0;
            int alt = value;
            while (alt > 0) {
               alt -= ep.xpBarCap();
               ep.addExperience(alt < 0 ? ep.xpBarCap() + alt : ep.xpBarCap());
            }
            sendMessage("Your experience was set at " + value);
         } else if (split[1].equalsIgnoreCase("give")) {
            int qty = 1;
            if (split.length > 3) {
               try {
                  qty = Integer.parseInt(split[3]);
               } catch (Exception e) {
                  sendError(ERRMSG_PARSE);
                  return;
               }
            }
            Random r = new Random();
            for (int i = 0; i < qty; i++) {
               mc.theWorld.spawnEntityInWorld(new EntityXPOrb(mc.theWorld, ep.posX + (r.nextInt(6) - 3), ep.posY + r.nextInt(3), ep.posZ + (r.nextInt(6) - 3), value));
            }
         } else if (split[1].equalsIgnoreCase("level")) {
            ep.experienceLevel = value;
            sendMessage("Your experience level was set at " + value);
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }

         /*
          * Allows the player to get a list of enchantments, remove them and add
          * them to their items
          */
      } else if (split[0].equalsIgnoreCase("enchant")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("list")) {
            String list = "";
            for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
               if (Enchantment.enchantmentsList[i] != null) {
                  list += StatCollector.translateToLocal(Enchantment.enchantmentsList[i].getName()).replace(' ', '_') + " (" + i + "), ";
               }
            }
            sendMessage("Enchantments [name (id)]:");
            sendMessage(list);
            return;
         } 
         if (ep.inventory.mainInventory[ep.inventory.currentItem] == null) {
            sendError("No currently selected item");
            return;
         } else if (ep.inventory.mainInventory[ep.inventory.currentItem].isStackable()) {
            sendError("This item cannot be enchanted");
            return;
         }
         if (split[1].equalsIgnoreCase("remove")) { 
            if (ep.inventory.mainInventory[ep.inventory.currentItem].isItemEnchanted()) {
               ep.inventory.mainInventory[ep.inventory.currentItem].stackTagCompound = null;
               sendMessage("Enchantment(s) for the current item were removed");
            } else {
               sendMessage("Current item doesn't have any enchantments");
            }
            return;
         }
         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("add")) { 
            // Sets the current item at the specified enchantment level
            int id = -1;
            Enchantment e = null;
            try {
               id = Integer.parseInt(split[2]);
               e = Enchantment.enchantmentsList[id];
            } catch (NumberFormatException nfe) {
            } catch (Exception wrongid) {
               sendError("Unknown enchantment id " + id);
               return;
            }
            for (int i = 0; i < Enchantment.enchantmentsList.length; i++) {
               if (Enchantment.enchantmentsList[i] != null) {
                  if (split[2].equalsIgnoreCase(StatCollector.translateToLocal(Enchantment.enchantmentsList[i].getName()).replace(' ', '_'))) {
                     e = Enchantment.enchantmentsList[i];
                  }
               }
            }
            if (e == null) {
               sendError("Could not find specified enchantment: " + split[2]);
               return;
            }
            int level = 0;
            if (split.length == 4) {
               try {
                  level = Integer.parseInt(split[3]);
               } catch (Exception ex) {
                  sendError(ERRMSG_PARSE);
                  return;
               }
               if (level > 127) {
                  sendMessage("Maximum level of 127 was set");
               } else if (level < -128) {
                  sendMessage("Minimum level of -128 was set");
               }
            }
            ep.inventory.mainInventory[ep.inventory.currentItem].addEnchantment(e, level);
            sendMessage("The " + e.getTranslatedName(level) + " enchantment was added to the current item.");
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }

         /*
          * Toggles the enderman picking up blocks
          */
      } else if (split[0].equalsIgnoreCase("enderman")) {
         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("pickup")) {
            Field f[] = EntityEnderman.class.getDeclaredFields();
            boolean enable = split[2].equalsIgnoreCase("enable");
            int num = 0;
            for (Field field : f) {
               field.setAccessible(true);
               Object o = null;
               if ((o = field.get(null)) instanceof boolean[]) {
                  boolean blocks[] = (boolean[])o;
                  blocks[Block.grass.blockID] = enable;
                  blocks[Block.dirt.blockID] = enable;
                  blocks[Block.sand.blockID] = enable;
                  blocks[Block.gravel.blockID] = enable;
                  blocks[Block.plantYellow.blockID] = enable;
                  blocks[Block.plantRed.blockID] = enable;
                  blocks[Block.mushroomBrown.blockID] = enable;
                  blocks[Block.mushroomRed.blockID] = enable;
                  blocks[Block.tnt.blockID] = enable;
                  blocks[Block.cactus.blockID] = enable;
                  blocks[Block.blockClay.blockID] = enable;
                  blocks[Block.pumpkin.blockID] = enable;
                  blocks[Block.melon.blockID] = enable;
                  blocks[Block.mycelium.blockID] = enable;
                  if (num == 1){
                    blocks[Block.stone.blockID] = enable;
                    blocks[Block.cobblestone.blockID] = enable;
                    blocks[Block.planks.blockID] = enable;
                    blocks[Block.oreGold.blockID] = enable;
                    blocks[Block.oreIron.blockID] = enable;
                    blocks[Block.oreCoal.blockID] = enable;
                    blocks[Block.wood.blockID] = enable;
                    blocks[Block.leaves.blockID] = enable;
                    blocks[Block.sponge.blockID] = enable;
                    blocks[Block.glass.blockID] = enable;
                    blocks[Block.oreLapis.blockID] = enable;
                    blocks[Block.blockLapis.blockID] = enable;
                    blocks[Block.sandStone.blockID] = enable;
                    blocks[Block.cloth.blockID] = enable;
                    blocks[Block.blockGold.blockID] = enable;
                    blocks[Block.blockSteel.blockID] = enable;
                    blocks[Block.brick.blockID] = enable;
                    blocks[Block.bookShelf.blockID] = enable;
                    blocks[Block.cobblestoneMossy.blockID] = enable;
                    blocks[Block.oreDiamond.blockID] = enable;
                    blocks[Block.blockDiamond.blockID] = enable;
                    blocks[Block.oreEmerald.blockID] = enable;
                    blocks[Block.blockEmerald.blockID] = enable;
                    blocks[Block.workbench.blockID] = enable;
                    blocks[Block.oreRedstone.blockID] = enable;
                    blocks[Block.oreRedstoneGlowing.blockID] = enable;
                    blocks[Block.ice.blockID] = enable;
                    blocks[Block.netherrack.blockID] = enable;
                    blocks[Block.slowSand.blockID] = enable;
                    blocks[Block.glowStone.blockID] = enable;
                    blocks[Block.pumpkinLantern.blockID] = enable;
                    blocks[Block.stoneBrick.blockID] = enable;
                    blocks[Block.mushroomCapBrown.blockID] = enable;
                    blocks[Block.mushroomCapRed.blockID] = enable;
                  }
                  field.set(null, blocks);
                  if (num >= 1){
                      break;
                  }
                  num++;
               }
            }
            sendMessage("Enderman pickup " + (enable ? "enabled" : "disabled"));
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }

         /*
          * Fog command - toggles the render distance
          */
      } else if (split[0].equalsIgnoreCase("fog")) {
         if (split.length == 1) {
            mc.gameSettings.setOptionValue(EnumOptions.RENDER_DISTANCE, 3);
            System.out.println(mc.gameSettings.renderDistance);
         } else if (split[1].equalsIgnoreCase("tiny")) {
            mc.gameSettings.renderDistance = 3;
         } else if (split[1].equalsIgnoreCase("small")) {
            mc.gameSettings.renderDistance = 2;
         } else if (split[1].equalsIgnoreCase("normal")) {
            mc.gameSettings.renderDistance = 1;
         } else if (split[1].equalsIgnoreCase("far")) {
            mc.gameSettings.renderDistance = 0;
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }

         /*
          * Adds potion effects to the player
          */
      } else if (split[0].equalsIgnoreCase("effect")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         if (split[1].equalsIgnoreCase("list")) {
            String list = "";
            for (int i = 0; i < Potion.potionTypes.length; i++) {
               if (Potion.potionTypes[i] != null) {
                  list += StatCollector.translateToLocal(Potion.potionTypes[i].getName()).replace(' ', '_') + " (" + i + "), ";
               }
            }
            sendMessage("Potion effects [name (id)]: ");
            sendMessage(list);
            return;
         }

         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("add") || split[1].equalsIgnoreCase("remove")) {
            if (split[1].equalsIgnoreCase("remove") && split[2].equalsIgnoreCase("all")) {
               ep.activePotionsMap.clear();
               ep.onNewPotionEffect(null);
               sendMessage("All potion effects removed.");
               return;
            }
            int type = -1;
            Potion p = null;
            try {
               type = Integer.parseInt(split[2]);
               p = Potion.potionTypes[type];
            } catch (NumberFormatException e) {  
            } catch (Exception e) {
               sendError("Could not find specified effect id " + type);
            }
            if (p == null) {
               for (int i = 0; i < Potion.potionTypes.length; i++) {
                  if (Potion.potionTypes[i] != null) {
                     if (split[2].equalsIgnoreCase(StatCollector.translateToLocal(Potion.potionTypes[i].getName()).replace(' ', '_'))) {
                        p = Potion.potionTypes[i];
                        break;
                     }
                  }
               }
            }
            if (p == null) {
               sendError("Could not find specified effect: " + split[2]);
               return;
            }
            if (split[1].equalsIgnoreCase("remove")) {
               ep.removePotionEffect(p.id);
               ep.onNewPotionEffect(null);
               sendMessage("Effect " + StatCollector.translateToLocal(Potion.potionTypes[p.id].getName()) + " was disabled");
               return;
            }

            int duration = 1;
            int effect = 1;
            if (split.length >= 4) {
               try {
                  duration = Integer.parseInt(split[3]);
               } catch (Exception e) {
                  sendError(ERRMSG_PARSE);
                  return;
               }
               if (split.length == 5) {
                  try {
                     effect = Integer.parseInt(split[4]);
                  } catch (Exception e) {
                     sendError(ERRMSG_PARSE);
                     return;
                  }
               }
            }
            PotionEffect pe = new PotionEffect(p.id,duration,effect);
            ep.addPotionEffect(pe);
            p.performEffect(ep, duration);
            sendMessage("Effect " + StatCollector.translateToLocal(Potion.potionTypes[p.id].getName()) + " enabled for " + duration + " at strength " + effect);
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }

         /*
          * Allows you to configure the mob that spawns from a mob spawner
          */
      } else if (split[0].equalsIgnoreCase("spawner")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }

         SPCObjectHit soh = this.getObjectHit();
         if (soh == null) {
            sendError("Your cursor needs to be hitting a block");
            return;
         }

         String name = "";
         try {
            name = getEntityName(Integer.parseInt(split[1]));
         } catch (Exception e) {
            name = split[1];
         }

         if (getEntity2(name) == null) {
            if (getEntity(name) != null){
                sendError("Spawner mob names are case sensitive");
            }else{
                sendError("Invalid mob name, please try again");
            }
            return;
         }

         mc.theWorld.setBlockWithNotify(soh.blockx, soh.blocky, soh.blockz,Block.mobSpawner.blockID);

         TileEntity te = mc.theWorld.getBlockTileEntity(soh.blockx, soh.blocky, soh.blockz);
         if (te != null && te instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner)te).setMobID(name);
         }

         /*
          * Turns climbing mode on/off
          */
      } else if (split[0].equalsIgnoreCase("climb")) {
         if (split.length < 2) {
            ladderMode = !ladderMode;
         } else if (split[1].equalsIgnoreCase("enable")) {
            ladderMode = true;
         } else if (split[1].equalsIgnoreCase("disable")) {
            ladderMode = true;
         }
         saveSettings();
         sendMessage("Climb mode is " + (ladderMode ? "enabled" : "disabled"));

         /*
          * Turns player sprinting on/off
          */
      } else if (split[0].equalsIgnoreCase("sprinting")) {
         if (split.length < 2) {
            sprinting = !sprinting;
         } else if (split[1].equalsIgnoreCase("enable")) {
            sprinting = true;
         } else if (split[1].equalsIgnoreCase("disable")) {
            sprinting = false;
         }
         saveSettings();
         sendMessage("Sprinting mode is " + (sprinting ? "enabled" : "disabled"));

      } else if (split[0].equalsIgnoreCase("calc") || split[0].equalsIgnoreCase("=")) {
         double total = 0;
         int lastoperator = -1;
         boolean f = false;
         for (int i = 1; i < split.length; i++) {
            if (!f && lastoperator == -1) {
               try {
                  total = Double.parseDouble(split[i]);
                  f = true;
               } catch (Exception e) {
                  sendError(ERRMSG_PARSE);
                  return;
               }
            } else if (lastoperator == -1) {
               if (split[i].equalsIgnoreCase("+")) {
                  lastoperator = 0;
               } else if (split[i].equalsIgnoreCase("-")) {
                  lastoperator = 1;
               } else if (split[i].equalsIgnoreCase("*")) {
                  lastoperator = 2;
               } else if (split[i].equalsIgnoreCase("/")) {
                  lastoperator = 3;
               }
            } else {
               try {
                  double second = Double.parseDouble(split[i]);
                  switch (lastoperator) {
                  case 0:
                     total += second;
                     break;
                  case 1:
                     total -= second;
                     break;
                  case 2: 
                     total *= second;
                     break;
                  case 3:
                     total /= second;
                     break;
                  default:
                     sendError(ERRMSG_PARSE);
                     return;
                  }
                  lastoperator = -1;
               } catch (Exception e) {
                  sendError(ERRMSG_PARSE);
                  return;
               }
            }
         }
         sendMessage("" + total);

         /*
          * Enables/disables critical hit only mode
          */
      } else if (split[0].equalsIgnoreCase("criticalhit")) {
         if (split.length == 1) {
            criticalHit = !criticalHit;
         } else if (split[1].equalsIgnoreCase("enable")) {
            criticalHit = true;
         } else if (split[1].equalsIgnoreCase("disable")) {
            criticalHit = false;
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }
         saveSettings();
         sendMessage(criticalHit ? "Every hit is a critical hit!" : "Critical hit mode disabled");

         /*
          * Allows you to toggle clouds on/off
          */
      } else if (split[0].equalsIgnoreCase("clouds")) {
         if (split.length == 1) {
            mc.gameSettings.clouds = !mc.gameSettings.clouds;
            //mc.gameSettings.field_40445_l = !mc.gameSettings.field_40445_l;
         } else if (split[1].equalsIgnoreCase("enable")) {
            mc.gameSettings.clouds = true;
            //mc.gameSettings.field_40445_l = true;
         } else if (split[1].equalsIgnoreCase("disable")) {
            mc.gameSettings.clouds = false;
            //mc.gameSettings.field_40445_l = false;
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }
         sendMessage("Clouds are now " + (mc.gameSettings.clouds ? "enabled" : "disabled"));
         //sendMessage("Clouds are now " + (mc.gameSettings.field_40445_l ? "enabled" : "disabled"));

      } else if (split[0].equalsIgnoreCase("size")) {//size, reset TODO: eewe
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("reset")) {
            playerSize = 2;
            sendMessage("Player size was reset");
         } else {
            try {
               playerSize = Double.parseDouble(split[1]);

               ep.boundingBox.contract(1, 1, 1);

               System.out.println(ep.boundingBox.minX + " " + 
                     ep.boundingBox.minY  + " " + 
                     ep.boundingBox.minZ  + " " + 
                     ep.boundingBox.maxX + " " + 
                     ep.boundingBox.maxY  + " " + 
                     ep.boundingBox.maxZ
               );

               sendMessage("Player size set to " + split[1]);
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
            }
         }

         /*
          * Rotates the inventory around
          */
      } else if (split[0].equalsIgnoreCase("invrotate")) {
         int items = 9;
         boolean leftToRight = true;
         if (split.length >= 2) {
            if (split[1].equalsIgnoreCase("item")) {
               items = 1;
            } else if (split[1].equalsIgnoreCase("line")) {
               items  = 9;
            } else {
               try {
                  items = Integer.parseInt(split[1]);
               } catch (Exception e) {
                  sendError(ERRMSG_PARSE);
                  return;
               }
            }
         }
         if (split.length >= 3) {
            if (split[2].equalsIgnoreCase("left")) {
               leftToRight = false;
            } else if (split[2].equalsIgnoreCase("right")) {
               leftToRight = true;
            } else {
               sendError(ERRMSG_PARSE);
               return;
            }
         }
         ItemStack main[] = ep.inventory.mainInventory;
         ItemStack sorted[] = new ItemStack[main.length];
         items = items % main.length;
         for (int i = 0; i < main.length; i++) {
            int pos = 0;
            if (leftToRight) {
               pos = i + items;
            } else {
               pos = i - items;
            }
            pos = pos < 0 ? main.length + pos : pos;
            sorted[pos % main.length] = main[i];
         }

         ep.inventory.mainInventory = sorted;
         main = null;

         /*
          * Enables the user to store their current inventory to file
          */
      } else if (split[0].equalsIgnoreCase("invstore")) {
         if (split.length == 2 && split[1].equalsIgnoreCase("list")) {
            Settings inv = new Settings(new File(MODDIR,"inventory.properties"));
            Iterator i = inv.keySet().iterator();
            String saves = "";
            while (i.hasNext()) {
               saves += i.next() + ", ";
            }
            sendMessage("Stored inventories: ");
            sendMessage(saves);
            return;
         }

         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }
         // Format: ITEMID:QUANTITY:DAMAGE,...
         Settings inv = new Settings(new File(MODDIR,"inventory.properties"));
         if (split[1].equalsIgnoreCase("load")) {
            String load = inv.getString(split[2], null);
            if (load == null) {
               sendError("Could not find specified inventory to load");
               return;
            }
            String inventory[] = load.split(",");
            ItemStack main[] = ep.inventory.mainInventory;
            int errors = 0;
            for (int i = 0; i < inventory.length && i < main.length; i++) {
               String item[] = inventory[i].split(":");
               if (item.length != 3) {
                  main[i] = null;
                  continue;
               }
               try {
                  main[i] = new ItemStack(Integer.parseInt(item[0]),Integer.parseInt(item[1]),Integer.parseInt(item[2]));
               } catch (Exception e) {
                  main[i] = null;
                  errors++;
               }
            }
            sendMessage("The " + split[2] + " inventory was loaded" + (errors != 0 ? " with " + errors + " errors." : "."));
         } else if (split[1].equalsIgnoreCase("save")) {
            String inventory = "";
            for (ItemStack item : ep.inventory.mainInventory) {
               if (item == null) {
                  inventory += ",";
               } else {
                  inventory += item.itemID + ":" + item.stackSize + ":" + item.getItemDamage() + ",";
               }
            }
            inv.set(split[2], inventory);
            if (inv.save()) {
               sendMessage("Inventory " + split[2] + " was saved.");
            } else {
               sendError("Could not save inventory " + split[2] + ", may not have permission to write file.");
            }            
         } else {
            sendError(ERRMSG_PARSE);
         }

         /*
          * Turns creeper explosions on/off
          */
      } else if (split[0].equalsIgnoreCase("creeper")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         if (split[1].equalsIgnoreCase("explosion")) {
            if (split.length == 3 && split[2].equalsIgnoreCase("enable")) {
               creeperExplosion = true;
            } else if (split.length == 3 && split[2].equalsIgnoreCase("disable")) {
               creeperExplosion = false;
            } else {
               creeperExplosion = !creeperExplosion;
            }
            sendMessage("Creeper explosions were " + (creeperExplosion ? "enabled" : "disabled") + ".");
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }
         saveSettings();

         /*
          * Gives the player a maximum stack of the block they are looking at
          */
      } else if (split[0].equalsIgnoreCase("pick")) {
         MovingObjectPosition m = ep.rayTrace(reachdistance, 1.0F);
         if (m == null) {
            return;
         }
         int blockid = mc.theWorld.getBlockId(m.blockX, m.blockY, m.blockZ);
         int metadata = mc.theWorld.getBlockMetadata(m.blockX, m.blockY, m.blockZ);
         if (blockid == 0 || Block.blocksList[blockid] == null) {
            sendError("Block can not be given.");
            return;
         }
         int quantity = 64;
         if (split.length == 2) {
            try {
               quantity = Integer.parseInt(split[1]);
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
         }
         givePlayerItem(new ItemStack(blockid,quantity,metadata));

      } else if (split[0].equalsIgnoreCase("binditem")) {
         // Binds an command to a mouse button on the specified item /binditem <BUTTON {COMMAND}>
         // BUTTON is either an ID or one of 'left','right','middle'
         // File format: button,id,damage=command
         if (split.length < 3) {
            sendError(ERRMSG_PARAM);
            return;
         }
         int id = -1;
         if (split[1].equalsIgnoreCase("left")) {
            id = 0;
         } else if (split[1].equalsIgnoreCase("right")) {
            id = 1;
         } else if (split[1].equalsIgnoreCase("middle")) {
            id = 2;
         } else {
            try {
               id = Integer.parseInt(split[1]);
            } catch (Exception e) {
               sendError(ERRMSG_PARSE);
               return;
            }
         }
         ItemStack currentitem = ep.inventory.getCurrentItem();
         int itemid = currentitem == null ? 0 : currentitem.itemID;
         int damage = currentitem == null || currentitem.isItemStackDamageable() ? 0 : currentitem.getItemDamage();
         String command = join(split, 2, split.length);
         if (itemsbound == null) {
            loadBindings();
         }
         itemsbound.set(id + "," + itemid + "," + damage, command);
         sendMessage("Item binding was successfully set.");
         if (!itemsbound.save()) {
            sendMessage("A problem occurred saving item bindings to file. May not have write privledges");
         }

         /*
          * Allows item bindings to be unbound
          */
      } else if (split[0].equalsIgnoreCase("unbinditem")) {
         if (split.length == 1) { // unbind current item bindings
            Iterator i = itemsbound.keySet().iterator();
            List<String> keys = new Vector<String>();
            while (i.hasNext()) {
               keys.add((String)i.next());
            }
            for (String key : keys) {
               try {
                  String binding[] = ((String)key).split(",");
                  ItemStack currentitem = ep.inventory.getCurrentItem();
                  int itemid = currentitem == null ? 0 : currentitem.itemID;
                  int damage = currentitem == null || currentitem.isItemStackDamageable() ? 0 : currentitem.getItemDamage();
                  if (binding[1].equalsIgnoreCase(itemid + "") && binding[2].equalsIgnoreCase(damage + "")) {
                     itemsbound.remove(key);
                  }
               } catch (Exception e) {
                  itemsbound.remove(key);
               }
            }
            sendMessage("All bindings successfully removed from the current item.");
         } else if (split.length == 2 && split[1].equalsIgnoreCase("all")) {
            itemsbound.clear();
            sendMessage("All bindings for all items were removed.");
         }
         if (!itemsbound.save()) {
            sendMessage("A problem occurred saving item bindings to file. May not have write privledges");
         }

         /*
          * Allows placing and editing of signs without a GUI", "<add|edit> [\"LINE1\"] [\"LINE2\"] [\"LINE3\"] [\"LINE4\"]"
          */
      } else if (split[0].equalsIgnoreCase("sign")) {
         if (split.length < 2) {
            sendError(ERRMSG_PARAM);
            return;
         }
         MovingObjectPosition block = ep.rayTrace(reachdistance,1.0F);
         if (block == null) {
            sendError("No block within range.");
            return;
         }
         String line = join(split,2,split.length);
         Pattern p = Pattern.compile("(\"(.*?)\")");
         //Pattern p = Pattern.compile("(?:\\s*)(?<=[-|/])(?<name>\\w*)[:|=](\"((?<value>.*?)(?<!\\\\)\")|(?<value>[\\w]*))");
         //Pattern p = Pattern.compile("(?<=[-{1,2}|/])([a-zA-Z0-9]*)[ |:|\"]*([\\w|.|?|=|&|+| |:|/|\\\\]*)(?=[ |\"]|$)");
         Matcher m = p.matcher(line);
         String lines[] = new String[]{"","","",""};
         int linelength = line.replaceAll(" ","").length();
         int newlength = 0;
         for (int i = 0; i < lines.length && m.find(); i++) {
            String temp = m.group();
            if (temp != null) {
               newlength += temp.replaceAll(" ","").length();
               if (temp.startsWith("\"") && temp.endsWith("\"")) {
                  lines[i] = replaceStringWithColours(temp.substring(1, temp.length() - 1));
               }
            }
         }

         if (newlength != linelength) {
            sendError("Could not parse input, \"quote\" each of the lines");
            return;
         }

         if (split[1].equalsIgnoreCase("add")) {
            int face = block.sideHit;
            int i = block.blockX;
            int j = block.blockY;
            int k = block.blockZ;
            if(face == 0) {
               sendError("You cannot place a sign here.");
               return;
            }
            if(face == 1) {
               j++;
            }
            if(face == 2) {
               k--;
            }
            if(face == 3) {
               k++;
            }
            if(face == 4) {
               i--;
            }
            if(face == 5) {
               i++;
            }
            if(!Block.signPost.canPlaceBlockAt(mc.theWorld, i, j, k)) {
               sendError("You cannot place a sign here.");
               return;
            }
            if (!placeSign(lines,i,j,k,face)) {
               sendError("Could not add the specified sign.");
            }
            /*if(face == 1) {
               int i1 = MathHelper.floor_double((double)(((ep.rotationYaw + 180F) * 16F) / 360F) + 0.5D) & 0xf;
               mc.theWorld.setBlockAndMetadataWithNotify(i, j, k, Block.signPost.blockID, i1);
            } else {
               mc.theWorld.setBlockAndMetadataWithNotify(i, j, k, Block.signWall.blockID, face);
            }
            try {
               TileEntitySign sign = (TileEntitySign)mc.theWorld.getBlockTileEntity(i,j,k);
               sign.signText = lines;
            } catch (Exception e) {
               sendError("Could not add the specified sign.");
            }*/
         } else if (split[1].equalsIgnoreCase("edit")) {
            int id = mc.theWorld.getBlockId(block.blockX, block.blockY, block.blockZ);
            if (id != Block.signPost.blockID && id != Block.signWall.blockID) {
               sendError("You need to be pointing at an existing sign to edit it");
               return;
            }
            try {
               TileEntitySign sign = (TileEntitySign)mc.theWorld.getBlockTileEntity(block.blockX, block.blockY, block.blockZ);
               sign.signText = lines;
            } catch (Exception e) {
               sendError("Could not edit the specified sign.");
            }
         } else {
            sendError(ERRMSG_PARSE);
            return;
         }

         /*
          * Lights the block above the one you're pointing at on fire
          */
      } else if (split[0].equalsIgnoreCase("ignite")) {

         SPCObjectHit spc = this.getObjectHit();
         if (spc == null || spc.blocky == -1) {
            return;
         }

         if (mc.theWorld.getBlockId(spc.blockx, spc.blocky + 1, spc.blockz) == 0) { 
            mc.theWorld.setBlockWithNotify(spc.blockx, spc.blocky + 1, spc.blockz, Block.fire.blockID);
         }

//       } else if (split[0].equalsIgnoreCase("test")) {
         
         //com.sijobe.spc.command.CommandHandler.reload();
         //com.sijobe.spc.util.DynamicClassLoader.getClasses(SPCPlugin.class);
         //com.sijobe.spc.util.DynamicClassLoader.loadClasses();

         /*mc.currentScreen = null;
         mc.displayGuiScreen(new GuiWinGame());*/

         //mc.displayGuiScreen(GuiConsole.getInstance());

         /*for (Block b : Block.blocksList) {
            if (b != null)
               b.blockIndexInTexture = 180;
         }
         mc.renderGlobal.updateRenderers(ep, true);//TODO: XRAY*/
         //mc.displayGuiScreen(GuiConsole.getInstance());

         /*File parent = new File(mc.getMinecraftDir(),"saves");
         World newworld = new World(new SaveHandler(parent, split[1], false),split[1], new WorldSettings(new Random().nextLong(),0,true, false));
         newworld.field_35473_a = Integer.parseInt(split[2]);
         newworld.field_35471_b = newworld.field_35473_a + 4;
         newworld.field_35472_c = 1 << newworld.field_35473_a;
         newworld.field_35469_d = newworld.field_35472_c - 1;
         newworld.field_35470_e = newworld.field_35472_c / 2 - 1;

         mc.changeWorld2(newworld, "Updating height");*/

         //mc.theWorld.field_35472_c = Integer.parseInt(split[1]);
         //mc.theWorld.worldProvider.hasNoSky=false;

//       } else if (split[0].equalsIgnoreCase("test2")) {

         /*MapData mapdata = Item.map.getMapData(ep.inventory.mainInventory[ep.inventory.currentItem], mc.theWorld);
         double x = ep.posX;
         double y = ep.posY;
         double z = ep.posZ;
         for (int i = -512; i < 512; i+=32) {
            for (int j = -512; j < 512; j+=32) {
               ep.setPosition(mapdata.xCenter + i, 127, mapdata.zCenter + j);
               Item.map.updateMapData(mc.theWorld, ep, mapdata);
            }
         }
         ep.setPosition(x,y,z);*/

         /* Benchmark
          * long before = System.currentTimeMillis();
         for (int i = 0; i < 100; i++) {
            VideoFrame.captureFrame(mc.displayWidth, mc.displayHeight);
         }
         long after = System.currentTimeMillis();
         sendMessage((after - before) + " ");*/


         /*System.out.println("FLAG " + split[1] + " " + split[2]);
         ep.setFlag(new Integer(split[1]), new Boolean(split[2]));*/

         /*for (int i = 0; i < ep.inventory.mainInventory.length; i++) {
            ItemStack in = ep.inventory.mainInventory[i];
            if (in != null) {
               int size = in.stackSize;
               for (int j = 0; j < size; j++) {
                  ep.dropCurrentItem();
                  //ep.dropPlayerItem(ep.inventory.decrStackSize(i, 1));
                  Thread.sleep(10);
               }
               ep.inventory.currentItem = i+1;
            }
         }*/

         /*
          * Help - provides help messages TODO: Just a placeholder - makes it easier to find bottom
          * of commands
          */
      } else if (/*split[0].equalsIgnoreCase("help") || */split[0].equalsIgnoreCase("h")) {
         if (split.length < 2) {
            String commands = "";
            SortedSet<String> sortedset = new TreeSet<String>(this.commands.keySet());
            Iterator<String> i = sortedset.iterator();
            while (i.hasNext()) {
               String key = i.next();
               if (key.startsWith("/")) {
                  continue;
               }
               String value = this.commands.get(key);
               if (i.hasNext()) {
                  commands += value + ", ";
               } else {
                  commands += value;
               }
            }
            sendMessage("Commands:");
            sendMessage(commands);
         } else if (commands.containsKey(split[1])) {
            String key = commands.get(split[1]);
            if (key == null) {
               return;
            }
            String msg[] = CMDS.get(key);
            if ((msg == null) || (msg.length != 3)) {
               return;
            }
            helpMessage(msg[0], split[1] + " " + msg[1], "/" + split[1] + " " + msg[2]);
         }
      }
   }
   
   public boolean placeSign(String lines[], int i, int j, int k, int face) {
      if(face == 1) {
         int i1 = MathHelper.floor_double((double)(((ep.rotationYaw + 180F) * 16F) / 360F) + 0.5D) & 0xf;
         mc.theWorld.setBlockAndMetadataWithNotify(i, j, k, Block.signPost.blockID, i1);
      } else {
         mc.theWorld.setBlockAndMetadataWithNotify(i, j, k, Block.signWall.blockID, face);
      }
      try {
         TileEntitySign sign = (TileEntitySign)mc.theWorld.getBlockTileEntity(i,j,k);
         sign.signText = lines;
      } catch (Exception e) {
         return false;
      }
      return true;
   }

   /**
    * Replaces &[0-f] with the applicable colour
    * 
    * @param raw - The raw string to get replaced
    * @return A colour formatted string
    */
   public String replaceStringWithColours(String raw) {
      if (raw == null || raw.length() == 0) {
         return "";
      }
      String temp = raw;
      char colours[] = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f','k'};
      Random r = new Random();
      for (int i = 0; i < colours.length; i++) {
         if (temp.contains("&" + colours[i])) {
            if (colours[i] == 'k') {
               temp = temp.replace("&" + colours[i], "\247" + colours[r.nextInt(colours.length - 1)]);
            } else {
               temp = temp.replace("&" + colours[i], "\247" + colours[i]);
            }
         }
      }
      return temp;
   }

   /**
    * Sets the Minecraft window size
    */
   public void setWindowSize(int width, int height) {      
      int widthDif = Frame.getWindows()[0].getWidth() - Display.getParent().getWidth();
      int heightDif = Frame.getWindows()[0].getHeight() - Display.getParent().getHeight();
      Frame.getWindows()[0].setSize(width + widthDif, height + heightDif);
      //Frame f = Frame.getFrames()[0];
      //f.setSize(width, height);
   }

   /**
    * Gets the Entity class associated with the String
    * @param s - The string to search for the specified entity
    * @return The class associated with the name
    */
   public Class getClass(String s) {
      Class class1 = (Class) getEntity(s);
      if (class1 != null) {
         return class1;
      }
      try {
         int i = Integer.parseInt(s);
         Class class2 = (Class) getEntityIdList().get(s);
         if (class2 != null) {
            return class2;
         }
      } catch (NumberFormatException numberformatexception) {
      }
      try {
         return Class.forName(s).asSubclass(Entity.class);
      } catch (Throwable throwable) {
         sendDebug("Invalid mob name: " + s);
      }
      return null;
   }

   /**
    * Changes a private final field
    * 
    * @param field - The field to change
    * @param newValue - The new value for the field
    * @param instance - The instance to use to set (null if static)
    */
   static void setFinal(Field field, Object newValue, Object instance) {
      try {
         field.setAccessible(true);

         Field modifiersField = Field.class.getDeclaredField("modifiers");
         modifiersField.setAccessible(true);
         modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

         field.set(instance, newValue);
      } catch (Exception e) {
      }
   }

   public void confuse(EntityCreature entitycreature, EntityCreature entitycreature1) {
      entitycreature.attackEntityAsMob(entitycreature1);
   }

   /**
    * Gets the class associated with the specified entity name
    * 
    * @param s - The entity name
    * @return The class associated with this name
    */
   /*
    * public Class getClass(String s) { Class class1 = (Class)getSpawnList().get(s); if(class1 !=
    * null) { return class1; } try { int i = Integer.parseInt(s); Class class2 =
    * (Class)getSpawnIdList().get(s); if(class2 != null) { return class2; } }
    * catch(NumberFormatException numberformatexception) { } try { return
    * Class.forName(s).asSubclass(Entity.class); } catch(Throwable throwable) {
    * //sendError("Invalid mob name: " + s); } return null; }
    */

   /**
    * Gets all of the specified mob type within the specified distance
    * 
    * @param class1 - The mob type to retrieve
    * @param d - The distance to search in
    * @return Returns the list of mobs which match that description
    */
   public List<Entity> getAllMobs(Class<?> class1, double d) {
      ArrayList<Entity> arraylist = new ArrayList<Entity>();
      for (int i = 0; i < mc.theWorld.loadedEntityList.size(); i++) {
         Entity entity = (Entity) mc.theWorld.loadedEntityList.get(i);
         if (entity != mc.thePlayer && !entity.isDead && class1.isInstance(entity) && (d <= 0.0D || mc.thePlayer.getDistanceSqToEntity(entity) <= d)) {
            arraylist.add(entity);
         }
      }

      Collections.sort(arraylist, new Comparator<Entity>() {

         public int compare(Entity entity1, Entity entity2) {
            double d1 = mc.thePlayer.getDistanceSqToEntity(entity1) - mc.thePlayer.getDistanceSqToEntity(entity2);
            return d1 >= 0.0D ? (d1 <= 0.0D ? 0 : 1) : -1;
         }
      });

      return arraylist;
   }

   /**
    * Gets the specified class based on string name
    * 
    * @param s - The name of the mob type
    * @return The class which associates with this mob
    */
   public Class getEntityClass(String s) {
      Class class1 = (Class) getEntityList().get(s);
      if (class1 != null) {
         return class1;
      }
      try {
         int i = Integer.parseInt(s);
         Class class2 = (Class) getEntityIdList().get(s);
         if (class2 != null) {
            return class2;
         }
      } catch (NumberFormatException numberformatexception) {
      }
      try {
         return Class.forName(s).asSubclass(Entity.class);
      } catch (Throwable throwable) {
         sendError("Invalid mob name: " + s);
      }
      return null;
   }

   /**
    * Gets the Minecraft sound manager
    * 
    * @return The sound manager
    */
   public SoundSystem getSoundSystem() {
      if (sound != null) {
         return sound;
      }
      try {
         Field f[] = SoundManager.class.getDeclaredFields();
         for (Field field : f) {
            field.setAccessible(true);
            Object o = field.get(mc.sndManager);
            if (o instanceof SoundSystem) {
               sound = (SoundSystem) o;
               return sound;
            }
         }
      } catch (Throwable t) {
         t.printStackTrace();
      }
      return null;
   }

   /**
    * Plays a random track of music using the in-built music player
    * 
    * @return true is music was able to be played
    */
   public boolean playRandomMusic() {
      boolean found = false;
      try {
         Field f[] = SoundManager.class.getDeclaredFields();
         for (int i = 0; i < 2; i++) {
            // Finds the field which relates to the time before music can be played
            for (Field field : f) {
               field.setAccessible(true);
               try {
                  /*
                   * There are two int instance variables in soundmanager, one of them is limited to
                   * 256. The other is the one we want.
                   */
                  if (field.getInt(mc.sndManager) > 255) {
                     field.set(mc.sndManager, 0);
                     found = true;
                     break;
                  }
               } catch (Exception e) {
               }
            }
            // If the field wasn't found try playing music 256 times then try and find the field
            // again
            if (!found) {
               for (int j = 0; j < 256; j++) {
                  mc.sndManager.playRandomMusicIfReady();
               }
            } else {
               break;
            }
         }
      } catch (Exception e) {
         printStackTrace(e);
      }
      mc.sndManager.playRandomMusicIfReady();
      return found;
   }

   public Settings getSettings(String name) {
      return new Settings(new File(MODDIR, name + ".properties"));
   }

   /**
    * Recursively deletes the specified file or folder tree
    * 
    * @param file The file or folder to delete
    */
   public void delete(File file) {
      if (file.isFile()) {
         file.delete();
         return;
      }
      File children[] = file.listFiles();
      for (File child : children) {
         if (child.isFile()) {
            child.delete();
         } else {
            delete(child);
         }
      }
      file.delete();
   }

   /**
    * Used to transfer items from one inventory type to another. It will not override items in the
    * second inventory with the ones from the first, any items which couldn't be cleanly transferred
    * remain in the first inventory.
    * 
    * @param from The inventory to transfer from
    * @param to The inventory to transfer to
    */
   public void transferInventory(IInventory from, IInventory to) {
      int count = 0;
      if (from == null) {
         return;
      }
      try {
         for (int i = 0; i < from.getSizeInventory(); i++) {
            if (to == null) {
               from.setInventorySlotContents(i, null);
               continue;
            }
            try {
               while (to.getStackInSlot(count) != null) {
                  count++;
               }
               if (count > to.getInventoryStackLimit()) {
                  break;
               }
               to.setInventorySlotContents(count, from.getStackInSlot(i));
               from.setInventorySlotContents(i, null);
            } catch (Exception e) {
               break;
            }
         }
      } catch (Exception e) {
         printStackTrace(e);
      }
   }

   /**
    * Joins a String array together at the specified start and end index
    * 
    * @param args The string array to join together
    * @param beginindex The starting index
    * @param endindex The ending index
    * @return The joined String array, or an empty string if the range is 0
    */
   public String join(String args[], int beginindex, int endindex) {
      String joined = "";
      try {
         for (int i = beginindex; i < endindex && i < args.length; i++) { // TODO: Changed from OR to AND
            joined += args[i] + " ";
         }
         return joined.trim();
      } catch (Exception e) {
         return "";
      }
   }

   /**
    * Adds a chat message to the in game GUI
    * 
    * @param message The message to add to the GUI
    */
   public void sendMessage(String message) {
      if (!output) {
         return;
      }
      sendMessage(message,textcolornormal);
   }

   /**
    * Adds a chat message to the in game GUI
    * 
    * @param message The message to add to the GUI
    * @param colour The colour that the string is in
    */
   public void sendMessage(String message, char colour) {
      if (!output) {
         return;
      }
      String colours = VALID_COLOURS;
      int c = colours.indexOf(colour);
      if (c < 0 || c > 15) {
         if (colour == 'r' || colour == 'R') {
            colour = textcolorrandom.charAt((new Random()).nextInt(textcolorrandom.length()));
         } else {
            colour = 'f';
         }
      }
      ep.addChatMessage("\247" + colour + message);
   }

   /**
    * Send error message - sends a message that is red
    * 
    * @param message The error message to add to the GUI
    */
   public void sendError(String message) {
      if (!output) {
         return;
      }
      sendMessage(message,textcolorerror);
   }

   /**
    * Sends a debug message to the console
    * 
    * @param message The message to send to the console.
    */
   public void sendDebug(String message) {
      if (DEBUG) {
         System.out.println(System.currentTimeMillis() + "-DEBUG: " + message);
      }
   }

   /**
    * Prints the given stacktrace
    * 
    * @param throwable The exception to print
    */
   public static void printStackTrace(Throwable throwable) {
      if (DEBUG) {
         throwable.printStackTrace();
      }
   }

   /**
    * Prints the current time to the minecraft gui
    */
   public void printCurrentTime() {
      int temp[] = getTime();
      String hr = temp[1] < 10 ? "0" + temp[1] : "" + temp[1];
      String mn = temp[0] < 10 ? "0" + temp[0] : "" + temp[0];
      sendMessage("Day: " + temp[2] + " at " + hr + ":" + mn);
   }

   /**
    * Returns the current time in minecraft.
    * 
    * Time is purely based on "ticks", ie: how fast the cpu is going, the time in minecraft does not
    * reflect "real" time in any way - it is processed in this function to appear like "real" time,
    * ie: minutes, hours, days.
    * 
    * @return int[] - int[0] = minutes, int[1] = hours, int[2] = days
    */
   public int[] getTime() {
      long worldtime = mc.theWorld.worldInfo.getWorldTime();
      int DD = (int) ((worldtime / 1000) / 24);
      int HH = (int) ((worldtime / 1000) % 24);
      int MM = (int) (((worldtime % 1000) / 1000.0) * 60);
      return new int[] { MM, HH, DD };
   }

   /**
    * Prints a help message to the minecraft gui
    * 
    * @param s1 Description of the command
    * @param s2 Syntax of the command
    * @param s3 Example of the command
    */
   public void helpMessage(String s1, String s2, String s3) {
      if (s1 != null) {
         sendMessage("Description:");
         sendMessage("" + s1);
      }
      if (s2 != null) {
         sendMessage("Syntax:");
         sendMessage("" + s2);
      }
      if (s3 != null) {
         sendMessage("Example:");
         sendMessage("" + s3);
      }
   }

   /**
    * Creates a string of the current player position.
    * 
    * @return String - Returns a string formatted as, (POSX,POSY,POSZ)
    */
   public String positionAsString() {
      DecimalFormat f = new DecimalFormat("#.##");
      return "(" + f.format(ep.posX) + "," + f.format(ep.posY) + "," + f.format(ep.posZ + 1) + ")";
   }

   // TODO: This method should check if the object has already been created an return that instead
   public SPCObjectHit getObjectHit() {
//       return new SPCObjectHit(mc.objectMouseOver);
      return new SPCObjectHit(ep.rayTrace(128, 1.0F));
   }

   public void handleSwing() {
      SPCObjectHit hit = new SPCObjectHit(mc.objectMouseOver);//getObjectHit();
      if (superpunch > 0) {
//          System.out.println("PUNCHED? " + (hit == null) + " " + (hit.entity == null));
         if (hit != null && hit.entity != null) {
            SPCEntity e = hit.entity;
            e.setRotation(ep.rotationYaw, ep.rotationPitch);
            double mx = (-MathHelper.sin((e.getYaw() / 180F) * 3.141593F) * MathHelper.cos((e.getPitch() / 180F) * 3.141593F)) * superpunch;
            double my = (MathHelper.cos((e.getYaw() / 180F) * 3.141593F) * MathHelper.cos((e.getPitch() / 180F) * 3.141593F)) * superpunch;
            double mz = (-MathHelper.sin((e.getPitch() / 180F) * 3.141593F)) * superpunch;
            e.setMotion(new SPCPoint(mx,my,mz));
            
         }
      }
   }

   /**
    * Handles a left mouse click event
    */
   public void handleLeftClick() {
      try {
         SPCObjectHit hit = getObjectHit();
         PLUGIN_MANAGER.callPluginMethods(PLUGIN_HANDLELEFTCLICK, hit);
         PLUGIN_MANAGER.callPluginMethods(PLUGIN_HANDLELEFTBUTTONDOWN, hit);
         lastleftcall = System.currentTimeMillis();
      } catch (Exception e) {
         printStackTrace(e);
      }
   }

   /**
    * Handles a left mouse button down event
    */
   public void handleLeftButtonDown() {
      SPCObjectHit hit = getObjectHit();
      try {
         if (System.currentTimeMillis() > lastleftcall + 200) {
            lastleftcall = System.currentTimeMillis();
            PLUGIN_MANAGER.callPluginMethods(PLUGIN_HANDLELEFTBUTTONDOWN, hit);
         }
      } catch (Exception e) {
         printStackTrace(e);
      }
   }

   /**
    * Handles a right mouse click event
    */
   public void handleRightClick() {
      try {
         SPCObjectHit hit = getObjectHit();
         PLUGIN_MANAGER.callPluginMethods(PLUGIN_HANDLERIGHTCLICK, hit);
         PLUGIN_MANAGER.callPluginMethods(PLUGIN_HANDLERIGHTBUTTONDOWN, hit);
         lastrightcall = System.currentTimeMillis();
      } catch (Exception e) {
         printStackTrace(e);
      }
   }

   /**
    * Handles a right mouse button down event
    */
   public void handleRightButtonDown() {
      try {
         SPCObjectHit hit = getObjectHit();
         if (System.currentTimeMillis() > lastrightcall + 200) {
            lastrightcall = System.currentTimeMillis();
            PLUGIN_MANAGER.callPluginMethods(PLUGIN_HANDLERIGHTBUTTONDOWN, hit);
         }
      } catch (Exception e) {
         printStackTrace(e);
      }
   }

   /**
    * This method processes the key stroke and if it's a movement key it will do the necessary
    * movement to immitate flying
    * 
    * @param key The keyboard movement key
    */
   public void fly(int key) {
      if (!flying) {
         return;
      }

      if (flymode.equalsIgnoreCase("minecraft")) {
         ep.motionY = 0;
         ep.onGround = false;
         return;
      }

      float pitch = ep.rotationPitch;
      GameSettings g = mc.gameSettings;
      boolean horizontal = false;
      boolean vertical = false;
      ep.onGround = true;
      if (key == g.keyBindForward.keyCode) {
         pitch = -pitch;
         horizontal = true;
      } else if (key == g.keyBindBack.keyCode) {
         horizontal = true;
      } else if (key == g.keyBindSneak.keyCode) {
         ep.jump();
         ep.motionY = -ep.motionY;
         vertical = true;
         return;
      } else if (key == g.keyBindJump.keyCode) {
         ep.jump();
         vertical = true;
         return;
      }
      if (flymode.equalsIgnoreCase("standard")) {
         ep.motionY = 0;
         return;
      }

      if (((MathHelper.sqrt_double(ep.motionX * ep.motionX) > 0.01) || (MathHelper.sqrt_double(ep.motionZ * ep.motionZ) > 0.01)) && (horizontal || vertical)) {
         ep.motionY += ((pitch / 360) * speed);
      } else if (!vertical) {
         ep.motionY = 0;
      }
   }

   /**
    * Handles a keypress.
    * 
    * @param key The keyboard key which was pressed
    */
   public void handleKeyPress(int key) {
      GameSettings g = mc.gameSettings;
      if (key == g.keyBindChat.keyCode) {
         if (mc.currentScreen == null && !opened) {
            mc.displayGuiScreen(new GuiChat());
            // mc.displayGuiScreen(new GuiConsole());
            opened = true;
         }
         opened = false;
      } else if (key == g.keyBindForward.keyCode) {
         fly(key);
      } else if (key == g.keyBindBack.keyCode) {
         fly(key);
      } else if (key == g.keyBindSneak.keyCode) {
         fly(key);
      } else if (key == g.keyBindJump.keyCode) {
         fly(key);
      }
      if (bound.containsKey("" + key)) {
         ep.sendChatMessage(bound.getProperty("" + key));
      }
   }

   /**
    * Handles when a keyboard key is held down
    * 
    * @param key - The keyboard key being held down
    */
   public void handleKeyDown(int key) {
      GameSettings g = mc.gameSettings;
      if (key == g.keyBindForward.keyCode) {
         fly(key);
      } else if (key == g.keyBindBack.keyCode) {
         fly(key);
      } else if (key == g.keyBindSneak.keyCode) {
         fly(key);
      } else if (key == g.keyBindJump.keyCode) {
         fly(key);
      }
   }

   /**
    * Gets called on Minecraft startup
    */
   public void startup() {
      if (!startup.equalsIgnoreCase("")) {
         processCommand(startup);
      }
      if (updateson) {
         try {
            Method m = PlayerHelper.class.getDeclaredMethod("updateAvailable", Vector.class);
            (new SPCCheckVersion(new SPCVersion[]{EntityPlayerSPSPC.SPCVERSION},EntityPlayerSPSPC.MCVERSION,m,this)).start();
         } catch (Throwable t) {
            PlayerHelper.printStackTrace(t);
         }
      }
   }

   /**
    * Handles the mouse item bindings
    */
   public void handleMouseBindings() {
      for (int i = 0; i < Mouse.getButtonCount(); i++) {
         if (Mouse.isButtonDown(i)) {
            if (mouse.contains(i)) {
               continue;
            }
            mouse.add(i);
         } else {
            mouse.remove((Integer)i);
            continue;
         }
         Iterator j = itemsbound.keySet().iterator();
         while (j.hasNext()) {
            Object key = j.next();
            try {
               String binding[] = ((String)key).split(",");
               if (binding[0].equalsIgnoreCase(i + "")) {
                  ItemStack currentitem = ep.inventory.getCurrentItem();
                  int itemid = currentitem == null ? 0 : currentitem.itemID;
                  int damage = currentitem == null || currentitem.isItemStackDamageable() ? 0 : currentitem.getItemDamage();
                  if (binding[1].equalsIgnoreCase(itemid + "") && binding[2].equalsIgnoreCase(damage + "")) {
                     ep.sendChatMessage(itemsbound.getProperty((String)key));
                     break;
                  }
               }
            } catch (Exception e) {
               itemsbound.remove(key);
            }
         }
      }  
   }

   /**
    * Gets called before a player onUpdate() call. Useful for time dependent commands such as
    * keyboard or mouse input.
    */
   public void beforeUpdate() {
      if (ismultiplayer) {
         return;
      }

      // Startup needs to run ASAP after the Player (and the World) has fully initialised
      if (HAS_STARTED_UP == null) {
         HAS_STARTED_UP = new Object();
         startup();
      }

      // Gets the player walk modified to control the step sound
      if (flying) {
         walked = ep.distanceWalkedModified;
      }

      mc.overrideMobSpawning = !animalspawn || !monsterspawn;

      // "Mouse Listener"
      handleMouseBindings();
      if (mc.currentScreen == null) {
         if (Mouse.isButtonDown(0)) {
            if (!leftbutton) {
               handleLeftClick();
            } else {
               handleLeftButtonDown();
            }
            leftbutton = true;
         } else {
            leftbutton = false;
         }
         if (Mouse.isButtonDown(1)) {
            if (!rightbutton) {
               handleRightClick();
            } else {
               handleRightButtonDown();
            }
            rightbutton = true;
         } else {
            rightbutton = false;
         }
      }

      // "Keyboard Listener"
      // TODO: Needs optimising/rewriting
      if (mc.currentScreen == null) {
         GameSettings g = mc.gameSettings;
         Vector<Integer> keys = new Vector<Integer>();
         keys.add(g.keyBindChat.keyCode);
         keys.add(g.keyBindForward.keyCode);
         keys.add(g.keyBindBack.keyCode);
         keys.add(g.keyBindSneak.keyCode);
         keys.add(g.keyBindJump.keyCode);
         Iterator<Object> bind = bound.keySet().iterator();
         while (bind.hasNext()) {
            try {
               keys.add(Integer.parseInt((String) bind.next()));
            } catch (Exception e) {
            }
         }
         for (int i = 0; i < keys.size(); i++) {
            int key = keys.elementAt(i);
            try {
               if (Keyboard.isKeyDown(key)) {
                  if (keyboard.contains(key)) {
                     handleKeyDown(key);
                  } else {
                     handleKeyPress(key);
                     keyboard.add(key);
                  }
               } else {
                  keyboard.remove(new Integer(key));
               }
            } catch (Exception e) {
               try {
                  bound.remove(key + "");
               } catch (Exception exc) {
               }
               PlayerHelper.printStackTrace(e);
            }
         }
      }
   }

   /**
    * This method is called after a Player onUpdate method is called
    */
   @SuppressWarnings("unchecked")
   public void afterUpdate() {
      if (ismultiplayer) {
         return;
      }
      if (!waterdamage) {
         //ep.air = ep.maxAir;
         ep.setAir(300);
      }
      if (!damage) {
         ep.hurtTime = 0;
         ep.hurtResistantTime = 0;
      }
      if (timeschedule != null) {
         int temp[] = getTime();
         if (lastrift != temp[2] && (temp[1] > timeschedule[2] || (temp[1] >= timeschedule[2] && temp[0] > timeschedule[3]))) {
            int day = 0;
            if (timeschedule[0] < timeschedule[2] || (timeschedule[0] <= timeschedule[2] && timeschedule[1] < timeschedule[2]))
               day = 1;
            ((WorldSSP)mc.theWorld).commandSetTime(((temp[2] + day) * 24000) + ((timeschedule[0] % 24) * 1000) + (int) (((timeschedule[1] % 60) / 60.0) * 1000), false);
            lastrift = temp[2];
            saveSettings();
         }
      }

      // TODO: Expensive call per frame - will reduce frame rate, needs to be revised
      if (!dropitems || instantplant) {
         int distance = 128;
         List<Entity> items = mc.theWorld.getEntitiesWithinAABBExcludingEntity(ep, AxisAlignedBB.getBoundingBox(ep.posX - distance, ep.posY - distance, ep.posZ - distance, ep.posX + distance, ep.posY + distance, ep.posZ + distance));
         for (int i = 0; i < items.size(); i++) {
            Entity e = items.get(i);
            if (e instanceof EntityItem) {
               EntityItem ei = (EntityItem) e;
               if (!dropitems && ei.age >= 0) {
                  mc.theWorld.setEntityDead(ei);
               } else if (instantplant && (ei).func_92059_d().itemID == Block.sapling.blockID) {
                  BlockSapling tmp = (BlockSapling) Block.sapling;
                  if (tmp.canPlaceBlockAt(mc.theWorld, (int) e.posX, (int) e.posY, (int) e.posZ)) {
                     mc.theWorld.setBlockWithNotify((int) e.posX, (int) e.posY, (int) e.posZ, Block.sapling.blockID);
                     if (instantgrow) {
                        tmp.growTree(mc.theWorld, (int) e.posX, (int) e.posY, (int) e.posZ, new Random());
                     }
                     mc.theWorld.setEntityDead(ei);
                  }
               }
            }
         }
      }

      // TODO: Expensive call per frame - will reduce frame rate, needs to be revised
      if (mobsfrozen || !mobdamage || !creeperExplosion) {
         for (int i = 0; i < mc.theWorld.loadedEntityList.size(); i++) {
            if (mc.theWorld.loadedEntityList.get(i) instanceof EntityLiving && !(mc.theWorld.loadedEntityList.get(i) instanceof EntityPlayer)) {
               EntityLiving e = (EntityLiving) mc.theWorld.loadedEntityList.get(i);
               if (mobsfrozen) {
                  e.setPosition(e.prevPosX, e.prevPosY, e.prevPosZ);
                  e.motionX = 0;
                  e.motionY = 0;
                  e.motionZ = 0;
               }

               boolean allow = mobsfrozen || !mobdamage;
               
               e.setAttackTarget(null);

               if (e instanceof EntityCreature && !(e instanceof EntityPlayerSPSPC) && allow) {
                  ((EntityCreature) e).attackTime = 20;
                  /*Render r = RenderManager.instance.getEntityRenderObject(e);
                  if (r instanceof RenderLiving) {
                     try {
                        ((RenderLiving)r).renderLivingLabel(e, "1", e.posX, e.posY, e.posZ, 32);
                     } catch (Exception ex) {}
                  }*/
               }

               if (e instanceof EntityCreeper) {
                  try{
                      Field timeSinceIgnitedField = (EntityCreeper.class).getDeclaredFields()[1];
                      timeSinceIgnitedField.setAccessible(true);
                      timeSinceIgnitedField.set(e, -1);
                  }catch(IllegalAccessException exc){
                      exc.printStackTrace();
                  }
//                   ((EntityCreeper) e).timeSinceIgnited = -1;
               }
            }
         }
      }

      if (timespeed != 0) {
         ((WorldSSP)mc.theWorld).commandSetTime(mc.theWorld.worldInfo.getWorldTime() + timespeed, false);
      }

      if (infiniteitems) {
         for (int i = 0; i < ep.inventory.mainInventory.length; i++) {
            if (ep.inventory.mainInventory[i] != null) {
               ep.inventory.mainInventory[i].stackSize = ep.inventory.mainInventory[i].getMaxStackSize();
            }
         }
      }

      if (!weather) {
         mc.theWorld.worldInfo.setRaining(false);
         mc.theWorld.worldInfo.setThundering(false);
         mc.theWorld.worldInfo.setRainTime(-Integer.MAX_VALUE);
         mc.theWorld.worldInfo.setThunderTime(-Integer.MAX_VALUE);
      }

      if (flying) {
         fly(-1);
         ep.distanceWalkedModified = walked;
      }

      PLUGIN_MANAGER.callPluginMethods(PLUGIN_ATUPDATE);
   }

   /**
    * This method is called to grow a "plant" at the specified location
    * 
    * @param i - coordinate
    * @param j - coordinate
    * @param k - coordinate
    * @param r - A random object
    * @param wgt - A world generator object to use at the location
    * @return True is returned when the method successfully grows a plant
    */
   public boolean growPlant(int i, int j, int k, Random r, WorldGenerator wgt) {
      if (mc.theWorld.getBlockId(i, j, k) == Block.sapling.blockID) {
         ((BlockSapling)Block.sapling).growTree(mc.theWorld, i, j, k, r);
         /*mc.theWorld.setBlock(i, j, k, 0);
         if (!wgt.generate(mc.theWorld, r, i, j, k)) {
            mc.theWorld.setBlock(i, j, k, Block.sapling.blockID);
            mc.theWorld.setBlockMetadataWithNotify(i, j, k, 15);
            return false;
         }*/
         return true;
      } else if ((mc.theWorld.getBlockId(i, j, k) == Block.crops.blockID) || (mc.theWorld.getBlockId(i, j, k) == Block.carrot.blockID) || (mc.theWorld.getBlockId(i, j, k) == Block.potato.blockID)) {
         mc.theWorld.setBlockMetadataWithNotify(i, j, k, 7);
         return true;
      } else if ((mc.theWorld.getBlockId(i, j, k) == Block.cactus.blockID) || (mc.theWorld.getBlockId(i, j, k) == Block.reed.blockID)) {
         int blockid = mc.theWorld.getBlockId(i, j, k);
         int length = 1;
         while (true) {
            int blen = length;
            if (mc.theWorld.getBlockId(i, j + length, k) == blockid) {
               length++;
            }
            if (mc.theWorld.getBlockId(i, j - length, k) == blockid) {
               length++;
            }
            if (blen == length) {
               break;
            }
         }
         if (length < 3) {
            for (int i1 = 0; i1 <= 3 - length; i1++) {
               mc.theWorld.setBlockWithNotify(i, j + i1, k, blockid);
            }
            return true;
         }
      } else if (mc.theWorld.getBlockId(i, j, k) == Block.cocoaPlant.blockID) {
         int meta = mc.theWorld.getBlockMetadata(i, j, k);
         mc.theWorld.setBlockMetadataWithNotify(i, j, k, 2 << 2 | BlockDirectional.getDirection(meta));
         return true;
      } 
      return false;
   }

   public ItemStack getItemStack(String args[]) {
      if (args == null) {
         return null;
      }
      Item i = null;
      try {
         if (args.length == 1) {
            ItemStack temp = getItem(args);
            i = temp.getItem();
            //System.out.println(temp.getItemDamage());
            return new ItemStack(i, i.maxStackSize, temp.getItemDamage());
         }
         try {
            String part[] = args[0].split("(\\^|:)");
            int id = Integer.parseInt(part[0]);
            int qty = Item.itemsList[id].maxStackSize;
            int damage = 0;
            if (args.length > 1) {
               qty = Integer.parseInt(args[1]);
            }
            if (part.length == 2) {
               damage = Integer.parseInt(part[1]);
            } else if (args.length > 2) {
               damage = Integer.parseInt(args[2]);
            }
            return new ItemStack(Item.itemsList[id], qty, damage);
         } catch (Exception e) {
            int qty = 1;
            int damage = 0;
            try {
               if (args.length > 1) {
                  Integer.parseInt(args[args.length - 1]);
                  qty = -1;
               }
               if (args.length > 2) {
                  Integer.parseInt(args[args.length - 2]);
                  damage = -1;
               }
            } catch (Exception e1) {
            }
            int offset = 0;
            if (damage == -1) {
               qty = Integer.parseInt(args[args.length - 2]);
               damage = Integer.parseInt(args[args.length - 1]);
               offset = 2;
            } else if (qty == -1) {
               qty = Integer.parseInt(args[args.length - 1]);
               offset = 1;
            }
            String name = "";
            for (int j = 0; j < args.length - offset; j++) {
               name += args[j] + " ";
            }
            String part[] = name.split("(\\^|:)");
            int val = ITEMNAMES.indexOf(part[0].trim().toLowerCase());
            if (val > -1) {
               i = Item.itemsList[val];
               if (offset == 0) {
                  qty = i.getItemStackLimit();
               }
               if (part.length == 2) {
                  damage = Integer.parseInt(part[1].trim());
               }
               return new ItemStack(Item.itemsList[val], qty, damage);
            }
         }
      } catch (Exception e) {
         return null;
      }
      return null;
   }

   public ItemStack getItem(String args[]) {
      if (args == null) {
         return null;
      }
      int damage = 0;
      if (args.length == 1) {
         try {
            String part[] = args[0].split("(\\^|:)");
            int i = Integer.parseInt(part[0]);
            try {
               damage = Integer.parseInt(part[1]);
            } catch (Exception e) {
               damage = 0;
            }
            return new ItemStack(Item.itemsList[i],0,damage);
         } catch (Exception e) {
         }
      }
      String name = "";
      for (int i = 0; i < args.length; i++) {
         name += args[i] + " ";
      }
      String part[] = name.split("(\\^|:)");
      try {
         damage = Integer.parseInt(part[1].trim());
      } catch (Exception e) {
         damage = 0;
      }
      int val = ITEMNAMES.indexOf(part[0].trim().toLowerCase());
      if (val > -1) {
         return new ItemStack(Item.itemsList[val],0,damage);
      }
      return null;
   }

   /**
    * Copies a directory from one location to another
    * 
    * @param from The origin directory - where the contents are copied from
    * @param to - The destination directory
    * @param ipg - Optional progress screen
    * @return Returns true if the copy was successful
    */
   public static boolean copyDirectory(File from, File to, IProgressUpdate ipg) {
      if (!from.isDirectory()) {
         return false;
      }
      if (!to.exists()) {
         to.mkdirs();
      }
      if (!to.isDirectory()) {
         return false;
      }
      if (ipg != null) {
         ipg.resetProgresAndWorkingMessage("Moving chunks");
      }
      try {
         File list[] = from.listFiles();
         for (int i = 0; i < list.length; i++) {
            if (list[i].isDirectory()) {
               copyDirectory(list[i], new File(to, list[i].getName()), null);
            } else if (list[i].isFile()) {
               copyFile(list[i], new File(to, list[i].getName()));
            }
            if (ipg != null) {
               ipg.setLoadingProgress((i * 100) / list.length);
            }
         }
      } catch (Exception e) {
         return false;
      }
      return true;
   }

   /**
    * Copies a single file from one location to another
    * 
    * @param from The origin file
    * @param to The destination file
    * @return Returns true if the file was able to be copied successfully
    */
   public static boolean copyFile(File sourceFile, File destFile) {
      if (!destFile.exists()) {
         try {
            destFile.createNewFile();
         } catch (IOException e) {
            return false;
         }
      }

      FileChannel source = null;
      FileChannel destination = null;
      try {
         source = new FileInputStream(sourceFile).getChannel();
         destination = new FileOutputStream(destFile).getChannel();
         destination.transferFrom(source, 0, source.size());
      } catch (Exception e) {
         return false;
      } finally {
         try {
            if (source != null) {
               source.close();
            }
            if (destination != null) {
               destination.close();
            }
         } catch (Exception e) {
         }
      }
      return true;
   }

   /**
    * Toggles the light on and off on the map
    * 
    * @param light Whether to turn light on or off
    */
   public void toggleLight(boolean light, boolean update) {
      WorldProvider temp = ep.worldObj.provider;
      EntityRenderer er = mc.entityRenderer;
      if (light) {
         for (int i = 0; i < temp.lightBrightnessTable.length; i++) {
            temp.lightBrightnessTable[i] = 1.0F;
         }
         for (int i = 0; i < er.lightTable.length; i++) {
            er.lightTable[i] = 1.0F;
         }
      } else {
         temp.generateLightBrightnessTable();
         er.calculateLightTable();
      }
      if (mc.oldlighting && update){
         mc.renderGlobal.updateAllRenderers(true);
      }
      try {
         //mc.renderGlobal.updateAllRenderers();
      } catch (Exception e) {
      }
   }

   public void setItemMaxDamage() {
      for (int i = 0; i < Item.itemsList.length; i++) {
         if (Item.itemsList[i] != null && Item.itemsList[i] instanceof Item) {
            if (Item.itemsList[i].getMaxDamage() > 0) {
               if (Item.itemsList[i].isDamageable()) {
                  ITEM_MAX_DAMAGE[i] = Item.itemsList[i].getMaxDamage();
               }
            }
            if (norepair) {
               if (Item.itemsList[i].isDamageable()) {
                  Item.itemsList[i].setMaxDamage(MAGICNUMBER);
               }
               // Item.itemsList[i].maxDamage = -1;
            }
         }
      }
   }

   public void destroyInventory() {
      for (int i = 0; i < ep.inventory.mainInventory.length; i++) {
         ep.inventory.mainInventory[i] = null;
      }
      for (int i = 0; i < ep.inventory.armorInventory.length; i++) {
         ep.inventory.armorInventory[i] = null;
      }
   }

   /**
    * Gets block ID from either an numerical ID or the name of the block
    * 
    * @param block The name of the block
    * @return The block ID or -1 if it wasn't found
    */
   public int getBlockID(String block) {
      try {
         int id = Integer.parseInt(block);
         if (id >= 0 && id < Block.blocksList.length) {
            return id;
         }
      } catch (Exception e) {
      }

      if (block.equalsIgnoreCase("air")) {
         return 0;
      }

      for (int i = 0; i < Block.blocksList.length; i++) {
         if (Block.blocksList[i] != null) {
            String curName = Block.blocksList[i].getBlockName();
            curName = curName.substring(curName.indexOf('.') + 1);
            if (block.equalsIgnoreCase(curName)) {
               return i;
            }
         }
      }
      sendDebug("Block not found..");
      return -1;
   }

   /*   public void confuseMobs() {
      List<EntityLiving> lel = ep.worldObj.loadedEntityList;

      if (lel == null) {
         return;
      }
      for (int i = 1; i < lel.size(); i++) {
         if (lel.get(i) instanceof EntityMob) {
            try {
               Field target = EntityLiving.class.getDeclaredField("field_4120_b");
               target.setAccessible(true);
               target.set(lel.get(i), lel.get(i - 1));
               sendMessage("Mob confused!");
            } catch (Exception e) {
               e.printStackTrace();
            }
         }
      }
   }*/

   /**
    * Changes the players skin
    * 
    * @param username The username of the player to change your skin to
    */
   public void changeSkin(EntityPlayer entity, String username) {
      entity.username = username;
      entity.skinUrl = (new StringBuilder()).append("http://s3.amazonaws.com/MinecraftSkins/").append(username).append(".png").toString();
      ep.worldObj.obtainEntitySkin(entity);
      // ep.worldObj.loadDownloadableImageTexture(ep.skinUrl, null);
      entity.updateCloak();

   }

   /**
    * Sends a list of comma seperated key bindings to the user
    */
   public void sendBindList() {
      if (bound == null) {
         sendMessage("No key bindings found.");
         return;
      }
      Iterator i = bound.keySet().iterator();
      String list = "";
      while (i.hasNext()) {
         try {
            int key = Integer.parseInt((String) i.next());
            list += Keyboard.getKeyName(key) + ", ";
         } catch (Exception e) {
         }
      }
      list = list.substring(0, list.length() - 2);
      if (!list.equalsIgnoreCase("")) {
         sendMessage("Key bindings:");
         sendMessage(list);
      } else {
         sendMessage("No key bindings found.");
      }
   }

   /**
    * Converts the user input so that the alias's are converted
    * @param input - The user input
    * @return The resulting String
    */
   public String convertInput(String input) {
      if (input == null) {
         return input;
      }
      String temp[] = input.split(" ");
      String newstring = temp[0];
      if (alias.containsKey(temp[0])) {
         newstring = alias.getString(temp[0], temp[0]);
      }
      return (newstring + " " + join(temp,1,temp.length)).trim();
   }   

   /**
    * Adds an alias into the game
    * @param alias - The name of the new alias (no spaces)
    * @param value - What the alias gets converted into
    */
   public void addAlias(String alias, String value) {
      this.alias.set(alias, value);
      this.alias.save();
      manager.registerCommand(createWrapper(alias));
   }

   /**
    * Called when the check update function finishes. Sends the result to the user
    * 
    * @param s - The updates which are available
    */
   public void checkUpdateAvailable(Vector<HashMap<String,Object>> s) {
      if (s == null || s.size() > 0) {
         updateAvailable(s);
      } else {
         sendMessage("No new updates are available.");
      }
   }

   /**
    * Called when an update is available
    * 
    * @param s - The vector containing any and all update information
    */
   public void updateAvailable(Vector<HashMap<String,Object>> s) {
      Iterator<HashMap<String,Object>> e = s.iterator();
      while (e.hasNext()) {
         HashMap<String,Object> t = (HashMap<String,Object>)e.next();
         char colour = textcolorrandom.charAt((new Random()).nextInt(textcolorrandom.length()));
         if (t.get("message") == null || ((String)t.get("message")).equalsIgnoreCase("")) {
            sendMessage(t.get("name") + " V" + t.get("version") + " now out! " + t.get("website"),colour);
         } else {
            sendMessage((String)t.get("message"),colour);
         }
      }
   }

   /**
    * Toggles mobs on/off
    * @param name - The name of the mob to turn on/off
    * @param disable - Whether to disable the mob or enable it
    */
   public boolean toggleMob(String name, boolean disable) {
      Class<?> c = getEntity(name);
      if (c == null) {
         return false;
      }
      Field fields[] = BiomeGenBase.class.getDeclaredFields();
      for (Field f : fields) {
         if (f == null) {
            continue;
         }
         try {
            f.setAccessible(true);
            f.get(null);
         } catch (Exception e) {
            continue;
         }
         try {
            if (f.get(null) instanceof BiomeGenBase) {
               BiomeGenBase b = (BiomeGenBase)f.get(null);
               List o[] = new List[]{b.spawnableCreatureList,b.spawnableMonsterList,b.spawnableWaterCreatureList};
               for (List l : o) {
                  if (!disable) { // enabling
                     //TODO: 1.7.3 - l.add(new SpawnListEntry(c,10));
                     continue;
                  }
                  for (Object e : l) { // disabling
                     if (disable && e instanceof SpawnListEntry) {
                        SpawnListEntry d = (SpawnListEntry)e;
                        if (d.entityClass == c) {
                           l.remove(e);
                        }
                     }
                     //System.out.println(((SpawnListEntry)e).entityClass.getName());
                  }
               }
            }
         } catch (Exception e) {
            e.printStackTrace();
            return false;
         }
      }
      return true;
   }

   /**
    * Turns noclip mode on/off
    * @param noclip - true to turn noclip on
    */
   public void noclip(boolean noclip) {
      //if (noclip == noClip) return;
      ep.fallDistance = 0;
      if (noclip) {
         prevflying = flying;
         prevdamage = damage;
         prevfiredamage = ep.isImmuneToFire;
         prevwaterdamage = waterdamage;
         prevfalldamage = falldamage;
         flying = true;
         damage = false;
         ep.isImmuneToFire = true;
         waterdamage = false;
         falldamage = false;
         ep.boundingBox.setBB(AxisAlignedBB.getBoundingBox(0, 0, 0, 0, 0, 0));
      } else {
         flying = prevflying;
         damage = prevdamage;
         ep.isImmuneToFire = prevfiredamage;
         waterdamage = prevwaterdamage;
         falldamage = prevfalldamage;
         ep.setPosition(ep.posX, ep.posY, ep.posZ);
      }
   }
   
   
   /**
    * Extracts the specified file to the specified destination
    * 
    * @param zip - The archive filename
    * @param filename - The filename to extract
    * @param destination - The destination directory
    * @return True if the file extracts successfully, false otherwise
    */
   public static boolean extractFile(File zip, String filename, File destination) {
      try {
         JarFile jar = new JarFile(zip);
         ZipEntry entry = jar.getEntry(filename);
         File efile = new File(destination, entry.getName());
         InputStream in = new BufferedInputStream(jar.getInputStream(entry));
         OutputStream out = new BufferedOutputStream(new FileOutputStream(efile));
         byte[] buffer = new byte[4096];
         while (true)  {
            int bytes = in.read(buffer);
            if (bytes <= 0) {
               break;
            }
            out.write(buffer, 0, bytes);
         }
         out.flush();
         out.close();
         in.close();
      } catch (Exception e) {
         return false;
      }
      return true;
   }

   public static void registerCommandWrappers(ClientCommandManager man){
      if (man != null){
          manager = man;
      }
      Iterator<String> i = PH.commands.keySet().iterator();
      while (i.hasNext()) {
         manager.registerCommand(createWrapper(i.next())); //Commands
      }
      Iterator i2 = PH.alias.keySet().iterator();
      while (i2.hasNext()) {
         manager.registerCommand(createWrapper(((String)i2.next()))); //Aliases
      }
      Iterator<String> i3 = PLUGIN_MANAGER.commands.keySet().iterator();
      while (i3.hasNext()) {
         String key = i3.next();
         String[] desc = PLUGIN_MANAGER.getHelp(key);
         manager.registerCommand(new CommandClientSPC(key, desc[0])); //Plugin commands
      }
   }

   private static CommandClientSPC createWrapper(String key){
     String[] desc = PH.CMDS.get(PH.commands.get(key));
     CommandClientSPC command = new CommandClientSPC(key, desc[1]);
     return command;
   }
}
