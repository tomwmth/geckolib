package com.eliotlash.mclib.math;

import com.eliotlash.mclib.math.functions.Function;
import com.eliotlash.mclib.math.functions.classic.*;
import com.eliotlash.mclib.math.functions.limit.Clamp;
import com.eliotlash.mclib.math.functions.limit.Max;
import com.eliotlash.mclib.math.functions.limit.Min;
import com.eliotlash.mclib.math.functions.rounding.Ceil;
import com.eliotlash.mclib.math.functions.rounding.Floor;
import com.eliotlash.mclib.math.functions.rounding.Round;
import com.eliotlash.mclib.math.functions.rounding.Trunc;
import com.eliotlash.mclib.math.functions.utility.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MathBuilder {
    public Map<String, Variable> variables = new HashMap<String, Variable>();
    public Map<String, Class<? extends Function>> functions = new HashMap<String, Class<? extends Function>>();

    public MathBuilder() {
        this.register(new Variable("PI", Math.PI));
        this.register(new Variable("E", Math.E));
        this.functions.put("floor", Floor.class);
        this.functions.put("round", Round.class);
        this.functions.put("ceil", Ceil.class);
        this.functions.put("trunc", Trunc.class);
        this.functions.put("clamp", Clamp.class);
        this.functions.put("max", Max.class);
        this.functions.put("min", Min.class);
        this.functions.put("abs", Abs.class);
        this.functions.put("acos", ACos.class);
        this.functions.put("asin", ASin.class);
        this.functions.put("atan", ATan.class);
        this.functions.put("atan2", ATan2.class);
        this.functions.put("cos", Cos.class);
        this.functions.put("sin", Sin.class);
        this.functions.put("exp", Exp.class);
        this.functions.put("ln", Ln.class);
        this.functions.put("sqrt", Sqrt.class);
        this.functions.put("mod", Mod.class);
        this.functions.put("pow", Pow.class);
        this.functions.put("lerp", Lerp.class);
        this.functions.put("lerprotate", LerpRotate.class);
        this.functions.put("hermite_blend", HermiteBlend.class);
        this.functions.put("die_roll", DieRoll.class);
        this.functions.put("die_roll_integer", DieRollInteger.class);
        this.functions.put("random", Random.class);
        this.functions.put("random_integer", RandomInteger.class);
    }

    public void register(Variable variable) {
        this.variables.put(variable.getName(), variable);
    }

    public IValue parse(String expression) throws Exception {
        return this.parseSymbols(this.breakdownChars(this.breakdown(expression)));
    }

    public String[] breakdown(String expression) throws Exception {
        if (!expression.matches("^[\\w\\d\\s_+-/*%^&|<>=!?:.,()]+$")) {
            throw new Exception("Given expression '" + expression + "' contains illegal characters!");
        }
        expression = expression.replaceAll("\\s+", "");
        String[] chars = expression.split("(?!^)");
        int left = 0;
        int right = 0;
        for (String s : chars) {
            if (s.equals("(")) {
                ++left;
                continue;
            }
            if (!s.equals(")")) continue;
            ++right;
        }
        if (left != right) {
            throw new Exception("Given expression '" + expression + "' has more uneven amount of parenthesis, there are " + left + " open and " + right + " closed!");
        }
        return chars;
    }

    public List<Object> breakdownChars(String[] chars) {
        ArrayList<Object> symbols = new ArrayList<>();
        StringBuilder buffer = new StringBuilder();
        int len = chars.length;
        block0:
        for (int i = 0; i < len; ++i) {
            boolean longOperator;
            String s = chars[i];
            boolean bl = longOperator = i > 0 && this.isOperator(chars[i - 1] + s);
            if (this.isOperator(s) || longOperator || s.equals(",")) {
                if (s.equals("-")) {
                    boolean isOperatorBehind;
                    int size = symbols.size();
                    boolean isFirst = size == 0 && (buffer.length() == 0);
                    boolean bl2 = isOperatorBehind = size > 0 && (this.isOperator(symbols.get(size - 1)) || symbols.get(size - 1).equals(",")) && (buffer.length() == 0);
                    if (isFirst || isOperatorBehind) {
                        buffer.append(s);
                        continue;
                    }
                }
                if (longOperator) {
                    s = chars[i - 1] + s;
                    buffer = new StringBuilder(buffer.substring(0, buffer.length() - 1));
                }
                if (buffer.length() > 0) {
                    symbols.add(buffer.toString());
                    buffer = new StringBuilder();
                }
                symbols.add(s);
                continue;
            }
            if (s.equals("(")) {
                if (buffer.length() > 0) {
                    symbols.add(buffer.toString());
                    buffer = new StringBuilder();
                }
                int counter = 1;
                for (int j = i + 1; j < len; ++j) {
                    String c = chars[j];
                    if (c.equals("(")) {
                        ++counter;
                    } else if (c.equals(")")) {
                        --counter;
                    }
                    if (counter == 0) {
                        symbols.add(this.breakdownChars(buffer.toString().split("(?!^)")));
                        i = j;
                        buffer = new StringBuilder();
                        continue block0;
                    }
                    buffer.append(c);
                }
                continue;
            }
            buffer.append(s);
        }
        if (buffer.length() > 0) {
            symbols.add(buffer.toString());
        }
        return symbols;
    }

    public IValue parseSymbols(List<Object> symbols) throws Exception {
        int lastOp;
        IValue ternary = this.tryTernary(symbols);
        if (ternary != null) {
            return ternary;
        }
        int size = symbols.size();
        if (size == 1) {
            return this.valueFromObject(symbols.get(0));
        }
        if (size == 2) {
            Object first = symbols.get(0);
            Object second = symbols.get(1);
            if ((this.isVariable(first) || first.equals("-")) && second instanceof List) {
                return this.createFunction((String) first, (List) second);
            }
        }
        int op = lastOp = this.seekLastOperator(symbols);
        while (op != -1) {
            int leftOp = this.seekLastOperator(symbols, op - 1);
            if (leftOp != -1) {
                Operation left = this.operationForOperator((String) symbols.get(leftOp));
                Operation right = this.operationForOperator((String) symbols.get(op));
                if (right.value > left.value) {
                    IValue leftValue = this.parseSymbols(symbols.subList(0, leftOp));
                    IValue rightValue = this.parseSymbols(symbols.subList(leftOp + 1, size));
                    return new Operator(left, leftValue, rightValue);
                }
                if (left.value > right.value) {
                    Operation initial = this.operationForOperator((String) symbols.get(lastOp));
                    if (initial.value < left.value) {
                        IValue leftValue = this.parseSymbols(symbols.subList(0, lastOp));
                        IValue rightValue = this.parseSymbols(symbols.subList(lastOp + 1, size));
                        return new Operator(initial, leftValue, rightValue);
                    }
                    IValue leftValue = this.parseSymbols(symbols.subList(0, op));
                    IValue rightValue = this.parseSymbols(symbols.subList(op + 1, size));
                    return new Operator(right, leftValue, rightValue);
                }
            }
            op = leftOp;
        }
        Operation operation = this.operationForOperator((String) symbols.get(lastOp));
        return new Operator(operation, this.parseSymbols(symbols.subList(0, lastOp)), this.parseSymbols(symbols.subList(lastOp + 1, size)));
    }

    protected int seekLastOperator(List<Object> symbols) {
        return this.seekLastOperator(symbols, symbols.size() - 1);
    }

    protected int seekLastOperator(List<Object> symbols, int offset) {
        for (int i = offset; i >= 0; --i) {
            Object o = symbols.get(i);
            if (!this.isOperator(o)) continue;
            return i;
        }
        return -1;
    }

    protected int seekFirstOperator(List<Object> symbols) {
        return this.seekFirstOperator(symbols, 0);
    }

    protected int seekFirstOperator(List<Object> symbols, int offset) {
        int size = symbols.size();
        for (int i = offset; i < size; ++i) {
            Object o = symbols.get(i);
            if (!this.isOperator(o)) continue;
            return i;
        }
        return -1;
    }

    protected IValue tryTernary(List<Object> symbols) throws Exception {
        int question = -1;
        int questions = 0;
        int colon = -1;
        int colons = 0;
        int size = symbols.size();
        for (int i = 0; i < size; ++i) {
            Object object = symbols.get(i);
            if (!(object instanceof String)) continue;
            if (object.equals("?")) {
                if (question == -1) {
                    question = i;
                }
                ++questions;
                continue;
            }
            if (!object.equals(":")) continue;
            if (colons + 1 == questions && colon == -1) {
                colon = i;
            }
            ++colons;
        }
        if (questions == colons && question > 0 && question + 1 < colon && colon < size - 1) {
            return new Ternary(this.parseSymbols(symbols.subList(0, question)), this.parseSymbols(symbols.subList(question + 1, colon)), this.parseSymbols(symbols.subList(colon + 1, size)));
        }
        return null;
    }

    protected IValue createFunction(String first, List<Object> args) throws Exception {
        if (first.equals("!")) {
            return new Negate(this.parseSymbols(args));
        }
        if (first.startsWith("!") && first.length() > 1) {
            return new Negate(this.createFunction(first.substring(1), args));
        }
        if (first.equals("-")) {
            return new Negative(new Group(this.parseSymbols(args)));
        }
        if (first.startsWith("-") && first.length() > 1) {
            return new Negative(this.createFunction(first.substring(1), args));
        }
        if (!this.functions.containsKey(first)) {
            throw new Exception("Function '" + first + "' couldn't be found!");
        }
        ArrayList<IValue> values = new ArrayList<IValue>();
        ArrayList<Object> buffer = new ArrayList<Object>();
        for (Object o : args) {
            if (o.equals(",")) {
                values.add(this.parseSymbols(buffer));
                buffer.clear();
                continue;
            }
            buffer.add(o);
        }
        if (!buffer.isEmpty()) {
            values.add(this.parseSymbols(buffer));
        }
        Class<? extends Function> function = this.functions.get(first);
        Constructor<? extends Function> ctor = function.getConstructor(IValue[].class, String.class);
        return ctor.newInstance(values.toArray(new IValue[0]), first);
    }

    public IValue valueFromObject(Object object) throws Exception {
        if (object instanceof String) {
            String symbol = (String) object;
            if (symbol.startsWith("!")) {
                return new Negate(this.valueFromObject(symbol.substring(1)));
            }
            if (this.isDecimal(symbol)) {
                return new Constant(Double.parseDouble(symbol));
            }
            if (this.isVariable(symbol)) {
                if (symbol.startsWith("-")) {
                    Variable value = this.getVariable(symbol = symbol.substring(1));
                    if (value != null) {
                        return new Negative(value);
                    }
                } else {
                    Variable value = this.getVariable(symbol);
                    if (value != null) {
                        return value;
                    }
                }
            }
        } else if (object instanceof List) {
            return new Group(this.parseSymbols((List) object));
        }
        throw new Exception("Given object couldn't be converted to value! " + object);
    }

    protected Variable getVariable(String name) {
        return this.variables.get(name);
    }

    protected Operation operationForOperator(String op) throws Exception {
        for (Operation operation : Operation.values()) {
            if (!operation.sign.equals(op)) continue;
            return operation;
        }
        throw new Exception("There is no such operator '" + op + "'!");
    }

    protected boolean isVariable(Object o) {
        return o instanceof String && !this.isDecimal((String) o) && !this.isOperator((String) o);
    }

    protected boolean isOperator(Object o) {
        return o instanceof String && this.isOperator((String) o);
    }

    protected boolean isOperator(String s) {
        return Operation.OPERATORS.contains(s) || s.equals("?") || s.equals(":");
    }

    protected boolean isDecimal(String s) {
        return s.matches("^-?\\d+(\\.\\d+)?$");
    }
}
