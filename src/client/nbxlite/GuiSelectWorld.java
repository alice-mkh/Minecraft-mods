package net.minecraft.src;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.lang.reflect.Method;
import java.io.File;
import net.minecraft.client.Minecraft;
import net.minecraft.src.nbxlite.gui.*;
import net.minecraft.src.nbxlite.format.SaveConverterMcRegion;
import net.minecraft.src.nbxlite.indev.McLevelImporter;

public class GuiSelectWorld extends GuiScreen
{
    public static boolean nbxlite = false;

    /** simple date formater */
    private final DateFormat dateFormatter = new SimpleDateFormat();

    /**
     * A reference to the screen object that created this. Used for navigating between screens.
     */
    protected GuiScreen parentScreen;

    /** The title string that is displayed in the top-center of the screen. */
    protected String screenTitle;

    /** True if a world has been selected. */
    private boolean selected;

    /** the currently selected world */
    private int selectedWorld;

    /** The save list for the world selection screen */
    private List saveList;
    private GuiWorldSlot worldSlotContainer;

    /** E.g. World, Welt, Monde, Mundo */
    private String localizedWorldText;
    private String localizedMustConvertText;
    private String localizedGameModeText[];

    /** set to true if you arein the process of deleteing a world/save */
    private boolean deleting;

    /** the rename button in the world selection gui */
    private GuiButton buttonRename;

    /** the select button in the world selection gui */
    private GuiButton buttonSelect;
    private GuiButton buttonSelect2;

    /** the delete button in the world selection gui */
    private GuiButton buttonDelete;
    private GuiButton field_82316_w;

    public GuiSelectWorld(GuiScreen par1GuiScreen)
    {
        screenTitle = "Select world";
        selected = false;
        localizedGameModeText = new String[3];
        parentScreen = par1GuiScreen;
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        screenTitle = stringtranslate.translateKey("selectWorld.title");
        localizedWorldText = stringtranslate.translateKey("selectWorld.world");
        localizedMustConvertText = stringtranslate.translateKey("selectWorld.conversion");
        localizedGameModeText[EnumGameType.SURVIVAL.getID()] = stringtranslate.translateKey("gameMode.survival");
        localizedGameModeText[EnumGameType.CREATIVE.getID()] = stringtranslate.translateKey("gameMode.creative");
        localizedGameModeText[EnumGameType.ADVENTURE.getID()] = stringtranslate.translateKey("gameMode.adventure");
        loadSaves();
        worldSlotContainer = new GuiWorldSlot(this);
        worldSlotContainer.registerScrollButtons(controlList, 4, 5);
        initButtons();
    }

    /**
     * loads the saves
     */
    private void loadSaves()
    {
        if (nbxlite){
            saveList = ODNBXlite.saveLoader.getSaveList();
        }else{
            saveList = mc.getSaveLoader().getSaveList();
        }
        Collections.sort(saveList);
        selectedWorld = -1;
    }

    /**
     * returns the file name of the specified save number
     */
    protected String getSaveFileName(int par1)
    {
        return ((SaveFormatComparator)saveList.get(par1)).getFileName();
    }

    /**
     * returns the name of the saved game
     */
    protected String getSaveName(int par1)
    {
        String s = ((SaveFormatComparator)saveList.get(par1)).getDisplayName();

        if (s == null || MathHelper.stringNullOrLengthZero(s))
        {
            StringTranslate stringtranslate = StringTranslate.getInstance();
            s = (new StringBuilder()).append(stringtranslate.translateKey("selectWorld.world")).append(" ").append(par1 + 1).toString();
        }

        return s;
    }

    /**
     * intilize the buttons for this GUI
     */
    public void initButtons()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(buttonSelect = new GuiButton(1, width / 2 - 154, height - 52, 150, 20, stringtranslate.translateKey("selectWorld.select")));
        controlList.add(buttonSelect2 = new GuiButton(-1, width / 2 - 209, height - 52, 50, 20, stringtranslate.translateKey("Play SMP")));
        controlList.add(new GuiButton(3, width / 2 + 4, height - 52, 150, 20, stringtranslate.translateKey("selectWorld.create")));
        controlList.add(buttonDelete = new GuiButton(6, width / 2 - 154, height - 28, 72, 20, stringtranslate.translateKey("selectWorld.rename")));
        controlList.add(buttonRename = new GuiButton(2, width / 2 - 76, height - 28, 72, 20, stringtranslate.translateKey("selectWorld.delete")));
        controlList.add(field_82316_w = new GuiButton(7, width / 2 + 4, height - 28, 72, 20, stringtranslate.translateKey("selectWorld.recreate")));
        controlList.add(new GuiButton(0, width / 2 + 82, height - 28, 72, 20, stringtranslate.translateKey("gui.cancel")));
        buttonSelect.enabled = false;
        buttonSelect2.enabled = false;
        buttonRename.enabled = false;
        buttonDelete.enabled = false;
        field_82316_w.enabled = false;
        buttonSelect2.drawButton = mc.useSP && mc.sspoptions.getSMPButton();
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (!par1GuiButton.enabled)
        {
            return;
        }

