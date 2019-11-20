package com.lucasambrosi.fileprocessor.model;

import java.util.regex.Matcher;

public class Salesman implements Model<Salesman> {

    private static final int CPF_INDEX = 2;
    private static final int NAME_INDEX = 3;
    private static final int SALARY_INDEX = 4;

    private Long cpf;
    private String name;
    private Double salary;

    @Override
    public Salesman of(Matcher splittedSalesman) {
        this.cpf = Long.valueOf(splittedSalesman.group(CPF_INDEX));
        this.name = splittedSalesman.group(NAME_INDEX);
        this.salary = Double.valueOf(splittedSalesman.group(SALARY_INDEX));
        return this;
    }

    public Long getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public Double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return "Salesman{" +
                "cpf=" + cpf +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                '}';
    }
}
