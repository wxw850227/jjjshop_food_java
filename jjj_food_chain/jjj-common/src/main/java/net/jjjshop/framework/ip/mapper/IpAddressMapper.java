

package net.jjjshop.framework.ip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import net.jjjshop.framework.ip.entity.IpAddress;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * IP地址 Mapper 接口
 */
@Repository
public interface IpAddressMapper extends BaseMapper<IpAddress> {

    /**
     * 通过ip地址获取IP对象
     *
     * @param ip
     * @return
     */
    IpAddress getByIp(@Param("ip") String ip);

}
