package net.minecraft.src;

public class mod_LeavesOrange extends BaseMod
{
   
public static final Block leavesOrange;
	
	static
	
	{
		leavesOrange = (new BlockLeavesOrange(156, 0)).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep).setBlockName("leavesOrange").setRequiresSelfNotify();
		
    }
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(leavesOrange);
      leavesOrange.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/leavesorange.png");
      ModLoader.addName(leavesOrange, "Leaves Orange");
   }
}