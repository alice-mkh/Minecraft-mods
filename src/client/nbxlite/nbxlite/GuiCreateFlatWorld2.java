package net.minecraft.src;

import java.util.List;
import java.util.Iterator;
import net.minecraft.client.Minecraft;
import net.minecraft.src.nbxlite.gui.GuiCreateWorld2;

public class GuiCreateFlatWorld2 extends GuiCreateFlatWorld
{
    private static RenderItem theRenderItem = new RenderItem();
    private final GuiCreateWorld2 createWorldGui;
    private FlatGeneratorInfo theFlatGeneratorInfo;
    private String customizationTitle;
    private String layerMaterialLabel;
    private String heightLabel;
    private GuiCreateFlatWorldListSlot createFlatWorldListSlotGui;
    private GuiButton buttonAddLayer;
    private GuiButton buttonEditLayer;
    private GuiButton buttonRemoveLayer;

    public GuiCreateFlatWorld2(GuiCreateWorld2 par1, String par2Str)
    {
        super(null, par2Str);
        theFlatGeneratorInfo = FlatGeneratorInfo.getDefaultFlatGenerator();
        createWorldGui = par1;
        setFlatGeneratorInfo(par2Str);
    }

    public String getFlatGeneratorInfo()
    {
        return theFlatGeneratorInfo.toString();
    }

    public void setFlatGeneratorInfo(String par1Str)
    {
        theFlatGeneratorInfo = FlatGeneratorInfo.createFlatGeneratorFromString(par1Str);
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        controlList.clear();
        customizationTitle = StatCollector.translateToLocal("createWorld.customize.flat.title");
        layerMaterialLabel = StatCollector.translateToLocal("createWorld.customize.flat.tile");
        heightLabel = StatCollector.translateToLocal("createWorld.customize.flat.height");
        createFlatWorldListSlotGui = new GuiCreateFlatWorldListSlot(this);
        controlList.add(buttonAddLayer = new GuiButton(2, width / 2 - 154, height - 52, 100, 20, (new StringBuilder()).append(StatCollector.translateToLocal("createWorld.customize.flat.addLayer")).append(" (NYI)").toString()));
        controlList.add(buttonEditLayer = new GuiButton(3, width / 2 - 50, height - 52, 100, 20, (new StringBuilder()).append(StatCollector.translateToLocal("createWorld.customize.flat.editLayer")).append(" (NYI)").toString()));
        controlList.add(buttonRemoveLayer = new GuiButton(4, width / 2 - 155, height - 52, 150, 20, StatCollector.translateToLocal("createWorld.customize.flat.removeLayer")));
        controlList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, StatCollector.translateToLocal("gui.done")));
        controlList.add(new GuiButton(5, width / 2 + 5, height - 52, 150, 20, StatCollector.translateToLocal("createWorld.customize.presets")));
        controlList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, StatCollector.translateToLocal("gui.cancel")));
        buttonAddLayer.drawButton = buttonEditLayer.drawButton = false;
        theFlatGeneratorInfo.func_82645_d();
        func_82270_g();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        int i = theFlatGeneratorInfo.getFlatLayers().size() - createFlatWorldListSlotGui.field_82454_a - 1;

        if (par1GuiButton.id == 1)
        {
            mc.displayGuiScreen(createWorldGui);
        }
        else if (par1GuiButton.id == 0)
        {
            createWorldGui.field_82290_a = getFlatGeneratorInfo();
            mc.displayGuiScreen(createWorldGui);
        }
        else if (par1GuiButton.id == 5)
        {
            mc.displayGuiScreen(new GuiFlatPresets(this));
        }
        else if (par1GuiButton.id == 4 && func_82272_i())
        {
            theFlatGeneratorInfo.getFlatLayers().remove(i);
            createFlatWorldListSlotGui.field_82454_a = Math.min(createFlatWorldListSlotGui.field_82454_a, theFlatGeneratorInfo.getFlatLayers().size() - 1);
        }

        theFlatGeneratorInfo.func_82645_d();
        func_82270_g();
    }

    public void func_82270_g()
    {
        boolean flag = func_82272_i();
        buttonRemoveLayer.enabled = flag;
        buttonEditLayer.enabled = flag;
        buttonEditLayer.enabled = false;
        buttonAddLayer.enabled = false;
    }

    private boolean func_82272_i()
    {
        return createFlatWorldListSlotGui.field_82454_a > -1 && createFlatWorldListSlotGui.field_82454_a < theFlatGeneratorInfo.getFlatLayers().size();
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        drawDefaultBackground();
        createFlatWorldListSlotGui.drawScreen(par1, par2, par3);
        drawCenteredString(fontRenderer, customizationTitle, width / 2, 8, 0xffffff);
        int i = width / 2 - 92 - 16;
        drawString(fontRenderer, layerMaterialLabel, i, 32, 0xffffff);
        drawString(fontRenderer, heightLabel, (i + 2 + 213) - fontRenderer.getStringWidth(heightLabel), 32, 0xffffff);
        GuiButton guibutton;
         for (Iterator iterator = controlList.iterator(); iterator.hasNext(); guibutton.drawButton(mc, par1, par2))
         {
            guibutton = (GuiButton)iterator.next();
        }
    }

    static RenderItem getRenderItem()
    {
        return theRenderItem;
    }

    static FlatGeneratorInfo func_82271_a(GuiCreateFlatWorld2 par0GuiCreateFlatWorld)
    {
        return par0GuiCreateFlatWorld.theFlatGeneratorInfo;
    }
}
