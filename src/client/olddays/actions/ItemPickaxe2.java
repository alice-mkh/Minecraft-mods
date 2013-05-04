package net.minecraft.src;

public class ItemPickaxe2 extends ItemPickaxe
{
    public static boolean oldhardness = false;

    private static Block blocksEffectiveAgainst[];
    private static Block blocksEffectiveAgainstOld[];

    protected ItemPickaxe2(int par1, EnumToolMaterial par2EnumToolMaterial)
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
                    Block.cobblestone, Block.stoneDoubleSlab, Block.stoneSingleSlab, Block.stone, Block.sandStone, Block.cobblestoneMossy, Block.oreIron, Block.blockIron, Block.oreCoal, Block.blockGold,
                    Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice, Block.netherrack, Block.oreLapis, Block.blockLapis, Block.oreRedstone, Block.oreRedstoneGlowing, Block.rail,
                    Block.railDetector, Block.railPowered
                });
        blocksEffectiveAgainstOld = (new Block[]
                {
                    Block.cobblestone, Block.stoneDoubleSlab, Block.stoneSingleSlab, Block.stone, Block.sandStone, Block.cobblestoneMossy, Block.oreIron, Block.blockIron, Block.oreCoal, Block.blockGold,
                    Block.oreGold, Block.oreDiamond, Block.blockDiamond, Block.ice, Block.netherrack, Block.oreLapis, Block.blockLapis
                });
    }
}
