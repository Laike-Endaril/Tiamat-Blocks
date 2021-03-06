package com.fantasticsource.tiamatblocks.resourcegen;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatblocks.TiamatBlocks;
import com.fantasticsource.tiamatblocks.block.*;
import com.fantasticsource.tools.Tools;
import net.minecraft.block.Block;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.*;
import java.util.Map;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;
import static com.fantasticsource.tiamatblocks.TiamatBlocks.NAME;

public class BlockstateGenerator
{
    protected static final String IN_JAR_PATH = "assets/" + MODID + "/blockstates/", IN_DIR_PATH = "assets" + File.separator + MODID + File.separator + "blockstates" + File.separator;
    protected static final String[] VALID_DATA_TYPES = new String[]
            {
                    "basic",
                    "stairs",
                    "pillar",
                    "top",
                    "bottom_top",
                    "directional",
                    "horizontal"
            };

    public static void generate(CustomBlockLoader block, IForgeRegistry<Block> registry)
    {
        for (CustomBlockLoader.BlockData data : block.blockDataSet.values()) generate(block, data, registry);
    }

    protected static void generate(CustomBlockLoader block, CustomBlockLoader.BlockData data, IForgeRegistry<Block> registry)
    {
        if (!Tools.contains(VALID_DATA_TYPES, data.type))
        {
            System.err.println(TextFormatting.RED + "Unknown data type: " + data.type);
            System.err.println(TextFormatting.RED + "For custom block: " + data.name);
            return;
        }


        BufferedReader reader = MCTools.getJarResourceReader(TiamatBlocks.class, IN_JAR_PATH + data.type + ".json");
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
            File file = new File(MCTools.getResourcePackDir() + NAME + File.separator + IN_DIR_PATH + data.name + ".json");
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
            case "top":
            case "bottom_top":
                registry.register(new BlockCustomBasic(block, data.name, data.cullNeighbors).copyProperties(block));
                break;

            case "stairs":
                registry.register(new BlockCustomStairs(block, data.name, data.cullNeighbors).copyProperties(block));
                break;

            case "pillar":
                registry.register(new BlockCustomPillar(block, data.name, data.cullNeighbors).copyProperties(block));
                break;

            case "directional":
                registry.register(new BlockCustomDirectional(block, data.name, data.cullNeighbors).copyProperties(block));
                break;

            case "horizontal":
                registry.register(new BlockCustomHorizontal(block, data.name, data.cullNeighbors).copyProperties(block));
                break;

            default:
                System.err.println(TextFormatting.RED + "Unknown data type: " + data.type);
                System.err.println(TextFormatting.RED + "For custom block: " + data.name);
        }
    }
}
