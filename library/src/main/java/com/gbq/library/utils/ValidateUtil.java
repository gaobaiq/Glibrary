package com.gbq.library.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 类说明：验证工具类
 * Author: Kuzan
 * Date: 2017/9/6 11:55.
 */
public class ValidateUtil {
    /**
     * 验证手机号码是否合法
     *
     * @param number 需要做验证的手机号码
     * @return 返回true表示合法，false表示非法
     */
    public static boolean isMobilePhone(String number) {
        /*
        * 国家号码段分配如下： 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188、147
        * 联通：130、131、132、152、155、156、185、186
        * 电信：133、153、177、180、189、（1349卫通）
        *
        * 注：手机号码必须是11位数
        */
        String telRegex = "[1][34578]\\d{9}";//"[1]"代表第1位为数字1，"[34578]"代表第二位可以为3、4、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            return number.matches(telRegex);
        }
    }

    /**
     * 验证电话号码
     *          正确格式为：XXXX-XXXXXXX，XXXX-XXXXXXXX，XXX-XXXXXXX，XXX-XXXXXXXX，XXXXXXX，XXXXXXXX
     * @param telephone 电话号码
     *
     * @return 返回true表示合法，false表示非法
     * */
    public static boolean isTelephone(String telephone) {
        Pattern p = Pattern.compile("^(\\d3,4|\\d{3,4}-)?\\d{7,8}$");
        Matcher m = p.matcher(telephone);
        return m.matches();
    }

    /**
     * 验证邮箱是否正确
     *
     * @param email     邮箱
     *
     * @return 返回true表示合法，false表示非法
     * */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";

        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 验证是否是手机或邮箱
     *
     * @param contactWay 手机号码或邮箱地址
     *
     * @return 返回true表示合法，false表示非法
     * */
    public static boolean isMobileOrEmail(String contactWay) {
        if (isMobilePhone(contactWay) || isEmail(contactWay)) {
            return true;
        }
        return false;
    }

    /**
     * 验证是否为6位数字的密码
     *
     * @param password 密码
     *
     * @return 返回true表示合法，false表示非法
     */
    public static boolean isNumPasswordAndSix(String password) {
        Pattern p = Pattern.compile("^\\d{6}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 验证用户密码(正确格式为：以字母开头，长度在6-18之间，只能包含字符、数字和下划线)
     *
     * @param password 密码
     *
     * @return 返回true表示合法，false表示非法
     * */
    public static boolean isPassword(String password) {
        Pattern p = Pattern.compile("^[a-zA-Z]\\w{5,17}$");
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 验证用户密码（数字和字母结合）
     *
     * @param password 密码
     *
     * @return 返回true表示合法，false表示非法
     * */
    public static boolean isSimplePassword(String password) {
        // 复杂匹配
        String str = "^(((?=.*[0-9].*)(?=.*[a-zA-Z].*))|((?=.*([^0-9a-zA-Z]).*)(?=.*[a-zA-Z].*))|((?=.*([^0-9a-zA-Z]).*)(?=.*[0-9].*))).{6,20}$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    /**
     * 验证是否为身份证号码（15位或18位数字）
     *
     * @param idCard 身份证号
     *
     * @return 返回true表示合法，false表示非法
     * */
    public static boolean isIdCard(String idCard) {
        Pattern p = Pattern.compile("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x|Y|y)$)");
        Matcher m = p.matcher(idCard);
        return m.matches();
    }
}
