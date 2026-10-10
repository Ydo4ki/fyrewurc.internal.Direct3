import com.ydo4ki.esast.EsastException;
import parser.Parser;
import val.Val;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static val.Val.*;

public class Main {
    public static void main0(String[] args) {
        System.out.println(substitutor.call(konst).call(symbol("HelloWorld")).call(symbol("a")));
    }

    public static void main(String[] args) throws IOException {
        File file = new File("declarations.txt");
        Parser parser = new Parser(file);
        Map<String, Val> initialDeclarations = new HashMap<>();
        initialDeclarations.put("K", Val.konst);
        initialDeclarations.put("S", Val.substitutor);
        try {
            parser.parseAll(initialDeclarations);
        } catch (EsastException e) {
            EsastException.handleException(System.err, e, String.join("\n", Files.readAllLines(file.toPath())), file);
            e.printStackTrace();
        }
        System.out.println(initialDeclarations);
    }
}
