package com.atguigu.crowd.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * \* Created with IntelliJ IDEA.
 * \* @author Peter
 * \* Date: 2021/9/11 22:58
 * \* To change this template use File | Settings | Editor | File Templates.
 * \* Description:
 * \
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberLauchInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;
    // 简单介绍
    private String descriptionSimple;
    // 详细介绍
    private String descriptionDetail;
    // 联系电话
    private String phoneNum;
    // 客服电话
    private String serviceNum;

}
