package com.xmx.androidframeworkbase.Tools.Utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.util.Collection;

/**
 * 文件工具类
 *
 * @author xiaoleilu
 */
public class FileUtil {

    /**
     * The Unix separator character.
     */
    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * 创建文件，如果这个文件存在，直接返回这个文件
     *
     * @param fullFilePath 文件的全路径，使用POSIX风格
     * @return 文件，若路径为null，返回null
     * @throws IOException
     */
    public static File touch(String fullFilePath) throws IOException {
        if (fullFilePath == null) {
            return null;
        }
        File file = new File(fullFilePath);

        file.getParentFile().mkdirs();
        if (!file.exists()) file.createNewFile();
        return file;
    }

    /**
     * 创建文件夹，如果存在直接返回此文件夹
     *
     * @param dirPath 文件夹路径，使用POSIX格式，无论哪个平台
     * @return 创建的目录
     */
    public static File mkdir(String dirPath) {
        if (dirPath == null) {
            return null;
        }
        File dir = new File(dirPath);
        dir.mkdirs();
        return dir;
    }

    /**
     * 创建临时文件<br>
     * 创建后的文件名为 prefix[Randon].suffix
     * From com.jodd.io.FileUtil
     *
     * @param prefix    前缀，至少3个字符
     * @param suffix    后缀，如果null则使用默认.tmp
     * @param dir       临时文件创建的所在目录
     * @param isReCreat 是否重新创建文件（删掉原来的，创建新的）
     * @return
     * @throws IOException
     */
    public static File createTempFile(String prefix, String suffix, File dir, boolean isReCreat) throws IOException {
        int exceptionsCount = 0;
        while (true) {
            try {
                File file = File.createTempFile(prefix, suffix, dir).getCanonicalFile();
                if (isReCreat) {
                    file.delete();
                    file.createNewFile();
                }
                return file;
            } catch (IOException ioex) {    // fixes java.io.WinNTFileSystem.createFileExclusively access denied
                if (++exceptionsCount >= 50) {
                    throw ioex;
                }
            }
        }
    }

    /**
     * 复制文件<br>
     * 如果目标文件为目录，则将源文件以相同文件名拷贝到目标目录
     *
     * @param src        源文件
     * @param dest       目标文件或目录
     * @param isOverride 是否覆盖目标文件
     * @throws IOException
     */
    public static void copy(File src, File dest, boolean isOverride) throws IOException {
        //check
        if (!src.exists()) {
            throw new FileNotFoundException("File not exist: " + src);
        }
        if (!src.isFile()) {
            throw new IOException("Not a file:" + src);
        }
        if (equals(src, dest)) {
            throw new IOException("Files '" + src + "' and '" + dest + "' are equal");
        }

        if (dest.exists()) {
            if (dest.isDirectory()) {
                dest = new File(dest, src.getName());
            }
            if (dest.exists() && !isOverride) {
                throw new IOException("File already exist: " + dest);
            }
        }

        // do copy file
        FileInputStream input = new FileInputStream(src);
        FileOutputStream output = new FileOutputStream(dest);
        try {
            IOUtil.copy(input, output);
        } finally {
            close(output);
            close(input);
        }

        if (src.length() != dest.length()) {
            throw new IOException("Copy file failed of '" + src + "' to '" + dest + "' due to different sizes");
        }
    }

    /**
     * 移动文件或者目录
     *
     * @param src        源文件或者目录
     * @param dest       目标文件或者目录
     * @param isOverride 是否覆盖目标
     * @throws IOException
     */
    public static void move(File src, File dest, boolean isOverride) throws IOException {
        //check
        if (!src.exists()) {
            throw new FileNotFoundException("File already exist: " + src);
        }
        if (dest.exists()) {
            if (!isOverride) {
                throw new IOException("File already exist: " + dest);
            }
            dest.delete();
        }

        //来源为文件夹，目标为文件
        if (src.isDirectory() && dest.isFile()) {
            throw new IOException(StrUtil.format("Can not move directory [{}] to file [{}]", src, dest));
        }

        //来源为文件，目标为文件夹
        if (src.isFile() && dest.isDirectory()) {
            dest = new File(dest, src.getName());
        }

        if (src.renameTo(dest) == false) {
            //在文件系统不同的情况下，renameTo会失败，此时使用copy，然后删除原文件
            try {
                copy(src, dest, isOverride);
                src.delete();
            } catch (Exception e) {
                throw new IOException(StrUtil.format("Move [{}] to [{}] failed!", src, dest), e);
            }

        }
    }

