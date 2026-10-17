package org.fw.direct3.val;

import com.ydo4ki.esast.BracketsTypes;
import com.ydo4ki.esast.EsastException;
import com.ydo4ki.esast.TokenOutput;
import org.fw.direct3.parser.Parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.fw.direct3.val.Val.konst;

public final class Util {
    private Util() {}

    public static Map<String, Val> getAll(File file, Map<String, Val> initialDeclarations) throws IOException {
        return getAllInline(file, new HashMap<>(initialDeclarations));
    }

    public static Map<String, Val> getAllInline(File file, Map<String, Val> initialDeclarations) throws IOException {
        TokenOutput toks = new TokenOutput(file, BracketsTypes.bracketsTypes, TokenOutput.Separators.of(",", "->", "."));
        Parser parser = new Parser(toks);
        try {
            parser.parseAll(initialDeclarations);
        } catch (EsastException e) {
            EsastException.handleException(System.err, e, String.join("\n", Files.readAllLines(file.toPath())), file);
            throw new IOException(e);
        }
        return initialDeclarations;
    }

    private static void getOne(Map<String, Val> declarations, String name) throws IOException {
        declarations.put(name, Util.getAll(new File("fw/" + name + ".dfw"), declarations).get(name));
    }

    public static Map<String, Val> loadBootStrap() throws IOException {
        Map<String, Val> declarations = new HashMap<>();
        declarations.put("K", konst);
        declarations.put("S", Val.substitutor);
        declarations.put("type", Val.type);
        declarations.put("EQ", Val.EQ);
        declarations.put("Call", Val.m_wrap(BoxValue.Call.type));
        declarations.put("telephonize", Val.telephonize);
        declarations.put("\\bot", Val.m_wrap(UNDECIDABILITY.generator));

        declarations.put("_GenesisInstancer", Val.m_wrap(new BoxValue(BoxValue.Instancer.type, BoxValue.Instancer.type)));
        declarations.put("_AllUnpacker", Val.m_wrap(BoxValue.AllUnpacker.unpacker));

        Util.getAllInline(new File("fw/bootstrap.dfw"), declarations);

        declarations.remove("_AllUnpacker");
        declarations.remove("_GenesisInstancer");

        getOne(declarations, "unit");
        getOne(declarations, "Bool");
        getOne(declarations, "eq");
        getOne(declarations, "ProductType");

        return declarations;
    }
}
