package org.drl8.test;

import java.util.List;

public class Unit {

    private final List<Person> data;

    public Unit(List<Person> data) {
        this.data = data;
    }

    public static class Person {
        private final String name;
        private final int age;

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }

        public boolean isAdult() {
            return age > 18;
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