    /**
     * 获取绝对路径<br/>
     * 此方法不会判定给定路径是否有效（文件或目录存在）
     *
     * @param path      相对路径
     * @param baseClass 相对路径所相对的类
     * @return 绝对路径
     */
    public static String getAbsolutePath(String path, Class<?> baseClass) {
        if (path == null) {
            path = StrUtil.EMPTY;
        }
        if (baseClass == null) {
            return getAbsolutePath(path);
        }
        return baseClass.getResource(path).getPath();
    }

    /**
     * 获取绝对路径，相对于classes的根目录
     *
     * @param pathBaseClassLoader 相对路径
     * @return 绝对路径
     */
    public static String getAbsolutePath(String pathBaseClassLoader) {
        if (pathBaseClassLoader == null) {
            pathBaseClassLoader = StrUtil.EMPTY;
        }

        ClassLoader classLoader = ClassUtil.getClassLoader();
        URL url = classLoader.getResource(pathBaseClassLoader);
        if (url == null) {
            return classLoader.getResource(StrUtil.EMPTY).getPath() + pathBaseClassLoader;
        }
        return url.getPath();
    }

    /**
     * 文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    public static boolean isExist(String path) {
        return new File(path).exists();
    }

    /**
     * 关闭
     *
     * @param closeable 被关闭的对象
     */
    public static void close(Closeable closeable) {
        if (closeable == null) return;
        try {
            closeable.close();
        } catch (IOException e) {
        }
    }

    /**
     * 检查两个文件是否是同一个文件
     *
     * @param file1 文件1
     * @param file2 文件2
     * @return 是否相同
     */
    public static boolean equals(File file1, File file2) {
        try {
            file1 = file1.getCanonicalFile();
            file2 = file2.getCanonicalFile();
        } catch (IOException ignore) {
            return false;
        }
        return file1.equals(file2);
    }

    /**
     * 获得一个带缓存的写入对象
     *
     * @param path     输出路径，绝对路径
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedWriter getBufferedWriter(String path, String charset, boolean isAppend) throws IOException {
        return new BufferedWriter(
                new OutputStreamWriter(
                        new FileOutputStream(touch(path), isAppend), charset
                )
        );
    }

    /**
     * 获得一个打印写入对象，可以有print
     *
     * @param path     输出路径，绝对路径
     * @param charset  字符集
     * @param isAppend 是否追加
     * @return 打印对象
     * @throws IOException
     */
    public static PrintWriter getPrintWriter(String path, String charset, boolean isAppend) throws IOException {
        return new PrintWriter(getBufferedWriter(path, charset, isAppend));
    }

    /**
     * 获得一个输出流对象
     *
     * @param path 输出到的文件路径，绝对路径
     * @return 输出流对象
     * @throws IOException
     */
    public static OutputStream getOutputStream(String path) throws IOException {
        return new FileOutputStream(touch(path));
    }

