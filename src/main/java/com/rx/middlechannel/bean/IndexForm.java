package com.rx.middlechannel.bean;

import lombok.Data;

/**
 * @author： KeA
 * @date： 2021-05-20 19:20:57
 * @version: 1.0
 * @describe:
 */
@Data
public class IndexForm {

    private String salt;

    private String clientCode;

    private String deviceId;

    private String oldPassword;

    private String newPassword;

}
