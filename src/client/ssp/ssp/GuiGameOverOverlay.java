package net.minecraft.src.ssp;

import java.util.List;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiGameOver;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.I18n;
import net.minecraft.src.ISaveFormat;
import net.minecraft.src.Minecraft;

public class GuiGameOverOverlay extends GuiOverlay{
    public GuiGameOverOverlay(){
        super(GuiGameOver.class);
    }

    @Override
    public boolean shouldBeAdded(){
        return Minecraft.getMinecraft().enableSP;
    }

    @Override
    public void initGui(List buttonList, int width, int height){
        if (Minecraft.getMinecraft().theWorld.getWorldInfo().isHardcoreModeEnabled()){
            renameButton(buttonList, 1, I18n.getString("deathScreen.deleteWorld"));
        }
    }

    @Override
    public boolean actionPerformed(GuiScreen gui, GuiButton par1GuiButton){
        Minecraft mc = Minecraft.getMinecraft();
        if (par1GuiButton.id == 1){
            boolean hardcore = mc.theWorld.getWorldInfo().isHardcoreModeEnabled();
            if (hardcore){
                String s = mc.theWorld.getSaveHandler().getWorldDirectoryName();
                mc.exitToMainMenu("Deleting world");
                ISaveFormat isaveformat = mc.getSaveLoader();
                isaveformat.flushCache();
                isaveformat.deleteWorldDirectory(s);
                mc.displayGuiScreen(new GuiMainMenu());
            }
            return !hardcore;
        }else if (par1GuiButton.id == 2){
            mc.changeWorld1(null);
            mc.displayGuiScreen(new GuiMainMenu());
            return false;
        }
        return true;
    }
}
