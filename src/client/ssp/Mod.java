package net.minecraft.src;

import net.minecraft.client.Minecraft;

public abstract class Mod{
    public boolean usesTick;
    public boolean usesGUITick;

    public abstract String getModVersion();

    public abstract String getMcVersion();

    public abstract String getModName();

    public abstract void load();

    public void onLoadingSP(String par1Str, String par2Str){}

    public void onTick(){}

    public void onGUITick(GuiScreen screen){}

    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, int override){
        return false;
    }

    protected void setUseTick(boolean game, boolean gui){
        usesTick = game;
        usesGUITick = gui;
    }
}