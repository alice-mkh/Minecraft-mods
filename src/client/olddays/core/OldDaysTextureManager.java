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
import java.util.zip.ZipFile;

public class OldDaysTextureManager{
    private RenderEngine renderEngine;
    protected ArrayList<TextureHook> textureHooks;
    protected ArrayList<TextureStitched> textureFXList;
    private String currentpack;
    private HashMap<String, Boolean> entryCache;

    public OldDaysTextureManager(){
        renderEngine = mod_OldDays.getMinecraft().renderEngine;
        textureHooks = new ArrayList<TextureHook>();
        textureFXList = new ArrayList<TextureStitched>();
        entryCache = new HashMap<String, Boolean>();
    }

    public void setTextureHook(String origname, String newname, boolean b){
        for (int i = 0; i < textureHooks.size(); i++){
            TextureHook hook = textureHooks.get(i);
            if (hook.origname.equals(origname) && hook.newname.equals(newname)){
                hook.enabled = b;
                refreshTextureHooks();
                return;
            }
        }
        textureHooks.add(new TextureHook(origname, newname, b));
        refreshTextureHooks();
    }
 
    public void onTick(){
        if (currentpack==null || currentpack!=mod_OldDays.getMinecraft().gameSettings.skin){
            currentpack=mod_OldDays.getMinecraft().gameSettings.skin;
            entryCache.clear();
            setFallback(!hasEntry("olddays"));
        }
    }

    public void refreshTextureHooks(){
        for (TextureHook hook : textureHooks){
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

    public Icon registerCustomIcon(IconRegister map, String par1Str, TextureStitched icon, Icon from){
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
        if (icon instanceof TextureFX){
            for (TextureStitched fx : textureFXList){
                if (fx.getIconName().equals(par1Str)){
                    textureFXList.remove(fx);
                    break;
                }
            }
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
            try{
                fx.updateAnimation();
            }catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public void removeTextureFXes(){
        renderEngine.refreshTextures();
        textureFXList.clear();
        renderEngine.updateDynamicTextures();
    }

    public void eraseIcon(Icon icon, String origIcon, boolean b){
        if (icon == null){
            return;
        }
        if (b){
            replaceIcon(icon, "", 0, 0, origIcon, false);
            return;
        }
        Texture sheet = (Texture)(mod_OldDays.getField(TextureStitched.class, icon, 1));
        boolean rot = (Boolean)(mod_OldDays.getField(TextureStitched.class, icon, 4));
        int width = (Integer)(mod_OldDays.getField(TextureStitched.class, icon, 7));
        int height = (Integer)(mod_OldDays.getField(TextureStitched.class, icon, 8));
        Texture tex = new Texture("", 2, width, height, 10496, GL11.GL_RGBA, 9728, 9728, 0, null);
        sheet.copyFrom(icon.getOriginX(), icon.getOriginY(), tex, rot);
    }

    public void replaceIcon(Icon icon, String newIcon, int x, int y, String origIcon, boolean b){
        if (icon == null){
            return;
        }
        b = b && hasEntry(newIcon);
        if (!b){
            x = 0;
            y = 0;
            newIcon = origIcon;
        }
        Texture sheet = (Texture)(mod_OldDays.getField(TextureStitched.class, icon, 1));
        boolean rot = (Boolean)(mod_OldDays.getField(TextureStitched.class, icon, 4));
        int width = (Integer)(mod_OldDays.getField(TextureStitched.class, icon, 7));
        int height = (Integer)(mod_OldDays.getField(TextureStitched.class, icon, 8));
        int[] ints = new int[width * height];
        try{
            ImageIO.read(getClass().getResource(newIcon)).getRGB(x * width, y * height, width, height, ints, 0, width);
        }catch(Exception e){
            e.printStackTrace();
        }
        Texture tex = new Texture(newIcon, 2, width, height, 10496, GL11.GL_RGBA, 9728, 9728, 0, null);
        byte[] bytes = new byte[ints.length * 4];
        for (int i = 0; i < ints.length; i++){
            int color = ints[i];
            bytes[i * 4 + 0] = (byte)(color >> 16 & 0xFF);
            bytes[i * 4 + 1] = (byte)(color >> 8 & 0xFF);
            bytes[i * 4 + 2] = (byte)(color & 0xFF);
            bytes[i * 4 + 3] = (byte)(color >>> 24 & 0xFF);
        }
        tex.getTextureData().position(0).limit(bytes.length);
        tex.getTextureData().put(bytes);
        tex.getTextureData().clear();
        sheet.copyFrom(icon.getOriginX(), icon.getOriginY(), tex, rot);
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