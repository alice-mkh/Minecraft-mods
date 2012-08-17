package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerAdventure extends PlayerControllerSP{
    public PlayerControllerAdventure(Minecraft par1Minecraft){
        super(par1Minecraft);
    }

    public void func_6473_b(EntityPlayer par1EntityPlayer)
    {
        PlayerControllerCreative.disableAbilities(par1EntityPlayer);
        disableAbilities(par1EntityPlayer);
    }

    public static void enableAbilities(EntityPlayer par0EntityPlayer)
    {
        par0EntityPlayer.capabilities.field_75099_e = true;
    }

    public static void disableAbilities(EntityPlayer par0EntityPlayer)
    {
        par0EntityPlayer.capabilities.field_75099_e = false;
    }

}