package net.minecraft.src;

//import com.sijobe.spc.hook.Player;
//import com.sijobe.spc.util.DynamicClassLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextPane;

import org.lwjgl.opengl.Display;

import net.minecraft.src.ssp.EntityPlayerSP2;

public class EntityPlayerSPSPC extends EntityPlayerSP2{
    public PlayerHelper ph;
    public boolean multiplayer;
    public boolean phexists;
    public static Object MESSAGESHOWN;
    public static Object STARTUP;
    public String curmcversion;
    public static final String MCVERSION = "1.6.4";
    public static final SPCVersion SPCVERSION = new SPCVersion("Single Player Commands","3.3",new Date(1333630063890L)); // 2012-04-05 22:47:43
    public Vector<String> missingRequiredClasses;
    public Vector<String> missingOptionalClasses;

//    public Player player;

    public EntityPlayerSPSPC(Minecraft par1Minecraft, World par2World, Session par3Session, int par4){
        super(par1Minecraft, par2World, par3Session, par4);

        initPlayerHelper(par3Session);
        phexists = true;

//        DynamicClassLoader.populateClassLoaderWithClasses();
//        player = new Player();
    }

    /**
     * Tries to moves the entity by the passed in displacement. Args: x, y, z
     */
    @Override
    public void moveEntity(double par1, double par3, double par5)
    {
//        player.movePlayer(par1, par3, par5);
        if (canRunSPC() && ph.moveplayer && !ph.movecamera && mc.renderViewEntity instanceof SPCEntityCamera) {
            ((SPCEntityCamera)mc.renderViewEntity).setCamera(0, 0, 0,ph.freezecamyaw, ph.freezecampitch);
        } else if (canRunSPC() && ph.noClip) {
            posX += par1;
            posY += par3;
            posZ += par5;
            return;
        } else if (canRunSPC() && mc.renderViewEntity instanceof SPCEntityCamera) {
            ((SPCEntityCamera)mc.renderViewEntity).setCamera(par1, par3, par5,rotationYaw, rotationPitch);
            return;
        }
        super.moveEntity(par1, par3, par5);
    }

    @Override
    public void swingItem(){
        ph.handleSwing();
        super.swingItem();
    }

