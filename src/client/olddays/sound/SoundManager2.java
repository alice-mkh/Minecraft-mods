package net.minecraft.src;

import java.lang.reflect.Field;
import java.io.File;
import java.util.Random;
import paulscode.sound.SoundSystem;

public class SoundManager2 extends SoundManager{
    public static boolean explode = false;
    public static boolean xporb = false;
    public static int fall = 2;
    public static int hurt = 2;
    public static boolean door = false;
    public static int chest = 2;
    public static boolean bow = false;
    public static boolean drr = false;
    public static boolean eat = false;
    public static boolean drink = false;
    public static boolean breaking = false;
    public static int lava = 2;
    public static int enderman = 2;
    public static boolean calm4 = false;
    public static boolean creeper = false;
    public static boolean steps = false;
    public static boolean cow = false;
    public static boolean slime = false;
    public static boolean skeleton = false;
    public static boolean levelup = true;
    public static boolean ignite = false;
    public static boolean shear = true;
    public static boolean splash = false;
    public static boolean swimming = true;
    public static boolean minecart = true;

    public SoundManager2(ResourceManager m, GameSettings g, File f){
        super(m, g, f);
    }

    @Override
    public void playSound(String par1Str, float par2, float par3, float par4, float par5, float par6){
        par1Str = oldSounds(par1Str);
        if (par1Str == "nothing")
        {
            return;
        }
        super.playSound(par1Str, par2, par3, par4, par5, par6);
    }

    @Override
    public void playEntitySound(String par1Str, Entity par2Entity, float par3, float par4, boolean par5){
        par1Str = oldSounds(par1Str);
        if (par1Str == "nothing")
        {
            return;
        }
        super.playEntitySound(par1Str, par2Entity, par3, par4, par5);
    
    }

    private String oldSounds(String par1Str){
        if (par1Str==null){
            return "nothing";
        }
        String str = par1Str;
        if (par1Str.startsWith("random.explode") && explode){
            str = "olddays.explode";
        }
        if (par1Str.startsWith("damage.hit") && hurt < 2){
            if (hurt == 1){
                return "olddays.hurtflesh";
            }
            str = "random.classic_hurt";
        }
        if (par1Str.startsWith("damage.fall") && fall < 2){
            if (fall == 1){
                return "olddays.fall" + par1Str.substring(11);
            }
            return "nothing";
        }
        if (par1Str.startsWith("random.bowhit") && drr){
            str = "olddays.drr";
        }
        if (par1Str.equals("random.bow") && bow){
            str = "olddays.bow";
        }
        if (par1Str.startsWith("random.orb") && xporb){
            str = "random.pop";
        }
        if (par1Str.startsWith("random.levelup") && !levelup){
            return "nothing";
        }
        if ((par1Str.startsWith("random.burp") || par1Str.startsWith("random.eat")) && eat){
            return "nothing";
        }
        if (par1Str.startsWith("random.drink") && drink){
            return "nothing";
        }
        if (par1Str.startsWith("random.break") && breaking){
            return "nothing";
        }
        if (par1Str.startsWith("liquid.lava")){
            if (lava == 0){
                return "nothing";
            }
            if (!par1Str.endsWith("lava")){
                str = par1Str;
            }else{
                str = (lava == 1) ? "olddays.lava" : "liquid.lava";
            }
        }
        if (par1Str.startsWith("mob.endermen.") && enderman<2){
            if (enderman<1){
                return "nothing";
            }
            if (par1Str.endsWith("death")){
                str = "mob.zombie.death";
            }
            if (par1Str.endsWith("hit") || par1Str.endsWith("scream")){
                str = "mob.zombie.hurt";
            }
            if (par1Str.endsWith("idle")){
                str = "mob.zombie.say";
            }
            if (par1Str.endsWith("portal") || par1Str.endsWith("stare")){
                return "nothing";
            }
        }
        if (par1Str.startsWith("mob.creeper.say") && creeper){
            str = "olddays.creeper";
        }
        if (par1Str.startsWith("random.door_open") && door){
            str = "olddays.door_open";
        }
        if (par1Str.startsWith("random.door_close") && door){
            str = "olddays.door_close";
        }
        if (par1Str.startsWith("random.chestopen") && chest<2){
            if (chest<1){
                return "nothing";
            }
            str = "olddays.door_open";
        }
        if (par1Str.startsWith("random.chestclosed") && chest<2){
            if (chest<1){
                return "nothing";
            }
            str = "olddays.door_close";
        }
        if (par1Str.startsWith("dig.sand") && steps){
            return "dig.gravel";
        }
        if (par1Str.startsWith("step.") && steps){
            if (par1Str.endsWith(".ladder")){
                return "nothing";
            }
            str = "dig."+par1Str.substring(5);
        }
        if ((par1Str.startsWith("step.snow") || par1Str.startsWith("dig.snow")) && steps){
            return "step.cloth";
        }
        if (par1Str.startsWith("mob.cow.say") && cow){
            str = "olddays.cow";
        }
        if (par1Str.startsWith("mob.cow.hurt") && cow){
            str = "olddays.cowhurt";
        }
        if (par1Str.startsWith("mob.slime") && slime){
            str = "mob.slime.small";
        }
        if ((par1Str.startsWith("mob.skeleton.hurt") || par1Str.startsWith("mob.skeleton.death")) && skeleton){
            str = "olddays.skeletonhurt";
        }
        if (par1Str.startsWith("fire.ignite") && ignite){
            str = "olddays.ignite";
        }
        if (par1Str.startsWith("mob.sheep.shear") && !shear){
            return "nothing";
        }
        if (par1Str.startsWith("liquid.splash") && splash){
            str = "olddays.splash";
        }
        if (par1Str.startsWith("liquid.swim") && !swimming){
            return "nothing";
        }
        if (par1Str.startsWith("minecart") && !minecart){
            return "nothing";
        }
        return str;
    }

