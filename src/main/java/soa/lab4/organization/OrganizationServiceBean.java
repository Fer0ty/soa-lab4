package soa.lab4.organization;

import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.soap.SOAPBinding;
import soa.lab4.organization.model.Organization;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@WebService(
        targetNamespace = "http://organization.lab4.soa/",
        serviceName = "OrganizationServiceBeanService",
        portName = "OrganizationServiceBeanPort"
)
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class OrganizationServiceBean implements OrganizationService {

    private final Map<Long, Organization> organizations = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    @WebMethod(operationName = "getOrganization")
    @WebResult(name = "organization")
    public Organization getOrganization(@WebParam(name = "id") Long id) {
        return organizations.get(id);
    }

    @WebMethod(operationName = "createOrganization")
    @WebResult(name = "organization")
    public Organization createOrganization(@WebParam(name = "organization") Organization organization) {
        System.out.println("Received organization: " + organization);
        organization.setId(idGenerator.getAndIncrement());
        organizations.put(organization.getId(), organization);
        return organization;
    }

    @WebMethod(operationName = "updateOrganization")
    @WebResult(name = "organization")
    public Organization updateOrganization(
            @WebParam(name = "id") Long id,
            @WebParam(name = "updatedOrganization") Organization updatedOrganization) {
        Organization existingOrg = organizations.get(id);
        if (existingOrg != null) {
            updatedOrganization.setId(id);
            updatedOrganization.setCreationDate(existingOrg.getCreationDate());
            organizations.put(id, updatedOrganization);
        }
        return updatedOrganization;
    }

    @WebMethod(operationName = "deleteOrganization")
    @WebResult(name = "result")
    public boolean deleteOrganization(@WebParam(name = "id") Long id) {
        return organizations.remove(id) != null;
    }

    @WebMethod(operationName = "getFilteredOrganizations")
    @WebResult(name = "organizations")
    public List<Organization> getFilteredOrganizations(
            @WebParam(name = "creationDate") String creationDate,
            @WebParam(name = "annualTurnover") Integer annualTurnover,
            @WebParam(name = "sort") String sort) {
        Stream<Organization> filteredStream = organizations.values().stream();

        if (creationDate != null) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date filterDate = sdf.parse(creationDate);
                filteredStream = filteredStream.filter(org -> org.getCreationDate().after(filterDate));
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid creationDate format. Expected format: yyyy-MM-dd");
            }
        }

        if (annualTurnover != null) {
            filteredStream = filteredStream.filter(org -> org.getAnnualTurnover() < annualTurnover);
        }

        if (sort != null) {
            String[] sortParams = sort.split(",");
            if (sortParams.length == 2) {
                String field = sortParams[0];
                boolean ascending = "asc".equalsIgnoreCase(sortParams[1]);

                Comparator<Organization> comparator = getComparator(field);
                if (comparator != null) {
                    filteredStream = filteredStream.sorted(applySortOrder(comparator, ascending));
                }
            } else {
                throw new IllegalArgumentException("Invalid sort parameter. Expected format: field,asc|desc");
            }
        }

        return filteredStream.collect(Collectors.toList());
    }

    @WebMethod(operationName = "countByEmployeesCount")
    @WebResult(name = "count")
    public long countByEmployeesCount(@WebParam(name = "count") Long count) {
        return organizations.values().stream()
                .filter(org -> org.getEmployeesCount() != null && org.getEmployeesCount() > count)
                .count();
    }

    @WebMethod(operationName = "searchByFullName")
    @WebResult(name = "organizations")
    public List<Organization> searchByFullName(@WebParam(name = "substring") String substring) {
        return organizations.values().stream()
                .filter(org -> org.getFullName() != null && org.getFullName().contains(substring))
                .collect(Collectors.toList());
    }

    private Comparator<Organization> getComparator(String field) {
        return switch (field) {
            case "name" -> Comparator.comparing(Organization::getName);
            case "creationDate" -> Comparator.comparing(Organization::getCreationDate);
            case "annualTurnover" -> Comparator.comparing(Organization::getAnnualTurnover, Comparator.nullsLast(Comparator.naturalOrder()));
            default -> null;
        };
    }

    private Comparator<Organization> applySortOrder(Comparator<Organization> comparator, boolean ascending) {
        return ascending ? comparator : comparator.reversed();
    }
}
