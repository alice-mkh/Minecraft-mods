package net.minecraft.src;

public class mod_LeavesRed extends BaseMod
{
   
public static final Block leavesRed;
	
	static
	
	{
		leavesRed = (new BlockLeavesRed(157, 0)).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep).setBlockName("leavesRed").setRequiresSelfNotify();
		
    }
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(leavesRed);
      leavesRed.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/leavesred.png");
      ModLoader.addName(leavesRed, "Leaves Red");
   }
}