package net.minecraft.src;

import java.util.ArrayList;

public class OldDaysPropertyRGB extends OldDaysPropertyInt{
    public OldDaysPropertyRGB(OldDaysModule m, int i, int v, int smp, String f){
        super(m, i, v, smp, f, 0xFFFFFF);
        defaultValue = v;
        guitype = GUI_TYPE_FIELD;
        smpValue = smp;
    }

    @Override
    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": 0x"+Integer.toHexString(value);
    }

    @Override
    public void loadFromString(String str){
        if (str.matches("^defau[l]?[t]?$")){
            value = defaultValue;
            return;
        }
        if (str.matches("^[0-9]{1,6}$")){
            str = str.toLowerCase();
            value = Integer.parseInt(str);
            return;
        }
        if (str.matches("^(0x|#)?[0-9a-fA-F]{1,6}$")){
            str = str.replaceAll("(0x|#)", "");
            str = str.toLowerCase();
            value = Integer.parseInt(str, 16);
            return;
        }
        if (str.matches("^([0-9]{1,3}[,.; ] ?){2}[0-9]{1,3}$")){
            String[] str2 = str.split("[,.; ] ?");
            if (str2.length>=3){
                value = Integer.parseInt(str2[0]) << 16 | Integer.parseInt(str2[1]) << 8 | Integer.parseInt(str2[2]);
            }else{
                value = 0;
            }
            return;
        }
        value = 0;
    }

    @Override
    public String saveToString(){
        return Integer.toHexString(value).toUpperCase();
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
        if (isDisabled()){
            if (num > 0){
                list.add("");
            }
            list.add("§4"+mod_OldDays.lang.get("gui.error"+getDisableReason()));
        }
        return list.toArray(new String[list.size()]);
    }
}