package org.thermoweb.aoc.days;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

import org.thermoweb.aoc.Day;
import org.thermoweb.aoc.DaySolver;
import org.thermoweb.aoc.utils.InputParseUtils;

class Trial {
    int red = 0;
    int blue = 0;
    int green = 0;
    Trial(String s){
        String[] pulls = s.split(", ");
        for (String pull : pulls){
            String[] num_colour = pull.split(" ");
            switch (num_colour[1]){
                case "red": {
                    this.red = Integer.parseInt(num_colour[0]);
                    break;
                }
                case "green": {
                    this.green = Integer.parseInt(num_colour[0]);
                    break;
                }
                case "blue": {
                    this.blue = Integer.parseInt(num_colour[0]);
                    break;
                }
            }
        }
    }

    Trial (int r, int g, int b){
        this.red = r;
        this.green = g;
        this.blue = b;
    }

    int power(){
        return this.blue * this.red * this.green;
    }
    
    @Override
    public String toString() {
        return String.format("r:%d,g:%d,b:%d", this.red,this.green,this.blue);
    }
}

class Game {
    int id;
    Trial[] trials;

    public int getId() {
      return id;
    }

    Game(String s){
        String[] spl = s.split(": ");
        this.id = Integer.parseInt(spl[0].replace("Game ", ""));
        this.trials = Arrays.stream(spl[1].split("; ")).map(trialString->{
            return new Trial(trialString);
        }).collect(Collectors.toList()).toArray(Trial[]::new);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        for (Trial t : this.trials){
            s.append(t.toString());
            s.append(" ");
        }
        return String.format("Id %s: %s\n", this.id,s.toString());
    }

    boolean validate(int red, int green, int blue){
        for (Trial t : this.trials){
            if (t.red > red || t.blue > blue || t.green > green){
                return false;
            }
        }
        return true;
    }

    Trial minimum() {
        int minRed = 0;
        int minGreen = 0;
        int minBlue = 0;
        for (Trial t : this.trials){
            if (t.red > minRed){
                minRed = t.red;
            }
            if (t.green > minGreen){
                minGreen = t.green;
            }
            if (t.blue > minBlue){
                minBlue = t.blue;
            }
        }
        return new Trial(minRed,minGreen,minBlue);
    }
}



@DaySolver(2)
public class Day2 implements Day {
    @Override
    public Optional<BigInteger> partOne(String input) {
        var result = InputParseUtils.lines(input).map(Game::new).filter(game->game.validate(12, 13, 14))
        .collect(Collectors.summingInt(Game::getId));
        return Optional.of(BigInteger.valueOf(result));
    }

    @Override
    public Optional<BigInteger> partTwo(String input) {
        var result = InputParseUtils.lines(input)
            .map(Game::new)
            .map(Game::minimum)
            .map(Trial::power)
            .collect(Collectors.summingInt(Integer::valueOf));
        return Optional.of(BigInteger.valueOf(result));
    }
}
