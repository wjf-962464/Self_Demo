package com.wjf.self_library.util.common;

import com.wjf.self_library.util.common.constant.MemoryConstants;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public final class FileIoUtils {

    private static int sBufferSize = MemoryConstants.BUFFER;

    /**
     * Write file from input stream.
     *
     * @param filePath The path of file.
     * @param is       The input stream.
     * @return {@code true}: success<br>
     * {@code false}: fail
     */
    public static boolean writeFileFromIS(final String filePath, final InputStream is) {
        return writeFileFromIS(FileUtils.getFileByPath(filePath), is, false);
    }

    /**
     * Write file from input stream.
     *
     * @param filePath The path of file.
     * @param is The input stream.
     * @param append True to append, false otherwise.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromIS(
            final String filePath, final InputStream is, final boolean append) {
        return writeFileFromIS(FileUtils.getFileByPath(filePath), is, append);
    }

    /**
     * Write file from input stream.
     *
     * @param file The file.
     * @param is The input stream.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromIS(final File file, final InputStream is) {
        return writeFileFromIS(file, is, false);
    }

    /**
     * Write file from input stream.
     *
     * @param file The file.
     * @param is The input stream.
     * @param append True to append, false otherwise.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromIS(
            final File file, final InputStream is, final boolean append) {
        if (!FileUtils.createOrExistsFile(file) || is == null) {
            return false;
        }

        try (OutputStream os = new BufferedOutputStream(new FileOutputStream(file, append))) {

            byte[] data = new byte[sBufferSize];
            for (int len; (len = is.read(data)) != -1; ) {
                os.write(data, 0, len);
            }
            return true;
        } catch (IOException e) {
            LogUtils.error(e);
            return false;
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                LogUtils.error(e);
            }
        }
    }

    /**
     * Write file from bytes by stream.
     *
     * @param filePath The path of file.
     * @param bytes The bytes.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromBytesByStream(final String filePath, final byte[] bytes) {
        return writeFileFromBytesByStream(FileUtils.getFileByPath(filePath), bytes, false);
    }

    /**
     * Write file from bytes by stream.
     *
     * @param filePath The path of file.
     * @param bytes The bytes.
     * @param append True to append, false otherwise.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromBytesByStream(
            final String filePath, final byte[] bytes, final boolean append) {
        return writeFileFromBytesByStream(FileUtils.getFileByPath(filePath), bytes, append);
    }

    /**
     * Write file from bytes by stream.
     *
     * @param file The file.
     * @param bytes The bytes.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromBytesByStream(final File file, final byte[] bytes) {
        return writeFileFromBytesByStream(file, bytes, false);
    }

    /**
     * Write file from bytes by stream.
     *
     * @param file The file.
     * @param bytes The bytes.
     * @param append True to append, false otherwise.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromBytesByStream(
            final File file, final byte[] bytes, final boolean append) {
        if (bytes == null || !FileUtils.createOrExistsFile(file)) {
            return false;
        }
        try (BufferedOutputStream bos =
                     new BufferedOutputStream(new FileOutputStream(file, append))) {
            bos.write(bytes);
            return true;
        } catch (IOException e) {
            LogUtils.error(e);
            return false;
        }
    }

    /**
     * Write file from bytes by channel.
     *
     * @param filePath The path of file.
     * @param bytes The bytes.
     * @param isForce 是否写入文件
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(
            final String filePath, final byte[] bytes, final boolean isForce) {
        return writeFileFromBytesByChannel(
                FileUtils.getFileByPath(filePath), bytes, false, isForce);
    }

    /**
     * Write file from bytes by channel.
     *
     * @param filePath The path of file.
     * @param bytes The bytes.
     * @param append True to append, false otherwise.
     * @param isForce True to force write file, false otherwise.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(
            final String filePath,
            final byte[] bytes,
            final boolean append,
            final boolean isForce) {
        return writeFileFromBytesByChannel(
                FileUtils.getFileByPath(filePath), bytes, append, isForce);
    }

    /**
     * Write file from bytes by channel.
     *
     * @param file The file.
     * @param bytes The bytes.
     * @param isForce True to force write file, false otherwise.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(
            final File file, final byte[] bytes, final boolean isForce) {
        return writeFileFromBytesByChannel(file, bytes, false, isForce);
    }

    /**
     * Write file from bytes by channel.
     *
     * @param file The file.
     * @param bytes The bytes.
     * @param append True to append, false otherwise.
     * @param isForce True to force write file, false otherwise.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromBytesByChannel(
            final File file, final byte[] bytes, final boolean append, final boolean isForce) {
        if (bytes == null) {
            return false;
        }
        try (FileChannel fc = new FileOutputStream(file, append).getChannel()) {
            fc.position(fc.size());
            fc.write(ByteBuffer.wrap(bytes));
            if (isForce) {
                fc.force(true);
            }
            return true;
        } catch (Exception e) {
            LogUtils.error(e);
            return false;
        }
    }

    /**
     * Write file from string.
     *
     * @param filePath The path of file.
     * @param content The string of content.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromString(final String filePath, final String content) {
        return writeFileFromString(FileUtils.getFileByPath(filePath), content, false);
    }

    /**
     * Write file from string.
     *
     * @param filePath The path of file.
     * @param content The string of content.
     * @param append True to append, false otherwise.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromString(
            final String filePath, final String content, final boolean append) {
        return writeFileFromString(FileUtils.getFileByPath(filePath), content, append);
    }

    /**
     * Write file from string.
     *
     * @param file The file.
     * @param content The string of content.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromString(final File file, final String content) {
        return writeFileFromString(file, content, false);
    }

    /**
     * Write file from string.
     *
     * @param file The file.
     * @param content The string of content.
     * @param append True to append, false otherwise.
     * @return {@code true}: success<br>
     *     {@code false}: fail
     */
    public static boolean writeFileFromString(
            final File file, final String content, final boolean append) {
        if (file == null || content == null) {
            return false;
        }
        if (!FileUtils.createOrExistsFile(file)) {
            return false;
        }
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, append))) {

            bw.write(content);
            return true;
        } catch (IOException e) {
            LogUtils.error(e);
            return false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // the divide line of write and read
    ///////////////////////////////////////////////////////////////////////////

    /**
     * Return the lines in file.
     *
     * @param filePath The path of file.
     * @return the lines in file
     */
    public static List<String> readFile2List(final String filePath) {
        return readFile2List(FileUtils.getFileByPath(filePath), null);
    }

    /**
     * Return the lines in file.
     *
     * @param filePath The path of file.
     * @param charsetName The name of charset.
     * @return the lines in file
     */
    public static List<String> readFile2List(final String filePath, final String charsetName) {
        return readFile2List(FileUtils.getFileByPath(filePath), charsetName);
    }

    /**
     * Return the lines in file.
     *
     * @param file The file.
     * @return the lines in file
     */
    public static List<String> readFile2List(final File file) {
        return readFile2List(file, 0, 0x7FFFFFFF, null);
    }

    /**
     * Return the lines in file.
     *
     * @param file The file.
     * @param charsetName The name of charset.
     * @return the lines in file
     */
    public static List<String> readFile2List(final File file, final String charsetName) {
        return readFile2List(file, 0, 0x7FFFFFFF, charsetName);
    }

    /**
     * Return the lines in file.
     *
     * @param filePath The path of file.
     * @param st The line's index of start.
     * @param end The line's index of end.
     * @return the lines in file
     */
    public static List<String> readFile2List(final String filePath, final int st, final int end) {
        return readFile2List(FileUtils.getFileByPath(filePath), st, end, null);
    }

    /**
     * Return the lines in file.
     *
     * @param filePath The path of file.
     * @param st The line's index of start.
     * @param end The line's index of end.
     * @param charsetName The name of charset.
     * @return the lines in file
     */
    public static List<String> readFile2List(
            final String filePath, final int st, final int end, final String charsetName) {
        return readFile2List(FileUtils.getFileByPath(filePath), st, end, charsetName);
    }

    /**
     * Return the lines in file.
     *
     * @param file The file.
     * @param st The line's index of start.
     * @param end The line's index of end.
     * @return the lines in file
     */
    public static List<String> readFile2List(final File file, final int st, final int end) {
        return readFile2List(file, st, end, null);
    }

    /**
     * Return the lines in file.
     *
     * @param file The file.
     * @param st The line's index of start.
     * @param end The line's index of end.
     * @param charsetName The name of charset.
     * @return the lines in file
     */
    public static List<String> readFile2List(
            final File file, final int st, final int end, final String charsetName) {
        if (!FileUtils.isFileExists(file)) {
            return new ArrayList<>();
        }
        if (st > end) {
            return new ArrayList<>();
        }
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(new FileInputStream(file), charsetName))) {
            String line;
            int curLine = 1;
            List<String> list = new ArrayList<>();

            while ((line = reader.readLine()) != null) {
                if (curLine > end) {
                    break;
                }
                if (st <= curLine && curLine <= end) {
                    list.add(line);
                }
                ++curLine;
            }
            return list;
        } catch (IOException e) {
            LogUtils.error(e);
            return new ArrayList<>();
        }
    }

    /**
     * Return the string in file.
     *
     * @param filePath The path of file.
     * @return the string in file
     */
    public static String readFile2String(final String filePath) {
        return readFile2String(FileUtils.getFileByPath(filePath), null);
    }

    /**
     * Return the string in file.
     *
     * @param filePath The path of file.
     * @param charsetName The name of charset.
     * @return the string in file
     */
    public static String readFile2String(final String filePath, final String charsetName) {
        return readFile2String(FileUtils.getFileByPath(filePath), charsetName);
    }

    /**
     * Return the string in file.
     *
     * @param file The file.
     * @return the string in file
     */
    public static String readFile2String(final File file) {
        return readFile2String(file, null);
    }

    /**
     * Return the string in file.
     *
     * @param file The file.
     * @param charsetName The name of charset.
     * @return the string in file
     */
    public static String readFile2String(final File file, final String charsetName) {
        byte[] bytes = readFile2BytesByStream(file);
        if (bytes == null) {
            return null;
        }
        if (StringUtils.isSpace(charsetName)) {
            return new String(bytes);
        } else {
            try {
                return new String(bytes, charsetName);
            } catch (UnsupportedEncodingException e) {
                LogUtils.error(e);
                return "";
            }
        }
    }

    /**
     * Return the bytes in file by stream.
     *
     * @param filePath The path of file.
     * @return the bytes in file
     */
    public static byte[] readFile2BytesByStream(final String filePath) {
        return readFile2BytesByStream(FileUtils.getFileByPath(filePath));
    }

    /**
     * Return the bytes in file by stream.
     *
     * @param file The file.
     * @return the bytes in file
     */
    public static byte[] readFile2BytesByStream(final File file) {
        if (!FileUtils.isFileExists(file)) {
            return new byte[0];
        }
        try {
            return is2Bytes(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            LogUtils.error(e);
            return new byte[0];
        }
    }

    /**
     * Return the bytes in file by channel.
     *
     * @param filePath The path of file.
     * @return the bytes in file
     */
    public static byte[] readFile2BytesByChannel(final String filePath) {
        return readFile2BytesByChannel(FileUtils.getFileByPath(filePath));
    }

    /**
     * Return the bytes in file by channel.
     *
     * @param file The file.
     * @return the bytes in file
     */
    public static byte[] readFile2BytesByChannel(final File file) {
        if (!FileUtils.isFileExists(file)) {
            return new byte[0];
        }
        try (FileChannel fc = new RandomAccessFile(file, "r").getChannel()) {
            ByteBuffer byteBuffer = ByteBuffer.allocate((int) fc.size());
            while (true) {
                if ((fc.read(byteBuffer) < 1)) {
                    break;
                }
            }
            return byteBuffer.array();
        } catch (Exception e) {
            LogUtils.error(e);
            return new byte[0];
        }
    }

    /**
     * Set the buffer's size.
     *
     * <p>Default size equals 8192 bytes.
     *
     * @param bufferSize The buffer's size.
     */
    public static void setBufferSize(final int bufferSize) {
        sBufferSize = bufferSize;
    }

    public static byte[] is2Bytes(final InputStream is) {
        if (is == null) {
            return new byte[0];
        }
        try (ByteArrayOutputStream os = new ByteArrayOutputStream();
             InputStream is2 = is) {
            byte[] b = new byte[sBufferSize];
            int len;
            while ((len = is2.read(b, 0, sBufferSize)) != -1) {
                os.write(b, 0, len);
            }
            return os.toByteArray();
        } catch (IOException e) {
            LogUtils.error(e);
            return new byte[0];
        }
    }
}
