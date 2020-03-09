package com.ve.solidprinciples;

// Open–closed principle: "Software entities ... should be open for extension,
// but closed for modification."

import java.util.List;
import java.util.stream.Stream;

public class OpenClosedPrinciple {
}

enum BookType { EBOOK, AUDIOBOOK, PRINTEDBOOK }

enum Size { DUODECIMO, OCTAVO, QUARTO, FOLIO }

class Product {
    public String name;
    public BookType bookType;
    public Size size;

    public Product(String name, BookType bookType, Size size) {
        this.name = name;
        this.bookType = bookType;
        this.size = size;
    }
}

class ProductFilter {
    public Stream<Product> filterByBookType(List<Product> products, BookType bookType) {
        return products.stream().filter(p -> p.bookType == bookType);
    }

    public Stream<Product> filterBySize(List<Product> products, Size size) {
        return products.stream().filter(p -> p.size == size);
    }

    public Stream<Product> filterBySizeAndBookType(List<Product> products, Size size, BookType bookType) {
        return products.stream().filter(p -> p.size == size && p.bookType == bookType);
    }
}

// we introduce two new interfaces that are open for extension
interface Specification<T> { boolean isSatisfied(T item); }

interface Filter<T> { Stream<T> filter(List<T> items, Specification<T> spec); }

class BookTypeSpecification implements Specification<Product> {
    private BookType bookType;

    public BookTypeSpecification(BookType bookType) {
        this.bookType = bookType;
    }

    @Override
    public boolean isSatisfied(Product p) {
        return p.bookType == bookType;
    }
}

class SizeSpecification implements Specification<Product> {
    private Size size;

    public SizeSpecification(Size size) {
        this.size = size;
    }

    @Override
    public boolean isSatisfied(Product p) {
        return p.size == size;
    }
}

class AndSpecification<T> implements Specification<T> {
    private Specification<T> first, second;

    public AndSpecification(Specification<T> first, Specification<T> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public boolean isSatisfied(T item) {
        return first.isSatisfied(item) && second.isSatisfied(item);
    }

}

class BetterFilter implements Filter<Product> {
    @Override
    public Stream<Product> filter(List<Product> items, Specification<Product> spec) {
        return items.stream().filter(p -> spec.isSatisfied(p));
    }
}

class RunDemoOCP {
    public static void main(String[] args) {
        Product book1 = new Product("A Tale of Two Cities by Charles Dickens", BookType.AUDIOBOOK, Size.DUODECIMO);
        Product book2 = new Product("The Lord of the Rings by J.R.R. Tolkien", BookType.AUDIOBOOK, Size.QUARTO);
        Product book3 = new Product("The Da Vinci Code by Dan Brown", BookType.PRINTEDBOOK, Size.QUARTO);

        List<Product> products = List.of(book1, book2, book3);

        ProductFilter pf = new ProductFilter();
        System.out.println("First Approach");
        System.out.println("-------------------");
        System.out.println("Audiobook products:");
        pf.filterByBookType(products, BookType.AUDIOBOOK)
                .forEach(p -> System.out.println(" - " + p.name + " is audiobook"));

        // ^^ FIRST APPROACH

        System.out.println("---------------------------------------------------------------");
        System.out.println("---------------------------------------------------------------");

        // vv SECOND APPROACH
        BetterFilter bf = new BetterFilter();
        System.out.println("Second Approach");
        System.out.println("-------------------");
        System.out.println("Audiobook products:");
        bf.filter(products, new BookTypeSpecification(BookType.AUDIOBOOK))
                .forEach(p -> System.out.println(" - " + p.name + " is audiobook"));

        System.out.println("Quatro size products:");
        bf.filter(products, new SizeSpecification(Size.QUARTO))
                .forEach(p -> System.out.println(" - " + p.name + " is quatro size"));

        System.out.println("Quatro size printed book items:");
        bf.filter(products,
                new AndSpecification<>(
                        new BookTypeSpecification(BookType.PRINTEDBOOK),
                        new SizeSpecification(Size.QUARTO)
                ))
                .forEach(p -> System.out.println(" - " + p.name + " is quatro size and printed book"));
    }
}