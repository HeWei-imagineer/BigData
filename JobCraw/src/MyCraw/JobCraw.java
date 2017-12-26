package MyCraw;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jeasy.analysis.MMAnalyzer;

public class JobCraw {
	
	private static Connection conn;
	
	static {
		try {
			
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			conn = DriverManager.getConnection(
					"jdbc:sqlserver://localhost:11588;DatabaseName=Craw_data", "sa",
					"123456");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	
	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		System.setProperty("webdriver.chrome.driver",
				"D:/abc/chromedriver.exe");
		// 建立驱动的核心操作对象
		WebDriver driver = new ChromeDriver();
		JavascriptExecutor jsDriver = (JavascriptExecutor) driver;
		WebDriverWait wait = new WebDriverWait(driver, 30);
		// 选择一个网址来打开
		driver.get("https://www.lagou.com/jobs/list_?px=new&gx=&hy=%E7%A7%BB%E5%8A%A8%E4%BA%92%E8%81%94%E7%BD%91&isSchoolJob=1");
		
		
		
		while (true) {
			
			
			//wait.until(ExpectedConditions.presenceOfElementLocated(By.tagName("a")));

			try {
				// 查找所有连接地址
				List<WebElement> allLink = driver.findElements(By.tagName("a"));
				for (WebElement link : allLink) {
					String href = link.getAttribute("href");
					try {
						if (href != null && (href.startsWith("http:") || href.startsWith("https:"))) {
							
							if (href.matches(".*/www.lagou.com/jobs/\\d+\\.html.*")) {
								// 去数据库判断是否有重复的地址
								insertUrls(href);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (org.openqa.selenium.StaleElementReferenceException ex) {
				// TODO: handle exception
				List<WebElement> allLink = driver.findElements(By.tagName("a"));
			}
			
			

			
			// 点下一页 
			WebElement nextEl = driver.findElement(By.className("pager_next"));
			if (nextEl == null) {
				break;
			}
			jsDriver.executeScript("window.scrollTo(0,document.body.scrollHeight * 0.9);");
			nextEl.click();
			System.out.println("next page");
		}
		conn.close();
	
	
	}
	
	private static void insertUrls(String url) throws Exception {
		conn.setAutoCommit(false);
		String sql = "SELECT url FROM url_info WHERE url = ?";
		PreparedStatement pst = conn.prepareStatement(sql);
		pst.setString(1, url);
		ResultSet rs = pst.executeQuery();
		if (!rs.next()) {
			// 不重复的时候，可以加入数据
			sql = "INSERT INTO url_info (url,flag) VALUES (?,0)";
			pst = conn.prepareStatement(sql);
			pst.setString(1, url);
			pst.executeUpdate();
		}
		conn.commit();

		rs.close();
		pst.close();
	}

}
