package com.xmx.androidframeworkbase.utils;

/**
 * Escape / Unescape
 *
 * @author xiaoleilu
 */
public class EscapeUtil {
    /**
     * Escape编码（Unicode）
     *
     * @param content
     * @return
     */
    public static String escape(String content) {
        if (StrUtil.isBlank(content)) {
            return content;
        }

        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(content.length() * 6);

        for (i = 0; i < content.length(); i++) {

            j = content.charAt(i);

            if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))
                tmp.append(j);
            else if (j < 256) {
                tmp.append("%");
                if (j < 16) tmp.append("0");
                tmp.append(Integer.toString(j, 16));
            } else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    /**
     * Escape解码
     *
     * @param content
     * @return
     */
    public static String unescape(String content) {
        if (StrUtil.isBlank(content)) {
            return content;
        }

        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(content.length());
        int lastPos = 0, pos = 0;
        char ch;
        while (lastPos < content.length()) {
            pos = content.indexOf("%", lastPos);
            if (pos == lastPos) {
                if (content.charAt(pos + 1) == 'u') {
                    ch = (char) Integer.parseInt(content.substring(pos + 2, pos + 6), 16);
                    tmp.append(ch);
                    lastPos = pos + 6;
                } else {
                    ch = (char) Integer.parseInt(content.substring(pos + 1, pos + 3), 16);
                    tmp.append(ch);
                    lastPos = pos + 3;
                }
            } else {
                if (pos == -1) {
                    tmp.append(content.substring(lastPos));
                    lastPos = content.length();
                } else {
                    tmp.append(content.substring(lastPos, pos));
                    lastPos = pos;
                }
            }
        }
        return tmp.toString();
    }

    public static void main(String[] args) {
        String s = unescape("%u5C0F%u561E%u5C0F%u561E%u5C0F%u561E%u9001%u5230%u5BB6%u91CC%u75AF%u72C2%u5C06%u963F%u65AF%u5229%u7684%u7A7A%u95F4");
        System.out.println(s);
    }
}
