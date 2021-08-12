package com.nilin.meteor.service;

import com.nilin.meteor.entity.Sysconfig;
import com.baomidou.mybatisplus.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author ywq
 * @since 2021-03-06
 */
public interface SysconfigService extends IService<Sysconfig> {

    public String getSysconfig(String key);

}
