package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;
import net.minecraft.client.Minecraft;

public abstract class TextureFX extends TextureStitched{
    protected byte[] imageData;
    protected boolean anaglyphEnabled;
    protected int tileSize;
    private ByteBuffer textureData;

    public TextureFX(String str){
        super(str);
        imageData = new byte[1024];
        anaglyphEnabled = false;
        tileSize = 1;
    }


    public abstract void onTick();

    @Override
    public void updateAnimation(){
        if (textureList == null || textureList.size() <= 0){
            return;
        }
        if (textureData == null){
            textureData = ((Texture)textureList.get(0)).getTextureData();
        }
        if (textureData == null){
            return;
        }
        anaglyphEnabled = Minecraft.getMinecraft().gameSettings.anaglyph;
        onTick();
        textureData.clear();
        if (tileSize == 1){
            textureData.put(imageData);
        }else{
            for (int i = 0; i < tileSize; i++){
                for (int j = 0; j < 16; j++){
                    for (int k = 0; k < tileSize; k++){
                        textureData.put(imageData, 16 * 4 * j, 16 * 4);
                    }
                }
            }
        }
        textureData.position(0).limit(imageData.length * tileSize * tileSize);
        textureSheet.copyFrom(originX, originY, (Texture)textureList.get(0), rotated);
    }
}