package com.xmx.androidframeworkbase.Tools.Utils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 语言祥光工具类
 *
 * @author xiaoleilu
 */
public class LangUtil {
    private static Logger log = LoggerFactory.getLogger(LangUtil.class);

    /**
     * 字符串是否为空 空的定义如下： <br/>
     * 1、为null <br/>
     * 2、""<br/>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * 字符串是否为空 空的定义如下： <br/>
     * 1、为null <br/>
     * 2、为不可见字符（如空格）<br/>
     * 3、""<br/>
     *
     * @param str 被检测的字符串
     * @return 是否为空
     */
    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    /**
     * 获得一个随机的字符串
     *
     * @param length 字符串的长度
     * @return 随机字符串
     */
    public static String getRandomString(int length) {
        Random random = new Random();
        StringBuffer sb = new StringBuffer();

        if (length < 1) {
            length = 1;
        }
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 强制转换类型
     *
     * @param clazz 被转换成的类型
     * @param value 需要转换的对象
     * @return 转换后的对象
     */
    public static Object cast(Class<?> clazz, Object value) {
        try {
            return clazz.cast(value);
        } catch (ClassCastException e) {
            String valueStr = String.valueOf(value);

            //判断标准日期
            if (clazz.isAssignableFrom(Date.class)) {
                try {
                    return DateUtil.parseDateTime(valueStr);
                } catch (Exception parseE) {
                    log.warn("Not standard format date: [{}]", valueStr);
                    return valueStr;
                }
            }

            //基本类型
            switch (BASIC_TYPE.valueOf(clazz.getSimpleName().toUpperCase())) {
                case BYTE:
                    return Byte.parseByte(valueStr);
                case SHORT:
                    return Short.parseShort(valueStr);
                case INT:
                    return Integer.parseInt(valueStr);
                case LONG:
                    return Long.parseLong(valueStr);
                case DOUBLE:
                    return Double.parseDouble(valueStr);
                case FLOAT:
                    return Float.parseFloat(valueStr);
                case BOOLEAN:
                    return Boolean.parseBoolean(valueStr);
                case CHAR:
                    return valueStr.charAt(0);
                default:
                    return value;
            }
        }
    }

    /**
     * 转换基本类型
     *
     * @param clazz
     * @return
     */
    public static Class<?> castToPrimitive(Class<?> clazz) {
        BASIC_TYPE basicType;
        try {
            basicType = BASIC_TYPE.valueOf(clazz.getSimpleName().toUpperCase());
        } catch (Exception e) {
            return clazz;
        }
        //基本类型
        switch (basicType) {
            case BYTE:
                return byte.class;
            case SHORT:
                return short.class;
            case INT:
                return int.class;
            case LONG:
                return long.class;
            case DOUBLE:
                return double.class;
            case FLOAT:
                return float.class;
            case BOOLEAN:
                return boolean.class;
            case CHAR:
                return char.class;
            default:
                return clazz;
        }
    }

    /**
     * 获得set或get方法对应的标准属性名<br/>
     * 例如：setName 返回 name
     *
     * @param methodNameWithGet
     * @return 如果是set或get方法名，返回field， 否则null
     */
    public static String getGeneralField(String methodNameWithGet) {
        if (methodNameWithGet.startsWith("get") || methodNameWithGet.startsWith("set")) {
            return cutPreAndLowerFirst(methodNameWithGet, 3);
        }
        return null;
    }

    /**
     * 生成set方法名<br/>
     * 例如：name 返回 setName
     *
     * @param fieldName 属性名
     * @return setXxx
     */
    public static String genSetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "set");
    }

    /**
     * 生成get方法名
     *
     * @param fieldName 属性名
     * @return getXxx
     */
    public static String genGetter(String fieldName) {
        return upperFirstAndAddPre(fieldName, "get");
    }

    /**
     * 去掉首部指定长度的字符串并将剩余字符串首字母小写<br/>
     * 例如：str=setName, preLength=3 -> return name
     *
     * @param str       被处理的字符串
     * @param preLength 去掉的长度
     * @return 处理后的字符串，不符合规范返回null
     */
    public static String cutPreAndLowerFirst(String str, int preLength) {
        if (str == null) {
            return null;
        }
        if (str.length() > preLength) {
            char first = Character.toLowerCase(str.charAt(preLength));
            if (str.length() > preLength + 1) {
                return first + str.substring(preLength + 1);
            }
            return String.valueOf(first);
        }
        return null;
    }

    /**
     * 原字符串首字母大写并在其首部添加指定字符串
     * 例如：str=name, preString=get -> return getName
     *
     * @param str       被处理的字符串
     * @param preString 添加的首部
     * @return 处理后的字符串
     */
    public static String upperFirstAndAddPre(String str, String preString) {
        if (str == null || preString == null) {
            return null;
        }
        return preString + Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 切分字符串<br/>
     * a#b#c -> [a,b,c]
     * a##b#c -> [a,"",b,c]
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @return 切分后的集合
     */
    public static List<String> split(String str, char separator) {
        return split(str, separator, 0);
    }

    /**
     * 切分字符串
     *
     * @param str       被切分的字符串
     * @param separator 分隔符字符
     * @param limit     限制分片数
     * @return 切分后的集合
     */
    public static List<String> split(String str, char separator, int limit) {
        if (str == null) {
            return null;
        }
        List<String> list = new ArrayList<String>(limit == 0 ? 16 : limit);
        if (limit == 1) {
            list.add(str);
            return list;
        }

        boolean isNotEnd = true;    //未结束切分的标志
        int strLen = str.length();
        StringBuilder sb = new StringBuilder(strLen);
        for (int i = 0; i < strLen; i++) {
            char c = str.charAt(i);
            if (isNotEnd && c == separator) {
                list.add(sb.toString());
                //清空StringBuilder
                sb.delete(0, sb.length());

                //当达到切分上限-1的量时，将所剩字符全部作为最后一个串
                if (limit != 0 && list.size() == limit - 1) {
                    isNotEnd = false;
                }
            } else {
                sb.append(c);
            }
        }
        list.add(sb.toString());
        return list;
    }

    /**
     * 重复某个字符
     *
     * @param c     被重复的字符
     * @param count 重复的数目
     * @return 重复字符字符串
     */
    public static String repeat(char c, int count) {
        char[] result = new char[count];
        for (int i = 0; i < count; i++) {
            result[i] = c;
        }
        return new String(result);
    }

    /**
     * 给定字符串转换字符编码<br/>
     * 如果参数为空，则返回原字符串，不报错。
     *
     * @param str           被转码的字符串
     * @param sourceCharset 原字符集
     * @param destCharset   目标字符集
     * @return 转换后的字符串
     */
    public static String transCharset(String str, String sourceCharset, String destCharset) {
        if (LangUtil.isEmpty(str) || LangUtil.isEmpty(sourceCharset) || LangUtil.isEmpty(destCharset)) {
            return str;
        }
        try {
            return new String(str.getBytes(sourceCharset), destCharset);
        } catch (UnsupportedEncodingException e) {
            return str;
        }
    }

    /**
     * 比较两个字符串是否相同，如果为null或者空串则算不同
     *
     * @param str1 字符串1
     * @param str2 字符串2
     * @return 是否非空相同
     */
    public static boolean equalsNotEmpty(String str1, String str2) {
        if (isEmpty(str1)) {
            return false;
        }
        return str1.equals(str2);
    }

    /**
     * 基本变量类型的枚举
     *
     * @author Luxiaolei
     */
    private static enum BASIC_TYPE {
        BYTE("byte"), SHORT("short"), INT("int"), LONG("long"), DOUBLE("double"), FLOAT("float"), BOOLEAN("boolean"), CHAR("char");

        private Object value;

        private BASIC_TYPE(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return this.name() + " : " + value;
        }
    }
}
