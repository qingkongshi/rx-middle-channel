package com.rx.middlechannel.bean;

import lombok.Data;

import javax.persistence.Column;
import java.util.List;

/**
 * @author： KeA
 * @date： 2021-04-23 15:06:25
 * @version: 1.0
 * @describe:
 */
@Data
public class Client {
    private Integer id;
    private String clientCode;
    private String uuid;
    private String clientName;
    private Integer status;

    private List<Device> deviceList;
}
