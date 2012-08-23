package net.minecraft.src;

import java.io.*;
import java.util.*;
import java.util.logging.Logger;
import net.minecraft.server.MinecraftServer;

public class NetServerHandler extends NetHandler
{
    public static Logger field_72577_a = Logger.getLogger("Minecraft");
    public NetworkManager field_72575_b;
    public boolean field_72576_c;
    private MinecraftServer field_72573_d;
    private EntityPlayerMP field_72574_e;
    private int field_72571_f;
    private int field_72572_g;
    private boolean field_72584_h;
    private int field_72585_i;
    private long field_72582_j;
    private static Random field_72583_k = new Random();
    private long field_72580_l;
    private int field_72581_m;
    private int field_72578_n;
    private double field_72579_o;
    private double field_72589_p;
    private double field_72588_q;
    private boolean field_72587_r;
    private IntHashMap field_72586_s;

    public NetServerHandler(MinecraftServer par1MinecraftServer, NetworkManager par2NetworkManager, EntityPlayerMP par3EntityPlayerMP)
    {
        field_72576_c = false;
        field_72581_m = 0;
        field_72578_n = 0;
        field_72587_r = true;
        field_72586_s = new IntHashMap();
        field_72573_d = par1MinecraftServer;
        field_72575_b = par2NetworkManager;
        par2NetworkManager.func_74425_a(this);
        field_72574_e = par3EntityPlayerMP;
        par3EntityPlayerMP.field_71135_a = this;
    }

    public NetServerHandler(EntityPlayerMP par3EntityPlayerMP)
    {
        field_72576_c = false;
        field_72581_m = 0;
        field_72578_n = 0;
        field_72587_r = true;
        field_72586_s = new IntHashMap();
        field_72574_e = par3EntityPlayerMP;
        par3EntityPlayerMP.field_71135_a = this;
    }

    public EntityPlayerMP getPlayer()
    {
        return field_72574_e;
    }

    public void func_72570_d()
    {
        field_72584_h = false;
        field_72571_f++;
        field_72573_d.field_71304_b.startSection("packetflow");
        field_72575_b.processReadPackets();
        field_72573_d.field_71304_b.endStartSection("keepAlive");

        if ((long)field_72571_f - field_72580_l > 20L)
        {
            field_72580_l = field_72571_f;
            field_72582_j = System.nanoTime() / 0xf4240L;
            field_72585_i = field_72583_k.nextInt();
            func_72567_b(new Packet0KeepAlive(field_72585_i));
        }

        if (field_72581_m > 0)
        {
            field_72581_m--;
        }

        if (field_72578_n > 0)
        {
            field_72578_n--;
        }

        field_72573_d.field_71304_b.endStartSection("playerTick");

        if (!field_72584_h && !field_72574_e.field_71136_j)
        {
            field_72574_e.func_71127_g();
        }

        field_72573_d.field_71304_b.endSection();
    }

    public void func_72565_c(String par1Str)
    {
        if (field_72576_c)
        {
            return;
        }
        else
        {
            field_72574_e.func_71123_m();
            func_72567_b(new Packet255KickDisconnect(par1Str));
            field_72575_b.serverShutdown();
            field_72573_d.func_71203_ab().func_72384_a(new Packet3Chat((new StringBuilder()).append("\247e").append(field_72574_e.username).append(" left the game.").toString()));
            field_72573_d.func_71203_ab().func_72367_e(field_72574_e);
            field_72576_c = true;
            return;
        }
    }

