package net.minecraft.src;

public class mod_CrackedSand extends BaseMod
{
   public static Block crackedSand = new BlockCrackedSand(126, 0).setHardness(1.2F).setResistance(0F).setBlockName("crackedSand").setStepSound(Block.soundStoneFootstep);
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(crackedSand);
      crackedSand.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/crackedsand.png");
      ModLoader.addName(crackedSand, "Cracked Sand");
   }
}