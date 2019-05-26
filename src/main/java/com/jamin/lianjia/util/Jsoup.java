package com.jamin.lianjia.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.jamin.lianjia.entity.House;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author jamin <br>
 * @date 2019/5/25 21:04<br>
 *     抓包工具类
 */
public class Jsoup {
  /**
   * 获取html源码
   *
   * @param urlStr url地址
   * @return html
   */
  public static Document doGet(String urlStr, Integer n) {
    LogFactory.getFactory()
        .setAttribute("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
    // HtmlUnit 模拟浏览器
    WebClient webClient = new WebClient(BrowserVersion.CHROME);
    webClient.getOptions().setJavaScriptEnabled(true);
    // 启用JS解释器，默认为true
    webClient.getOptions().setCssEnabled(false);
    // 禁用css支持
    webClient.getOptions().setThrowExceptionOnScriptError(false);
    // js运行错误时，是否抛出异常
    webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
    try {
      webClient.setAjaxController(new NicelyResynchronizingAjaxController());
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    // 设置连接超时时间
    webClient.getOptions().setTimeout(10 * 1000);
    HtmlPage page = null;

    try {
      page = webClient.getPage(urlStr);
    } catch (IOException e) {
      e.printStackTrace();
    }
    Document doc = null;
    if (page != null) {
      // 等待js后台执行30秒
      webClient.waitForBackgroundJavaScript(n * 1000);
      String pageAsXml = page.asXml();

      // Jsoup解析处理
      doc = org.jsoup.Jsoup.parse(pageAsXml, urlStr);
    }
    return doc;
  }

  /** 抓包工具 */
  public static List<House> jsoup() {
    Scanner scanner = new Scanner(System.in);
    System.out.println("请输入你链家地址");
    String url = scanner.next();
    List<House> houses = new ArrayList<House>();
    System.out.println("设置js加载时间(单位秒)");
    int n = scanner.nextInt();
    System.out.println("正在获取数据总数...");
    // 伪装获取document对象
    Document all = com.jamin.lianjia.util.Jsoup.doGet(url, n);
    int num = Integer.parseInt(all.select("span.content__title--hl").text());
    System.out.println("共有" + num + "条数据");
    int page = num % 30 == 0 ? num / 30 : ((num / 30) + 1);
    System.out.println("共有" + page + "页数据");
    System.out.println("请输入抓取到第几页");
    page = scanner.nextInt();
    System.out.println("即将开始");
    Date date = new Date();
    Calendar instance = Calendar.getInstance();
    instance.add(Calendar.SECOND, page * n * 30);
    System.out.println("预计结束时间:" + new SimpleDateFormat("HH:mm:ss").format(instance.getTime()));
    // 去除空格
    String urlBefore = url.trim();
    // 添加list
    String urlAfter = urlBefore.concat("#contentList");
    for (int i = 1; i <= page; i++) {
      String urlEnd = urlAfter.replace("rt", "pg" + i + "rt");
      Document document = com.jamin.lianjia.util.Jsoup.doGet(url, n);
      if (document != null) {

        // 获取标题集合
        Elements select = document.select("div.content__list--item--main");
        // 遍历元素
        int j = 0;
        for (Element element : select) {
          i++;
          System.out.println("正在抓取第" + i + "页的第" + j + "条数据");

          // 获得标题
          String tittle = element.select("p:nth-child(1)").text();
          // 截取获得连接
          String href = element.select("a").attr("href");
          String deUrl = "https://hf.lianjia.com" + href;
          // 处理链接
          Document document1 = Jsoup.doGet(deUrl, n);
          //      房屋标题
          String title = document1.select("p.content__title").text();
          //      房屋价格及付费类型
          String str[] = document1.select("p.content__aside--title").text().split("\\(|\\)");
          //      房屋类型
          String price = str[0].split("元")[0];

          String type =
              document1.select("p.content__article__table").select("span:nth-child(2)").text();

          // 房屋面积
          String area =
              document1.select("p.content__article__table").select("span:nth-child(3)").text();
          // 发布时间
          String pubTime =
              document1
                  .select("div.content__article__info")
                  .select("ul")
                  .select("li:nth-child(2)")
                  .text()
                  .substring(3);
          // 楼层
          String floor =
              document1
                  .select("div.content__article__info")
                  .select("ul")
                  .select("li:nth-child(8)")
                  .text()
                  .substring(3);
          // 租期
          String rentTime =
              document1
                  .select("div.content__article__info")
                  .select("ul")
                  .select("li:nth-child(5)")
                  .text()
                  .substring(3);
          // 地址
          String adress =
              document1.select("div.content__article__info4").select("ul").select("li").text();
          House house = new House();
          house.setAdress(adress);
          house.setArea(area);
          house.setFloor(floor);
          house.setResume(title);
          house.setPrice(price);
          house.setPayType(str[1]);
          house.setPubTime(pubTime);
          house.setTime(rentTime);
          house.setType(type);
          house.setUrl(deUrl);
          houses.add(house);
        }
      } else {
        continue;
      }
    }
    return houses;
  }
}
