package net.minecraft.src;

import java.util.Random;

public class mod_ShortGrass extends BaseMod
{
   public static Block shortGrass = (BlockShortGrass)(new BlockShortGrass(159, 0)).setHardness(0.0F).setStepSound(Block.soundGrassFootstep).setBlockName("shortgrass");
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(shortGrass);
      shortGrass.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/shortgrass.png");
      ModLoader.addName(shortGrass, "Short Grass");
   }
}