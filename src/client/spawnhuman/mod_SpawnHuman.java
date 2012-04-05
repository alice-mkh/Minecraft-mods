package net.minecraft.src;
import java.util.*;
import java.io.*;
import net.minecraft.client.Minecraft;

public class mod_SpawnHuman extends BaseMod{
    public String getVersion(){
        return "1.2.5";
    }

    public mod_SpawnHuman(){
        ModLoader.registerKey(this, this.KeySpawn, false);
        ModLoader.addLocalization("key_spawn", "Spawn Human");
        ModLoader.registerEntityID(EntityHuman.class, "human", ModLoader.getUniqueEntityId());
        SpawnHumanProperties properties = new SpawnHumanProperties();
        try{
            File file = new File((new StringBuilder()).append(Minecraft.getMinecraftDir()).append("/config/SpawnHuman.properties").toString());
            boolean flag = file.createNewFile();
            if(flag){
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

    public void keyboardEvent(KeyBinding keybinding){
        Minecraft mc = ModLoader.getMinecraftInstance();
        EntityPlayer entityplayer = mc.thePlayer;
        World theWorld = mc.theWorld;
        PlayerController playerController = mc.playerController;
        if(!theWorld.isRemote && (playerController.isInCreativeMode()||AllowInSurvival)){
            EntityLiving entityliving = (EntityLiving)EntityList.createEntityByName("human", theWorld);
            entityliving.setLocationAndAngles(entityplayer.posX, entityplayer.posY - 1.62D + entityplayer.getEyeHeight(), entityplayer.posZ, theWorld.rand.nextFloat() * 360F, 0.0F);
            theWorld.spawnEntityInWorld(entityliving);
            if (SpawnEffect){
                entityliving.spawnExplosionParticle();
            }
        }
    }

    public void addRenderer(Map map){   
        map.put(net.minecraft.src.EntityHuman.class, new RenderHuman(new ModelHuman(), 0.5F));
    }

    public void load(){};

    public KeyBinding KeySpawn = new KeyBinding("key_spawn", 34);
    public static int Health;
    public static boolean AllowInSurvival;
    public static boolean SpawnEffect;
    public static boolean DeathEffect;
    public static boolean CustomTexture;
}