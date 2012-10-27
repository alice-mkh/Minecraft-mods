package net.minecraft.src;

import java.lang.reflect.Field;
import java.util.Random;
import paulscode.sound.SoundSystem;

public class SoundManager2 extends SoundManager{
    public static boolean explode = false;
    public static boolean xporb = false;
    public static boolean nofall = true;
    public static boolean hurt = false;
    public static boolean door = false;
    public static int chest = 2;
    public static boolean bow = false;
    public static boolean drr = false;
    public static boolean eat = false;
    public static boolean drink = false;
    public static boolean breaking = false;
    public static boolean lava = false;
    public static int enderman = 2;
    public static boolean calm4 = false;
    public static boolean creeper = false;
    public static boolean steps = false;
    public static boolean cow = false;
    public static boolean slime = false;
    public static boolean skeleton = false;
    public static boolean levelup = false;
    public static boolean ignite = false;

    public void playSound(String par1Str, float par2, float par3, float par4, float par5, float par6){
        par1Str = oldSounds(par1Str);
        if (par1Str == "nothing")
        {
            return;
        }
        super.playSound(par1Str, par2, par3, par4, par5, par6);
    }

    private String oldSounds(String par1Str){
        if (par1Str==null){
            return "nothing";
        }
        String str = par1Str;
        if (par1Str.startsWith("random.explode") && explode){
            str = "olddays.explode";
        }
        if (par1Str.startsWith("damage.hurt") && hurt){
            str = "random.classic_hurt";
        }
        if (par1Str.startsWith("damage.fall") && nofall){
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
        if (par1Str.startsWith("random.levelup") && levelup){
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
        if (par1Str.startsWith("liquid.lava") && lava){
            return "nothing";
        }
        if (par1Str.startsWith("mob.endermen.") && enderman<2){
            if (enderman<1){
                return "nothing";
            }
            if (par1Str.endsWith("death")){
                str = "mob.zombiedeath";
            }
            if (par1Str.endsWith("hit")){
                str = "mob.zombiehurt";
            }
            if (par1Str.endsWith("idle")){
                str = "mob.zombie";
            }
            if (par1Str.endsWith("portal")){
                return "nothing";
            }
        }
        if (par1Str.equals("mob.creeper") && creeper){
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
        if (par1Str.startsWith("step.") && steps){
            if (par1Str.endsWith(".ladder")){
                return "nothing";
            }
            str = "dig."+par1Str.substring(5);
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
        if ((par1Str.startsWith("mob.skeleton.hurt") || par1Str.startsWith("mob.skeleton.death")) && slime){
            str = "olddays.skeletonhurt";
        }
        if (par1Str.startsWith("fire.ignite") && ignite){
            str = "olddays.ignite";
        }
        return str;
    }

    /**
     * If its time to play new music it starts it up.
     */
    public void playRandomMusicIfReady()
    {
        GameSettings options = (GameSettings)getField(5);

        if (!(Boolean)getField(7) || options.musicVolume == 0.0F)
        {
            return;
        }

        SoundSystem sndSystem = (SoundSystem)getField(0);
        int ticksBeforeMusic = (Integer)getField(9);
        if (!sndSystem.playing("BgMusic") && !sndSystem.playing("streaming"))
        {
            if (ticksBeforeMusic > 0)
            {
                ticksBeforeMusic--;
                return;
            }

            SoundPoolEntry soundpoolentry = ((SoundPool)getField(3)).getRandomSound();
            if (soundpoolentry.soundName.startsWith("calm4") && !calm4){
                return;
            }

            if (soundpoolentry != null)
            {
                ticksBeforeMusic = ((Random)getField(8)).nextInt(12000) + 12000;
                sndSystem.backgroundMusic("BgMusic", soundpoolentry.soundUrl, soundpoolentry.soundName, false);
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
}