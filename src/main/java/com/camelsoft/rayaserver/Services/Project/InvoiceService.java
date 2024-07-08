package com.camelsoft.rayaserver.Services.Project;

import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceRelated;
import com.camelsoft.rayaserver.Enum.Project.Invoice.InvoiceStatus;
import com.camelsoft.rayaserver.Enum.Project.Loan.LoanStatus;
import com.camelsoft.rayaserver.Enum.User.RoleEnum;
import com.camelsoft.rayaserver.Models.Auth.Role;
import com.camelsoft.rayaserver.Models.Project.Invoice;
import com.camelsoft.rayaserver.Models.Project.Loan;
import com.camelsoft.rayaserver.Models.Project.Product;
import com.camelsoft.rayaserver.Models.User.users;
import com.camelsoft.rayaserver.Repository.Project.InvoiceRepository;
import com.camelsoft.rayaserver.Repository.Project.InvoiceRepository;
import com.camelsoft.rayaserver.Response.Project.DynamicResponse;
import com.camelsoft.rayaserver.Tools.Exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
 public Invoice FindByThirdpartypoid(String thpoid) {
        try {

                return this.repository.findByThirdpartypoid(thpoid);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }
 public boolean existByThirdpartypoid(String thpoid) {
        try {

                return this.repository.existsByThirdpartypoid(thpoid);

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
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



 public DynamicResponse FindAllByStateandsupplier(int page, int size, InvoiceStatus status, InvoiceRelated related,users user) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Invoice> pckge = this.repository.findAllByStatusAndArchiveIsFalseAndRelatedAndCreatedby(pg, status, related,user);
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


    public List<Invoice> findAllByDueDateAndArchiveIsFalse(Date duedate) {
        try {
            return this.repository.findByDuedateAndArchiveIsFalse(duedate);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public List<Invoice> findAllInvoicesBySubAdmin(users user) {
        try {
            return this.repository.findByPurshaseorder_SubadminassignedtoAndArchiveIsFalseOrderByTimestampDesc(user);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

    }

    public List<Invoice> findAllInvoicesForAdmin() {
        try {
            return this.repository.findByArchiveIsFalseOrderByTimestampDesc();
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
            calendar.set(year, month , 1, 0, 0, 0);
            Date startDate = calendar.getTime();

            // Set the end date to the last day of the specified month
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            // Call repository method to count invoices within the specified month
            return this.repository.countByTimestampBetweenAndRelated(startDate, endDate, related);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }


    public Integer countAllInvoicesPerMonth(Date date) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1; // Calendar month is zero-based
            int year = calendar.get(Calendar.YEAR);

            // Set the start date to the first day of the specified month
            calendar.set(year, month , 1, 0, 0, 0);
            Date startDate = calendar.getTime();

            // Set the end date to the last day of the specified month
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            // Call repository method to count invoices within the specified month
            return this.repository.countByTimestampBetween(startDate, endDate);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }


    public Integer countInvoicePerMonthAndStatus(Date date, InvoiceRelated related) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1; // Calendar month is zero-based
            int year = calendar.get(Calendar.YEAR);

            // Set the start date to the first day of the specified month
            calendar.set(year, month , 1, 0, 0, 0);
            Date startDate = calendar.getTime();

            // Set the end date to the last day of the specified month
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            // Call repository method to count invoices within the specified month
            return this.repository.countByTimestampBetweenAndRelated(startDate, endDate, related);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }




    public Integer countInvoicePerMonthAndUser(Date date, users user) {
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int month = calendar.get(Calendar.MONTH) + 1; // Calendar month is zero-based
            int year = calendar.get(Calendar.YEAR);

            // Set the start date to the first day of the specified month
            calendar.set(year, month , 1, 0, 0, 0);
            Date startDate = calendar.getTime();

            // Set the end date to the last day of the specified month
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            Date endDate = calendar.getTime();

            // Call repository method to count invoices within the specified month
            return this.repository.countByTimestampBetweenAndCreatedbyOrRelatedto(startDate, endDate, user, user);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }






    public Integer countInvoicePerMonthAndStatus(Date date, InvoiceStatus status, InvoiceRelated related) {
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
            return this.repository.countByTimestampBetweenAndStatusAndRelated(startDate, endDate, status, related);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }


    public Integer countAllInvoicePerMonthAndStatus(Date date, InvoiceStatus status) {
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
            return this.repository.countByTimestampBetweenAndStatus(startDate, endDate, status);
        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }
    }





    public Integer countInvoicePerMonthAndStatusAndUser(Date date, InvoiceStatus status,users user) {
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
            return this.repository.countByTimestampBetweenAndStatusAndCreatedbyOrRelatedto(startDate, endDate, status, user, user);
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
        if (invoice.getProducts() == null || invoice.getProducts().isEmpty())
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

    public List<Map<String, Double>> calculateRevenueByMonthAndSupplier(Date startDate, Date endDate, users user) {
        List<Invoice> paidInvoices = this.repository.findByStatusAndArchiveIsFalseAndTimestampBetweenAndRelatedto(InvoiceStatus.PAID, startDate, endDate, user);
        List<Invoice> paidInvoices2 = this.repository.findByStatusAndArchiveIsFalseAndTimestampBetweenAndCreatedby(InvoiceStatus.PAID, startDate, endDate, user);

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
        while (calendar.getTime().before(endDate) || calendar.getTime().equals(endDate)) {
            double totalRevenue = 0.0;
            int month = calendar.get(Calendar.MONTH);
            String monthName = new SimpleDateFormat("MMMM").format(calendar.getTime());

            for (Invoice invoice : paidInvoices2) {
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
        Set<InvoiceStatus> statuses = new HashSet<>(Arrays.asList(InvoiceStatus.PAID, InvoiceStatus.PARTIALLY_PAID));
        return this.repository.countUnpaidOrPartiallyPaidInvoicesByCreatedByRole(statuses, RoleEnum.ROLE_SUB_ADMIN);
    }


    public DynamicResponse getInvoicesByUser(int page, int size, users user) {
        try {
            PageRequest pg = PageRequest.of(page, size);
            Page<Invoice> pckge = this.repository.findByCreatedbyOrRelatedto(user, user,pg);
            return new DynamicResponse(pckge.getContent(), pckge.getNumber(), pckge.getTotalElements(), pckge.getTotalPages());

        } catch (NoSuchElementException ex) {
            throw new NotFoundException(ex.getMessage());
        }

        }


        public Double  totalVehiclesPandingPaymentByUser(users user, InvoiceStatus status){
        return this.repository.findAllByCreatedbyOrRelatedtoAndStatusAndArchiveIsFalse(user,user,status)
                .stream().map(Invoice::getProducts)
                .flatMap(Set::stream)
                .mapToDouble(Product::getQuantity)
                .sum();
        }


    public Double totalRevenuByUser(users user){
        return this.repository.findAllByCreatedbyOrRelatedtoAndStatusAndArchiveIsFalse(user,user,InvoiceStatus.PAID)
                .stream().map(Invoice::getProducts)
                .flatMap(Set::stream)
                .mapToDouble(Product::getSubtotal)
                .sum();
    }

    public Date calculateInvoiceDueDate(Date timestamp, int dueDateOffset) {
        // Use Calendar to manipulate the date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);

        // Add the dueDateOffset days
        calendar.add(Calendar.DAY_OF_MONTH, dueDateOffset);

        // Set the time part to 00:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Return the resulting date
        return calendar.getTime();
    }


}
