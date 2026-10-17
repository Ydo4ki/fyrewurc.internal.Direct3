import org.fw.direct3.val.Util;
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
        File file = new File("fw/bootstrap.dfw");
        Map<String, Val> initialDeclarations = new HashMap<>();
        initialDeclarations.put("K", konst);
        initialDeclarations.put("S", Val.substitutor);
        initialDeclarations = Util.getAll(file, initialDeclarations);
        Val test = initialDeclarations.get("test");
        System.out.println(test.call(EQ));
    }

    public static void main2(String[] args) {
        System.out.println(EQ.call(konst).call(konst));
        System.out.println(EQ.call(konst).call(substitutor));
    }

    public static void main3(String[] args) throws IOException {
        Map<String, Val> decls = Util.loadBootStrap();
        Util.getAllInline(new File("fw/test.dfw"), decls);
        for (Map.Entry<String, Val> entry : decls.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
    }

    public static void main(String[] args) throws Exception {
        main3(args);
    }
}
