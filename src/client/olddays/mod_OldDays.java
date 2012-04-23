package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;

public class mod_OldDays extends BaseModMp{
    public String getVersion(){
        return "1.2.5";
    }

    public mod_OldDays(){
    }

    public void load(){
        moduleGui = new GuiOldDaysModules(null);
        ModLoader.registerKey(this, this.keySettings, false);
        ModLoader.addLocalization("key_settings", "Old Days Settings");
        ModLoader.setInGameHook(this, true, true);
    }

    public boolean onTickInGame(float f, Minecraft minecraft){
        if (minecraft.theWorld.isRemote == needSettings){
            for (int i = 0; i < modules.length; i++){
                if (modules[i]!=null){
                    modulenum = i;
                    loadModuleProperties();
                    if (minecraft.theWorld.isRemote){
                        setDefaultSMPSettings(i);
                        requestSettings(i);
                    }
                }
            }
            needSettings = !needSettings;
        }
        return true;
    }

    private void setDefaultSMPSettings(int module){
        for (int i = 1; i < proplength[module]; i++){
            try{
                if (propsmp[module][i]>=0){
                    propvalue[module][i] = propsmp[module][i];
                    sendCallback(module, i, propsmp[module][i]);
                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
    }

    public void requestSettings(int module){
        Packet230ModLoader packet = new Packet230ModLoader();
        packet.packetType = 1;
        packet.dataInt = new int[]{module};
        ModLoaderMp.sendPacket(this, packet);
    }

    public void handlePacket(Packet230ModLoader packet){
        int[] settings = packet.dataInt;
        int module = settings[1];
        try{
            for (int i = 1; i < settings.length-1; i++){
                propvalue[module][i] = settings[i+1];
                sendCallback(module, i, settings[i+1]);
            }
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public static void saveModuleProperties(int id){
        Properties properties = new Properties();
        try{
            File file = new File((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/OldDays"+modules2[id].replaceFirst("mod_OldDays","")+".properties").toString());
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            for (int i = 1; i <= proplength[id]; i++){
                properties.setProperty(propfield[id][i].getName(), Integer.toString(propvalue[id][i]));
            }
            properties.store(fileoutputstream, "Old Days config");
            fileoutputstream.close();
        }
        catch(IOException ioexception){
            ioexception.printStackTrace();
        }
    }

    protected static void registerModule(int num){
        modulenum = num;
        modulegui[modulenum] = null;
    }

    protected static void registerGui(GuiScreen gui){
        modulegui[modulenum] = gui;
    }

    protected static void loadModuleProperties(){
        Properties properties = new Properties();
        try{
            File file = new File((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/OldDays"+modules2[modulenum].replaceFirst("mod_OldDays","")+".properties").toString());
            boolean flag = file.createNewFile();
            if(flag){
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                for (int i = 1; i <= proplength[modulenum]; i++){
                    properties.setProperty(propfield[modulenum][i].getName(), Integer.toString(propvalue[modulenum][i]));
                }
                properties.store(fileoutputstream, "Old Days config");
                fileoutputstream.close();
            }
            try{
                properties.load(new FileInputStream((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/OldDays"+modules2[modulenum].replaceFirst("mod_OldDays","")+".properties").toString()));
                for (int i = 1; i <= proplength[modulenum]; i++){
                    int val = Integer.parseInt(properties.getProperty(propfield[modulenum][i].getName()));;
                    propvalue[modulenum][i] = val;
                    sendCallback(modulenum, i, val);
                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }
        catch(IOException ioexception){
            ioexception.printStackTrace();
        }
    }

    public static void addModules(GuiOldDaysModules gui){
        for (int i = 0; i < modules2.length; i++){
            if (modules2[i]!=null){
                gui.addModule(i, modules2[i].replaceFirst("mod_OldDays", ""));
            }
        }
    }

    protected void setBool(Class where, String what, boolean value){
        try{
            where.getDeclaredField(what).setBoolean(null, value);
        }catch(Exception ex){
            System.out.println("Error, disabling option "+lastmodule+" "+lastoption);
            disabled[lastmodule][lastoption]=true;
        }
    }

    protected void setInt(Class where, String what, int value){
        try{
            where.getDeclaredField(what).setInt(null, value);
        }catch(Exception ex){
            System.out.println("Error, disabling option "+lastmodule+" "+lastoption);
            disabled[lastmodule][lastoption]=true;
        }
    }

    protected static void addProperty(Object module, int i2, String name, int mp, int val, String var, String desc){
        String modulename = module.getClass().getName();
        if (modules[modulenum]==null || modules2[modulenum]==null){
            if (modulename.startsWith("net.minecraft.src.mod_OldDays") && modulename != "net.minecraft.src.mod_OldDays"){
                modules[modulenum]=modulename;
                modules2[modulenum]=modulename.replaceFirst("net.minecraft.src.", "");
                modulecount++;
            }else if (modulename.startsWith("mod_OldDays") && modulename != "mod_OldDays"){
                modules[modulenum]=modulename;
                modules2[modulenum]=modulename;
                modulecount++;
            }
        }
        if (propname==null){
            propname=new String[10][30];
        }
        if (propdesc==null){
            propdesc=new String[10][30];
        }
        if (propvalue==null){
            propvalue=new int[10][30];
        }
        if (propfield==null){
            propfield=new Field[10][30];
        }
        if (disabled==null){
            disabled=new boolean[10][30];
        }
        if (propsmp==null){
            propsmp=new int[10][30];
        }
        if (propmax==null){
            propmax=new int[10][30];
        }
        if (proplength==null){
            proplength=new int[10];
            proplength[modulenum]=0;
        }
        propname[modulenum][i2]=name;
        propvalue[modulenum][i2]=val;
        disabled[modulenum][i2]=false;
        propsmp[modulenum][i2]=mp;
        propdesc[modulenum][i2]=desc;
        propmax[modulenum][i2]=2;
        try{
            propfield[modulenum][i2]=Class.forName(modules[modulenum]).getDeclaredField(var);
        }catch (Exception ex){}
        proplength[modulenum]++;
    }

    public void keyboardEvent(KeyBinding keybinding){
        if (keybinding==keySettings && ModLoader.getMinecraftInstance().currentScreen==null){
            ModLoader.openGUI(ModLoader.getMinecraftInstance().thePlayer, moduleGui);
        }
    }

    public void callback (int i){}

    protected static void sendCallback(int id, int i2, int b){
        try{
            if (propmax[id][i2]<=2){
                mod_OldDays.propfield[id][i2].set(Class.forName(modules[id]), b>0);
            }else{
                mod_OldDays.propfield[id][i2].set(Class.forName(modules[id]), b);
            }
        }catch(Exception ex){}
        lastmodule = id;
        lastoption = i2;
        int id2 = 0;
        List list = ModLoader.getLoadedMods();
        Object obj = null;
        for(int i = 0; i < list.size(); i++){
            try{
                if (list.get(i).getClass() == Class.forName(modules[id])){
                    obj = list.get(i);
                    break;
                }
            }catch (Exception ex){
                continue;
            }
        }
        try{
            ((mod_OldDays)obj).callback(i2);
        }catch (Exception ex){}
    }

    protected GuiOldDaysModules moduleGui;
    public KeyBinding keySettings = new KeyBinding("key_settings", 35);
    public static String[][] propname;
    public static int[][] propvalue;
    public static Field[][] propfield;
    public static String[][] propdesc;
    public static int[][] propmax;
    public static boolean[][] disabled;
    public static int[][] propsmp;
    public static int[] proplength;
    public static String[] modules = new String[10];
    public static String[] modules2 = new String[10];
    public static int modulecount = 0;
    private static int modulenum;
    public static GuiScreen[] modulegui = new GuiScreen[10];
    public static int lastmodule = 0;
    public static int lastoption = 0;
    public static boolean needSettings = true;
}