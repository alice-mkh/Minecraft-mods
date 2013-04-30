package net.minecraft.src;

import java.util.ArrayList;

public class OldDaysPropertyFloat extends OldDaysProperty{
    public float value;
    public float smpValue;
    public float defaultValue;
    public float min;
    public float max;

    public OldDaysPropertyFloat(OldDaysModule m, int i, float v, float smp, String f, float m1, float m2){
        super(m, i, f);
        value = defaultValue = v;
        guitype = GUI_TYPE_FIELD;
        min = m1;
        max = m2;
        smpValue = smp;
    }

    @Override
    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+value;
    }

    @Override
    public void onChange(boolean flag){
        if (shouldSkipUpdates() && flag){
            return;
        }
        try{
            field.set(module, value);
            module.callback(id);
        }catch(Exception ex){
            if (flag){
                ex.printStackTrace();
                disable();
            }
        }
    }

    @Override
    public void incrementValue(){
        if (value < max || max <= min){
            value += 1.0F;
        }else{
            value = min;
        }
    }

    @Override
    public void decrementValue(){
        if (value > min || max <= min){
            value -= 1.0F;
        }else{
            value = max;
        }
    }

    @Override
    public void updateValue(){
        try{
            value = ((Float)field.get(module));
        }catch(Exception ex){
            ex.printStackTrace();
            disable();
            return;
        }
    }

    @Override
    public String getSMPValue(){
        return ""+smpValue;
    }

    @Override
    public void setSMPValue(){
        value = smpValue;
    }

    @Override
    public String getDefaultValue(){
        return ""+defaultValue;
    }

    @Override
    public void setDefaultValue(){
        value = defaultValue;
    }

    @Override
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

    @Override
    public String saveToString(){
        return ""+value;
    }

    @Override
    public String[] getTooltip(){
        ArrayList<String> list = new ArrayList<String>();
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
        return list.toArray(new String[list.size()]);
    }
}