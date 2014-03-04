package net.minecraft.src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import net.minecraft.src.ssp.Mod;
import net.minecraft.src.ssp.Packet300Custom;

public class mod_OldDays extends Mod implements ResourceManagerReloadListener{
    public mod_OldDays(){
        texman = new OldDaysTextureManager(this);
        saveman = new SavingManager(this);
        smpman = new SMPManager(this);
        modules = new ArrayList<OldDaysModule>();
        newlyDisabled = new ArrayList<OldDaysProperty>();
        lang = new OldDaysEasyLocalization("olddays");
        keyBindings = new ArrayList<KeyBinding>();
        resetOptionsForNextWorld = false;
    }

    @Override
    public String getModVersion(){
        return "4.1";
    }

    @Override
    public String getMcVersion(){
        return "1.6.4";
    }

    @Override
    public String getModName(){
        return "OldDays";
    }

    @Override
    public void load(){
        setUseTick(true, true);
        registerKey(keySettings = new KeyBinding(lang.get("OldDays Settings"), 35));
        loadModules();
        initModules();
        saveman.loadAll();
    }

    private void initModules(){
        for (OldDaysModule module : modules){
            module.replaceBlocks();
        }
        for (OldDaysModule module : modules){
            module.replaceTools();
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager par1ResourceManager){
        texman.changeResourcePack(par1ResourceManager);
    }

    @Override
    public boolean renderBlocks(RenderBlocks r, IBlockAccess i, Block b, int x, int y, int z, int id, Icon override){
        for (int j = 0; j < modules.size(); j++){
            if (modules.get(j).renderBlocks(r, i, b, x, y, z, id, override)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void onTick(boolean worldExists){
        for (OldDaysProperty prop : newlyDisabled){
            prop.module.last = -1;
            prop.onChange(false);
        }
        newlyDisabled.clear();
        if (!worldExists){
            return;
        }
        for (int i = 0; i < modules.size(); i++){
            modules.get(i).onTick();
        }
        for (int i = 0; i < keyBindings.size(); i++){
            KeyBinding key = keyBindings.get(i);
            if (key.isPressed()){
                if (key == keySettings && getMinecraft().currentScreen == null){
                    getMinecraft().displayGuiScreen(new GuiOldDaysModules(null, this));
                    continue;
                }
                for (int j = 0; j < modules.size(); j++){
                    modules.get(j).catchKeyEvent(key);
                }
            }
        }
    }

    @Override
    public void handlePacketFromClient(Packet300Custom packet, EntityPlayerMP player){
        if (packet.getId() == SMPManager.PACKET_C2S_PROP){
            OldDaysProperty prop = readProperty(packet.getData()[0]);
            sendPacketToAll(SMPManager.PACKET_S2C_PROP, prop.module.id+" "+prop.id+" "+prop.saveToString());
            System.out.println("Sent "+prop.getName()+" prop to all.");
            return;
        }
        if (packet.getId() == SMPManager.PACKET_C2S_REQUEST){
            int module = Integer.parseInt(packet.getData()[0]);
            if (module >= 0){
                String[] data = writeModule(getModuleById(module));
                sendPacketToPlayer(player, SMPManager.PACKET_S2C_MODULE, data);
                System.out.println("Sending module "+module+".");
            }else{
                for (int i = 0; i < modules.size(); i++){
                    String[] data = writeModule(modules.get(i));
                    sendPacketToPlayer(player, SMPManager.PACKET_S2C_MODULE, data);
                    System.out.println("Sending module "+i+".");
                }
            }
            return;
        }
        super.handlePacketFromClient(packet, player);
    }

    @Override
    public void handlePacketFromServer(Packet300Custom packet){
        if (packet.getId() == SMPManager.PACKET_S2C_PROP){
            OldDaysProperty prop = readProperty(packet.getData()[0]);
            System.out.println("Received "+prop.getName()+" prop: "+prop.saveToString()+".");
            return;
        }
        if (packet.getId() == SMPManager.PACKET_S2C_MODULE){
            String[] data = packet.getData();
            int id = Integer.parseInt(data[0]);
            int size = getModuleById(id).properties.size();
            for (int i = 1; i <= size; i++){
                readProperty(packet.getData()[i]);
            }
            if (data.length - size > 1){
                String[] data2 = new String[data.length - size - 1];
                for (int i = 0; i < data2.length; i++){
                    data2[i] = data[size + 1 + i];
                }
                getModuleById(id).readAdditionalPackageData(data2);
            }
            System.out.println("Received "+getModuleById(id).name+" module.");
            resetOptionsForNextWorld = true;
            return;
        }
        if (packet.getId() == SMPManager.PACKET_S2C_SEED){
            long seed = Long.parseLong(packet.getData()[0]);
            World world = Minecraft.getMinecraft().theWorld;
            world.getWorldInfo().setSeed(seed);
            world.provider.registerWorld(world);
            System.out.println("Received "+seed+" world seed.");
            return;
        }
        super.handlePacketFromServer(packet);
    }

    private String[] writeModule(OldDaysModule module){
        int length = 1 + module.properties.size();
        String[] data2 = module.getAdditionalPackageData();
        if (data2 != null){
            length += data2.length;
        }
        String[] data = new String[length];
        data[0] = ""+module.id;
        for (int i = 0; i < module.properties.size(); i++){
            OldDaysProperty prop = module.properties.get(i);
            data[1 + i] = prop.module.id+" "+prop.id+" "+prop.saveToString();
        }
        if (data2 != null){
            for (int i = 0; i < data2.length; i++){
                data[module.properties.size() + 1 + i] = data2[i];
            }
        }
        return data;
    }

    private OldDaysProperty readProperty(String str){
        String[] data = str.split(" ", 3);
        int module = Integer.parseInt(data[0]);
        int id = Integer.parseInt(data[1]);
        String value = data[2];
        OldDaysProperty prop = getModuleById(module).getPropertyById(id);
        prop.loadFromString(value);
        sendCallback(prop.module.id, prop.id);
        return prop;
    }

    @Override
    public void onLoginServer(EntityPlayerMP player){
        sendPacketToPlayer(player, SMPManager.PACKET_S2C_SEED, ""+player.worldObj.getSeed());
    }

    @Override
    public void onLoginClient(){
        if (!isVanillaSMP()){
            return;
        }
        for (int id = 0; id < modules.size(); id++){
            OldDaysModule module = modules.get(id);
            for (int i = 1; i <= module.properties.size(); i++){
                OldDaysProperty prop = module.getPropertyById(i);
                if (!module.isLocal){
                    prop.setSMPValue();
                    sendCallback2(id, i);
                }
            }
        }
    }

    @Override
    public void onInitClient(){
        sendPacketToServer(SMPManager.PACKET_C2S_REQUEST, ""+-1);
    }

    @Override
    public void onGUITick(GuiScreen gui){
        for (int i = 0; i < modules.size(); i++){
            modules.get(i).onGUITick(gui);
        }
    }

    public void loadModules(){
        Class c = net.minecraft.src.mod_OldDays.class;
        String p = "";
        try{
            p = c.getPackage().getName()+".";
        }catch(Exception ex){}
        String path = c.getProtectionDomain().getCodeSource().getLocation().getPath();
        try{
            path = URLDecoder.decode(path, "UTF-8");
        }catch(Exception e){}
        File file = new File(path+p.replace(".", "/"));
        ArrayList<String> classes = new ArrayList<String>();
        if (file.getName().endsWith(".zip") || file.getName().endsWith(".jar") && !file.isDirectory()){
            try{
                ZipFile jar = new ZipFile(file);
                Enumeration entries = jar.entries();
                while (entries.hasMoreElements()){
                    String str = ((ZipEntry)entries.nextElement()).getName();
                    if (str.startsWith("OD") && str.endsWith(".class")){
                        classes.add(str.replace(".class", ""));
                    }
                }
            }catch(Exception ex){
                System.out.println(ex);
            }
        }else{
            String[] str = file.list();
            for (int i = 0; i < str.length; i++){
                if (str[i].startsWith("OD") && str[i].endsWith(".class")){
                    classes.add(str[i].replace(".class", ""));
                }
            }
        }
        for (int i = 0; i < classes.size(); i++){
            String name = classes.get(i);
            Class c2 = null;
            try{
                c2 = c.getClassLoader().loadClass(p+name);
            }catch(Exception ex){
                System.out.println("OldDays: Failed to load module: "+ex);
                ex.printStackTrace();
                continue;
            }
            if (!((net.minecraft.src.OldDaysModule.class).isAssignableFrom(c2))){
                continue;
            }
            OldDaysModule module = null;
            try{
                module = ((OldDaysModule)c2.getDeclaredConstructor(c).newInstance(this));
            }catch(Exception ex){
                System.out.println("OldDays: Failed to load module: "+ex);
                ex.printStackTrace();
                continue;
            }
            modules.add(module);
            System.out.println("OldDays: Loaded "+module.name+" module");
        }
        Collections.sort(modules);
    }

    public static String getPropertyButtonText(OldDaysProperty prop){
        return prop.isDisabled() ? prop.getDisabledButtonText() : prop.getButtonText();
    }

    public static OldDaysModule getModuleById(int id){
        for (int i = 0; i < modules.size(); i++){
            OldDaysModule module = modules.get(i);
            if (module.id == id){
                return module;
            }
        }
        return null;
    }

    public static void sendCallback(int id, int id2){
        getModuleById(id).last = id2;
        getModuleById(id).getPropertyById(id2).onChange(true);
    }

    public static void sendCallback2(int id, int id2){
        modules.get(id).last = id2;
        modules.get(id).getPropertyById(id2).onChange(true);
    }

    public static void sendCallbackAndSave(int id, int id2){
        sendCallback(id, id2);
        saveman.saveModuleProperties(id);
    }

    public static Minecraft getMinecraft(){
        return Minecraft.getMinecraft();
    }

    public static int getDescriptionNumber(String s){
        boolean end = false;
        int i = 0;
        while (!end){
            i++;
            end = !isTranslated(s+i);
        }
        return i - 1;
    }

    public static boolean isTranslated(String s){
        return !s.equals(lang.get(s));
    }

    public static void refreshConditionProperties(){
        for (int i = 0; i < modules.size(); i++){
            OldDaysModule module = modules.get(i);
            for (int j = 0; j < module.properties.size(); j++){
                OldDaysProperty prop = module.properties.get(j);
                if (prop instanceof OldDaysPropertyCond || prop instanceof OldDaysPropertyCond2){
                    prop.onChange(true);
                }
            }
        }
    }

    public static Object getField(Class c, Object o, String str){
        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++){
            if (fields[i].getName().equals(str)){
                System.out.println("FIXME: Use \""+i+"\" instead of \""+str+"\"!");
                return getField(c, o, i);
            }
        }
        System.out.println("FIXME: No such field: \""+str+"\"!");
        return null;
    }

    public static Object getField(Class c, Object o, int num){
        try{
            Field f = c.getDeclaredFields()[num];
            f.setAccessible(true);
            return f.get(o);
        }catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    public static void setField(Class c, Object o, String str, Object val){
        Field[] fields = c.getDeclaredFields();
        for (int i = 0; i < fields.length; i++){
            if (fields[i].getName().equals(str)){
                System.out.println("FIXME: Use \""+i+"\" instead of \""+str+"\"!");
                setField(c, o, i,val);
                return;
            }
        }
        System.out.println("FIXME: No such field: \""+str+"\"!");
    }

    public static void setField(Class c, Object o, int num, Object val){
        try{
            Field f = c.getDeclaredFields()[num];
            f.setAccessible(true);
            Field modifiers = f.getClass().getDeclaredField("modifiers");
            modifiers.setAccessible(true);
            modifiers.setInt(f, f.getModifiers() & ~Modifier.FINAL);
            f.set(o, val);
        }catch(Exception ex){
            System.out.println(ex);
        }
    }

    public static void registerKey(KeyBinding key){
        GameSettings s = getMinecraft().gameSettings;
        KeyBinding[] newb = new KeyBinding[s.keyBindings.length + 1];
        for (int i = 0; i < s.keyBindings.length; i++){
            newb[i] = s.keyBindings[i];
        }
        newb[s.keyBindings.length] = key;
        s.keyBindings = newb;
        keyBindings.add(key);
        try{
            File optionsFile = new File(getMinecraft().mcDataDir, "options.txt");
            if (!optionsFile.exists()){
                return;
            }
            BufferedReader bufferedreader = new BufferedReader(new FileReader(optionsFile));
            for (String str = ""; (str = bufferedreader.readLine()) != null;){
                try{
                    String as[] = str.split(":");
                    if (as[0].equals((new StringBuilder()).append("key_").append(key.keyDescription).toString())){
                        key.keyCode = Integer.parseInt(as[1]);
                    }
                }catch (Exception exception1){
                    System.out.println((new StringBuilder()).append("Skipping bad option: ").append(s).toString());
                }
            }
            bufferedreader.close();
        }
        catch (Exception exception){
            System.out.println("Failed to load options");
            exception.printStackTrace();
        }
        KeyBinding.resetKeyBindingArrayAndHash();
    }

    @Override
    public void onLoadingSP(String par1Str, String par2Str){
        for (int i = 0; i < modules.size(); i++){
            OldDaysModule module = modules.get(i);
            module.onLoadingSP(par1Str, par2Str);
        }
        if (resetOptionsForNextWorld){
            saveman.loadAll();
            resetOptionsForNextWorld = false;
        }
    }

    public static boolean isVanillaSMP(){
        return getMinecraft().isMultiplayerWorld() && !getMinecraft().isSingleplayer();
    }

    public boolean unpackSound(String dir, String name){
        try{
            File file = new File(getMinecraft().getAssetsDir(), dir+"/"+name);
            if (file.exists()){
                return true;
            }
            InputStream str = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("olddays/sounds/"+name)).getInputStream();
            DataInputStream stream = new DataInputStream(str);
            byte[] bytes = new byte[stream.available()];
            stream.readFully(bytes);
            stream.close();
            (new File(getMinecraft().getAssetsDir(), dir)).mkdirs();
            FileOutputStream stream2 = new FileOutputStream(file);
            stream2.write(bytes);
            stream2.close();
            System.out.println("OldDays: Unpacked "+name+" sound");
            return true;
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public void refreshTextures(){
        for (int i = 0; i < modules.size(); i++){
            OldDaysModule module = modules.get(i);
            module.refreshTextures();
        }
        texman.refreshTextureHooks();
    }

    @Override
    public void updateTextures(){
        texman.updateTextureFXes();
    }

    @Override
    public void onInitPlayer(EntityClientPlayerMP player, GameSettings settings){
        for (int i = 0; i < modules.size(); i++){
            OldDaysModule module = modules.get(i);
            module.onInitPlayer(player, settings);
        }
    }

    public KeyBinding keySettings;
    public static OldDaysTextureManager texman;
    public static SavingManager saveman;
    public static SMPManager smpman;
    public static ArrayList<OldDaysModule> modules;
    public static OldDaysEasyLocalization lang;
    public static ArrayList<KeyBinding> keyBindings;
    public static boolean resetOptionsForNextWorld;
    public static ArrayList<OldDaysProperty> newlyDisabled;
}
