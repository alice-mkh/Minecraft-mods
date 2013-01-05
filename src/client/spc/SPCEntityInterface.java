package net.minecraft.src;

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

public interface SPCEntityInterface {
   public void setPosition(SPCPoint position);
   public SPCPoint getPosition();
   public float getYaw();
   public void setYaw(float yaw);
   public float getPitch();
   public void setPitch(float pitch);
   public SPCWorldInterface getWorld();
   public void setNoclip(boolean noclip);
   public boolean getNoclip();
   public void setAir(int air);
   public int getAir();
   public void setFire(int fire);
   public int getFire();
   public void kill();
   public void dropItem(int id, int damage);
   public void setMotion(SPCPoint motion);
   public SPCPoint getMotion();
   public void setRotation(float yaw, float pitch);
   public Entity getRawEntity();
   public boolean getImmuneToFire();
   public void setImmuneToFire(boolean immune);
}
