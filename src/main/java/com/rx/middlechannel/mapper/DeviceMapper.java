package com.rx.middlechannel.mapper;

import com.rx.middlechannel.bean.Device;
import com.rx.middlechannel.bean.DeviceVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * @author： KeA
 * @date： 2021-04-23 18:45:02
 * @version: 1.0
 * @describe:
 */
@Mapper
public interface DeviceMapper {

    @Insert("insert into device (client_id , address_code , number,device_name,param1,param2,param3,param4,param5,param6,param7,password) values (#{clientId},#{addressCode},#{number},#{deviceName},#{param1},#{param2},#{param3},#{param4},#{param5},#{param6},#{param7},#{password})")
    Integer insertDevice(Device device);

    @Update("update device set status = #{status} where client_id = #{clientId} and address_code = #{addressCode}")
    Integer updateStatusByCodeAndId(Device device);

    @Update("update device set password = #{password} where id = #{id}")
    Integer updatePasswordById(Device device);

    @Select("select *,client_id as clientId,address_code as addressCode , device_name as deviceName from device where client_id = #{clientId}")
    List<Device> getByClientId(Integer clientId);

    @Select("select *,client_id as clientId,address_code as addressCode , device_name as deviceName from device where client_id = #{clientId}")
    List<DeviceVo> getByClientIdForInterface(Integer clientId);

    @Select("select * from device where client_id = #{clientId} and status = 0")
    List<Device> getStatusByClientId(Integer clientId);

    @Select("select *,client_id as clientId,address_code as addressCode , device_name as deviceName from device where id = #{id}")
    Device getById(Integer id);

    @Select("select * from device where  client_id = #{clientId} and address_code = #{addressCode}")
    Device getByClientIdAndAddressCode(Device device);
}
