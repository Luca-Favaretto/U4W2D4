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
        return LocalDate.now().minusDays(rnd.nextInt(4));
    };
    static Supplier<LocalDate> future = () -> {
        Random rnd = new Random();
        return LocalDate.now().plusDays(rnd.nextInt(4));
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


        System.out.println("Exercise1");
        Map<String, List<Order>> list = orders.stream().collect(Collectors.groupingBy(Order::getCustomersName));
        list.forEach((custumer, orders) -> System.out.println(custumer + ", " + orders));

        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Exercise2");
        Map<String, Double> list2 = orders.stream().collect(Collectors.groupingBy(Order::getCustomersName, Collectors.summingDouble(Order::getTotal)));
        list2.forEach((custumer, sumOrd) -> System.out.println(custumer + " spent " + sumOrd + "$"));
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Exercise3");
        products.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).limit(5).forEach(product -> System.out.println("The product more expensive are " + product.getName() + " and they cost " + product.getPrice() + "$"));
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Exercise4");
        double avaregeOrders = orders.stream().mapToDouble(Order::getTotal).average().orElse(0.0);
        System.out.println("The average of orders is " + avaregeOrders);

        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Exercise5");
        Map<String, Double> list5 = products.stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.summingDouble(Product::getPrice)));
        list5.forEach((category, sumCateg) -> System.out.println("Category " + category + " sum " + sumCateg));
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Exercise6");
        saveToDisk();
        System.out.println("-----------------------------------------");
        System.out.println();
        System.out.println("Exercise7");
        List<Product> prod = loadFromDisk();
        prod.forEach(System.out::println);


    }

    static void create() throws IOException {

        for (int i = 0; i < 5; i++) {
            customers.add(newCustomer.get());
            products.add(newBeer.get());
            products.add(newBook.get());
            products.add(newFood.get());
        }

        for (Customer customer : customers) {

            Random rnd = new Random();
            List<Product> productsOrder = new ArrayList<>();
            for (int y = 0; y < 3; y++) {
                productsOrder.add(products.get(rnd.nextInt(products.size())));
            }


            orders.add(new Order("complete", past.get(), future.get(), productsOrder, customer));

        }


    }

    public static void saveToDisk() throws IOException {
        StringBuilder toWrite = new StringBuilder();

        for (Product product : products) {
            toWrite.append(product.getName()).append("@").append(product.getCategory()).append("@").append(product.getPrice()).append("#");

        }
        File file = new File("products.txt");
        FileUtils.writeStringToFile(file, toWrite.toString(), "UTF-8");
    }

    public static List<Product> loadFromDisk() throws IOException {
        File file = new File("products.txt");

        String fileString = FileUtils.readFileToString(file, "UTF-8");

        List<String> splitElementString = Arrays.asList(fileString.split("#"));

        return splitElementString.stream().map(stringa -> {

            String[] productInfos = stringa.split("@");
            return new Product(productInfos[0], productInfos[1], Double.parseDouble(productInfos[2]));
        }).toList();

    }
}

