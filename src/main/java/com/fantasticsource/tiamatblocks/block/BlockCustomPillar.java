package com.fantasticsource.tiamatblocks.block;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.state.IBlockState;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class BlockCustomPillar extends BlockRotatedPillar implements ICustomBlock
{
    public final String shortName;
    public final boolean cullNeighbors;

    public BlockCustomPillar(CustomBlockLoader block, String name, boolean cullNeighbors)
    {
        super(block.getDefaultState().getMaterial());

        shortName = name;
        this.cullNeighbors = cullNeighbors;
        setRegistryName(shortName);
        setUnlocalizedName(MODID + ":" + shortName);

        CustomBlockLoader.BLOCKS.put(shortName, this);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return cullNeighbors;
    }


    @Override
    public BlockCustomPillar copyProperties(CustomBlockLoader from)
    {
        fullBlock = from.isFullBlock(null);
        lightOpacity = from.getLightOpacity(null);
        try
        {
            translucent = (boolean) BlockCustomBasic.BLOCK_TRANSLUCENT_FIELD.get(from);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
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
