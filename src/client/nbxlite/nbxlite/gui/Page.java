package net.minecraft.src.nbxlite.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.Tessellator;
import net.minecraft.src.WorldInfo;

public abstract class Page extends GuiScreen{
    public static final int SCROLLBAR_WIDTH = 6;
    protected int leftmargin = 90;
    protected int scrolling;
    protected int minScrolling;
    protected int maxScrolling;

    private boolean dragging;
    private boolean scrollbarDragging;
    private int clickY;

    protected GuiNBXlite parent;

    public Page(GuiNBXlite parent){
        this.parent = parent;
        scrolling = 10;
        clickY = 0;
        minScrolling = 10;
        maxScrolling = 10;
        dragging = false;
        scrollbarDragging = false;
    }

    public abstract void initButtons();

    public abstract void scrolled();

    public void updateButtonPosition(){}

    public void updateButtonVisibility(){}

    public void updateButtonText(){}

    public abstract void applySettings();

    public abstract void setDefaultSettings();

    public abstract void loadFromWorldInfo(WorldInfo w);

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

    protected abstract int getContentHeight();

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
        if (!canBeScrolled()){
            return;
        }
        dragging = true;
        if (par1 > getRight() - SCROLLBAR_WIDTH){
            scrollbarDragging = true;
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
        scrollbarDragging = false;
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
        if (scrollbarDragging){
            int bottomtop = getBottom() - getTop();
            float scrollMultiplier = -1.0F;
            int cHeight = getContentHeight() + 44;
            int l2 = cHeight - bottomtop + 4;
            if (l2 < 1){
                l2 = 1;
            }
            int k3 = (int)((float)(bottomtop * bottomtop) / (float)cHeight);
            if (k3 < 32){
                k3 = 32;
            }
            if (k3 > bottomtop - 8){
                k3 = bottomtop - 8;
            }
            scrollMultiplier /= (float)(bottomtop - k3) / (float)l2;
            delta *= scrollMultiplier;
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

    public void calculateMinScrolling(){
        minScrolling = -getContentHeight() - 40 + getBottom() - getTop();
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

    public void drawScrollbar(){
        int top = getTop();
        int bottom = getBottom();
        int delta = bottom - top;
        int cHeight = getContentHeight() + 44;
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
                size = delta;
            }
            int k4 = ((-scrolling + maxScrolling) * (delta - size)) / j3 + top;
            if (k4 < top){
                k4 = top;
            }
            if (k4 + size > bottom){
                k4 = bottom - size;
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
    }
}