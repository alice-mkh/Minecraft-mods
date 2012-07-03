package net.minecraft.src;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.*;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;

public class mod_OldDays extends BaseModMp{
    public String getVersion(){
        return "1.2.5";
    }

    public mod_OldDays(){
        texman = new TextureManager();
        saveman = new SavingManager(this);
        smpman = new SMPManager(this);
        modules = new ArrayList();
    }

    public void load(){
        ModLoader.registerKey(this, this.keySettings, false);
        ModLoader.addLocalization("key_settings", "Old Days Settings");
        ModLoader.setInGameHook(this, true, true);
        loadModules(this);
        saveman.loadAll();
    }

    public void keyboardEvent(KeyBinding keybinding){
        if (keybinding==keySettings && ModLoader.getMinecraftInstance().currentScreen==null){
            ModLoader.openGUI(ModLoader.getMinecraftInstance().thePlayer, new GuiOldDaysModules(null));
        }
    }

    public boolean onTickInGame(float f, Minecraft minecraft){
        smpman.onTick();
        texman.onTick();
        return true;
    }

    public static void loadModules(mod_OldDays core){
        Class c = net.minecraft.src.mod_OldDays.class;
        String p = c.getPackage().getName();
        File file = new File(c.getProtectionDomain().getCodeSource().getLocation().getPath()+p.replace(".", "/"));
        List classes = new ArrayList();
        List classes2 = new ArrayList();
        if (file.getName().endsWith(".zip") || file.getName().endsWith(".jar")){
            try{
                ZipFile jar = new ZipFile(file);
                Enumeration entries = jar.entries();
                while (entries.hasMoreElements()){
                    System.out.println(entries.nextElement());
                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }else{
            String[] str = file.list();
            for (int i = 0; i < str.length; i++){
                classes.add(str[i]);
            }
        }
        for (int i = 0; i < classes.size(); i++){
            String s = ((String)classes.get(i));
            if (s.startsWith("OD") && s.endsWith(".class")){
                classes2.add(s.replace(".class", ""));
            }
        }
        for (int i = 0; i < classes2.size(); i++){
            String name = ((String)classes2.get(i));
            Class c2 = null;
            try{
                c2 = c.getClassLoader().loadClass(p+"."+name);
            }catch(Exception ex){
                System.out.println("OldDays: Failed to load module: "+ex);
                continue;
            }
            if (!((net.minecraft.src.OldDaysModule.class).isAssignableFrom(c2))){
                continue;
            }
            OldDaysModule module = null;
            try{
                module = ((OldDaysModule)c2.getDeclaredConstructor(c).newInstance(core));
            }catch(Exception ex){
                System.out.println("OldDays: Failed to load module: "+ex);
                continue;
            }
            modules.add(module);
            System.out.println("OldDays: Loaded "+module.name+" module");
        }
    }

    public void handlePacket(Packet230ModLoader packet){}

    public static void loadModuleProperties(){}
    
    public static int getPropertyType(int id, int id2){
        return getModuleById(id).getPropertyById(id2).type;
    }

    public static String getPropertyButtonText(int id, int id2){
        return getModuleById(id).getPropertyById(id2).getButtonText();
    }

    public static OldDaysModule getModuleById(int id){
        for (int i = 0; i < modules.size(); i++){
            OldDaysModule module = ((OldDaysModule)modules.get(i));
            if (module.id == id){
                return module;
            }
        }
        return null;
    }

    public static void saveModuleProperties(int id){}

    public static String getStringPropValue(int id, int id2){
        if (getPropertyType(id, id2) != OldDaysProperty.TYPE_STRING){
            return "";
        }
        return ((OldDaysPropertyString)getModuleById(id).getPropertyById(id2)).value;
    }

    public static void setStringPropValue(int id, int id2, String str){
        if (getPropertyType(id, id2) != OldDaysProperty.TYPE_STRING){
            return;
        }
        ((OldDaysPropertyString)getModuleById(id).getPropertyById(id2)).value = str;
    }

    public static void sendCallback(int id, int id2){
        getModuleById(id).last = id2;
        getModuleById(id).getPropertyById(id2).onChange();
    }

    public static void sendCallbackAndSave(int id, int id2){
        sendCallback(id, id2);
        saveman.saveAll();
    }

    public static void bumpProperties(){
        for (int i = 0; i < modules.size(); i++){
            for (int j = 1; j < getModuleById(i).properties.size(); j++){
                sendCallback(i, j);
            }
        }
    }

    public KeyBinding keySettings = new KeyBinding("key_settings", 35);
    public static TextureManager texman;
    public static SavingManager saveman;
    public static SMPManager smpman;
    public static List modules;
    public static Map map;
}
