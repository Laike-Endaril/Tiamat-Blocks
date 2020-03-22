package com.fantasticsource.tiamatblocks.block;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatblocks.Names;
import com.fantasticsource.tiamatblocks.PropertyString;
import com.fantasticsource.tools.ReflectionTool;
import com.fantasticsource.tools.Tools;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;

public class BlockCustom extends Block
{
    public static final Field blockStateField = ReflectionTool.getField(Block.class, "field_176227_L", "blockState");

    public static final LinkedHashMap<String, CreativeTabs> CREATIVE_TABS = new LinkedHashMap<>();

    public static final LinkedHashMap<String, Block> BLOCKS = new LinkedHashMap<>();
    public static final LinkedHashMap<String, ItemBlockCustom> BLOCK_ITEMS = new LinkedHashMap<>();


    protected final String shortName;
    final LinkedHashMap<PropertyString, String> textures = new LinkedHashMap<>();

    protected final ArrayList<String> stairSets = new ArrayList<>();

    protected BlockCustom(String name, Material material)
    {
        super(material, material.getMaterialMapColor());

        this.shortName = name;
        setRegistryName(name);
        setUnlocalizedName(MODID + ":" + name);

        BLOCKS.put(name, this);
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

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return 0;
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
                        for (String stairSetName : block.stairSets)
                        {
                            registry.register(new BlockCustomStairs(block, stairSetName).copyProperties(block));
                        }
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


            //Required inputs
            Material material = Names.MATERIALS.get(reader.readLine().toUpperCase());
            if (material == null) return null;


            //Name (from filename)
            String name = file.getName();
            int index = name.indexOf(".");
            if (index != -1) name = name.substring(0, index);
            BlockCustom block = new BlockCustom(name, material);


            //Optional inputs
            String line = reader.readLine();
            while (line != null)
            {
                String[] tokens = Tools.fixedSplit(line, ":");
                switch (tokens[0].toLowerCase())
                {
                    //Vanilla block traits
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


                    //Blockstate loading arguments
                    case "stairs":
                        block.stairSets.add(tokens[1]);
                        break;

                    //Property loading arguments
                    case "texture":
                        block.textures.put(new PropertyString(tokens[1], tokens[2]), tokens[2]);
                        break;
                }

                line = reader.readLine();
            }


            //Close reader
            reader.close();


            //Apply custom properties
            BlockStateContainer stateContainer = block.createBlockState();
            ReflectionTool.set(BlockCustom.blockStateField, block, stateContainer);

            IBlockState pState = block.getDefaultState(), state = stateContainer.getBaseState();
            for (Map.Entry<IProperty<?>, Comparable<?>> entry : pState.getProperties().entrySet())
            {
                Object o1 = entry.getKey(), o2 = entry.getValue();
                state = state.withProperty((IProperty) o1, (Comparable) o2);
            }
            for (Map.Entry<PropertyString, String> entry : block.textures.entrySet()) state = state.withProperty(entry.getKey(), entry.getValue());
            block.setDefaultState(state);


            //Return
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
    }
}
