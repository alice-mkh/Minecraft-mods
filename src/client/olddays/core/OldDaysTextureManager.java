package net.minecraft.src;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.*;

public class OldDaysTextureManager{
    private RenderEngine renderEngine;
//    protected ArrayList<TextureSpriteFX> textureHooks;
    protected ArrayList<TextureHook> textureHooks2;
    protected ArrayList<TextureStitched> textureFXList;
    public ArrayList<TextureStitched> texturesToRedraw;
    private String currentpack;
    private HashMap<String, Boolean> entryCache;

    public OldDaysTextureManager(){
        renderEngine = mod_OldDays.getMinecraft().renderEngine;
//        textureHooks = new ArrayList<TextureSpriteFX>();
        textureHooks2 = new ArrayList<TextureHook>();
        textureFXList = new ArrayList<TextureStitched>();
        texturesToRedraw = new ArrayList<TextureStitched>();
        entryCache = new HashMap<String, Boolean>();
    }

    public void setTextureHook(String name, int i2, String name2, int index, boolean b){
/*        TextureSpriteFX fx2 = null;
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
        fx2.onTick2();
        try{
            renderEngine.updateDynamicTextures();
        }catch(Exception ex){}*/
    }

    public void setTextureHook(String origname, String newname, boolean b){
        for (int i = 0; i < textureHooks2.size(); i++){
            TextureHook hook = textureHooks2.get(i);
            if (hook.origname.equals(origname) && hook.newname.equals(newname)){
                hook.enabled = b;
                refreshTextureHooks();
                return;
            }
        }
        textureHooks2.add(new TextureHook(origname, newname, b));
        refreshTextureHooks();
    }
 
    public void onTick(){
        if (currentpack==null || currentpack!=mod_OldDays.getMinecraft().gameSettings.skin){
//             renderEngine.bindTexture("/terrain.png");
//             TextureSpriteFX.w = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH) / 16;
            currentpack=mod_OldDays.getMinecraft().gameSettings.skin;
            entryCache.clear();
            setFallback(!hasEntry("olddays"));
        }
    }

    public void refreshTextureHooks(){
        for (TextureHook hook : textureHooks2){
            try{
                TexturePackList packList = mod_OldDays.getMinecraft().texturePackList;
                ITexturePack texpack = ((ITexturePack)mod_OldDays.getField(TexturePackList.class, packList, 6));
                BufferedImage image = ImageIO.read(texpack.getResourceAsStream(hook.enabled ? hook.newname : hook.origname));
                int id = 0;
                try{
                    Method m = null;
                    Method[] methods = (RenderEngine.class).getDeclaredMethods();
                    for (int i = 0; i < methods.length; i++){
                        if (methods[i].toGenericString().matches("^private int (net.minecraft.src.)?[a-zA-Z]{1,12}.[a-zA-Z]{1,10}.java.lang.String.$")){
                            m = methods[i];
                            break;
                        }
                    }
                    if (m == null){
                        return;
                    }
                    m.setAccessible(true);
                    id = (Integer)(m.invoke(renderEngine, hook.origname));
                }catch(Exception ex){
                    ex.printStackTrace();
                }
                renderEngine.setupTexture(image, id);
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void setFallback(boolean b){
        for (int i = 0; i < mod_OldDays.modules.size(); i++){
            OldDaysModule module = mod_OldDays.modules.get(i);
            for (int j = 1; j <= module.properties.size(); j++){
                if (module.getPropertyById(j).shouldRefreshOnFallback()){
                    mod_OldDays.sendCallback(module.id, j);
                }
            }
            module.onFallbackChange(b);
        }
/*        for (int i = 0; i < textureHooks.size(); i++){
            textureHooks.get(i).refresh(false);
            textureHooks.get(i).onTick2();
        }*/
    }

    public boolean hasEntry(String... str){
        try{
            TexturePackList packList = mod_OldDays.getMinecraft().texturePackList;
            ITexturePack texpack = ((ITexturePack)mod_OldDays.getField(TexturePackList.class, packList, 6));
            if (texpack instanceof TexturePackDefault){
                return true;
            }
            for (int i = 0; i < str.length; i++){
                if (entryCache.containsKey(str[i])){
                    if (!entryCache.get(str[i])){
                        return false;
                    }
                }else if (texpack instanceof TexturePackFolder){
                    File orig = ((File)mod_OldDays.getField(TexturePackImplementation.class, texpack, 2));
                    File file = new File(orig, str[i]);
                    boolean b = file.exists();
                    entryCache.put(str[i], b);
                    if (!b){
                        return false;
                    }
                }else{
                    ZipFile file = ((ZipFile)mod_OldDays.getField(TexturePackCustom.class, texpack, 0));
                    boolean b = file.getEntry(str[i]) != null;
                    entryCache.put(str[i], b);
                    if (!b){
                        return false;
                    }
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return true;
    }

    public boolean hasIcons(boolean items, String... str){
        for (String s : str){
            s = "/textures/" + (items ? "items" : "terrain") + "/" + s + ".png";
        }
        return hasEntry(str);
    }

    public void addTextureHook(String origname, int origi, String newname, int newi){
        addTextureHook(origname, origi, newname, newi, 16, 16);
    }

    public void addTextureHook(String origname, int origi, String newname, int newi, int w, int h){
/*        RenderEngine renderEngine = mod_OldDays.getMinecraft().renderEngine;
        TextureSpriteFX fx = new TextureSpriteFX(origname, newname, w, h, origi, newi);
        renderEngine.registerTextureFX(fx);
        textureHooks.add(fx);*/
    }

    public Icon registerCustomIcon(IconRegister map, String par1Str, TextureStitched icon, Icon from)
    {
        if (par1Str == null || !(map instanceof TextureMap)){
            return icon;
        }
        Map textureStitchedMap = (Map)(mod_OldDays.getField(TextureMap.class, (TextureMap)map, 9));
        if (textureStitchedMap == null){
            return icon;
        }
        TextureStitched texturestitched = (TextureStitched)textureStitchedMap.get(par1Str);
        if (texturestitched == null || texturestitched != icon)
        {
            textureStitchedMap.put(par1Str, icon);
        }
        for (TextureStitched fx : textureFXList){
            if (fx.getIconName().equals(par1Str)){
                textureFXList.remove(fx);
                break;
            }
        }
        if (icon instanceof TextureFX){
            textureFXList.add(icon);
        }
        if (from != null){
            try{
                icon.copyFrom((TextureStitched)from);
            }catch(Exception e){}
        }
        return icon;
    }

    public Icon registerCustomIcon(IconRegister map, String par1Str, TextureStitched icon){
        return registerCustomIcon(map, par1Str, icon, null);
    }

    public void updateTextureFXes(){
        for (TextureStitched fx : textureFXList){
            fx.updateAnimation();
        }
        for (TextureStitched tex : texturesToRedraw){
            tex.updateAnimation();
        }
        texturesToRedraw.clear();
    }

    public void removeTextureFXes(){
        renderEngine.refreshTextures();
        textureFXList.clear();
        renderEngine.updateDynamicTextures();
    }

    private class TextureHook{
        private String origname;
        private String newname;
        private boolean enabled;

        public TextureHook(String str1, String str2, boolean b){
            origname = str1;
            newname = str2;
            enabled = b;
        }
    }
}