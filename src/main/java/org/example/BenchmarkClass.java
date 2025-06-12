package org.example;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.HostAccess;
import org.graalvm.polyglot.Source;
import org.openjdk.jmh.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Warmup(iterations = 3, time = 10)
@Measurement(iterations = 3, time = 10)
@Fork(1)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class BenchmarkClass {

    public Context context;


    private static Map<String, String> getLanguageOptions() {
        Map<String, String> options = new HashMap<>();
        options.put("js.esm-eval-returns-exports", "true");
        options.put("js.webassembly", "true");
        options.put("js.commonjs-require", "true");
        return options;
    }


    @Setup(Level.Trial)
    public void setup() throws IOException {
            Context context = createPolyglotContext();
        byte[] wasmfile = Files.readAllBytes(Paths.get("./src/main/resources/sql-wasm.wasm"));
        context.getBindings("js").putMember("wasmfile", wasmfile);

        context.eval(Source.newBuilder("js",BenchmarkClass.class.getResource("/sql-wasm.js"))
                .build());
        context.eval(Source.newBuilder("js",BenchmarkClass.class.getResource("/implementaion.js"))
                .build());
            this.context=context;

    }

    @Benchmark
    public void test() throws IOException {
        String query="CREATE TABLE orders (id INT, user_id INT, amount DECIMAL);";
        String query2 = "INSERT INTO orders VALUES (1, 1, 100.0), (2, 1, 200.0), (3, 2, 150.0), (4, 3, 50.0);";
        String query3 = "SELECT * FROM orders;";
        String query4 = "drop Table orders;";
        context.getBindings("js").getMember("execQuery").execute(query);
        context.getBindings("js").getMember("execQuery").execute(query2);
        context.getBindings("js").getMember("execQuery").execute(query3);
        context.getBindings("js").getMember("execQuery").execute(query4);


    }

    private static Context createPolyglotContext() {
        return Context.newBuilder("js", "wasm")
                .allowAllAccess(true)
                .options(getLanguageOptions())
                .build();
    }


}
