package net.minecraft.src;

import java.io.*;
import java.util.*;
import java.util.zip.*;
import net.minecraft.client.Minecraft;

public class SavingManager{
    public mod_OldDays core;

    public SavingManager(mod_OldDays c){
        core = c;
    }

    public void loadCoreProperties(){
        Properties properties = new Properties();
        Minecraft mc = Minecraft.getMinecraft();
        try{
            File dir = new File(mod_OldDays.getMinecraft().getMinecraftDir()+"/olddays");
            dir.mkdirs();
            File file = new File(dir, "Core.properties");
            if(file.createNewFile()){
                mc.useSP = true;
                saveCoreProperties();
                return;
            }
            properties.load(new FileInputStream(mod_OldDays.getMinecraft().getMinecraftDir()+"/olddays/Core.properties"));
            try{
                String value = properties.getProperty("ssp");
                mc.useSP = value.matches("^*([Oo][Nn]|[Tt][Rr][Uu][Ee]?|[Yy][Ee]?[SsPpAa]?[Hh]?)*$");
            }catch(Exception ex){
                mc.useSP = false;
            }
        }
        catch(Exception ex){
            System.out.println("OldDays: Failed to load properties for core: "+ex);
        }
    }

    public void saveCoreProperties(){
        Properties properties = new Properties();
        Minecraft mc = Minecraft.getMinecraft();
        try{
            File dir = new File(mod_OldDays.getMinecraft().getMinecraftDir()+"/olddays");
            dir.mkdirs();
            File file = new File(dir, "Core.properties");
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            properties.setProperty("ssp", ""+mc.useSP);
            properties.store(fileoutputstream, "Old Days config");
            fileoutputstream.close();
        }
        catch(Exception ex){
            System.out.println("OldDays: Failed to save properties for core: "+ex);
        }
    }

    public void loadModuleProperties(int id){
        Properties properties = new Properties();
        OldDaysModule module = core.getModuleById(id);
        try{
            File dir = new File(mod_OldDays.getMinecraft().getMinecraftDir()+"/olddays");
            dir.mkdirs();
            File file = new File(dir, module.name+".properties");
            if(file.createNewFile()){
                for (int i = 1; i <= module.properties.size(); i++){
                    core.sendCallback(id, i);
                }
            }
            properties.load(new FileInputStream(mod_OldDays.getMinecraft().getMinecraftDir()+"/olddays/"+module.name+".properties"));
            for (int i = 1; i <= module.properties.size(); i++){
                OldDaysProperty prop = module.getPropertyById(i);
                if (!prop.canBeLoaded){
                    continue;
                }
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
        OldDaysModule module = core.getModuleById(id);
        if (mod_OldDays.isVanillaSMP() && !module.isLocal){
            return;
        }
        Properties properties = new Properties();
        try{
            File dir = new File(mod_OldDays.getMinecraft().getMinecraftDir()+"/olddays");
            dir.mkdirs();
            File file = new File(dir, module.name+".properties");
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            for (int i = 1; i <= module.properties.size(); i++){
                OldDaysProperty prop = module.getPropertyById(i);
                properties.setProperty(prop.field.getName(), prop.saveToString());
            }
            properties.store(fileoutputstream, "OldDays config");
            fileoutputstream.close();
        }
        catch(Exception ex){
            System.out.println("OldDays: Failed to save properties for "+module.name+" module: "+ex);
        }
    }

    public void loadAll(){
        loadCoreProperties();
        for (int i = 0; i < core.modules.size(); i++){
            loadModuleProperties(core.modules.get(i).id);
        }
    }

    public void saveAll(){
        for (int i = 0; i < core.modules.size(); i++){
            saveModuleProperties(core.modules.get(i).id);
        }
    }

    public void loadPreset(String name, boolean custom){
        Properties properties = new Properties();
        try{
            if (custom){
                File dir = new File(mod_OldDays.getMinecraft().getMinecraftDir()+"/olddays/presets");
                dir.mkdirs();
                properties.load(new FileInputStream(new File(dir, name)));
            }else{
                InputStream stream = getClass().getClassLoader().getResourceAsStream("olddays/presets/"+name);
                properties.load(stream);
            }
            for (int i = 0; i < core.modules.size(); i++){
                OldDaysModule module = core.modules.get(i);
                for (int j = 1; j <= module.properties.size(); j++){
                    OldDaysProperty prop = module.getPropertyById(j);
                    String propname = prop.module.name+"."+prop.field.getName();
                    if (!prop.canBeLoaded){
                        continue;
                    }
                    String value = properties.getProperty(propname, prop.getDefaultValue()).trim();
                    prop.loadFromString(value);
                    core.sendCallback2(i, j);
                }
                saveModuleProperties(i);
            }
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("OldDays: Failed to load preset");
        }
    }

    public void savePreset(String name){
        try{
            File dir = new File(mod_OldDays.getMinecraft().getMinecraftDir()+"/olddays/presets");
            dir.mkdirs();
            FileWriter filewriter = new FileWriter(new File(dir, name));
            BufferedWriter writer = new BufferedWriter(filewriter);
            for (int i = 0; i < core.modules.size(); i++){
                OldDaysModule module = core.modules.get(i);
                for (int j = 1; j <= module.properties.size(); j++){
                    OldDaysProperty prop = module.getPropertyById(j);
                    String propname = prop.module.name+"."+prop.field.getName();
                    if (!prop.canBeLoaded || prop.saveToString().equals(prop.getDefaultValue())){
                        continue;
                    }
                    writer.write(propname+"="+prop.saveToString());
                    writer.newLine();
                }
            }
            writer.close();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("OldDays: Failed to load preset");
        }
    }

    public void deletePreset(String name){
        try{
            File dir = new File(mod_OldDays.getMinecraft().getMinecraftDir()+"/olddays/presets");
            (new File(dir, name)).delete();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("OldDays: Failed to delete preset");
        }
    }

    public String[] getDefaultPresets(){
        ArrayList<String> presets = new ArrayList<String>();
        try{
            String str = "";
            try{
                str = getClass().getResource("/olddays/presets").toString();
                str = str.substring(9, str.lastIndexOf('!'));
            }catch(Exception e2){
                str = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
                str = str.substring(5, str.length());
            }
            str = str.replace("%20", " ").replace("%23", "#");
            ZipFile jar = new ZipFile(str);
            Enumeration<? extends ZipEntry> entries = jar.entries();
            while (entries.hasMoreElements()){
                ZipEntry entry = entries.nextElement();
                String name = entry.toString();
                if (!name.startsWith("olddays/presets/") || name.endsWith("olddays/presets/")){
                    continue;
                }
                presets.add(name.replace("olddays/presets/", ""));
            }
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("OldDays: Failed to get list of default presets");
        }
        return presets.toArray(new String[presets.size()]);
    }

    public String[] getCustomPresets(){
        File dir = new File(mod_OldDays.getMinecraft().getMinecraftDir()+"/olddays/presets");
        String[] str = dir.list();
        if (str == null){
            str = new String[]{};
        }
        return str;
    }
}