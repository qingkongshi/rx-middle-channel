package com.rx.middlechannel.server;

import com.rx.middlechannel.bean.Client;
import com.rx.middlechannel.bean.ClientVo;

import java.util.List;

/**
 * @author： KeA
 * @date： 2021-04-23 17:38:56
 * @version: 1.0
 * @describe:
 */
public interface ClientService {

    List<Client> selectList();

    List<ClientVo> selectListForInterface();

    Integer updateClientName(Client client);

    String selectById(Integer id);

    Client selectByClientCode(String clientCode);

}
