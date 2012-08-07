package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class OldDaysPropertyFloat extends OldDaysProperty{
    public float value;
    public float smpValue;
    public float defaultValue;
    public float min;
    public float max;

    public OldDaysPropertyFloat(OldDaysModule m, int i, float v, String f, float m1, float m2){
        super(m, i, f);
        value = defaultValue = v;
        guitype = GUI_TYPE_FIELD;
        min = m1;
        max = m2;
    }

    public OldDaysPropertyFloat(OldDaysModule m, int i, float v, float smp, String f, float m1, float m2){
        this(m, i, v, f, m1, m2);
        smpValue = smp;
    }

    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+value;
    }

    public void onChange(){
        try{
            field.set(module, value);
            module.callback(id);
        }catch(Exception ex){
            System.out.println(ex);
            disable();
        }
    }

    public void incrementValue(){
        if (value < max || max <= min){
            value += 1.0F;
        }else{
            value = min;
        }
    }

    public void updateValue(){
        try{
            value = ((Float)field.get(module));
        }catch(Exception ex){
            System.out.println(ex);
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
        float i = 0;
        str = str.replace(",", ".");
        try{
            i = Float.parseFloat(str);
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
        List list = new ArrayList();
        list.add(mod_OldDays.lang.get(getName()+".name"));
        list.add("");
        int num = mod_OldDays.getDescriptionNumber(getName()+".desc");
        for (int i = 0; i < num; i++){
            list.add("§7"+mod_OldDays.lang.get(getName()+".desc"+(i+1)));
        }
        list.add("§7"+mod_OldDays.lang.get("gui.possible")+": §r"+min+"-"+max);
        if (isDisabled()){
            list.add("");
            list.add("§4"+mod_OldDays.lang.get("gui.error"+getDisableReason()));
        }
        return (String[])list.toArray(new String[list.size()]);
    }
}