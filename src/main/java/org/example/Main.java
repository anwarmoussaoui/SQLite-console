package org.example;

import org.graalvm.polyglot.*;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Main {

    //to save your progress
    private static void saveData(Value buffer){
        if (buffer != null && buffer.hasArrayElements()) {
            int length = (int) buffer.getArraySize();
            byte[] fileBytes = new byte[length];

            // Extract byte array from the Polyglot Value (JavaScript binary data)
            for (int j = 0; j < length; j++) {
                fileBytes[j] = (byte) buffer.getArrayElement(j).asInt();
            }

            try {
                // Save the binary data as a SQLite database file
                Files.write(Paths.get("output.sqlite"), fileBytes);  // Save as .sqlite
                System.out.println("Database saved as output.sqlite");
            } catch (Exception e) {
                System.err.println("Error saving the database file: " + e.getMessage());
            }
        } else {
            System.err.println("No buffer exported from JavaScript.");
        }
    }



    private static Map<String, String> getLanguageOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("js.webassembly", "true");
        options.put("js.commonjs-require", "true");
        options.put("js.esm-eval-returns-exports", "true");
        return options;
    }

    public static void main(String[] args) {


        try (Context context = Context.newBuilder("js", "wasm")
                .allowAllAccess(true)
                .options(getLanguageOptions())
                .build()) {
            byte[] wasmfile = Files.readAllBytes(Paths.get("./src/main/resources/sql-wasm.wasm"));
            context.getBindings("js").putMember("wasmfile", wasmfile);

            context.eval(Source.newBuilder("js",Main.class.getResource("/sql-wasm.js"))
                    .build());
            context.eval(Source.newBuilder("js",Main.class.getResource("/implementaion.js"))
                    .build());
            Scanner scanner = new Scanner(System.in);
            String query = "";
            while (!query.equalsIgnoreCase("quit")) {
                System.out.println("Enter SQL query (or 'quit' to exit): ");
                query = scanner.nextLine();
                if (!query.equalsIgnoreCase("quit")) {
                    context.getBindings("js").getMember("execQuery").execute(query);
                }
            }
            context.getBindings("js").getMember("close").execute();
            Value buffer = context.getPolyglotBindings().getMember("binaryData");
            saveData(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}