package capstone.boardgame.main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Kyle on 5/12/2016.
 */
public class GetResourceListing {
    private static final String tag = "GetResourceListing";

    public static List<File> addFiles(List<File> files, File dir)
    {
        if (files == null)
            files = new LinkedList<>();

        if (!dir.isDirectory())
        {
            files.add(dir);
            return files;
        }

        for (File file : dir.listFiles())
            addFiles(files, file);
        return files;
    }

    private static ArrayList<String> fileNames(String path, String addPath) {
        ArrayList<String> results = new ArrayList<>();
        File folder = new File(path + addPath);
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                ArrayList<String> subDir = fileNames(path, addPath + file.getName() + "/");
                results.addAll(subDir);
            } else {
                Log.d(tag, "Adding file: " + addPath + file.getName());
                results.add(addPath + file.getName());
            }
        }

        return results;
    }

    /**
     * List directory contents for a resource folder. Not recursive.
     * This is basically a brute-force implementation.
     * Works for regular files and also JARs.
     *
     * @author Greg Briggs
     * @param clazz Any java class that lives in the same place as the resources you want.
     * @param path Should end with "/", but not start with one.
     * @return Just the name of each member item, not the full paths.
     * @throws URISyntaxException
     * @throws IOException
     */
    public static String[] getResourceListing(Class clazz, String path) throws URISyntaxException, IOException {
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
        /* A file path: easy enough */
            try {
                ArrayList<String> paths = fileNames(dirURL.getPath(), "");
                for (String pth : paths) {
                    Log.d(tag, pth);
                }
                return paths.toArray(new String[path.length()]);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        if (dirURL == null) {
        /*
         * In case of a jar file, we can't actually find a directory.
         * Have to assume the same jar as clazz.
         */
            String me = clazz.getName().replace(".", "/")+".class";
            dirURL = clazz.getClassLoader().getResource(me);
        }

        if (dirURL.getProtocol().equals("jar")) {
        /* A JAR path */
            String jarPath = dirURL.getPath().substring(5, dirURL.getPath().indexOf("!")); //strip out only the JAR file
            JarFile jar = new JarFile(URLDecoder.decode(jarPath, "UTF-8"));
            Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            Set<String> result = new HashSet<String>(); //avoid duplicates in case it is a subdirectory
            while(entries.hasMoreElements()) {
                String name = entries.nextElement().getName();
                if (name.startsWith(path)) { //filter according to the path
                    Log.d("GetResourceListing", "Adding " + name);
                    String entry = name.substring(path.length());
                    if (entry.lastIndexOf("/") == entry.length() - 1) { //skip subdirectory itself
                        continue;
                    }
                    /*int checkSubdir = entry.indexOf("/");
                    if (checkSubdir >= 0) {
                        // if it is a subdirectory, we just return the directory name
                        entry = entry.substring(0, checkSubdir);
                    }*/
                    result.add(entry);
                }
            }
            return result.toArray(new String[result.size()]);
        }

        throw new UnsupportedOperationException("Cannot list files for URL "+dirURL);
    }
}
