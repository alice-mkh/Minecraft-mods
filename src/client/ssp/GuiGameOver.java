package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class GuiGameOver extends GuiScreen
{
    public static boolean oldScore = false;

    /**
     * The cooldown timer for the buttons, increases every tick and enables all buttons when reaching 20.
     */
    private int cooldownTimer;

    public GuiGameOver()
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        buttonList.clear();

        if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
        {
            if (mc.isIntegratedServerRunning() || mc.enableSP)
            {
                buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n.func_135053_a("deathScreen.deleteWorld")));
            }
            else
            {
                buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, I18n.func_135053_a("deathScreen.leaveServer")));
            }
        }
        else
        {
            buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 72, I18n.func_135053_a("deathScreen.respawn")));
            buttonList.add(new GuiButton(2, width / 2 - 100, height / 4 + 96, I18n.func_135053_a("deathScreen.titleScreen")));

            if (mc.func_110432_I() == null)
            {
                ((GuiButton)buttonList.get(1)).enabled = false;
            }
        }

        for (Iterator iterator = buttonList.iterator(); iterator.hasNext();)
        {
            GuiButton guibutton = (GuiButton)iterator.next();
            guibutton.enabled = false;
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char c, int i)
    {
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (mc.enableSP){
            actionPerformedSP(par1GuiButton);
            return;
        }
        switch (par1GuiButton.id)
        {
            case 1:
                mc.thePlayer.respawnPlayer();
                mc.displayGuiScreen(null);
                break;
            case 2:
                mc.theWorld.sendQuittingDisconnectingPacket();
                mc.loadWorld(null);
                mc.displayGuiScreen(new GuiMainMenu());
                break;
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformedSP(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            default:
                break;

            case 1:
                if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
                {
                    String s = mc.theWorld.getSaveHandler().getWorldDirectoryName();
                    mc.exitToMainMenu("Deleting world");
                    ISaveFormat isaveformat = mc.getSaveLoader();
                    isaveformat.flushCache();
                    isaveformat.deleteWorldDirectory(s);
                    mc.displayGuiScreen(new GuiMainMenu());
                }
                else
                {
                    mc.thePlayer.respawnPlayer();
                    mc.displayGuiScreen(null);
                    return;
                }

                break;

            case 2:
                if (mc.isMultiplayerWorld())
                {
                    mc.theWorld.sendQuittingDisconnectingPacket();
                }

                mc.changeWorld1(null);
                mc.displayGuiScreen(new GuiMainMenu());
                break;
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawGradientRect(0, 0, width, height, 0x60500000, 0xa0803030);
        GL11.glPushMatrix();
        GL11.glScalef(2.0F, 2.0F, 2.0F);
        boolean flag = mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
        String s = flag ? I18n.func_135053_a("deathScreen.title.hardcore") : I18n.func_135053_a("deathScreen.title");
        drawCenteredString(fontRenderer, s, width / 2 / 2, 30, 0xffffff);
        GL11.glPopMatrix();

        if (flag)
        {
            drawCenteredString(fontRenderer, I18n.func_135053_a("deathScreen.hardcoreInfo"), width / 2, 144, 0xffffff);
        }

        drawCenteredString(fontRenderer, (new StringBuilder()).append(I18n.func_135053_a("deathScreen.score")).append(": ").append(oldScore ? ": &e" : EnumChatFormatting.YELLOW).append(mc.thePlayer.getScore()).toString(), width / 2, 100, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Returns true if this GUI should pause the game when it is displayed in single-player
     */
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    /**
     * Called from the main game loop to update the screen.
     */
    public void updateScreen()
    {
        super.updateScreen();
        cooldownTimer++;

        if (cooldownTimer == 20)
        {
            for (Iterator iterator = buttonList.iterator(); iterator.hasNext();)
            {
                GuiButton guibutton = (GuiButton)iterator.next();
                guibutton.enabled = true;
            }
        }
    }
}
