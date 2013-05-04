package net.minecraft.src;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;

public class GuiScrolling{
    public static final int SCROLLBAR_WIDTH = 6;

    private IScrollingGui gui;
    public int scrolling;
    public int minScrolling;
    public int maxScrolling;
    private boolean dragging;
    private boolean scrollbarDragging;
    private int clickY;

    public GuiScrolling(IScrollingGui gui){
        this.gui = gui;
        scrolling = 10;
        minScrolling = 10;
        maxScrolling = 10;
        clickY = 0;
        dragging = false;
        scrollbarDragging = false;
    }

    public void handleMouseInput(Minecraft mc){
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
                gui.scrolled();
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
        gui.scrolled();
    }

    public boolean canBeScrolled(){
        return minScrolling < maxScrolling;
    }

    public void func_85041_a(int i, int j, int k, long l){
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
        gui.scrolled();
    }

    public void drawScrollbar(){
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

    public void mouseClicked(int par1, int par2, int par3){
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
        if (!canBeScrolled() || !dragging){
            return;
        }
        dragging = false;
        scrollbarDragging = false;
    }

    public static void drawDirtRect(int x1, int x2, int y1, int y2, boolean scrolling, int i)
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
}