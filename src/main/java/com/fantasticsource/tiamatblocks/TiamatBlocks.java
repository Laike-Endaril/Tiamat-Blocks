package com.fantasticsource.tiamatblocks;

import com.fantasticsource.tiamatblocks.block.CustomBlockLoader;
import com.fantasticsource.tiamatblocks.resourcegen.ResourcePackGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = TiamatBlocks.MODID, name = TiamatBlocks.NAME, version = TiamatBlocks.VERSION, dependencies = "required-after:fantasticlib@[1.12.2.034a,)")
public class TiamatBlocks
{
    public static final String MODID = "tiamatblocks";
    public static final String NAME = "Tiamat Blocks";
    public static final String VERSION = "1.12.2.000b";

    @Mod.EventHandler
    public static void prePreInit(FMLConstructionEvent event)
    {
        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
        {
            //Physical client
            ResourcePackGenerator.init();
        }
    }

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(TiamatBlocks.class);
        MinecraftForge.EVENT_BUS.register(CustomBlockLoader.class);
    }

    @SubscribeEvent
    public static void saveConfig(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.getModID().equals(MODID)) ConfigManager.sync(MODID, Config.Type.INSTANCE);
    }
}
