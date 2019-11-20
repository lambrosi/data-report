package com.lucasambrosi.fileprocessor.dto;

public class ReportDto {

    private String filename;
    private Long amountCustomers;
    private Long amountSalesman;
    private Long mostExpensiveSaleId;
    private String worstSalesmanName;

    public ReportDto(String filename, Long amountCustomers, Long amountSalesman, Long mostExpensiveSaleId, String worstSalesmanName) {
        this.filename = filename;
        this.amountCustomers = amountCustomers;
        this.amountSalesman = amountSalesman;
        this.mostExpensiveSaleId = mostExpensiveSaleId;
        this.worstSalesmanName = worstSalesmanName;
    }

    public String getFilename() {
        return filename;
    }

    public Long getAmountCustomers() {
        return amountCustomers;
    }

    public Long getAmountSalesman() {
        return amountSalesman;
    }

    public Long getMostExpensiveSaleId() {
        return mostExpensiveSaleId;
    }

    public String getWorstSalesmanName() {
        return worstSalesmanName;
    }

    @Override
    public String toString() {
        return "ReportDto{" +
                "amountCustomers=" + amountCustomers +
                ", amountSalesman=" + amountSalesman +
                ", mostExpensiveSaleId=" + mostExpensiveSaleId +
                ", worstSalesmanName='" + worstSalesmanName + '\'' +
                '}';
    }
}
