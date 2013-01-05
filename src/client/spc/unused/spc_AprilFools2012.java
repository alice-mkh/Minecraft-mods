package net.minecraft.src;

import java.awt.Desktop;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import net.minecraft.client.Minecraft;


public class spc_AprilFools2012 extends SPCPlugin {

   public boolean active;
   public boolean alert;
   public File destfile;

   public spc_AprilFools2012() {
      destfile = new File(Minecraft.getMinecraftDir(),"resources/sound/spcfools.mid");
      /* Checked if April 1st 2012 - otherwise delete the sound file if it exists */
      /* if (System.currentTimeMillis() > 1333198800000L && System.currentTimeMillis() < 1333288799000L) {
         active = true;
      } else if (destfile.exists()) {
         destfile.delete();
      }*/
      active = true;
      alert = false;
   }

   public void setup() {
      try {
         String filename = "SPCPatch.class";
         File destination = destfile.getParentFile();
         if (PlayerHelper.extractFile(new File(Minecraft.getMinecraftDir(),"bin/Minecraft.jar"),filename,destination)) {
            File destfile1 = new File(destination,filename);
            destfile1.renameTo(destfile);
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public String getVersion() {
      return "1.0";
   }

   @Override
   public String getName() {
      return "roll";
   }

   @Override
   public boolean handleCommand(String args[]) {
      if (active) {
         if (args[0].equalsIgnoreCase("roll") || args[0].equalsIgnoreCase("/roll")) {
            if (!destfile.exists()) {
               setup();
            }
            ph.mc.sndManager.addSound(destfile.getName(), destfile);
            ph.mc.sndManager.playSoundFX("spcfools",1.0F,1.0F);
            return true;
         }
         if (args.length == 2 && (args[0].equalsIgnoreCase("help") || args[0].equalsIgnoreCase("/help")) && args[1].equalsIgnoreCase("roll")) {
            if (Desktop.isDesktopSupported()) {
               Desktop d = Desktop.getDesktop();
               try {
                  d.browse( new URI("http://www.youtube.com/watch?v=oHg5SJYRHA0") );
               } catch (Exception e) { 
                  e.printStackTrace();
               }
            }
         }
      }
      return false;
   }
   
   @Override
   public void atUpdate() {
      if (active && alert) {
         ph.sendMessage("\2477New Command added! \2476/roll \2475Try it out today! ");
         alert = false;
      }
   }
}
