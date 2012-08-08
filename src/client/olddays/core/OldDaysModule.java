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
            getPropertyById(id).noSounds = true;
        }
    }

    public void addMusic(int id, String name){
        File sound = new File(mod_OldDays.getMinecraftInstance().mcDataDir, "resources/music/"+name+".ogg");
        if (sound.exists()){
            mod_OldDays.getMinecraftInstance().installResource("music/"+sound.getName(), sound);
        }else{
            getPropertyById(id).noSounds = true;
        }
    }

    public void addRecipe(ItemStack stack, Object[] obj){
        CraftingManager.getInstance().addRecipe(stack, obj);
    }

    public void addShapelessRecipe(ItemStack stack, Object[] obj){
        CraftingManager.getInstance().addShapelessRecipe(stack, obj);
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

    public static void reload(){
        try{
            mod_OldDays.getMinecraftInstance().renderGlobal.loadRenderers();
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

    public void set(Class c, String name, Object value){
        try{
            c.getDeclaredField(name).set(null, value);
        }catch(Exception ex){
            getPropertyById(last).disable();
        }
    }

    public void addRenderer(Class c, Render r){
        RenderManager renderMan = RenderManager.instance;
        try{
            r.setRenderManager(renderMan);
            HashMap map = ((HashMap)mod_OldDays.getField(net.minecraft.src.RenderManager.class, renderMan, 0));
            map.put(c, r);
            renderersAdded = true;
            System.out.println("OldDays: Added "+r.getClass().getName()+" renderer");
        }catch(Exception ex){
            System.out.println("OldDays: Failed to add renderer: "+ex);
        }
    }

    public static void setInWorldInfo(String var, Object b){
        World world = mod_OldDays.getMinecraftInstance().field_71441_e;
        if (world==null){
            return;
        }
        WorldInfo info = world.getWorldInfo();
        try{
            info.getClass().getDeclaredField(var).set(info, b);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, int override){
        return false;
    }

    public void registerKey(KeyBinding key){
        core.registerKey(key);
    }

    public void catchKeyEvent(KeyBinding keybinding){}

    public boolean onTick(){
        return true;
    }

    public boolean onGUITick(GuiScreen gui){
        return true;
    }

    public void onLoadingSP(String par1Str, String par2Str){}
}