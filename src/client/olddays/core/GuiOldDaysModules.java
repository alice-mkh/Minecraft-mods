package net.minecraft.src;

public class GuiOldDaysModules extends GuiScreen{
    private GuiScreen parent;

    public GuiOldDaysModules(GuiScreen guiscreen){
        parent = guiscreen;
    }

    public void updateScreen()
    {
    }

    public void initGui()
    {
        StringTranslate stringtranslate = StringTranslate.getInstance();
        controlList.add(new GuiButton(0, width / 2 - 75, height - 28, 150, 20, stringtranslate.translateKey("menu.returnToGame")));
        for (int i = 0; i < mod_OldDays.modules.size(); i++){
            OldDaysModule module = ((OldDaysModule)mod_OldDays.modules.get(i));
            addModule(module.id);
        }
    }
    
    public void addModule(int i){
        String name = mod_OldDays.lang.get("module."+mod_OldDays.getModuleById(i).name.toLowerCase());
        int margin = 30;
        int x = width / 2 - 155;
        if (i % 2 != 0){
            x+=160;
        }
        GuiButton button = new GuiButton(i+1, x, height / 6 + 15 + ((i/2)*margin), 150, 20, name);
        controlList.add(button);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        if (!guibutton.enabled){
            return;
        }
        if (guibutton.id == 0)
        {
            mc.displayGuiScreen(parent);
        }else{
            mc.displayGuiScreen(new GuiOldDaysSettings(this, guibutton.id-1));
        }
    }

    public void drawScreen(int i, int j, float f)
    {
        drawDefaultBackground();
        super.drawScreen(i,j,f);
    }
}