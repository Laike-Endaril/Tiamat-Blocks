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

    public BlockCustomStairs copyProperties(BlockCustom from)
    {
        fullBlock = from.isFullBlock(null);
        lightOpacity = from.getLightOpacity(null);
        translucent = from.isTranslucent(null);
        lightValue = from.getLightValue(null);
        useNeighborBrightness = from.getUseNeighborBrightness(null);
        blockHardness = from.getBlockHardness(null, null, null);
        blockResistance = from.getExplosionResistance(null) * 5; //Because the output form the method is divided by 5 compared to the internal value
        enableStats = from.getEnableStats();
        needsRandomTick = from.getTickRandomly();
        setSoundType(from.getSoundType());
        blockParticleGravity = from.blockParticleGravity;
        slipperiness = from.slipperiness;

        return this;
    }
}
