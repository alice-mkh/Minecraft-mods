package net.minecraft.src;

import java.util.List;

/**
 * This class provides a method of adding content to Minecraft and Single Player Commands.
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
public abstract class SPCPlugin {

   public PlayerHelper ph;

   /**
    * Requires a default, no parameter, plugin constructor.
    */
   public SPCPlugin() {
   }

   /**
    * This method sets the up-to-date playerhelper object.
    * 
    * @param ph - The updated player helper object
    */
   public void setPlayerHelper(PlayerHelper ph) {
      this.ph = ph;
   }

   /**
    * Returns the version of the plugin - not yet used
    * 
    * @return A string containing the version of the plugin
    */
   public abstract String getVersion();

   /**
    * Returns the name of the plugin
    * 
    * @return A string containing the name of the plugin
    */
   public abstract String getName();

   /**
    * Handles the input from the user as a command
    * 
    * @param cmd - The input from the user
    * @return True if the command was found - false otherwise
    */
   public boolean handleCommand(String cmd[]) {
      return false;
   }

   /**
    * Handles a left click event.
    * 
    * @param object - The object which the mouse hit when this event was called. If 
    * blockx, blocky and blockz = -1 no block was hit. If entity = null no entity
    * was hit
    */
   public void handleLeftClick(SPCObjectHit object) {
   }

   /**
    * Handles a right click event.
    * 
    * @param object - The object which the mouse hit when this event was called. If 
    * blockx, blocky and blockz = -1 no block was hit. If entity = null no entity
    * was hit
    */
   public void handleRightClick(SPCObjectHit object) {
   }

   /**
    * Handles a left button down event.
    * 
    * @param object - The object which the mouse hit when this event was called. If 
    * blockx, blocky and blockz = -1 no block was hit. If entity = null no entity
    * was hit
    */
   public void handleLeftButtonDown(SPCObjectHit object) {
   }

   /**
    * Handles a right button down event.
    * 
    * @param object - The object which the mouse hit when this event was called. If 
    * blockx, blocky and blockz = -1 no block was hit. If entity = null no entity
    * was hit
    */
   public void handleRightButtonDown(SPCObjectHit object) {
   }

   /**
    * Handles a CUI event. Please note that this method is not currently called by Single Player
    * commands but rather is used by WorldEdit.
    * 
    * @param typeid The type idenification of the CUI event
    * @param parameters The parameters of the CUI event
    */
   public void handleCUIEvent(String typeid, String parameters[]) {
   }

   /*
    * Not yet available
    * 
    * public void handleButtonPress(int button) { }
    * 
    * public void handleButtonDown(int button) { }
    */

   /**
    * This method is called once per frame
    * <p>
    * Anything that the plugin needs to process continuously should go in this method. Try not to
    * run loops excessively in this method, as this will slow down the game.
    */
   public void atUpdate() {
   }

   /**
    * Returns a list of commands that this mod has - not yet used
    * 
    * @return The list of commands
    */
   public List<String> getCommands() {
      return null;
   }

   /**
    * Returns a String array containing 1-3 Strings where the first argument is a description of the
    * command. The second argument is what the arguments for the command are, and the third argument
    * is an example.
    * 
    * @param commandname - The name of the command (ie: args[0])
    * @return An array of Strings containing the help information specific to that plugin
    */
   public String[] getHelp(String commandname) {
      return null;
   }
}
