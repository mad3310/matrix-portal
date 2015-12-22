package com.letv.portal.proxy;

import org.springframework.web.multipart.MultipartFile;

import com.letv.portal.model.swift.SwiftServer;

public interface ISwiftServerProxy extends IBaseProxy<SwiftServer> {
	
	public void saveAndBuild(SwiftServer swift);
	public void deleteAndBuild(Long swiftId);
	public Object getFiles(Long id, String directory);
	public void changeService(Long id, String level,Long storeSize);
	public String postFiles(Long id, MultipartFile file, String directory);
	public void addFolder(Long id, String file, String directory);
	public void deleteFile(Long id, String file,boolean folder);
}
