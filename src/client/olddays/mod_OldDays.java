package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;
import java.lang.reflect.Field;

public class mod_OldDays extends BaseMod{
    public String getVersion(){
        return "1.2.5";
    }

    public mod_OldDays(){
    }

    public void load(){
        moduleGui = new GuiOldDaysModules(null);
        ModLoader.registerKey(this, this.keySettings, false);
        ModLoader.addLocalization("key_settings", "Old Days Settings");
    }

    public static void saveModuleProperties(int id){
        Properties properties = new Properties();
        try{
            File file = new File((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/OldDays"+modules2[id].replaceFirst("mod_OldDays","")+".properties").toString());
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            for (int i = 1; i <= proplength[id]; i++){
                properties.setProperty(propfield[id][i].getName(), Boolean.toString(propvalue[id][i]));
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
                    properties.setProperty(propfield[modulenum][i].getName(), Boolean.toString(propvalue[modulenum][i]));
                }
                properties.store(fileoutputstream, "Old Days config");
                fileoutputstream.close();
            }
            try{
                properties.load(new FileInputStream((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/OldDays"+modules2[modulenum].replaceFirst("mod_OldDays","")+".properties").toString()));
                for (int i = 1; i <= proplength[modulenum]; i++){
                    boolean val = Boolean.parseBoolean(properties.getProperty(propfield[modulenum][i].getName()));;
                    propvalue[modulenum][i] = val;
                    propfield[modulenum][i].setBoolean(Class.forName(modules[modulenum]), val);
                    sendCallback(modulenum, i);
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

    protected void setBool(String where, String what, boolean value){
        try{
            Class.forName(where).getDeclaredField(what).setBoolean(null, value);
        }catch(Exception ex){
            try{
                Class.forName("net.minecraft.src."+where).getDeclaredField(what).setBoolean(null, value);
            }catch(Exception ex2){
                System.out.println("Error, disabling option "+lastmodule+" "+lastoption);
                disabled[lastmodule][lastoption]=true;
            }
        }
    }

    protected static void addProperty(Object module, int i2, String name, boolean val, String var){
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
        if (propvalue==null){
            propvalue=new boolean[10][30];
        }
        if (propfield==null){
            propfield=new Field[10][30];
        }
        if (disabled==null){
            disabled=new boolean[10][30];
        }
        if (proplength==null){
            proplength=new int[10];
            proplength[modulenum]=0;
        }
        propname[modulenum][i2]=name;
        propvalue[modulenum][i2]=val;
        disabled[modulenum][i2]=false;
        try{
            propfield[modulenum][i2]=Class.forName(modules[modulenum]).getDeclaredField(var);
        }catch (Exception ex){}
        proplength[modulenum]++;
    }

    public void keyboardEvent(KeyBinding keybinding){
        if (keybinding==keySettings){
            ModLoader.openGUI(ModLoader.getMinecraftInstance().thePlayer, moduleGui);
        }
    }

    public void callback (int i){}

    protected static void sendCallback(int id, int i2){
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
    public static boolean[][] propvalue;
    public static Field[][] propfield;
    public static boolean[][] disabled;
    public static int[] proplength;
    public static String[] modules = new String[10];
    public static String[] modules2 = new String[10];
    public static int modulecount = 0;
    private static int modulenum;
    public static GuiScreen[] modulegui = new GuiScreen[10];
    public static int lastmodule = 0;
    public static int lastoption = 0;
}