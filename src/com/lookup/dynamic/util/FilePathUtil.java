/*
 * developer spirit_demon  at 2015.
 */

package com.lookup.dynamic.util;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * Created by Administrator on 2015/8/26 0026.
 */
public class FilePathUtil {
    public static String getFileContext(String fileName) {
        File file = new File(ClassLoader.getSystemClassLoader().getResource(fileName).getFile());
        StringBuffer result = new StringBuffer();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                result.append(line).append("\n");
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }
}
