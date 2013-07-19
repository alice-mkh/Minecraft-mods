package net.minecraft.src;

import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import java.io.File;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipFile;

public class OldDaysTextureManager{
    private mod_OldDays core;
    private TextureManager renderEngine;
    protected ArrayList<TextureHook> textureHooks;
//     protected ArrayList<TextureAtlasSprite> textureFXList;
    private String currentpack;
    private HashMap<String, Boolean> entryCache;

    public OldDaysTextureManager(mod_OldDays olddays){
        core = olddays;
        renderEngine = mod_OldDays.getMinecraft().func_110434_K();
        textureHooks = new ArrayList<TextureHook>();
//         textureFXList = new ArrayList<TextureAtlasSprite>();
        entryCache = new HashMap<String, Boolean>();
    }

    public void setTextureHook(String origname, String newname, boolean b){
        for (int i = 0; i < textureHooks.size(); i++){
            TextureHook hook = textureHooks.get(i);
            if (hook.equals(origname, newname)){
                hook.enabled = b;
                refreshTextureHooks();
                return;
            }
        }
        textureHooks.add(new TextureHook(origname, newname, b));
        refreshTextureHooks();
    }

    public void changeResourcePack(ResourceManager res){
        currentpack=mod_OldDays.getMinecraft().gameSettings.skin;
        entryCache.clear();
        core.refreshTextures();
        setFallback(!hasEntry("olddays"));
    }

    public void refreshTextureHooks(){
        for (TextureHook hook : textureHooks){
            TextureObject tex = new SimpleTexture(hook.enabled ? hook.newname : hook.origname);
            renderEngine.func_110579_a(hook.origname, tex);
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
        ResourcePackRepository repo = mod_OldDays.getMinecraft().func_110438_M();
        List list = repo.func_110613_c();
        if (list.size() == 0){
            return true;
        }
        for (Object o : list){
            ResourcePack pack = ((ResourcePackRepositoryEntry)o).func_110514_c();
            for (String s : str){
                if (entryCache.containsKey(s)){
                    if (!entryCache.get(s)){
                        return false;
                    }
                    continue;
                }
                boolean b = pack.func_110589_b(new ResourceLocation(s));
                entryCache.put(s, b);
                if (!b){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasIcons(boolean items, String... str){
        for (int i = 0; i < str.length; i++){
            str[i] = "textures/" + (items ? "items" : "blocks") + "/" + str[i] + ".png";
        }
        return hasEntry(str);
    }

    public Icon registerCustomIcon(IconRegister map, String par1Str, TextureAtlasSprite icon, Icon from){
        if (par1Str == null || !(map instanceof TextureMap)){
            return icon;
        }
        Map textureStitchedMap = (Map)(mod_OldDays.getField(TextureMap.class, (TextureMap)map, 4));
        if (textureStitchedMap == null){
            return icon;
        }
/*        TextureAtlasSprite texturestitched = (TextureAtlasSprite)textureStitchedMap.get(par1Str);
        if (texturestitched == null || texturestitched != icon)
        {
            textureStitchedMap.put(par1Str, icon);
        }
        if (icon instanceof TextureFX){
            for (TextureAtlasSprite fx : textureFXList){
                if (fx.getIconName().equals(par1Str)){
                    textureFXList.remove(fx);
                    break;
                }
            }
            textureFXList.add(icon);
        }
        if (from != null){
            try{
                icon.copyFrom((TextureAtlasSprite)from);
            }catch(Exception e){}
        }*/
        return icon;
    }

    public Icon registerCustomIcon(IconRegister map, String par1Str, TextureAtlasSprite icon){
        return registerCustomIcon(map, par1Str, icon, null);
    }

    public void updateTextureFXes(){
/*        for (TextureAtlasSprite fx : textureFXList){
            try{
                fx.updateAnimation();
            }catch(Exception e){
                e.printStackTrace();
            }
        }*/
    }

    public void removeTextureFXes(){
/*        renderEngine.refreshTextures();
        textureFXList.clear();
        System.gc();
        renderEngine.func_110550_d();*/
    }

    public void eraseIcon(Icon icon, String origIcon, boolean b){
        if (icon == null || !(icon instanceof TextureAtlasSprite)){
            return;
        }
        if (b){
            replaceIcon(icon, "", 0, 0, origIcon, false);
            return;
        }
        int width = icon.getOriginX();
        int height = icon.getOriginY();
        int x = ((TextureAtlasSprite)icon).func_130010_a();
        int y = ((TextureAtlasSprite)icon).func_110967_i();
        int[] ints = new int[width * height];
        boolean terrain = origIcon.split("/")[1].equals("blocks");
        renderEngine.func_110577_a(terrain ? TextureMap.field_110575_b : TextureMap.field_110576_c);
        TextureUtil.func_110998_a(ints, width, height, x, y, false, false);
    }

    public void replaceIcon(Icon icon, String newIcon, int x, int y, String origIcon, boolean b){
        if (icon == null || !(icon instanceof TextureAtlasSprite)){
            return;
        }
        b = b && newIcon.length() > 0 && hasEntry(newIcon);
        if (!b){
            x = 0;
            y = 0;
            newIcon = origIcon;
        }
        int width = icon.getOriginX();
        int height = icon.getOriginY();
        int[] ints = new int[width * height];
        try{
            ResourceLocation res = new ResourceLocation(newIcon);
            ImageIO.read(Minecraft.getMinecraft().func_110442_L().func_110536_a(res).func_110527_b()).getRGB(x * width, y * height, width, height, ints, 0, width);
        }catch(Exception e){
            e.printStackTrace();
        }
        x = ((TextureAtlasSprite)icon).func_130010_a();
        y = ((TextureAtlasSprite)icon).func_110967_i();
        boolean terrain = origIcon.split("/")[1].equals("blocks");
        renderEngine.func_110577_a(terrain ? TextureMap.field_110575_b : TextureMap.field_110576_c);
        TextureUtil.func_110998_a(ints, width, height, x, y, false, false);
    }

    private class TextureHook{
        private ResourceLocation origname;
        private ResourceLocation newname;
        private boolean enabled;

        public TextureHook(String str1, String str2, boolean b){
            origname = new ResourceLocation(str1);
            newname = new ResourceLocation(str2);
            enabled = b;
        }

        public boolean equals(String str1, String str2){
            return origname.func_110623_a().equals(str1) && newname.func_110623_a().equals(str2);
        }
    }
}