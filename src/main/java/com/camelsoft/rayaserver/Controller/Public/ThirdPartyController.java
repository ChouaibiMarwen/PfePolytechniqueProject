package com.camelsoft.rayaserver.Controller.Public;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping(value = "/api/v1/third_part")
public class ThirdPartyController {


    private static final String PO_LIST_SERVICE_URI = "https://devsoa.almajdouie.com/soa-infra/resources/RAYA/PoListService/PoListServiceEp/getPoList";
    private static final String PO_INVOICE_SERVICE_URI = "https://devsoa.almajdouie.com/soa-infra/resources/RAYA/PoInvoiceListService/getPoInvListServiceEp/PoInvoice";
    private static final String PO_INVOICE_PAYMENT_SERVICE_URI = "https://devsoa.almajdouie.com/soa-infra/resources/RAYA/PoInvPmtListService/PoInvPmtServiceEp/InvoicePayment";
    private static final String USERNAME = "rayarosom";
    private static final String PASSWORD = "Digital@intg1";

    private final RestTemplate restTemplate;

    public ThirdPartyController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @PostMapping(value = {"/po_list"})
    public ResponseEntity<String> getPoList() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes()));
        headers.set("Content-Type", "application/json");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PO_LIST_SERVICE_URI,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;
    }


    @PostMapping(value = {"/po_invoice"})
    public ResponseEntity<String> getPoInvoice() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes()));
        headers.set("Content-Type", "application/json");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PO_INVOICE_SERVICE_URI,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;
    }

    @PostMapping(value = {"/po_invoice_payment"})
    public ResponseEntity<String> getPoInvoicePayment() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + Base64Utils.encodeToString((USERNAME + ":" + PASSWORD).getBytes()));
        headers.set("Content-Type", "application/json");

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                PO_INVOICE_PAYMENT_SERVICE_URI,
                HttpMethod.POST,
                entity,
                String.class
        );

        return response;
    }
}

