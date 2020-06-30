/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.qenherkhopeshef.finitestate.lazy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * The result of a search. Contains the positions of the various parts matched
 * by the search.
 *
 * <p>
 * A <code>MatchResult</code> is usually created by a
 * <code>RegularExtractor</code>.
 * <p>
 * Value type: two matchresults are equals iff they contain the same positions.
 *
 * <p>
 * Positions in a match result are supposed to be increasing (not strictly). No
 * provision is made to test this, however.
 * </p>
 *
 * @author rosmord
 */
public class MatchResult implements Iterable<Integer> {

    private final List<Integer> positions;

    /**
     * Creates a match results from positions.
     *
     * @param positions a sequence of increasing positions.
     */
    public MatchResult(Integer... positions) {
        this.positions = Arrays.asList(positions);
    }

    /**
     * Creates a match results from a list of positions.
     *
     * @param positions a sequence of increasing positions.
     */
    public MatchResult(List<Integer> positions) {
        this.positions = new ArrayList<>(positions);
    }

    public int getNumberOfParts() {
        return positions.size() - 1;
    }

    /**
     * Returns the position for the matched end of part partNumber
     *
     * @param partNumber the index of the part (starts at 0).
     * @return
     */
    public int getEndOfPart(int partNumber) {
        return positions.get(partNumber + 1);
    }

    /**
     * Returns the position for the matched start of part partNumber
     *
     * @param partNumber the index of the part (starts at 0).
     * @return
     */
    public int getStartOfPart(int partNumber) {
        return positions.get(partNumber);
    }

    /**
     * The first position in this match.
     *
     * @return
     */
    public int getFirstPosition() {
        return positions.get(0);
    }

    public int getLastPosition() {
        return positions.get(positions.size() - 1);
    }

    public int getMatchLength() {
        return getLastPosition() - getFirstPosition();
    }

    /**
     * Returns a simplified version of this match result, without intermediary
     * parts. Mostly to ease testing at the time.
     *
     * @return
     */
    public MatchResult getAsOneMatch() {
        return new MatchResult(getFirstPosition(), getLastPosition());
    }

    /**
     * extract the match from a string.
     *
     * @param input the string which was originally searched.
     * @return the full text of the part matched in input.
     */
    public String extractFullMatch(String input) {
        return input.substring(getFirstPosition(), getLastPosition());
    }

    /**
     * extract the matched text for a specific part of this match from a string.
     *
     * @param input the string which was originally searched.
     * @param partIndex the index of the specific part we are interested in.
     * @return the full text of the part matched in input.
     */
    public String extractPart(String input, int partIndex) {
        return input.substring(getStartOfPart(partIndex), getEndOfPart(partIndex));
    }

    @Override
    public Iterator<Integer> iterator() {
        return positions.iterator();
    }

    @Override
    public int hashCode() {
        return positions.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final MatchResult other = (MatchResult) obj;
        return this.positions.equals(other.positions);
    }

    public Stream<Integer> stream() {
        return positions.stream();
    }

    @Override
    public String toString() {
        return positions.toString();
    }

}
