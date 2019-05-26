package com.jamin.lianjia.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jamin <br>
 * @date 2019/5/25 20:56<br>
 *     房屋实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class House {
  /** 简介 */
  private String resume;
  /** 价格 */
  private String price;
  /** 付费类型 */
  private String payType;
  /** 类型 */
  private String type;
  /** 面积 */
  private String area;
  /** 发布时间 */
  private String pubTime;
  /** 租期 */
  private String time;
  /** 楼层 */
  private String floor;
  /** 地址 */
  private String adress;
  /** 网络地址 */
  private String url;
}
