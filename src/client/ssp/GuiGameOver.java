package net.minecraft.src;

import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
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
        controlList.clear();
        if (mc.theWorld.getWorldInfo().isHardcoreModeEnabled())
        {
            if (mc.isIntegratedServerRunning())
            {
                controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, StatCollector.translateToLocal("deathScreen.deleteWorld")));
            }
            else
            {
                controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 96, StatCollector.translateToLocal("deathScreen.leaveServer")));
            }
        }
        else
        {
            controlList.add(new GuiButton(1, width / 2 - 100, height / 4 + 72, StatCollector.translateToLocal("deathScreen.respawn")));
            controlList.add(new GuiButton(2, width / 2 - 100, height / 4 + 96, StatCollector.translateToLocal("deathScreen.titleScreen")));

            if (mc.session == null)
            {
                ((GuiButton)controlList.get(1)).enabled = false;
            }
        }

        for (Iterator iterator = controlList.iterator(); iterator.hasNext();)
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
                    String s = mc.theWorld.getSaveHandler().getSaveDirectoryName();
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
        String s = flag ? StatCollector.translateToLocal("deathScreen.title.hardcore") : StatCollector.translateToLocal("deathScreen.title");
        drawCenteredString(fontRenderer, s, width / 2 / 2, 30, 0xffffff);
        GL11.glPopMatrix();

        if (flag)
        {
            drawCenteredString(fontRenderer, StatCollector.translateToLocal("deathScreen.hardcoreInfo"), width / 2, 144, 0xffffff);
        }

        drawCenteredString(fontRenderer, (new StringBuilder()).append(StatCollector.translateToLocal("deathScreen.score")).append(oldScore ? ": &e" : ": \247e").append(mc.thePlayer.getScore()).toString(), width / 2, 100, 0xffffff);
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
            for (Iterator iterator = controlList.iterator(); iterator.hasNext();)
            {
                GuiButton guibutton = (GuiButton)iterator.next();
                guibutton.enabled = true;
            }
        }
    }
}
