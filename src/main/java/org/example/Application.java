package org.example;

import com.github.javafaker.Faker;
import org.example.classes.Customer;
import org.example.classes.Order;
import org.example.classes.Product;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application {
    static Supplier<Customer> newCustumer = () -> {
        Faker faker = new Faker(Locale.ITALY);
        Random rnd = new Random();
        return new Customer(faker.lordOfTheRings().character(), rnd.nextInt(0, 4));
    };
    static Supplier<Product> newBeer = () -> {
        Faker faker = new Faker(Locale.ITALY);
        Random rnd = new Random();
        return new Product(faker.beer().name(), "beer", rnd.nextDouble(4, 25));
    };
    static Supplier<Product> newBook = () -> {
        Faker faker = new Faker(Locale.ITALY);
        Random rnd = new Random();
        return new Product(faker.book().title(), "book", rnd.nextDouble(3, 50));
    };
    static Supplier<Product> newFood = () -> {
        Faker faker = new Faker(Locale.ITALY);
        Random rnd = new Random();
        return new Product(faker.food().dish(), "food", rnd.nextDouble(7, 40));
    };

    static Supplier<LocalDate> past = () -> {
        Random rnd = new Random();
        LocalDate today = LocalDate.now().minusDays(rnd.nextInt(4));
        return today;
    };
    static Supplier<LocalDate> future = () -> {
        Random rnd = new Random();
        LocalDate today = LocalDate.now().plusDays(rnd.nextInt(4));
        return today;
    };
    static List<Customer> customers = new ArrayList<>();
    static List<Product> products = new ArrayList<>();
    static List<Order> orders = new ArrayList<>();

    public static void main(String[] args) {
        create();
        System.out.println(customers.get(1));
        System.out.println(orders);

        System.out.println("Esercizio1");
        Map<Customer, List<Order>> custOrder = orders.stream().filter(order -> order.getCustomers() != null).collect(Collectors.groupingBy(Order::getCustomers));
        custOrder.forEach((custumer, orders) -> System.out.println("Customer; " + custumer.getName() + ", " + orders));

        System.out.println("-----------------------------------------");
        System.out.println();

    }

    static void create() {

        for (int i = 0; i < 20; i++) {
            customers.add(newCustumer.get());
            products.add(newBeer.get());
            products.add(newBook.get());
            products.add(newFood.get());
        }
        for (int i = 0; i < customers.size(); i++) {
            Random rnd = new Random();
            List<Product> productsOrder = new ArrayList<>();
            for (int y = 0; y < 3; y++) {
                productsOrder.add(products.get(rnd.nextInt(products.size())));
            }
            orders.add(new Order("complete", past.get(), future.get(), productsOrder, customers.get(rnd.nextInt(customers.size()))));

//            orders.add(new Order("complete", past.get(), future.get(), productsOrder, customers.get(rnd.nextInt(customers.size()))));
        }
    }
}

