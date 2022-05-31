package com.meishe.libcommon.file;

import android.os.Environment;

import com.meishe.libbase.BaseApplication;
import com.meishe.libcommon.AndroidOS;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PathUtils {

    private static final String TAG = PathUtils.class.getName();
    private static String SDK_FILE_ROOT_DIRECTORY = "MSCapture" + File.separator;
    private static String CREASH_LOG_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "Log";
    private static String ASSET_DOWNLOAD_DIRECTORY = SDK_FILE_ROOT_DIRECTORY + "Asset";
    private static final String LICENSE_FILE_FOLDER = SDK_FILE_ROOT_DIRECTORY + "License";

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteDirectoryFile(f);
                }
            }
            /*
             * 如要保留文件夹，只删除文件，请注释这行
             * To keep the folder and delete only the files, comment this line
             * */
            file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }

    public static void deleteDirectoryFile(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    File f = files[i];
                    deleteDirectoryFile(f);
                }
            }
            /*
             * 如要保留文件夹，只删除文件，请注释这行
             * To keep the folder and delete only the files, comment this line
             * */
            //file.delete();
        } else if (file.exists()) {
            file.delete();
        }
    }


    public static String getLogDir() {
        return getFolderDirPath(CREASH_LOG_DIRECTORY);
    }


    public static String getFolderDirPath(String dstDirPathToCreate) {
        File dstFileDir = new File(Environment.getExternalStorageDirectory(), dstDirPathToCreate);
        if (AndroidOS.USE_SCOPED_STORAGE) {
            dstFileDir = new File(BaseApplication.mContext.getExternalFilesDir(""), dstDirPathToCreate);
        }
        if (!dstFileDir.exists() && !dstFileDir.mkdirs()) {
            return null;
        }
        return dstFileDir.getAbsolutePath();
    }


    public static boolean unZipFile(String zipFile, String folderPath) {
        ZipFile zfile = null;
        try {
            /*
             * 转码为GBK格式，支持中文
             * Transcode to GBK format, support Chinese
             * */
            zfile = new ZipFile(zipFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        Enumeration zList = zfile.entries();
        ZipEntry ze = null;
        byte[] buf = new byte[1024];
        while (zList.hasMoreElements()) {
            ze = (ZipEntry) zList.nextElement();
            /*
             * 列举的压缩文件里面的各个文件，判断是否为目录
             * To determine whether each file in the compressed file is a directory
             * */
            if (ze.isDirectory()) {
                String dirstr = folderPath + ze.getName();
                dirstr.trim();
                File f = new File(dirstr);
                f.mkdir();
                continue;
            }
            OutputStream os = null;
            FileOutputStream fos = null;

            File realFile = getRealFileName(folderPath, ze.getName());
            try {
                fos = new FileOutputStream(realFile);
            } catch (FileNotFoundException e) {
                return false;
            }
            os = new BufferedOutputStream(fos);
            InputStream is = null;
            try {
                is = new BufferedInputStream(zfile.getInputStream(ze));
            } catch (IOException e) {
                return false;
            }
            int readLen = 0;
            try {
                while ((readLen = is.read(buf, 0, 1024)) != -1) {
                    os.write(buf, 0, readLen);
                }
            } catch (IOException e) {
                return false;
            }
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                return false;
            }
        }
        try {
            zfile.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public static File getRealFileName(String baseDir, String absFileName) {
        absFileName = absFileName.replace("\\", "/");
        String[] dirs = absFileName.split("/");
        File ret = new File(baseDir);
        String substr = null;
        if (dirs.length > 1) {
            for (int i = 0; i < dirs.length - 1; i++) {
                substr = dirs[i];
                ret = new File(ret, substr);
            }

            if (!ret.exists()) {
                ret.mkdirs();
            }
            substr = dirs[dirs.length - 1];
            ret = new File(ret, substr);
            return ret;
        } else {
            ret = new File(ret, absFileName);
        }
        return ret;
    }


    public static String getFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.indexOf('.');
            int lastSeparator = filename.lastIndexOf('/');
            if ((dot > -1) && (dot < (filename.length()))) {
                return filename.substring(lastSeparator + 1);
            }
        }
        return filename;
    }

    public static int getAssetVersionWithPath(String path) {
        String[] strings = path.split("/");
        if (strings.length > 0) {
            String filename = strings[strings.length - 1];
            String[] parts = filename.split(".");
            if (parts.length == 3) {
                return Integer.parseInt(parts[1]);
            } else {
                return 1;
            }
        } else {
            return 1;
        }
    }

    public static String getLicenseFileFolder() {
        String dstDirPath = getFolderDirPath(LICENSE_FILE_FOLDER);
        if (dstDirPath == null) {
            return null;
        }
        return dstDirPath;
    }

}
