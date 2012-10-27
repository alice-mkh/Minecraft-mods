package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiShareToLan extends GuiScreen
{
    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    private final GuiScreen parentScreen;
    private GuiButton buttonAllowCommandsToggle;
    private GuiButton buttonGameMode;

    /**
     * The currently selected game mode. One of 'survival', 'creative', or 'adventure'
     */
    private String gameMode;

    /** True if 'Allow Cheats' is currently enabled */
    private boolean allowCommands;
    private boolean singleplayer;

    public GuiShareToLan(GuiScreen par1GuiScreen)
    {
        gameMode = "survival";
        allowCommands = false;
        parentScreen = par1GuiScreen;
        singleplayer = false;
    }

    public GuiShareToLan setUseSP(){
        singleplayer = true;
        return this;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        controlList.clear();
        controlList.add(new GuiButton(101, width / 2 - 155, height - 28, 150, 20, StatCollector.translateToLocal("lanServer.start")));
        controlList.add(new GuiButton(102, width / 2 + 5, height - 28, 150, 20, StatCollector.translateToLocal("gui.cancel")));
        controlList.add(buttonGameMode = new GuiButton(104, width / 2 - 155, 100, 150, 20, StatCollector.translateToLocal("selectWorld.gameMode")));
        controlList.add(buttonAllowCommandsToggle = new GuiButton(103, width / 2 + 5, 100, 150, 20, StatCollector.translateToLocal("selectWorld.allowCommands")));
        func_74088_g();
    }

    private void func_74088_g()
    {
        StringTranslate stringtranslate;
        stringtranslate = StringTranslate.getInstance();
        buttonGameMode.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.gameMode")).append(" ").append(stringtranslate.translateKey((new StringBuilder()).append("selectWorld.gameMode.").append(gameMode).toString())).toString();
        buttonAllowCommandsToggle.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.allowCommands")).append(" ").toString();

        if (!(!allowCommands))
        {
            buttonAllowCommandsToggle.displayString += stringtranslate.translateKey("options.on");
        }
        else
        {
            buttonAllowCommandsToggle.displayString += stringtranslate.translateKey("options.off");
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 102)
        {
            mc.displayGuiScreen(parentScreen);
        }
        else if (par1GuiButton.id == 104)
        {
            if (gameMode.equals("survival"))
            {
                gameMode = "creative";
            }
            else if (gameMode.equals("creative"))
            {
                gameMode = "adventure";
            }
            else
            {
                gameMode = "survival";
            }

            func_74088_g();
        }
        else if (par1GuiButton.id == 103)
        {
            allowCommands = !allowCommands;
            func_74088_g();
        }
        else if (par1GuiButton.id == 101)
        {
            mc.displayGuiScreen(null);
            if (singleplayer){
                mc.quitAndStartServer();
            }
            String s = mc.getIntegratedServer().shareToLAN(EnumGameType.getByName(gameMode), allowCommands);
            String s1 = "";

            if (!singleplayer){
                if (s != null){
                    s1 = mc.thePlayer.translateString("commands.publish.started", new Object[]{s});
                }else{
                    s1 = mc.thePlayer.translateString("commands.publish.failed", new Object[0]);
                }
            }else{
                if (s != null){
                    s1 = StringTranslate.getInstance().translateKeyFormat("commands.publish.started", new Object[]{s});
                }else{
                    s1 = StringTranslate.getInstance().translateKeyFormat("commands.publish.failed", new Object[0]);
                }
            }

            mc.ingameGUI.getChatGUI().printChatMessage(s1);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, StatCollector.translateToLocal("lanServer.title"), width / 2, 50, 0xffffff);
        drawCenteredString(fontRenderer, StatCollector.translateToLocal("lanServer.otherPlayers"), width / 2, 82, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
