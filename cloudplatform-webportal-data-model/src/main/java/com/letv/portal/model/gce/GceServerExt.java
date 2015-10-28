package com.letv.portal.model.gce;

import com.letv.common.model.BaseModel;
import com.letv.portal.model.DbModel;
import com.letv.portal.model.cbase.CbaseBucketModel;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
public class GceServerExt extends BaseModel {
	
	private static final long serialVersionUID = -7362879987176365977L;
	private Long gceId;
	private Long rdsId;
	private Long ocsId;
	
	private DbModel db;
	private CbaseBucketModel cbase;
	
	public GceServerExt(){};
	public GceServerExt(Long gceId,Long rdsId,Long ocsId){
		this.gceId = gceId;
		this.rdsId = rdsId;
		this.ocsId = ocsId;
	};
	
	public DbModel getDb() {
		return db;
	}
	public void setDb(DbModel db) {
		this.db = db;
	}
	public CbaseBucketModel getCbase() {
		return cbase;
	}
	public void setCbase(CbaseBucketModel cbase) {
		this.cbase = cbase;
	}
	public Long getGceId() {
		return gceId;
	}
	public void setGceId(Long gceId) {
		this.gceId = gceId;
	}
	public Long getRdsId() {
		return rdsId;
	}
	public void setRdsId(Long rdsId) {
		this.rdsId = rdsId;
	}
	public Long getOcsId() {
		return ocsId;
	}
	public void setOcsId(Long ocsId) {
		this.ocsId = ocsId;
	}

	
}
