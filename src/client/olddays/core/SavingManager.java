package net.minecraft.src;

import java.io.*;
import java.util.*;
import java.util.zip.*;

public class SavingManager{
    public mod_OldDays core;
    public static boolean highlight = true;

    public SavingManager(mod_OldDays c){
        core = c;
    }

    public void loadCoreProperties(){
        Properties properties = new Properties();
        Minecraft mc = Minecraft.getMinecraft();
        try{
            File dir = new File(mc.mcDataDir+"/olddays");
            if (dir.mkdirs()){
                highlight = false;
            }
            File file = new File(dir, "Core.properties");
            if(file.createNewFile()){
                mc.useSP = true;
                saveCoreProperties();
                return;
            }
            properties.load(new FileInputStream(mc.mcDataDir+"/olddays/Core.properties"));
            try{
                String value = properties.getProperty("ssp");
                mc.useSP = value.matches("^*([Oo][Nn]|[Tt][Rr][Uu][Ee]?|[Yy][Ee]?[SsPpAa]?[Hh]?)*$");
            }catch(Exception ex){
                mc.useSP = true;
            }
            try{
                String value = properties.getProperty("indevShapeSize");
                mc.indevShapeSize = value.matches("^*([Oo][Nn]|[Tt][Rr][Uu][Ee]?|[Yy][Ee]?[SsPpAa]?[Hh]?)*$");
            }catch(Exception ex){
                mc.indevShapeSize = false;
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
            File dir = new File(mc.mcDataDir+"/olddays");
            dir.mkdirs();
            File file = new File(dir, "Core.properties");
            FileOutputStream fileoutputstream = new FileOutputStream(file);
            properties.setProperty("ssp", ""+mc.useSP);
            properties.setProperty("indevShapeSize", ""+mc.indevShapeSize);
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
            File dir = new File(mod_OldDays.getMinecraft().mcDataDir+"/olddays");
            if (dir.mkdirs()){
                highlight = false;
            }
            File file = new File(dir, module.name+".properties");
            if(file.createNewFile()){
                for (int i = 1; i <= module.properties.size(); i++){
                    module.getPropertyById(i).highlight = highlight;
                    module.highlight = highlight;
                    core.sendCallback(id, i);
                }
                saveModuleProperties(id);
                return;
            }
            properties.load(new FileInputStream(mod_OldDays.getMinecraft().mcDataDir+"/olddays/"+module.name+".properties"));
            for (int i = 1; i <= module.properties.size(); i++){
                OldDaysProperty prop = module.getPropertyById(i);
                if (!prop.canBeLoaded){
                    continue;
                }
                try{
                    String value = properties.getProperty(prop.field.getName()).trim();
                    if (value==null){
                        prop.setDefaultValue();
                        prop.highlight = highlight;
                        prop.module.highlight = highlight;
                        core.sendCallback(id, i);
                        continue;
                    }
                    prop.loadFromString(value);
                    core.sendCallback(id, i);
                }catch(Exception ex){
                    try{
                        prop.setDefaultValue();
                        prop.highlight = highlight;
                        prop.module.highlight = highlight;
                        core.sendCallback(id, i);
                    }catch(Exception ex2){
                        System.out.println("OldDays: Error with loading property "+prop.field.getName()+" in module "+module.name);
                        ex2.printStackTrace();
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
            File dir = new File(mod_OldDays.getMinecraft().mcDataDir+"/olddays");
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
            int mode = 0;
            if (custom){
                File dir = new File(mod_OldDays.getMinecraft().mcDataDir+"/olddays/presets");
                dir.mkdirs();
                properties.load(new FileInputStream(new File(dir, name)));
            }else{
                if (name.equals("OldDays")){
                    mode = 1;
                }else if (name.equals("Vanilla")){
                    mode = 2;
                }else{
                    InputStream stream = getClass().getClassLoader().getResourceAsStream("olddays/presets/"+name);
                    properties.load(stream);
                }
            }
            for (int i = 0; i < core.modules.size(); i++){
                OldDaysModule module = core.modules.get(i);
                for (int j = 1; j <= module.properties.size(); j++){
                    OldDaysProperty prop = module.getPropertyById(j);
                    String propname = prop.module.name+"."+prop.field.getName();
                    if (!prop.canBeLoaded){
                        continue;
                    }
                    String oldVal = prop.saveToString();
                    switch(mode){
                        case 0: String value = properties.getProperty(propname, prop.getDefaultValue()).trim();
                                prop.loadFromString(value); break;
                        case 1: prop.setDefaultValue(); break;
                        case 2: prop.setSMPValue(); break;
                    }
                    if (!oldVal.equals(prop.saveToString())){
                        core.sendCallback2(i, j);
                    }
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
            File dir = new File(mod_OldDays.getMinecraft().mcDataDir+"/olddays/presets");
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
            System.out.println("OldDays: Failed to save preset");
        }
    }

    public void deletePreset(String name){
        try{
            File dir = new File(mod_OldDays.getMinecraft().mcDataDir+"/olddays/presets");
            (new File(dir, name)).delete();
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("OldDays: Failed to delete preset");
        }
    }

    public String[] getDefaultPresets(){
        ArrayList<String> presets = new ArrayList<String>();
        presets.add("OldDays");
        presets.add("Vanilla");
        try{
            String str = "";
            try{
                str = getClass().getResource("/olddays/presets").toString();
                str = str.substring(9, str.lastIndexOf('!'));
            }catch(Exception e2){
                str = getClass().getProtectionDomain().getCodeSource().getLocation().toString();
                str = str.substring(5);
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
        File dir = new File(mod_OldDays.getMinecraft().mcDataDir+"/olddays/presets");
        String[] str = dir.list();
        if (str == null){
            str = new String[]{};
        }
        return str;
    }
}