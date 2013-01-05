package net.minecraft.src;

import net.minecraft.client.Minecraft;
import java.util.ArrayList;

public class mod_SSPC extends Mod{
    public void load(){
        //Replacing EntityPlayerSP2 class
        Minecraft.getMinecraft().playerClass = net.minecraft.src.EntityPlayerSPSPC.class;
    }

    public String getModName(){
        return "SSPC";
    }

    public String getModVersion(){
        return "3.3";
    }

    public String getMcVersion(){
        return EntityPlayerSPSPC.MCVERSION;
    }

    public void addSPCommands(ClientCommandManager manager){
        PlayerHelper.registerCommandWrappers(manager);
    }
}