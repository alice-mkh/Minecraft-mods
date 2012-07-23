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
            addButton(module.id, false, i, mod_OldDays.lang.get("module."+module.name.toLowerCase()), true);
        }
        postInitGui(count);
    }

    protected void actionPerformed(GuiButton guibutton)
    {
        super.actionPerformed(guibutton);
        if (guibutton.id <= 0 || guibutton.id >= 98){
            return;
        }
        mc.displayGuiScreen(new GuiOldDaysSettings(this, guibutton.id-1));
    }
}