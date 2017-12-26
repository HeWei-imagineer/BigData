package MyCraw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class severlet_echart {
	
	private static Connection conn;
	
	public static void main(String[] args){ 
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:11588;DatabaseName=Craw_data", "sa",
					"123456");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			//data show
			//location_info
			 conn.setAutoCommit(false);
			 String sql = "select location,count(*)num from job_info group by location";
			 PreparedStatement pst = conn.prepareStatement(sql);	 
			 ResultSet rs = pst.executeQuery();
			 String location = "";
			 while(rs.next()){	
				String temp=rs.getString(1);
				temp = temp.trim();
				int num = rs.getInt(2);
				System.out.println("{name:'"+temp+"',value:"+num+"},");
				}
			 
			 //salary info
			 /*String sql = "select clean_sal,count(*)num from bigData_info group by clean_sal order by clean_sal";
			 PreparedStatement pst = conn.prepareStatement(sql);
			 ResultSet rs = pst.executeQuery(); 
				int num = 0;
				while(rs.next()){
					
					num = rs.getInt(1);
					System.out.print("'"+num+"k',");
					num = rs.getInt(2);
					System.out.print(num+",");
				}*/
				
			
			 
			 
			 
			 conn.commit();
			 rs.close();
			 pst.close();
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	  
	 
 

}
}
