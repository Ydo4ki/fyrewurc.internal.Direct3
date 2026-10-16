package org.fw.direct3.parser;

import com.ydo4ki.esast.EsastException;
import com.ydo4ki.esast.Token;
import org.fw.direct3.val.Val;

import java.util.function.Function;

class IdentifierExpression extends Expression {
    final Token symbol;

    IdentifierExpression(Token symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return symbol.getText();
    }

    @Override
    public Val compile(Function<String, Val> declarations) {
        String name = symbol.getText();
        if (name.charAt(0) == '\'' && name.charAt(name.length() - 1) == '\'')
            return Val.symbol(name.substring(1, name.length() - 1));

        Val val = declarations.apply(name);
        if (val == null)
            throw new EsastException(symbol.getLocation(), "Cannot find symbol: " + symbol.getText());
        return val;
    }
}
