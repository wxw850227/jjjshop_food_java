

package net.jjjshop.framework.ip.service;

import net.jjjshop.framework.common.service.BaseService;
import net.jjjshop.framework.ip.entity.IpAddress;

/**
 * IP地址 服务类
 */
public interface IpAddressService extends BaseService<IpAddress> {

    /**
     * 通过ip地址获取IP对象
     *
     * @param ip
     * @return
     */
    IpAddress getByIp(String ip);

    /**
     * 通过ip地址获取区域
     *
     * @param ip
     * @return
     */
    String getAreaByIp(String ip);

    /**
     * 通过ip地址获取运营商
     *
     * @param ip
     * @return
     */
    String getOperatorByIp(String ip);

}
