package net.minecraft.src;

import java.io.File;
import java.io.IOException;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * The plugin class for WorldEdit. It uses the WorldEditPlugin class to load
 * all the interfaces and variables found in WorldEdit.jar
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
public class spc_WorldEdit extends SPCPlugin {
   public int blockrightx;
   public int blockrighty;
   public int blockrightz;
   public int blockleftx;
   public int blocklefty;
   public int blockleftz;

   public static File WORLDEDITJAR = new File(Minecraft.getMinecraft().mcDataDir,"WorldEdit.jar");
   public static File RHINOJAR = new File(Minecraft.getMinecraft().mcDataDir,"rhino.jar");

   public static boolean CANINITIALISE = true;

   public WorldEditPlugin WEP;

   static {
/*      if (!WORLDEDITJAR.exists()) { // Attempt to extract it from Minecraft.jar as noobs can't read instructions
         PlayerHelper.extractFile(new File(Minecraft.getMinecraft().mcDataDir,"bin/Minecraft.jar"), "WorldEdit.jar", new File(Minecraft.getMinecraft().mcDataDir,""));
      }*/
      if (!PlayerHelper.addToClasspath(WORLDEDITJAR)) {
         CANINITIALISE = false;
      }
      PlayerHelper.addToClasspath(RHINOJAR);
   }

   /**
    * @throws Exception If WorldEdit cannot be initialised. This is generally
    * caused by WorldEdit.jar not being in the correct location
    */
   public spc_WorldEdit() throws Exception { 
      if (!CANINITIALISE) {
         throw new Exception("Check WorldEdit.jar is in the correct location");
      }
      if (WEP == null) {
         WEP = new WorldEditPlugin();
      }
   }
   
   /**
    * Overwrites default behavior to intercept call and update the WorldEdit
    * player interface
    * @see net.minecraft.src.SPCPlugin#setPlayerHelper(net.minecraft.src.PlayerHelper)
    */
   public void setPlayerHelper(PlayerHelper ph) {
      super.setPlayerHelper(ph);
      WEP.setPlayer(ph.ep);
   }

   /**
    * Processes the command that the user issued.
    * @param args The arguments that the user issued.
    * @return If the command was found true is returned
    */
   @Override
   public boolean handleCommand(String args[]) {
      if (!args[0].startsWith("//")){
         return false;
      }
      try {
         if ((Boolean)WEP.getHandleCommand().invoke(WEP.getController(), new Object[]{WEP.getPlayer(),args.clone()})) {
            return true;
         } else {
            return false;
         }
      } catch (Throwable t) {
         t.printStackTrace();
         return false;
      }
   }