    public void handleFlying(Packet10Flying par1Packet10Flying)
    {
        WorldServer worldserver = field_72573_d.func_71218_a(field_72574_e.dimension);
        field_72584_h = true;

        if (field_72574_e.field_71136_j)
        {
            return;
        }

        if (!field_72587_r)
        {
            double d = par1Packet10Flying.yPosition - field_72589_p;

            if (par1Packet10Flying.xPosition == field_72579_o && d * d < 0.01D && par1Packet10Flying.zPosition == field_72588_q)
            {
                field_72587_r = true;
            }
        }

        if (field_72587_r)
        {
            if (field_72574_e.ridingEntity != null)
            {
                float f = field_72574_e.rotationYaw;
                float f1 = field_72574_e.rotationPitch;
                field_72574_e.ridingEntity.updateRiderPosition();
                double d2 = field_72574_e.posX;
                double d4 = field_72574_e.posY;
                double d6 = field_72574_e.posZ;
                double d8 = 0.0D;
                double d9 = 0.0D;

                if (par1Packet10Flying.rotating)
                {
                    f = par1Packet10Flying.yaw;
                    f1 = par1Packet10Flying.pitch;
                }

                if (par1Packet10Flying.moving && par1Packet10Flying.yPosition == -999D && par1Packet10Flying.stance == -999D)
                {
                    if (par1Packet10Flying.xPosition > 1.0D || par1Packet10Flying.zPosition > 1.0D)
                    {
                        System.err.println((new StringBuilder()).append(field_72574_e.username).append(" was caught trying to crash the server with an invalid position.").toString());
                        func_72565_c("Nope!");
                        return;
                    }

                    d8 = par1Packet10Flying.xPosition;
                    d9 = par1Packet10Flying.zPosition;
                }

                field_72574_e.onGround = par1Packet10Flying.onGround;
                field_72574_e.func_71127_g();
                field_72574_e.moveEntity(d8, 0.0D, d9);
                field_72574_e.setPositionAndRotation(d2, d4, d6, f, f1);
                field_72574_e.motionX = d8;
                field_72574_e.motionZ = d9;

                if (field_72574_e.ridingEntity != null)
                {
                    worldserver.func_73050_b(field_72574_e.ridingEntity, true);
                }

                if (field_72574_e.ridingEntity != null)
                {
                    field_72574_e.ridingEntity.updateRiderPosition();
                }

                field_72573_d.func_71203_ab().func_72358_d(field_72574_e);
                field_72579_o = field_72574_e.posX;
                field_72589_p = field_72574_e.posY;
                field_72588_q = field_72574_e.posZ;
                worldserver.updateEntity(field_72574_e);
                return;
            }

            if (field_72574_e.isPlayerSleeping())
            {
                field_72574_e.func_71127_g();
                field_72574_e.setPositionAndRotation(field_72579_o, field_72589_p, field_72588_q, field_72574_e.rotationYaw, field_72574_e.rotationPitch);
                worldserver.updateEntity(field_72574_e);
                return;
            }

            double d1 = field_72574_e.posY;
            field_72579_o = field_72574_e.posX;
            field_72589_p = field_72574_e.posY;
            field_72588_q = field_72574_e.posZ;
            double d3 = field_72574_e.posX;
            double d5 = field_72574_e.posY;
            double d7 = field_72574_e.posZ;
            float f2 = field_72574_e.rotationYaw;
            float f3 = field_72574_e.rotationPitch;

            if (par1Packet10Flying.moving && par1Packet10Flying.yPosition == -999D && par1Packet10Flying.stance == -999D)
            {
                par1Packet10Flying.moving = false;
            }

            if (par1Packet10Flying.moving)
            {
                d3 = par1Packet10Flying.xPosition;
                d5 = par1Packet10Flying.yPosition;
                d7 = par1Packet10Flying.zPosition;
                double d10 = par1Packet10Flying.stance - par1Packet10Flying.yPosition;

                if (!field_72574_e.isPlayerSleeping() && (d10 > 1.6499999999999999D || d10 < 0.10000000000000001D))
                {
                    func_72565_c("Illegal stance");
                    field_72577_a.warning((new StringBuilder()).append(field_72574_e.username).append(" had an illegal stance: ").append(d10).toString());
                    return;
                }

                if (Math.abs(par1Packet10Flying.xPosition) > 32000000D || Math.abs(par1Packet10Flying.zPosition) > 32000000D)
                {
                    func_72565_c("Illegal position");
                    return;
                }
            }

            if (par1Packet10Flying.rotating)
            {
                f2 = par1Packet10Flying.yaw;
                f3 = par1Packet10Flying.pitch;
            }

            field_72574_e.func_71127_g();
            field_72574_e.ySize = 0.0F;
            field_72574_e.setPositionAndRotation(field_72579_o, field_72589_p, field_72588_q, f2, f3);

            if (!field_72587_r)
            {
                return;
            }

            double d11 = d3 - field_72574_e.posX;
            double d12 = d5 - field_72574_e.posY;
            double d13 = d7 - field_72574_e.posZ;
            double d14 = Math.min(Math.abs(d11), Math.abs(field_72574_e.motionX));
            double d15 = Math.min(Math.abs(d12), Math.abs(field_72574_e.motionY));
            double d16 = Math.min(Math.abs(d13), Math.abs(field_72574_e.motionZ));
            double d17 = d14 * d14 + d15 * d15 + d16 * d16;

            if (d17 > 100D && (!field_72573_d.func_71264_H() || !field_72573_d.func_71214_G().equals(field_72574_e.username)))
            {
                field_72577_a.warning((new StringBuilder()).append(field_72574_e.username).append(" moved too quickly! ").append(d11).append(",").append(d12).append(",").append(d13).append(" (").append(d14).append(", ").append(d15).append(", ").append(d16).append(")").toString());
                func_72569_a(field_72579_o, field_72589_p, field_72588_q, field_72574_e.rotationYaw, field_72574_e.rotationPitch);
                return;
            }

            float f4 = 0.0625F;
            boolean flag = worldserver.getCollidingBoundingBoxes(field_72574_e, field_72574_e.boundingBox.copy().contract(f4, f4, f4)).isEmpty();

            if (field_72574_e.onGround && !par1Packet10Flying.onGround && d12 > 0.0D)
            {
                field_72574_e.addExhaustion(0.2F);
            }

            field_72574_e.moveEntity(d11, d12, d13);
            field_72574_e.onGround = par1Packet10Flying.onGround;
            field_72574_e.addMovementStat(d11, d12, d13);
            double d18 = d12;
            d11 = d3 - field_72574_e.posX;
            d12 = d5 - field_72574_e.posY;

            if (d12 > -0.5D || d12 < 0.5D)
            {
                d12 = 0.0D;
            }

            d13 = d7 - field_72574_e.posZ;
            d17 = d11 * d11 + d12 * d12 + d13 * d13;
            boolean flag1 = false;

            if (d17 > 0.0625D && !field_72574_e.isPlayerSleeping() && !field_72574_e.field_71134_c.func_73083_d())
            {
                flag1 = true;
                field_72577_a.warning((new StringBuilder()).append(field_72574_e.username).append(" moved wrongly!").toString());
            }

            field_72574_e.setPositionAndRotation(d3, d5, d7, f2, f3);
            boolean flag2 = worldserver.getCollidingBoundingBoxes(field_72574_e, field_72574_e.boundingBox.copy().contract(f4, f4, f4)).isEmpty();

            if (flag && (flag1 || !flag2) && !field_72574_e.isPlayerSleeping())
            {
                func_72569_a(field_72579_o, field_72589_p, field_72588_q, f2, f3);
                return;
            }

            AxisAlignedBB axisalignedbb = field_72574_e.boundingBox.copy().expand(f4, f4, f4).addCoord(0.0D, -0.55000000000000004D, 0.0D);

            if (!field_72573_d.func_71231_X() && !field_72574_e.field_71134_c.func_73083_d() && !worldserver.func_72829_c(axisalignedbb))
            {
                if (d18 >= -0.03125D)
                {
                    field_72572_g++;

                    if (field_72572_g > 80)
                    {
                        field_72577_a.warning((new StringBuilder()).append(field_72574_e.username).append(" was kicked for floating too long!").toString());
                        func_72565_c("Flying is not enabled on this server");
                        return;
                    }
                }
            }
            else
            {
                field_72572_g = 0;
            }

            field_72574_e.onGround = par1Packet10Flying.onGround;
            field_72573_d.func_71203_ab().func_72358_d(field_72574_e);
            field_72574_e.func_71122_b(field_72574_e.posY - d1, par1Packet10Flying.onGround);
        }
    }

