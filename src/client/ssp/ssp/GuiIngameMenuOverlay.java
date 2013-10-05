package net.minecraft.src.ssp;

import java.util.List;
import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiIngameMenu;
import net.minecraft.src.GuiMainMenu;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiShareToLan;
import net.minecraft.src.MathHelper;
import net.minecraft.src.Minecraft;
import net.minecraft.src.I18n;
import net.minecraft.src.StatList;

public class GuiIngameMenuOverlay extends GuiOverlay{
    private int updateCounter;
    private int updateCounter2;

    public GuiIngameMenuOverlay(){
        super(GuiIngameMenu.class);
    }

    @Override
    public boolean shouldBeAdded(){
        return Minecraft.getMinecraft().enableSP;
    }

    @Override
    public void onAdded(GuiScreen gui){
        updateCounter = 0;
        updateCounter2 = 0;
    }

    @Override
    public void initGui(List buttonList, int width, int height){
        updateCounter2 = 0;
        renameButton(buttonList, 1, I18n.getString("menu.returnToMenu"));
        if (!SSPOptions.getShareButton()){
            resizeButton(buttonList, 0, 200, 20);
            hideButton(buttonList, 7, false);
        }else{
            disableButton(buttonList, 7, true);
        }
    }

    @Override
    public boolean actionPerformed(GuiScreen gui, GuiButton par1GuiButton){
        Minecraft mc = Minecraft.getMinecraft();
        switch (par1GuiButton.id){
            case 1:
                mc.statFileWriter.readStat(StatList.leaveGameStat, 1);
                mc.changeWorld1(null);
                mc.displayGuiScreen(new GuiMainMenu());
                return false;
            case 4:
                mc.displayGuiScreen(null);
                mc.setIngameFocus();
                mc.sndManager.resumeAllSounds(); //?
                return false;
        }
        return true;
    }

    @Override
    public void postUpdateScreen(GuiScreen gui){
        updateCounter++;
    }

    @Override
    public void postDrawScreen(GuiScreen gui, int x, int y, float f){
        boolean flag = !((WorldSSP)Minecraft.getMinecraft().theWorld).quickSaveWorld(updateCounter2++);
        if (flag || updateCounter < 20){
            float f2 = ((float)(updateCounter % 10) + f) / 10F;
            f2 = MathHelper.sin(f2 * (float)Math.PI * 2.0F) * 0.2F + 0.8F;
            int i = (int)(255F * f2);
            gui.drawString(gui.getFontRenderer(), "Saving level..", 8, gui.height - 16, i << 16 | i << 8 | i);
        }
    }
}
