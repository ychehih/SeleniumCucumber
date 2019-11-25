package stepDefinitions;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import pageObjects.AddCustomerPage;
import pageObjects.LoginPage;
import pageObjects.SearchCustomerPage;

public class Steps extends BaseClass
{

	@Before
	public void setup() throws IOException
	{
		//Logger
		logger=Logger.getLogger("nopComemrce"); //Added logger
		PropertyConfigurator.configure("Log4j.properties");//Added logger
		
		//Reading properties
		configProp=new Properties();
		FileInputStream configPropfile=new FileInputStream("config.properties");
		configProp.load(configPropfile);
		
		String br=configProp.getProperty("browser");
		
		if(br.equals("chrome"))
		{
		System.setProperty("webdriver.chrome.driver",configProp.getProperty("chromepath"));
		driver=new ChromeDriver();
		}
		else if (br.equals("firefox")) {
			System.setProperty("webdriver.gecko.driver",configProp.getProperty("firefoxpath"));
			driver = new FirefoxDriver();
		}
		else if (br.equals("ie")) {
			System.setProperty("webdriver.ie.driver",configProp.getProperty("iepath"));
			driver = new InternetExplorerDriver();
		}
		
		logger.info("******** Launching browser*********");
	}
	
	
	@Given("User Launch Chrome browser")
	public void user_Launch_Chrome_browser() {
				
		loginPage=new LoginPage(driver);
	}

	@When("User opens URL {string}")
	public void user_opens_URL(String url) {
		logger.info("******** Opening URL*********");
		driver.get(url);
	  driver.manage().window().maximize();
	}

	@When("User enters Email as {string} and Password as {string}")
	public void user_enters_Email_as_and_Password_as(String email, String password) {
		logger.info("******** Providing login details*********");
		loginPage.setUserName(email);
	    loginPage.setPassword(password);
	}

	@When("Click on Login")
	public void click_on_Login() throws InterruptedException {
		logger.info("******** started login*********");
		loginPage.clickLogin();
	   Thread.sleep(3000);
	}

	@Then("Page Title should be {string}")
	public void page_Title_should_be(String title) throws InterruptedException {
	   
		if (driver.getPageSource().contains("Login was unsuccessful.")) {
			driver.close();
			logger.info("******** Login Filed*********");
			Assert.assertTrue(false);
		} else {
			logger.info("******** Login Passed*********");
			Assert.assertEquals(title, driver.getTitle());
		}
		Thread.sleep(3000);
		
	}

	@When("User click on Log out link")
	public void user_click_on_Log_out_link() throws InterruptedException {
		logger.info("******** Click on logout link*********");
		loginPage.clickLogout();
		Thread.sleep(3000);
	}

	@Then("close browser")
	public void close_browser() {
		logger.info("********closing browser********");
		driver.quit();
	}
	
	//Customer feature step definitions..........................................
	
	@Then("User can view Dashboad")
	public void user_can_view_Dashboad() {
		addCustomerPage = new AddCustomerPage(driver);
		Assert.assertEquals("Dashboard / nopCommerce administration",addCustomerPage.getPageTitle());
	}

	@When("User click on customers Menu")
	public void user_click_on_customers_Menu() throws InterruptedException {
		Thread.sleep(3000);
		addCustomerPage.clickOnCustomersMenu();
	}

	@When("click on customers Menu Item")
	public void click_on_customers_Menu_Item() throws InterruptedException {
		Thread.sleep(2000);
	   addCustomerPage.clickOnCustomersMenuItem();
	}

	@When("click on Add new button")
	public void click_on_Add_new_button() throws InterruptedException {
	   addCustomerPage.clickOnAddnew();
	   Thread.sleep(2000);
	}

	@Then("User can view Add new customer page")
	public void user_can_view_Add_new_customer_page() {
	  Assert.assertEquals("Add a new customer / nopCommerce administration", addCustomerPage.getPageTitle());
	}

	@When("User enter customer info")
	public void user_enter_customer_info() throws InterruptedException {
		
		logger.info("********Adding new customer*********");
		logger.info("********Proving customer details*********");
		String email = randomString() + "@gmail.com";
		addCustomerPage.setEmail(email);
		addCustomerPage.setPassword("test123");
		// Registered - default
		// The customer cannot be in both 'Guests' and 'Registered' customer roles
		// Add the customer to 'Guests' or 'Registered' customer role
		addCustomerPage.setCustomerRoles("Guest");
		Thread.sleep(3000);

		addCustomerPage.setManagerOfVendor("Vendor 2");
		addCustomerPage.setGender("Male");
		addCustomerPage.setFirstName("John");
		addCustomerPage.setLastName("Doe");
		addCustomerPage.setDob("7/05/1985"); // Format: D/MM/YYY
		addCustomerPage.setCompanyName("I-City");
		addCustomerPage.setAdminContent("This is for testing.........");
	}

	@When("click on Save button")
	public void click_on_Save_button() throws InterruptedException {
		logger.info("********Saving customer data*********");
		addCustomerPage.clickOnSave();
	   Thread.sleep(2000);
	}

	@Then("User can view confirmation message {string}")
	public void user_can_view_confirmation_message(String msg) {
		Assert.assertTrue(driver.findElement(By.tagName("body")).getText()
				.contains("The new customer has been added successfully"));
	}

	
	//steps for searching a customer using Email ID.........
	@When("Enter customer EMail")
	public void enter_customer_EMail() {
		
		logger.info("********Searching customer by email id*********");
		searchCustomerPage=new SearchCustomerPage(driver);
		searchCustomerPage.setEmail("victoria_victoria@nopCommerce.com");
	}

	@When("Click on search button")
	public void click_on_search_button() throws InterruptedException {
		searchCustomerPage.clickSearch();
		Thread.sleep(3000);
	}

	@Then("User should found Email in the Search table")
	public void user_should_found_Email_in_the_Search_table() {
	   boolean status=searchCustomerPage.searchCustomerByEmail("victoria_victoria@nopCommerce.com");
	   	Assert.assertEquals(true, status);
	
	}
	
	//steps for searching a customer by using First Name & Lastname
	@When("Enter customer FirstName")
	public void enter_customer_FirstName() {
		
		logger.info("********Searching customer by Name*********");
		searchCustomerPage=new SearchCustomerPage(driver);
		searchCustomerPage.setFirstName("Victoria");
	}

	@When("Enter customer LastName")
	public void enter_customer_LastName() {
		searchCustomerPage.setLastName("Terces");
	}

	@Then("User should found Name in the Search table")
	public void user_should_found_Name_in_the_Search_table() {
		boolean status=searchCustomerPage.searchCustomerByName("Victoria Terces");
		Assert.assertEquals(true, status);
	}

	
}
