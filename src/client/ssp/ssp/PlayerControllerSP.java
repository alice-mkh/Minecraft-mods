package net.minecraft.src.ssp;

import net.minecraft.src.Block;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

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
    @Override
    public void flipPlayer(EntityPlayer par1EntityPlayer)
    {
        par1EntityPlayer.rotationYaw = -180F;
    }

    @Override
    public boolean shouldDrawHUD()
    {
        return true;
    }

    /**
     * Called when a player completes the destruction of a block
     */
    @Override
    public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4)
    {
        int i = mc.theWorld.getBlockId(par1, par2, par3);
        int j = mc.theWorld.getBlockMetadata(par1, par2, par3);
        boolean flag = super.onPlayerDestroyBlock(par1, par2, par3, par4);
        ItemStack itemstack = mc.thePlayer.getCurrentEquippedItem();
        boolean flag1 = mc.thePlayer.canHarvestBlock(Block.blocksList[i]);

        if (itemstack != null)
        {
            itemstack.onBlockDestroyed(mc.theWorld, i, par1, par2, par3, mc.thePlayer);

            if (itemstack.stackSize == 0)
            {
//                 itemstack.onItemDestroyedByUse(mc.thePlayer);
                mc.thePlayer.destroyCurrentEquippedItem();
            }
        }

        if (flag && flag1)
        {
            Block.blocksList[i].harvestBlock(mc.theWorld, mc.thePlayer, par1, par2, par3, j);
        }

        return flag;
    }

    /**
     * Called by Minecraft class when the player is hitting a block with an item. Args: x, y, z, side
     */
    @Override
    public void clickBlock(int par1, int par2, int par3, int par4)
    {
        if (!mc.thePlayer.isCurrentToolAdventureModeExempt(par1, par2, par3))
        {
            return;
        }

        mc.theWorld.extinguishFire(mc.thePlayer, par1, par2, par3, par4);
        int i = mc.theWorld.getBlockId(par1, par2, par3);

        if (i > 0 && curBlockDamage == 0.0F)
        {
            Block.blocksList[i].onBlockClicked(mc.theWorld, par1, par2, par3, mc.thePlayer);
        }

        if (i > 0 && Block.blocksList[i].getPlayerRelativeBlockHardness(mc.thePlayer, mc.thePlayer.worldObj, par1, par2, par3) >= 1.0F)
        {
            onPlayerDestroyBlock(par1, par2, par3, par4);
        }
    }

    /**
     * Resets current block damage and isHittingBlock
     */
    @Override
    public void resetBlockRemoving()
    {
        curBlockDamage = 0.0F;
        blockHitWait = 0;
        mc.theWorld.destroyBlockInWorldPartially(mc.thePlayer.entityId, curBlockX, curBlockY, curBlockZ, -1);
    }

    /**
     * Called when a player damages a block and updates damage counters
     */
    @Override
    public void onPlayerDamageBlock(int par1, int par2, int par3, int par4)
    {
        if (blockHitWait > 0)
        {
            blockHitWait--;
            return;
        }

        if (par1 == curBlockX && par2 == curBlockY && par3 == curBlockZ)
        {
            int i = mc.theWorld.getBlockId(par1, par2, par3);

            if (!mc.thePlayer.isCurrentToolAdventureModeExempt(par1, par2, par3))
            {
                return;
            }

            if (i == 0)
            {
                return;
            }

            Block block = Block.blocksList[i];
            curBlockDamage += block.getPlayerRelativeBlockHardness(mc.thePlayer, mc.thePlayer.worldObj, par1, par2, par3);

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
            mc.theWorld.destroyBlockInWorldPartially(mc.thePlayer.entityId, curBlockX, curBlockY, curBlockZ, (int)(curBlockDamage * 10F) - 1);
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

    @Override
    public void updateController()
    {
        prevBlockDamage = curBlockDamage;
        mc.sndManager.playRandomMusicIfReady();
    }

    /**
     * Handles a players right click
     */
    @Override
    public boolean onPlayerRightClick(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, Vec3 par8Vec3)
    {
        int i = par2World.getBlockId(par4, par5, par6);

        float f = (float)par8Vec3.xCoord - (float)par4;
        float f1 = (float)par8Vec3.yCoord - (float)par5;
        float f2 = (float)par8Vec3.zCoord - (float)par6;

        if (!par1EntityPlayer.isSneaking() || par1EntityPlayer.getHeldItem() == null)
        {
            if (i > 0 && Block.blocksList[i].onBlockActivated(par2World, par4, par5, par6, par1EntityPlayer, par7, f, f1, f2))
            {
                return true;
            }
        }

        if (par3ItemStack == null)
        {
            return false;
        }
        else
        {
            return par3ItemStack.tryPlaceItemIntoWorld(par1EntityPlayer, par2World, par4, par5, par6, par7, f, f1, f2);

        }
    }
}
