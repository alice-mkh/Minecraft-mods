package net.minecraft.src.nbxlite.gui;

import net.minecraft.src.GuiScreen;

public abstract class Page extends GuiScreen{
    protected int leftmargin = 90;

    protected GuiNBXlite parent;

    public Page(GuiNBXlite parent){
        this.parent = parent;
    }

    public abstract void initButtons();

    public abstract void selectSettings();

    public void updateButtonPosition(){}

    public void updateButtonVisibility(){}

    public void updateButtonText(){}

    public void mouseClicked2(int par1, int par2, int par3){
        mouseClicked(par1, par2, par3);
    }

    public void mouseMovedOrUp2(int par1, int par2, int par3){
        mouseMovedOrUp(par1, par2, par3);
    }
}