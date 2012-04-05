package net.minecraft.src;
import java.util.*;
import java.io.*;
import java.lang.reflect.Field;

public class mod_OldDays extends BaseModMp{
    public String getVersion(){
        return "1.2.5";
    }

    public boolean hasClientSide()
    {
        return false;
    }

    public mod_OldDays(){
    }

    public void load(){}

    public static void loadModuleProperties(int id){
        PropertyManager pmanager = new PropertyManager(new File("oldDays"+modules2[id].replaceFirst("mod_OldDays","")+".properties"));
        for (int i = 1; i <= proplength[id]; i++){
            try{
                propvalue[id][i] = pmanager.getBooleanProperty(propfield[id][i].getName(), propvalue[id][i]);
                propfield[id][i].setBoolean(Class.forName(modules[id]), propvalue[id][i]);
                sendCallback(id, i);
            }catch(Exception ex){
                System.out.println(id+" "+i+" "+ex);
            }
        }
    }

    public static void addProperty(Object module, int num, int i2, String name, boolean val, String var){
        String modulename = module.getClass().getName();
        if (modules[num]==null || modules2[num]==null){
            if (modulename.startsWith("net.minecraft.src.mod_OldDays") && modulename != "net.minecraft.src.mod_OldDays"){
                modules[num]=modulename;
                modules2[num]=modulename.replaceFirst("net.minecraft.src.", "");
                modulecount++;
            }else if (modulename.startsWith("mod_OldDays") && modulename != "mod_OldDays"){
                modules[num]=modulename;
                modules2[num]=modulename;
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
        if (proplength==null){
            proplength=new int[10];
            proplength[num]=0;
        }
        propname[num][i2]=name;
        propvalue[num][i2]=val;
        try{
            propfield[num][i2]=Class.forName(modules[num]).getDeclaredField(var);
        }catch (Exception ex){}
        proplength[num]++;
    }

    public void callback (int i){}

    public static void sendCallback(int id, int i2){
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

    public static String[][] propname;
    public static boolean[][] propvalue;
    public static Field[][] propfield;
    public static int[] proplength;
    public static String[] modules = new String[10];
    public static String[] modules2 = new String[10];
    public static int modulecount = 0;
}