package com.hollykunge.vote.service;

import com.hollykunge.vote.entity.SysLog;

/**
 * @program: Lark-Server
 * @description: 日志
 * @author: Mr.Do
 * @create: 2019-09-30 11:21
 */
public interface SysLogService {
    /**
     * 保存日志
     * @param syslog
     */
    void saveSysLog(SysLog syslog);
}