    public void func_72569_a(double par1, double par3, double par5, float par7, float par8)
    {
        field_72587_r = false;
        field_72579_o = par1;
        field_72589_p = par3;
        field_72588_q = par5;
        field_72574_e.setPositionAndRotation(par1, par3, par5, par7, par8);
        field_72574_e.field_71135_a.func_72567_b(new Packet13PlayerLookMove(par1, par3 + 1.6200000047683716D, par3, par5, par7, par8, false));
    }

    public void handleBlockDig(Packet14BlockDig par1Packet14BlockDig)
    {
        WorldServer worldserver = field_72573_d.func_71218_a(field_72574_e.dimension);

        if (par1Packet14BlockDig.status == 4)
        {
            field_72574_e.dropOneItem();
            return;
        }

        if (par1Packet14BlockDig.status == 5)
        {
            field_72574_e.stopUsingItem();
            return;
        }

        boolean flag = worldserver.field_73060_c = worldserver.worldProvider.worldType != 0 || field_72573_d.func_71203_ab().func_72353_e(field_72574_e.username) || field_72573_d.func_71264_H();
        boolean flag1 = false;

        if (par1Packet14BlockDig.status == 0)
        {
            flag1 = true;
        }

        if (par1Packet14BlockDig.status == 2)
        {
            flag1 = true;
        }

        int i = par1Packet14BlockDig.xPosition;
        int j = par1Packet14BlockDig.yPosition;
        int k = par1Packet14BlockDig.zPosition;

        if (flag1)
        {
            double d = field_72574_e.posX - ((double)i + 0.5D);
            double d1 = (field_72574_e.posY - ((double)j + 0.5D)) + 1.5D;
            double d3 = field_72574_e.posZ - ((double)k + 0.5D);
            double d5 = d * d + d1 * d1 + d3 * d3;

            if (d5 > 36D)
            {
                return;
            }

            if (j >= field_72573_d.func_71207_Z())
            {
                return;
            }
        }

        ChunkCoordinates chunkcoordinates = worldserver.getSpawnPoint();
        int l = MathHelper.func_76130_a(i - chunkcoordinates.posX);
        int i1 = MathHelper.func_76130_a(k - chunkcoordinates.posZ);

        if (l > i1)
        {
            i1 = l;
        }

        if (par1Packet14BlockDig.status == 0)
        {
            if (i1 > 16 || flag)
            {
                field_72574_e.field_71134_c.func_73074_a(i, j, k, par1Packet14BlockDig.face);
            }
            else
            {
                field_72574_e.field_71135_a.func_72567_b(new Packet53BlockChange(i, j, k, worldserver));
            }
        }
        else if (par1Packet14BlockDig.status == 2)
        {
            field_72574_e.field_71134_c.func_73082_a(i, j, k);

            if (worldserver.getBlockId(i, j, k) != 0)
            {
                field_72574_e.field_71135_a.func_72567_b(new Packet53BlockChange(i, j, k, worldserver));
            }
        }
        else if (par1Packet14BlockDig.status == 1)
        {
            field_72574_e.field_71134_c.func_73073_c(i, j, k);

            if (worldserver.getBlockId(i, j, k) != 0)
            {
                field_72574_e.field_71135_a.func_72567_b(new Packet53BlockChange(i, j, k, worldserver));
            }
        }
        else if (par1Packet14BlockDig.status == 3)
        {
            double d2 = field_72574_e.posX - ((double)i + 0.5D);
            double d4 = field_72574_e.posY - ((double)j + 0.5D);
            double d6 = field_72574_e.posZ - ((double)k + 0.5D);
            double d7 = d2 * d2 + d4 * d4 + d6 * d6;

            if (d7 < 256D)
            {
                field_72574_e.field_71135_a.func_72567_b(new Packet53BlockChange(i, j, k, worldserver));
            }
        }

        worldserver.field_73060_c = false;
    }

