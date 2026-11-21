package com.study.jdbc;

public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private double salary;
    private int depratmentId;

    public Employee(int id, String firstName, String lastName, double salary, int depratmentId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.salary = salary;
        this.depratmentId = depratmentId;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public double getSalary() {
        return salary;
    }

    public int getDepratmentId() {
        return depratmentId;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", salary=" + salary +
                ", depratmentId=" + depratmentId +
                '}';
    }
}
