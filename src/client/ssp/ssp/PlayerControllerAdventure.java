package net.minecraft.src.ssp;

import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EnumGameType;
import net.minecraft.src.Minecraft;

public class PlayerControllerAdventure extends PlayerControllerSP{
    public PlayerControllerAdventure(Minecraft par1Minecraft){
        super(par1Minecraft);
    }

    @Override
    public void setPlayerCapabilities(EntityPlayer par1EntityPlayer)
    {
        mc.setGameMode(EnumGameType.ADVENTURE);
    }

    public static void enableAbilities(EntityPlayer par0EntityPlayer)
    {
        par0EntityPlayer.capabilities.allowEdit = true;
    }

    public static void disableAbilities(EntityPlayer par0EntityPlayer)
    {
        par0EntityPlayer.capabilities.allowEdit = false;
    }
}