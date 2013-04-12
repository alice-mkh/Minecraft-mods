package net.minecraft.src.nbxlite.gui;

import java.util.List;
import java.util.Collections;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class GuiNBXlite extends GuiScreen{
    private String selectedWorld;
    private int number;
    private boolean newworld;
    private int leftmargin = 90;
    private GuiScreen parent;
    private GuiButton[] genButtons;

    private Page page;

    public GuiNBXlite(GuiScreen guiscreen){
        parent = guiscreen;
        newworld = true;
    }

    public GuiNBXlite(GuiScreen guiscreen, String world, int i){
        this(guiscreen);
        selectedWorld = world;
        number = i;
        newworld = false;
    }

    @Override
    public void updateScreen()
    {
    }

    @Override
    public void initGui()
    {
        buttonList.add(new GuiButton(0, width / 2 - 155, height - 28, 150, 20, mod_OldDays.lang.get("continue")));
        buttonList.add(new GuiButton(1, width / 2 + 5, height - 28, 150, 20, StringTranslate.getInstance().translateKey("gui.cancel")));
        genButtons = new GuiButton[GeneratorList.genlength + 1];
        for (int i = 0; i < genButtons.length; i++){
            genButtons[i] = new GuiButton(2 + i, width / 2 - 170, height / 6 + (i * 21), 100, 20, "");
            genButtons[i].displayString = mod_OldDays.lang.get(GeneratorList.genname[i]);
            buttonList.add(genButtons[i]);
        }
        genButtons[GeneratorList.gencurrent].enabled = false;
    }

    public void refreshPage(){
        switch (GeneratorList.gencurrent){
            case 0: page = new PageFinite(this, false); break;
            case 1: page = new PageFinite(this, true); break;
            case 2: page = new PageInfdevAlpha(this, 2); break;
            case 3: page = new PageInfdevAlpha(this, 1); break;
            case 4: page = new PageInfdevAlpha(this, 0); break;
            case 5: page = new PageBeta(this); break;
            default: page = new PageRelease(this); break;
        }
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int scaledWidth = scaledresolution.getScaledWidth();
        int scaledHeight = scaledresolution.getScaledHeight();
        page.setWorldAndResolution(mc, scaledWidth, scaledHeight);
        page.initButtons();
        page.updateButtonText();
        page.updateButtonVisibility();
        page.updateButtonPosition();
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height){
        super.setWorldAndResolution(mc, width, height);
        refreshPage();
    }

    public void selectNBXliteSettings(){
        page.selectSettings();
        ODNBXlite.setCloudHeight(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, ODNBXlite.IndevMapType);
        ODNBXlite.setSkyBrightness(ODNBXlite.MapTheme);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 0);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 1);
        ODNBXlite.setSkyColor(ODNBXlite.Generator, ODNBXlite.MapFeatures, ODNBXlite.MapTheme, 2);
    }

    public void selectWorld()
    {
        ISaveFormat isaveformat = ODNBXlite.saveLoader;
        List saveList = null;
        try{
            saveList = isaveformat.getSaveList();
        }catch(Exception e){
            e.printStackTrace();
            return;
        }
        Collections.sort(saveList);
        mc.displayGuiScreen(null);
        selectNBXliteSettings();
        String s = selectedWorld;
        if (s == null)
        {
            s = (new StringBuilder()).append("World").append(selectedWorld).toString();
        }
        if (mc.enableSP){
            mc.setController(((SaveFormatComparator)saveList.get(number)).getEnumGameType());
            mc.startWorldSSP(s, selectedWorld, null);
            mc.displayGuiScreen(null);
        }else{
            mc.launchIntegratedServer(s, selectedWorld, null);
        }
    }

    @Override
    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }
        if (guibutton.id == 1){
            GuiCreateWorld2.setDefaultNBXliteSettings();
            mc.displayGuiScreen(parent);
            if (parent instanceof GuiCreateWorld2){
                ((GuiCreateWorld2)parent).fixHardcoreButtons();
            }
        }else if (guibutton.id == 0){
            if (!newworld){
                selectWorld();
                return;
            }
            selectNBXliteSettings();
            mc.displayGuiScreen(parent);
            if (parent instanceof GuiCreateWorld2){
                ((GuiCreateWorld2)parent).fixHardcoreButtons();
            }
        }else if (guibutton.id >= 2 && guibutton.id < 2 + genButtons.length){
            genButtons[GeneratorList.gencurrent].enabled = true;
            GeneratorList.gencurrent = guibutton.id - 2;
            guibutton.enabled = false;
            refreshPage();
        }
    }

    @Override
    protected void mouseClicked(int par1, int par2, int par3){
        if (page != null){
            page.mouseClicked2(par1, par2, par3);
        }
        super.mouseClicked(par1, par2, par3);
    }

    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3){
        if (page != null){
            page.mouseMovedOrUp2(par1, par2, par3);
        }
        super.mouseMovedOrUp(par1, par2, par3);
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, mod_OldDays.lang.get(GeneratorList.gendesc[GeneratorList.gencurrent]), width / 2 + leftmargin, height / 6 - 30, 0xa0a0a0);
        page.drawScreen(i, j, f);
        super.drawScreen(i, j, f);
    }
}