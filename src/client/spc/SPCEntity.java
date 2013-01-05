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

public class SPCEntity implements SPCEntityInterface {

   private Entity e;
   
   public SPCEntity(Entity e) {
      this.e = e;
   }
   
   @Override
   public void dropItem(int id, int damage) {
      e.dropItem(id, damage);
   }

   @Override
   public int getAir() {
      return e.getAir();
      //return e.air;
   }

   @Override
   public int getFire() {
      return e.fireResistance;
      //return e.fire;
   }

   @Override
   public SPCPoint getMotion() {
      return new SPCPoint(e.motionX,e.motionY,e.motionZ);
   }

   @Override
   public float getPitch() {
      return e.rotationPitch;
   }

   @Override
   public SPCPoint getPosition() {
      return new SPCPoint(e.posX,e.posY,e.posZ);
   }

   @Override
   public SPCWorldInterface getWorld() {
      return new SPCWorld(e.worldObj);
   }

   @Override
   public float getYaw() {
      return e.rotationYaw;
   }

   @Override
   public void kill() {
      e.kill();
   }

   @Override
   public void setNoclip(boolean noclip) {
      e.noClip = noclip;
   }
   
   @Override
   public boolean getNoclip() {
      return e.noClip;
   }

   @Override
   public void setAir(int air) {
      e.setAir(air);
      //e.air = air;
   }

   @Override
   public void setFire(int fire) {
      //e.fire = fire;
      e.fireResistance = fire;
   }

   @Override
   public void setMotion(SPCPoint motion) {
      e.setVelocity(motion.dx, motion.dy, motion.dz);
   }

   @Override
   public void setPitch(float pitch) {
      e.rotationPitch = pitch;
   }

   @Override
   public void setPosition(SPCPoint position) {
      e.setPosition(position.dx, position.dy, position.dz);
   }

   @Override
   public void setYaw(float yaw) {
      e.rotationYaw = yaw;
   }
   
   @Override
   public void setRotation(float yaw, float pitch) {
      e.setRotation(yaw, pitch);
   }
   
   @Override
   public Entity getRawEntity() {
      return e;
   }

   @Override
   public boolean getImmuneToFire() {
      return e.isImmuneToFire;
   }

   @Override
   public void setImmuneToFire(boolean immune) {
      e.isImmuneToFire = immune;
   }
}