    /**
     * If its time to play new music it starts it up.
     */
    @Override
    public void playRandomMusicIfReady()
    {
        GameSettings options = (GameSettings)getField(7);

        if (!(Boolean)getField(2) || options.musicVolume == 0.0F)
        {
            return;
        }

        SoundSystem sndSystem = (SoundSystem)getField(1);
        if (!sndSystem.playing("BgMusic") && !sndSystem.playing("streaming"))
        {
            int ticksBeforeMusic = (Integer)getField(12);
            if (ticksBeforeMusic > 0)
            {
                setField(12, --ticksBeforeMusic);
                return;
            }

            SoundPoolEntry soundpoolentry = ((SoundPool)getField(5)).getRandomSound();
            if (soundpoolentry.getSoundName().startsWith("calm4") && !calm4){
                return;
            }

            if (soundpoolentry != null)
            {
                setField(12, ((Random)getField(11)).nextInt(12000) + 12000);
                sndSystem.backgroundMusic("BgMusic", soundpoolentry.getSoundUrl(), soundpoolentry.getSoundName(), false);
                sndSystem.setVolume("BgMusic", options.musicVolume);
                sndSystem.play("BgMusic");
            }
        }
    }

    public Object getField(int num){
        try{
            Field f = (SoundManager.class).getDeclaredFields()[num];
            f.setAccessible(true);
            return f.get(this);
        }catch(Exception ex){
            System.out.println("Exception in SoundManager2!");
            System.out.println(ex);
            return null;
        }
    }

    public void setField(int num, int newVal){
        try{
            Field f = (SoundManager.class).getDeclaredFields()[num];
            f.setAccessible(true);
            f.set(this, newVal);
        }catch(Exception ex){
            System.out.println("Exception in SoundManager2!");
            System.out.println(ex);
        }
    }
}