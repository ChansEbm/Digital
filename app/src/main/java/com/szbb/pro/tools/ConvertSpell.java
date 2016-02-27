package com.szbb.pro.tools;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by ChanZeeBm on 2015/11/6.
 */
//中文转换为英文
public class ConvertSpell {
    private HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();

    public ConvertSpell() {
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
    }

    /**
     * 取得转换的拼音(或者首字母)
     *
     * @param allName the character which need convert
     * @return the converted character
     */
    public String getCharacterSpell(String allName) {
        String cString = allName.substring(0, 1);
        //如果首字母是英文,直接返回整个字符串
        if (cString.matches("^[A-Za-z]+$")) {
            return allName;
        }
        StringBuilder stringBuilder = new StringBuilder();
        //转换首字符
        String tempFirst = convertToPinYin(allName.charAt(0));
        //如果无法转换,则说明首字符不是字母也不是中文,是其他符号,返回#
        if (tempFirst == null)
            return "#";
        else
            //否则,把整个转换后的拼音存起来
            stringBuilder.append(tempFirst.toUpperCase());
        return stringBuilder.toString();
    }

    //字符转换为拼音
    private String convertToPinYin(char c) {
        try {
            String[] strings = PinyinHelper.toHanyuPinyinStringArray(c, hanyuPinyinOutputFormat);
            if (strings != null)
                return strings[0];
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }
        return null;
    }
}
