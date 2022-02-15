package com.telebott.moviejava.service;

import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.dao.SmsRecordsDao;
import com.telebott.moviejava.entity.SmsCode;
import com.telebott.moviejava.entity.SmsRecords;
import com.telebott.moviejava.util.SmsBaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsRecordsService {
    @Autowired
    private SmsRecordsDao smsRecordsDao;
    @Autowired
    private AuthDao authDao;
    public boolean _sendSmsCode(SmsCode smsCode){
        SmsRecords smsRecords = new SmsRecords();
        smsRecords.setCode(smsCode.getCode());
        smsRecords.setNumber(smsCode.getPhone());
        smsRecords.setStatus(0);
        smsRecords.setCtime(System.currentTimeMillis() / 1000L);
        smsRecords.setData(smsCode.getId());
        if (SmsBaoUtil.sendSmsCode(smsCode)){
            smsRecordsDao.saveAndFlush(smsRecords);
            authDao.pushCode(smsCode);
            return true;
        }else {
            return false;
        }
    }
    public boolean _verifyCode(String id, String code){
        SmsCode smsCode = authDao.findCode(id);
        if (smsCode != null){
            authDao.popCode(smsCode);
            SmsRecords smsRecords = smsRecordsDao.findAllByData(smsCode.getId());
            smsRecords.setStatus(1);
            smsRecordsDao.saveAndFlush(smsRecords);
            return true;
        }
        return false;
    }
}
