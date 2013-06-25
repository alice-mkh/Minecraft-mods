package net.minecraft.src;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.src.ssp.ClientCommandManager;
import net.minecraft.src.ssp.Mod;

public class mod_SSPC extends Mod{
    @Override
    public void load(){
        //Replacing EntityPlayerSP2 class
        Minecraft.getMinecraft().playerClass = net.minecraft.src.EntityPlayerSPSPC.class;
    }

    @Override
    public String getModName(){
        return "SSPC";
    }

    @Override
    public String getModVersion(){
        return "3.3.1";
    }

    @Override
    public String getMcVersion(){
        return EntityPlayerSPSPC.MCVERSION;
    }

    @Override
    public void addSPCommands(ClientCommandManager manager){
        PlayerHelper.registerCommandWrappers(manager);
    }
}