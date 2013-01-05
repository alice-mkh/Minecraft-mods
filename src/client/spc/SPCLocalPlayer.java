package net.minecraft.src;

import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.ServerInterface;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldVector;
import com.sk89q.worldedit.bags.BlockBag;
import com.sk89q.worldedit.blocks.BaseItemStack;
import com.sk89q.worldedit.cui.CUIEvent;

/**
 * Instantiates the WorldEdit player class which is passed into WorldEdit so
 * that WorldEdit knows how to correctly call the Single Player methods
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
public class SPCLocalPlayer extends com.sk89q.worldedit.LocalPlayer {

   /**
    * The single player class
    */
   public EntityPlayerSPSPC player;
   
   /**
    * @param server The server interface
    * @param player The single player class
    */
   public SPCLocalPlayer(ServerInterface server, EntityPlayerSPSPC player) {
      super(server);
      this.player = player;
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#getGroups()
    */
   @Override
   public String[] getGroups() {
      return new String[0];
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#getInventoryBlockBag()
    */
   @Override
   public BlockBag getInventoryBlockBag() {
      return null;
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#getItemInHand()
    */
   @Override
   public int getItemInHand() {
      ItemStack itemstack = player.inventory.getCurrentItem();
      return itemstack == null ? 0 : itemstack.itemID;
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#getName()
    */
   @Override
   public String getName() {
      return player.username;
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#getPitch()
    */
   @Override
   public double getPitch() {
      return player.rotationPitch;
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#getPosition()
    */
   @Override
   public WorldVector getPosition() {
      return new WorldVector(getWorld(),player.posX,player.posY-1,player.posZ);
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#getWorld()
    */
   @Override
   public LocalWorld getWorld() {
      return new SPCLocalWorld(player.worldObj);
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#getYaw()
    */
   @Override
   public double getYaw() {
      return player.rotationYaw;
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#giveItem(int, int)
    */
   @Override
   public void giveItem(int type, int qty) {
      BaseItemStack item = new BaseItemStack(type, qty);
      getWorld().dropItem(getPosition(), item);
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#hasPermission(java.lang.String)
    */
   @Override
   public boolean hasPermission(String perm) {
      return true;
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#print(java.lang.String)
    */
   @Override
   public void print(String arg0) {
      player.ph.sendMessage(arg0);
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#printError(java.lang.String)
    */
   @Override
   public void printError(String arg0) {
      player.ph.sendError(arg0);
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#printRaw(java.lang.String)
    */
   @Override
   public void printRaw(String arg0) {
      print(arg0);
   }
   
   /**
    * Prints a debug message
    * @param text The text to print out
    */
   @Override
   public void printDebug(String text) {
      player.ph.sendDebug(text);
   }

   /**
    * @see com.sk89q.worldedit.LocalPlayer#setPosition(com.sk89q.worldedit.Vector, float, float)
    */
   @Override
   public void setPosition(Vector pos, float pitch, float yaw) {
      player.setPositionAndRotation(pos.getX(), pos.getY() + 2, pos.getZ(), yaw, pitch);
   }
   
   /**
    * @see com.sk89q.worldedit.LocalPlayer#getBlockTrace(int)
    */
   @Override
   public WorldVector getBlockTrace(int range) {
      MovingObjectPosition m = player.rayTrace(range, 1.0F);
      return new WorldVector(getWorld(),m.blockX,m.blockY,m.blockZ);
   }
   
   /**
    * @see com.sk89q.worldedit.LocalPlayer#getSolidBlockTrace(int)
    */
   @Override
   public WorldVector getSolidBlockTrace(int range) {
      return getBlockTrace(range);
   }
   
   /**
    * @see com.sk89q.worldedit.LocalPlayer#dispatchCUIEvent(com.sk89q.worldedit.cui.CUIEvent)
    */
   @Override
   public void dispatchCUIEvent(CUIEvent event) {
       try {
          SPCPluginManager.getPluginManager().callPluginMethods(PlayerHelper.PLUGIN_HANDLECUIEVENT, event.getTypeId(), event.getParameters());
       } catch (Exception e) {
       }
   }
}
