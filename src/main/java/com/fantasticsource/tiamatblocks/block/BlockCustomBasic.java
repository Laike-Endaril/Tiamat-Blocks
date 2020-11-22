package com.fantasticsource.tiamatblocks.block;

import com.fantasticsource.tools.ReflectionTool;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import java.lang.reflect.Field;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class BlockCustomBasic extends Block implements ICustomBlock
{
    public static final Field BLOCK_TRANSLUCENT_FIELD = ReflectionTool.getField(Block.class, "field_149785_s", "translucent");
    public final String shortName;
    public final boolean cullNeighbors;

    public BlockCustomBasic(CustomBlockLoader block, String name, boolean cullNeighbors)
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
    public BlockCustomBasic copyProperties(CustomBlockLoader from)
    {
        fullBlock = from.isFullBlock(null);
        lightOpacity = from.getLightOpacity(null);
        try
        {
            translucent = (boolean) BLOCK_TRANSLUCENT_FIELD.get(from);
        }
        catch (IllegalAccessException e)
        {
            e.printStackTrace();
        }
        lightValue = from.getLightValue(null);
        useNeighborBrightness = from.getUseNeighborBrightness(null);
        blockHardness = from.getBlockHardness(null, null, null);
        blockResistance = from.getExplosionResistance(null) * 5; //Because the output from the method is divided by 5 compared to the internal value
        enableStats = from.getEnableStats();
        needsRandomTick = from.getTickRandomly();
        setSoundType(from.getSoundType());
        blockParticleGravity = from.blockParticleGravity;
        slipperiness = from.slipperiness;

        return this;
    }
}
