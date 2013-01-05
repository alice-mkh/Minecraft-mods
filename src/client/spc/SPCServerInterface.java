package net.minecraft.src;

import com.sk89q.worldedit.BiomeTypes;


/**
 * Instantiates the WorldEdit server class which WorldEdit uses to determine
 * the validity of various things in the current environment.
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
public class SPCServerInterface extends com.sk89q.worldedit.ServerInterface {

   /**
    * The world class
    */
   public World world;
   
   /**
    * @param world The world class
    */
   public SPCServerInterface(World world) {
      super();
      this.world = world;
   }

   /**
    * @see com.sk89q.worldedit.ServerInterface#isValidMobType(java.lang.String)
    */
   @Override
   public boolean isValidMobType(String arg0) {
      return true;
   }

   /**
    * @see com.sk89q.worldedit.ServerInterface#resolveItem(java.lang.String)
    */
   @Override
   public int resolveItem(String arg0) {
      return 0;
   }

   /**
    * @see com.sk89q.worldedit.ServerInterface#reload()
    */
   @Override
   public void reload() {      
   }

   @Override
   public BiomeTypes getBiomes() {
      // TODO Auto-generated method stub
      return null;
   }
   
   /**
    * @see com.sk89q.worldedit.ServerInterface#isValidBlockType(int)
    */
   /*@Override
   public boolean isValidBlockType(int type) {
      if (type == 0) return true;
      if (type > -1 && type < Block.blocksList.length) {
         return Block.blocksList[type] != null;
      }
      return false;
   }*/
}
