package com.jamin.lianjia.ui;

import com.jamin.lianjia.entity.House;
import com.jamin.lianjia.util.ExportExcel;
import com.jamin.lianjia.util.Jsoup;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Jamin <br>
 * @date 2019/5/25 22:55<br>
 *     测试类
 */
public class UI {
  public static void main(String[] args) {
    long start = System.currentTimeMillis();
    // 获得信息
    List<House> houses = new ArrayList<>();

    houses = Jsoup.jsoup();

    System.out.println("数据获取成功正在处理");

    Map<String, String> map = new LinkedHashMap<>();
    map.put("resume", "简介");
    map.put("price", "价格");
    map.put("payType", "支付类型");
    map.put("type", "类型");
    map.put("area", "面积");
    map.put("pubTime", "发布时间");
    map.put("time", "租期");
    map.put("floor", "楼层");
    map.put("adress", "地址");
    map.put("url", "网络地址");
    String sheetName = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒").format(new Date());
    ExportExcel.excelExport(houses, map, sheetName);
    System.out.println("导出成功");
    long end = System.currentTimeMillis();
    System.out.println("实际结束时间为" + new SimpleDateFormat("HH:mm:ss").format(new Date()));
    System.out.println("共耗时" + ((end - start) / 60000) + "分钟");
  }
}