    public void handlePlace(Packet15Place par1Packet15Place)
    {
        WorldServer worldserver = field_72573_d.func_71218_a(field_72574_e.dimension);
        ItemStack itemstack = field_72574_e.inventory.getCurrentItem();
        boolean flag = false;
        int i = par1Packet15Place.func_73403_d();
        int j = par1Packet15Place.func_73402_f();
        int k = par1Packet15Place.func_73407_g();
        int l = par1Packet15Place.func_73401_h();
        boolean flag1 = worldserver.field_73060_c = worldserver.worldProvider.worldType != 0 || field_72573_d.func_71203_ab().func_72353_e(field_72574_e.username) || field_72573_d.func_71264_H();

        if (par1Packet15Place.func_73401_h() == 255)
        {
            if (itemstack == null)
            {
                return;
            }

            field_72574_e.field_71134_c.func_73085_a(field_72574_e, worldserver, itemstack);
        }
        else if (par1Packet15Place.func_73402_f() < field_72573_d.func_71207_Z() - 1 || par1Packet15Place.func_73401_h() != 1 && par1Packet15Place.func_73402_f() < field_72573_d.func_71207_Z())
        {
            ChunkCoordinates chunkcoordinates = worldserver.getSpawnPoint();
            int i1 = MathHelper.func_76130_a(i - chunkcoordinates.posX);
            int j1 = MathHelper.func_76130_a(k - chunkcoordinates.posZ);

            if (i1 > j1)
            {
                j1 = i1;
            }

            if (field_72587_r && field_72574_e.getDistanceSq((double)i + 0.5D, (double)j + 0.5D, (double)k + 0.5D) < 64D && (j1 > 16 || flag1))
            {
                field_72574_e.field_71134_c.func_73078_a(field_72574_e, worldserver, itemstack, i, j, k, l, par1Packet15Place.func_73406_j(), par1Packet15Place.func_73404_l(), par1Packet15Place.func_73408_m());
            }

            flag = true;
        }
        else
        {
            field_72574_e.field_71135_a.func_72567_b(new Packet3Chat((new StringBuilder()).append("\2477Height limit for building is ").append(field_72573_d.func_71207_Z()).toString()));
            flag = true;
        }

        if (flag)
        {
            field_72574_e.field_71135_a.func_72567_b(new Packet53BlockChange(i, j, k, worldserver));

            if (l == 0)
            {
                j--;
            }

            if (l == 1)
            {
                j++;
            }

            if (l == 2)
            {
                k--;
            }

            if (l == 3)
            {
                k++;
            }

            if (l == 4)
            {
                i--;
            }

            if (l == 5)
            {
                i++;
            }

            field_72574_e.field_71135_a.func_72567_b(new Packet53BlockChange(i, j, k, worldserver));
        }

        itemstack = field_72574_e.inventory.getCurrentItem();

        if (itemstack != null && itemstack.stackSize == 0)
        {
            field_72574_e.inventory.mainInventory[field_72574_e.inventory.currentItem] = null;
            itemstack = null;
        }

        if (itemstack == null || itemstack.getMaxItemUseDuration() == 0)
        {
            field_72574_e.field_71137_h = true;
            field_72574_e.inventory.mainInventory[field_72574_e.inventory.currentItem] = ItemStack.copyItemStack(field_72574_e.inventory.mainInventory[field_72574_e.inventory.currentItem]);
            Slot slot = field_72574_e.craftingInventory.func_75147_a(field_72574_e.inventory, field_72574_e.inventory.currentItem);
            field_72574_e.craftingInventory.updateCraftingResults();
            field_72574_e.field_71137_h = false;

            if (!ItemStack.areItemStacksEqual(field_72574_e.inventory.getCurrentItem(), par1Packet15Place.func_73405_i()))
            {
                func_72567_b(new Packet103SetSlot(field_72574_e.craftingInventory.windowId, slot.slotNumber, field_72574_e.inventory.getCurrentItem()));
            }
        }

        worldserver.field_73060_c = false;
    }

