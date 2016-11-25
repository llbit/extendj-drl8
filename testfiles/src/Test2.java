package org.drl8.test;

import java.util.List;

public class Unit {

    private final List<Person> data;

    public Unit(List<Person> data) {
        this.data = data;
    }

    public static class Person {
        private final String name;
        private final int myAge;

        public Person(String name, int age) {
            this.name = name;
            this.myAge = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return myAge;
        }
    }

    rule Adult {
        when {
            Person p := /data{age > 18};
        } then {
            System.out.println(p.getName());
        }
    }
}
