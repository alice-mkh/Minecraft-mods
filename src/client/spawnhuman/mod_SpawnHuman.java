package net.minecraft.src;

import java.util.*;
import java.io.*;
import java.lang.reflect.Field;
import net.minecraft.client.Minecraft;

public class mod_SpawnHuman extends Mod{
    @Override
    public String getMcVersion(){
        return "1.5.2";
    }

    @Override
    public String getModVersion(){
        return "r2";
    }

    @Override
    public String getModName(){
        return "SpawnHuman";
    }

    public mod_SpawnHuman(){
        keySpawn = new KeyBinding("Spawn Human", 34);
        registerKey(keySpawn);
        setUseTick(true, false);
        RenderManager renderMan = RenderManager.instance;
        try{
            RenderHuman r = new RenderHuman(new ModelHuman(), 0.5F);
            r.setRenderManager(renderMan);
            HashMap map = ((HashMap)getField(RenderManager.class, renderMan, 0));
            map.put(EntityHuman.class, r);
            System.out.println("Added "+r.getClass().getName()+" renderer");
        }catch(Exception ex){
            System.out.println("Failed to add renderer: "+ex);
        }
        /*ModLoader.registerEntityID(EntityHuman.class, "human", ModLoader.getUniqueEntityId());*/
        Properties properties = new Properties();
        try{
            File dir = new File(Minecraft.getMinecraftDir() + "/config/");
            dir.mkdirs();
            File file = new File(dir, "SpawnHuman.properties");
            if(file.createNewFile()){
                FileOutputStream fileoutputstream = new FileOutputStream(file);
                properties.setProperty("CustomTexture",Boolean.toString(true));
                properties.setProperty("DeathEffect",Boolean.toString(true));
                properties.setProperty("SpawnEffect",Boolean.toString(true));
                properties.setProperty("Health",Integer.toString(1));
                properties.setProperty("AllowInSurvival",Boolean.toString(false));
                properties.store(fileoutputstream,"Spawn human config");
                fileoutputstream.close();
            }
            properties.load(new FileInputStream((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/SpawnHuman.properties").toString()));
            Health = Integer.parseInt(properties.getProperty("Health"));
            AllowInSurvival = Boolean.parseBoolean(properties.getProperty("AllowInSurvival"));
            SpawnEffect = Boolean.parseBoolean(properties.getProperty("SpawnEffect"));
            DeathEffect = Boolean.parseBoolean(properties.getProperty("DeathEffect"));
            CustomTexture = Boolean.parseBoolean(properties.getProperty("CustomTexture"));
        }
        catch(IOException ioexception){
            ioexception.printStackTrace();
        }
    }

    private void registerKey(KeyBinding key){
        GameSettings s = Minecraft.getMinecraft().gameSettings;
        KeyBinding[] newb = new KeyBinding[s.keyBindings.length + 1];
        for (int i = 0; i < s.keyBindings.length; i++){
            newb[i] = s.keyBindings[i];
        }
        newb[s.keyBindings.length] = key;
        s.keyBindings = newb;
        try{
            File optionsFile = new File(Minecraft.getMinecraftDir(), "options.txt");
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
    public void onTick(boolean worldExists){
        if (!worldExists){
            return;
        }
        if (keySpawn.isPressed()){
            Minecraft mc = Minecraft.getMinecraft();
            EntityPlayer entityplayer = mc.thePlayer;
            World theWorld = mc.theWorld;
            if(!theWorld.isRemote && (theWorld.worldInfo.areCommandsAllowed() || AllowInSurvival)){
                EntityLiving entityliving = new EntityHuman(theWorld);
                entityliving.setLocationAndAngles(entityplayer.posX, entityplayer.posY - entityplayer.yOffset + entityplayer.getEyeHeight(), entityplayer.posZ, theWorld.rand.nextFloat() * 360F, 0.0F);
                theWorld.spawnEntityInWorld(entityliving);
                if (SpawnEffect){
                    entityliving.spawnExplosionParticle();
                }
            }
        }
    }

    private Object getField(Class c, Object o, int num){
        try{
            Field f = c.getDeclaredFields()[num];
            f.setAccessible(true);
            return f.get(o);
        }catch(Exception ex){
            System.out.println(ex);
            return null;
        }
    }

    @Override
    public void load(){};

    private KeyBinding keySpawn;
    public static int Health;
    public static boolean AllowInSurvival;
    public static boolean SpawnEffect;
    public static boolean DeathEffect;
    public static boolean CustomTexture;
}