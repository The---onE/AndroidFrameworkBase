package com.xmx.androidframeworkbase.Tools.Utils.Net;

import com.xmx.androidframeworkbase.Tools.Utils.StrUtil;

/**
 * HTML工具类
 *
 * @author Luxiaolei
 */
public class HtmlUtil {

    public static final String RE_HTML_MARK = "(<.*?>)|(<[\\s]*?/.*?>)|(<.*?/[\\s]*?>)";
    public static final String RE_SCRIPT = "<[\\s]*?script[^>]*?>.*?<[\\s]*?\\/[\\s]*?script[\\s]*?>";

    /**
     * 还原被转义的HTML特殊字符
     *
     * @param htmlStr 包含转义符的HTML内容
     * @return 转换后的字符串
     */
    public static String restoreEscaped(String htmlStr) {
        if (StrUtil.isBlank(htmlStr)) {
            return htmlStr;
        }
        return htmlStr
                .replace("&lt", "<")
                .replace("&lt;", "<")
                .replace("&gt;", ">")
                .replace("&amp;", "&")
                .replace("&quot;", "\"")
                .replace("&#39;", "'")
                .replace("&nbsp;", " ");
    }

    public static String cleanHtmlTag(String content) {
        return content.replaceAll(RE_HTML_MARK, "");
    }
}
