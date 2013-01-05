package net.minecraft.src;

/**
 * Class provides a storage method for an x,y,z point on the map
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
public class SPCPoint {
   public int ix;
   public int iy;
   public int iz;
   public double dx;
   public double dy;
   public double dz;
   
   public SPCPoint(int x, int y, int z) {
      this.set(x,y,z);
   }
   
   public SPCPoint(double x, double y, double z) {
      this.set(x,y,z);
   }
   
   public void setX(int x) {
      ix = x;
      dx = x;
   }
   
   public void setX(double x) {
      ix = SPCPoint.getInt(x);
      dx = x;
   }
   
   public void setY(int y) {
      iy = y;
      dy = y;
   }
   
   public void setY(double y) {
      iy = SPCPoint.getInt(y);
      dy = y;
   }
   
   public void setZ(int z) {
      iz = z;
      dz = z;
   }
   
   public void setZ(double z) {
      iz = SPCPoint.getInt(z);
      dz = z;
   }
   
   public void set(int x, int y, int z) {
      ix = x;
      iy = y;
      iz = z;
      dx = x;
      dy = y;
      dz = z;
   }
   
   public void set(double x, double y, double z) {
      ix = SPCPoint.getInt(x);
      iy = SPCPoint.getInt(y);
      iz = SPCPoint.getInt(z);
      dx = x;
      dy = y;
      dz = z;
   }
   
   public static int getInt(double value) {
      int i = (int)value;
      return value >= (double)i ? i : i - 1;
   }
}
