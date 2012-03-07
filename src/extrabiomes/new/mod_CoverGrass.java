package net.minecraft.src;

public class mod_CoverGrass extends BaseMod
{
   public static Block coverGrass = new BlockCoverGrass(151, 0).setHardness(0F).setResistance(0F).setBlockName("shortGrass").setStepSound(Block.soundGrassFootstep);
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(coverGrass);
      coverGrass.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/cover.png");
      ModLoader.addName(coverGrass, "Cover Grass");
   }
}