package net.minecraft.src;
import java.io.*;
import java.net.URL;
import java.util.*;
import java.util.zip.*;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class mod_OldDays extends Mod{
    public String getModVersion(){
        return "2.0";
    }

    public String getMcVersion(){
        return "1.3.1";
    }

    public String getModName(){
        return "OldDays";
    }

    public mod_OldDays(){
        texman = new TextureManager();
        saveman = new SavingManager(this);
        smpman = new SMPManager(this);
        modules = new ArrayList();
        lang = new OldDaysEasyLocalization("olddays");
        keyBindings = new ArrayList();
    }

    public void load(){
        setUseTick(true, true);
        registerKey(keySettings = new KeyBinding(lang.get("OldDays Settings"), 35));
        loadModules(this);
        saveman.loadAll();
    }

    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, int override){
        for (int j = 0; j < modules.size(); j++){
            if (((OldDaysModule)mod_OldDays.modules.get(j)).renderBlocks(r, i, b, x, y, z, id, override)){
                return true;
            }
        }
        return false;
    }

    public void onTick(){
        texman.onTick();
        for (int i = 0; i < modules.size(); i++){
            ((OldDaysModule)mod_OldDays.modules.get(i)).onTick();
        }
        for (int i = 0; i < keyBindings.size(); i++){
            KeyBinding key = ((KeyBinding)keyBindings.get(i));
            if (key.isPressed()){
                if (key == keySettings && getMinecraftInstance().currentScreen == null){
                    getMinecraftInstance().displayGuiScreen(new GuiOldDaysModules(null));
                    continue;
                }
                for (int j = 0; j < modules.size(); j++){
                    ((OldDaysModule)mod_OldDays.modules.get(j)).catchKeyEvent(key);
                }
            }
        }
    }

    public void handlePacketFromClient(Packet300Custom packet){
        super.handlePacketFromClient(packet);
        if (packet.getId() != SMPManager.PACKET_C2S_REQUEST){
            return;
        }
        System.out.println("WTF");
        sendPacketToAll(0, "Client, it's server, sending an answer.",
                           "Do you see it?");
    }

    public void onGUITick(GuiScreen gui){
        for (int i = 0; i < modules.size(); i++){
            ((OldDaysModule)mod_OldDays.modules.get(i)).onGUITick(gui);
        }
    }

    public static void loadModules(mod_OldDays core){
        Class c = net.minecraft.src.mod_OldDays.class;
        String p = "";
        try{
            p = c.getPackage().getName()+".";
        }catch(Exception ex){}
        String path = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        File file = new File(path.replace("%20", " ")+p.replace(".", "/"));
        List classes = new ArrayList();
        if (file.getName().endsWith(".zip") || file.getName().endsWith(".jar")){
            try{
                ZipFile jar = new ZipFile(file);
                Enumeration entries = jar.entries();
                while (entries.hasMoreElements()){
                    String str = ((ZipEntry)entries.nextElement()).getName();
                    if (str.startsWith("OD") && str.endsWith(".class")){
                        classes.add(str.replace(".class", ""));
                    }
                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }else{
            String[] str = file.list();
            for (int i = 0; i < str.length; i++){
                if (str[i].startsWith("OD") && str[i].endsWith(".class")){
                    classes.add(str[i].replace(".class", ""));
                }
            }
        }
        for (int i = 0; i < classes.size(); i++){
            String name = ((String)classes.get(i));
            Class c2 = null;
            try{
                c2 = c.getClassLoader().loadClass(p+name);
            }catch(Exception ex){
                System.out.println("OldDays: Failed to load module: "+ex);
                ex.printStackTrace();
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
                ex.printStackTrace();
                continue;
            }
            modules.add(module);
            System.out.println("OldDays: Loaded "+module.name+" module");
        }
    }

    public static void loadModuleProperties(){}
    
    public static int getPropertyGuiType(int id, int id2){
        return getModuleById(id).getPropertyById(id2).guitype;
    }

    public static String getPropertyButtonText(int id, int id2){
        OldDaysProperty prop = getModuleById(id).getPropertyById(id2);
        return prop.isDisabled() ? prop.getDisabledButtonText() : prop.getButtonText();
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
        return getModuleById(id).getPropertyById(id2).saveToString();
    }

    public static void setStringPropValue(int id, int id2, String str){
        getModuleById(id).getPropertyById(id2).loadFromString(str);
    }

    public static void sendCallback(int id, int id2){
        getModuleById(id).last = id2;
        getModuleById(id).getPropertyById(id2).onChange();
        texman.onTick();
    }

    public static void sendCallbackAndSave(int id, int id2){
        sendCallback(id, id2);
        saveman.saveAll();
    }

    public static Minecraft getMinecraftInstance(){
        return Minecraft.getMinecraftInstance();
    }

    public static int getDescriptionNumber(String s){
        boolean end = false;
        int i = 0;
        while (!end){
            i++;
            end = (s+i).startsWith(lang.get(s+i));
        }
        return i - 1;
    }

    public static void refreshConditionProperties(){
        for (int i = 0; i < modules.size(); i++){
            OldDaysModule module = ((OldDaysModule)modules.get(i));
            for (int j = 0; j < module.properties.size(); j++){
                OldDaysProperty prop = ((OldDaysProperty)module.properties.get(j));
                if (prop instanceof OldDaysPropertyCond){
                    OldDaysPropertyCond prop2 = ((OldDaysPropertyCond)prop);
                    prop2.boolValue = prop2.getBoolValue(1);
                    prop2.onChange();
                }
            }
        }
    }

    public static Object getField(Class c, Object o, String str){
        System.out.println("FIXME: Use number instead of \""+str+"\"!");
        try{
            Field f = c.getDeclaredField(str);
            f.setAccessible(true);
            return f.get(o);
        }catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public static Object getField(Class c, Object o, int num){
        try{
            Field f = c.getDeclaredFields()[num];
            f.setAccessible(true);
            return f.get(o);
        }catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public static void setField(Class c, Object o, String str, Object val){
        System.out.println("FIXME: Use number instead of \""+str+"\"!");
        try{
            Field f = c.getDeclaredField(str);
            f.setAccessible(true);
            f.set(o, val);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public static void setField(Class c, Object o, int num, Object val){
        try{
            Field f = c.getDeclaredFields()[num];
            f.setAccessible(true);
            f.set(o, val);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public static Object callMethod(Class c, Object o, String str, Class[] pars, Object[] args){
        try{
            Method m = c.getDeclaredMethod(str, pars);
            m.setAccessible(true);
            return m.invoke(o, args);
        }catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public static Object callMethod(Class c, Object o, String str){
        return callMethod(c, o, str, new Class[]{}, new Object[]{});
    }

    public static void registerKey(KeyBinding key){
        GameSettings s = getMinecraftInstance().gameSettings;
        KeyBinding[] newb = new KeyBinding[s.keyBindings.length + 1];
        for (int i = 0; i < s.keyBindings.length; i++){
            newb[i] = s.keyBindings[i];
        }
        newb[s.keyBindings.length] = key;
        s.keyBindings = newb;
        keyBindings.add(key);
    }

    public void onLoadingSP(String par1Str, String par2Str){
        for (int i = 0; i < modules.size(); i++){
            OldDaysModule module = ((OldDaysModule)modules.get(i));
            module.onLoadingSP(par1Str, par2Str);
        }
    }

    public void onLoadingMP(){
        System.out.println("WTTF");
        smpman.requestSettings();
    }

    public KeyBinding keySettings ;
    public static TextureManager texman;
    public static SavingManager saveman;
    public static SMPManager smpman;
    public static List modules;
    public static OldDaysEasyLocalization lang;
    public static List keyBindings;
}
