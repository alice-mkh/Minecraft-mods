package net.minecraft.src;

import net.minecraft.client.Minecraft;

public class PlayerControllerSP extends PlayerController
{
    private int curBlockX;
    private int curBlockY;
    private int curBlockZ;
    private float curBlockDamage;
    private float prevBlockDamage;
    private float blockDestroySoundCounter;
    private int blockHitWait;

    public PlayerControllerSP(Minecraft par1Minecraft)
    {
        super(par1Minecraft);
        curBlockX = -1;
        curBlockY = -1;
        curBlockZ = -1;
        curBlockDamage = 0.0F;
        prevBlockDamage = 0.0F;
        blockDestroySoundCounter = 0.0F;
        blockHitWait = 0;
    }

    /**
     * Flips the player around. Args: player
     */
    public void flipPlayer(EntityPlayer par1EntityPlayer)
    {
        par1EntityPlayer.rotationYaw = -180F;
    }

    public boolean shouldDrawHUD()
    {
        return true;
    }

    /**
     * Called when a player completes the destruction of a block
     */
    public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4)
    {
        int i = mc.field_71441_e.getBlockId(par1, par2, par3);
        int j = mc.field_71441_e.getBlockMetadata(par1, par2, par3);
        boolean flag = super.onPlayerDestroyBlock(par1, par2, par3, par4);
        ItemStack itemstack = mc.field_71439_g.getCurrentEquippedItem();
        boolean flag1 = mc.field_71439_g.canHarvestBlock(Block.blocksList[i]);

        if (itemstack != null)
        {
            itemstack.func_77941_a(mc.field_71441_e, i, par1, par2, par3, mc.field_71439_g);

            if (itemstack.stackSize == 0)
            {
//                 itemstack.onItemDestroyedByUse(mc.field_71439_g);
                mc.field_71439_g.destroyCurrentEquippedItem();
            }
        }

        if (flag && flag1)
        {
            Block.blocksList[i].harvestBlock(mc.field_71441_e, mc.field_71439_g, par1, par2, par3, j);
        }

        return flag;
    }

    /**
     * Called by Minecraft class when the player is hitting a block with an item. Args: x, y, z, side
     */
    public void clickBlock(int par1, int par2, int par3, int par4)
    {
        if (!mc.field_71439_g.canPlayerEdit(par1, par2, par3))
        {
            return;
        }

        mc.field_71441_e.func_72886_a(mc.field_71439_g, par1, par2, par3, par4);
        int i = mc.field_71441_e.getBlockId(par1, par2, par3);

        if (i > 0 && curBlockDamage == 0.0F)
        {
            Block.blocksList[i].onBlockClicked(mc.field_71441_e, par1, par2, par3, mc.field_71439_g);
        }

        if (i > 0 && Block.blocksList[i].func_71908_a(mc.field_71439_g, mc.field_71439_g.worldObj, par1, par2, par3) >= 1.0F)
        {
            onPlayerDestroyBlock(par1, par2, par3, par4);
        }
    }

    /**
     * Resets current block damage and isHittingBlock
     */
    public void resetBlockRemoving()
    {
        curBlockDamage = 0.0F;
        blockHitWait = 0;
        mc.field_71441_e.func_72888_f(mc.field_71439_g.entityId, curBlockX, curBlockY, curBlockZ, -1);
    }

    /**
     * Called when a player damages a block and updates damage counters
     */
    public void onPlayerDamageBlock(int par1, int par2, int par3, int par4)
    {
        if (blockHitWait > 0)
        {
            blockHitWait--;
            return;
        }

        if (par1 == curBlockX && par2 == curBlockY && par3 == curBlockZ)
        {
            int i = mc.field_71441_e.getBlockId(par1, par2, par3);

            if (!mc.field_71439_g.canPlayerEdit(par1, par2, par3))
            {
                return;
            }

            if (i == 0)
            {
                return;
            }

            Block block = Block.blocksList[i];
            curBlockDamage += block.func_71908_a(mc.field_71439_g, mc.field_71439_g.worldObj, par1, par2, par3);

            if (blockDestroySoundCounter % 4F == 0.0F && block != null)
            {
                mc.sndManager.playSound(block.stepSound.getStepSound(), (float)par1 + 0.5F, (float)par2 + 0.5F, (float)par3 + 0.5F, (block.stepSound.getVolume() + 1.0F) / 8F, block.stepSound.getPitch() * 0.5F);
            }

            blockDestroySoundCounter++;

            if (curBlockDamage >= 1.0F)
            {
                onPlayerDestroyBlock(par1, par2, par3, par4);
                curBlockDamage = 0.0F;
                prevBlockDamage = 0.0F;
                blockDestroySoundCounter = 0.0F;
                blockHitWait = 5;
            }
            mc.field_71441_e.func_72888_f(mc.field_71439_g.entityId, curBlockX, curBlockY, curBlockZ, (int)(curBlockDamage * 10F) - 1);
        }
        else
        {
            curBlockDamage = 0.0F;
            prevBlockDamage = 0.0F;
            blockDestroySoundCounter = 0.0F;
            curBlockX = par1;
            curBlockY = par2;
            curBlockZ = par3;
        }
    }
/*
    public void setPartialTime(float par1)
    {
        if (curBlockDamage <= 0.0F)
        {
            mc.ingameGUI.damageGuiPartialTime = 0.0F;
            mc.renderGlobal.damagePartialTime = 0.0F;
        }
        else
        {
            float f = prevBlockDamage + (curBlockDamage - prevBlockDamage) * par1;
            mc.ingameGUI.damageGuiPartialTime = f;
            mc.renderGlobal.damagePartialTime = f;
        }
    }
*/
    /**
     * player reach distance = 4F
     */
    public float getBlockReachDistance()
    {
        return 4F;
    }

    /**
     * Called on world change with the new World as the only parameter.
     */
    public void onWorldChange(World par1World)
    {
        super.onWorldChange(par1World);
    }

    public EntityPlayer func_78754_a(World par1World)
    {
        EntityPlayer entityplayer = super.func_78754_a(par1World);
        return entityplayer;
    }

    public void updateController()
    {
        prevBlockDamage = curBlockDamage;
        mc.sndManager.playRandomMusicIfReady();
    }

    /**
     * Handles a players right click
     */
    public boolean func_78760_a(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, Vec3 par8Vec3)
    {
        int i = par2World.getBlockId(par4, par5, par6);

        float f = (float)par8Vec3.xCoord - (float)par4;
        float f1 = (float)par8Vec3.yCoord - (float)par5;
        float f2 = (float)par8Vec3.zCoord - (float)par6;
        if (i > 0 && Block.blocksList[i].func_71903_a(par2World, par4, par5, par6, par1EntityPlayer, par7, f, f1, f2))
        {
            return true;
        }

        if (par3ItemStack == null)
        {
            return false;
        }
        else
        {
            return par3ItemStack.func_77943_a(par1EntityPlayer, par2World, par4, par5, par6, par7, f, f1, f2);

        }
    }

    public boolean func_35642_f()
    {
        return true;
    }
}