    public void handleErrorMessage(String par1Str, Object par2ArrayOfObj[])
    {
        field_72577_a.info((new StringBuilder()).append(field_72574_e.username).append(" lost connection: ").append(par1Str).toString());
        field_72573_d.func_71203_ab().func_72384_a(new Packet3Chat((new StringBuilder()).append("\247e").append(field_72574_e.username).append(" left the game.").toString()));
        field_72573_d.func_71203_ab().func_72367_e(field_72574_e);
        field_72576_c = true;

        if (field_72573_d.func_71264_H() && field_72574_e.username.equals(field_72573_d.func_71214_G()))
        {
            field_72577_a.info("Stopping singleplayer server as player logged out");
            field_72573_d.func_71263_m();
        }
    }

    public void registerPacket(Packet par1Packet)
    {
        field_72577_a.warning((new StringBuilder()).append(getClass()).append(" wasn't prepared to deal with a ").append(par1Packet.getClass()).toString());
        func_72565_c("Protocol error, unexpected packet");
    }

    public void func_72567_b(Packet par1Packet)
    {
        if (par1Packet instanceof Packet3Chat)
        {
            Packet3Chat packet3chat = (Packet3Chat)par1Packet;
            int i = field_72574_e.func_71126_v();

            if (i == 2)
            {
                return;
            }

            if (i == 1 && !packet3chat.func_73475_d())
            {
                return;
            }
        }

        field_72575_b.addToSendQueue(par1Packet);
    }

    public void handleBlockItemSwitch(Packet16BlockItemSwitch par1Packet16BlockItemSwitch)
    {
        if (par1Packet16BlockItemSwitch.id < 0 || par1Packet16BlockItemSwitch.id >= InventoryPlayer.func_70451_h())
        {
            field_72577_a.warning((new StringBuilder()).append(field_72574_e.username).append(" tried to set an invalid carried item").toString());
            return;
        }
        else
        {
            field_72574_e.inventory.currentItem = par1Packet16BlockItemSwitch.id;
            return;
        }
    }

    public void handleChat(Packet3Chat par1Packet3Chat)
    {
        net.minecraft.client.Minecraft.invokeModMethod("ModLoader", "serverChat", new Class[]{NetServerHandler.class, String.class}, this, par1Packet3Chat.message);
        if (field_72574_e.func_71126_v() == 2)
        {
            func_72567_b(new Packet3Chat("Cannot send chat message."));
            return;
        }

        String s = par1Packet3Chat.message;

        if (s.length() > 100)
        {
            func_72565_c("Chat message too long");
            return;
        }

        s = s.trim();

        for (int i = 0; i < s.length(); i++)
        {
            if (!ChatAllowedCharacters.isAllowedCharacter(s.charAt(i)))
            {
                func_72565_c("Illegal characters in chat");
                return;
            }
        }

        if (s.startsWith("/"))
        {
            func_72566_d(s);
        }
        else
        {
            if (field_72574_e.func_71126_v() == 1)
            {
                func_72567_b(new Packet3Chat("Cannot send chat message."));
                return;
            }

            s = (new StringBuilder()).append("<").append(field_72574_e.username).append("> ").append(s).toString();
            field_72577_a.info(s);
            field_72573_d.func_71203_ab().func_72384_a(new Packet3Chat(s, false));
        }

        field_72581_m += 20;

        if (field_72581_m > 200 && !field_72573_d.func_71203_ab().func_72353_e(field_72574_e.username))
        {
            func_72565_c("disconnect.spam");
        }
    }

    private void func_72566_d(String par1Str)
    {
        if (field_72573_d.func_71203_ab().func_72353_e(field_72574_e.username) || "/seed".equals(par1Str))
        {
            field_72577_a.info((new StringBuilder()).append(field_72574_e.username).append(" issued server command: ").append(par1Str).toString());
            field_72573_d.func_71187_D().func_71556_a(field_72574_e, par1Str);
        }
    }

