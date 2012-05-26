package net.minecraft.src;
import java.util.*;
import java.io.*;
import java.lang.reflect.Field;

public class mod_OldDays extends BaseModMp{
    public String getVersion(){
        return "1.2.5";
    }

    public mod_OldDays(){
    }

    public void load(){}

    public void handlePacket(Packet230ModLoader packet, EntityPlayerMP player){
        switch(packet.packetType){
            case 1:{
                int module = packet.dataInt[0];
                sendSettings(module, player);
            }
        }
    }

    public void sendSettings(int module, EntityPlayerMP entityplayermp){
        int[] settings = new int[proplength[module]+1];
        try{
            settings[1] = module;
        }catch(Exception ex){
            return;
        }
        for (int i = 1; i < proplength[module]; i++){
            settings[i+1] = propvalue[module][i];
        }
        Packet230ModLoader packet = new Packet230ModLoader();
        packet.packetType = 0;
        packet.dataInt = settings;
        ModLoaderMp.sendPacketTo(this, entityplayermp, packet);
    }

    protected static void loadModuleProperties(){
        PropertyManager pmanager = new PropertyManager(new File("oldDays"+modules2[modulenum].replaceFirst("mod_OldDays","")+".properties"));
        for (int i = 1; i <= proplength[modulenum]; i++){
            try{
                int val = 0;
                if (propmax[modulenum][i]<=2){
                    val = pmanager.getBooleanProperty(propfield[modulenum][i].getName(), propvalue[modulenum][i]>0) ? 1 : 0;
                }else{
                    val = pmanager.getIntProperty(propfield[modulenum][i].getName(), propvalue[modulenum][i]);
                }
                propvalue[modulenum][i] = val;
                sendCallback(modulenum, i, val);
            }catch(Exception ex){
                System.out.println(modulenum+" "+i+" "+ex);
            }
        }
    }

    private static void addProp(int i2, String name, int mp, int val, String var, String desc, int max){
        propname[modulenum][i2]=name;
        propvalue[modulenum][i2]=val;
        propmax[modulenum][i2]=max;
        try{
            propfield[modulenum][i2]=Class.forName(modules[modulenum]).getDeclaredField(var);
        }catch (Exception ex){}
        proplength[modulenum]++;
    }

    protected static void addProperty(int i2, String name, boolean mp, boolean val, String var, String desc){
        addProp(i2, name, mp ? 1 : 0, val ? 1 : 0, var, desc, 2);
    }

    protected static void addProperty(int i2, String name, boolean val, String var, String desc){
        addProp(i2, name, -1, val ? 1 : 0, var, desc, 2);
    }

    protected static void addProperty(int i2, String name, int mp, int val, String var, String desc, int max){
        addProp(i2, name, mp, val, var, desc, max);
    }

    protected static void addProperty(int i2, String name, int val, String var, String desc, int max){
        addProp(i2, name, -1, val, var, desc, max);
    }

    protected static void addProperty(int i2, String name, int mp, int val, String var, String desc, String[] names){
        if (names==null){
            System.out.println("Failed to add option "+modulenum+" "+i2);
            return;
        }
        addProp(i2, name, mp, val, var, desc, names.length);
    }

    protected static void addProperty(int i2, String name, int val, String var, String desc, String[] names){
        if (names==null){
            System.out.println("Failed to add option "+modulenum+" "+i2);
            return;
        }
        addProp(i2, name, -1, val, var, desc, names.length);
    }

    private static void disableProperty(int id, int i2){
        System.out.println("Error, disabling option "+id+" "+i2);
    }

    protected static void registerModule(Object module, int num){
        modulenum = num;
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
            propvalue=new int[10][30];
        }
        if (propfield==null){
            propfield=new Field[10][30];
        }
        if (propmax==null){
            propmax=new int[10][30];
        }
        if (proplength==null){
            proplength=new int[10];
            proplength[modulenum]=0;
        }
    }

    protected void setBool(Class where, String what, boolean value){
        try{
            where.getDeclaredField(what).setBoolean(null, value);
        }catch(Exception ex){
            disableProperty(lastmodule, lastoption);
        }
    }

    protected void setInt(Class where, String what, int value){
        try{
            where.getDeclaredField(what).setInt(null, value);
        }catch(Exception ex){
            disableProperty(lastmodule, lastoption);
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

    public static String[][] propname;
    public static int[][] propmax;
    public static int[][] propvalue;
    public static Field[][] propfield;
    public static int[] proplength;
    public static String[] modules = new String[10];
    public static String[] modules2 = new String[10];
    public static int modulecount = 0;
    private static int modulenum;
    public static int lastmodule = 0;
    public static int lastoption = 0;
}