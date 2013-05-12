package net.minecraft.src;

import java.util.ArrayList;

public class OldDaysPropertySet extends OldDaysProperty{
    public int[] value;
    public int[] smpValue;
    public int[] defaultValue;
    private boolean[] useNames;
    private int[] min;
    private int[] max;
    private int length;

    public OldDaysPropertySet(OldDaysModule m, int i, String v, String smp, String f, String content){
        super(m, i, f);
        parseContent(content);
        value = defaultValue = parseString(v);
        smpValue = parseString(smp);
        guitype = GUI_TYPE_PAGE;
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
    public void incrementValue(){}

    @Override
    public void updateValue(){
        try{
            value = ((int[])field.get(module));
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
        value = parseString(str);
    }

    public int[] parseString(String str){
        String[] s = str.split(";");
        int[] values = new int[length];
        for (int i = 0; i < length; i++){
            values[i] = parseInt(s[i]);
            if (values[i] < min[i]){
                values[i] = min[i];
            }
            if (values[i] > max[i]){
                values[i] = max[i];
            }
        }
        return values;
    }

    @Override
    public String saveToString(){
        StringBuilder b = new StringBuilder();
        for (int i : value){
            b.append(i);
            b.append(";");
        }
        b.deleteCharAt(b.length() - 1);
        return b.toString();
    }

    public String getValueButtonText(int i){
        StringBuilder b = new StringBuilder();
        b.append(mod_OldDays.lang.get(getName()+".value"+i));
        b.append(": ");
        if (useNames[i]){
            b.append(mod_OldDays.lang.get(getName()+".value"+i+".name"+value[i]));
        }else if (min[i] == 0 && max[i] == 1){
            b.append(mod_OldDays.lang.get("gui." + (value[i] > 0 ? "on" : "off")));
        }else{
            b.append(value[i]);
        }
        return b.toString();
    }

    public void incrementValue(int i){
        value[i]++;
        if (value[i] > max[i]){
            value[i] = min[i];
        }
    }

    public void decrementValue(int i){
        value[i]--;
        if (value[i] < min[i]){
            value[i] = max[i];
        }
    }

    private void parseContent(String str){
        String[] s = str.split(";");
        length = s.length;
        useNames = new boolean[length];
        min = new int[length];
        max = new int[length];
        for (int i = 0; i < length; i++){
            s[i] = s[i].trim();
            if (s[i].startsWith("b")){
                s[i] = "0,1,0";
            }
            String[] str2 = s[i].split(",");
            min[i] = parseInt(str2[0]);
            max[i] = parseInt(str2[1]);
            useNames[i] = parseInt(str2[2]) > 0;
        }
    }
}