package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiIngameMenuSP extends GuiScreen
{
    /** Also counts the number of updates, not certain as to why yet. */
    private int updateCounter2;

    /** Counts the number of screen updates. */
    private int updateCounter;

    public GuiIngameMenuSP()
    {
        updateCounter2 = 0;
        updateCounter = 0;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
        updateCounter2 = 0;
        buttonList.clear();
        byte byte0 = -16;
        buttonList.add(new GuiButton(1, width / 2 - 100, height / 4 + 120 + byte0, StatCollector.translateToLocal("menu.returnToMenu")));

        if (mc.isMultiplayerWorld())
        {
            ((GuiButton)buttonList.get(0)).displayString = StatCollector.translateToLocal("menu.disconnect");
        }

        buttonList.add(new GuiButton(4, width / 2 - 100, height / 4 + 24 + byte0, StatCollector.translateToLocal("menu.returnToGame")));
        buttonList.add(new GuiButton(5, width / 2 - 100, height / 4 + 48 + byte0, 98, 20, StatCollector.translateToLocal("gui.achievements")));
        buttonList.add(new GuiButton(6, width / 2 + 2, height / 4 + 48 + byte0, 98, 20, StatCollector.translateToLocal("gui.stats")));
        if (SSPOptions.getShareButton()){
            buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + byte0, 98, 20, StatCollector.translateToLocal("menu.options")));
            GuiButton guibutton;
            buttonList.add(guibutton = new GuiButton(7, width / 2 + 2, height / 4 + 96 + byte0, 98, 20, StatCollector.translateToLocal("menu.shareToLan")));
            guibutton.enabled = !mc.isMultiplayerWorld();
        }else{
            buttonList.add(new GuiButton(0, width / 2 - 100, height / 4 + 96 + byte0, StatCollector.translateToLocal("menu.options")));
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    @Override
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        switch (par1GuiButton.id)
        {
            case 2:
            case 3:
            default:
                break;

            case 0:
                mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings));
                break;

            case 1:
                mc.statFileWriter.readStat(StatList.leaveGameStat, 1);

                if (mc.isMultiplayerWorld())
                {
                    mc.theWorld.sendQuittingDisconnectingPacket();
                }

                mc.changeWorld1(null);
                mc.displayGuiScreen(new GuiMainMenu());
                break;

            case 4:
                mc.displayGuiScreen(null);
                mc.setIngameFocus();
                break;

            case 5:
                mc.displayGuiScreen(new GuiAchievements(mc.statFileWriter));
                break;

            case 6:
                mc.displayGuiScreen(new GuiStats(this, mc.statFileWriter));
                break;

            case 7:
                mc.displayGuiScreen(new GuiShareToLan(this).setUseSP());
                break;
        }
    }

    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
        super.updateScreen();
        updateCounter++;
    }

    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        boolean flag = !((WorldSSP)mc.theWorld).quickSaveWorld(updateCounter2++);

        if (flag || updateCounter < 20)
        {
            float f = ((float)(updateCounter % 10) + par3) / 10F;
            f = MathHelper.sin(f * (float)Math.PI * 2.0F) * 0.2F + 0.8F;
            int i = (int)(255F * f);
            drawString(fontRenderer, "Saving level..", 8, height - 16, i << 16 | i << 8 | i);
        }

        drawCenteredString(fontRenderer, "Game menu", width / 2, 40, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
