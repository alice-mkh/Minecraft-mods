package net.minecraft.src;

public class ItemAxe2 extends ItemAxe
{
    public static boolean oldhardness = false;

    private static Block blocksEffectiveAgainst[];
    private static Block blocksEffectiveAgainstOld[];

    protected ItemAxe2(int par1, EnumToolMaterial par2EnumToolMaterial)
    {
        super(par1, par2EnumToolMaterial);
    }

    /**
     * Returns the strength of the stack against a given block. 1.0F base, (Quality+1)*2 if correct blocktype, 1.5F if
     * sword
     */
    @Override
    public float getStrVsBlock(ItemStack par1ItemStack, Block par2Block)
    {
        if (!oldhardness){
            return super.getStrVsBlock(par1ItemStack, par2Block);
        }
        Block ablock[] = oldhardness ? blocksEffectiveAgainstOld : blocksEffectiveAgainst;
        int i = ablock.length;

        for (int j = 0; j < i; j++)
        {
            Block block = ablock[j];

            if (block == par2Block)
            {
                return efficiencyOnProperMaterial;
            }
        }

        return 1.0F;
    }

    static
    {
        blocksEffectiveAgainst = (new Block[]
                {
                    Block.planks, Block.bookShelf, Block.wood, Block.chest, Block.stoneDoubleSlab, Block.stoneSingleSlab, Block.pumpkin, Block.pumpkinLantern
                });
        blocksEffectiveAgainstOld = (new Block[]
                {
                    Block.planks, Block.bookShelf, Block.wood, Block.chest
                });
    }
}
