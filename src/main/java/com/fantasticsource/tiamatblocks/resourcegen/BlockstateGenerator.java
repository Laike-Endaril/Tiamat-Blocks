package com.fantasticsource.tiamatblocks.resourcegen;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatblocks.TiamatBlocks;
import com.fantasticsource.tiamatblocks.block.BlockCustomBasic;
import com.fantasticsource.tiamatblocks.block.BlockCustomPillar;
import com.fantasticsource.tiamatblocks.block.BlockCustomStairs;
import com.fantasticsource.tiamatblocks.block.CustomBlockLoader;
import com.fantasticsource.tools.Tools;
import net.minecraft.block.Block;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.*;
import java.util.Map;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class BlockstateGenerator
{
    protected static final String INTERNAL_PATH = "assets" + File.separator + MODID + File.separator + "blockstates" + File.separator;
    protected static final String[] VALID_DATA_TYPES = new String[]
            {
                    "basic",
                    "stairs",
                    "pillar"
            };

    public static void generate(CustomBlockLoader block, IForgeRegistry<Block> registry)
    {
        for (CustomBlockLoader.BlockData data : block.blockDataSet.values()) generate(block, data, registry);
    }

    protected static void generate(CustomBlockLoader block, CustomBlockLoader.BlockData data, IForgeRegistry<Block> registry)
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


        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            File file = new File(MCTools.getResourcePackDir() + "Tiamat Blocks" + File.separator + INTERNAL_PATH + data.name + ".json");
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
        }


        switch (data.type)
        {
            case "basic":
                registry.register(new BlockCustomBasic(block, data.name, data.cullNeighbors).copyProperties(block));
                break;

            case "stairs":
                registry.register(new BlockCustomStairs(block, data.name, data.cullNeighbors).copyProperties(block));
                break;

            case "pillar":
                registry.register(new BlockCustomPillar(block, data.name, data.cullNeighbors).copyProperties(block));
                break;

                default:
                    System.err.println(TextFormatting.RED + "Unknown data type: " + data.type);
                    System.err.println(TextFormatting.RED + "For custom block: " + data.name);
        }
    }
}
