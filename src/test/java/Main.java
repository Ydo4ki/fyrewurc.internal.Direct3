import org.fw.direct3.Util;
import org.fw.direct3.val.Val;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.fw.direct3.val.Val.*;

public class Main {
    public static void main0(String[] args) {
        System.out.println(substitutor.call(konst).call(symbol("HelloWorld")).call(symbol("a")));
    }

    public static void main1(String[] args) throws IOException {
        File file = new File("fw/declarations.txt");
        Map<String, Val> initialDeclarations = new HashMap<>();
        initialDeclarations.put("K", konst);
        initialDeclarations.put("S", Val.substitutor);
        initialDeclarations = Util.getAll(file, initialDeclarations);
        Val test = initialDeclarations.get("test");
        System.out.println(test.call(eq));
    }

    public static void main2(String[] args) {
        System.out.println(eq.call(konst).call(konst));
        System.out.println(eq.call(konst).call(substitutor));
    }

    public static void main3(String[] args) throws IOException {
        Map<String, Val> declarations = new HashMap<>();
        declarations.put("K", konst);
        declarations.put("S", Val.substitutor);
        declarations.put("type", Val.type);
        Util.getAllInline(new File("fw/declarations.txt"), declarations);
        System.out.println(declarations);
    }

    public static void main(String[] args) throws Exception {
        main3(args);
    }
}
