package net.minecraft.src;

import java.lang.reflect.Field;

public class OldDaysProperty{
    public static int TYPE_BOOLEAN = 1;
    public static int TYPE_INTEGER = 2;
    public static int TYPE_STRING = 3;

    public int id;
    public String name;
    public String description;
    public int type;
    public Field field;
    public OldDaysModule module;
    public boolean error;
    public boolean allowedInSMP;
    public boolean allowedInFallback;

    public OldDaysProperty(OldDaysModule m, int i, String s, int t, String f){
        module = m;
        id = i;
        name = s;
        type = t;
        error = false;
        allowedInSMP = true;
        allowedInFallback = true;
        try{
            field = module.getClass().getDeclaredField(f);
        }catch(Exception ex){
            disable();
        }
    }

    public String getButtonText(){
        return name;
    }

    public void onChange(){}

    public boolean isDisabled(){
        if (error){
            return true;
        }
        if (!allowedInFallback && mod_OldDays.texman.fallbacktex){
            return true;
        }
        if (!allowedInSMP && ModLoader.getMinecraftInstance().theWorld!=null){
            if (ModLoader.getMinecraftInstance().theWorld.isRemote){
                return true;
            }
        }
        return false;
    }

    public void setFieldValue(){
        disable();
    }

    protected void disable(){
        System.out.println("Error in module "+module.name+", property "+name+", disabling");
        error = true;
    }

    public void incrementValue(){}

    public void setSMPValue(){}

    public void loadFromString(String str){}

    public String saveToString(){
        return "";
    }
}