package com.example.demo.Service;
import org.openqa.selenium.support.ui.Select;

//  import com.example.demo.models.Claim;
import com.example.demo.models.Claim;
import com.example.demo.models.DisputeData;
import com.example.demo.properties.AppProperties;
//  import com.example.demo.repisotory.ClaimsRepository;
import com.example.demo.repisotory.ClaimsRepository;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;


import java.io.File;
import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
@Slf4j
public class AppService {
    int count = 0;
    private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    private static SimpleDateFormat sdfFormat = new SimpleDateFormat("yyyy-MM-dd");


    @Autowired
    AppProperties properties;

    @Autowired
    ClaimsRepository claimsRepository;




    public void testRepository(){
        Claim claim =  new Claim();




        claimsRepository.save(claim);
    }


    public List<Claim> init(String filePath) throws Exception {

        try {
            properties.setStartDate("");
            properties.setEndDate("");

            System.setProperty(
                    "webdriver.chrome.driver",
                    "/Users/onirnarahari/downloads/chromedriver_mac64/chromedriver");
            // Instantiate a ChromeDriver class.

            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();


            chromePrefs.put("plugins.always_open_pdf_externally", true);
            chromePrefs.put("download.default_directory", 0);
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
            chromePrefs.put("download.prompt_for_download", false);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.setExperimentalOption("prefs", chromePrefs);


            WebDriver driver = new ChromeDriver(options);


            // Maximize the browser

            // Launch Website

                driver.get("https://psologin.homedepot.com/siteminderagent/forms/hdlogin.fcc?TYPE=33554433&REALMOID=06-000a412e-6097-17e3-b5a1-69460a45a0be&GUID=&SMAUTHREASON=0&METHOD=GET&SMAGENTNAME=-SM-bKXoXwKCEvltTJBCjSrsu7l%2bQJruYy6XbhvKhW85WgNZWvbWKVnM2vfsVekTxpGS&TARGET=-SM-HTTPS%3a%2f%2fpsoaccess%2ehomedepot%2ecom%2faffwebservices%2fredirectjsp%2fredirect%2ejsp%3fSPID%3ddirectcommerce%2ecom%26SMPORTALURL%3dhttps-%3A-%2F-%2Fpsoaccess%2ehomedepot%2ecom-%2Faffwebservices-%2Fpublic-%2Fsaml2sso%26SAMLTRANSACTIONID%3d11472c2b--015cebe4--c4e87c81--85b046a6--b8115b35--f7d");
                driver.findElement(By.name("j_username")).sendKeys("SXA9SUK");
                driver.findElement(By.name("j_password")).sendKeys("Winter#223");

                driver.findElement(By.xpath("//*[@id=\"mobileDiv\"]/div/div[2]/button")).click();


            driver.findElement(By.xpath("//*[@id=\"left-nav\"]/ul/li[2]/ul/li[3]/a")).click();


            driver.findElement(By.xpath("//*[@id=\"page-content\"]/div/div[2]/table/tbody/tr/td/div[2]/a")).click();
            count++;








            List<Claim> claims = new ArrayList<>();

            Scanner sc = new Scanner(new File(filePath));

            sc.useDelimiter("\n");
            sc.next();
            //sets the delimiter pattern
            while (sc.hasNext())  //returns a boolean value
            {


                String line = sc.next();

                System.out.println(line);

                String[] columns = line.split(",");

                Claim claim = new Claim();


                claim.setExceptionType(columns[0].replace("\"",""));
                claim.setVendorNumber(columns[1].replace("\"",""));
                claim.setVendorName(columns[2].replace("\"",""));

                if(columns[3] != null && !StringUtils.isEmpty(columns[3].replace("\"","").trim())) {
                    String recDate = sdfFormat.format(sdfFormat.parse(columns[3].replace("\"","")));

                    claim.setInvoiceDate(Date.valueOf(recDate));
                }

                claim.setInvoiceNumber(columns[4].replace("\"",""));
                claim.setPoNumber(columns[5].replace("\"",""));
                claim.setStoreNumber(columns[6].replace("\"",""));
                claim.setInvoiceAmount(new BigDecimal(columns[7].replace("\"","")));
                claim.setVoucher(columns[8].replace("\"",""));
                claim.setErrorDesc(columns[9].replace("\"",""));


                Claim alreadyExistClaim = claimsRepository.findByInvoiceNumber(claim.getInvoiceNumber());

                log.info("claim  :: [{}]", (claim != null));

                //&& claim.getExceptionType() != null && claim.getExceptionType().trim().equals("POD Dispute")

                if(alreadyExistClaim == null && claim.getExceptionType() != null && claim.getExceptionType().trim().equalsIgnoreCase("POD Dispute")){

                    claims.add(claim);
                    log.info("ADD Claim");
                }


                System.out.println();



            }
            sc.close();  //closes the scanner

            log.info("claims [{}]", claims.size());

            claimsRepository.saveAll(claims);

            log.info("all claims [{}]", claimsRepository.findAll().size());

            return claims;


        }catch (Exception e){
            e.printStackTrace();
            throw new Exception("csv file read exception...");
        }

    }

