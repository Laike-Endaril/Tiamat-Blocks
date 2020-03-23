package com.fantasticsource.tiamatblocks.resourcegen;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatblocks.TiamatBlocks;
import com.fantasticsource.tiamatblocks.block.BlockCustomBasic;
import com.fantasticsource.tiamatblocks.block.BlockCustomLoader;
import com.fantasticsource.tiamatblocks.block.BlockCustomStairs;
import com.fantasticsource.tools.Tools;
import net.minecraft.block.Block;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.*;
import java.util.Map;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class BlockstateGenerator
{
    protected static final String
            INTERNAL_PATH = "assets" + File.separator + MODID + File.separator + "blockstates" + File.separator,
            EXTERNAL_PATH = MCTools.getResourcePackDir() + "Tiamat Blocks" + File.separator + INTERNAL_PATH;
    protected static final String[] VALID_DATA_TYPES = new String[]
            {
                    "basic",
                    "stairs"
            };

    public static void generate(BlockCustomLoader block, IForgeRegistry<Block> registry)
    {
        for (BlockCustomLoader.BlockData data : block.blockDataSet.values()) generate(block, data, registry);
    }

    protected static void generate(BlockCustomLoader block, BlockCustomLoader.BlockData data, IForgeRegistry<Block> registry)
    {
        if (!Tools.contains(VALID_DATA_TYPES, data.type)) return;


        BufferedReader reader = MCTools.getJarResourceReader(TiamatBlocks.class, INTERNAL_PATH + data.type + ".json");
        StringBuilder builder = new StringBuilder();
        try
        {
            String line = reader.readLine();
            while (line != null)
            {
                builder.append(line).append("\r\n");
                line = reader.readLine();
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        String fullString = builder.toString();
        for (Map.Entry<String, String> entry : data.replacements.entrySet())
        {
            fullString = fullString.replaceAll(entry.getKey(), entry.getValue());
        }


        File file = new File(EXTERNAL_PATH + data.name + ".json");
        file.mkdirs();
        file.delete();
        try
        {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            writer.write(fullString);

            writer.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }


        switch (data.type)
        {
            case "basic":
                registry.register(new BlockCustomBasic(block, data.name, data.cullNeighbors).copyProperties(block));
                break;

            case "stairs":
                registry.register(new BlockCustomStairs(block, data.name, data.cullNeighbors).copyProperties(block));
                break;
        }
    }
}