    public void handleAnimation(Packet18Animation par1Packet18Animation)
    {
        if (par1Packet18Animation.animate == 1)
        {
            field_72574_e.swingItem();
        }
    }

    /**
     * runs registerPacket on the given Packet19EntityAction
     */
    public void handleEntityAction(Packet19EntityAction par1Packet19EntityAction)
    {
        if (par1Packet19EntityAction.state == 1)
        {
            field_72574_e.setSneaking(true);
        }
        else if (par1Packet19EntityAction.state == 2)
        {
            field_72574_e.setSneaking(false);
        }
        else if (par1Packet19EntityAction.state == 4)
        {
            field_72574_e.setSprinting(true);
        }
        else if (par1Packet19EntityAction.state == 5)
        {
            field_72574_e.setSprinting(false);
        }
        else if (par1Packet19EntityAction.state == 3)
        {
            field_72574_e.wakeUpPlayer(false, true, true);
            field_72587_r = false;
        }
    }

    public void handleKickDisconnect(Packet255KickDisconnect par1Packet255KickDisconnect)
    {
        field_72575_b.networkShutdown("disconnect.quitting", new Object[0]);
    }

    public int func_72568_e()
    {
        return field_72575_b.func_74426_e();
    }

    public void handleUseEntity(Packet7UseEntity par1Packet7UseEntity)
    {
        WorldServer worldserver = field_72573_d.func_71218_a(field_72574_e.dimension);
        Entity entity = worldserver.func_73045_a(par1Packet7UseEntity.targetEntity);

        if (entity != null)
        {
            boolean flag = field_72574_e.canEntityBeSeen(entity);
            double d = 36D;

            if (!flag)
            {
                d = 9D;
            }

            if (field_72574_e.getDistanceSqToEntity(entity) < d)
            {
                if (par1Packet7UseEntity.isLeftClick == 0)
                {
                    field_72574_e.func_70998_m(entity);
                }
                else if (par1Packet7UseEntity.isLeftClick == 1)
                {
                    field_72574_e.attackTargetEntityWithCurrentItem(entity);
                }
            }
        }
    }

    public void func_72458_a(Packet205ClientCommand par1Packet205ClientCommand)
    {
        if (par1Packet205ClientCommand.field_73447_a == 1)
        {
            if (field_72574_e.field_71136_j)
            {
                field_72574_e = field_72573_d.func_71203_ab().func_72368_a(field_72574_e, 0, true);
            }
            else if (field_72574_e.func_71121_q().getWorldInfo().isHardcoreModeEnabled())
            {
                if (field_72573_d.func_71264_H() && field_72574_e.username.equals(field_72573_d.func_71214_G()))
                {
                    field_72574_e.field_71135_a.func_72565_c("You have died. Game over, man, it's game over!");
                    field_72573_d.func_71272_O();
                }
                else
                {
                    BanEntry banentry = new BanEntry(field_72574_e.username);
                    banentry.func_73689_b("Death in Hardcore");
                    field_72573_d.func_71203_ab().func_72390_e().func_73706_a(banentry);
                    field_72574_e.field_71135_a.func_72565_c("You have died. Game over, man, it's game over!");
                }
            }
            else
            {
                if (field_72574_e.getHealth() > 0)
                {
                    return;
                }

                field_72574_e = field_72573_d.func_71203_ab().func_72368_a(field_72574_e, 0, false);
            }
        }
    }

    public boolean func_72469_b()
    {
        return true;
    }

    /**
     * respawns the player
     */
    public void handleRespawn(Packet9Respawn packet9respawn)
    {
    }

    public void handleCloseWindow(Packet101CloseWindow par1Packet101CloseWindow)
    {
        field_72574_e.func_71128_l();
    }

    public void handleWindowClick(Packet102WindowClick par1Packet102WindowClick)
    {
        if (field_72574_e.craftingInventory.windowId == par1Packet102WindowClick.window_Id && field_72574_e.craftingInventory.func_75129_b(field_72574_e))
        {
            ItemStack itemstack = field_72574_e.craftingInventory.slotClick(par1Packet102WindowClick.inventorySlot, par1Packet102WindowClick.mouseClick, par1Packet102WindowClick.holdingShift, field_72574_e);

            if (ItemStack.areItemStacksEqual(par1Packet102WindowClick.itemStack, itemstack))
            {
                field_72574_e.field_71135_a.func_72567_b(new Packet106Transaction(par1Packet102WindowClick.window_Id, par1Packet102WindowClick.action, true));
                field_72574_e.field_71137_h = true;
                field_72574_e.craftingInventory.updateCraftingResults();
                field_72574_e.func_71113_k();
                field_72574_e.field_71137_h = false;
            }
            else
            {
                field_72586_s.addKey(field_72574_e.craftingInventory.windowId, Short.valueOf(par1Packet102WindowClick.action));
                field_72574_e.field_71135_a.func_72567_b(new Packet106Transaction(par1Packet102WindowClick.window_Id, par1Packet102WindowClick.action, false));
                field_72574_e.craftingInventory.func_75128_a(field_72574_e, false);
                ArrayList arraylist = new ArrayList();

                for (int i = 0; i < field_72574_e.craftingInventory.inventorySlots.size(); i++)
                {
                    arraylist.add(((Slot)field_72574_e.craftingInventory.inventorySlots.get(i)).getStack());
                }

                field_72574_e.func_71110_a(field_72574_e.craftingInventory, arraylist);
            }
        }
    }

