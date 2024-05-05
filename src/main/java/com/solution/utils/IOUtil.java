package com.solution.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

public class IOUtil {
    public static void str2File(String filePath, String value, boolean isAppend, String encode) {
        File f = new File(filePath);
        FileUtil.createFile(filePath);
        FileOutputStream fos = null;
        try {
            if (isAppend) {
                fos = new FileOutputStream(f, isAppend);
            } else {
                fos = new FileOutputStream(f);
            }
            fos.write(value.getBytes(encode));
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

    public static List<String> file2List(String filePath, String encode) {
        if (filePath == null || filePath.isEmpty()) {
            System.out.println(filePath + "为null或者是空串");
            return null;
        }

        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            System.out.println(filePath + "文件不存在或是文件夹");
            return null;
        }

        List<String> lineList = new LinkedList<String>();
        InputStreamReader read = null;
        BufferedReader br = null;
        try {
            read = new InputStreamReader(new FileInputStream(file), encode);
            br = new BufferedReader(read);
            String temp_line = null;
            while ((temp_line = br.readLine()) != null) {
                if (!"".equals(temp_line)) {
                    lineList.add(temp_line);
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (read != null) {
                    read.close();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return lineList;
    }
}