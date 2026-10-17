package org.fw.direct3.val;

interface Value {
    Value call(Value value);

    boolean isInNormalForm();

    /**
     * Perform beta-reduction.
     * @return reduced value
     */
    Value reduce();

    Value getType();
}

