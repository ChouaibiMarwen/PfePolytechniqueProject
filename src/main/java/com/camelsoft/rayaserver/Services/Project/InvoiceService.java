package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Repository.Project.InvoiceRepository;
import com.camelsoft.rayaserver.Repository.Project.InvoiceRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository repository;


    public Invoice Save(Invoice model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Invoice Update(Invoice model) {
        try {
            return this.repository.save(model);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public Invoice FindById(Long id) {
        try {
            if (this.repository.findById(id).isPresent())
                return this.repository.findById(id).get();
            return null;
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }

    public DynamicResponse FindAllPg(int page, int size, InvoiceRelated related) {
        try {


            PageRequest pg = PageRequest.of(page, size);
            Page<Invoice> pckge = this.repository.findAllByArchiveIsFalseAndRelated(pg, related);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public DynamicResponse FindAllByState(int page, int size, InvoiceStatus status, InvoiceRelated related) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Invoice> pckge = this.repository.findAllByStatusAndArchiveIsFalseAndRelated(pg, status, related);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public List<Invoice> findAll() {
        try {
            return this.repository.findAll();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }


    public boolean ExistById(Long id) {
        try {
            return this.repository.existsById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }

    public boolean ExistByInvoiceNumber(Integer invoicenumber) {
        try {
            return this.repository.existsByInvoicenumber(invoicenumber);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found not found [%s] in our data base", invoicenumber));
        }

    }


    public void DeleteById(Long id) {
        try {
            this.repository.deleteById(id);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(String.format("No file found with id [%s] in our data base", id));
        }

    }


    public Long Count() {
        try {
            return this.repository.count();
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public Integer countInvoicePerMonth(Date date, InvoiceRelated related) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1; // Calendar month is zero-based
            int year = calendar.get(Calendar.YEAR);

            // Set the start date to the first day of the specified month
            calendar.set(year, month - 1, 1, 0, 0, 0);
            Date startDate = calendar.getTime();

            // Set the end date to the last day of the specified month
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            // Call repository method to count invoices within the specified month
            return this.repository.countByTimestampBetweenAndRelated(startDate, endDate,related);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }
    public Integer countInvoicePerMonthAndStatus(Date date,InvoiceStatus status, InvoiceRelated related) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1; // Calendar month is zero-based
            int year = calendar.get(Calendar.YEAR);

            // Set the start date to the first day of the specified month
            calendar.set(year, month - 1, 1, 0, 0, 0);
            Date startDate = calendar.getTime();

            // Set the end date to the last day of the specified month
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            // Call repository method to count invoices within the specified month
            return this.repository.countByTimestampBetweenAndStatusAndRelated(startDate, endDate,status,related);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }


    public double getTotalRevenueFromPaidInvoices() {
        List<Invoice> paidInvoices = this.repository.findByStatus(InvoiceStatus.PAID);
        double totalRevenue = 0.0;
        for (Invoice invoice : paidInvoices) {
            for (Product product : invoice.getProducts()) {
                totalRevenue += product.getSubtotal();
            }
        }
        return totalRevenue;
    }

    public double getTotalRevenueFromOnePaidInvoice(Invoice invoice) {
       // List<Invoice> paidInvoices = this.repository.findByStatus(InvoiceStatus.PAID);
        double totalRevenue = 0.0;
        if(invoice.getProducts() == null || invoice.getProducts().isEmpty())
            return 0;
        for (Product product : invoice.getProducts()) {
            totalRevenue += product.getSubtotal();
        }
        return totalRevenue;
    }






    //get totla revevenu by date :
    public List<Map<String, Double>> calculateRevenueByMonth(Date startDate, Date endDate) {
        List<Invoice> paidInvoices = this.repository.findByStatusAndArchiveIsFalseAndTimestampBetween(InvoiceStatus.PAID, startDate, endDate);

        List<Map<String, Double>> revenueByMonth = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
            double totalRevenue = 0.0;
            int month = calendar.get(Calendar.MONTH);
            String monthName = new SimpleDateFormat("MMMM").format(calendar.getTime());

            for (Invoice invoice : paidInvoices) {
                if (isSameMonth(invoice.getTimestamp(), calendar.getTime())) {
                    for (Product product : invoice.getProducts()) {
                        totalRevenue += product.getSubtotal();
                    }
                }
            }

            Map<String, Double> monthlyRevenue = new HashMap<>();
            monthlyRevenue.put(monthName, totalRevenue);
            revenueByMonth.add(monthlyRevenue);
            calendar.add(Calendar.MONTH, 1);
        }

        return revenueByMonth;
    }

    private boolean isSameMonth(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    }
    //gettotla paid invoices for su_admins by date :
    public List<Map<String, Double>> calculateRevenueByMonthForSubAdmins(Date startDate, Date endDate) {
        List<Invoice> paidInvoices = this.repository.findByStatusAndArchiveIsFalseAndTimestampBetweenAndCreatedByRole(InvoiceStatus.PAID, startDate, endDate, RoleEnum.ROLE_SUB_ADMIN);

        List<Map<String, Double>> revenueByMonth = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);

        while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
            double totalRevenue = 0.0;
            int month = calendar.get(Calendar.MONTH);
            String monthName = new SimpleDateFormat("MMMM").format(calendar.getTime());

            for (Invoice invoice : paidInvoices) {
                if (isSameMonth(invoice.getTimestamp(), calendar.getTime())) {
                    for (Product product : invoice.getProducts()) {
                        totalRevenue += product.getSubtotal();
                    }
                }
            }

            Map<String, Double> monthlyRevenue = new HashMap<>();
            monthlyRevenue.put(monthName, totalRevenue);
            revenueByMonth.add(monthlyRevenue);
            calendar.add(Calendar.MONTH, 1);
        }

        return revenueByMonth;
    }


    public long countPaidInvoicesCreatedBySubAdmin(InvoiceStatus status) {
        return this.repository.countByStatusAndCreatedByRole(status, RoleEnum.ROLE_SUB_ADMIN);
    }

    public long countInvoicesCreatedBySubAdmins() {
        return this.repository.countByCreatedByRole(RoleEnum.ROLE_SUB_ADMIN);
    }
        public long countInvoicesCreatedBySubAdminsAndNotPaid() {
        return this.repository.countUnpaidOrPartiallyPaidInvoicesByCreatedByRole(RoleEnum.ROLE_SUB_ADMIN);
    }


}
