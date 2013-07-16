package net.minecraft.src;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import com.sk89q.worldedit.WorldEdit;

/**
 * This class is used to setup all the interfaces and core objects which 
 * WorldEdit loads from the JAR and uses. 
 * 
 * @author simo_415
 * Copyright (C) 2010-2011 simo_415 - (http://bit.ly/spcmod) 
 * 
 *  This file is part of Single Player Commands.
 *
 *  Single Player Commands is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Single Player Commands is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Single Player Commands. If not, see <http://www.gnu.org/licenses/>.
 */
public class WorldEditPlugin {

   public WorldEdit controller;
   
   public Constructor<?> worldvector;
   
   public Method handleCommand;
   public Method handleBlockLeftClick;
   public Method handleBlockRightClick;
   public Method handleArmSwing;
   public Method handleRightClick;
   
   public SPCLocalPlayer localplayer;
   public SPCServerInterface serverinterface;
   public SPCLocalConfiguration localconfiguration;
   
   /**
    * WorldEdit constructor which initialises the WorldEdit interfaces
    * @throws Exception - When a problem occurs loading the WorldEdit classes
    * and interfaces
    */
   public WorldEditPlugin() throws Exception {
      localconfiguration = new SPCLocalConfiguration(Minecraft.getMinecraft().mcDataDir);
      localconfiguration.load();
      EntityPlayerSP player = PlayerHelper.PH.ep;
      serverinterface = new SPCServerInterface(player.worldObj);
      Class<?> localworld;

      try {
         Class<?> wec = Class.forName("com.sk89q.worldedit.WorldEdit");
         controller = (WorldEdit)wec.getConstructors()[0].newInstance(new Object[]{serverinterface,localconfiguration});
         Method m[] = wec.getMethods();
         for (int i = 0; i < m.length; i++) {
            if (m[i].getName().compareTo("handleCommand") == 0) {
               handleCommand = m[i];
            } else if (m[i].getName().compareTo("handleBlockLeftClick") == 0) {
               handleBlockLeftClick = m[i];
            } else if (m[i].getName().compareTo("handleBlockRightClick") == 0) {
               handleBlockRightClick = m[i];
            } else if (m[i].getName().compareTo("handleRightClick") == 0) {
               handleRightClick = m[i];
            } else if (m[i].getName().compareTo("handleArmSwing") == 0) {
               handleArmSwing = m[i];
            }
         }
      } catch (Exception e) {
         e.printStackTrace();
         throw new Exception("Couldnt find the WorldEdit method.");
      }
      
      try {
         Class<?> worldvector = Class.forName("com.sk89q.worldedit.WorldVector");
         localworld = Class.forName("com.sk89q.worldedit.LocalWorld");
         this.worldvector = worldvector.getConstructor(localworld,int.class,int.class,int.class);
      } catch (Exception e) {
         e.printStackTrace();
         throw new Exception("Couldnt find the WorldVector method.");
      }
   }
   
   /**
    * Gets the command controller
    * @return The command controller
    */
   public Object getController() {
      return controller;
   }

   /**
    * Gets the vector which the world uses. This is used for the mouse button 
    * processing
    * @return The world vector
    */
   public Constructor<?> getWorldvector() {
      return worldvector;
   }
   
   /**
    * Gets the method which handles commands
    * @return The method which handles commands
    */
   public Method getHandleCommand() {
      return handleCommand;
   }

   /**
    * Gets the method which handles block left clicks
    * @return The method which handles left clicks
    */
   public Method getHandleBlockLeftClick() {
      return handleBlockLeftClick;
   }

   /**
    * Gets the method which handles block right clicks
    * @return The method which handles right clicks
    */
   public Method getHandleBlockRightClick() {
      return handleBlockRightClick;
   }

   /**
    * Gets the method which handles the player arm swing
    * @return The method which handles arm swings
    */
   public Method getHandleArmSwing() {
      return handleArmSwing;
   }

   /**
    * Gets the method which handles right clicks
    * @return The method which handles right clicks
    */
   public Method getHandleRightClick() {
      return handleRightClick;
   }
   
   /**
    * Sets the Local Player interface to the updated version
    * @param ep - The EntityPlayer object to create the new interface out of
    */
   public void setPlayer(EntityPlayerSPSPC ep) {
      localplayer = new SPCLocalPlayer(serverinterface,ep);
      controller.getSession(localplayer).setCUISupport(true);
      controller.getSession(localplayer).dispatchCUISetup(localplayer);
   }

   /**
    * Gets the player interface, this is used in the command handling methods
    * @return The player interface
    */
   public SPCLocalPlayer getPlayer() {
      return localplayer;
   }

   /**
    * Gets the server interface which is used for command processing
    * @return The server interface
    */
   public SPCServerInterface getServerinterface() {
      return serverinterface;
   }

   /**
    * Gets the single player configuration
    * @return The single player configuration 
    */
   public SPCLocalConfiguration getLocalconfiguration() {
      return localconfiguration;
   }
}
