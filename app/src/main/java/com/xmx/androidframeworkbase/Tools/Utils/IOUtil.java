package com.xmx.androidframeworkbase.Tools.Utils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;

/**
 * IO工具类
 *
 * @author xiaoleilu
 */
public class IOUtil {

    /**
     * 默认缓存大小
     */
    public final static int DEFAULT_BUFFER_SIZE = 1024;

    //-------------------------------------------------------------------------------------- Copy start

    /**
     * 将Reader中的内容复制到Writer中
     * 使用默认缓存大小
     *
     * @param reader Reader
     * @param writer Writer
     * @return 拷贝的字节数
     * @throws IOException
     */
    public static int copy(Reader reader, Writer writer) throws IOException {
        return copy(reader, writer, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 将Reader中的内容复制到Writer中
     */
    public static int copy(Reader reader, Writer writer, int bufferSize) throws IOException {
        char[] buffer = new char[bufferSize];
        int count = 0;
        int readSize;
        while ((readSize = reader.read(buffer, 0, bufferSize)) >= 0) {
            writer.write(buffer, 0, readSize);
            count += readSize;
        }
        writer.flush();

        return count;
    }

    /**
     * 拷贝流，使用默认Buffer大小
     *
     * @param in         输入流
     * @param out        输出流
     * @param bufferSize 缓存大小
     * @throws IOException
     */
    public static int copy(InputStream in, OutputStream out) throws IOException {
        return copy(in, out, DEFAULT_BUFFER_SIZE);
    }

    /**
     * 拷贝流
     *
     * @param in         输入流
     * @param out        输出流
     * @param bufferSize 缓存大小
     * @throws IOException
     */
    public static int copy(InputStream in, OutputStream out, int bufferSize) throws IOException {
        byte[] buffer = new byte[bufferSize];
        int count = 0;
        for (int n = -1; (n = in.read(buffer)) != -1; ) {
            out.write(buffer, 0, n);
            count += n;
        }
        out.flush();

        return count;
    }

    /**
     * 拷贝文件流，使用NIO
     *
     * @param in  输入
     * @param out 输出
     * @return 拷贝的字节数
     * @throws IOException
     */
    public static long copy(FileInputStream in, FileOutputStream out) throws IOException {
        FileChannel inChannel = in.getChannel();
        FileChannel outChannel = out.getChannel();

        return inChannel.transferTo(0, inChannel.size(), outChannel);
    }
    //-------------------------------------------------------------------------------------- Copy end

    /**
     * 从流中读取内容
     *
     * @param in      输入流
     * @param charset 字符集
     * @return 内容
     * @throws IOException
     */
    public static String getString(InputStream in, String charset) throws IOException {
        StringBuilder content = new StringBuilder(); // 存储返回的内容

        // 从返回的内容中读取所需内容
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, charset));
        String line = null;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }

        return content.toString();
    }
}