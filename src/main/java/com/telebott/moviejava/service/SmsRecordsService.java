package com.telebott.moviejava.service;

import com.telebott.moviejava.dao.AuthDao;
import com.telebott.moviejava.dao.SmsRecordsDao;
import com.telebott.moviejava.entity.SmsCode;
import com.telebott.moviejava.entity.SmsRecords;
import com.telebott.moviejava.util.SmsBaoUtil;
import com.telebott.moviejava.util.TimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SmsRecordsService {
    @Autowired
    private SmsRecordsDao smsRecordsDao;
    @Autowired
    private AuthDao authDao;
    @Autowired
    private SystemConfigService systemConfigService;
    public boolean _sendSmsCode(SmsCode smsCode){

//        SmsCode code= _getCode(smsCode.getPhone());
//        if (code != null){
//            smsCode = code;
//        }else {
//            authDao.removeByPhone(smsCode.getPhone());
//            authDao.pushCode(smsCode);
//        }
        authDao.removeByPhone(smsCode.getPhone());
        authDao.pushCode(smsCode);

        SmsRecords smsRecords = new SmsRecords();
        smsRecords.setCode(smsCode.getCode());
        smsRecords.setNumber(smsCode.getPhone());
        smsRecords.setStatus(0);
        smsRecords.setCtime(System.currentTimeMillis() / 1000L);
        smsRecordsDao.saveAndFlush(smsRecords);
        if (SmsBaoUtil.sendSmsCode(smsCode)){
            smsRecords.setStatus(1);
            smsRecordsDao.saveAndFlush(smsRecords);
            return true;
        }else {
            authDao.popCode(smsCode);
            return false;
        }
    }
    public SmsCode _getCode(String phone){
//        long lastTime = (System.currentTimeMillis() / 1000L) - 300L;
        SmsCode smsCode = authDao.findByPhone(phone);
        if (smsCode != null){
            return smsCode;
        }
        return null;
    }
    public boolean _checkSmsMax(String phone){
        long count = smsRecordsDao.countTodayMax(TimeUtil.getTodayZero() / 1000L,phone);
        long max = Long.parseLong(systemConfigService.getValueByKey("sms_count_max_day"));
        return count < max;
    }
    public String _verifyCode(String id, String code){
        SmsCode smsCode = authDao.findCode(id);
//        System.out.println(smsCode);
        if (smsCode != null && smsCode.getCode().equals(code)){
            authDao.popCode(smsCode);
//            SmsRecords smsRecords = smsRecordsDao.findAllByData(smsCode.getId());
//            smsRecords.setStatus(2);
//            smsRecordsDao.saveAndFlush(smsRecords);
            return smsCode.getPhone();
        }
        return null;
    }
}
