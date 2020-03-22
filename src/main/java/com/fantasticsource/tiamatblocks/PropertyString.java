package com.fantasticsource.tiamatblocks;

import com.google.common.base.Optional;
import net.minecraft.block.properties.PropertyHelper;

import java.util.Arrays;
import java.util.Collection;

public class PropertyString extends PropertyHelper<String>
{
    protected final String[] allowedValues;

    public PropertyString(String name, String... allowedValues)
    {
        super(name, String.class);

        this.allowedValues = allowedValues;
    }

    @Override
    public Collection<String> getAllowedValues()
    {
        return Arrays.asList(allowedValues);
    }

    @Override
    public Optional<String> parseValue(String value)
    {
        return Optional.of(value);
    }

    @Override
    public String getName(String value)
    {
        return value;
    }
}
