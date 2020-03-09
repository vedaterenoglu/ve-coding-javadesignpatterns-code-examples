package com.ve.solidprinciples;
// Single responsibility principle: A class should only have a single responsibility,
// that is, only changes to one part of the software's specification should be able to
// affect the specification of the class.

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SingleResponsibilityPrinciple {
}

class Book {
    private final List<String> books = new ArrayList<>();

    private static int i = 0;

    public void addBook(String text)
    {
        books.add("" + (++i) + ": " + text);
    }

    public void removeBook(int index) { books.remove(index); }

    @Override
    public String toString() {
        return String.join(System.lineSeparator(), books);
    }

    // here we break SRP
    public void save(String filename) throws Exception {
        try (PrintStream out = new PrintStream(filename)) {
            out.println(toString());
        }
    }

    public void load(String filename) {}
    public void load(URL url) {}

    public List<String> getBooks() {
        return books;
    }
}

// handles the responsibility of persisting objects
class Persistence {
    public void saveToFile(Book book, String filename, boolean overwrite) throws Exception {
        if (overwrite || new File(filename).exists())
            try (PrintStream out = new PrintStream(filename)) {
                out.println(book.toString());
            }
    }

    public void load(Book book, String filename) {}
    public void load(Book book, URL url) {}
}

// add two books and save them to 'books.txt file under the src folder'

class RunDemoSRP {
    private static Book b = new Book();
    public static void main(String[] args) throws Exception {
        // add the two books to yhe list books then print them in terminal
        b.addBook("A Tale of Two Cities by Charles Dickens, e-Book");
        b.addBook("The Lord of the Rings by J.R.R. Tolkien, Audiobook");
        b.addBook("The Da Vinci Code by Dan Brown, Audiobook");

        // It will write on the terminal the three books that we have added
        System.out.println(" ");
        System.out.println("After adding three books and before removing one of them");
        System.out.println(b);
        System.out.println("-----------------------------------------------------");

        // It will write on the terminal the two books
        System.out.println("After removing one of the books  (index(0))");
        b.removeBook(0);
        System.out.println(b);

        // save them in 'books.txt' file
        Persistence p = new Persistence();
        String filename = "books.txt";
        p.saveToFile(b, filename, true);
    }
}




