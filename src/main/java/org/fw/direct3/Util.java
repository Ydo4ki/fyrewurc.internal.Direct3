package org.fw.direct3;

import com.ydo4ki.esast.BracketsTypes;
import com.ydo4ki.esast.EsastException;
import com.ydo4ki.esast.TokenOutput;
import org.fw.direct3.parser.Parser;
import org.fw.direct3.val.Val;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

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
}