    public void handleEnchantItem(Packet108EnchantItem par1Packet108EnchantItem)
    {
        if (field_72574_e.craftingInventory.windowId == par1Packet108EnchantItem.windowId && field_72574_e.craftingInventory.func_75129_b(field_72574_e))
        {
            field_72574_e.craftingInventory.enchantItem(field_72574_e, par1Packet108EnchantItem.enchantment);
            field_72574_e.craftingInventory.updateCraftingResults();
        }
    }

    /**
     * Handle a creative slot packet.
     */
    public void handleCreativeSetSlot(Packet107CreativeSetSlot par1Packet107CreativeSetSlot)
    {
        if (field_72574_e.field_71134_c.func_73083_d())
        {
            boolean flag = par1Packet107CreativeSetSlot.slot < 0;
            ItemStack itemstack = par1Packet107CreativeSetSlot.itemStack;
            boolean flag1 = par1Packet107CreativeSetSlot.slot >= 1 && par1Packet107CreativeSetSlot.slot < 36 + InventoryPlayer.func_70451_h();
            boolean flag2 = itemstack == null || itemstack.itemID < Item.itemsList.length && itemstack.itemID >= 0 && Item.itemsList[itemstack.itemID] != null;
            boolean flag3 = itemstack == null || itemstack.getItemDamage() >= 0 && itemstack.getItemDamage() >= 0 && itemstack.stackSize <= 64 && itemstack.stackSize > 0;

            if (flag1 && flag2 && flag3)
            {
                if (itemstack == null)
                {
                    field_72574_e.inventorySlots.putStackInSlot(par1Packet107CreativeSetSlot.slot, null);
                }
                else
                {
                    field_72574_e.inventorySlots.putStackInSlot(par1Packet107CreativeSetSlot.slot, itemstack);
                }

                field_72574_e.inventorySlots.func_75128_a(field_72574_e, true);
            }
            else if (flag && flag2 && flag3 && field_72578_n < 200)
            {
                field_72578_n += 20;
                EntityItem entityitem = field_72574_e.dropPlayerItem(itemstack);

                if (entityitem != null)
                {
                    entityitem.func_70288_d();
                }
            }
        }
    }

    public void handleTransaction(Packet106Transaction par1Packet106Transaction)
    {
        Short short1 = (Short)field_72586_s.lookup(field_72574_e.craftingInventory.windowId);

        if (short1 != null && par1Packet106Transaction.shortWindowId == short1.shortValue() && field_72574_e.craftingInventory.windowId == par1Packet106Transaction.windowId && !field_72574_e.craftingInventory.func_75129_b(field_72574_e))
        {
            field_72574_e.craftingInventory.func_75128_a(field_72574_e, true);
        }
    }

    /**
     * Updates Client side signs
     */
    public void handleUpdateSign(Packet130UpdateSign par1Packet130UpdateSign)
    {
        WorldServer worldserver = field_72573_d.func_71218_a(field_72574_e.dimension);

        if (worldserver.blockExists(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition))
        {
            TileEntity tileentity = worldserver.getBlockTileEntity(par1Packet130UpdateSign.xPosition, par1Packet130UpdateSign.yPosition, par1Packet130UpdateSign.zPosition);

            if (tileentity instanceof TileEntitySign)
            {
                TileEntitySign tileentitysign = (TileEntitySign)tileentity;

                if (!tileentitysign.isEditable())
                {
                    field_72573_d.func_71236_h((new StringBuilder()).append("Player ").append(field_72574_e.username).append(" just tried to change non-editable sign").toString());
                    return;
                }
            }

            for (int i = 0; i < 4; i++)
            {
                boolean flag = true;

                if (par1Packet130UpdateSign.signLines[i].length() > 15)
                {
                    flag = false;
                }
                else
                {
                    for (int l = 0; l < par1Packet130UpdateSign.signLines[i].length(); l++)
                    {
                        if (ChatAllowedCharacters.allowedCharacters.indexOf(par1Packet130UpdateSign.signLines[i].charAt(l)) < 0)
                        {
                            flag = false;
                        }
                    }
                }

                if (!flag)
                {
                    par1Packet130UpdateSign.signLines[i] = "!?";
                }
            }

            if (tileentity instanceof TileEntitySign)
            {
                int j = par1Packet130UpdateSign.xPosition;
                int k = par1Packet130UpdateSign.yPosition;
                int i1 = par1Packet130UpdateSign.zPosition;
                TileEntitySign tileentitysign1 = (TileEntitySign)tileentity;
                System.arraycopy(par1Packet130UpdateSign.signLines, 0, tileentitysign1.signText, 0, 4);
                tileentitysign1.onInventoryChanged();
                worldserver.markBlockNeedsUpdate(j, k, i1);
            }
        }
    }

