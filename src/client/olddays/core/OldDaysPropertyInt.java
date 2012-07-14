package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

public class OldDaysPropertyInt extends OldDaysProperty{
    public int value;
    public int smpValue;
    public int count;
    public boolean fromOne;
    private String[] names;
    public boolean useNames;

    public OldDaysPropertyInt(OldDaysModule m, int i, String s, int v, int c, String f){
        super(m, i, s, TYPE_INTEGER, f);
        value = v;
        guitype = GUI_TYPE_BUTTON;
        count = c;
        names = null;
        useNames = false;
        fromOne = false;
    }

    public OldDaysPropertyInt(OldDaysModule m, int i, String s, int v, int c, int smp, String f){
        this(m, i, s, v, c, f);
        smpValue = smp;
        allowedInSMP = false;
        fromOne = false;
    }

    public void setNames(String[] str){
        names = str;
        useNames = true;
    }

    public String getButtonText(){
        return mod_OldDays.lang.get(getName()+".name")+": "+(useNames ? mod_OldDays.lang.get(getName()+(value+1)) : value);
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
        if (value < (fromOne ? count : count - 1) || count <= (fromOne ? 1 : 0)){
            value++;
        }else{
            value = fromOne ? 1 : 0;
        }
    }

    public void setSMPValue(){
        value = smpValue;
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
        if (i < 0){
            i = 0;
        }
        if (count > 0 && i >= count){
            i = count - 1;
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
        if (useNames){
            for (int i = (fromOne ? 1 : 0); i < (fromOne ? count + 1 : count); i++){
                list.add("<- • §a"+mod_OldDays.lang.get(getName()+(i+1))+"§7: "+mod_OldDays.lang.get(getName()+(i+1)+".desc"));
            }
        }else{
            list.add("§7"+mod_OldDays.lang.get("gui.possible")+": "+(fromOne ? "1" : "0")+"-"+(fromOne ? count : count-1));
        }
        if (isDisabled()){
            if (num > 0 || (useNames || count > 0)){
                list.add("");
            }
            list.add("§4"+mod_OldDays.lang.get("gui.error"+getDisableReason()));
        }
        return (String[])list.toArray(new String[list.size()]);
    }
}