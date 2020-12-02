package com.fantasticsource.tiamatblocks.resourcegen;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatblocks.TiamatBlocks;
import com.fantasticsource.tools.Tools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.*;
import java.util.ArrayList;

import static com.fantasticsource.tiamatblocks.TiamatBlocks.MODID;
import static com.fantasticsource.tiamatblocks.TiamatBlocks.NAME;

public class ResourcePackGenerator
{
    protected static final String IN_JAR_PATH = "assets/" + MODID + "/autogen/"; //DO NOT USE File.separator FOR JAR REFERENCES
    protected static final String[] FILE_NAMES = new String[]
            {
                    "pack.png",
                    "pack.mcmeta"
            };


    public static void init()
    {
        //Copy files
        File root = new File(MCTools.getResourcePackDir() + NAME);
        while (root.exists()) Tools.deleteFilesRecursively(root);
        root.mkdirs();

        for (String filename : FILE_NAMES)
        {
            File file = new File(root.getAbsolutePath() + File.separator + filename);
            if (file.exists()) continue;


            //Create parent directories
            file.mkdirs();
            file.delete();


            //Copy file
            try
            {
                InputStream is = MCTools.getJarResourceStream(TiamatBlocks.class, IN_JAR_PATH + filename);
                FileOutputStream os = new FileOutputStream(file);

                byte[] b = new byte[1024];
                int len;
                while ((len = is.read(b, 0, 1024)) > 0)
                {
                    os.write(b, 0, len);
                }

                is.close();
                os.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }


        //Set resource pack as active
        GameSettings gameSettings = Minecraft.getMinecraft().gameSettings;
        for (String s : gameSettings.resourcePacks) System.out.println(TextFormatting.AQUA + s);
        //Check options.txt; if listed resource packs don't include the mod, add and restart MC
        boolean changed = false;
        File file = new File(MCTools.getConfigDir() + ".." + File.separator + "options.txt");
        ArrayList<String> lines = new ArrayList<>();

        //Read
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));

            String line = br.readLine();
            while (line != null)
            {
                lines.add(line);
                line = br.readLine();
            }

            br.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        //Write
        try
        {
            BufferedWriter bw = new BufferedWriter(new FileWriter(file));

            for (String line : lines)
            {
                if (line.contains("resourcePacks") && !line.contains(NAME))
                {
                    int start = line.indexOf('[') + 1, end = line.indexOf(']');

                    line = line.substring(0, start) + '"' + NAME + '"' + (line.substring(start, end).trim().equals("") ? "" : ",") + line.substring(start);
                    changed = true;
                }

                bw.write(line + "\r\n");
            }

            bw.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        if (changed)
        {
            System.out.println(TextFormatting.LIGHT_PURPLE + NAME + " resource was not loaded; adding it to resource pack list and shutting down (need to start MC one more time)");
            FMLCommonHandler.instance().exitJava(0, true);
        }
    }
}
