/*
* (C) Copyright 2010-2017 Nexmo Inc. All Rights Reserved.
* These materials are unpublished, proprietary, confidential source code of
* Nexmo Inc and constitute a TRADE SECRET of Nexmo Inc.
* Nexmo Inc retains all titles to an intellectual property rights in these materials.
*/
package lambdas;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;

public class LambdasTest {
    
    
    @Test
    public void predicate() {
        List<Employee> emps = new ArrayList<>();
        emps.add(new Employee("A",100));
        emps.add(new Employee("B",5000));
        emps.add(new Employee("C",6000));
         
        assertEquals("B",findFirst(emps, e -> e.getSalary() >= 5000).name);
    }

    public <T> T findFirst (List<T> canditates, Predicate<T> match) {
        for(T c : canditates) {
            if(match.test(c)) return c;
        }
        return null;
    }
    
    class Employee {
        int salary;
        String name;
        
        public Employee(String name, int salary) {
            this.name = name;
            this.salary = salary;
        }

        int getSalary() {
            return salary;
        }
        @Override
        public String toString() {
            return ""+salary;
        }
    }
}
