package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class TextureFX extends TextureAtlasSprite{
    protected byte[] imageData;
    protected int[] imageDataInts;
    protected boolean anaglyphEnabled;

    public TextureFX(String str, boolean items){
        super("textures/" + (items ? "items" : "blocks") + "/" + str + ".png");
        anaglyphEnabled = false;
        imageData = new byte[1024];
    }

    public TextureFX(String str){
        this(str, false);
    }

    public abstract void onTick();

    @Override
    public void updateAnimation(){
        anaglyphEnabled = Minecraft.getMinecraft().gameSettings.anaglyph;
        onTick();
        int[] data = new int[256];
        if (imageDataInts != null){
            data = imageDataInts;
        }else{
            for (int i = 0; i < data.length; i++){
                int r = imageData[i * 4];
                int g = imageData[i * 4 + 1];
                int b = imageData[i * 4 + 2];
                int a = imageData[i * 4 + 3];
                data[i] = a << 24 | r << 16 | g << 8 | (b + 256);
            }
        }
        for (int i = 0; i <  getOriginX() / 16; i++){
            for (int j = 0; j < getOriginY() / 16; j++){
                TextureUtil.func_110998_a(data, 16, 16, func_130010_a() + 16 * i, func_110967_i() + 16 * j, false, false);
            }
        }
    }

    protected BufferedImage getImage(String str) throws IOException{
        ResourceLocation res = new ResourceLocation(str);
        InputStream stream = Minecraft.getMinecraft().func_110442_L().func_110536_a(res).func_110527_b();
        return ImageIO.read(stream);
    }
}