    /**
     * 清空一个目录
     *
     * @param dirPath 需要删除的文件夹路径
     */
    public static void cleanDir(String dirPath) {
        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File file : files) {
                if (file.isDirectory()) cleanDir(file.getAbsolutePath());
                file.delete();
            }
        }
    }

    /**
     * 获得一个文件读取器
     *
     * @param path    绝对路径
     * @param charset 字符集
     * @return BufferedReader对象
     * @throws IOException
     */
    public static BufferedReader getReader(String path, String charset) throws IOException {
        return new BufferedReader(new InputStreamReader(new FileInputStream(path), charset));
    }

    /**
     * 从文件中读取每一行数据
     *
     * @param path       文件路径
     * @param charset    字符集
     * @param collection 集合
     * @return 文件中的每行内容的集合
     * @throws IOException
     */
    public static <T extends Collection<String>> T readLines(String path, String charset, T collection) throws IOException {
        BufferedReader reader = null;
        try {
            reader = getReader(path, charset);
            while (true) {
                String line = reader.readLine();
                if (line == null) break;
                collection.add(line);
            }
            return collection;
        } finally {
            close(reader);
        }
    }

    /**
     * 按照给定的readerHandler读取文件中的数据
     *
     * @param readerHandler Reader处理类
     * @param path          文件的绝对路径
     * @param charset       字符集
     * @return 从文件中load出的数据
     * @throws IOException
     */
    public static <T> T load(ReaderHandler<T> readerHandler, String path, String charset) throws IOException {
        BufferedReader reader = null;
        T result = null;
        try {
            reader = getReader(path, charset);
            result = readerHandler.handle(reader);
        } catch (IOException e) {
            throw new IOException(e);
        } finally {
            close(reader);
        }
        return result;
    }

    /**
     * 获得文件的扩展名
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String getExtension(String fileName) {
        if (fileName == null) {
            return null;
        }
        int index = fileName.lastIndexOf(StrUtil.DOT);
        if (index == -1) {
            return StrUtil.EMPTY;
        } else {
            String ext = fileName.substring(index + 1);
            //扩展名中不能包含路径相关的符号
            return (ext.contains(String.valueOf(UNIX_SEPARATOR)) || ext.contains(String.valueOf(WINDOWS_SEPARATOR))) ? StrUtil.EMPTY : ext;
        }
    }

    /**
     * 获得最后一个文件路径分隔符的位置
     *
     * @param filePath 文件路径
     * @return
     */
    public static int indexOfLastSeparator(String filePath) {
        if (filePath == null) {
            return -1;
        }
        int lastUnixPos = filePath.lastIndexOf(UNIX_SEPARATOR);
        int lastWindowsPos = filePath.lastIndexOf(WINDOWS_SEPARATOR);
        return (lastUnixPos >= lastWindowsPos) ? lastUnixPos : lastWindowsPos;
    }

    /**
     * 将String写入文件，覆盖模式
     *
     * @param content 写入的内容
     * @param path    文件路径
     * @param charset 字符集
     * @throws IOException
     */
    public static void writeString(String content, String path, String charset) throws IOException {
        PrintWriter writer = null;
        try {
            writer = getPrintWriter(path, charset, false);
            writer.print(content);
        } finally {
            close(writer);
        }
    }

    /**
     * 读取文件内容
     *
     * @param path    文件路径
     * @param charset 字符集
     * @return 内容
     * @throws IOException
     */
    public static String readString(String path, String charset) throws IOException {
        return new String(readBytes(new File(path)), charset);
    }

    //-------------------------------------------------------------------------- Write and read bytes

    /**
     * 写数据到文件中
     *
     * @param dest 目标文件
     * @param data 数据
     * @throws IOException
     */
    public static void writeBytes(byte[] data, String path) throws IOException {
        writeBytes(touch(path), data);
    }

    /**
     * 写数据到文件中
     *
     * @param dest 目标文件
     * @param data 数据
     * @throws IOException
     */
    public static void writeBytes(File dest, byte[] data) throws IOException {
        writeBytes(dest, data, 0, data.length, false);
    }

    /**
     * 写入数据到文件
     *
     * @param dest   目标文件
     * @param data   数据
     * @param off
     * @param len
     * @param append
     * @throws IOException
     */
    public static void writeBytes(File dest, byte[] data, int off, int len, boolean append) throws IOException {
        if (dest.exists() == true) {
            if (dest.isFile() == false) {
                throw new IOException("Not a file: " + dest);
            }
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest, append);
            out.write(data, off, len);
        } finally {
            close(out);
        }
    }

    /**
     * 读取文件所有数据<br>
     * 文件的长度不能超过Integer.MAX_VALUE
     *
     * @param file 文件
     * @return 字节码
     * @throws IOException
     */
    public static byte[] readBytes(File file) throws IOException {
        //check
        if (!file.exists()) {
            throw new FileNotFoundException("File not exist: " + file);
        }
        if (!file.isFile()) {
            throw new IOException("Not a file:" + file);
        }

        long len = file.length();
        if (len >= Integer.MAX_VALUE) {
            throw new IOException("File is larger then max array size");
        }

        byte[] bytes = new byte[(int) len];
        FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(bytes);
        } finally {
            close(in);
        }

        return bytes;
    }

    // ---------------------------------------------------------------- stream

    /**
     * 将流的内容写入文件<br>
     *
     * @param dest 目标文件
     * @param in   输入流
     * @throws IOException
     */
    public static void writeStream(File dest, InputStream in) throws IOException {
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(dest);
            IOUtil.copy(in, out);
        } finally {
            close(out);
        }
    }

    /**
     * 将流的内容写入文件<br>
     *
     * @param fullFilePath 文件绝对路径
     * @param in           输入流
     * @throws IOException
     */
    public static void writeStream(String fullFilePath, InputStream in) throws IOException {
        writeStream(touch(fullFilePath), in);
    }

    //-------------------------------------------------------------------------- Interface

    /**
     * Reader处理接口
     *
     * @param <T>
     * @author Luxiaolei
     */
    public interface ReaderHandler<T> {
        public T handle(BufferedReader reader) throws IOException;
    }

    //---------------------------------------------------------------------------------------- Private method start
    //---------------------------------------------------------------------------------------- Private method end

    public static void main(String[] args) throws IOException {
        createTempFile("test", null, mkdir("e:\\opt\\temp"), false);
    }
}
