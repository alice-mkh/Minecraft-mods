package net.minecraft.src;

import java.io.File;

/**
 * DESCRIPTION HERE
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
public class SPCWorld implements SPCWorldInterface {

   private World world;
   private WorldInfo wi;
   
   public SPCWorld(World world) {
      this.world = world;
      this.wi = world.worldInfo;
   }

   @Override
   public int getBlock(SPCPoint position, int block) {
      return world.getBlockId(position.ix, position.iy, position.iz);
   }

   @Override
   public int getMetadata(SPCPoint position, int block) {
      return world.getBlockMetadata(position.ix, position.iy, position.iz);
   }

   @Override
   public SPCPoint getSpawn() {
      return new SPCPoint(wi.getSpawnX(),wi.getSpawnY(),wi.getSpawnZ());
   }

   @Override
   public long getTime() {
      return wi.getWorldTime();
   }

   @Override
   public File getWorldDir() {
      ISaveHandler sh = ((ISaveHandler) world.saveHandler);
      if (sh instanceof SaveHandler) {
         return ((SaveHandler) sh).getSaveDirectory();
      } else {
         return new File("");
      }
   }

   @Override
   public void setBlock(SPCPoint position, int block) {
      world.setBlock(position.ix, position.iy, position.iz, block);
   }

   @Override
   public void setBlockWithNotify(SPCPoint position, int block) {
      world.setBlockWithNotify(position.ix, position.iy, position.iz, block);
   }

   @Override
   public void setMetadata(SPCPoint position, int data) {
      world.setBlockMetadata(position.ix, position.iy, position.iz, data);
   }

   @Override
   public void setMetadataWithNotify(SPCPoint position, int data) {
      world.setBlockMetadataWithNotify(position.ix, position.iy, position.iz, data);
   }

   @Override
   public void setSpawn(SPCPoint position) {
      wi.setSpawnPosition(position.ix, position.iy, position.iz);
      /*wi.func_22294_a(position.ix);
      wi.func_22308_b(position.iy);
      wi.func_22298_c(position.iz);*/
   }

   @Override
   public void setTime(long time) {
      wi.setWorldTime(time);
   }
   
}
