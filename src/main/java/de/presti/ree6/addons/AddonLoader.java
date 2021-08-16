package de.presti.ree6.addons;

import de.presti.ree6.main.Main;
import de.presti.ree6.utils.ArrayUtil;
import de.presti.ree6.utils.Logger;
import org.simpleyaml.configuration.file.FileConfiguration;
import org.simpleyaml.configuration.file.YamlConfiguration;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class AddonLoader {

    private static void createFolders() {
        if (!new File("addons/").exists()) {
            new File("addons/").mkdir();
        }

        if (!new File("addons/tmp/").exists()) {
            new File("addons/tmp/").mkdir();
        }
    }

    public static void loadAllAddons() {

        createFolders();

        File[] files = new File("addons/").listFiles();

        for (File f : files) {
            if (f.getName().endsWith("jar")) {
                try {
                    Addon addon = loadAddon(f.getName());
                    Main.addonManager.loadAddon(addon);
                } catch (Exception ex) {
                    Logger.log("AddonManager", "Couldnt load the Addon " + f.getName() + "\nException: " + ex.getCause().getMessage());
                    ex.printStackTrace();
                }
            }
        }

    }

    public static Addon loadAddon(String fileName) throws Exception {

        String name = null;
        String author = null;
        String addonver = null;
        String ree6ver = null;
        String mainpath = null;

        File f = null;

        ZipInputStream jis = new ZipInputStream(new FileInputStream(new File("addons/" + fileName)));
        ZipEntry entry;
        while ((entry = jis.getNextEntry()) != null) {
            try {
                String fname = entry.getName();
                if (!entry.isDirectory()) {
                    if (fname.equalsIgnoreCase("addon.yml")) {

                        f = new File("addons/tmp/temp_" + ArrayUtil.getRandomShit(9) + ".yml");
                        FileOutputStream os = new FileOutputStream(f);

                        for (int c = jis.read(); c != -1; c = jis.read()) {
                            os.write(c);
                        }

                        os.close();

                        FileConfiguration conf = YamlConfiguration.loadConfiguration(f);

                        name = conf.getString("name");
                        author = conf.getString("author");
                        addonver = conf.getString("version");
                        ree6ver = conf.getString("ree6-version");
                        mainpath = conf.getString("main");

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                jis.closeEntry();
            } finally {
                jis.closeEntry();
            }
        }
        jis.close();

        if (f != null) {
            f.delete();
        }

        if (name == null && mainpath == null) {
            throw new FileNotFoundException("Couldnt find addon.yml");
        } else {
            return new Addon(name, author, addonver, ree6ver, mainpath, new File("addons/" + fileName));
        }

    }

}