    @Override
    public void onLivingUpdate(){
        if (canRunSPC() && ph.sprinting) {
            setSprinting(true);
        }
        if (canRunSPC() && ph.flying && ph.flymode.equalsIgnoreCase("minecraft") && !capabilities.isFlying) { // SPC_FLYMODE_MINECRAFT
            if(movementInput.sneak)
            {
                motionY -= 0.14999999999999999D * 2;
            }
            if(movementInput.jump)
            {
                motionY += 0.14999999999999999D * 2;
            }
         }
        super.onLivingUpdate();
    }

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeEntityToNBT(par1NBTTagCompound);
        if (canRunSPC()) {
            ph.writeWaypointsToNBT(((SaveHandler) this.mc.theWorld.saveHandler).getWorldDirectory());
        }
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readEntityFromNBT(par1NBTTagCompound);
        if (canRunSPC()) {
            ph.readWaypointsFromNBT(((SaveHandler) this.mc.theWorld.saveHandler).getWorldDirectory());
        }
    }

    @Override
    public boolean isEntityInsideOpaqueBlock() {
        if (canRunSPC() && ph.noClip) {
            return false;
        }
        return super.isEntityInsideOpaqueBlock();
    }

    @Override
    protected String getHurtSound() {
        if (multiplayer || (canRunSPC() && ph.damage)) {
            return super.getHurtSound();
        } else {
            return "";
        }
    }

    @Override
    public float getCurrentPlayerStrVsBlock(Block block, boolean par2) {
        if (canRunSPC() && ph.instant) {
            return Float.MAX_VALUE;
        }
        return super.getCurrentPlayerStrVsBlock(block, par2);
    }

    @Override
    public boolean canHarvestBlock(Block block) {
        if (canRunSPC() && ph.instant) {
            return true;
        }
        return super.canHarvestBlock(block);
    }

    @Override
    protected void fall(float f) {
        if (canRunSPC() && !ph.falldamage) {
            return;
        }
        super.fall(f);
    }

    @Override
    public void addExhaustion(float f) {
        if (canRunSPC() && (ph.flying || ph.disableHunger)) {
            return;
        }
        super.addExhaustion(f);
    }

    @Override
    protected void jump() {
        if (canRunSPC() && ph.gravity > 1.0D) {
            this.motionY = (0.4199999868869782D * ph.gravity);
            return;
        }
        super.jump();
    }

    @Override
    public void moveFlying(float f, float f1, float f2) {
        if (!canRunSPC() || ph.speed <= 1.0F) {
            super.moveFlying(f, f1, f2);
            return;
        }
        float f3 = MathHelper.sqrt_float(f * f + f1 * f1);
        if (f3 < 0.01F) {
            return;
        }
        if (f3 < 1.0F) {
            f3 = 1.0F;
        }
        f3 = f2 / f3;
        f *= f3;
        f1 *= f3;
        float f4 = MathHelper.sin(this.rotationYaw * 3.141593F / 180.0F);
        float f5 = MathHelper.cos(this.rotationYaw * 3.141593F / 180.0F);
        double speed = ((canRunSPC()) ? ph.speed : 1);
        this.motionX += (f * f5 - f1 * f4) * speed;
        this.motionZ += (f1 * f5 + f * f4) * speed;
    }

    @Override
    public void onUpdate() {
//        player.onTick();
        if (canRunSPC()) {
            ph.beforeUpdate();
            super.onUpdate();
            ph.afterUpdate();
        } else {
            super.onUpdate();
        }
    }

    @Override
    protected void damageEntity(DamageSource d, float f) {
        if (canRunSPC() && !ph.damage) {
            return;
        }
        super.damageEntity(d, f);
    }

    @Override
    public void setDead() {
        if (canRunSPC()) {
            ph.setCurrentPosition();
        }
        super.setDead();
    }

    @Override
    public double getDistanceSqToEntity(Entity entity) {
        if (canRunSPC() && (!ph.mobdamage || ph.mobsfrozen)) {
            return Double.MAX_VALUE;
        }
        return super.getDistanceSqToEntity(entity);
    }

    @Override
    public void onDeath(DamageSource entity) {
        if (canRunSPC() && ph.keepitems && PlayerHelper.INV_BEFORE_DEATH != null) {

            for (int j = 0; j < inventory.armorInventory.length; j++) {
                PlayerHelper.INV_BEFORE_DEATH.armorInventory[j] = inventory.armorItemInSlot(j);
            }
            for (int j = 0; j < inventory.mainInventory.length; j++) {
                PlayerHelper.INV_BEFORE_DEATH.mainInventory[j] = inventory.mainInventory[j];
            }
            ph.destroyInventory();
        }
        super.onDeath(entity);
    }

    @Override
    public void attackTargetEntityWithCurrentItem(Entity entity) {
        if (canRunSPC() && ph.instantkill) {
            entity.attackEntityFrom(DamageSource.causePlayerDamage(this), Integer.MAX_VALUE);
            entity.kill();
            return;
        } else if (canRunSPC() && ph.criticalHit) {
            double my = motionY;
            boolean og = onGround;
            boolean iw = inWater;
            float fd = fallDistance;
            super.motionY = -0.1D;
            super.inWater = false;
            super.onGround = false;
            super.fallDistance = 0.1F;
            super.attackTargetEntityWithCurrentItem(entity);
            motionY = my;
            onGround = og;
            inWater = iw;
            fallDistance = fd;
            return;
        }
        super.attackTargetEntityWithCurrentItem(entity);
    }

    @Override
    public boolean handleWaterMovement() {
        if (canRunSPC() && !ph.watermovement) {
            return false;
        }
        return super.handleWaterMovement();
    }

    @Override
    public boolean handleLavaMovement() {
        if (canRunSPC() && !ph.watermovement) {
            return false;
        }
        return super.handleLavaMovement();
    }

    @Override
    public EntityItem dropPlayerItemWithRandomChoice(ItemStack itemstack, boolean flag) {
        if (canRunSPC()) {
            ph.givePlayerItemNaturally(itemstack);
            return null;
        }
        return super.dropPlayerItemWithRandomChoice(itemstack,flag);
    }

    @Override
    public MovingObjectPosition rayTrace(double d, float f) {
        if (canRunSPC() && d == this.mc.playerController.getBlockReachDistance()) {
            d = ph.getReachDistance();
        }
        return super.rayTrace(d, f);
    }

    @Override
    public boolean isOnLadder() {
        if (canRunSPC() && ph.ladderMode && isCollidedHorizontally) {
            return true;
        }
        return super.isOnLadder();
    }

    /*
     * initPlayerHelper - initializes the PlayerHelper variable. Only called if all necessary SPC files exist.
     */
    public void initPlayerHelper(Session session) {
        ph = new PlayerHelper(this.mc, this);
        ph.readWaypointsFromNBT(ph.getWorldDir());
        multiplayer = mc.isMultiplayerWorld();

        if (STARTUP == null && !multiplayer) {
            ph.sendMessage("\2478Single Player Commands (" + SPCVERSION.getVersion() + ")");
            Calendar cal = Calendar.getInstance();
            if (cal.get(Calendar.DAY_OF_MONTH) == 25 && cal.get(Calendar.MONTH) == 11) {
                String name = username == null || username.equalsIgnoreCase("") ? "" : "Dear " + username + ", ";
                ph.sendMessage("\2474" + name + "Merry Christmas! From simo_415");
            } else if (cal.get(Calendar.DAY_OF_MONTH) == 6 && cal.get(Calendar.MONTH) == 11) {
                ph.sendMessage("\2475Happy birthday Single Player Commands. Now a year older!");
            }
            STARTUP = new Object();
        }
        if (session != null && session.getUsername() != null && session.getUsername().length() > 0) {
            ph.sessionusername = session.getUsername();
        }
    }

    /*
     * checkClasses - checks if all the required SPC classes exist. If they do, returns true. Otherwise, returns false.
     */
    public boolean checkClasses() {
        missingRequiredClasses = new Vector<String>();
        missingOptionalClasses = new Vector<String>();
        phexists = true;
        curmcversion = Display.getTitle().split(" ")[Display.getTitle().split(" ").length - 1];
        /*
         * Pointless bit of code which trunks insists on leaving in
         */
        if (!curmcversion.equalsIgnoreCase(MCVERSION)) {
            addChatMessage("\2474" + "Single Player Commands v" + SPCVERSION.getVersion() + " is not compatible with Minecraft v" + curmcversion);
            System.err.println("Single Player Commands v" + SPCVERSION.getVersion() + " is not compatible with Minecraft v" + curmcversion);
        }
        Package p = EntityPlayerSP.class.getPackage();
        String prefix = p == null ? "" : p.getName() + ".";
        String requiredClasses[] = new String[] { "PlayerHelper", "Settings", "SPCPlugin", "SPCPluginManager", "SPCCommand" };
        String optionalClasses[] = new String[] { "spc_WorldEdit", "SPCLocalConfiguration", "SPCLocalPlayer", "SPCLocalWorld", "SPCServerInterface", "WorldEditPlugin" };

        for (String classname : requiredClasses) {
            try {
                Class.forName(prefix + classname);
            } catch (Throwable e) {
                missingRequiredClasses.add(classname);
            }
        }
        for (String classname : optionalClasses) {
            try {
                Class.forName(prefix + classname);
            } catch (Throwable e) {
                missingOptionalClasses.add(classname);
            }
        }

        if (missingRequiredClasses.size() != 0) {
            addChatMessage("\2474" + "You are missing these class files: ");
            String list = "";
            for (String missing : missingRequiredClasses) {
                list += missing + ", ";
            }
            addChatMessage("\2474" + list);
            addChatMessage("\2474" + "Please try reinstalling.");
            phexists = false;
        }
        return phexists;
    }

    /**
     * Checks if SPC is allowed to run or not.
     * @return true if SPC is allowed to run
     */
    public boolean canRunSPC() {
        return phexists && !multiplayer;
    }
}