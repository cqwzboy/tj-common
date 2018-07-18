package com.tj.common.zookeeper.base;

import java.util.List;

import org.apache.curator.framework.api.ACLProvider;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooDefs.Perms;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;

/**
 * 自定义zk节点权限控制器
 * @author silongz
 *
 */
public class TjACLProvider implements ACLProvider{
	
	private String userName;

	private String password;

	private static List<ACL> acl ;  
	
	public TjACLProvider(String userName , String password){
		this.userName = userName ;
		this.password = password ;
	}
	
	@Override
	public List<ACL> getDefaultAcl() {
		if(acl ==null){  
			acl = ZooDefs.Ids.READ_ACL_UNSAFE ;  
			acl.add(new ACL(Perms.ALL, new Id("auth", userName.concat(":").concat(password))));  
        }  
        return acl ;  
	}

	@Override
	public List<ACL> getAclForPath(String path) {
		return getDefaultAcl() ;  
	}

}
