package com.fantasticsource.tiamatblocks.block;

import com.fantasticsource.tools.Tools;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;

import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class ItemBlockCustom extends ItemBlock
{
    public static final LinkedHashMap<String, String[]> ITEM_CREATIVE_TABS = new LinkedHashMap<>();

    final String shortName;
    protected final String[] creativeTabLabels;

    public ItemBlockCustom(Block block)
    {
        super(block);

        shortName = block.getRegistryName().getResourcePath();
        setRegistryName(shortName);
        setUnlocalizedName(MODID + ":" + shortName);

        this.creativeTabLabels = ITEM_CREATIVE_TABS.get(shortName);

        CustomBlockLoader.BLOCK_ITEMS.put(shortName, this);
    }

    @Override
    protected boolean isInCreativeTab(CreativeTabs targetTab)
    {
        return targetTab == CreativeTabs.SEARCH || (creativeTabLabels != null && Tools.contains(creativeTabLabels, targetTab.getTabLabel()));
    }
}
