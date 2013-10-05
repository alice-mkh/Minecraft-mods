package net.minecraft.src.ssp;

import org.lwjgl.input.Keyboard;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.GameSettings;
import net.minecraft.src.GuiScreenDemo;
import net.minecraft.src.I18n;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Minecraft;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class PlayerControllerDemo extends PlayerControllerSP
{
    private boolean field_55296_c;
    private boolean field_55294_d;
    private int field_55295_e;
    private int field_55293_f;

    public PlayerControllerDemo(Minecraft par1Minecraft)
    {
        super(par1Minecraft);
        field_55296_c = false;
        field_55294_d = false;
        field_55295_e = 0;
        field_55293_f = 0;
    }

    @Override
    public void updateController()
    {
        super.updateController();
        field_55293_f++;
        long l = mc.theWorld.getTotalWorldTime();
        long l1 = l / 24000L + 1L;

        if (!field_55296_c && field_55293_f > 20)
        {
            field_55296_c = true;
            mc.displayGuiScreen(new GuiScreenDemo());
        }

        field_55294_d = l > 0x1d6b4L;

        if (field_55294_d)
        {
            field_55295_e++;
        }

        if (l % 24000L == 500L)
        {
            if (l1 <= 6L)
            {
                mc.ingameGUI.getChatGUI().addTranslatedMessage((new StringBuilder()).append("demo.day.").append(l1).toString(), null);
            }
        }
        else if (l1 == 1L)
        {
            GameSettings gamesettings = mc.gameSettings;
            String s = null;

            if (l == 100L)
            {
                s = I18n.getString("demo.help.movement");
                s = String.format(s, new Object[]
                        {
                            Keyboard.getKeyName(gamesettings.keyBindForward.keyCode), Keyboard.getKeyName(gamesettings.keyBindLeft.keyCode), Keyboard.getKeyName(gamesettings.keyBindBack.keyCode), Keyboard.getKeyName(gamesettings.keyBindRight.keyCode)
                        });
            }
            else if (l == 175L)
            {
                s = I18n.getString("demo.help.jump");
                s = String.format(s, new Object[]
                        {
                            Keyboard.getKeyName(gamesettings.keyBindJump.keyCode)
                        });
            }
            else if (l == 250L)
            {
                s = I18n.getString("demo.help.inventory");
                s = String.format(s, new Object[]
                        {
                            Keyboard.getKeyName(gamesettings.keyBindInventory.keyCode)
                        });
            }

            if (s != null)
            {
                mc.ingameGUI.getChatGUI().addTranslatedMessage(s, null);
            }
        }
        else if (l1 == 5L && l % 24000L == 22000L)
        {
            mc.ingameGUI.getChatGUI().addTranslatedMessage("demo.day.warning", null);
        }
    }

    private void func_55292_j()
    {
        if (field_55295_e > 100)
        {
            mc.ingameGUI.getChatGUI().addTranslatedMessage("demo.reminder", null);
            field_55295_e = 0;
        }
    }

    /**
     * Called by Minecraft class when the player is hitting a block with an item. Args: x, y, z, side
     */
    @Override
    public void clickBlock(int par1, int par2, int par3, int par4)
    {
        if (field_55294_d)
        {
            func_55292_j();
            return;
        }
        else
        {
            super.clickBlock(par1, par2, par3, par4);
            return;
        }
    }

    /**
     * Called when a player damages a block and updates damage counters
     */
    @Override
    public void onPlayerDamageBlock(int par1, int par2, int par3, int par4)
    {
        if (field_55294_d)
        {
            return;
        }
        else
        {
            super.onPlayerDamageBlock(par1, par2, par3, par4);
            return;
        }
    }

    /**
     * Called when a player completes the destruction of a block
     */
    @Override
    public boolean onPlayerDestroyBlock(int par1, int par2, int par3, int par4)
    {
        if (field_55294_d)
        {
            return false;
        }
        else
        {
            return super.onPlayerDestroyBlock(par1, par2, par3, par4);
        }
    }

    /**
     * Notifies the server of things like consuming food, etc...
     */
    @Override
    public boolean sendUseItem(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack)
    {
        if (field_55294_d)
        {
            func_55292_j();
            return false;
        }
        else
        {
            return super.sendUseItem(par1EntityPlayer, par2World, par3ItemStack);
        }
    }

    /**
     * Handles a players right click
     */
    @Override
    public boolean onPlayerRightClick(EntityPlayer par1EntityPlayer, World par2World, ItemStack par3ItemStack, int par4, int par5, int par6, int par7, Vec3 vec3)
    {
        if (field_55294_d)
        {
            func_55292_j();
            return false;
        }
        else
        {
            return super.onPlayerRightClick(par1EntityPlayer, par2World, par3ItemStack, par4, par5, par6, par7, vec3);
        }
    }

    /**
     * Attacks an entity
     */
    @Override
    public void attackEntity(EntityPlayer par1EntityPlayer, Entity par2Entity)
    {
        if (field_55294_d)
        {
            func_55292_j();
            return;
        }
        else
        {
            super.attackEntity(par1EntityPlayer, par2Entity);
            return;
        }
    }
}
