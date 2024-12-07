package org.thermoweb.aoc.days;

import java.lang.Override;
import java.lang.String;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.InputParseUtils;
import org.thermoweb.aoc.utils.Timer;
import org.thermoweb.aoc.utils.TreeNode;

enum Operator {
    
    MULT("*",(Long a, Long b)->{return a.longValue()  *b.longValue();}),
    ADD("+",(Long a, Long b)->{return a.longValue() + b.longValue();}),
    CONCAT("||",(Long a, Long b)->{return Long.parseLong(a.toString()+b.toString());});
    
    String character;
    BiFunction<Long,Long,Long> func;

    Operator(String c, BiFunction<Long,Long,Long> func){
        this.character = c;
        this.func = func;
    }
    
    long operate(long a, long b){
        return this.func.apply(a, b);
    }
    @Override
    public String toString() {
        return this.character;
    }
}

record Node(Operator operator, long value,long accumulator){
    @Override
    public final String toString() {
        if (operator == null){
            return "%s".formatted(value);
        }
        return "%s %s (%s)".formatted(operator.toString(),value, accumulator);
    }
}

@DaySolver(7)
public class Day7 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        Timer t = new Timer();
        return t.runAndStop(()->run(input,Operator.ADD,Operator.MULT));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        Timer t = new Timer();
        return t.runAndStop(()->run(input, Operator.values()));
    }

    Optional<BigInteger> run(String input,Operator ...operators){
        AtomicLong valid = new AtomicLong(0);
        InputParseUtils.lines(input).forEach((line)->{
            String[] final_input = line.split(": ");
            long finalVal = Long.parseLong(final_input[0]);
            List<Long> vals = Arrays.stream(final_input[1].split("\\s+")).mapToLong(Long::parseLong).boxed().collect(Collectors.toList());
            TreeNode<Node> rootNode = null;
            for (Long val : vals){
                if (rootNode == null){
                    rootNode = new TreeNode<Node>(new Node(null,val,val));
                    continue;
                }
                Node[] children = Arrays.stream(operators).map((o)->new Node(o,val,0)).collect(Collectors.toList()).toArray(Node[]::new);
                rootNode.addChildToAllLeaves((node)->node.getData().accumulator()<=finalVal,
                        children
                    );
                rootNode.iterator().forEachRemaining((node)->{
                    if (node.getData().accumulator() != 0){
                        return;
                    }
                    Node data = node.getData();
                    Node parentData = node.getParent().getData();
                    node.setData(new Node(data.operator(),data.value(),data.operator().operate(parentData.accumulator(),data.value())));
                    
                });
            }
            var iterator = rootNode.iterator();
            boolean isValid = false;
            while (!isValid && iterator.hasNext()){
                var t = iterator.next();
                if (!t.isLeaf()){
                    continue;
                }
                if (t.getData().accumulator() == finalVal){
                    isValid = true;
                    valid.addAndGet(finalVal);
                }
            }
        });


        return Optional.of(BigInteger.valueOf(valid.get()));
    }
}
