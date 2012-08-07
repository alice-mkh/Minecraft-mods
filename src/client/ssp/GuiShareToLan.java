package net.minecraft.src;

import java.util.List;
import net.minecraft.client.Minecraft;

public class GuiShareToLan extends GuiScreen
{
    private final GuiScreen field_74092_a;
    private GuiButton field_74090_b;
    private GuiButton field_74091_c;
    private String field_74089_d;
    private boolean field_74093_m;
    private boolean singleplayer;

    public GuiShareToLan(GuiScreen par1GuiScreen)
    {
        field_74089_d = "survival";
        field_74093_m = false;
        field_74092_a = par1GuiScreen;
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
        controlList.add(field_74091_c = new GuiButton(104, width / 2 - 155, 100, 150, 20, StatCollector.translateToLocal("selectWorld.gameMode")));
        controlList.add(field_74090_b = new GuiButton(103, width / 2 + 5, 100, 150, 20, StatCollector.translateToLocal("selectWorld.allowCommands")));
        func_74088_g();
    }

    private void func_74088_g()
    {
        StringTranslate stringtranslate;
        stringtranslate = StringTranslate.getInstance();
        field_74091_c.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.gameMode")).append(" ").append(stringtranslate.translateKey((new StringBuilder()).append("selectWorld.gameMode.").append(field_74089_d).toString())).toString();
        field_74090_b.displayString = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.allowCommands")).append(" ").toString();

        if (!(!field_74093_m))
        {
            field_74090_b.displayString += stringtranslate.translateKey("options.on");
        }
        else
        {
            field_74090_b.displayString += stringtranslate.translateKey("options.off");
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.id == 102)
        {
            mc.displayGuiScreen(field_74092_a);
        }
        else if (par1GuiButton.id == 104)
        {
            if (field_74089_d.equals("survival"))
            {
                field_74089_d = "creative";
            }
            else if (field_74089_d.equals("creative"))
            {
                field_74089_d = "adventure";
            }
            else
            {
                field_74089_d = "survival";
            }

            func_74088_g();
        }
        else if (par1GuiButton.id == 103)
        {
            field_74093_m = !field_74093_m;
            func_74088_g();
        }
        else if (par1GuiButton.id == 101)
        {
            mc.displayGuiScreen(null);
            if (singleplayer){
                mc.quitAndStartServer();
                System.out.println("A");
            }
            String s = mc.func_71401_C().func_71206_a(EnumGameType.func_77142_a(field_74089_d), field_74093_m);
            String s1 = "";

            if (!singleplayer){
                if (s != null){
                    s1 = mc.field_71439_g.func_70004_a("commands.publish.started", new Object[]{s});
                }else{
                    s1 = mc.field_71439_g.func_70004_a("commands.publish.failed", new Object[0]);
                }
            }else{
                if (s != null){
                    s1 = StringTranslate.getInstance().translateKeyFormat("commands.publish.started", new Object[]{s});
                }else{
                    s1 = StringTranslate.getInstance().translateKeyFormat("commands.publish.failed", new Object[0]);
                }
            }

            mc.ingameGUI.func_73827_b().func_73765_a(s1);
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
