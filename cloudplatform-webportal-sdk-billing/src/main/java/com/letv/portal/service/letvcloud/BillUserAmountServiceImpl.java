package com.letv.portal.service.letvcloud;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.letv.common.exception.CommonException;
import com.letv.common.paging.impl.Page;
import com.letv.portal.dao.letvcloud.BillRechargeRecordMapper;
import com.letv.portal.dao.letvcloud.BillServiceOrderMapper;
import com.letv.portal.dao.letvcloud.BillUserAmountMapper;
import com.letv.portal.model.letvcloud.BillRechargeRecord;
import com.letv.portal.model.letvcloud.BillUserAmount;

/**
 * Created by wanglei14 on 2015/6/28.
 */
@Service
public class BillUserAmountServiceImpl implements BillUserAmountService {

    private static final BigDecimal ZERO = new BigDecimal("0");
    @Autowired
    BillUserAmountMapper billUserAmountMapper;
    @Autowired
    BillRechargeRecordMapper billRechargeRecordMapper;
    @Autowired
    BillServiceOrderMapper billServiceOrderDao;

    @Override
    public void createUserAmount(Long userId) throws CommonException {
        long ret = Long.valueOf(billUserAmountMapper.insertUserAmountDefault(userId));
        if (ret < 1) {
            throw new CommonException("创建用户余额失败");
        }
    }

    @Override
    public String recharge(long userId, BigDecimal amount, int type) {
        BillRechargeRecord record = new BillRechargeRecord();
        DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String tradeNum = format.format(System.currentTimeMillis());
        record.setTradeNum(tradeNum);
        record.setAmount(amount);
        record.setUserId(userId);
        record.setRechargeType(type);
        billRechargeRecordMapper.insert(record);

        return tradeNum;
    }

    @Override
    @Transactional
    public long rechargeSuccess(Long userId, String tradeNum, String orderNum, BigDecimal amount) {
        Map<String, Object> recordSuc = new HashMap<String, Object>();
        recordSuc.put("tradeNum", tradeNum);
        recordSuc.put("orderNum", orderNum);
        recordSuc.put("amount", amount);
        billRechargeRecordMapper.updateSuc(recordSuc);

        //充值业务逻辑
        long ret;
        int count = 0;
        do {
            ret = this.addUserAmount(userId, amount);
            count++;
        } while (ret < 1 && count < 10);
        //充值失败次数
        return ret;
    }

    //充值业务逻辑
    private long addUserAmount(Long userId, BigDecimal amount) {
        BillUserAmount billUserAmount = billUserAmountMapper.getUserAmout(userId);

        //账户余额
        BigDecimal availableAmount = billUserAmount.getAvailableAmount();
        //账户总额
        BigDecimal totalAmount = availableAmount.add(amount);
        //设置用户余额为总金额
        billUserAmount.setAvailableAmount(totalAmount);
        //如果
        if (ZERO.compareTo(availableAmount) == 1 && ZERO.compareTo(totalAmount) == 0) {
            billUserAmountMapper.updateArrearageTime(userId);
        }
        long ret = billUserAmountMapper.addAmount(billUserAmount);

        return ret;

    }

    @Override
    public Map<Long, Date> getAllUserArrears(String serviceCode) {
        List<Long> users = billServiceOrderDao.getUserByServiceCode(serviceCode);
        Map<Long, Date> result = new HashMap<Long, Date>();
        for (Long userId : users) {
            BillUserAmount amount = billUserAmountMapper.getUserArrears(userId);
            if (amount != null) {
                result.put(amount.getUserId(), amount.getArrearageTime());
            } else {
                continue;
            }
        }
        return result;
    }

    @Override
    public BillUserAmount getUserAmount(Long userId) {
        return billUserAmountMapper.getUserAmout(userId);
    }

    @Override
    public Page getUserAmountRecord(Page pageInfo, Long userId) {
    	Page pageResult = null;
       /* Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("offset", pageInfo.getOffset());
        params.put("rows", pageInfo.getRows());
        List<BillRechargeRecord> records = billRechargeRecordMapper.getUserAmountRecord(params);
        if (records != null && records.size() > 0) {
            pageResult = new PageResult();
            pageResult.setRows(records);
            int addCnt = billRechargeRecordMapper.getAddRecordCnt(userId);
            pageResult.setTotal(addCnt);
        }*/

        return pageResult;
    }

    @Override
    public BillRechargeRecord getRechargeRecord(String tradeNum) {
        return billRechargeRecordMapper.getAmount(tradeNum);
    }

    @Override
    public Map<String, Object> getUserAmountState(long userId) {
        Map<String, Object> result = new HashMap<String, Object>();
        BillUserAmount billUserAmount = billUserAmountMapper.getUserAmout(userId);
        result.put("userAccount", billUserAmount.getAvailableAmount());
        result.put("arrearageTime", billUserAmount.getArrearageTime());
        if (billUserAmount.getAvailableAmount().compareTo(new BigDecimal(0)) > 0) {
            result.put("arrearageState", "1");
        } else {
            result.put("arrearageState", "0");
        }
        return result;
    }

}
