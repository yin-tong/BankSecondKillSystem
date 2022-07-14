package com.bsks.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.bsks.entity.FirstFilterRecord;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 初筛记录表 服务类
 * </p>
 *
 * @author Li
 * @since 2022-02-22
 */
public interface FirstFilterRecordService extends IService<FirstFilterRecord> {

    void createFirstFilterRecord(String identityId, String result, String rejectReason);

    boolean isRejected(String identityId,int age);

    int findAllRecordCount();

    List<FirstFilterRecord> findAllRecord(long currentPage, long pageSize);

    int findRecordCountByDate(Date start, Date end);

    List<FirstFilterRecord> findRecordByDate(Date start,Date end,long currentPage, long pageSize);

    int findRecordCountByIdentityId(String identityId);

    List<FirstFilterRecord> findRecordByIdentityId(String identityId,long currentPage, long pageSize);

    int findRecordCountByIdentityIdDate(String identityId,Date start,Date end);

    List<FirstFilterRecord> findRecordByIdentityIdDate(String identityId, Date start, Date end, long currentPage, long pageSize);
}
