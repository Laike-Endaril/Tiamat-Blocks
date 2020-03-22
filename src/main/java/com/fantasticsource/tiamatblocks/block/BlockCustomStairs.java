package com.fantasticsource.tiamatblocks.block;

import net.minecraft.block.BlockStairs;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class BlockCustomStairs extends BlockStairs
{
    final String shortName;

    public BlockCustomStairs(BlockCustom block, String suffix)
    {
        super(block.getDefaultState());

        shortName = block.shortName + "_" + suffix;
        setRegistryName(shortName);
        setUnlocalizedName(MODID + ":" + shortName);

        BlockCustom.BLOCKS.put(shortName, this);
    }
}
