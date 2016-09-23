public class Unit {
    public class Person {
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
    }

    rule Adult {
        when {
            Person p := /data{ #Person, age > 18 }
        } then {
            System.out.println(p.getName());
        }
    }
}
