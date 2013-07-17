package net.minecraft.src;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.src.ssp.EntityPlayerSP2;

public class OldDaysModule implements Comparable<OldDaysModule>{
    public int id;
    public String name;
    public ArrayList<OldDaysProperty> properties;
    protected mod_OldDays core;
    protected Minecraft minecraft;
    public int last;
    public boolean renderersAdded;
    public boolean isLocal;
    public boolean highlight;

    public OldDaysModule(mod_OldDays c, int i, String s){
        core = c;
        id = i;
        name = s;
        properties = new ArrayList<OldDaysProperty>();
        minecraft = mod_OldDays.getMinecraft();
        last = 0;
        renderersAdded = false;
        isLocal = false;
        highlight = false;
    }

    public OldDaysProperty getPropertyById(int id){
        for (int i = 0; i < properties.size(); i++){
            OldDaysProperty prop = properties.get(i);
            if (prop.id == id){
                return prop;
            }
        }
        return null;
    }

    public void addSound(int id, String name){
        File sound = new File(mod_OldDays.getMinecraft().mcDataDir, "assets/sound/olddays/"+name+".ogg");
        core.unpackSound("sound/olddays", name+".ogg");
        if (sound.exists()){
            String s = "olddays/"+name+".ogg";
            mod_OldDays.getMinecraft().sndManager.addSound(s);
        }else{
            getPropertyById(id).noSounds = true;
        }
    }

    public void addMusic(int id, String name){
        File sound = new File(mod_OldDays.getMinecraft().mcDataDir, "assets/music/"+name+".ogg");
        core.unpackSound("music", name+".ogg");
        if (sound.exists()){
            String s = name+".ogg";
            mod_OldDays.getMinecraft().sndManager.addMusic(s);
        }else{
            getPropertyById(id).noSounds = true;
        }
    }

    public void addRecipe(ItemStack stack, Object... obj){
        CraftingManager.getInstance().addRecipe(stack, obj);
    }

    public void addShapelessRecipe(ItemStack stack, Object... obj){
        CraftingManager.getInstance().addShapelessRecipe(stack, obj);
    }

