package pages;

    import org.openqa.selenium.WebDriver;
    import org.openqa.selenium.WebElement;
    import org.openqa.selenium.support.FindBy;
    import org.openqa.selenium.support.How;

public class SamplePage {
  final WebDriver driver;

  @FindBy(how = How.XPATH, using = "//*[@id=\"qacookies-continue-button\"]")
  public WebElement continueBtn;

  public SamplePage (WebDriver driver) {
    this.driver = driver;
  }
}
