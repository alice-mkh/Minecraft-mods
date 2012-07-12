package net.minecraft.src.backport;

import java.util.Random;
import net.minecraft.src.*;

public class WeightedRandomChestContent extends WeightedRandomChoice
{
    private int field_55220_a;
    private int field_55218_b;
    private int field_55219_c;
    private int field_55217_e;

    public WeightedRandomChestContent(int par1, int par2, int par3, int par4, int par5)
    {
        super(par5);
        field_55220_a = par1;
        field_55218_b = par2;
        field_55219_c = par3;
        field_55217_e = par4;
    }

    public static void func_55216_a(Random par0Random, WeightedRandomChestContent par1ArrayOfWeightedRandomChestContent[], TileEntityChest par2TileEntityChest, int par3)
    {
        for (int i = 0; i < par3; i++)
        {
            WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
            int j = weightedrandomchestcontent.field_55219_c + par0Random.nextInt((weightedrandomchestcontent.field_55217_e - weightedrandomchestcontent.field_55219_c) + 1);

            if (Item.itemsList[weightedrandomchestcontent.field_55220_a].getItemStackLimit() >= j)
            {
                par2TileEntityChest.setInventorySlotContents(par0Random.nextInt(par2TileEntityChest.getSizeInventory()), new ItemStack(weightedrandomchestcontent.field_55220_a, j, weightedrandomchestcontent.field_55218_b));
                continue;
            }

            for (int k = 0; k < j; k++)
            {
                par2TileEntityChest.setInventorySlotContents(par0Random.nextInt(par2TileEntityChest.getSizeInventory()), new ItemStack(weightedrandomchestcontent.field_55220_a, 1, weightedrandomchestcontent.field_55218_b));
            }
        }
    }

    public static void func_56542_a(Random par0Random, WeightedRandomChestContent par1ArrayOfWeightedRandomChestContent[], TileEntityDispenser par2TileEntityDispenser, int par3)
    {
        for (int i = 0; i < par3; i++)
        {
            WeightedRandomChestContent weightedrandomchestcontent = (WeightedRandomChestContent)WeightedRandom.getRandomItem(par0Random, par1ArrayOfWeightedRandomChestContent);
            int j = weightedrandomchestcontent.field_55219_c + par0Random.nextInt((weightedrandomchestcontent.field_55217_e - weightedrandomchestcontent.field_55219_c) + 1);

            if (Item.itemsList[weightedrandomchestcontent.field_55220_a].getItemStackLimit() >= j)
            {
                par2TileEntityDispenser.setInventorySlotContents(par0Random.nextInt(par2TileEntityDispenser.getSizeInventory()), new ItemStack(weightedrandomchestcontent.field_55220_a, j, weightedrandomchestcontent.field_55218_b));
                continue;
            }

            for (int k = 0; k < j; k++)
            {
                par2TileEntityDispenser.setInventorySlotContents(par0Random.nextInt(par2TileEntityDispenser.getSizeInventory()), new ItemStack(weightedrandomchestcontent.field_55220_a, 1, weightedrandomchestcontent.field_55218_b));
            }
        }
    }
}
