package net.minecraft.src;

public class mod_LeavesBrown extends BaseMod
{
   
public static final BlockLeavesBrown leavesBrown;
	
	static
	
	{
		leavesBrown = (BlockLeavesBrown)(new BlockLeavesBrown(155, 0)).setHardness(0.2F).setLightOpacity(1).setStepSound(Block.soundGrassFootstep).setBlockName("leaves").setRequiresSelfNotify();
		
    }
   
   public String getVersion()
   {
      return "1.2.3";
   }
   
   public void load()
   {
      ModLoader.registerBlock(leavesBrown);
      leavesBrown.blockIndexInTexture = ModLoader.addOverride("/terrain.png", "/extrabiomes/leavesbrown.png");
      ModLoader.addName(leavesBrown, "Leaves Brown");
   }
}