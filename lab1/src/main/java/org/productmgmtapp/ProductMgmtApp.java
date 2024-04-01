package org.productmgmtapp;

import org.productmgmtapp.model.Product;

import java.time.LocalDate;
import java.util.*;

public class ProductMgmtApp {
    public static void main(String[] args) {
        List<Product> products = fillProductList();
        printProductsJSON(products);
    }

    public static List<Product> fillProductList() {
        List<Product> productList = new ArrayList<>();

        productList.add(new Product(3128874119L, "Banana", LocalDate.parse("2023-01-24"), 124, 0.55));
        productList.add(new Product(2927458265L, "Apple", LocalDate.parse("2022-12-09"), 18, 1.09));
        productList.add(new Product(9189927460L, "Carrot", LocalDate.parse("2023-03-31"), 89, 2.99));

        return productList;
    }

    public static void printProductsJSON(List<Product> products) {
        products.sort(Comparator.comparing(Product::getName));

        System.out.println("Products in JSON format:");

        System.out.println("[");
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.printf("\t{\"productId\": %d, \"name\": \"%s\", \"dateSupplied\": \"%s\", " +
                            "\"quantityInStock\": %d, \"unitPrice\": \"%s\"}", product.getProductId(), product.getName(),
                    product.getDateSupplied(), product.getQuantityInStock(), product.getUnitPrice());
            if (i != products.size() - 1) {
                System.out.println(",");
            } else {
                System.out.println();
            }
        }
        System.out.println("]");


        System.out.println("Products in XML format:");
        System.out.println("<products>");
        for (Product product : products) {
            System.out.print("\t<product");
            System.out.print(" productId="+ '"' + product.getProductId() + '"');
            System.out.print(" name=" + '"' + product.getName() + '"');
            System.out.print(" dateSupplied=" + '"' + product.getDateSupplied() + '"');
            System.out.print(" quantityInStock=" + '"' + product.getQuantityInStock() + '"');
            System.out.print(" unitPrice=" + '"' + product.getUnitPrice() + '"');
            System.out.print(" ></product>");
            System.out.println();
        }
        System.out.println("</products>");

        System.out.println("Products in CSV format:");
        System.out.println("productId,name,dateSupplied,quantityInStock,unitPrice");
        for (Product product : products) {
            System.out.println(product.getProductId() + "," +
                    product.getName() + "," +
                    product.getDateSupplied() + "," +
                    product.getQuantityInStock() + "," +
                    product.getUnitPrice());
        }
    }
}