package net.minecraft.src;

import java.io.*;
import java.util.*;

public class SavingManager{
    public mod_OldDays core;

    public SavingManager(mod_OldDays c){
        core = c;
    }

    public void loadModuleProperties(int id){
        Properties properties = new Properties();
        OldDaysModule module = core.getModuleById(id);
        try{
            File dir = new File(mod_OldDays.getMinecraftInstance().getMinecraftDir()+"/olddays");
            dir.mkdirs();
            File file = new File(dir, module.name+".properties");
            if(file.createNewFile()){
                for (int i = 1; i <= module.properties.size(); i++){
                    core.sendCallback(id, i);
                }
            }
            properties.load(new FileInputStream(mod_OldDays.getMinecraftInstance().getMinecraftDir()+"/olddays/"+module.name+".properties"));
            for (int i = 1; i <= module.properties.size(); i++){
                OldDaysProperty prop = module.getPropertyById(i);
                try{
                    String value = properties.getProperty(prop.field.getName()).trim();
                    if (value==null){
                        prop.setDefaultValue();
                        core.sendCallback(id, i);
                        continue;
                    }
                    prop.loadFromString(value);
                    core.sendCallback(id, i);
                }catch(Exception ex){
                    try{
                        prop.setDefaultValue();
                        core.sendCallback(id, i);
                    }catch(Exception ex2){
                        System.out.println("OldDays: Error with loading property "+prop.field.getName()+" in module "+module.name);
                        System.out.println(ex2);
                    }
                }
            }
        }
        catch(Exception ex){
            System.out.println("OldDays: Failed to load properties for "+module.name+" module: "+ex);
        }
    }

    public void saveModuleProperties(int id){
        if (mod_OldDays.getMinecraftInstance().field_71441_e != null){
            if (mod_OldDays.getMinecraftInstance().field_71441_e.isRemote){
                return;
            }
        }
        Properties properties = new Properties();
        OldDaysModule module = core.getModuleById(id);
        try{
            File dir = new File(mod_OldDays.getMinecraftInstance().getMinecraftDir()+"/olddays");
            dir.mkdirs();
            File file = new File(dir, module.name+".properties");
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            for (int i = 1; i <= module.properties.size(); i++){
                OldDaysProperty prop = module.getPropertyById(i);
                properties.setProperty(prop.field.getName(), prop.saveToString());
            }
            properties.store(fileoutputstream, "Old Days config");
            fileoutputstream.close();
        }
        catch(Exception ex){
            System.out.println("OldDays: Failed to save properties for "+module.name+" module: "+ex);
        }
    }

    public void loadAll(){
        for (int i = 0; i < core.modules.size(); i++){
            loadModuleProperties(((OldDaysModule)core.modules.get(i)).id);
        }
    }

    public void saveAll(){
        for (int i = 0; i < core.modules.size(); i++){
            saveModuleProperties(((OldDaysModule)core.modules.get(i)).id);
        }
    }
}