    public void disputeClaims(){

        try{
            System.setProperty(
                    "webdriver.chrome.driver",
                    "/Users/onirnarahari/downloads/chromedriver_mac64/chromedriver");
            // Instantiate a ChromeDriver class.

            HashMap<String, Object> chromePrefs = new HashMap<String, Object>();

            chromePrefs.put("plugins.always_open_pdf_externally", true);
            chromePrefs.put("download.default_directory", "/Users/onirnarahari/Downloads/Temp");
            chromePrefs.put("profile.default_content_settings.popups", 0);
            chromePrefs.put("profile.content_settings.exceptions.automatic_downloads.*.setting", 1);
            chromePrefs.put("download.prompt_for_download", false);
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--remote-allow-origins=*");
            options.setExperimentalOption("prefs", chromePrefs);
            WebDriver driver = new ChromeDriver(options);


            List<DisputeData> disputableClaims =  this.getDisputeData();

            if(disputableClaims.size() > 0){

                driver.get("https://psologin.homedepot.com/siteminderagent/forms/hdlogin.fcc?TYPE=33554433&REALMOID=06-000a412e-6097-17e3-b5a1-69460a45a0be&GUID=&SMAUTHREASON=0&METHOD=GET&SMAGENTNAME=-SM-bKXoXwKCEvltTJBCjSrsu7l%2bQJruYy6XbhvKhW85WgNZWvbWKVnM2vfsVekTxpGS&TARGET=-SM-HTTPS%3a%2f%2fpsoaccess%2ehomedepot%2ecom%2faffwebservices%2fredirectjsp%2fredirect%2ejsp%3fSPID%3ddirectcommerce%2ecom%26SMPORTALURL%3dhttps-%3A-%2F-%2Fpsoaccess%2ehomedepot%2ecom-%2Faffwebservices-%2Fpublic-%2Fsaml2sso%26SAMLTRANSACTIONID%3d11472c2b--015cebe4--c4e87c81--85b046a6--b8115b35--f7d");
                driver.findElement(By.name("j_username")).sendKeys("SXA9SUK");
                driver.findElement(By.name("j_password")).sendKeys("Winter#223");
                driver.findElement(By.xpath("//*[@id=\"mobileDiv\"]/div/div[2]/button")).click();


            }


            for(DisputeData disputeClaim : disputableClaims) {

                driver.findElement(By.xpath("//*[@id=\"quickSearchText\"]")).sendKeys(disputeClaim.getInvoiceNumber());
                Select invoiceExceptions = new Select(driver.findElement(By.id("action")));
                invoiceExceptions.selectByVisibleText("Invoice Exceptions (Invoice #)");
                driver.findElement(By.xpath("//*[@id=\"QUICK_SEARCH_SEARCH_BUTTON\"]")).click();
                    if(!driver.findElement(By.xpath("//*[@id=\"page-content\"]/div/div[2]/table/tbody/tr/td/table[2]/tbody/tr[2]/td")).getText().equals("There are currently no invoices to list.")) {
                        if (driver.findElement(By.xpath("//*[@id=\"DATA_ROW_1\"]/td[1]")).getText().equals("POD Dispute")) {
                            driver.findElement(By.xpath("//*[@id=\"DATA_ROW_1\"]/td[1]/a")).click();


                            //tracking number
                            if (disputeClaim.getTrackingNumber() != null) {
                                driver.findElement(By.xpath("//*[@id=\"inv_tracking_1\"]")).sendKeys(disputeClaim.getTrackingNumber());
                            }
                            //  dptNumber.
                            if (disputeClaim.getDepartmentNumber() != null) {

                                Select dptNumber = new Select(driver.findElement(By.id("inv_dept_1")));
                                dptNumber.selectByVisibleText(disputeClaim.getDepartmentNumber());
                            }
                            // dispute amount
                            if (disputeClaim.getDisputedAmount() != null && disputeClaim.getDisputedAmount() == "0") {

                                driver.findElement(By.xpath("//*[@id=\"inv_amount_1\"]")).sendKeys(disputeClaim.getDisputedAmount());
                            }
                            // freight carrier
                            if (disputeClaim.getFreightCarrier() != null) {

                                driver.findElement(By.xpath("//*[@id=\"inv_ship_1\"]")).sendKeys(disputeClaim.getFreightCarrier());
                            }
                        } else {
                            System.out.println("notvalid");
                        }
                    }
               }

        }catch(Exception e){
            e.printStackTrace();
        }

    }


public List<DisputeData> getDisputeData(){
   try {
       RestTemplate restTemplate = new RestTemplate();
       ResponseEntity<List<DisputeData>> response =
               restTemplate.exchange(properties.getApi(),
                       HttpMethod.GET, null, new ParameterizedTypeReference<List<DisputeData>>() {
                       });
       return response.getBody();
   }
   catch(Exception e){
       e.printStackTrace();
   }
    return Collections.emptyList();
}

}
