package net.minecraft.src;

import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class LoadingScreenRenderer implements IProgressUpdate
{
    public static boolean smooth = false;

    private String field_73727_a;

    /** A reference to the Minecraft object. */
    private Minecraft mc;

    /**
     * The text currently displayed (i.e. the argument to the last call to printText or func_73722_d)
     */
    private String currentlyDisplayedText;
    private long field_73723_d;
    private boolean field_73724_e;

    public LoadingScreenRenderer(Minecraft par1Minecraft)
    {
        field_73727_a = "";
        currentlyDisplayedText = "";
        field_73723_d = Minecraft.func_71386_F();
        field_73724_e = false;
        mc = par1Minecraft;
    }

    public void printText(String par1Str)
    {
        field_73724_e = false;
        func_73722_d(par1Str);
    }

    /**
     * Shows the 'Saving level' string.
     */
    public void displaySavingString(String par1Str)
    {
        field_73724_e = true;
        func_73722_d(par1Str);
    }

    public void func_73722_d(String par1Str)
    {
        currentlyDisplayedText = par1Str;

        if (!mc.running)
        {
            if (field_73724_e)
            {
                return;
            }
            else
            {
                throw new MinecraftError();
            }
        }
        else
        {
            ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
            GL11.glClear(256);
            GL11.glMatrixMode(GL11.GL_PROJECTION);
            GL11.glLoadIdentity();
            GL11.glOrtho(0.0D, scaledresolution.func_78327_c(), scaledresolution.func_78324_d(), 0.0D, 100D, 300D);
            GL11.glMatrixMode(GL11.GL_MODELVIEW);
            GL11.glLoadIdentity();
            GL11.glTranslatef(0.0F, 0.0F, -200F);
            return;
        }
    }

    /**
     * Displays a string on the loading screen supposed to indicate what is being done currently.
     */
    public void displayLoadingString(String par1Str)
    {
        if (!mc.running)
        {
            if (field_73724_e)
            {
                return;
            }
            else
            {
                throw new MinecraftError();
            }
        }
        else
        {
            field_73723_d = 0L;
            field_73727_a = par1Str;
            setLoadingProgress(-1);
            field_73723_d = 0L;
            return;
        }
    }

    /**
     * Updates the progress bar on the loading screen to the specified amount. Args: loadProgress
     */
    public void setLoadingProgress(int par1)
    {
        if (!mc.running)
        {
            if (field_73724_e)
            {
                return;
            }
            else
            {
                throw new MinecraftError();
            }
        }

        long l = Minecraft.func_71386_F();

        if (l - field_73723_d < (smooth ? 20L : 100L))
        {
            return;
        }

        field_73723_d = l;
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0.0D, scaledresolution.func_78327_c(), scaledresolution.func_78324_d(), 0.0D, 100D, 300D);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -200F);
        GL11.glClear(16640);
        Tessellator tessellator = Tessellator.instance;
        int k = mc.renderEngine.getTexture("/gui/background.png");
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, k);
        float f = 32F;
        tessellator.startDrawingQuads();
        tessellator.setColorOpaque_I(0x404040);
        tessellator.addVertexWithUV(0.0D, j, 0.0D, 0.0D, (float)j / f);
        tessellator.addVertexWithUV(i, j, 0.0D, (float)i / f, (float)j / f);
        tessellator.addVertexWithUV(i, 0.0D, 0.0D, (float)i / f, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
        tessellator.draw();

        if (par1 >= 0)
        {
            byte byte0 = 100;
            byte byte1 = 2;
            int i1 = i / 2 - byte0 / 2;
            int j1 = j / 2 + 16;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            tessellator.setColorOpaque_I(0x808080);
            tessellator.addVertex(i1, j1, 0.0D);
            tessellator.addVertex(i1, j1 + byte1, 0.0D);
            tessellator.addVertex(i1 + byte0, j1 + byte1, 0.0D);
            tessellator.addVertex(i1 + byte0, j1, 0.0D);
            tessellator.setColorOpaque_I(0x80ff80);
            tessellator.addVertex(i1, j1, 0.0D);
            tessellator.addVertex(i1, j1 + byte1, 0.0D);
            tessellator.addVertex(i1 + par1, j1 + byte1, 0.0D);
            tessellator.addVertex(i1 + par1, j1, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        mc.fontRenderer.drawStringWithShadow(currentlyDisplayedText, (i - mc.fontRenderer.getStringWidth(currentlyDisplayedText)) / 2, j / 2 - 4 - 16, 0xffffff);
        mc.fontRenderer.drawStringWithShadow(field_73727_a, (i - mc.fontRenderer.getStringWidth(field_73727_a)) / 2, (j / 2 - 4) + 8, 0xffffff);
        Display.update();

        try
        {
            Thread.yield();
        }
        catch (Exception exception) { }
    }

    public void func_73717_a()
    {
    }
}
