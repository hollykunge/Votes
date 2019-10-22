package com.hollykunge.service.impl;

import com.hollykunge.model.ExtToken;
import com.hollykunge.repository.ExtTokenRepository;
import com.hollykunge.service.ExtTokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: zhhongyu
 * @description:
 * @since: Create in 9:39 2019/10/22
 */
@Slf4j
@Service
public class ExtTokenServiceImp implements ExtTokenService {
    private final ExtTokenRepository extTokenRepository;

    @Autowired
    public ExtTokenServiceImp(ExtTokenRepository extTokenRepository) {
        this.extTokenRepository = extTokenRepository;
    }

    @Override
    public String getToken(String clentIp) throws Exception {
        String token = "vote_token_" + clentIp + System.currentTimeMillis();
        ExtToken extToken = new ExtToken();
        extToken.setToken(token);
        extToken.setIp(clentIp);
        if (!StringUtils.isEmpty(clentIp)) {
            List<ExtToken> byToken = extTokenRepository.findByIp(clentIp);
            if (byToken.size() > 0) {
                return byToken.get(0).getToken();
            }
        }
        extTokenRepository.saveAndFlush(extToken);
        log.info("生成token为:{}", token);
        return token;
    }

    @Override
    public boolean findToken(String token) {
        List<ExtToken> listTokens = extTokenRepository.findByToken(token);
        if (listTokens.isEmpty() || listTokens.size() == 0) {
            return false;
        }
        //获取成功后删除key
        listTokens.stream().forEach(extToken -> {
            extTokenRepository.delete(extToken.getId());
        });
        return true;
    }

    @Scheduled(cron = "0 0/1 * * * ?")
    public void testTasks() {
        Date end = new Date();
        Date start = getDate(end);
        List<ExtToken> byCreateDateBetween = extTokenRepository.findByCreateDateBetween(start, end);
        byCreateDateBetween.forEach(extToken -> {
            extTokenRepository.delete(extToken);
            log.info("ip地址中的{},没用token已经删除..",extToken.getIp());
        });
    }

    private Date getDate(Date start){
        Calendar calendar = Calendar.getInstance();
        /* HOUR_OF_DAY 指示一天中的小时 */
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("一个小时前的时间：" + df.format(calendar.getTime()));
        return calendar.getTime();
    }

}
