package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import javax.imageio.ImageIO;

public abstract class TextureFX extends TextureStitched{
    protected byte[] imageData;
    protected boolean anaglyphEnabled;
    private ByteBuffer textureData;

    public TextureFX(String str){
        super(str);
        imageData = new byte[1024];
        anaglyphEnabled = false;
    }


    public abstract void onTick();

    @Override
    public void updateAnimation(){
        onTick();
        if (textureData == null){
            textureData = ((Texture)textureList.get(0)).getTextureData();
        }
        if  (textureData == null || imageData == null){
            return;
        }
        textureData.clear();
        textureData.put(imageData);
        textureData.position(0).limit(1024);
        textureSheet.copyFrom(originX, originY, (Texture)textureList.get(0), rotated);
    }
}