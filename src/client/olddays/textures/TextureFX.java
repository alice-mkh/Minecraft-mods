package net.minecraft.src;

import java.util.ArrayList;
import net.minecraft.client.Minecraft;

public abstract class TextureFX extends TextureStitched{
    protected byte[] imageData;
    protected boolean anaglyphEnabled;
    protected int tileSize;
    private Texture tmp;

    public TextureFX(String str){
        super(str);
        imageData = new byte[1024];
        anaglyphEnabled = false;
        tileSize = 1;
    }

    public abstract void onTick();

    @Override
    public void updateAnimation(){
        if (textureSheet == null){
            return;
        }
        if (tmp == null){
            tmp = new Texture(getIconName(), 2, 16 * tileSize, 16 * tileSize, 10496, 6408, 9728, 9728, 0, null);
        }
        anaglyphEnabled = Minecraft.getMinecraft().gameSettings.anaglyph;
        onTick();
        tmp.getTextureData().clear();
        if (tileSize == 1){
            tmp.getTextureData().put(imageData);
        }else{
            for (int i = 0; i < tileSize; i++){
                for (int j = 0; j < 16; j++){
                    for (int k = 0; k < tileSize; k++){
                        tmp.getTextureData().put(imageData, 16 * 4 * j, 16 * 4);
                    }
                }
            }
        }
        tmp.getTextureData().position(0).limit(imageData.length * tileSize * tileSize);
        textureSheet.copyFrom(originX, originY, tmp, rotated);
    }
}