package net.minecraft.src;

import java.io.PrintStream;
import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiConnecting extends GuiScreen
{
    /** A reference to the NetClientHandler. */
    private NetClientHandler clientHandler;

    /** True if the connection attempt has been cancelled. */
    private boolean cancelled;

    public GuiConnecting(Minecraft par1Minecraft, ServerData par2ServerData)
    {
        cancelled = false;
        mc = par1Minecraft;
        ServerAddress serveraddress = ServerAddress.func_78860_a(par2ServerData.field_78845_b);
        par1Minecraft.func_71403_a(null);
        par1Minecraft.func_71351_a(par2ServerData);
        func_74255_a(serveraddress.func_78861_a(), serveraddress.func_78864_b());
    }

    public GuiConnecting(Minecraft par1Minecraft, String par2Str, int par3)
    {
        cancelled = false;
        mc = par1Minecraft;
        par1Minecraft.func_71403_a(null);
        func_74255_a(par2Str, par3);
    }

    private void func_74255_a(String par1Str, int par2)
    {
        System.out.println((new StringBuilder()).append("Connecting to ").append(par1Str).append(", ").append(par2).toString());
        (new ThreadConnectToServer(this, par1Str, par2)).start();
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        if (clientHandler != null)
        {
            clientHandler.processReadPackets();
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char c, int i)
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.clear();
        controlList.add(new GuiButton(0, width / 2 - 100, height / 4 + 120 + 12, stringtranslate.translateKey("gui.cancel")));
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 0)
        {
            cancelled = true;

            if (clientHandler != null)
            {
                clientHandler.disconnect();
            }

            mc.displayGuiScreen(new GuiMainMenu());
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        StringTranslate stringtranslate = StringTranslate.getInstance();

        if (clientHandler == null)
        {
            drawCenteredString(fontRenderer, stringtranslate.translateKey("connect.connecting"), width / 2, height / 2 - 50, 0xffffff);
            drawCenteredString(fontRenderer, "", width / 2, height / 2 - 10, 0xffffff);
        }
        else
        {
            drawCenteredString(fontRenderer, stringtranslate.translateKey("connect.authorizing"), width / 2, height / 2 - 50, 0xffffff);
            drawCenteredString(fontRenderer, clientHandler.field_72560_a, width / 2, height / 2 - 10, 0xffffff);
        }

        super.drawScreen(par1, par2, par3);
    }

    /**
     * Sets the NetClientHandler.
     */
    static NetClientHandler setNetClientHandler(GuiConnecting par0GuiConnecting, NetClientHandler par1NetClientHandler)
    {
        return par0GuiConnecting.clientHandler = par1NetClientHandler;
    }

    static Minecraft func_74256_a(GuiConnecting par0GuiConnecting)
    {
        return par0GuiConnecting.mc;
    }

    static boolean func_74257_b(GuiConnecting par0GuiConnecting)
    {
        return par0GuiConnecting.cancelled;
    }

    static Minecraft func_74254_c(GuiConnecting par0GuiConnecting)
    {
        return par0GuiConnecting.mc;
    }

    /**
     * Gets the NetClientHandler.
     */
    static NetClientHandler getNetClientHandler(GuiConnecting par0GuiConnecting)
    {
        return par0GuiConnecting.clientHandler;
    }

    static Minecraft func_74249_e(GuiConnecting par0GuiConnecting)
    {
        return par0GuiConnecting.mc;
    }

    static Minecraft func_74250_f(GuiConnecting par0GuiConnecting)
    {
        return par0GuiConnecting.mc;
    }

    static Minecraft func_74251_g(GuiConnecting par0GuiConnecting)
    {
        return par0GuiConnecting.mc;
    }
}