        if (par1GuiButton.id == 2)
        {
            String s = getSaveName(selectedWorld);

            if (s != null)
            {
                deleting = true;
                GuiYesNo guiyesno = getDeleteWorldScreen(this, s, selectedWorld);
                mc.displayGuiScreen(guiyesno);
            }
        }
        else if (par1GuiButton.id == 1)
        {
            mc.enableSP = mc.useSP;
            selectWorld(selectedWorld);
        }
        else if (par1GuiButton.id == -1)
        {
            mc.enableSP = false;
            selectWorld(selectedWorld);
        }
        else if (par1GuiButton.id == 3)
        {
            if (nbxlite){
                try{
                    Class c = net.minecraft.src.nbxlite.gui.GuiCreateWorld2.class;
                    Object o = c.getDeclaredConstructor(GuiScreen.class).newInstance(this);
                    mc.displayGuiScreen((GuiScreen)o);
                }catch(Throwable t){
                    nbxlite = false;
                }
            }else{
                mc.displayGuiScreen(new GuiCreateWorld(this));
            }
        }
        else if (par1GuiButton.id == 6)
        {
            mc.displayGuiScreen(new GuiRenameWorld(this, getSaveFileName(selectedWorld)));
        }
        else if (par1GuiButton.id == 0)
        {
            mc.displayGuiScreen(parentScreen);
        }
        else if (par1GuiButton.id == 7)
        {
            GuiCreateWorld guicreateworld = new GuiCreateWorld(this);
            ISaveHandler isavehandler = mc.getSaveLoader().getSaveLoader(getSaveFileName(selectedWorld), false);
            WorldInfo worldinfo = isavehandler.loadWorldInfo();
            isavehandler.flush();
            guicreateworld.func_82286_a(worldinfo);
            mc.displayGuiScreen(guicreateworld);
        }
        else
        {
            worldSlotContainer.actionPerformed(par1GuiButton);
        }
    }


    /**
     * Gets the selected world.
     */
    public void selectWorld(int par1)
    {
        if (nbxlite && getSaveFileName(par1).endsWith(".mclevel")){
            File dir = ((SaveConverterMcRegion)ODNBXlite.saveLoader).getSaveDirectory();
            try{
                File mclevel = new File(dir, getSaveFileName(par1));
                ODNBXlite.mclevelimporter = new McLevelImporter(mclevel);
                if (mc.enableSP){
                    mc.setController(EnumGameType.SURVIVAL);
                    mc.startWorldSSP(getSaveFileName(par1).replace(".mclevel",""), getSaveName(par1), new WorldSettings(0L, EnumGameType.SURVIVAL, false, false, WorldType.DEFAULT));
//             MinecraftHook.startWorldHook(s, getSaveName(par1), null);
                    mc.displayGuiScreen(null);
                }else{
                    String s1 = getSaveName(par1);
                    if (s1 == null){
                        s1 = (new StringBuilder()).append("World").append(par1).toString();
                    }
                    mc.launchIntegratedServer(getSaveFileName(par1).replace(".mclevel",""), getSaveName(par1), new WorldSettings(0L, EnumGameType.SURVIVAL, false, false, WorldType.DEFAULT));
                }
                mclevel.renameTo(new File(dir, getSaveFileName(par1).replace(".mclevel","")+"/"+mclevel.getName()));
                mc.displayGuiScreen(null);
            }catch(Exception ex){
                ISaveFormat isaveformat = ODNBXlite.saveLoader;
                isaveformat.flushCache();
                File mclevel = new File(dir, getSaveFileName(par1).replace(".mclevel","")+"/"+getSaveFileName(par1));
                mclevel.renameTo(new File(dir, mclevel.getName()));
                isaveformat.deleteWorldDirectory(getSaveFileName(par1).replace(".mclevel",""));
                loadSaves();
                System.out.println(ex);
                return;
            }
            return;
        }
        if (nbxlite && !mc.getSaveLoader().getSaveLoader(getSaveFileName(par1), false).loadWorldInfo().nbxlite){
            try{
                ODNBXlite.Import = true;
                Class c = net.minecraft.src.nbxlite.gui.GuiCreateWorld2.class;
                Method method = c.getDeclaredMethod("setDefaultNBXliteSettings");
                method.invoke(null);
                Class c2 = net.minecraft.src.nbxlite.gui.GuiNBXlite.class;
                Object o = c2.getDeclaredConstructor(GuiScreen.class, String.class, Integer.TYPE).newInstance(this, getSaveFileName(par1), par1);
                mc.displayGuiScreen((GuiScreen)o);
                return;
            }catch(Throwable t){
                t.printStackTrace();
                nbxlite = false;
            }
        }
        mc.displayGuiScreen(null);

        if (selected)
        {
            return;
        }

        selected = true;

        String s = getSaveFileName(par1);

        if (s == null)
        {
            s = (new StringBuilder()).append("World").append(par1).toString();
        }
        if (nbxlite){
            ODNBXlite.Import = false;
        }
        if (mc.enableSP){
            mc.setController(((SaveFormatComparator)saveList.get(par1)).getEnumGameType());
            mc.startWorldSSP(s, getSaveName(par1), null);
//             MinecraftHook.startWorldHook(s, getSaveName(par1), null);
            mc.displayGuiScreen(null);
        }else{
            String s1 = getSaveName(par1);
            if (s1 == null){
                s1 = (new StringBuilder()).append("World").append(par1).toString();
            }
            mc.launchIntegratedServer(s, s1, null);
        }
    }

    public void confirmClicked(boolean par1, int par2)
    {
        if (deleting)
        {
            deleting = false;

            if (par1)
            {
                ISaveFormat isaveformat = mc.getSaveLoader();
                isaveformat.flushCache();
                isaveformat.deleteWorldDirectory(getSaveFileName(par2));
                loadSaves();
            }

            mc.displayGuiScreen(this);
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        worldSlotContainer.drawScreen(par1, par2, par3);
        drawCenteredString(fontRenderer, screenTitle, width / 2, 20, 0xffffff);
        super.drawScreen(par1, par2, par3);
    }

    /**
     * Gets a GuiYesNo screen with the warning, buttons, etc.
     */
    public static GuiYesNo getDeleteWorldScreen(GuiScreen par0GuiScreen, String par1Str, int par2)
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        String s = stringtranslate.translateKey("selectWorld.deleteQuestion");
        String s1 = (new StringBuilder()).append("'").append(par1Str).append("' ").append(stringtranslate.translateKey("selectWorld.deleteWarning")).toString();
        String s2 = stringtranslate.translateKey("selectWorld.deleteButton");
        String s3 = stringtranslate.translateKey("gui.cancel");
        GuiYesNo guiyesno = new GuiYesNo(par0GuiScreen, s, s1, s2, s3, par2);
        return guiyesno;
    }

    static List getSize(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.saveList;
    }

    /**
     * called whenever an element in this gui is selected
     */
    static int onElementSelected(GuiSelectWorld par0GuiSelectWorld, int par1)
    {
        return par0GuiSelectWorld.selectedWorld = par1;
    }

    /**
     * returns the world currently selected
     */
    static int getSelectedWorld(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.selectedWorld;
    }

    /**
     * returns the select button
     */
    static GuiButton getSelectButton(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonSelect;
    }

    static GuiButton getSMPSelectButton(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonSelect2;
    }

    /**
     * returns the rename button
     */
    static GuiButton getRenameButton(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonRename;
    }

    /**
     * returns the delete button
     */
    static GuiButton getDeleteButton(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.buttonDelete;
    }

    static GuiButton func_82312_f(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.field_82316_w;
    }

    static String func_82313_g(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.localizedWorldText;
    }

    static DateFormat func_82315_h(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.dateFormatter;
    }

    static String func_82311_i(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.localizedMustConvertText;
    }

    static String[] func_82314_j(GuiSelectWorld par0GuiSelectWorld)
    {
        return par0GuiSelectWorld.localizedGameModeText;
    }
}
