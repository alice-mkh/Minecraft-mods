package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class OldDaysProperty{
    public static int GUI_TYPE_BUTTON = 1;
    public static int GUI_TYPE_DROPDOWN = 2;
    public static int GUI_TYPE_FIELD = 3;

    public int id;
    public int type;
    public int guitype;
    public Field field;
    public OldDaysModule module;
    public boolean error;
    public boolean allowedInSMP;
    public boolean allowedInFallback;
    public boolean noSounds;
    public int disabled;
    public boolean guiRefresh;

    public OldDaysProperty(OldDaysModule m, int i, String f){
        module = m;
        id = i;
        guitype = GUI_TYPE_BUTTON;
        error = false;
        allowedInSMP = true;
        allowedInFallback = true;
        noSounds = false;
        disabled = 0;
        guiRefresh = false;
        try{
            field = module.getClass().getDeclaredField(f);
        }catch(Exception ex){
            disable();
        }
        module.properties.add(this);
    }

    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name");
    }

    public String getDisabledButtonText(){
        return mod_OldDays.lang.get(getName()+".name");
    }

    public void onChange(){}

    public int getDisableReason(){
        if (error){
            return 1;
        }
        if (!allowedInFallback && mod_OldDays.texman.fallbacktex){
            return 2;
        }
        if (!allowedInSMP && mod_OldDays.getMinecraft().theWorld!=null){
            if (mod_OldDays.getMinecraft().theWorld.isRemote){
                return 3;
            }
        }
        if (noSounds){
            return 4;
        }
        return disabled;
    }

    public boolean isDisabled(){
        return getDisableReason() > 0;
    }

    public void setFieldValue(){
        disable();
    }

    protected void disable(){
        System.out.println("Error in "+module.name+" module, "+field.getName()+" property, disabling");
        error = true;
    }

    public void incrementValue(){}

    public void updateValue(){}

    public void setSMPValue(){}

    public void setDefaultValue(){}

    public void loadFromString(String str){}

    public String saveToString(){
        return "";
    }

    public String getName(){
        return module.name.toLowerCase()+"."+field.getName().toLowerCase();
    }

    public String[] getTooltip(){
        ArrayList<String> list = new ArrayList<String>();
        list.add(mod_OldDays.lang.get(getName()+".name"));
        list.add("");
        int num = mod_OldDays.getDescriptionNumber(getName()+".desc");
        for (int i = 0; i < num; i++){
            list.add("§7"+mod_OldDays.lang.get(getName()+".desc"+(i+1)));
        }
        if (isDisabled()){
            if (num > 0){
                list.add("");
            }
            list.add("§4"+mod_OldDays.lang.get("gui.error"+getDisableReason()));
        }
        return list.toArray(new String[list.size()]);
    }

    public void setGUIRefresh(){
        guiRefresh = true;
    }
}