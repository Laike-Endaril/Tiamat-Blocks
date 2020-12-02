package com.fantasticsource.tiamatblocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

import java.util.LinkedHashMap;

public class Names
{
    public static final LinkedHashMap<String, Material> MATERIALS = new LinkedHashMap<>();
    public static final LinkedHashMap<String, SoundType> SOUND_TYPES = new LinkedHashMap<>();

    static
    {
        MATERIALS.put("AIR", Material.AIR);
        MATERIALS.put("GRASS", Material.GRASS);
        MATERIALS.put("GROUND", Material.GROUND);
        MATERIALS.put("DIRT", Material.GROUND);
        MATERIALS.put("SOIL", Material.GROUND);
        MATERIALS.put("WOOD", Material.WOOD);
        MATERIALS.put("ROCK", Material.ROCK);
        MATERIALS.put("STONE", Material.ROCK);
        MATERIALS.put("IRON", Material.IRON);
        MATERIALS.put("ANVIL", Material.ANVIL);
        MATERIALS.put("WATER", Material.WATER);
        MATERIALS.put("LAVA", Material.LAVA);
        MATERIALS.put("LEAVES", Material.LEAVES);
        MATERIALS.put("PLANTS", Material.PLANTS);
        MATERIALS.put("VINE", Material.VINE);
        MATERIALS.put("SPONGE", Material.SPONGE);
        MATERIALS.put("CLOTH", Material.CLOTH);
        MATERIALS.put("FIRE", Material.FIRE);
        MATERIALS.put("SAND", Material.SAND);
        MATERIALS.put("CIRCUITS", Material.CIRCUITS);
        MATERIALS.put("CARPET", Material.CARPET);
        MATERIALS.put("GLASS", Material.GLASS);
        MATERIALS.put("REDSTONE_LIGHT", Material.REDSTONE_LIGHT);
        MATERIALS.put("TNT", Material.TNT);
        MATERIALS.put("CORAL", Material.CORAL);
        MATERIALS.put("ICE", Material.ICE);
        MATERIALS.put("PACKED_ICE", Material.PACKED_ICE);
        MATERIALS.put("SNOW", Material.SNOW);
        MATERIALS.put("CRAFTED_SNOW", Material.CRAFTED_SNOW);
        MATERIALS.put("CACTUS", Material.CACTUS);
        MATERIALS.put("CLAY", Material.CLAY);
        MATERIALS.put("GOURD", Material.GOURD);
        MATERIALS.put("DRAGON_EGG", Material.DRAGON_EGG);
        MATERIALS.put("PORTAL", Material.PORTAL);
        MATERIALS.put("CAKE", Material.CAKE);
        MATERIALS.put("WEB", Material.WEB);
        MATERIALS.put("PISTON", Material.PISTON);
        MATERIALS.put("BARRIER", Material.BARRIER);
        MATERIALS.put("STRUCTURE_VOID", Material.STRUCTURE_VOID);

        SOUND_TYPES.put("WOOD", SoundType.WOOD);
        SOUND_TYPES.put("GROUND", SoundType.GROUND);
        SOUND_TYPES.put("DIRT", SoundType.GROUND);
        SOUND_TYPES.put("SOIL", SoundType.GROUND);
        SOUND_TYPES.put("PLANT", SoundType.PLANT);
        SOUND_TYPES.put("STONE", SoundType.STONE);
        SOUND_TYPES.put("ROCK", SoundType.STONE);
        SOUND_TYPES.put("METAL", SoundType.METAL);
        SOUND_TYPES.put("GLASS", SoundType.GLASS);
        SOUND_TYPES.put("CLOTH", SoundType.CLOTH);
        SOUND_TYPES.put("SAND", SoundType.SAND);
        SOUND_TYPES.put("SNOW", SoundType.SNOW);
        SOUND_TYPES.put("LADDER", SoundType.LADDER);
        SOUND_TYPES.put("ANVIL", SoundType.ANVIL);
        SOUND_TYPES.put("SLIME", SoundType.SLIME);
    }
}
