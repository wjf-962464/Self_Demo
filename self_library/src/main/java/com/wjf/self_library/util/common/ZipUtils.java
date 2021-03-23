package com.wjf.self_library.util.common;

import com.wjf.self_library.util.common.constant.MemoryConstants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public final class ZipUtils {

    private static final int BUFFER_LEN = MemoryConstants.BUFFER;

    /**
     * Zip the files.
     *
     * @param srcFiles    The source of files.
     * @param zipFilePath The path of ZIP file.
     * @return {@code true}: success<br>
     * {@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFiles(final Collection<String> srcFiles, final String zipFilePath)
            throws IOException {
        return zipFiles(srcFiles, zipFilePath, null);
    }

    /**
     * Zip the files.
     *
     * @param srcFilePaths The paths of source files.
     * @param zipFilePath The path of ZIP file.
     * @param comment The comment.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFiles(
            final Collection<String> srcFilePaths, final String zipFilePath, final String comment)
            throws IOException {
        if (srcFilePaths == null || zipFilePath == null) {
            return false;
        }
        try (FileOutputStream out = new FileOutputStream(zipFilePath);
             ZipOutputStream zos = new ZipOutputStream(out)) {

            for (String srcFile : srcFilePaths) {
                File file = FileUtils.getFileByPath(srcFile);
                if (file != null && !zipFile(file, "", zos, comment)) {
                    return false;
                }
            }
            zos.finish();
            return true;
        }
    }

    /**
     * Zip the files.
     *
     * @param srcFiles The source of files.
     * @param zipFile The ZIP file.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFiles(final Collection<File> srcFiles, final File zipFile)
            throws IOException {
        return zipFiles(srcFiles, zipFile, null);
    }

    /**
     * Zip the files.
     *
     * @param srcFiles The source of files.
     * @param zipFile The ZIP file.
     * @param comment The comment.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFiles(
            final Collection<File> srcFiles, final File zipFile, final String comment)
            throws IOException {
        if (srcFiles == null || zipFile == null) {
            return false;
        }
        try (FileOutputStream out = new FileOutputStream(zipFile);
             ZipOutputStream zos = new ZipOutputStream(out)) {

            for (File srcFile : srcFiles) {
                if (!zipFile(srcFile, "", zos, comment)) {
                    return false;
                }
            }

            zos.finish();
            return true;
        }
    }

    /**
     * Zip the file.
     *
     * @param srcFilePath The path of source file.
     * @param zipFilePath The path of ZIP file.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFile(final String srcFilePath, final String zipFilePath)
            throws IOException {
        return zipFile(
                FileUtils.getFileByPath(srcFilePath), FileUtils.getFileByPath(zipFilePath), null);
    }

    /**
     * Zip the file.
     *
     * @param srcFilePath The path of source file.
     * @param zipFilePath The path of ZIP file.
     * @param comment The comment.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFile(
            final String srcFilePath, final String zipFilePath, final String comment)
            throws IOException {
        return zipFile(
                FileUtils.getFileByPath(srcFilePath),
                FileUtils.getFileByPath(zipFilePath),
                comment);
    }

    /**
     * Zip the file.
     *
     * @param srcFile The source of file.
     * @param zipFile The ZIP file.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFile(final File srcFile, final File zipFile) throws IOException {
        return zipFile(srcFile, zipFile, null);
    }

    /**
     * Zip the file.
     *
     * @param srcFile The source of file.
     * @param zipFile The ZIP file.
     * @param comment The comment.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     * @throws IOException if an I/O error has occurred
     */
    public static boolean zipFile(final File srcFile, final File zipFile, final String comment)
            throws IOException {
        if (srcFile == null || zipFile == null) {
            return false;
        }
        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            return zipFile(srcFile, "", zos, comment);
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return false;
    }

    private static boolean zipFile(
            final File srcFile, String rootPath, final ZipOutputStream zos, final String comment)
            throws IOException {
        rootPath =
                rootPath
                        + (StringUtils.isSpace(rootPath) ? "" : File.separator)
                        + srcFile.getName();
        if (srcFile.isDirectory()) {
            File[] fileList = srcFile.listFiles();
            if (fileList == null || fileList.length <= 0) {
                ZipEntry entry = new ZipEntry(rootPath + '/');
                entry.setComment(comment);
                zos.putNextEntry(entry);
                zos.closeEntry();
            } else {
                for (File file : fileList) {
                    if (!zipFile(file, rootPath, zos, comment)) {
                        return false;
                    }
                }
            }
        } else {
            try (InputStream is = new BufferedInputStream(new FileInputStream(srcFile))) {
                ZipEntry entry = new ZipEntry(rootPath);
                entry.setComment(comment);
                zos.putNextEntry(entry);
                byte[] buffer = new byte[BUFFER_LEN];
                int len;
                while ((len = is.read(buffer, 0, BUFFER_LEN)) != -1) {
                    zos.write(buffer, 0, len);
                }
                zos.closeEntry();
            }
        }
        return true;
    }

    /**
     * Unzip the file.
     *
     * @param zipFilePath The path of ZIP file.
     * @param destDirPath The path of destination directory.
     * @return the unzipped files
     * @throws IOException if unzip unsuccessfully
     */
    public static List<File> unzipFile(final String zipFilePath, final String destDirPath)
            throws IOException {
        return unzipFileByKeyword(zipFilePath, destDirPath, null);
    }

    /**
     * Unzip the file.
     *
     * @param zipFile The ZIP file.
     * @param destDir The destination directory.
     * @return the unzipped files
     * @throws IOException if unzip unsuccessfully
     */
    public static List<File> unzipFile(final File zipFile, final File destDir) throws IOException {
        return unzipFileByKeyword(zipFile, destDir, null);
    }

    /**
     * Unzip the file by keyword.
     *
     * @param zipFilePath The path of ZIP file.
     * @param destDirPath The path of destination directory.
     * @param keyword The keyboard.
     * @return the unzipped files
     * @throws IOException if unzip unsuccessfully
     */
    public static List<File> unzipFileByKeyword(
            final String zipFilePath, final String destDirPath, final String keyword)
            throws IOException {
        return unzipFileByKeyword(
                FileUtils.getFileByPath(zipFilePath),
                FileUtils.getFileByPath(destDirPath),
                keyword);
    }

    /**
     * Unzip the file by keyword.
     *
     * @param zipFile The ZIP file.
     * @param destDir The destination directory.
     * @param keyword The keyboard.
     * @return the unzipped files
     * @throws IOException if unzip unsuccessfully
     */
    public static List<File> unzipFileByKeyword(
            final File zipFile, final File destDir, final String keyword) throws IOException {
        if (zipFile == null || destDir == null) {
            return new ArrayList<>();
        }
        List<File> files = new ArrayList<>();
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<?> entries = zip.entries();
        try {
            if (StringUtils.isSpace(keyword)) {
                while (entries.hasMoreElements()) {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    String entryName = entry.getName();
                    if (entryName.contains("../")) {
                        continue;
                    }
                    if (!unzipChildFile(destDir, files, zip, entry, entryName)) {
                        return files;
                    }
                }
            } else {
                while (entries.hasMoreElements()) {
                    ZipEntry entry = ((ZipEntry) entries.nextElement());
                    String entryName = entry.getName();
                    if (entryName.contains("../")) {
                        continue;
                    }
                    if (entryName.contains(keyword)) {
                        if (!unzipChildFile(destDir, files, zip, entry, entryName)) {
                            return files;
                        }
                    }
                }
            }
        } finally {
            zip.close();
        }
        return files;
    }

    private static boolean unzipChildFile(
            final File destDir,
            final List<File> files,
            final ZipFile zip,
            final ZipEntry entry,
            final String name)
            throws IOException {
        File file = new File(destDir, name);
        files.add(file);
        if (entry.isDirectory()) {
            return FileUtils.createOrExistsDir(file);
        } else {
            if (!FileUtils.createOrExistsFile(file)) {
                return false;
            }
            try (InputStream in = new BufferedInputStream(zip.getInputStream(entry));
                 FileOutputStream fis = new FileOutputStream(file);
                 OutputStream out = new BufferedOutputStream(fis)) {

                byte[] buffer = new byte[BUFFER_LEN];
                int len;
                while ((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } catch (Exception e) {
                LogUtils.error(e);
                return false;
            }
        }
        return true;
    }

    /**
     * Return the files' path in ZIP file.
     *
     * @param zipFilePath The path of ZIP file.
     * @return the files' path in ZIP file
     * @throws IOException if an I/O error has occurred
     */
    public static List<String> getFilesPath(final String zipFilePath) throws IOException {
        return getFilesPath(FileUtils.getFileByPath(zipFilePath));
    }

    /**
     * Return the files' path in ZIP file.
     *
     * @param zipFile The ZIP file.
     * @return the files' path in ZIP file
     * @throws IOException if an I/O error has occurred
     */
    public static List<String> getFilesPath(final File zipFile) throws IOException {
        if (zipFile == null) {
            return new ArrayList<>();
        }
        List<String> paths = new ArrayList<>();
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<?> entries = zip.entries();
        while (entries.hasMoreElements()) {
            String entryName = ((ZipEntry) entries.nextElement()).getName();
            paths.add(entryName);
        }
        zip.close();
        return paths;
    }

    /**
     * Return the files' comment in ZIP file.
     *
     * @param zipFilePath The path of ZIP file.
     * @return the files' comment in ZIP file
     * @throws IOException if an I/O error has occurred
     */
    public static List<String> getComments(final String zipFilePath) throws IOException {
        return getComments(FileUtils.getFileByPath(zipFilePath));
    }

    /**
     * Return the files' comment in ZIP file.
     *
     * @param zipFile The ZIP file.
     * @return the files' comment in ZIP file
     * @throws IOException if an I/O error has occurred
     */
    public static List<String> getComments(final File zipFile) throws IOException {
        if (zipFile == null) {
            return new ArrayList<>();
        }
        List<String> comments = new ArrayList<>();
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<?> entries = zip.entries();
        while (entries.hasMoreElements()) {
            ZipEntry entry = ((ZipEntry) entries.nextElement());
            comments.add(entry.getComment());
        }
        zip.close();
        return comments;
    }
}
