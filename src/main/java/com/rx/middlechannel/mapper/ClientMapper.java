package com.rx.middlechannel.mapper;

import com.rx.middlechannel.bean.Client;
import com.rx.middlechannel.bean.ClientVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author： KeA
 * @date： 2021-04-23 15:06:01
 * @version: 1.0
 * @describe:
 */
@Mapper
public interface ClientMapper {

    @Select("select id, client_code as clientCode  , uuid , client_name as clientName from client where client_code = #{clientCode}")
    Client selectByClientCode(String clientCode);

    @Select("select uuid from client where id = #{id}")
    String selectById(Integer id);

    @Select("select id, client_code as clientCode  , uuid , client_name  as clientName ,status from client where uuid = #{uuid}")
    Client selectByUuid(String uuid);

    @Select("select id, client_code as clientCode  , uuid , client_name  as clientName ,status from client where status = 1")
    List<Client> selectList();

    @Select("select id, client_code as clientCode  , uuid , client_name  as clientName ,status from client where status = 1")
    List<ClientVo> selectListForInterface();

    @Insert("insert into client (client_code , uuid , client_name,status) values (#{clientCode},#{uuid},#{clientName},#{status})")
    Integer insertClient(Client client);

    @Update("update client set uuid = #{uuid} ,status = #{status} where id = #{id}")
    Integer updateClientUUID(Client client);

    @Update("update client set client_name = #{clientName} where id = #{id}")
    Integer updateClientName(Client client);

    @Update("update client set uuid = null ,status = 0 where uuid = #{uuid}")
    Integer offline(String uuid);
}
