package cn.shiva.utils;

/**
 * @author shiva   2020/6/5 20:12
 */
public class CommonUtil {

    /**
     * 根据期数，计算多少页
     * 返回数组{页码，最后一页条数}
     */
    public static int[] calc(int num){
        //先根据期数，计算有多少页，每页20条
        if (num > 20 * 99 ){
            //一共99页，超过默认99
            return new int[]{99,20};
        }
        return new int[]{num / 20 + 1, num % 20};
    }


    /**
     * 对网页html进行分割，去除多余部分
     */
    public static String subHtml(String source){
        //从<tbody>开始到</tbody>结束
        if (source == null || source.length() < 500 ){
            return null;
        }
        return source.substring(source.indexOf("<tbody>") + 7, source.indexOf("</tbody>"));
    }



}
