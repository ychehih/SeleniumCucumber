package testRunner;

import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;


@RunWith(Cucumber.class)
@CucumberOptions
		(
		features= {".//features/"},
		glue="stepDefinitions",
		dryRun=false,
		monochrome=true, // Console output in readable format
		plugin= {"pretty",
				"html:target/report/cucumber-report",
				"json:target/report/cucumber.json",
				//"com.cucumber.listener.ExtentCucumberFormatter:target/report/ExtentReport.html"
				},
		tags= {"@sanity, @regression"}
		)

public class TestRun {

 
}
