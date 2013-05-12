package net.minecraft.src.nbxlite;

import java.util.ArrayList;
import net.minecraft.src.*;

public class OldDaysPropertyFlags extends OldDaysPropertySet{
    public OldDaysPropertyFlags(OldDaysModule m, int i, String f){
        super(m, i, createDefaultValueString(false), createDefaultValueString(true), f, createContentString());
    }

    public String[] getTooltip(){
        ArrayList<String> list = new ArrayList<String>();
        list.add(mod_OldDays.lang.get(getName()+".name"));
        list.add("");
        int num = mod_OldDays.getDescriptionNumber(getName()+".desc");
        for (int i = 0; i < num; i++){
            list.add("§7"+mod_OldDays.lang.get(getName()+".desc"+(i+1)));
        }
        for (int i = 0; i < ODNBXlite.FLAGS.length; i++){
            String str = "flag."+ODNBXlite.FLAGS[i]+".desc";
            list.add("<- §a"+ODNBXlite.FLAGS[i]+"§7: "+mod_OldDays.lang.get(str));
            int num2 = mod_OldDays.getDescriptionNumber(str);
            for (int j = 0; j < num2; j++){
                list.add("<-    §7"+mod_OldDays.lang.get(str+(j+1)));
            }
        }
        if (isDisabled()){
            if (num > 0){
                list.add("");
            }
            list.add("§4"+mod_OldDays.lang.get("gui.error"+getDisableReason()));
        }
        return list.toArray(new String[list.size()]);
    }

    @Override
    public String getValueButtonText(int i){
        StringBuilder b = new StringBuilder();
        b.append(mod_OldDays.lang.get("flag." + ODNBXlite.FLAGS[i]));
        b.append(": ");
        b.append(mod_OldDays.lang.get("gui." + (value[i] > 0 ? "on" : "off")));
        return b.toString();
    }

    private static String createDefaultValueString(boolean smp){
        StringBuilder b = new StringBuilder();
        for (String str : ODNBXlite.FLAGS){
            b.append(ODNBXlite.getDefaultFlag(str) ? 1 : 0);
            b.append(";");
        }
        return b.substring(0, b.length() - 1);
    }

    private static String createContentString(){
        StringBuilder b = new StringBuilder();
        for (String str : ODNBXlite.FLAGS){
            b.append("b;");
        }
        return b.substring(0, b.length() - 1);
    }
}