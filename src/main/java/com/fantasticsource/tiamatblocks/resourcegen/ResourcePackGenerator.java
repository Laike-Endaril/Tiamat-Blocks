package com.fantasticsource.tiamatblocks.resourcegen;

import com.fantasticsource.mctools.MCTools;
import com.fantasticsource.tiamatblocks.TiamatBlocks;
import com.fantasticsource.tools.Tools;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.ResourcePackRepository;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
        Minecraft mc = Minecraft.getMinecraft();
        if (!mc.gameSettings.resourcePacks.contains(NAME))
        {
            mc.getResourcePackRepository().updateRepositoryEntriesAll();

            List<ResourcePackRepository.Entry> list = new ArrayList<>(mc.getResourcePackRepository().getRepositoryEntries());
            for (ResourcePackRepository.Entry entry : mc.getResourcePackRepository().getRepositoryEntriesAll())
            {
                if (NAME.equals(entry.getResourcePackName()))
                {
                    list.add(entry);
                    break;
                }
            }
            mc.getResourcePackRepository().setRepositories(list);

            mc.gameSettings.resourcePacks.add(NAME);
            mc.gameSettings.saveOptions();
            mc.refreshResources();
        }
    }
}
