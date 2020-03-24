package com.fantasticsource.tiamatblocks.block;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatblocks.Names;
import com.fantasticsource.tiamatblocks.resourcegen.BlockstateGenerator;
import com.fantasticsource.tools.Tools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class CustomBlockLoader extends Block
{
    public static final LinkedHashMap<String, Block> BLOCKS = new LinkedHashMap<>();
    public static final LinkedHashMap<String, ItemBlockCustom> BLOCK_ITEMS = new LinkedHashMap<>();


    public final String shortName;
    protected boolean cullNeighbors = true;
    public final LinkedHashMap<String, BlockData> blockDataSet = new LinkedHashMap<>();

    protected CustomBlockLoader(String name, Material material)
    {
        super(material, material.getMaterialMapColor());

        this.shortName = name;
        setRegistryName(name);
        setUnlocalizedName(MODID + ":" + name);

        BLOCKS.put(name, this);
    }

    @Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return cullNeighbors;
    }


    @SubscribeEvent
    public static void blockRegistry(RegistryEvent.Register<Block> event)
    {
        IForgeRegistry<Block> registry = event.getRegistry();

        File dir = new File(MCTools.getConfigDir() + MODID + File.separator + "blocks");
        if (!dir.exists()) dir.mkdirs();
        else
        {
            File[] files = dir.listFiles();
            if (files != null)
            {
                for (File file : files)
                {
                    CustomBlockLoader block = loadBlock(file);
                    if (block != null)
                    {
                        BlockstateGenerator.generate(block, registry);
                    }
                }
            }
        }
    }


    protected static CustomBlockLoader loadBlock(File file)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));


            //Required inputs
            Material material = Names.MATERIALS.get(reader.readLine().toUpperCase());
            if (material == null) return null;


            //Name (from filename)
            String name = file.getName();
            int index = name.indexOf(".");
            if (index != -1) name = name.substring(0, index);
            CustomBlockLoader block = new CustomBlockLoader(name, material);


            //Optional inputs
            String line = reader.readLine();
            while (line != null)
            {
                String[] tokens = Tools.fixedSplit(line, ";");
                String fullName;
                BlockData data;
                switch (tokens[0].toLowerCase())
                {
                    //Vanilla block properties
                    case "fullblock":
                        block.fullBlock = Boolean.parseBoolean(tokens[1]);
                        break;

                    case "opacity":
                        block.lightOpacity = Integer.parseInt(tokens[1]);
                        break;

                    case "translucent":
                        block.translucent = Boolean.parseBoolean(tokens[1]);
                        break;

                    case "light":
                        block.lightValue = Integer.parseInt(tokens[1]);
                        break;

                    case "useneighborbrightness":
                        block.useNeighborBrightness = Boolean.parseBoolean(tokens[1]);
                        break;

                    case "hardness":
                        block.blockHardness = Float.parseFloat(tokens[1]);
                        break;

                    case "resistance":
                        block.blockResistance = Float.parseFloat(tokens[1]);
                        break;

                    case "stats":
                        block.enableStats = Boolean.parseBoolean(tokens[1]);
                        break;

                    case "randomtick":
                        block.needsRandomTick = Boolean.parseBoolean(tokens[1]);
                        break;

                    case "sounds":
                        block.setSoundType(Names.SOUND_TYPES.get(tokens[1].toUpperCase()));
                        break;

                    case "particlegravity":
                        block.blockParticleGravity = Float.parseFloat(tokens[1]);
                        break;

                    case "slip":
                        block.slipperiness = Float.parseFloat(tokens[1]);
                        break;


                    //Custom block properties
                    case "cullneighbors":
                        block.cullNeighbors = Boolean.parseBoolean(tokens[1]);
                        break;


                    //Blockstate loading arguments
                    case "add":
                        fullName = name + tokens[1].trim();
                        data = new BlockData(fullName, tokens[2].trim(), false);
                        block.blockDataSet.put(fullName, data);
                        ItemBlockCustom.ITEM_CREATIVE_TABS.put(fullName, Tools.fixedSplit(tokens[3].trim(), ","));
                        for (int i = 4; i < tokens.length; i += 2)
                        {
                            data.replacements.put(tokens[i], tokens[i + 1]);
                        }
                        break;

                    //Blockstate loading arguments
                    case "addcull":
                        fullName = name + tokens[1].trim();
                        data = new BlockData(fullName, tokens[2].trim(), true);
                        block.blockDataSet.put(fullName, data);
                        ItemBlockCustom.ITEM_CREATIVE_TABS.put(fullName, Tools.fixedSplit(tokens[3].trim(), ","));
                        for (int i = 4; i < tokens.length; i += 2)
                        {
                            data.replacements.put(tokens[i], tokens[i + 1]);
                        }
                        break;
                }

                line = reader.readLine();
            }


            //Return
            reader.close();
            return block;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }


    @SubscribeEvent
    public static void itemRegistry(RegistryEvent.Register<Item> event)
    {
        IForgeRegistry<Item> registry = event.getRegistry();

        for (Block block : BLOCKS.values())
        {
            registry.register(new ItemBlockCustom(block));
        }
    }

    @SubscribeEvent
    public static void modelRegistry(ModelRegistryEvent event)
    {
        for (ItemBlockCustom item : BLOCK_ITEMS.values())
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(MODID + ":" + item.shortName, "inventory"));
        }
        Minecraft.getMinecraft().refreshResources();
    }


    public static class BlockData
    {
        public String name, type;
        public LinkedHashMap<String, String> replacements = new LinkedHashMap<>();
        public boolean cullNeighbors;

        public BlockData(String name, String type, boolean cullNeighbors)
        {
            this.name = name;
            this.type = type;
            this.cullNeighbors = cullNeighbors;
        }
    }
}
