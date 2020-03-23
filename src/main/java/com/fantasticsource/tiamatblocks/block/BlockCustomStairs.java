package com.fantasticsource.tiamatblocks.block;

import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class BlockCustomStairs extends BlockStairs
{
    final String shortName;
    protected final boolean cullNeighbors;

    public BlockCustomStairs(BlockCustomLoader block, String name, boolean cullNeighbors)
    {
        super(block.getDefaultState());

        shortName = name;
        this.cullNeighbors = cullNeighbors;
        setRegistryName(shortName);
        setUnlocalizedName(MODID + ":" + shortName);

        BlockCustomLoader.BLOCKS.put(shortName, this);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return cullNeighbors;
    }


    public BlockCustomStairs copyProperties(BlockCustomLoader from)
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