   /**
    * Handle left click
    */
   @Override
   public void handleLeftButtonDown(SPCObjectHit o) {
      if (o == null || o.blocky < 0) {
         return;
      }
      try {
         WEP.getHandleArmSwing().invoke(WEP.getController(), new Object[]{WEP.getPlayer()});
         if ((o.blockx != blockleftx || o.blocky != blocklefty || o.blockz != blockleftz) && (o.blocky > -1)) {
            Object vector = WEP.getWorldvector().newInstance(WEP.getPlayer().getWorld(),o.blockx,o.blocky,o.blockz);
            WEP.getHandleBlockLeftClick().invoke(WEP.getController(), new Object[]{WEP.getPlayer(),vector});
            blockleftx = o.blockx;
            blocklefty = o.blocky;
            blockleftz = o.blockz;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   /**
    * Handle right click
    * @throws Exception is thrown when the command was unable to run
    */
   @Override
   public void handleRightButtonDown(SPCObjectHit o) {
      if (o == null || o.blocky < 0) {
         return;
      }
      try {
         WEP.getHandleRightClick().invoke(WEP.getController(), new Object[]{WEP.getPlayer()});
         if ((o.blockx != blockrightx || o.blocky != blockrighty || o.blockz != blockrightz) && (o.blocky > -1)) {
            Object vector = WEP.getWorldvector().newInstance(WEP.getPlayer().getWorld(),o.blockx,o.blocky,o.blockz);
            WEP.getHandleBlockRightClick().invoke(WEP.getController(), new Object[]{WEP.getPlayer(),vector});
            blockrightx = o.blockx;
            blockrighty = o.blocky;
            blockrightz = o.blockz;
         }
      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   @Override
   public String getVersion() {
      return "1.3";
   }

   @Override
   public String getName() {
      return "WorldEditPlugin";
   }

   @SPCCommand (cmd="/",help="")
   public void empty(String[] args) {
      handleCommand(args);
   }

//.s [args...]
//<script>.js [args...]

/*Obviously, I didn't type all the following methods by hand. Instead, I typed this:
   cmd limit "<limit>"
   cmd brush "<sphere|cylinder|clipboard|smooth|gravity>"
   cmd mask "[mask]"
   cmd mat "<mat>"
   cmd size "[size]"
   cmd chunkinfo ""
   cmd delchunks ""
   cmd listchunks ""
   cmd clearclipboard ""
   cmd copy ""
   cmd cut ""
   cmd flip "[dir]"
   cmd load "<filename>"
   cmd paste "[-ao]"
   cmd rotate "<angle-in-degrees>"
   cmd save "<filename>"
   cmd search "<item>"
   cmd worldedit "<reload|tz|version>"
   cmd cyl "<block> <radius> [height]"
   cmd forestgen "[size] [type] [density]"
   cmd hcyl "<block> <radius> [height]"
   cmd hsphere "<block> <radius> [raised?]"
   cmd pumpkins "[size]"
   cmd sphere "<block> <radius> [raised?]"
   cmd ascend ""
   cmd ceil "[clearance]"
   cmd descend ""
   cmd jumpto ""
   cmd thru ""
   cmd unstuck ""
   cmd up "[distance]"
   cmd clearhistory ""
   cmd redo "[num-steps]"
   cmd undo "[num-steps]"
   cmd move "[count] [direction] [leave-id]"
   cmd overlay "<block>"
   cmd outline "<block>"
   cmd regen ""
   cmd replace "[from-block] <to-block>"
   cmd set "<block>"
   cmd smooth "[iterations]"
   cmd stack "[count] [direction]"
   cmd walls "<block>"
   cmd cs "<script> [args...]"
   cmd chunk ""
   cmd contract "<amount> [[direction]|<reverse-amount> [direction]]"
   cmd count "<block>"
   cmd distr "[-c]"
   cmd expand "<<amount> [[direction]|<reverse-amount> [direction]]|<vert>>"
   cmd hpos1 ""
   cmd hpos2 ""
   cmd inset "[-hv] <amount>"
   cmd outset "[-hv] <amount>"
   cmd pos1 ""
   cmd pos2 ""
   cmd sel "<cuboid or poly>"
   cmd shift "<amount> [direction]"
   cmd toggleeditwand ""
   cmd wand ""
   cmd restore "[snapshot]"
   cmd snap "<before <date>|after <date>|list [num]|use <snapshot>>"
   cmd sp "<area <range>|recur <range>|single>"
   cmd cycler ""
   cmd info ""
   cmd none ""
   cmd repl "<block>"
   cmd tree "[type]"
   cmd butcher "[radius]"
   cmd drain "<radius>"
   cmd ex "[size]"
   cmd fill "<block> <radius> [depth]"
   cmd fillr "<block> <radius>"
   cmd fixlava "<radius>"
   cmd fixwater "<radius>"
   cmd remove "<type> <radius>"
   cmd removeabove "[size] [height]"
   cmd removebelow "[size] [height]"
   cmd removenear "[block] [size]"
   cmd replacenear "<size> <from-id> <to-id>"
   cmd snow "[radius]"
   cmd thaw "[radius]"
   cmd toggleplace ""
and replaced this regexp pattern:
^( {,})(cmd) (.{,}) (".{,}")( //)?$
with this:
\1@SPCCommand \(\2="/\3",help=\4)\5\n\1public void \3\(String\[\] args\) \{\n\1\1handleCommand\(args\);\n\1\}\n
That's all.*/
   @SPCCommand (cmd="/limit",help="<limit>")
   public void limit(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/brush",help="<sphere|cylinder|clipboard|smooth|gravity>")
   public void brush(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/mask",help="[mask]")
   public void mask(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/mat",help="<mat>")
   public void mat(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/size",help="[size]")
   public void size(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/chunkinfo",help="")
   public void chunkinfo(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/delchunks",help="")
   public void delchunks(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/listchunks",help="")
   public void listchunks(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/clearclipboard",help="")
   public void clearclipboard(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/copy",help="")
   public void copy(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/cut",help="")
   public void cut(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/flip",help="[dir]")
   public void flip(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/load",help="<filename>")
   public void load(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/paste",help="[-ao]")
   public void paste(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/rotate",help="<angle-in-degrees>")
   public void rotate(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/save",help="<filename>")
   public void save(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/search",help="<item>")
   public void search(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/worldedit",help="<reload|tz|version>")
   public void worldedit(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/cyl",help="<block> <radius> [height]")
   public void cyl(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/forestgen",help="[size] [type] [density]")
   public void forestgen(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/hcyl",help="<block> <radius> [height]")
   public void hcyl(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/hsphere",help="<block> <radius> [raised?]")
   public void hsphere(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/pumpkins",help="[size]")
   public void pumpkins(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/sphere",help="<block> <radius> [raised?]")
   public void sphere(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/ascend",help="")
   public void ascend(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/ceil",help="[clearance]")
   public void ceil(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/descend",help="")
   public void descend(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/jumpto",help="")
   public void jumpto(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/thru",help="")
   public void thru(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/unstuck",help="")
   public void unstuck(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/up",help="[distance]")
   public void up(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/clearhistory",help="")
   public void clearhistory(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/redo",help="[num-steps]")
   public void redo(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/undo",help="[num-steps]")
   public void undo(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/move",help="[count] [direction] [leave-id]")
   public void move(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/overlay",help="<block>")
   public void overlay(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/outline",help="<block>")
   public void outline(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/regen",help="")
   public void regen(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/replace",help="[from-block] <to-block>")
   public void replace(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/set",help="<block>")
   public void set(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/smooth",help="[iterations]")
   public void smooth(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/stack",help="[count] [direction]")
   public void stack(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/walls",help="<block>")
   public void walls(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/cs",help="<script> [args...]")
   public void cs(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/chunk",help="")
   public void chunk(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/contract",help="<amount> [[direction]|<reverse-amount> [direction]]")
   public void contract(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/count",help="<block>")
   public void count(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/distr",help="[-c]")
   public void distr(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/expand",help="<<amount> [[direction]|<reverse-amount> [direction]]|<vert>>")
   public void expand(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/hpos1",help="")
   public void hpos1(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/hpos2",help="")
   public void hpos2(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/inset",help="[-hv] <amount>")
   public void inset(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/outset",help="[-hv] <amount>")
   public void outset(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/pos1",help="")
   public void pos1(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/pos2",help="")
   public void pos2(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/sel",help="<cuboid or poly>")
   public void sel(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/shift",help="<amount> [direction]")
   public void shift(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/toggleeditwand",help="")
   public void toggleeditwand(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/wand",help="")
   public void wand(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/restore",help="[snapshot]")
   public void restore(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/snap",help="<before <date>|after <date>|list [num]|use <snapshot>>")
   public void snap(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/sp",help="<area <range>|recur <range>|single>")
   public void sp(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/cycler",help="")
   public void cycler(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/info",help="")
   public void info(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/none",help="")
   public void none(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/repl",help="<block>")
   public void repl(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/tree",help="[type]")
   public void tree(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/butcher",help="[radius]")
   public void butcher(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/drain",help="<radius>")
   public void drain(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/ex",help="[size]")
   public void ex(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/fill",help="<block> <radius> [depth]")
   public void fill(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/fillr",help="<block> <radius>")
   public void fillr(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/fixlava",help="<radius>")
   public void fixlava(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/fixwater",help="<radius>")
   public void fixwater(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/remove",help="<type> <radius>")
   public void remove(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/removeabove",help="[size] [height]")
   public void removeabove(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/removebelow",help="[size] [height]")
   public void removebelow(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/removenear",help="[block] [size]")
   public void removenear(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/replacenear",help="<size> <from-id> <to-id>")
   public void replacenear(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/snow",help="[radius]")
   public void snow(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/thaw",help="[radius]")
   public void thaw(String[] args) {
      handleCommand(args);
   }

   @SPCCommand (cmd="/toggleplace",help="")
   public void toggleplace(String[] args) {
      handleCommand(args);
   }

}
