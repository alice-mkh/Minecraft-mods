package net.minecraft.src;

public class mod_DeadGrass extends BaseMod
{
   public static Block deadGrass = new BlockDeadGrass(152, 0).setHardness(0F).setResistance(0F).setBlockName("deadGrass").setStepSound(Block.soundGrassFootstep);
   public static Block deadGrassY = new BlockDeadGrass(153, 0).setHardness(0F).setResistance(0F).setBlockName("deadGrassY").setStepSound(Block.soundGrassFootstep);
   public static Block deadTallGrass = new BlockDeadGrass(154, 0).setHardness(0F).setResistance(0F).setBlockName("deadTallGrass").setStepSound(Block.soundGrassFootstep);
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(deadGrass);
      ModLoader.registerBlock(deadGrassY);
      ModLoader.registerBlock(deadTallGrass);
      deadGrass.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/deadgrass.png");
      deadGrassY.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/deadgrass2.png");
      deadTallGrass.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/deadtallgrass.png");
      ModLoader.addName(deadGrass, "Dead Grass");
      ModLoader.addName(deadGrassY, "Dead Grass");
      ModLoader.addName(deadTallGrass, "Dead Grass");
   }
}