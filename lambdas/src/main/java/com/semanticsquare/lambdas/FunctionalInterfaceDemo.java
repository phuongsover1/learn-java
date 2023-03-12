package com.semanticsquare.lambdas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

import org.htmlcleaner.HtmlCleaner;

class FunctionalInterfaceDemo {
    public static void main(String[] args) {
        String doc1 = "<html><body>One of the most common uses of <i>streams</i> is to represent queries over data in collections</body></html>";
        String doc2 = "<html><body>Information integration systems provide valuable services to users by integrating information from a number of autonomous, heterogeneous and distributed Web sources</body></html>";
        String doc3 = "<html><body>Solr is the popular, blazing fast open source enterprise search platform from the Apache Lucene</body></html>";
        String doc4 = "<html><body>Java 8 goes one more step ahead and has developed a streams API which lets us think about parallelism</body></html>";

        List<String> documents = new ArrayList<>(Arrays.asList(doc1, doc2, doc3, doc4));
        List<String> targetDocuments = new ArrayList<>();
        for (String doc : documents) {
            Predicate<String> myFilter = d -> d.contains("stream");
            // boolean isTargetDoc = filter(doc, d -> d.contains("streams"));
            // boolean isTargetDoc = filter(doc, myFilter);

            // BiFunction<String, String, Boolean> biFunction = (d, c) -> d.contains(c);

            // (ii) Method References (ObjectRef::instanceMethod)
            Function<String, Boolean> function = doc::contains;
            // (iii) Method References (Classname::instanceMethod)
            BiFunction<String, String, Boolean> biFunction = String::contains;


            // if (biFunction.apply(doc, "streams")) {
            if (function.apply("streams")) {
                // doc = transform(doc, d -> Indexer.stripHtmlTags(d));
                // doc = transform(doc, d -> Indexer.removeStopWords(d));
                // doc = transform(doc, myRemoveStopWords);
                UnaryOperator<String> HtmlCleaner = d -> Indexer.stripHtmlTags(d);
                // Function<String, String> myRemoveStopWords = d -> Indexer.removeStopWords(d);

                // (i) Method References (ClassName::staticMethod)
                Function<String, String> myRemoveStopWords = Indexer::removeStopWords;


                Function<String, String> docProcessor = HtmlCleaner.andThen(myRemoveStopWords);
                doc = docProcessor.apply(doc);

                // System.out.println(doc);

                targetDocuments.add(doc);

            }
        }
        // targetDocuments.forEach(d -> System.out.println(d));

        // (ii) Method References (objectRef::instanceMethod)
        // targetDocuments.forEach(System.out::println);

        for (String doc : targetDocuments) {
            if (doc.length() > 80) {
                try {

                    throw new Exception("Oversized document !!!");
                } catch (Exception e) {
                    print(() -> e.getMessage() + " ~ " + doc);
                }

            }
        }

        // Typical scenario
        Supplier<String> supplier = String::new; // () -> new String();
        System.out.println("\nsupplier.get: " + supplier.get());

        Function<String, String> function = String::new;//s -> new String(s);
        System.out.println("\nfunction.apply: " + function.apply("java"));

        BiFunction<Integer,Float,HashMap> biFunction = HashMap::new; //(i,fl) -> new HashMap(i,fl);
        System.out.println("\nbiFunction.apply: " + biFunction.apply(100, 0.75f));

        Consumer<String> consumer = String::new;
        consumer.accept("Java");
        // Create own interface if exising functional interfaces are not useful!!
        // Later, we will dicuss them in Stream API


    }

    private static boolean errorFlag = false;

    private static void print(Supplier<String> supplier) {
        if (errorFlag)
            System.out.println(supplier.get());
    }

    static boolean filter(String doc, Predicate<String> filter) {
        return filter.test(doc);
    }

    static String transform(String doc, UnaryOperator<String> transformer) {
        return transformer.apply(doc);
    }

    static String transform(String doc, Function<String, String> transformer) {
        // return transformer.apply(doc);
        return transformer.apply(doc);
    }
}

