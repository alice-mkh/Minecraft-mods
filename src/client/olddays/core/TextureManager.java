package net.minecraft.src;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import java.io.File;
import java.util.ArrayList;
import java.util.zip.*;

public class TextureManager{
    private RenderEngine renderEngine;
    protected ArrayList<TextureSpriteFX> textureHooks;
    public boolean fallbacktex;
    private String currentpack;

    public TextureManager(){
        renderEngine = mod_OldDays.getMinecraft().renderEngine;
        textureHooks = new ArrayList<TextureSpriteFX>();
        fallbacktex = true;
    }

    public void setTextureHook(String name, int i2, String name2, int index, boolean b){
        TextureSpriteFX fx2 = null;
        for (int i = 0; i < textureHooks.size(); i++){
            TextureSpriteFX fx = textureHooks.get(i);
            if (fx.sprite2 == name && fx.index2 == i2){
                fx2 = fx;
                break;
            }
        }
        if (fx2 == null){
            addTextureHook(name, i2, name2, index);
            for (int i = 0; i < textureHooks.size(); i++){
                TextureSpriteFX fx = textureHooks.get(i);
                if (fx.sprite2 == name && fx.index2 == i2){
                    fx2 = fx;
                    break;
                }
            }
        }
        fx2.sprite = name2;
        fx2.changeIndex(index, b, false);
        renderEngine.updateDynamicTextures();
    }

    public void setTextureHook(String origname, String newname, boolean b){
        try{
            TexturePackList packList = mod_OldDays.getMinecraft().texturePackList;
            TexturePackBase texpack = ((TexturePackBase)mod_OldDays.getField(net.minecraft.src.TexturePackList.class, packList, 6));
            BufferedImage image = ImageIO.read(texpack.getResourceAsStream(b ? newname : origname));
            renderEngine.setupTexture(image, renderEngine.getTexture(origname));
        }catch(Exception ex){
            ex.printStackTrace();
            setFallback(true);
        }
    }
 
    public void onTick(){
        if (currentpack==null || currentpack!=mod_OldDays.getMinecraft().gameSettings.skin){
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, mod_OldDays.getMinecraft().renderEngine.getTexture("/terrain.png"));
            TextureSpriteFX.w = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) / 16;
            currentpack=mod_OldDays.getMinecraft().gameSettings.skin;
            setFallback(!hasEntry("olddays"));
        }
    }

    private void setFallback(boolean b){
        fallbacktex = b;
        for (int i = 0; i < mod_OldDays.modules.size(); i++){
            OldDaysModule module = mod_OldDays.modules.get(i);
            for (int j = 1; j <= module.properties.size(); j++){
                if (!module.getPropertyById(j).shouldRefreshOnFallback()){
                    mod_OldDays.sendCallback(module.id, j);
                }
            }
            module.onFallbackChange(b);
        }
        for (int i = 0; i < textureHooks.size(); i++){
            textureHooks.get(i).refresh(false);
        }
    }

    public boolean hasEntry(String str){
        try{
            TexturePackList packList = mod_OldDays.getMinecraft().texturePackList;
            TexturePackBase texpack = ((TexturePackBase)mod_OldDays.getField(net.minecraft.src.TexturePackList.class, packList, 6));
            if (texpack instanceof TexturePackDefault){
                return true;
            }
            if (texpack instanceof TexturePackFolder){
                File orig = ((File)mod_OldDays.getField(net.minecraft.src.TexturePackFolder.class, texpack, 2));
                File file = new File(orig, str);
                return file.exists();
            }else{
                ZipFile file = ((ZipFile)mod_OldDays.getField(net.minecraft.src.TexturePackCustom.class, texpack, 0));
                return file.getEntry(str)!=null;
            }
        }catch(Exception ex){
            ex.printStackTrace();
            return true;
        }
    }

    public void addTextureHook(String origname, int origi, String newname, int newi){
        addTextureHook(origname, origi, newname, newi, 16, 16);
    }

    public void addTextureHook(String origname, int origi, String newname, int newi, int w, int h){
        RenderEngine renderEngine = mod_OldDays.getMinecraft().renderEngine;
        TextureSpriteFX fx = new TextureSpriteFX(origname, newname, w, h, origi, newi);
        renderEngine.registerTextureFX(fx);
        textureHooks.add(fx);
    }
}