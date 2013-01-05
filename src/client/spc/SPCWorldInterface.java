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
public interface SPCWorldInterface {
   public void setBlock(SPCPoint position, int block);
   public void setBlockWithNotify(SPCPoint position, int block);
   public void setMetadata(SPCPoint position, int block);
   public void setMetadataWithNotify(SPCPoint position, int block);
   public int getBlock(SPCPoint position, int block);
   public int getMetadata(SPCPoint position, int block);
   public SPCPoint getSpawn();
   public void setSpawn(SPCPoint position);
   public File getWorldDir();
   public long getTime();
   public void setTime(long time);
}
