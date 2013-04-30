package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.ArrayList;

public abstract class OldDaysProperty{
    public static final int GUI_TYPE_BUTTON = 1;
    public static final int GUI_TYPE_DROPDOWN = 2;
    public static final int GUI_TYPE_FIELD = 3;

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
    public boolean refreshFallback;
    public String[] fallback;
    public boolean canBeLoaded;

    public OldDaysProperty(OldDaysModule m, int i, String f){
        module = m;
        id = i;
        guitype = GUI_TYPE_BUTTON;
        error = false;
        allowedInSMP = true;
        allowedInFallback = true;
        refreshFallback = false;
        noSounds = false;
        disabled = 0;
        guiRefresh = false;
        fallback = new String[0];
        try{
            field = module.getClass().getDeclaredField(f);
        }catch(Exception ex){
            disable();
        }
        canBeLoaded = true;
        module.properties.add(this);
    }

    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name");
    }

    public String getDisabledButtonText(){
        return mod_OldDays.lang.get(getName()+".name");
    }

    public int getDisableReason(){
        if (error){
            return 1;
        }
        if (!mod_OldDays.texman.hasEntry(fallback)){
            return 2;
        }
        if (!allowedInSMP && mod_OldDays.isVanillaSMP()){
            return 3;
        }
        if (noSounds){
            return 4;
        }
        return disabled;
    }

    public boolean isDisabled(){
        return getDisableReason() > 0;
    }

    public boolean shouldSkipUpdates(){
        return getDisableReason() == 1;
    }

    public void setFieldValue(){
        disable();
    }

    public abstract void incrementValue();

    public void decrementValue(){
        incrementValue();
    }

    public abstract void updateValue();

    public abstract void setSMPValue();

    public abstract String getDefaultValue();

    public abstract void setDefaultValue();

    public abstract void loadFromString(String str);

    public abstract String saveToString();

    public abstract void onChange(boolean flag);

    protected void disable(){
        if (error){
            return;
        }
        System.out.println("Error in "+module.name+" module, "+field.getName()+" property, disabling");
        error = true;
        setSMPValue();
        mod_OldDays.newlyDisabled.add(this);
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

    public OldDaysProperty setGUIRefresh(){
        guiRefresh = true;
        return this;
    }

    public boolean shouldRefreshOnFallback(){
        return !allowedInFallback || refreshFallback;
    }

    public OldDaysProperty setRefreshOnFallback(){
        refreshFallback = true;
        return this;
    }

    public OldDaysProperty setFallback(String... str){
        fallback = str;
        allowedInFallback = false;
        return this;
    }

    public OldDaysProperty disableLoading(){
        canBeLoaded = false;
        return this;
    }
}