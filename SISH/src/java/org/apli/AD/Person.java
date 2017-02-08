/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apli.AD;

/**
 *
 * @author Isa
 */
public class Person {

    private String name1;

    public String getName() {
        return name1;
    }

    public void setName(String name) {
        this.name1 = name;
    }

    public String getSurname() {
        return surname1;
    }

    public void setSurname(String surname) {
        this.surname1 = surname;
    }

    public int getAge() {
        return age1;
    }

    public void setAge(int age) {
        this.age1 = age;
    }

    public String getCity() {
        return city1;
    }

    public void setCity(String city) {
        this.city1 = city;
    }
    private String surname1;
    private int age1;
    private String city1;
    public Person(String name,String surname,int age,String city){
        this.name1=name;
        this.surname1=surname;
        this.age1=age;
        this.city1=city;
    }
   
}
