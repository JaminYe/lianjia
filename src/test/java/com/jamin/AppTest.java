package com.jamin;

import com.jamin.lianjia.util.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

/** 测试 */
public class AppTest {

  @Test
  public void test() {

    Document document1 = Jsoup.doGet("https://hf.lianjia.com/zufang/HF2232337147279712256.html", 5);

    //      房屋类型
    String type = document1.select("p.content__article__table").select("span:nth-child(2)").text();
    System.out.println(type);

    // 房屋面积
    String area = document1.select("p.content__article__table").select("span:nth-child(3)").text();
    System.out.println(area);
  }

  @Test
  public void test1() {
    String s = "730 元/月 ";
    String s1 = s.split("元")[0];
    System.out.println(s1);
  }
}
