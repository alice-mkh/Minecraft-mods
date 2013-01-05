package net.minecraft.src;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * This class manages the Single Player commands plugins. It has various methods which allow control
 * over how plugins are loaded, when they are loaded and where they are loaded from. It also has the
 * ability to remove and configure plugins.
 * 
 * @author simo_415 Copyright (C) 2010-2011 simo_415 - (http://bit.ly/spcmod)
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
public class SPCPluginManager {

   public Vector<SPCPlugin> plugins;
   public Vector<SPCPlugin> disabledplugins;
   public HashMap<String, Object[]> commands;
   public PlayerHelper ph;
   public boolean enabled;

   public static SPCPluginManager MANAGER;

   /*
    * Loads all the .jars contained within .minecraft/mods/sppcommands/jars
    */
   static {
      try {
         File jar = new File(PlayerHelper.MODDIR, "jars");
         if (!jar.exists()) {
            jar.mkdir();
         } else {
            File bin[] = jar.listFiles();
            Vector<String> ignore = new Vector<String>();
            ignore.add("minecraft.jar");
            ignore.add("lwjgl.jar");
            ignore.add("lwjgl_util.jar");
            ignore.add("jinput.jar");
            for (File temp : bin) {
               String name = temp.getName();
               if (!ignore.contains(name) && (name.endsWith(".jar"))) {
                  PlayerHelper.addToClasspath(temp);
               }
            }
         }
      } catch (Exception e) {
         PlayerHelper.printStackTrace(e);
      }
   }

   public SPCPluginManager(PlayerHelper ph) {
      this.ph = ph;
      plugins = new Vector<SPCPlugin>();
      disabledplugins = new Vector<SPCPlugin>();
      commands = new HashMap<String, Object[]>();
      enabled = true;
      MANAGER = this;
   }

   /**
    * @TODO - Need to fix into proper singleton pattern
    * @return
    */
   public static SPCPluginManager getPluginManager() {
      return MANAGER;
   }

   /**
    * Sets whether plugins are enabled or not
    * 
    * @param enabled true if plugins are enabled
    */
   public void setEnabled(boolean enabled) {
      this.enabled = enabled;
   }

   /**
    * Loads plugins from within the JAR - Must start with spc_
    * 
    * @return True is returned when no bad exceptions occurred.
    */
   public boolean loadPlugins() {
      String failed = "";
      try {
         String test = PlayerHelper.class.getResource("PlayerHelper.class").toString();
         if (test.toLowerCase().startsWith("jar")) {
            String jarfile = test.replaceAll("jar:", "").split("!")[0];
            JarFile jf = new JarFile(new File((new URL(jarfile)).toURI()));
            Enumeration<JarEntry> em = jf.entries();

            while (em.hasMoreElements()) {
               JarEntry je = em.nextElement();
               try {
                  if (!je.getName().toLowerCase().contains("spc_")) {
                     continue;
                  }
                  Package p = PlayerHelper.class.getPackage();
                  SPCPlugin plugin = loadPlugin(je.getName(), p == null ? null : p.getName());
                  if (plugin != null) {
                     plugins.add(plugin);
                     Map<String,Object[]> cmds = loadCommands(plugin);
                     if (cmds != null) {
                        commands.putAll(cmds);
                     }
                  } else {
                     failed += je.getName() + " ";
                  }
                  /*if (!loadPlugin(je.getName(), p == null ? null : p.getName())) {
                     failed += je.getName() + " ";
                  }*/
               } catch (Throwable t) {
                  t.printStackTrace();
               }
            }
         } else {
            String classLocation = PlayerHelper.class.getName().replace('.', '/') + ".class";
            URLClassLoader loader = (URLClassLoader) PlayerHelper.class.getClassLoader();
            URL u = loader.getResource(classLocation);
            File f[] = (new File(u.getFile())).getParentFile().listFiles();

            if (f == null) {
               f = (new File(u.toURI())).getParentFile().listFiles();
            }

            for (File temp : f) {
               if (!temp.isFile() || !temp.getName().toLowerCase().startsWith("spc_")) {
                  continue;
               }
               try {
                  Package p = PlayerHelper.class.getPackage();
                  SPCPlugin plugin = loadPlugin(temp.getName(), p == null ? null : p.getName());
                  if (plugin != null) {
                     plugins.add(plugin);
                     Map<String,Object[]> cmds = loadCommands(plugin);
                     if (cmds != null) {
                        commands.putAll(cmds);
                     }
                  } else {
                     failed += temp.getName() + " ";
                  }
                  /*if (!loadPlugin(temp.getName(), p == null ? null : p.getName())) {
                     failed += temp.getName() + " ";
                  }*/
               } catch (Exception e) {
               }
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         return false;
      }
      if (!failed.equalsIgnoreCase("")) {
         ph.sendError("Plugin(s) failed to load: " + failed);
         ph.sendError("Please refer to installation instructions.");
      }
      return true;
   }

   /**
    * Adds a plugin to the plugin list. First it will run some checks to verify that the supplied
    * file name is a valid class name. (Starts with ends with .class) then it will add the
    * specified package to the front of the string and attempt to load the class.
    * 
    * @param plugin - The filename of the plugin to load
    * @param pack - The package of the supplied file
    * @return SPCPlugin which was loaded. null if a problem occurred loading the specified plugin
    * @throws Exception Is thrown if the specified plugin is not a valid plugin type (ie: doesn't
    *            follow the rules).
    */
   public static SPCPlugin loadPlugin(String plugin, String pack) throws Exception {
      if (!plugin.endsWith(".class")) {
         throw new Exception("Not a plugin.");
      }
      plugin = plugin.split("\\.")[0];
      plugin = pack == null ? plugin : pack + "." + plugin;
      plugin = plugin.replaceAll("/", ".");

      System.out.println("Attempting to load: " + plugin);
      URLClassLoader loader = (URLClassLoader) PlayerHelper.class.getClassLoader();
      Class<?> c;
      try {
         c = loader.loadClass(plugin);
      } catch (Exception e) {
         e.printStackTrace();
         return null;
      }
      if (c != null && SPCPlugin.class.isAssignableFrom(c)) {
         try {
            SPCPlugin temp = (SPCPlugin) c.newInstance();
            if (temp != null) {
               return temp;
            }
         } catch (Exception e) {
            e.printStackTrace();
            return null;
         }
      } else {
         throw new Exception("Not a plugin.");
      }
      return null;
   }

   /**
    * Loads all the commands which are in the plugin class. Plugins are considered any method which
    * has the SPCCommand annotation and has the first parameter type as a String array.
    * 
    * @param plugin - The plugin class to load the commands from
    * @return Map containing all the commands found in the specified Plugin
    */
   public static Map<String,Object[]> loadCommands(SPCPlugin plugin) {
      Method method[] = null;
      try {
         method = plugin.getClass().getDeclaredMethods();
      } catch (Exception e) {
         PlayerHelper.printStackTrace(e);
         return null;
      }
      HashMap<String,Object[]> temp = new HashMap<String,Object[]>();
      for (Method m : method) {
         try {
            m.setAccessible(true);
            Class<?> parameters[] = m.getParameterTypes();
            if (parameters.length == 0 || !parameters[0].isAssignableFrom(String[].class)) {
               continue;
            }
            SPCCommand info = null;
            if ((info = m.getAnnotation(SPCCommand.class)) != null) {
               temp.put(info.cmd(), new Object[] { plugin, m, info });
            }
         } catch (Exception e) {
            PlayerHelper.printStackTrace(e);
         }
      }
      return temp;
   }

   /**
    * Calls all the plugins specified method with the specified arguments.
    * 
    * @param m - The method to call
    * @param args - The arguments provided
    * @return true if the method returns boolean and at least one of the method calls returned true
    */
   public boolean callPluginMethods(Method m, Object... args) {
      if (m == null || args == null || !enabled) {
         return false;
      }
      boolean found = false;
      for (SPCPlugin p : plugins) {
         try {
            Object o = m.invoke(p, args);
            if (o instanceof Boolean && (Boolean) o) {
               found = true;
            }
         } catch (Throwable t) {
            t.printStackTrace();
         }
      }
      return found;
   }

   /**
    * Handles a command which is given by the user
    * @param args - The arguments which the user provided
    * @return True is returned if the command was found
    */
   public boolean handleCommand(String args[]) {
      /*if (args == null || args[0].equalsIgnoreCase("")) {
         return false;
      }
      Object value[] = commands.get(args[0]);
      if (value == null) {
         return false;
      }
      try {
         Method command = (Method) value[1];
         SPCPlugin plugin = (SPCPlugin) value[0];
         command.invoke(plugin, (Object) args);
      } catch (Exception e) {
         PlayerHelper.printStackTrace(e);
      }
      return true;*/
      args[0] = args[0].substring(1);
      return handleCommand(args,commands);
   }

   /**
    * Handles a command for a custom command list
    * @param args - The arguments that the user gave
    * @param cmds - The custom command list
    * @return True is returned when the command is found
    */
   public boolean handleCommand(String args[], HashMap<String,Object[]> cmds) {
      if (args == null || cmds == null || args[0].equalsIgnoreCase("")) {
         return false;
      }
      Object value[] = cmds.get(args[0]);
      if (value == null || value.length != 3) {
         return false;
      }
      try {
         Method command = (Method) value[1];
         SPCPlugin plugin = (SPCPlugin) value[0];
         command.invoke(plugin, (Object) args);
      } catch (Exception e) {
         PlayerHelper.printStackTrace(e);
      }
      return true;
   }

   /**
    * Gets a list of all plugin based commands
    * @return A list containing all the commands from plugins
    */
   public String[] getCommands() {
      String help[] = new String[commands.size()];
      Iterator<String> i = commands.keySet().iterator();
      for (int j = 0; j < help.length; j++) {
         help[j] = i.next();
      }
      Vector<List<String>> v = new Vector<List<String>>();
      int count = 0;
      for (SPCPlugin plugin : plugins) {
         List<String> temp = plugin.getCommands();
         if (temp != null) {
            count += temp.size();
            v.add(temp);
         }
      }
      String allhelp[] = new String[count + help.length];
      int pointer = 0;
      for (String h : help) {
         allhelp[pointer++] = h;
      }
      Iterator<List<String>> it = v.iterator();
      while (it.hasNext()) {
         Iterator<String> l = it.next().iterator();
         while (l.hasNext()) {
            allhelp[pointer++] = l.next();
         }
      }
      return allhelp;
   }

   /**
    * Gets help on the specified plugin command
    * @param command - The command to get help on
    * @return A String containing help information
    */
   public String[] getHelp(String command) {
      if (command == null || command.equalsIgnoreCase("")) {
         return null;
      }
      Object value[] = commands.get(command);
      if (value == null) {
         for (SPCPlugin plugin : plugins) {
            String help[] = null;
            if ((help = plugin.getHelp(command)) != null) {
               return help;
            }
         }
         return null;
      }
      try {
         SPCCommand info = (SPCCommand)value[2];
         return new String[] {info.help(), info.args(), info.example()};
      } catch (Exception e) {
         PlayerHelper.printStackTrace(e);
      }
      return null;
   }

   /**
    * Gets the list of loaded plugins
    * 
    * @return The list of loaded plugins
    */
   public List<SPCPlugin> getPlugins() {
      return plugins;
   }

   /**
    * Gets the list of disabled plugins
    * 
    * @return The list of disabled plugins
    */
   public List<SPCPlugin> getDisabledPlugins() {
      return disabledplugins;
   }

   /**
    * This method disables the specified plugin and returns true if successful.
    * 
    * @param plugin the plugin to disable
    * @return true if the specified plugin was disabled
    */
   public boolean disablePlugin(SPCPlugin plugin) {
      if (plugins.remove(plugin)) {
         disabledplugins.add(plugin);
         return true;
      }
      return false;
   }

   /**
    * This method enables the specified plugin and returns true if successful.
    * 
    * @param plugin the plugin to enable
    * @return true if the specified plugin was enabled
    */
   public boolean enablePlugin(SPCPlugin plugin) {
      if (disabledplugins.remove(plugin)) {
         plugins.add(plugin);
         return true;
      }
      return false;
   }

   /**
    * Gets an array of plugins based on the name of the plugin
    * 
    * @param name of the plugin
    * @return an array of plugins which are named the same
    */
   public SPCPlugin[] getPlugin(String name) {
      Vector<SPCPlugin> temp = new Vector<SPCPlugin>();
      for (SPCPlugin plugin : plugins) {
         if (plugin.getName() == null) {
            continue;
         }
         if (plugin.getName().equalsIgnoreCase(name)) {
            temp.add(plugin);
         }
      }
      return temp.toArray(new SPCPlugin[temp.size()]);
   }
}
