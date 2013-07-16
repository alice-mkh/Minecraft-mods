package net.minecraft.src.ssp;

import java.util.List;
import net.minecraft.src.CommandServerTp;
import net.minecraft.src.EntityPlayerSP;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.Minecraft;
import net.minecraft.src.NumberInvalidException;
import net.minecraft.src.WrongUsageException;

public class CommandClientTp extends CommandServerTp
{
    @Override
    public void processCommand(ICommandSender par1ICommandSender, String par2ArrayOfStr[])
    {
        if (par2ArrayOfStr.length == 3 || par2ArrayOfStr.length == 4)
        {
            Minecraft minecraft = Minecraft.getMinecraft();
            if (minecraft.theWorld != null)
            {
                int i = par2ArrayOfStr.length - 3;
                EntityPlayerSP entityplayersp = ClientCommandManager.getPlayer(par1ICommandSender, par2ArrayOfStr.length == 4 ? par2ArrayOfStr[0] : null);
                double d = func_82368_a(par1ICommandSender, entityplayersp.posX, par2ArrayOfStr[i++]);
                double d1 = func_82367_a(par1ICommandSender, entityplayersp.posY, par2ArrayOfStr[i++], 0, 0);
                double d2 = func_82368_a(par1ICommandSender, entityplayersp.posZ, par2ArrayOfStr[i++]);
                entityplayersp.setPositionAndUpdate(d, d1, d2);
                notifyAdmins(par1ICommandSender, "commands.tp.success.coordinates", new Object[]
                        {
                            entityplayersp.getEntityName(), Double.valueOf(d), Double.valueOf(d1), Double.valueOf(d2)
                        });
            }

            return;
        }
        else if (par2ArrayOfStr.length == 1 || par2ArrayOfStr.length == 2){
            EntityPlayerSP p = ClientCommandManager.getPlayer(par1ICommandSender, par2ArrayOfStr[0]);
            ClientCommandManager.getPlayer(par1ICommandSender, par2ArrayOfStr[1]);
            p.setPositionAndUpdate(p.posX, p.posY, p.posZ);
            notifyAdmins(par1ICommandSender, "commands.tp.success", new Object[]
                    {
                        p.getEntityName(), p.getEntityName()
                    });
    }
        else
        {
            throw new WrongUsageException("commands.tp.usage", new Object[0]);
        }
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return Minecraft.getMinecraft().theWorld.getWorldInfo().areCommandsAllowed();
    }

    private double func_82368_a(ICommandSender par1ICommandSender, double par2, String par4Str)
    {
        return func_82367_a(par1ICommandSender, par2, par4Str, 0xfe363c80, 0x1c9c380);
    }

    private double func_82367_a(ICommandSender par1ICommandSender, double par2, String par4Str, int par5, int par6)
    {
        boolean flag = par4Str.startsWith("~");
        double d = flag ? par2 : 0.0D;

        if (!flag || par4Str.length() > 1)
        {
            boolean flag1 = par4Str.contains(".");

            if (flag)
            {
                par4Str = par4Str.substring(1);
            }

            d += parseDouble(par1ICommandSender, par4Str);

            if (!flag1 && !flag)
            {
                d += 0.5D;
            }
        }

        if (par5 != 0 || par6 != 0)
        {
            if (d < (double)par5)
            {
                throw new NumberInvalidException("commands.generic.double.tooSmall", new Object[]
                        {
                            Double.valueOf(d), Integer.valueOf(par5)
                        });
            }

            if (d > (double)par6)
            {
                throw new NumberInvalidException("commands.generic.double.tooBig", new Object[]
                        {
                            Double.valueOf(d), Integer.valueOf(par6)
                        });
            }
        }

        return d;
    }
}
