package net.minecraft.src;

public class mod_RedRock extends BaseMod
{
   public static Block redRock = new BlockRedRock(125, 0).setHardness(2.0F).setResistance(2.0F).setBlockName("redRock").setStepSound(Block.soundStoneFootstep);
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(redRock);
      redRock.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/redrock.png");
      ModLoader.addName(redRock, "Red Rock");
   }
}