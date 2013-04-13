package net.minecraft.src.nbxlite.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.ScaledResolution;
import net.minecraft.src.Tessellator;

public abstract class Page extends GuiScreen{
    public static final int SCROLLBAR_WIDTH = 6;
    protected int leftmargin = 90;
    protected int scrolling;
    protected int minScrolling;
    protected int maxScrolling;

    private boolean dragging;
    private boolean reverse;
    private int clickY;

    protected GuiNBXlite parent;

    public Page(GuiNBXlite parent){
        this.parent = parent;
        scrolling = 10;
        clickY = 0;
        minScrolling = 10;
        maxScrolling = 10;
        dragging = false;
        reverse = false;
    }

    public abstract void initButtons();

    public abstract void scrolled();

    public void updateButtonPosition(){}

    public void updateButtonVisibility(){}

    public void updateButtonText(){}

    public abstract void selectSettings();

    public int getScrolling(){
        return scrolling - maxScrolling;
    }

    public int getLeft(){
        return width / 2 - 50;
    }

    public int getRight(){
        return width / 2 + 220 > width ? width : width / 2 + 220;
    }

    public int getTop(){
        return height / 6 - 15;
    }

    public int getBottom(){
        return height - 35;
    }

    public boolean canBeScrolled(){
        return minScrolling < maxScrolling;
    }

    public void mouseMovedOrUp2(int par1, int par2, int par3){
        mouseMovedOrUp(par1, par2, par3);
    }

    protected void func_85041_a_2(int i, int j, int k, long l){
        func_85041_a(i, j, k, l);
    }

    @Override
    public void mouseClicked(int par1, int par2, int par3){
        super.mouseClicked(par1, par2, par3);
        if (!canBeScrolled()){
            return;
        }
        if (par1 > getLeft() && par1 < getRight() && par2 > getTop() && par2 < getBottom()){
            dragging = true;
/*            if (par1 > getRight() - SCROLLBAR_WIDTH){
                reverse = true;
            }*/
        }
        clickY = par2;
    }

    @Override
    public void mouseMovedOrUp(int par1, int par2, int par3){
        super.mouseMovedOrUp(par1, par2, par3);
        if (!canBeScrolled() || !dragging){
            return;
        }
        dragging = false;
        reverse = false;
    }

    @Override
    protected void func_85041_a(int i, int j, int k, long l){
        super.func_85041_a(i, j, k, l);
        if (!canBeScrolled() || !dragging){
            return;
        }
        int delta = j - clickY;
        if (delta == 0){
            return;
        }
        if (reverse){
            delta = -delta;
//             delta = -delta / (getTop() - bottom - 4) * (maxScrolling - minScrolling);
        }
        if (scrolling + delta < minScrolling){
            scrolling = minScrolling;
        }else if (delta + scrolling > maxScrolling){
            scrolling = maxScrolling;
        }else{
            scrolling += delta;
        }
        clickY = j;
        scrolled();
    }

    private int getContentHeight(){
        int min = 0;
        for (Object o : buttonList){
            GuiButton b = (GuiButton)o;
            if (!b.drawButton){
                continue;
            }
            int y = -b.yPosition - 20;
            if (y < min){
                min = y;
            }
        }
        return -min;
    }

    public void calculateMinScrolling(){
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int temp = scrolling;
        scrolling = 5 * scaledresolution.getScaleFactor();
        scrolled();
        scrolling = temp;
        int min = -getContentHeight();
        maxScrolling = (5 * scaledresolution.getScaleFactor());
        min += height * 5 / 6 + maxScrolling;
        minScrolling = min;
        if (scrolling < minScrolling){
            scrolling = minScrolling;
        }
        if (!canBeScrolled() || scrolling > maxScrolling){
            scrolling = maxScrolling;
        }
        scrolled();
    }

    @Override
    public void handleMouseInput(){
        if (canBeScrolled() && !mc.gameSettings.touchscreen){
            int l = Mouse.getEventDWheel();
            if (l != 0){
                if (l > 0){
                    l = -1;
                }else if (l < 0){
                    l = 1;
                }
                scrolling -= l * 10;
                if (scrolling < minScrolling){
                    scrolling = minScrolling;
                }
                if (!canBeScrolled() || scrolling > maxScrolling){
                    scrolling = maxScrolling;
                }
                scrolled();
            }
        }
        super.handleMouseInput();
    }
/*
    public void drawScrollbar(){
        ScaledResolution sres = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int top = getTop();
        int bottom = getBottom();
        int delta = bottom - top;
        int cHeight = getContentHeight() + delta;
        int j3 = cHeight - (delta - 4);
        int right = getRight();
        int left = right - SCROLLBAR_WIDTH;
        Tessellator tessellator = Tessellator.instance;
        if (j3 > 0){
            int size = (delta * delta) / cHeight;
            if (size < 32){
                size = 32;
            }
            if (size > delta){
//                 size = delta;
            }
            int k4 = (-scrolling * (delta - size)) / j3 + top;
            if (k4 < top){
//                 k4 = top;
            }
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0, 255);
            tessellator.addVertexWithUV(left, bottom, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(right, bottom, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(right, top, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(left, top, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0x808080, 255);
            tessellator.addVertexWithUV(left, k4 + size, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(right, k4 + size, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(right, k4, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(left, k4, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_I(0xc0c0c0, 255);
            tessellator.addVertexWithUV(left, k4 + size - 1, 0.0D, 0.0D, 1.0D);
            tessellator.addVertexWithUV(right - 1, k4 + size - 1, 0.0D, 1.0D, 1.0D);
            tessellator.addVertexWithUV(right - 1, k4, 0.0D, 1.0D, 0.0D);
            tessellator.addVertexWithUV(left, k4, 0.0D, 0.0D, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }*/
}