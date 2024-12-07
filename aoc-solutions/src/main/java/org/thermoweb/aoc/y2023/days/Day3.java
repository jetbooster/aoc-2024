package org.thermoweb.aoc.y2023.days;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.Element;
import org.thermoweb.aoc.utils.Grid;


@DaySolver(3)
public class Day3 implements Day {

    int getNumberFromElement(Grid<Element> grid, Element e) {
        int left = 0;
        boolean leftDone = false;
        int right = 0;
        boolean rightDone = false;
        List<Element> elems = new ArrayList<>();
        elems.add(e);
        while (!leftDone){
            left -= 1;
            var nextElem = grid.neighbour(e, left, 0);
            if (nextElem != null && nextElem.getContent().matches("\\d")){
                elems.add(nextElem);
            } else {
                leftDone = true;
            }
        }
        Collections.reverse(elems);
        while (!rightDone){
            right += 1;
            var nextElem = grid.neighbour(e, right, 0);
            if (nextElem != null && nextElem.getContent().matches("\\d")){
                elems.add(nextElem);
            } else {
                rightDone = true;
            }
        }
        var str = elems.stream().map(Element::getContent).collect(Collectors.joining());
        System.out.println(str);
        return Integer.parseInt(str);
    }
    
    List<Integer> getAdjacentNumbers(Grid<Element> grid, Element e){
        return new ArrayList<>((grid.neighbours(e,true)
            .filter(elem->elem.getContent().matches("\\d"))
            .map((elem)->getNumberFromElement(grid, elem))
            .collect(Collectors.toSet())));
    }

    @Override
    public Optional<BigInteger> partOne(String input) {
        return Optional.empty();
    }
    
    @Override
    public Optional<BigInteger> partTwo(String input) {
        try{
            var grid = new Grid<Element>(input,Element.class);
            var results = grid.filter("*")
                .map((elem)->getAdjacentNumbers(grid, elem))
                .filter((list)->list.size()==2)
                .map((list)->list.get(0)*list.get(1))
                .collect(Collectors.summingInt(Integer::valueOf));
            System.out.println(results);
            return Optional.empty();

        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
