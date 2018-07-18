package com.tj.common.io.ssh2;

import com.trilead.ssh2.Session;

public interface SshExecuteCallBack {

	<T> T excute(Session session) ;
}
