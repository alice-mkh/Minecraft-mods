package net.minecraft.src.nbxlite.gui;

import java.util.List;
import java.util.Collections;
import java.util.Random;
import org.lwjgl.opengl.GL11;
import net.minecraft.client.Minecraft;
import net.minecraft.src.*;

public class GuiNBXlite extends GuiScreen{
    private int currentGen;
    private int prevGen;

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
        prevGen = -1;
        setDefaultSettings();
        applySettings();
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
            genButtons[i] = new GuiButton(2 + i, width / 2 - 170, height / 6 + ((i + 1) * 21), 100, 20, "");
            genButtons[i].displayString = mod_OldDays.lang.get("nbxlite.defaultgenerator" + (i + 1));
            buttonList.add(genButtons[i]);
        }
        genButtons[currentGen].enabled = false;
    }

    public void setPage(){
        prevGen = currentGen;
        switch (currentGen){
            case 0: page = new PageFinite(this, false); break;
            case 1: page = new PageFinite(this, true); break;
            case 2: page = new PageAlpha(this); break;
            case 3: page = new PageBeta(this); break;
            default: page = new PageRelease(this); break;
        }
    }

    public void refreshPage(boolean reset){
        if (currentGen != prevGen){
            setPage();
        }
        page.setMc(mc);
        ScaledResolution scaledresolution = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        int scaledWidth = scaledresolution.getScaledWidth();
        int scaledHeight = scaledresolution.getScaledHeight();
        page.setWorldAndResolution(mc, scaledWidth, scaledHeight);
        if (reset){
            page.setDefaultSettings();
        }
        page.initButtons();
        page.updateButtonText();
        page.updateButtonVisibility();
        page.scrolled();
        page.calculateMinScrolling();
    }

    @Override
    public void setWorldAndResolution(Minecraft mc, int width, int height){
        super.setWorldAndResolution(mc, width, height);
        refreshPage(false);
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
        applySettings();
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
            setDefaultSettings();
            applySettings();
            mc.displayGuiScreen(parent);
            if (parent instanceof GuiCreateWorld2){
                ((GuiCreateWorld2)parent).fixHardcoreButtons();
            }
        }else if (guibutton.id == 0){
            if (!newworld){
                selectWorld();
                return;
            }
            applySettings();
            mc.displayGuiScreen(parent);
            if (parent instanceof GuiCreateWorld2){
                ((GuiCreateWorld2)parent).fixHardcoreButtons();
            }
        }else if (guibutton.id >= 2 && guibutton.id < 2 + genButtons.length){
            genButtons[currentGen].enabled = true;
            currentGen = guibutton.id - 2;
            guibutton.enabled = false;
            refreshPage(true);
            page.setDefaultSettings();
        }
    }

    @Override
    protected void mouseMovedOrUp(int par1, int par2, int par3){
        if (page != null){
            page.mouseMovedOrUp2(par1, par2, par3);
        }
        super.mouseMovedOrUp(par1, par2, par3);
    }

    @Override
    protected void func_85041_a(int i, int j, int k, long l)
    {
        if (page != null){
            page.func_85041_a_2(i, j, k, l);
        }
        super.func_85041_a(i, j, k, l);
    }

    @Override
    public void handleMouseInput()
    {
        if (page != null){
            page.handleMouseInput();
        }
        super.handleMouseInput();
    }

    @Override
    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        page.drawScrollingBackground();
        page.drawButtons(i, j);
        page.drawScreen(i, j, f);
        page.drawFrameAndScrollbar();
        drawCenteredString(fontRenderer, mod_OldDays.lang.get("nbxlite.defaultgenerator" + (currentGen + 1) + ".desc"), width / 2 + leftmargin, height / 6 - 30, 0xa0a0a0);
        super.drawScreen(i, j, f);
    }

    public String getButtonName(){
        return mod_OldDays.lang.get("settings") + ": " + page.getString();
    }

    public void applySettings(){
        ODNBXlite.flags.clear();
        page.applySettings();
        ODNBXlite.Structures = ODNBXlite.getDefaultStructures(enableStructuresByDefault(), ODNBXlite.Generator, ODNBXlite.MapFeatures);
        ODNBXlite.setDefaultColors();
    }

    public void setDefaultSettings(){
        currentGen = ODNBXlite.DefaultGenerator;
        if (page != null){
            page.setDefaultSettings();
        }
        setPage();
        page.setDefaultSettings();
    }

    public void loadSettingsFromWorldInfo(WorldInfo par1WorldInfo){
        ODNBXlite.Generator = par1WorldInfo.mapGen;
        ODNBXlite.MapFeatures = par1WorldInfo.mapGenExtra;
        if (ODNBXlite.Generator == ODNBXlite.GEN_OLDBIOMES){
            currentGen = 3;
        }else if (ODNBXlite.Generator == ODNBXlite.GEN_NEWBIOMES){
            currentGen = 4;
        }else{
            switch (ODNBXlite.MapFeatures){
                case ODNBXlite.FEATURES_INDEV: currentGen = 1; break;
                case ODNBXlite.FEATURES_CLASSIC: currentGen = 0; break;
                default: currentGen = 2; break;
            }
        }
        setPage();
        page.loadFromWorldInfo(par1WorldInfo);

        ODNBXlite.IndevWidthX = par1WorldInfo.indevX;
        ODNBXlite.IndevWidthZ = par1WorldInfo.indevZ;
        ODNBXlite.IndevHeight = par1WorldInfo.indevY;
        ODNBXlite.SurrWaterType = par1WorldInfo.surrwatertype;
        ODNBXlite.SurrWaterHeight = par1WorldInfo.surrwaterheight;
        ODNBXlite.SurrGroundType = par1WorldInfo.surrgroundtype;
        ODNBXlite.SurrGroundHeight = par1WorldInfo.surrgroundheight;

        ODNBXlite.CloudHeight = par1WorldInfo.cloudheight;
        ODNBXlite.SkyBrightness = par1WorldInfo.skybrightness;
        ODNBXlite.SkyColor = par1WorldInfo.skycolor;
        ODNBXlite.FogColor = par1WorldInfo.fogcolor;
        ODNBXlite.CloudColor = par1WorldInfo.cloudcolor;
    }

    public int allowWorldTypes(){
        if (ODNBXlite.Generator != ODNBXlite.GEN_NEWBIOMES){
            return 0;
        }
        return GeneratorList.feat2worldtype[ODNBXlite.MapFeatures];
    }

    public boolean enableStructuresByDefault(){
        return ODNBXlite.Generator == ODNBXlite.GEN_NEWBIOMES;
    }

    public boolean isIndev(){
        return ODNBXlite.Generator == ODNBXlite.GEN_BIOMELESS && ODNBXlite.MapFeatures == ODNBXlite.FEATURES_INDEV;
    }
}