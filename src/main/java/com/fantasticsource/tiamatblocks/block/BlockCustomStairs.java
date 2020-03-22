package com.fantasticsource.tiamatblocks.block;

import com.fantasticsource.tiamatblocks.PropertyString;
import com.fantasticsource.tools.ReflectionTool;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class BlockCustomStairs extends BlockStairs
{
    final String shortName;
    final LinkedHashMap<PropertyString, String> textures = new LinkedHashMap<>();

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


        //Apply custom properties
        textures.putAll(from.textures);

        BlockStateContainer stateContainer = createBlockState();
        ReflectionTool.set(BlockCustom.blockStateField, this, stateContainer);

        IBlockState pState = getDefaultState(), state = stateContainer.getBaseState();
        for (Map.Entry<IProperty<?>, Comparable<?>> entry : pState.getProperties().entrySet())
        {
            Object o1 = entry.getKey(), o2 = entry.getValue();
            state = state.withProperty((IProperty) o1, (Comparable) o2);
        }
        for (Map.Entry<PropertyString, String> entry : textures.entrySet()) state = state.withProperty(entry.getKey(), entry.getValue());
        setDefaultState(state);


        return this;
    }

    @Override
    protected BlockStateContainer createBlockState()
    {
        ArrayList<IProperty> properties = new ArrayList<>();
        properties.addAll(super.createBlockState().getProperties());
        if (textures != null) properties.addAll(textures.keySet());

        return new BlockStateContainer(this, properties.toArray(new IProperty[0]));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        state = super.getActualState(state, worldIn, pos);
        for (Map.Entry<PropertyString, String> entry : textures.entrySet()) state = state.withProperty(entry.getKey(), entry.getValue());
        return state;
    }
}
