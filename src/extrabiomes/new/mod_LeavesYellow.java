package net.minecraft.src;

public class mod_LeavesYellow extends BaseMod
{
   
public static final Block leavesYellow;
	
	static
	
	{
		leavesYellow = (new BlockLeavesYellow(158, 0)).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep).setBlockName("leavesYellow").setRequiresSelfNotify();
		
    }
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(leavesYellow);
      leavesYellow.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/leavesyellow.png");
      ModLoader.addName(leavesYellow, "Leaves Yellow");
   }
}