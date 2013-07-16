package net.minecraft.src;

import java.util.List;

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
        buttonList.clear();
        buttonList.add(new GuiButton(101, width / 2 - 155, height - 28, 150, 20, I18n.func_135053_a("lanServer.start")));
        buttonList.add(new GuiButton(102, width / 2 + 5, height - 28, 150, 20, I18n.func_135053_a("gui.cancel")));
        buttonList.add(buttonGameMode = new GuiButton(104, width / 2 - 155, 100, 150, 20, I18n.func_135053_a("selectWorld.gameMode")));
        buttonList.add(buttonAllowCommandsToggle = new GuiButton(103, width / 2 + 5, 100, 150, 20, I18n.func_135053_a("selectWorld.allowCommands")));
        func_74088_g();
    }

    private void func_74088_g()
    {
        buttonGameMode.displayString = (new StringBuilder()).append(I18n.func_135053_a("selectWorld.gameMode")).append(" ").append(I18n.func_135053_a((new StringBuilder()).append("selectWorld.gameMode.").append(gameMode).toString())).toString();
        buttonAllowCommandsToggle.displayString = (new StringBuilder()).append(I18n.func_135053_a("selectWorld.allowCommands")).append(" ").toString();

        if (!(!allowCommands))
        {
            buttonAllowCommandsToggle.displayString += I18n.func_135053_a("options.on");
        }
        else
        {
            buttonAllowCommandsToggle.displayString += I18n.func_135053_a("options.off");
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
            ChatMessageComponent chatmessagecomponent;

            if (s != null)
            {
                chatmessagecomponent = ChatMessageComponent.func_111082_b("commands.publish.started", new Object[]
                        {
                            s
                        });
            }
            else
            {
                chatmessagecomponent = ChatMessageComponent.func_111066_d("commands.publish.failed");
            }

            mc.ingameGUI.getChatGUI().printChatMessage(chatmessagecomponent.func_111068_a(true));
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, I18n.func_135053_a("lanServer.title"), width / 2, 50, 0xffffff);
        drawCenteredString(fontRenderer, I18n.func_135053_a("lanServer.otherPlayers"), width / 2, 82, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }
}
