package com.fantasticsource.tiamatblocks.block;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatblocks.Names;
import com.fantasticsource.tools.Tools;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class BlockCustom extends Block
{
    public static final LinkedHashMap<String, CreativeTabs> CREATIVE_TABS = new LinkedHashMap<>();

    public static final LinkedHashMap<String, Block> BLOCKS = new LinkedHashMap<>();
    public static final LinkedHashMap<String, ItemBlockCustom> BLOCK_ITEMS = new LinkedHashMap<>();

    protected final String shortName;
    protected final ArrayList<String> stairSets = new ArrayList<>();

    protected BlockCustom(String name, Material material, SoundType soundType, ArrayList<String> stairSets)
    {
        super(material, material.getMaterialMapColor());
        setSoundType(soundType);

        this.shortName = name;
        setRegistryName(name);
        setUnlocalizedName(MODID + ":" + name);

        BLOCKS.put(name, this);


        this.stairSets.addAll(stairSets);


//        enableStats = true;
//        blockParticleGravity = 1;
//        slipperiness = 0.6f;

//        fullBlock = getDefaultState().isOpaqueCube();

//        lightValue = 0;
//        lightOpacity = fullBlock ? 255 : 0;
//        translucent = !material.blocksLight();


//        setResistance(0);
//        setHardness(0);
//        setBlockUnbreakable();
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
                    BlockCustom block = loadBlock(file);
                    if (block != null)
                    {
                        registry.register(block);
                        for (String stairSetName : block.stairSets) registry.register(new BlockCustomStairs(block, stairSetName));
                    }
                }
            }
        }
    }


    protected static BlockCustom loadBlock(File file)
    {
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(file));



            Material material = Names.MATERIALS.get(reader.readLine().toUpperCase());
            if (material == null) return null;

            SoundType soundType = Names.SOUND_TYPES.get(reader.readLine().toUpperCase());
            if (soundType == null) return null;

            ArrayList<String> stairSets = new ArrayList<>();
            String line = reader.readLine();
            while (line != null)
            {
                String[] tokens = Tools.fixedSplit(line, ":");
                if (tokens.length == 2)
                {
                    if (tokens[0].toLowerCase().equals("stairs")) stairSets.add(tokens[1]);
                }

                line = reader.readLine();
            }


            String name = file.getName();
            int index = name.indexOf(".");
            if (index != -1) name = name.substring(0, index);


            reader.close();
            return new BlockCustom(name, material, soundType, stairSets);
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
    }
}
