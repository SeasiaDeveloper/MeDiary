
package com.kevalpatel.passcodeview.patternCells;
public final class PatternPoint {

    private final int mRow;
    private final int mColumn;

    public PatternPoint(final int row, final int column) {
        mRow = row;
        mColumn = column;

        if (row < 0 || column < 0) throw new RuntimeException("Invalid mRow or mColumn number.");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PatternPoint patternPoint = (PatternPoint) o;
        return mRow == patternPoint.mRow && mColumn == patternPoint.mColumn;
    }

    @Override
    public int hashCode() {
        int result = mRow;
        result = 31 * result + mColumn;
        return result;
    }

    @Override
    public String toString() {
        return "PatternPoint(" + mRow + ", " + mColumn + ")";
    }

}
