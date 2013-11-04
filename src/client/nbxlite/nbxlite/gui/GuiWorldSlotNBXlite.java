package net.minecraft.src;

import net.minecraft.src.nbxlite.gui.PageAlpha;
import net.minecraft.src.nbxlite.gui.PageBeta;
import net.minecraft.src.nbxlite.gui.PageFinite;
import net.minecraft.src.nbxlite.gui.PageRelease;

class GuiWorldSlotNBXlite extends GuiWorldSlot{
    public GuiWorldSlotNBXlite(GuiSelectWorld par1GuiSelectWorld){
        super(par1GuiSelectWorld, 46);
    }

    @Override
    protected int getContentHeight(){
        return GuiSelectWorld.getSize(parentWorldGui).size() * 46;
    }

    @Override
    protected void drawSlot(int par1, int par2, int par3, int par4, Tessellator par5Tessellator){
        super.drawSlot(par1, par2, par3, par4, par5Tessellator);
        SaveFormatComparator sfc = (SaveFormatComparator)GuiSelectWorld.getSize(parentWorldGui).get(par1);
        String name = sfc.getFileName();

        ISaveFormat loader = ODNBXlite.saveLoader;
        ISaveHandler handler = loader.getSaveLoader(name, false);
        WorldInfo info = handler.loadWorldInfo();

        String str = "§cUnknown";
        if (info.nbxlite){
            if (info.mapGen == ODNBXlite.GEN_OLDBIOMES){
                str = PageBeta.getString(info.mapGenExtra, ODNBXlite.getFlagFromString(info.flags, "jungle"));
            }else if (info.mapGen == ODNBXlite.GEN_NEWBIOMES){
                str = PageRelease.getString(info.mapGenExtra);
            }else if (info.mapGenExtra == ODNBXlite.FEATURES_CLASSIC || info.mapGenExtra == ODNBXlite.FEATURES_INDEV){
                boolean indev = info.mapGenExtra == ODNBXlite.FEATURES_INDEV;
                str = PageFinite.getString(info.indevX, info.indevY, info.indevZ, info.mapType, info.mapTheme, indev);
            }else{
                int features = -1;
                for (int i = 0; i < ODNBXlite.BIOMELESS_FEATURES.length; i++){
                    if (ODNBXlite.BIOMELESS_FEATURES[i] == info.mapGenExtra){
                        features = i;
                    }
                }
                str = PageAlpha.getString(features, info.mapTheme, info.snowCovered);
            }
        }

        parentWorldGui.drawString(parentWorldGui.fontRenderer, str, par2 + 2, par3 + 32, 0x808080);
    }
}
