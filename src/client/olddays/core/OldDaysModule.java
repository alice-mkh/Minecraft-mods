package net.minecraft.src;

import net.minecraft.client.Minecraft;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OldDaysModule{
    public int id;
    public String name;
    public List properties;
    protected mod_OldDays core;
    protected Minecraft minecraft;
    public int last;
    public boolean renderersAdded;

    public OldDaysModule(mod_OldDays c, int i, String s){
        core = c;
        id = i;
        name = s;
        properties = new ArrayList();
        minecraft = mod_OldDays.getMinecraftInstance();
        last = 0;
        renderersAdded = false;
    }

    public OldDaysProperty getPropertyById(int id){
        for (int i = 0; i < properties.size(); i++){
            OldDaysProperty prop = ((OldDaysProperty)properties.get(i));
            if (prop.id == id){
                return prop;
            }
        }
        return null;
    }

    public void addSound(int id, String name){
        File sound = new File(mod_OldDays.getMinecraftInstance().mcDataDir, "resources/newsound/olddays/"+name+".ogg");
        if (sound.exists()){
            mod_OldDays.getMinecraftInstance().installResource("newsound/olddays/"+sound.getName(), sound);
        }else{
            getPropertyById(id).disable();
        }
    }

    public void addMusic(int id, String name){
        File sound = new File(mod_OldDays.getMinecraftInstance().mcDataDir, "resources/music/"+name+".ogg");
        if (sound.exists()){
            mod_OldDays.getMinecraftInstance().installResource("music/"+sound.getName(), sound);
        }else{
            getPropertyById(id).disable();
        }
    }

    public void removeRecipe(String str1){
        try{
            List list = CraftingManager.getInstance().getRecipeList();
            for (int i = 0; i < list.size(); i++){
                String match = ((IRecipe)list.get(i)).getRecipeOutput().toString();
                if (match.equals(str1)){
                    list.remove(i);
                }
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public void reload(){
        try{
            minecraft.renderGlobal.loadRenderers();
        }catch(Exception ex){}
    }

    public static void addTextureHook(String origname, int origi, String newname, int newi){
        mod_OldDays.texman.addTextureHook(origname, origi, newname, newi);
    }

    public static void addTextureHook(String origname, int origi, String newname, int newi, int w, int h){
        mod_OldDays.texman.addTextureHook(origname, origi, newname, newi, w, h);
    }

    public static void setTextureHook(String origname, String newname, boolean b){
        mod_OldDays.texman.setTextureHook(origname, newname, b);
    }

    public static void setTextureHook(String name, int i2, String name2, int index, boolean b){
        mod_OldDays.texman.setTextureHook(name, i2, name2, index, b);
    }

    public static boolean getFallback(){
        return mod_OldDays.texman.fallbacktex;
    }

    public void callback(int i){}

    public void addProperty(OldDaysProperty prop){
        properties.add(prop);
    }

    public void addProperty(int num, String name, boolean value, String fname, String desc){
        OldDaysProperty prop = new OldDaysPropertyBool(this, num, name, value, fname);
        prop.description = desc;
        properties.add(prop);
    }

    public void addProperty(int num, String name, boolean smp, boolean value, String fname, String desc){
        OldDaysProperty prop = new OldDaysPropertyBool(this, num, name, value, smp, fname);
        prop.description = desc;
        properties.add(prop);
    }

    public void addProperty(int num, String name, int value, String fname, String desc, int count){
        OldDaysProperty prop = new OldDaysPropertyInt(this, num, name, value, count, fname);
        prop.description = desc;
        properties.add(prop);
    }

    public void addProperty(int num, String name, int smp, int value, String fname, String desc, int count){
        OldDaysProperty prop = new OldDaysPropertyInt(this, num, name, value, count, smp, fname);
        prop.description = desc;
        properties.add(prop);
    }

    public void addProperty(int num, String name, int value, String fname, String desc, String[] names){
        int count = names.length;
        OldDaysProperty prop = new OldDaysPropertyInt(this, num, name, value, count, fname);
        ((OldDaysPropertyInt)prop).setNames(names);
        prop.description = desc;
        properties.add(prop);
    }

    public void addProperty(int num, String name, int smp, int value, String fname, String desc, String[] names){
        int count = names.length;
        OldDaysProperty prop = new OldDaysPropertyInt(this, num, name, value, count, smp, fname);
        ((OldDaysPropertyInt)prop).setNames(names);
        prop.description = desc;
        properties.add(prop);
    }

    public void addProperty(int num, String name, String value, String fname, String desc){
        OldDaysProperty prop = new OldDaysPropertyString(this, num, name, value, fname);
        prop.description = desc;
        properties.add(prop);
    }

    public void addProperty(int num, String name, String smp, String value, String fname, String desc){
        OldDaysProperty prop = new OldDaysPropertyString(this, num, name, value, fname, smp);
        prop.description = desc;
        properties.add(prop);
    }

    public void setBool(Class c, String name, boolean value){
        try{
            c.getDeclaredField(name).setBoolean(null, value);
        }catch(Exception ex){
            getPropertyById(last).disable();
        }
    }

    public void setInt(Class c, String name, int value){
        try{
            c.getDeclaredField(name).setInt(null, value);
        }catch(Exception ex){
            getPropertyById(last).disable();
        }
    }
    public void setStr(Class c, String name, String value){
        try{
            c.getDeclaredField(name).set(null, ((Object)value));
        }catch(Exception ex){
            getPropertyById(last).disable();
        }
    }

    public void addRenderer(Class c, Render r){
        RenderManager renderMan = RenderManager.instance;
        try{
            r.setRenderManager(renderMan);
            HashMap map = ((HashMap)ModLoader.getPrivateValue(net.minecraft.src.RenderManager.class, renderMan, 0));
            map.put(c, r);
            renderersAdded = true;
            System.out.println("OldDays: Added renderer");
        }catch(Exception ex){
            System.out.println("OldDays: Failed to add renderer: "+ex);
        }
    }

    public void keyboardEvent(KeyBinding keybinding){}
}