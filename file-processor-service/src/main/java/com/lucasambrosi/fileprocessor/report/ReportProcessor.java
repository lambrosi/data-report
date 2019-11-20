package com.lucasambrosi.fileprocessor.report;

import com.lucasambrosi.fileprocessor.dto.FileContentDto;
import com.lucasambrosi.fileprocessor.dto.ReportDto;
import com.lucasambrosi.fileprocessor.model.Customer;
import com.lucasambrosi.fileprocessor.model.Model;
import com.lucasambrosi.fileprocessor.model.Sale;
import com.lucasambrosi.fileprocessor.model.Salesman;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ReportProcessor {

    public ReportDto generateReport(FileContentDto<Model> data) {
        Map<Class<? extends Model>, Long> countGrouppedByClass = countGrouppedByClass(data.getContent());
        Long amountCustomers = countGrouppedByClass.get(Customer.class);
        Long amountSalesman = countGrouppedByClass.get(Salesman.class);

        final List<Sale> saleList = getSaleListFromData(data.getContent());
        Long idMostExpensiveSale = getMostExpensiveSaleId(saleList);
        String worstSalesman = getWorstSalesmanName(saleList);

        return new ReportDto(data.getFilename(), amountCustomers, amountSalesman, idMostExpensiveSale, worstSalesman);
    }

    private Map<Class<? extends Model>, Long> countGrouppedByClass(List<Model> data) {
        return data.stream()
                .collect(Collectors.groupingBy(Model::getClass, Collectors.counting()));
    }

    private List<Sale> getSaleListFromData(List<Model> data) {
        return data.stream()
                .filter(Sale.class::isInstance)
                .map(Sale.class::cast)
                .collect(Collectors.toList());
    }

    private Long getMostExpensiveSaleId(List<Sale> saleList) {
        return saleList.stream()
                .max(Comparator.comparingDouble(Sale::getTotalPriceSale))
                .map(Sale::getId)
                .orElse(0L);
    }

    private String getWorstSalesmanName(List<Sale> saleList) {
        Map<String, List<Sale>> salesGroupBySalesman = saleList.stream()
                .collect(Collectors.groupingBy(Sale::getSalesmanName));

        Map<String, Double> totalValueSoldBySalesman = salesGroupBySalesman.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, this::sumValueFromAllSales));

        return totalValueSoldBySalesman.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("/undefined/");
    }

    private Double sumValueFromAllSales(Map.Entry<String, List<Sale>> it) {
        return it.getValue().stream().mapToDouble(Sale::getTotalPriceSale).sum();
    }
}
