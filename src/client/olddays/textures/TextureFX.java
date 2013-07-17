package net.minecraft.src;

import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public abstract class TextureFX extends TextureAtlasSprite{
    protected byte[] imageData;
    protected boolean anaglyphEnabled;
//     private Texture tmp;

    public TextureFX(String str){
        super(str);
        anaglyphEnabled = false;
        imageData = new byte[1024];
    }

    public abstract void onTick();

    @Override
    public void updateAnimation(){
//         if (tmp == null){
//             tmp = new Texture(getIconName(), 2, 16, 16, 10496, 6408, 9728, 9728, 0, null);
//         }
        anaglyphEnabled = Minecraft.getMinecraft().gameSettings.anaglyph;
        onTick();
        int[] data = new int[getOriginX() * getOriginY()];
        for (int i = 0; i < data.length; i++){
            data[i] = imageData[i << 2] << 24 | imageData[i << 2 + 1] << 16 | imageData[i << 2 + 2] << 8 | imageData[i << 2 + 3];
        }
//         tmp.getTextureData().put(imageData);
        for (int i = 0; i <  getOriginX() / 16; i++){
            for (int j = 0; j < getOriginY() / 16; j++){
//                 tmp.getTextureData().position(0);
//                 textureSheet.func_104062_b(originX + 16 * i, originY + 16 * j, tmp);
                TextureUtil.func_110998_a(data, getOriginX(), getOriginY(), func_130010_a(), func_110967_i(), false, false);
            }
        }
    }

    protected BufferedImage getImage(String str) throws IOException{
        ResourceLocation res = new ResourceLocation(str);
        InputStream stream = Minecraft.getMinecraft().func_110442_L().func_110536_a(res).func_110527_b();
        return ImageIO.read(stream);
    }
}