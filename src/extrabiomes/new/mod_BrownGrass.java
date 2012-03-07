package net.minecraft.src;

public class mod_BrownGrass extends BaseMod
{
   public static Block brownGrass = new BlockBrownGrass(163, 0).setHardness(0F).setResistance(0F).setBlockName("brownGrass").setStepSound(Block.soundGrassFootstep);
   public static Block brownGrassShort = new BlockBrownGrass(164, 0).setHardness(0F).setResistance(0F).setBlockName("brownGrassShort").setStepSound(Block.soundGrassFootstep);
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(brownGrass);
      ModLoader.registerBlock(brownGrassShort);
      brownGrass.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/browngrass.png");
      brownGrassShort.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/browngrassshort.png");
      ModLoader.addName(brownGrass, "Brown Grass");
      ModLoader.addName(brownGrassShort, "Brown Grass");
   }
}