package com.camelsoft.rayaserver.Controller.Public;

import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Request.thirdPart.ThirdPartRequst;
import com.camelsoft.rayaserver.Services.Project.InvoiceService;
import com.nimbusds.jose.shaded.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/third_part")
public class ThirdPartyController {


    private static final String PO_LIST_SERVICE_URI = "https://devsoa.almajdouie.com/soa-infra/resources/RAYA/PoListService/PoListServiceEp/getPoList";
    private static final String PO_INVOICE_SERVICE_URI = "https://devsoa.almajdouie.com/soa-infra/resources/RAYA/PoInvoiceListService/getPoInvListServiceEp/PoInvoice";
    private static final String PO_INVOICE_PAYMENT_SERVICE_URI = "https://devsoa.almajdouie.com/soa-infra/resources/RAYA/PoInvPmtListService/PoInvPmtServiceEp/InvoicePayment";
    private static final String USERNAME = "rayarosom";
    private static final String PASSWORD = "Digital@intg1";
    @Autowired
    private InvoiceService invoiceService;


    private final RestTemplate restTemplate;

    public ThirdPartyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping(value = {"/po_list"})
    public ResponseEntity<String> getPoListWithFilters(@RequestBody ThirdPartRequst filterRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes()));
        headers.set("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("VendorNumber", filterRequest.getVendorNumber());
        requestJson.put("FromDate", filterRequest.getFromDate());
        requestJson.put("ToDate", filterRequest.getToDate());

        HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PO_LIST_SERVICE_URI,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;
    }

    @PostMapping(value = {"/po_invoice"})
    public ResponseEntity<String> getPoInvoiceWithFilters(@RequestBody ThirdPartRequst filterRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes()));
        headers.set("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("VendorNumber", filterRequest.getVendorNumber());
        requestJson.put("FromDate", filterRequest.getFromDate());
        requestJson.put("ToDate", filterRequest.getToDate());

        HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PO_INVOICE_SERVICE_URI,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;
    }

    @PostMapping(value = {"/po_invoice_payment"})
    public ResponseEntity<String> getPoInvoicePaymentWithFilters(@RequestBody ThirdPartRequst filterRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes()));
        headers.set("Content-Type", "application/json");

        JSONObject requestJson = new JSONObject();
        requestJson.put("VendorNumber", filterRequest.getVendorNumber());
        requestJson.put("FromDate", filterRequest.getFromDate());
        requestJson.put("ToDate", filterRequest.getToDate());

        HttpEntity<String> entity = new HttpEntity<>(requestJson.toString(), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PO_INVOICE_PAYMENT_SERVICE_URI,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;

    }
   @PostMapping(value = {"/patch_invoice"})
    public ResponseEntity<String> patch_invoice() {

       List<Invoice> list = this.invoiceService.findAll();
       for(Invoice i : list){
           i.setRole(i.getCreatedby().getRole().getRole());
           this.invoiceService.Update(i);
       }

       return  new ResponseEntity("donee" ,HttpStatus.OK );

    }


}

