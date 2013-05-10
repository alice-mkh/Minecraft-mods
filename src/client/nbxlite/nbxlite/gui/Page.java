package net.minecraft.src.nbxlite.gui;

import org.lwjgl.input.Mouse;
import net.minecraft.client.Minecraft;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.WorldInfo;
import net.minecraft.src.IScrollingGui;
import net.minecraft.src.GuiButtonOldDays;
import net.minecraft.src.GuiScrolling;

public abstract class Page extends GuiScreen implements IScrollingGui{
    protected int leftmargin = 90;
    protected GuiScrolling scrollingGui;
    protected GuiNBXlite parent;
    private int contentHeight;

    public Page(GuiNBXlite parent){
        this.parent = parent;
        scrollingGui = new GuiScrolling(this);
        contentHeight = 0;
    }

    public void setMc(Minecraft mc){
        this.mc = mc;
        scrollingGui.mc = mc;
    }

    public abstract void initButtons();

    @Override
    public void scrolled(){
        contentHeight = 0;
    }

    public void updateButtonPosition(){}

    public void updateButtonVisibility(){}

    public void updateButtonText(){}

    public abstract void applySettings();

    public abstract void setDefaultSettings();

    public abstract void loadFromWorldInfo(WorldInfo w);

    public abstract String getString();

    @Override
    public int getLeft(){
        return width / 2 - 50;
    }

    @Override
    public int getRight(){
        return width / 2 + 220 > width ? width : width / 2 + 220;
    }

    @Override
    public int getTop(){
        return height / 6 - 15;
    }

    @Override
    public int getBottom(){
        return height - 35;
    }

    public boolean canBeScrolled(){
        return scrollingGui.canBeScrolled();
    }

    @Override
    public int getContentHeight(){
        return contentHeight;
    }

    public void mouseMovedOrUp2(int par1, int par2, int par3){
        mouseMovedOrUp(par1, par2, par3);
    }

    protected void func_85041_a_2(int i, int j, int k, long l){
        func_85041_a(i, j, k, l);
    }

    @Override
    public void mouseClicked(int par1, int par2, int par3){
        if (par1 < getLeft() || par1 > getRight() || par2 < getTop() || par2 > getBottom()){
            return;
        }
        super.mouseClicked(par1, par2, par3);
        scrollingGui.mouseClicked(par1, par2, par3);
    }

    @Override
    public void mouseMovedOrUp(int par1, int par2, int par3){
        super.mouseMovedOrUp(par1, par2, par3);
        scrollingGui.mouseMovedOrUp(par1, par2, par3);
    }

    @Override
    protected void func_85041_a(int i, int j, int k, long l){
        super.func_85041_a(i, j, k, l);
        scrollingGui.func_85041_a(i, j, k, l);
    }

    public void calculateMinScrolling(){
        scrollingGui.calculateMinScrolling();
    }

    @Override
    public void handleMouseInput(){
        scrollingGui.handleMouseInput();
        super.handleMouseInput();
    }

    public void drawScrollingBackground(){
        if (!canBeScrolled()){
            return;
        }
        scrollingGui.drawScrollingBackground();
    }

    public void drawButtons(int i, int j){
        scrollingGui.drawButtons(i, j);
    }

    public void drawFrameAndScrollbar(){
        if (!scrollingGui.canBeScrolled()){
            return;
        }
        scrollingGui.drawFrameAndScrollbar(height);
    }

    @Override
    public void actionPerformedScrolling(GuiButton b){
        updateButtonVisibility();
        scrolled();
        updateButtonText();
        calculateMinScrolling();
    }

    protected final void addButton(GuiButtonOldDays b){
        scrollingGui.buttonList.add(b);
    }

    protected void setY(GuiButton b, int i){
        b.yPosition = height / 6 + scrollingGui.scrolling + i;
        if (i > contentHeight && b.drawButton){
            contentHeight = i;
        }
    }
}