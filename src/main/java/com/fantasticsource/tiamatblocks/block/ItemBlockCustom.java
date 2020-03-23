package com.fantasticsource.tiamatblocks.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class ItemBlockCustom extends ItemBlock
{
    final String shortName;

    public ItemBlockCustom(Block block)
    {
        super(block);

        shortName = block.getRegistryName().getResourcePath();
        setRegistryName(shortName);
        setUnlocalizedName(MODID + ":" + shortName);

        BlockCustomLoader.BLOCK_ITEMS.put(shortName, this);
    }
}
