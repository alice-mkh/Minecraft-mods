package net.minecraft.src;

public class GuiOldDaysModules extends GuiOldDaysBase{
    public GuiOldDaysModules(GuiScreen guiscreen){
        super(guiscreen);
    }

    public void initGui(){
        super.initGui();
        int count = mod_OldDays.modules.size();
        for (int i = 0; i < count; i++){
            OldDaysModule module = ((OldDaysModule)mod_OldDays.modules.get(i));
            if (module == null){
                continue;
            }
            addButton(module.id, true, module.id, mod_OldDays.lang.get("module."+module.name.toLowerCase()), true);
        }
        postInitGui(count);
        controlList.add(new GuiButton(100, width / 2 - 75 - 80, height - 28, 75, 20, mod_OldDays.lang.get("gui.ssp")+": "+mod_OldDays.lang.get(mc.useSP ? "gui.on" : "gui.off")));
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);
        if (guibutton.id == 100){
            mc.useSP = !mc.useSP;
            guibutton.displayString = mod_OldDays.lang.get("gui.ssp")+": "+mod_OldDays.lang.get(mc.useSP ? "gui.on" : "gui.off");
            mod_OldDays.saveman.saveCoreProperties();
        }
        if (guibutton.id <= 0 || guibutton.id >= LEFT_ID){
            return;
        }
        if (!guibutton.enabled){
            return;
        }
        mc.displayGuiScreen(new GuiOldDaysSettings(this, guibutton.id-1));
    }
}