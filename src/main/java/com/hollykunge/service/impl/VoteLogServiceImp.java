package com.hollykunge.service.impl;

import com.hollykunge.model.User;
import com.hollykunge.model.Vote;
import com.hollykunge.repository.VoteRepository;

/**
 * @program: Lark-Server
 * @description: rizhi
 * @author: Mr.Do
 * @create: 2020-04-28 13:56
 */
@Service
public class VoteLogServiceImp {

    private final VoteLogRepository voteLogRepository;

    @Autowired
    public VoteLogServiceImp(VoteLogRepository voteLogRepository) {
        this.voteLogRepository = voteLogRepository;
    }

    @Override
    public VoteLog save(VoteLog voteLog) {
        return voteLogRepository.saveAndFlush(voteLog);
    }


    @Override
    public Page<VoteLog> findByUserOrderedByDatePageable(User user, int page) {
        return voteLogRepository.findByUserOrderByCreateDateDesc(user, new PageRequest(subtractPageByOne(page), 5));
    }

    private int subtractPageByOne(int page){
        return (page < 1) ? 0 : page - 1;
    }
}
