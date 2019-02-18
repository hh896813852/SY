package com.edusoho.yunketang.utils;

import android.text.Html;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Gh0st on 2016/6/2 002.
 */
public class StringUtils {

    /**
     * 给字符串指定位置加"*"号
     *
     * @param number 需要加"*"号的字符串
     * @param index  下标
     * @param length *号长度
     * @return 如果不符条件（下标越界），则返回原字符串
     */
    public static String hideNumber(String number, int index, int length) {
        if (!TextUtils.isEmpty(number) && number.length() >= (index + length)) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < number.length(); i++) {
                char c = number.charAt(i);
                if (i >= index && i <= (index + length - 1)) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            return sb.toString();
        }

        return number;
    }

    /**
     * 给字符串指定每隔length长度加" "空格
     *
     * @param str    需要加空格的字符串
     * @param length 每隔几位加空格
     * @return 如果不符条件（下标越界），则返回原字符串
     */
    public static String addBlank(String str, int length) {
        String blankStr = "";
        int i;
        if (str.length() < length) {
            return str;
        }
        for (i = 0; i < str.length() - length; i += length) {
            blankStr += str.substring(i, i + length) + " ";
        }
        blankStr += str.substring(i, str.length());
        return blankStr;
    }

    /**
     * EX：18994433999
     *
     * @param phoneNumber 手机号码
     * @return 189 9443 3999
     */
    public static String addBlankForPhoneNumber(String phoneNumber) {
        phoneNumber = phoneNumber.replace(" ", "");
        if (phoneNumber.length() > 3 && phoneNumber.length() <= 7) {
            phoneNumber = phoneNumber.substring(0, 3) + " " + phoneNumber.substring(3, phoneNumber.length());
        } else if (phoneNumber.length() > 7) {
            phoneNumber = phoneNumber.substring(0, 3) + " " + phoneNumber.substring(3, 7) + " " + phoneNumber.substring(7, phoneNumber.length());
        }
        return phoneNumber;
    }

    /**
     * @param oldStr    原字符串
     * @param insertStr 需要插入的字符串
     * @param location  插入位置
     */
    public static String getInsert(String oldStr, String insertStr, int location) {
        char[] str = oldStr.toCharArray();
        char[] subStr = insertStr.toCharArray();
        int str_length = str.length;
        int substr_length = subStr.length;
        //判断是否是合法的位置
        if (location < 0 || location < str.length) {
            return null;
        }
        //插入一个指定长度的字符数组
        char[] insertedStr = new char[str.length + subStr.length];
        //前面的一段
        for (int i = 0; i < location; i++) {
            insertedStr[i] = str[i];
        }
        //插入的一段
        for (int i = 0; i < substr_length; i++) {
            insertedStr[i + location] = str[i];
        }
        //后面的一段
        for (int i = 0; i < str_length; i++) {
            insertedStr[i + substr_length] = str[i];
        }

        return (insertedStr.toString());
    }


    /**
     * 手机号判断
     *
     * @return
     */
    public static boolean isMobilePhone(String mobile) {
        if (TextUtils.isEmpty(mobile)) {
            return false;
        }
        //update 1开头11位
        String telRegex = "[1]\\d{10}";// "[1]"代表第1位为数字1，"\\d{10}"代表后面是可以是0～9的数字，有10位。
        return mobile.matches(telRegex);
    }

    /**
     * 密码判断
     */
    public static boolean checkPasswordDigitOrLetter(String password) {
        return !TextUtils.isEmpty(password) && password.matches("^[0-9a-zA-Z_]+$") && password.length() >= 6 && password.length() <= 20;
    }

    /**
     * 密码判断 (6-20位数字字母组合)
     */
    public static boolean checkPasswordDigitAndLetter(String password) {
        String check = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z_]{6,20}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(password);
        return matcher.matches();
    }

    /**
     * 邮箱判断
     */
    public static boolean isEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        return email.matches("^[a-z0-9A-Z]+[- | a-z0-9A-Z . _]+@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-z]{2,}$");
    }

    /**
     * The pyvalue.
     */
    private static int[] pyvalue = new int[]{-20319, -20317, -20304, -20295, -20292, -20283, -20265, -20257, -20242, -20230, -20051, -20036, -20032,
            -20026, -20002, -19990, -19986, -19982, -19976, -19805, -19784, -19775, -19774, -19763, -19756, -19751, -19746, -19741, -19739, -19728,
            -19725, -19715, -19540, -19531, -19525, -19515, -19500, -19484, -19479, -19467, -19289, -19288, -19281, -19275, -19270, -19263, -19261,
            -19249, -19243, -19242, -19238, -19235, -19227, -19224, -19218, -19212, -19038, -19023, -19018, -19006, -19003, -18996, -18977, -18961,
            -18952, -18783, -18774, -18773, -18763, -18756, -18741, -18735, -18731, -18722, -18710, -18697, -18696, -18526, -18518, -18501, -18490,
            -18478, -18463, -18448, -18447, -18446, -18239, -18237, -18231, -18220, -18211, -18201, -18184, -18183, -18181, -18012, -17997, -17988,
            -17970, -17964, -17961, -17950, -17947, -17931, -17928, -17922, -17759, -17752, -17733, -17730, -17721, -17703, -17701, -17697, -17692,
            -17683, -17676, -17496, -17487, -17482, -17468, -17454, -17433, -17427, -17417, -17202, -17185, -16983, -16970, -16942, -16915, -16733,
            -16708, -16706, -16689, -16664, -16657, -16647, -16474, -16470, -16465, -16459, -16452, -16448, -16433, -16429, -16427, -16423, -16419,
            -16412, -16407, -16403, -16401, -16393, -16220, -16216, -16212, -16205, -16202, -16187, -16180, -16171, -16169, -16158, -16155, -15959,
            -15958, -15944, -15933, -15920, -15915, -15903, -15889, -15878, -15707, -15701, -15681, -15667, -15661, -15659, -15652, -15640, -15631,
            -15625, -15454, -15448, -15436, -15435, -15419, -15416, -15408, -15394, -15385, -15377, -15375, -15369, -15363, -15362, -15183, -15180,
            -15165, -15158, -15153, -15150, -15149, -15144, -15143, -15141, -15140, -15139, -15128, -15121, -15119, -15117, -15110, -15109, -14941,
            -14937, -14933, -14930, -14929, -14928, -14926, -14922, -14921, -14914, -14908, -14902, -14894, -14889, -14882, -14873, -14871, -14857,
            -14678, -14674, -14670, -14668, -14663, -14654, -14645, -14630, -14594, -14429, -14407, -14399, -14384, -14379, -14368, -14355, -14353,
            -14345, -14170, -14159, -14151, -14149, -14145, -14140, -14137, -14135, -14125, -14123, -14122, -14112, -14109, -14099, -14097, -14094,
            -14092, -14090, -14087, -14083, -13917, -13914, -13910, -13907, -13906, -13905, -13896, -13894, -13878, -13870, -13859, -13847, -13831,
            -13658, -13611, -13601, -13406, -13404, -13400, -13398, -13395, -13391, -13387, -13383, -13367, -13359, -13356, -13343, -13340, -13329,
            -13326, -13318, -13147, -13138, -13120, -13107, -13096, -13095, -13091, -13076, -13068, -13063, -13060, -12888, -12875, -12871, -12860,
            -12858, -12852, -12849, -12838, -12831, -12829, -12812, -12802, -12607, -12597, -12594, -12585, -12556, -12359, -12346, -12320, -12300,
            -12120, -12099, -12089, -12074, -12067, -12058, -12039, -11867, -11861, -11847, -11831, -11798, -11781, -11604, -11589, -11536, -11358,
            -11340, -11339, -11324, -11303, -11097, -11077, -11067, -11055, -11052, -11045, -11041, -11038, -11024, -11020, -11019, -11018, -11014,
            -10838, -10832, -10815, -10800, -10790, -10780, -10764, -10587, -10544, -10533, -10519, -10331, -10329, -10328, -10322, -10315, -10309,
            -10307, -10296, -10281, -10274, -10270, -10262, -10260, -10256, -10254};

    public static String[] pystr = new String[]{"a", "ai", "an", "ang", "ao", "ba", "bai", "ban", "bang", "bao", "bei", "ben", "beng", "bi", "bian",
            "biao", "bie", "bin", "bing", "bo", "bu", "ca", "cai", "can", "cang", "cao", "ce", "ceng", "cha", "chai", "chan", "chang", "chao", "che",
            "chen", "cheng", "chi", "chong", "chou", "chu", "chuai", "chuan", "chuang", "chui", "chun", "chuo", "ci", "cong", "cou", "cu", "cuan",
            "cui", "cun", "cuo", "da", "dai", "dan", "dang", "dao", "de", "deng", "di", "dian", "diao", "die", "ding", "diu", "dong", "dou", "du",
            "duan", "dui", "dun", "duo", "e", "en", "er", "fa", "fan", "fang", "fei", "fen", "feng", "fo", "fou", "fu", "ga", "gai", "gan", "gang",
            "gao", "ge", "gei", "gen", "geng", "gong", "gou", "gu", "gua", "guai", "guan", "guang", "gui", "gun", "guo", "ha", "hai", "han", "hang",
            "hao", "he", "hei", "hen", "heng", "hong", "hou", "hu", "hua", "huai", "huan", "huang", "hui", "hun", "huo", "ji", "jia", "jian",
            "jiang", "jiao", "jie", "jin", "jing", "jiong", "jiu", "ju", "juan", "jue", "jun", "ka", "kai", "kan", "kang", "kao", "ke", "ken",
            "keng", "kong", "kou", "ku", "kua", "kuai", "kuan", "kuang", "kui", "kun", "kuo", "la", "lai", "lan", "lang", "lao", "le", "lei", "leng",
            "li", "lia", "lian", "liang", "liao", "lie", "lin", "ling", "liu", "long", "lou", "lu", "lv", "luan", "lue", "lun", "luo", "ma", "mai",
            "man", "mang", "mao", "me", "mei", "men", "meng", "mi", "mian", "miao", "mie", "min", "ming", "miu", "mo", "mou", "mu", "na", "nai",
            "nan", "nang", "nao", "ne", "nei", "nen", "neng", "ni", "nian", "niang", "niao", "nie", "nin", "ning", "niu", "nong", "nu", "nv", "nuan",
            "nue", "nuo", "o", "ou", "pa", "pai", "pan", "pang", "pao", "pei", "pen", "peng", "pi", "pian", "piao", "pie", "pin", "ping", "po", "pu",
            "qi", "qia", "qian", "qiang", "qiao", "qie", "qin", "qing", "qiong", "qiu", "qu", "quan", "que", "qun", "ran", "rang", "rao", "re",
            "ren", "reng", "ri", "rong", "rou", "ru", "ruan", "rui", "run", "ruo", "sa", "sai", "san", "sang", "sao", "se", "sen", "seng", "sha",
            "shai", "shan", "shang", "shao", "she", "shen", "sheng", "shi", "shou", "shu", "shua", "shuai", "shuan", "shuang", "shui", "shun",
            "shuo", "si", "song", "sou", "su", "suan", "sui", "sun", "suo", "ta", "tai", "tan", "tang", "tao", "te", "teng", "ti", "tian", "tiao",
            "tie", "ting", "tong", "tou", "tu", "tuan", "tui", "tun", "tuo", "wa", "wai", "wan", "wang", "wei", "wen", "weng", "wo", "wu", "xi",
            "xia", "xian", "xiang", "xiao", "xie", "xin", "xing", "xiong", "xiu", "xu", "xuan", "xue", "xun", "ya", "yan", "yang", "yao", "ye", "yi",
            "yin", "ying", "yo", "yong", "you", "yu", "yuan", "yue", "yun", "za", "zai", "zan", "zang", "zao", "ze", "zei", "zen", "zeng", "zha",
            "zhai", "zhan", "zhang", "zhao", "zhe", "zhen", "zheng", "zhi", "zhong", "zhou", "zhu", "zhua", "zhuai", "zhuan", "zhuang", "zhui",
            "zhun", "zhuo", "zi", "zong", "zou", "zu", "zuan", "zui", "zun", "zuo"};


    /**
     * getChsAscii 汉字转成ASCII码
     *
     * @param chs
     * @return
     */
    public static int getChsAscii(String chs) {
        int asc = 0;
        try {
            byte[] bytes = chs.getBytes("gb2312");
            /*if (bytes == null || bytes.length > 2 || bytes.length <= 0) {
                throw new RuntimeException("illegal resource string");
            }*/
            if (bytes.length == 1) {
                asc = bytes[0];
            }
            if (bytes.length == 2) {
                int hightByte = 256 + bytes[0];
                int lowByte = 256 + bytes[1];
                asc = (256 * hightByte + lowByte) - 256 * 256;
            }
        } catch (Exception e) {
//            System.out.println("ERROR:ChineseSpelling.class-getChsAscii(String chs)" + e);
        }
        return asc;
    }

    /**
     * convert 单字解析
     *
     * @param str
     * @return
     */
    public static String convert(String str) {
        String result = null;
        int ascii = getChsAscii(str);
        if (ascii > 0 && ascii < 160) {
            result = String.valueOf((char) ascii);
        } else {
            for (int i = (pyvalue.length - 1); i >= 0; i--) {
                if (pyvalue[i] <= ascii) {
                    result = pystr[i];
                    break;
                }
            }
        }
        return result;
    }

    /**
     * getSelling 词组解析
     *
     * @param chs
     * @return
     */
    public static String getSelling(String chs) {
        String key, value;
        StringBuilder buffer = new StringBuilder();
        for (int i = 0; i < chs.length(); i++) {
            key = chs.substring(i, i + 1);
            if (key.getBytes().length >= 2) {
                value = convert(key);
                if (value == null) {
                    value = "unknown";
                }
            } else {
                value = key;
            }
            buffer.append(value);
        }
        return buffer.toString();
    }

    public static String parseEmpty(String str) {
        if (str == null || "null".equals(str.trim())) {
            str = "";
        }
        return str.trim();
    }

    public static boolean isEmpty(String str) {
        return str == null || str.trim().length() == 0 || "".equals(str);
    }

    /**
     * chineseLength 中文长度
     *
     * @param str
     * @return
     */
    public static int chineseLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                String temp = str.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    valueLength += 2;
                }
            }
        }
        return valueLength;
    }

    /**
     * strLength 字符串长度
     *
     * @param str
     * @return
     */
    public static int strLength(String str) {
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                String temp = str.substring(i, i + 1);
                if (temp.matches(chinese)) {
                    valueLength += 2;
                } else {
                    valueLength += 1;
                }
            }
        }
        return valueLength;
    }

    /**
     * subStringLength 获取指定长度的字符所在位置
     *
     * @param str
     * @param maxL
     * @return
     */
    public static int subStringLength(String str, int maxL) {
        int currentIndex = 0;
        int valueLength = 0;
        String chinese = "[\u0391-\uFFE5]";
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (temp.matches(chinese)) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
            if (valueLength >= maxL) {
                currentIndex = i;
                break;
            }
        }
        return currentIndex;
    }

    /**
     * isChinese 是否是中文
     *
     * @param str
     * @return
     */
    public static Boolean isChinese(String str) {
        Boolean isChinese = true;
        String chinese = "[\u0391-\uFFE5]";
        if (!isEmpty(str)) {
            for (int i = 0; i < str.length(); i++) {
                String temp = str.substring(i, i + 1);
                isChinese = temp.matches(chinese);
            }
        }
        return isChinese;
    }

    /**
     * isContainChinese 是否包含中文
     *
     * @param str
     * @return
     */
    public static Boolean isContainChinese(String str) {
//        Boolean isChinese = false;
//        String chinese = "[\u0391-\uFFE5]";
//        if (!isEmpty(str)) {
//            for (int i = 0; i < str.length(); i++) {
//                String temp = str.substring(i, i + 1);
//                isChinese = temp.matches(chinese);
//            }
//        }
//        return isChinese;
        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    /**
     * strFormat2 不足2位前面补0
     *
     * @param str
     * @return
     */
    public static String strFormat2(String str) {
        try {
            if (str.length() <= 1) {
                str = "0" + str;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * convert2Int 类型安全转换
     *
     * @param value
     * @param defaultValue
     * @return
     */
    public static int convert2Int(Object value, int defaultValue) {
        if (value == null || "".equals(value.toString().trim())) {
            return defaultValue;
        }
        try {
            return Double.valueOf(value.toString()).intValue();
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    /**
     * decimalFormat 指定小数输出
     */
    public static String decimalFormat(String s, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(s);
    }

    /**
     * 判断金额是否大于零
     */
    public static boolean amountIsMoreThan0(String money) {
        if (TextUtils.isEmpty(money)) {
            return false;
        }
        Double dMoney = Double.parseDouble(money);
        return dMoney > 0;
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     */
    public static String removeZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 改变字符串部分字符的颜色
     *
     * @param str   字符串
     * @param color 颜色
     * @return CharSequence
     */
    public static CharSequence replaceColor(String str, int color) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        String colorStr = Integer.toHexString(color & 0x00ffffff);
        String html = str.replaceAll("\\{", "<font color='#" + colorStr + "'>").replaceAll("\\}", "</font>");
        return Html.fromHtml(html);
    }

    /**
     * 每隔'blank4Letter'位字符加一个空格
     *
     * @param input        abcdefghijk
     * @param blank4Letter 加空格的间隙字符个数
     * @return 每'blank4Letter'位加一个空格的字符串
     */
    public static String getBlankSpaceStr(String input, int blank4Letter) {
        if (!TextUtils.isEmpty(input)) {
            String regex = "(.{" + blank4Letter + "})";
            return input.replaceAll(regex, "$1 ");
        }
        return "";
    }

    /**
     * 判断是否包含特殊字符（除英文、数字、"-"、"_"外的其他字符）
     */
    public static boolean isContainSpecialCharacter(String source) {
        String regEx = "[\\u4e00-\\u9fa5]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(source);
        String sourceLeft = m.replaceAll("");
        return sourceLeft.replaceAll("[a-z]*[A-Z]*\\d*-*_*", "").length() != 0;
    }

    /**
     * 是否都是数字
     */
    public static boolean isAllDigit(String source) {
        return source.matches("[0-9]+");
    }

    /**
     * 保留两位小数
     * ex: 0 -> 0.00
     */
    public static String formatMoney(String money) {
        if (!TextUtils.isEmpty(money)) {
            DecimalFormat df = new DecimalFormat("##.00");
            String s = df.format(Double.valueOf(money));
            if (s.startsWith(".")) {
                s = "0" + s;
            }
            return s;
        }
        return "0.00";
    }

    /**
     * 是否包含表情符
     */
    public static boolean containsEmoji(String str) {
        if(TextUtils.isEmpty(str)) {
            return false;
        }
        int len = str.length();
        for (int i = 0; i < len; i++) {
            if (isEmojiCharacter(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    private static boolean isEmojiCharacter(char codePoint) {
        return !((codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF)));
    }

    /**
     * 去除空格
     */
    public static String replaceBlank(String str) {
        String dest = "";
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
            // Pattern p2 = Pattern.compile("\\s*\"");
            // Matcher m2 = p2.matcher(dest);
            // dest = m2.replaceAll("\'");
            dest = dest.replace("=\"", "='");
            p = Pattern.compile("\"\0*>");
            m = p.matcher(dest);
            dest = m.replaceAll(">'");
        }
        return dest;
    }

    /**
     * 将坏的json数据里面的双引号，改为中文的双引号(啥都行，只要不是双引号就行)
     */
    public static String jsonStringConvert(String s) {
        char[] temp = s.toCharArray();
        int n = temp.length;
        for (int i = 0; i < n; i++) {
            if (temp[i] == ':' && temp[i + 1] == '"') {
                for (int j = i + 2; j < n; j++) {
                    if (temp[j] == '"') {
                        if (temp[j + 1] != ',' && temp[j + 1] != '}') {
                            temp[j] = '@';
                        } else if (temp[j + 1] == ',' || temp[j + 1] == '}') {
                            break;
                        }
                    }
                }
            }
        }
        return new String(temp);
    }
}