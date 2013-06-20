package net.minecraft.src;

import java.util.ArrayList;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

public class GuiScrolling extends Gui{
    public static final int SCROLLBAR_WIDTH = 6;

    private IScrollingGui gui;
    public Minecraft mc;
    public int scrolling;
    private int minScrolling;
    private int maxScrolling;
    private boolean dragging;
    private boolean scrollbarDragging;
    private int clickY;

    public ArrayList<GuiButtonOldDays> buttonList;
    private GuiButton selectedButton;

    public GuiScrolling(IScrollingGui gui){
        this.gui = gui;
        scrolling = 10;
        minScrolling = 10;
        maxScrolling = 10;
        clickY = 0;
        dragging = false;
        scrollbarDragging = false;
        buttonList = new ArrayList<GuiButtonOldDays>();
    }

    public void handleMouseInput(){
        if (canBeScrolled() && !mc.gameSettings.touchscreen && !Mouse.isButtonDown(0) && Mouse.next()){
            int l = Mouse.getEventDWheel();
            if (l != 0){
                if (l > 0){
                    l = -1;
                }else if (l < 0){
                    l = 1;
                }
                scrolling -= l * 20;
                if (scrolling < minScrolling){
                    scrolling = minScrolling;
                }
                if (!canBeScrolled() || scrolling > maxScrolling){
                    scrolling = maxScrolling;
                }
                scrolled();
            }
        }
    }

    public void calculateMinScrolling(){
        minScrolling = -gui.getContentHeight() - 40 + gui.getBottom() - gui.getTop();
        if (scrolling < minScrolling){
            scrolling = minScrolling;
        }
        if (!canBeScrolled() || scrolling > maxScrolling){
            scrolling = maxScrolling;
        }
        scrolled();
    }

    public boolean canBeScrolled(){
        return minScrolling < maxScrolling;
    }

    public void drag(int i, int j){
        if (!canBeScrolled() || !dragging){
            return;
        }
        int delta = j - clickY;
        if (delta == 0){
            return;
        }
        if (scrollbarDragging){
            int bottomtop = gui.getBottom() - gui.getTop();
            float scrollMultiplier = -1.0F;
            int cHeight = gui.getContentHeight() + 44;
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

    public void mouseClicked(int par1, int par2, int par3){
        if (par1 < gui.getLeft() || par1 > gui.getRight() || par2 < gui.getTop() || par2 > gui.getBottom()){
            return;
        }
        if (par3 == 0){
            for (int i = 0; i < buttonList.size(); i++){
                GuiButtonOldDays guibutton = buttonList.get(i);
                if (guibutton.mousePressed(mc, par1, par2) && guibutton.enabled){
                    selectedButton = guibutton;
                    mc.sndManager.playSoundFX("random.click", 1.0F, 1.0F);
                    gui.actionPerformedScrolling(guibutton);
                }
            }
        }
        if (!canBeScrolled()){
            return;
        }
        dragging = true;
        if (par1 > gui.getRight() - SCROLLBAR_WIDTH){
            scrollbarDragging = true;
        }
        clickY = par2;
    }

    public void mouseMovedOrUp(int par1, int par2, int par3){
        if (selectedButton != null && par3 == 0){
            selectedButton.mouseReleased(par1, par2);
            selectedButton = null;
        }
        if (!canBeScrolled() || !dragging){
            return;
        }
        dragging = false;
        scrollbarDragging = false;
    }

    private void drawScrollbar(){
        int top = gui.getTop();
        int bottom = gui.getBottom();
        int delta = bottom - top;
        int cHeight = gui.getContentHeight() + 44;
        int j3 = cHeight - (delta - 4);
        int right = gui.getRight();
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

    private void drawDirtRect(int x1, int x2, int y1, int y2, boolean scrolling, int i)
    {
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_FOG);
        Tessellator tessellator = Tessellator.instance;
        mc.renderEngine.bindTexture("/gui/background.png");
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        float f = 32F;
        tessellator.startDrawingQuads();
        float xOffset = (x1 % 32) / f;
        float yOffset = (y1 % 32) / f;
        tessellator.setColorOpaque_I(scrolling ? 0x202020 : 0x404040);
        tessellator.addVertexWithUV(x1, y2, 0.0D, xOffset, (float)(y2 - y1 - i) / f + yOffset);
        tessellator.addVertexWithUV(x2, y2, 0.0D, (float)(x2 - x1) / f + xOffset, (float)(y2 - y1 - i) / f + yOffset);
        tessellator.addVertexWithUV(x2, y1, 0.0D, (float)(x2 - x1) / f + xOffset, yOffset - i / f);
        tessellator.addVertexWithUV(x1, y1, 0.0D, xOffset, yOffset - i / f);
        tessellator.draw();
    }

    public void drawFrameAndScrollbar(int height){
        int pageLeft = gui.getLeft();
        int pageRight = gui.getRight();
        int pageTop = gui.getTop();
        int pageBottom = gui.getBottom();
        drawDirtRect(pageLeft, pageRight, 0, pageTop, false, 0);
        drawDirtRect(pageLeft, pageRight, pageBottom, height, false, 0);
        drawGradientRect(pageLeft, pageTop, pageRight, pageTop + 5, 0xff000000, 0x00000000);
        drawGradientRect(pageLeft, pageBottom - 5, pageRight, pageBottom, 0x00000000, 0xff000000);
        drawRect(pageLeft - 1, pageTop, pageLeft, pageBottom, 0xff000000);
        drawScrollbar();
    }
 
    public void drawScrollingBackground(){
        drawDirtRect(gui.getLeft(), gui.getRight(), gui.getTop(), gui.getBottom(), true, scrolling - maxScrolling);
    }

    private void scrolled(){
        for (GuiButtonOldDays button: buttonList){
            button.scrolled(canBeScrolled(), scrolling);
        }
        gui.scrolled();
    }

    public void drawButtons(int i, int j){
        for (int k = 0; k < buttonList.size(); k++){
            GuiButtonOldDays guibutton = buttonList.get(k);
            if (guibutton.yPosition + guibutton.getHeight() <= gui.getTop() || guibutton.yPosition >= gui.getBottom()){
                continue;
            }
            if (i < gui.getLeft() || i > gui.getRight() || j < gui.getTop() || j > gui.getBottom()){
                guibutton.drawButton(mc, -1000, -1000);
            }else{
                guibutton.drawButton(mc, i, j);
            }
        }
    }
}