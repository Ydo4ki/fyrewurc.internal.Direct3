import com.ydo4ki.esast.BracketsTypes;
import com.ydo4ki.esast.EsastException;
import com.ydo4ki.esast.TokenOutput;
import com.ydo4ki.esast.TokenType;
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

    public static void main1(String[] args) throws IOException {
        File file = new File("declarations.txt");
        TokenOutput toks = new TokenOutput(file, BracketsTypes.bracketsTypes, TokenOutput.Separators.of(",", "->", "."));
        Parser parser = new Parser(toks);
        Map<String, Val> initialDeclarations = new HashMap<>();
        initialDeclarations.put("K", Val.konst);
        initialDeclarations.put("S", Val.substitutor);
        try {
            parser.parseAll(initialDeclarations);
        } catch (EsastException e) {
            EsastException.handleException(System.err, e, String.join("\n", Files.readAllLines(file.toPath())), file);
            e.printStackTrace();
        }
        Val test = initialDeclarations.get("test");
        System.out.println(test.call(eq));
    }

    public static void main2(String[] args) {
        System.out.println(eq.call(konst).call(konst));
        System.out.println(eq.call(konst).call(substitutor));
    }

    public static void main(String[] args) throws Exception {
        main1(args);
    }
}
