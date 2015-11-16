package com.letv.portal.service.swift;

import com.letv.portal.model.swift.SwiftServer;
import com.letv.portal.service.IBaseService;

public interface ISwiftServerService extends IBaseService<SwiftServer> {

    Integer selectCountByStatus(Integer value);
}
