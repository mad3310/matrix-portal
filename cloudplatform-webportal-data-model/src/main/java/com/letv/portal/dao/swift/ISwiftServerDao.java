package com.letv.portal.dao.swift;

import com.letv.common.dao.IBaseDao;
import com.letv.portal.model.swift.SwiftServer;

public interface ISwiftServerDao extends IBaseDao<SwiftServer> {

    Integer selectCountByStatus(Integer _parameter);
}
