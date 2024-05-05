package com.solution.utils;

import java.io.File;

public class FileUtil {
    /**
     * 创建文件
     */
    public static void createFile(String filePath) {
        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.getParentFile().mkdirs();
            }
            f.createNewFile();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}