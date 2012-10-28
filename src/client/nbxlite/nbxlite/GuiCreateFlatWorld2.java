package net.minecraft.src;

import java.util.List;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.src.nbxlite.gui.GuiCreateWorld2;

public class GuiCreateFlatWorld2 extends GuiCreateFlatWorld
{
    private static RenderItem field_82282_a = new RenderItem();
    private final GuiCreateWorld2 field_82277_b;
    private FlatGeneratorInfo field_82279_c;
    private String field_82276_d;
    private String field_82285_m;
    private String field_82283_n;
    private GuiCreateFlatWorldListSlot field_82284_o;
    private GuiButton field_82281_p;
    private GuiButton field_82280_q;
    private GuiButton field_82278_r;

    public GuiCreateFlatWorld2(GuiCreateWorld2 par1, String par2Str)
    {
        super(null, par2Str);
        field_82279_c = FlatGeneratorInfo.func_82649_e();
        field_82277_b = par1;
        func_82273_a(par2Str);
    }

    public String func_82275_e()
    {
        return field_82279_c.toString();
    }

    public void func_82273_a(String par1Str)
    {
        field_82279_c = FlatGeneratorInfo.func_82651_a(par1Str);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        controlList.clear();
        field_82276_d = StatCollector.translateToLocal("createWorld.customize.flat.title");
        field_82285_m = StatCollector.translateToLocal("createWorld.customize.flat.tile");
        field_82283_n = StatCollector.translateToLocal("createWorld.customize.flat.height");
        field_82284_o = new GuiCreateFlatWorldListSlot(this);
        controlList.add(field_82281_p = new GuiButton(2, width / 2 - 154, height - 52, 100, 20, (new StringBuilder()).append(StatCollector.translateToLocal("createWorld.customize.flat.addLayer")).append(" (NYI)").toString()));
        controlList.add(field_82280_q = new GuiButton(3, width / 2 - 50, height - 52, 100, 20, (new StringBuilder()).append(StatCollector.translateToLocal("createWorld.customize.flat.editLayer")).append(" (NYI)").toString()));
        controlList.add(field_82278_r = new GuiButton(4, width / 2 - 155, height - 52, 150, 20, StatCollector.translateToLocal("createWorld.customize.flat.removeLayer")));
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, StatCollector.translateToLocal("gui.done")));
        controlList.add(new GuiButton(5, width / 2 + 5, height - 52, 150, 20, StatCollector.translateToLocal("createWorld.customize.presets")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, StatCollector.translateToLocal("gui.cancel")));
        field_82281_p.drawButton = field_82280_q.drawButton = false;
        field_82279_c.func_82645_d();
        func_82270_g();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        int i = field_82279_c.func_82650_c().size() - field_82284_o.field_82454_a - 1;

        if (par1GuiButton.id == 1)
        {
            mc.displayGuiScreen(field_82277_b);
        }
        else if (par1GuiButton.id == 0)
        {
            field_82277_b.field_82290_a = func_82275_e();
            mc.displayGuiScreen(field_82277_b);
        }
        else if (par1GuiButton.id == 5)
        {
            mc.displayGuiScreen(new GuiFlatPresets(this));
        }
        else if (par1GuiButton.id == 4 && func_82272_i())
        {
            field_82279_c.func_82650_c().remove(i);
            field_82284_o.field_82454_a = Math.min(field_82284_o.field_82454_a, field_82279_c.func_82650_c().size() - 1);
        }

        field_82279_c.func_82645_d();
        func_82270_g();
    }

    public void func_82270_g()
    {
        boolean flag = func_82272_i();
        field_82278_r.enabled = flag;
        field_82280_q.enabled = flag;
        field_82280_q.enabled = false;
        field_82281_p.enabled = false;
    }

    private boolean func_82272_i()
    {
        return field_82284_o.field_82454_a > -1 && field_82284_o.field_82454_a < field_82279_c.func_82650_c().size();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        field_82284_o.drawScreen(par1, par2, par3);
        drawCenteredString(fontRenderer, field_82276_d, width / 2, 8, 0xffffff);
        int i = width / 2 - 92 - 16;
        drawString(fontRenderer, field_82285_m, i, 32, 0xffffff);
        drawString(fontRenderer, field_82283_n, (i + 2 + 213) - fontRenderer.getStringWidth(field_82283_n), 32, 0xffffff);
        GuiButton guibutton;

        for (Iterator iterator = controlList.iterator(); iterator.hasNext(); guibutton.drawButton(mc, par1, par2))
        {
            guibutton = (GuiButton)iterator.next();
        }
    }

    static RenderItem func_82274_h()
    {
        return field_82282_a;
    }

    static FlatGeneratorInfo func_82271_a(GuiCreateFlatWorld2 par0GuiCreateFlatWorld)
    {
        return par0GuiCreateFlatWorld.field_82279_c;
    }
}
