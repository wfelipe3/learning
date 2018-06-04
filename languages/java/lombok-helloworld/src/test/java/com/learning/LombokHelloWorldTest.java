package com.learning;

import lombok.*;
import org.junit.Test;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class LombokHelloWorldTest {

    @Test
    public void valTest() {
        val message = "HelloWorld";
        assertThat(message, is("HelloWorld"));
    }

    @Test(expected = NullPointerException.class)
    public void testNonNull() {
        assertThat(getValue("test"), is("test"));
        getValue(null);
    }

    @Test
    public void testGetterSetter() {
        val user = new User();
        user.setName("felipe");
        assertThat(user.getName(), is("felipe"));
    }

    @Test
    public void testToString() {
        val user = new User();
        user.setName("felipe");
        assertThat(user.toString(), is("LombokHelloWorldTest.User(name=felipe)"));
    }

    @Test
    public void testEqualsAndHashCode() {
        val user = new User();
        user.setName("felipe");

        val user2 = new User();
        user2.setName("felipe");

        assertThat(user, is(user2));
    }

    @Test
    public void testConstructor() {
        val person = new Person("felipe", 30);
        val person2 = new Person("felipe", 30);
        assertThat(person, is(person2));
    }

    private String getValue(@NonNull String value) {
        return value;
    }

    @ToString
    @EqualsAndHashCode
    private class User {
        @Getter
        @Setter
        private String name;
    }

    @EqualsAndHashCode
    @AllArgsConstructor
    private class Person {
        @Getter
        private String name;
        @Getter
        private int age;
    }
}
