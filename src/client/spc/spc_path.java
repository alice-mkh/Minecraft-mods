package net.minecraft.src;

/**
 * Single Player Commands (http://bit.ly/spcmod) Plugin
 * 
 * Adds the path command
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

public class spc_path extends SPCPlugin {

   public int block;
   public int size;
   
   private int prevx;
   private int prevy;
   private int prevz;
   
   public spc_path() {
      block = -1;
      size = 3;
      prevx = -1;
      prevy = -1;
      prevz = -1;
   }
   
   @Override
   public String getVersion() {
      return "1.0";
   }

   @Override
   public String getName() {
      return "Path";
   }

   @SPCCommand (cmd="path",help="[ITEMCODE|ITEMNAME] [SIZE]")
   public void path(String[] args){
      if (args.length > 1) {
         int block = -1;
         int size = -1;
         
         try {
            block = Integer.parseInt(args[1]);
         } catch (NumberFormatException e) {
            block = ph.getBlockID(args[1]);
         }
         
         if (args.length > 2) {
            try {
               size = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
               size = -1;
            }
         }
         
         if (size > 0) {
            this.size = size;
         }
         if (block > -1) {
            this.block = block;
            ph.sendMessage("Path mode enabled.");
         }
      } else {
         if (block > -1) {
            block = -1;
            ph.sendMessage("Path mode disabled.");
         }
      }
   }

   @Override
   public void atUpdate() {
      if (block < 0) {
         return;
      }
      
      int x = MathHelper.floor_double(ph.ep.posX);
      int y = MathHelper.floor_double(ph.ep.posY) - 1;
      int z = MathHelper.floor_double(ph.ep.posZ);
      
      if (x == prevx && y == prevy && z == prevz) {
         return;
      }
      
      int start = (size * -1) + 1;
      
      for (int i = start; i < size; i++) {
         for (int j = -1; j < size; j++) {
            for (int k = start; k < size; k++) {
               if (j == -1) {
                  setBlock(x+i,y+j,z+k,block);
               } else {
                  setBlock(x+i,y+j,z+k,0);
               }
            }  
         }
      }
      prevx = x;
      prevy = y;
      prevz = z;
   }
   
   private void setBlock(int i, int j, int k, int type) {
      ph.mc.theWorld.setBlockWithNotify(i,j,k,type);
   }
}