    public void dumpRecipes(String str){
        try{
            List list = CraftingManager.getInstance().getRecipeList();
            for (int i = 0; i < list.size(); i++){
                ItemStack stack = ((IRecipe)list.get(i)).getRecipeOutput();
                if (stack == null){
                    continue;
                }
                String match = stack.toString();
                if (str == null || match.contains(str)){
                    System.out.println("OldDays: Found recipe: "+match);
                }
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void removeRecipe(String str1){
        try{
            int count = 0;
            List list = CraftingManager.getInstance().getRecipeList();
            for (int i = 0; i < list.size(); i++){
                ItemStack stack = ((IRecipe)list.get(i)).getRecipeOutput();
                if (stack == null || Item.itemsList[stack.itemID] == null){
                    continue;
                }
                String match = stack.toString();
//                 System.out.println(match);
                if (match.equals(str1)){
                    list.remove(i);
                    count++;
                }
            }
            if (count <= 0){
//                 System.out.println("OldDays: Invalid recipe identifier: "+str1);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void reload(){
        try{
            mod_OldDays.getMinecraft().renderGlobal.updateAllRenderers(true);
        }catch(Throwable t){
            try{
                mod_OldDays.getMinecraft().renderGlobal.loadRenderers();
            }catch(Exception e){}
        }
    }

    public static void setTextureHook(String origname, String newname, boolean b){
        mod_OldDays.texman.setTextureHook(origname, newname, b);
    }

    public static boolean hasTextures(String... str){
        return mod_OldDays.texman.hasEntry(str);
    }

    public static boolean hasIcons(boolean b, String... str){
        return mod_OldDays.texman.hasIcons(b, str);
    }

    public void replaceBlockIcon(Block bl, String newIcon, int x, int y, boolean b){
        Icon i = null;
        try{
            i = bl.getBlockTextureFromSide(1);
        }catch(NullPointerException e){
            return;
        }
        replaceIcon(i, newIcon, x, y, "textures/blocks/" + bl.getUnlocalizedName().substring(5) + ".png", b);
    }

    public void replaceItemIcon(Item it, String newIcon, int x, int y, boolean b){
        Icon i = null;
        try{
            i = it.getIconFromDamage(0);
        }catch(NullPointerException e){
            return;
        }
        replaceIcon(i, newIcon, x, y, "textures/items/" + it.getUnlocalizedName().substring(5) + ".png", b);
    }

    public void replaceIcon(Icon i, String newIcon, int x, int y, String orig, boolean b){
        mod_OldDays.texman.replaceIcon(i, newIcon, x, y, orig, b);
    }

    public void eraseIcon(Icon i, String orig, boolean b){
        mod_OldDays.texman.eraseIcon(i, orig, b);
    }

    public void callback(int i){}

    public void set(Class c, String name, Object value, boolean necessary){
        if (c == EntityPlayer.class){
            try{
                (EntityPlayerSP2.class).getDeclaredField(name).set(null, value);
            }catch(Exception e){}
        }
        try{
            c.getDeclaredField(name).set(null, value);
        }catch(Exception ex){
            if (necessary){
                if (getPropertyById(last) != null){
                    System.out.println("No \""+name+"\" field at the "+c.getName()+" class");
                    getPropertyById(last).disable();
                }
            }
        }
    }

    public void set(Class c, String name, Object value){
        set(c, name, value, true);
    }

    public void addRenderer(Class c, Render r){
        RenderManager renderMan = RenderManager.instance;
        try{
            r.setRenderManager(renderMan);
            HashMap map = ((HashMap)mod_OldDays.getField(RenderManager.class, renderMan, 0));
            if (map.get(c) != null && map.get(c).getClass() == r.getClass()){
                return;
            }
            map.put(c, r);
            renderersAdded = true;
            System.out.println("OldDays: Added "+r.getClass().getName()+" renderer");
        }catch(Exception ex){
            System.out.println("OldDays: Failed to add renderer: "+ex);
            ex.printStackTrace();
        }
    }

    public void addEntity(Class par0Class, String par1Str, int par2){
        addEntity(par0Class, par1Str, par2, -1, -1);
    }

    public void addEntity(Class par0Class, String par1Str, int par2, int par3, int par4){
        try{
            int id = 1;
            Method m = null;
            Method[] methods = (EntityList.class).getDeclaredMethods();
            for (int i = 0; i < methods.length; i++){
                if (par3 >= 0 && par4 >= 0 && methods[i].toGenericString().matches("^private static void (net.minecraft.src.)?([a-zA-Z]{1,10}).[a-zA-Z]{1,10}.java.lang.Class,java.lang.String,int,int,int.$")){
                    m = methods[i];
                    break;
                }
                if (methods[i].toGenericString().matches("^private static void (net.minecraft.src.)?([a-zA-Z]{1,10}).[a-zA-Z]{1,10}.java.lang.Class,java.lang.String,int.$")){
                    m = methods[i];
                    break;
                }
            }
            m.setAccessible(true);
            if (par3 >= 0 && par4 >= 0){
                m.invoke(null, par0Class, par1Str, par2, par3, par4);
            }else{
                m.invoke(null, par0Class, par1Str, par2);
            }
            System.out.println("OldDays: Added "+par1Str+" entity");
        }catch(Exception ex){
            System.out.println("OldDays: Failed to add entity: "+ex);
        }
    }

    public static void setInWorldInfo(String var, Object b){
        World world = mod_OldDays.getMinecraft().theWorld;
        if (world==null){
            return;
        }
        WorldInfo info = world.getWorldInfo();
        try{
            info.getClass().getDeclaredField(var).set(info, b);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, Icon override){
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

    public void onFallbackChange(boolean fallback){}

    public String[] getAdditionalPackageData(){
        return null;
    }

    public void readAdditionalPackageData(String[] data){}

    public void refreshTextures(){}

    public void addMobSpawn(EnumCreatureType t, Class c, int i, int j, int k){
        for (OldDaysModule module : core.modules){
            module.addMobSpawn_do(t, c, i, j, k);
        }
        for (BiomeGenBase biome : BiomeGenBase.biomeList){
            if (biome == null || biome == BiomeGenBase.hell || biome == BiomeGenBase.sky){
                continue;
            }
            List list = biome.getSpawnableList(t);
            list.add(new SpawnListEntry(c, i, j, k));
        }
    }

    protected void addMobSpawn_do(EnumCreatureType t, Class c, int i, int j, int k){}

    public void onInitPlayer(EntityClientPlayerMP player, GameSettings settings){}

    @Override
    public int compareTo(OldDaysModule m){
        if (id < m.id){
            return -1;
        }else if (id > m.id){
            return 1;
        }
        return 0;
    }
}