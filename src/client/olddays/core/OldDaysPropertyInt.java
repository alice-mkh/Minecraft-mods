package net.minecraft.src;

import java.util.ArrayList;

public class OldDaysPropertyInt extends OldDaysProperty{
    public int value;
    public int smpValue;
    public int defaultValue;
    public int min;
    public int max;
    public boolean useNames;

    public OldDaysPropertyInt(OldDaysModule m, int i, int v, String f, int m1, int m2){
        super(m, i, f);
        value = defaultValue = v;
        guitype = GUI_TYPE_BUTTON;
        min = m1;
        max = m2;
        useNames = false;
    }

    public OldDaysPropertyInt(OldDaysModule m, int i, int v, int smp, String f, int m1, int m2){
        this(m, i, v, f, m1, m2);
        smpValue = smp;
        allowedInSMP = false;
    }

    public OldDaysPropertyInt(OldDaysModule m, int i, int v, String f, int m2){
        this(m, i, v, f, 0, m2);
    }

    public OldDaysPropertyInt(OldDaysModule m, int i, int v, int smp, String f, int m2){
        this(m, i, v, smp, f, 0, m2);
    }

    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+(useNames ? mod_OldDays.lang.get(getName()+(value+1)) : value);
    }

    public void onChange(){
        try{
            field.set(module, value);
            module.callback(id);
        }catch(Exception ex){
            ex.printStackTrace();
            disable();
        }
    }

    public void incrementValue(){
        if (value < max){
            value++;
        }else{
            value = min;
        }
    }

    public void updateValue(){
        try{
            value = ((Integer)field.get(module));
        }catch(Exception ex){
            ex.printStackTrace();
            disable();
            return;
        }
    }

    public void setSMPValue(){
        value = smpValue;
    }

    public void setDefaultValue(){
        value = defaultValue;
    }

    protected void disable(){
        super.disable();
        value = smpValue;
    }

    public void loadFromString(String str){
        int i = 0;
        try{
            i = Integer.parseInt(str);
        }catch(Exception ex){}
        if (i < min){
            i = min;
        }
        if (i > max){
            i = max;
        }
        value = i;
    }

    public String saveToString(){
        return ""+value;
    }

    public String[] getTooltip(){
        ArrayList<String> list = new ArrayList<String>();
        list.add(mod_OldDays.lang.get(getName()+".name"));
        list.add("");
        int num = mod_OldDays.getDescriptionNumber(getName()+".desc");
        for (int i = 0; i < num; i++){
            list.add("§7"+mod_OldDays.lang.get(getName()+".desc"+(i+1)));
        }
        if (useNames){
            for (int i = min; i <= max; i++){
                list.add("<- • §a"+mod_OldDays.lang.get(getName()+(i+1))+"§7: "+mod_OldDays.lang.get(getName()+(i+1)+".desc"));
                int num2 = mod_OldDays.getDescriptionNumber(getName()+(i+1)+".desc");
                for (int j = 0; j < num2; j++){
                    list.add("<-    §7"+mod_OldDays.lang.get(getName()+(i+1)+".desc"+(j+1)));
                }
            }
        }else{
            list.add("§7"+mod_OldDays.lang.get("gui.possible")+": §r"+min+"-"+max);
        }
        if (isDisabled()){
            if (num > 0 || useNames){
                list.add("");
            }
            list.add("§4"+mod_OldDays.lang.get("gui.error"+getDisableReason()));
        }
        return list.toArray(new String[list.size()]);
    }

    public OldDaysPropertyInt setUseNames(){
        useNames = true;
        return this;
    }

    public OldDaysPropertyInt setField(){
        guitype = GUI_TYPE_FIELD;
        return this;
    }
}