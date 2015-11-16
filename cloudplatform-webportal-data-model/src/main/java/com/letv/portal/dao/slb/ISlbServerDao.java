package com.letv.portal.dao.slb;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.slb.SlbServer;

public interface ISlbServerDao extends IBaseDao<SlbServer> {

    Integer selectCountByStatus(Integer _parameter);
}
