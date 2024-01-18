package org.example;

import com.github.javafaker.Faker;
import org.apache.commons.io.FileUtils;
import org.example.classes.Customer;
import org.example.classes.Order;
import org.example.classes.Product;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Application {
    static Supplier<Customer> newCustomer = () -> {
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

    public static void main(String[] args) throws IOException {

        try {
            create();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        System.out.println(customers.get(0));
        System.out.println(customers);
        orders.forEach(System.out::println);

        System.out.println("Esercizio1");
        Map<Customer, List<Order>> custOrder = orders.stream().filter(customers -> customers == null).collect(Collectors.groupingBy(Order::getCustomers));
        custOrder.forEach((custumer, orders) -> System.out.println("Customer; " + custumer.getName() + ", " + orders));

        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Esercizio2");
        Map<Customer, Double> list2 = orders.stream().filter(customers -> customers == null).collect(Collectors.groupingBy(Order::getCustomers, Collectors.summingDouble(Order::getTotal)));
        list2.forEach((custumer, sumOrd) -> System.out.println("Customer; " + custumer.getName() + "spent " + sumOrd));
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Esercizio3");
        products.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).limit(5).forEach(product -> System.out.println(product.getPrice()));
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Esercizio4");
        double sum = orders.stream().mapToDouble(Order::getTotal).average().orElse(0.0);
        System.out.println(sum);
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Esercizio5");
        Map<String, Double> list5 = products.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice)));
        list5.forEach((category, sumCateg) -> System.out.println("Category " + category + " sum " + sumCateg));
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Esercizio6");
        saveToDisk();
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Esercizio7");
        List<Product> prod = loadFromDisk();
        prod.forEach(System.out::println);


    }

    static void create() throws IOException {

        for (int i = 0; i < 3; i++) {
            customers.add(newCustomer.get());
            products.add(newBeer.get());
            products.add(newBook.get());
            products.add(newFood.get());
        }

        for (Customer customer : customers) {
            System.out.println(customer);
            Random rnd = new Random();
            List<Product> productsOrder = new ArrayList<>();
            for (int y = 0; y < 3; y++) {
                productsOrder.add(products.get(rnd.nextInt(products.size())));
            }


            orders.add(new Order("complete", past.get(), future.get(), productsOrder, customer));

        }


    }

    public static void saveToDisk() throws IOException {
        String toWrite = "";

        for (Product product : products) {
            toWrite += product.getName() + "@" + product.getCategory() + "@" + product.getPrice() + "#";

        }
        File file = new File("products.txt");
        FileUtils.writeStringToFile(file, toWrite, "UTF-8");
    }

    public static List<Product> loadFromDisk() throws IOException {
        File file = new File("products.txt");

        String fileString = FileUtils.readFileToString(file, "UTF-8");

        List<String> splitElementiString = Arrays.asList(fileString.split("#"));

        return splitElementiString.stream().map(stringa -> {

            String[] productInfos = stringa.split("@");
            return new Product(productInfos[0], productInfos[1], Double.parseDouble(productInfos[2]));
        }).toList();

    }
}

