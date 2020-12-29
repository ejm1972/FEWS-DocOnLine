package net.sourceforge.jtds.jdbc;

import java.sql.SQLException;

import com.jolbox.bonecp.ConnectionHandle;


public class CLOB extends ClobImpl{

	public static CLOB createTemporary(Object connection){
		if (connection instanceof ConnectionHandle) {
			return new CLOB((JtdsConnection) ((ConnectionHandle) connection).getInternalConnection());
		}

		return new CLOB((JtdsConnection) connection);
	} 
	
	private CLOB(JtdsConnection connection) {
		super(connection);
	}
	
	public void putChars(int pos,char []value) throws SQLException{
		this.setString(pos, new String(value));
	}
	
	
}
