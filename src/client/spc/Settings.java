package net.minecraft.src;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

/**
 * This class handles settings stored to a file by the SPC mod
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
public class Settings extends Properties {

   private static final long serialVersionUID = 1L;
   private File settings;

   public Settings() {
      super();
   }
   
   public Settings(File f) {
      this(f, true);
   }
   
   public Settings(File f, boolean load) {
      super();
      this.settings = f;
      if (load) {
         load(f);
      }
   }
   
   public void set(String key, boolean value) {
      setProperty(key,new Boolean(value).toString());
   }
   
   public boolean getBoolean(String key, boolean base) {
      String value = getProperty(key);
      try {
         return (value == null || value.trim().equalsIgnoreCase("")) ? base : new Boolean(value);
      } catch (Exception e) {
         return base;
      }
   }
   
   public void set(String key, int value) {
      setProperty(key,new Integer(value).toString());
   }
   
   public int getInteger(String key, int base) {
      String value = getProperty(key);
      try {
         return isEmpty(value) ? base : new Integer(value);
      } catch (NumberFormatException e) {
         return base;
      }
   }
   
   public void set(String key, char value) {
      setProperty(key,new Character(value).toString());
   }
   
   public char getCharacter(String key, char base) {
      String value = getProperty(key);
      try {
         return isEmpty(value) ? base : value.charAt(0);
      } catch (NumberFormatException e) {
         return base;
      }
   }
   
   public void set(String key, double value) {
      setProperty(key,new Double(value).toString());
   }
   
   public double getDouble(String key, double base) {
      String value = getProperty(key);
      try {
         return isEmpty(value) ? base : new Double(value);
      } catch (NumberFormatException e) {
         return base;
      }
   }
   
   public void set(String key, float value) {
      setProperty(key,new Float(value).toString());
   }
   
   public float getFloat(String key, float base) {
      String value = getProperty(key);
      try {
         return isEmpty(value) ? base : new Float(value);
      } catch (NumberFormatException e) {
         return base;
      }
   }
   
   public void set(String key, String value) {
      setProperty(key,value);
   }
   
   public String getString(String key, String base) {
      String value = getProperty(key);
      return isEmpty(value) ? base : value;
   }
   
   public boolean save() {
      return this.save("");
   }
   
   public boolean save(String header) {
      return this.save(settings,header);
   }
   
   public boolean save(File file, String header) {
      if (file == null || file.isDirectory()) {
         return false;
      }
      try {
         if (!file.exists()) {
            file.createNewFile();
         }
         FileOutputStream fos = new FileOutputStream(file);
         super.store(fos, header);
         fos.close();
         return true;
      } catch (Exception e) {
         return false;
      }
   }
   
   public boolean load() {
      return this.load(settings);
   }
   
   public boolean load(File file) {
      if (file == null || file.isDirectory()) {
         return false;
      }
      try {
         if (!file.exists()) {
            file.createNewFile();
            return true;
         }
         super.load(new FileInputStream(file));
         return true;
      } catch (Exception e) {
         return false;
      }
   }
   
   public File getFile() {
      return settings;
   }
   
   public void setFile(File settings) {
      this.settings = settings;
   }
   
   private boolean isEmpty(String value) {
      return (value == null || value.trim().equalsIgnoreCase(""));
   }
}