    /**
     * Handle a keep alive packet.
     */
    public void handleKeepAlive(Packet0KeepAlive par1Packet0KeepAlive)
    {
        if (par1Packet0KeepAlive.randomId == field_72585_i)
        {
            int i = (int)(System.nanoTime() / 0xf4240L - field_72582_j);
            field_72574_e.field_71138_i = (field_72574_e.field_71138_i * 3 + i) / 4;
        }
    }

    /**
     * determine if it is a server handler
     */
    public boolean isServerHandler()
    {
        return true;
    }

    /**
     * Handle a player abilities packet.
     */
    public void handlePlayerAbilities(Packet202PlayerAbilities par1Packet202PlayerAbilities)
    {
        field_72574_e.capabilities.isFlying = par1Packet202PlayerAbilities.func_73350_f() && field_72574_e.capabilities.allowFlying;
    }

    public void func_72461_a(Packet203AutoComplete par1Packet203AutoComplete)
    {
        StringBuilder stringbuilder = new StringBuilder();
        String s;

        for (Iterator iterator = field_72573_d.func_71248_a(field_72574_e, par1Packet203AutoComplete.func_73473_d()).iterator(); iterator.hasNext(); stringbuilder.append(s))
        {
            s = (String)iterator.next();

            if (stringbuilder.length() > 0)
            {
                stringbuilder.append("\0");
            }
        }

        field_72574_e.field_71135_a.func_72567_b(new Packet203AutoComplete(stringbuilder.toString()));
    }

    public void func_72504_a(Packet204ClientInfo par1Packet204ClientInfo)
    {
        field_72574_e.func_71125_a(par1Packet204ClientInfo);
    }

    public void handleCustomPayload(Packet250CustomPayload par1Packet250CustomPayload)
    {
        if ("MC|BEdit".equals(par1Packet250CustomPayload.channel))
        {
            try
            {
                DataInputStream datainputstream = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                ItemStack itemstack = Packet.readItemStack(datainputstream);

                if (!ItemWritableBook.func_77829_a(itemstack.getTagCompound()))
                {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack itemstack2 = field_72574_e.inventory.getCurrentItem();

                if (itemstack != null && itemstack.itemID == Item.field_77821_bF.shiftedIndex && itemstack.itemID == itemstack2.itemID)
                {
                    itemstack2.setTagCompound(itemstack.getTagCompound());
                }
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
            }
        }
        else if ("MC|BSign".equals(par1Packet250CustomPayload.channel))
        {
            try
            {
                DataInputStream datainputstream1 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                ItemStack itemstack1 = Packet.readItemStack(datainputstream1);

                if (!ItemEditableBook.func_77828_a(itemstack1.getTagCompound()))
                {
                    throw new IOException("Invalid book tag!");
                }

                ItemStack itemstack3 = field_72574_e.inventory.getCurrentItem();

                if (itemstack1 != null && itemstack1.itemID == Item.field_77823_bG.shiftedIndex && itemstack3.itemID == Item.field_77821_bF.shiftedIndex)
                {
                    itemstack3.setTagCompound(itemstack1.getTagCompound());
                    itemstack3.itemID = Item.field_77823_bG.shiftedIndex;
                }
            }
            catch (Exception exception1)
            {
                exception1.printStackTrace();
            }
        }
        else if ("MC|TrSel".equals(par1Packet250CustomPayload.channel))
        {
            try
            {
                DataInputStream datainputstream2 = new DataInputStream(new ByteArrayInputStream(par1Packet250CustomPayload.data));
                int i = datainputstream2.readInt();
                Container container = field_72574_e.craftingInventory;

                if (container instanceof ContainerMerchant)
                {
                    ((ContainerMerchant)container).func_75175_c(i);
                }
            }
            catch (Exception exception2)
            {
                exception2.printStackTrace();
            }
        }
        else
        {
            net.minecraft.client.Minecraft.invokeModMethod("ModLoader", "serverCustomPayload", new Class[]{NetServerHandler.class, Packet250CustomPayload.class}, this, par1Packet250CustomPayload);
        }
    }
}
