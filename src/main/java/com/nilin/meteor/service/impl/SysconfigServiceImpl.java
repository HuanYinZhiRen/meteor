package com.nilin.meteor.service.impl;

import com.nilin.meteor.common.util.RedisUtil;
import com.nilin.meteor.entity.Sysconfig;
import com.nilin.meteor.mapper.SysconfigMapper;
import com.nilin.meteor.service.SysconfigService;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ywq
 * @since 2021-03-06
 */
@Service
public class SysconfigServiceImpl extends ServiceImpl<SysconfigMapper, Sysconfig> implements SysconfigService {

    private final static Logger logger = LoggerFactory.getLogger(SysconfigServiceImpl.class);

    @Autowired
    private RedisUtil redisUtil;


    @Override
    public String getSysconfig(String key) {
        String value = null;
        value = Objects.toString(redisUtil.get(key),"");
        if(StringUtils.isBlank(value)) {
            Sysconfig t = new Sysconfig();
            t.setSysc004(key);
            Sysconfig config = baseMapper.selectOne(t);
            value = config.getSysc005();
            redisUtil.set(key, value);
        }
        return value;
    }
}
