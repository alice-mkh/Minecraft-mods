package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class TextureFX extends TextureAtlasSprite{
    protected int[] imageData;
    protected boolean anaglyphEnabled;
    protected int size;
    private int prevSize;
    protected int tile;

    public TextureFX(String str, boolean items){
        super("textures/" + (items ? "items" : "blocks") + "/" + str);
        anaglyphEnabled = false;
        prevSize = 0;
        tile = 1;
    }

    public TextureFX(String str){
        this(str, false);
    }

    public abstract void onTick();

    protected void updateSize(){
        imageData = new int[size * size];
    }

    @Override
    public void updateAnimation(){
        anaglyphEnabled = Minecraft.getMinecraft().gameSettings.anaglyph;
        size = getIconWidth() / tile;
        if (prevSize != size){
            updateSize();
        }
        prevSize = size;
        onTick();
        for (int i = 0; i < tile; i++){
            for (int j = 0; j < tile; j++){
                TextureUtil.uploadTextureSub(imageData, size, size, getOriginX() + size * i, getOriginY() + size * j, false, false);
            }
        }
    }

    protected int[] getImageData(String str, int size){
        try{
            ResourceLocation res = new ResourceLocation(str);
            InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(res).getInputStream();
            BufferedImage image = ImageIO.read(stream);
            stream.close();
            int width = image.getWidth();
            int height = image.getHeight();
            int[] data = new int[width * height];
            image.getRGB(0, 0, width, height, data, 0, width);
            if (width != size || height != size){
                return resize(data, width, height, size, size);
            }
            return data;
        }catch(Exception e){
            e.printStackTrace();
        }
        return new int[size * size];
    }

    private int[] resize(int[] data, int width, int height, int newWidth, int newHeight){
        int[] newData = new int[newWidth * newHeight];
        for (int i = 0; i < newWidth; i++){
            for (int j = 0; j < newHeight; j++){
                int i2 = (int)((float)i / (float)newWidth * (float)width);
                int j2 = (int)((float)j / (float)newHeight * (float)height);
                newData[j * newHeight + i] = data[j2 * height + i2];
            }
        }
        return newData;
    }
}