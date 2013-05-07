package net.minecraft.src.nbxlite;

import java.util.ArrayList;
import net.minecraft.src.*;

public class OldDaysPropertyFlags extends OldDaysPropertyString{
    public OldDaysPropertyFlags(OldDaysModule m, int i, String f){
        super(m, i, "", "", f);
    }

    @Override
    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name");
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
}