package com.fantasticsource.tiamatblocks.block;

import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class BlockCustomHorizontal extends BlockHorizontal implements ICustomBlock
{
    public final String shortName;
    public final boolean cullNeighbors;

    public BlockCustomHorizontal(CustomBlockLoader block, String name, boolean cullNeighbors)
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
    public BlockCustomHorizontal copyProperties(CustomBlockLoader from)
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
        blockResistance = from.getExplosionResistance(null) * 5; //Because the output from the method is divided by 5 compared to the internal value
        enableStats = from.getEnableStats();
        needsRandomTick = from.getTickRandomly();
        setSoundType(from.getSoundType());
        blockParticleGravity = from.blockParticleGravity;
        slipperiness = from.slipperiness;

        return this;
    }


    @Override
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, FACING);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot)
    {
        return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirrorIn)
    {
        return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(FACING).getHorizontalIndex();
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta));
    }
}
