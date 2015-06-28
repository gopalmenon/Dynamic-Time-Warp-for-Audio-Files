package menon.cs6890.assignment6;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Stores the DTW table elements that can be accessed
 * through row and column coordinates
 */
public class DynamicTimeWarpEditDistanceTable {
	
	private ArrayList<TableElement> table;
	private double[] target;
	private double[] source;
	
	/**
	 * Constructor takes the target and source strings
	 * 
	 * @param target
	 * @param source
	 */
	public DynamicTimeWarpEditDistanceTable(double[] target, double[] source) {
				
		if (target == null || source == null) {
			throw new IllegalArgumentException("Null parameters not allowed.");
		}
	
		this.target = target;
		this.source = source;
		
		this.table = new ArrayList<TableElement>(this.target.length * this.source.length);
		for (int sourceElementIndex = 0; sourceElementIndex < this.source.length; ++sourceElementIndex) {
			for (int targetStringIndex = 0; targetStringIndex < this.target.length; ++targetStringIndex) {
				this.table.add(new TableElement(null, null, null, 0, sourceElementIndex, targetStringIndex));
			}
		}
	}
	
	/**
	 * Map the source element to the target element and update the DTW table
	 */
	public void warpSourceToTarget() {
		
		//Find the cost of warping the source to the first element in the target.
		TableElement currentTableElement = null, previousElement = null;
		double alignmentCost = 0.0;
		for (int sourceElementIndex = 0; sourceElementIndex < this.source.length; ++sourceElementIndex) {
			currentTableElement = getElement(0, sourceElementIndex);
			assert currentTableElement != null;
			if (this.source[sourceElementIndex] == this.target[0]) {
				if (previousElement == null) {
					currentTableElement.setAlignmentCost(0);
				} else {
					currentTableElement.setAlignmentCost(previousElement.getAlignmentCost());
				}
			} else {
				alignmentCost = Math.abs(this.source[sourceElementIndex] - this.target[0]);
				if (previousElement == null) {
					currentTableElement.setAlignmentCost(alignmentCost);
				} else {
					currentTableElement.setAlignmentCost(previousElement.getAlignmentCost() + alignmentCost);
				}
			}
			currentTableElement.setMinimumDistanceFromSameSource(previousElement);
			previousElement = currentTableElement;
			setElement(0, sourceElementIndex, currentTableElement);
		}
		
		//Find the cost of warping the first character in the source to the target.
		previousElement = null;
		for (int targetElementIndex = 0; targetElementIndex < this.target.length; ++targetElementIndex) {
			currentTableElement = getElement(targetElementIndex, 0);
			assert currentTableElement != null;
			if (this.target[targetElementIndex] == this.source[0]) {
				if (previousElement == null) {
					currentTableElement.setAlignmentCost(0);
				} else {
					currentTableElement.setAlignmentCost(previousElement.getAlignmentCost());
				}
			} else {
				alignmentCost = Math.abs(this.target[targetElementIndex] - this.source[0]);
				if (previousElement == null) {
					currentTableElement.setAlignmentCost(alignmentCost);
				} else {
					currentTableElement.setAlignmentCost(previousElement.getAlignmentCost() + alignmentCost);
				}
			}
			currentTableElement.setMinimumDistanceFromSameTarget(previousElement);
			previousElement = currentTableElement;
			setElement(targetElementIndex, 0, currentTableElement);
		}		
		
		double distanceFromPreviousInSource = 0, distanceFromPreviousInTarget = 0, distanceFromPreviousInSourceAndTarget = 0;
		TableElement previousInSourceElement = null, previousInTargetElement = null, previousInSourceAndTargetElement = null;
		
		//Loop through the elements in the table
		for (int sourceElementIndex = 1; sourceElementIndex < this.source.length; ++sourceElementIndex) {
			for (int targetElementIndex = 1; targetElementIndex < this.target.length; ++targetElementIndex) {
				
				currentTableElement = getElement(targetElementIndex, sourceElementIndex);
				assert currentTableElement != null;
				
				//Get distance from previous element in source string
				previousInSourceElement = getNeighborAboveOnPreviousRow(targetElementIndex, sourceElementIndex);
				assert previousInSourceElement != null;
				if (this.source[sourceElementIndex] == this.target[targetElementIndex]) {
					distanceFromPreviousInSource = previousInSourceElement.getAlignmentCost();
				} else {
					alignmentCost = Math.abs(this.source[sourceElementIndex] - this.target[targetElementIndex]);
					distanceFromPreviousInSource = previousInSourceElement.getAlignmentCost() + alignmentCost;
				}
				
				//Get distance from previous element in target string 
				previousInTargetElement = getPreviousNeighborOnSameRow(targetElementIndex, sourceElementIndex);
				assert previousInTargetElement != null;
				if (this.source[sourceElementIndex] == this.target[targetElementIndex]) {
					distanceFromPreviousInTarget = previousInTargetElement.getAlignmentCost();
				} else {
					alignmentCost = Math.abs(this.source[sourceElementIndex] - this.target[targetElementIndex]);
					distanceFromPreviousInTarget = previousInTargetElement.getAlignmentCost() + alignmentCost;
				}
				//Get distance from previous elements in source and target strings 
				previousInSourceAndTargetElement = getDiagonalNeighborOnPreviousRow(targetElementIndex, sourceElementIndex);
				assert previousInSourceAndTargetElement != null;
				if (this.source[sourceElementIndex] == this.target[targetElementIndex]) {
					distanceFromPreviousInSourceAndTarget = previousInSourceAndTargetElement.getAlignmentCost();
				} else {
					alignmentCost = Math.abs(this.source[sourceElementIndex] - this.target[targetElementIndex]);
					distanceFromPreviousInSourceAndTarget = previousInSourceAndTargetElement.getAlignmentCost() + 1;
				}
				
				currentTableElement.setAlignmentCost(Math.min(Math.min(distanceFromPreviousInTarget, distanceFromPreviousInSource), distanceFromPreviousInSourceAndTarget));
				currentTableElement.setMinimumDistanceFromSameSource(previousInSourceElement);
				currentTableElement.setMinimumDistanceFromSameTarget(previousInTargetElement);
				currentTableElement.setMinimumDistanceFromDiagonal(previousInSourceAndTargetElement);
				
			}
		}
		
	}
	
	/**
	 * Get element by column and row number
	 * 
	 * @param column
	 * @param row
	 * @return a table element
	 */
	TableElement getElement(int column, int row) {
		
		//Check if valid values have been passed in
		if (!isValidCoordinate(column, row)) {
			return null;
		}
		
		int currentElementOffset = getTableOffset(column, row);
		
		TableElement element = this.table.get(currentElementOffset);

		return element;
		
	}
	
	/**
	 * Put an element into the specified row and column
	 * 
	 * @param column
	 * @param row
	 * @param element
	 * @return true if the element is inserted
	 */
	private boolean setElement(int column, int row, TableElement element) {
		
		//Check if valid coordinate values have been passed in
		if (!isValidCoordinate(column, row)) {
			return false;
		}
		
		int currentElementOffset = getTableOffset(column, row);
		
		this.table.set(currentElementOffset, element);
		
		return true;
	}

	/**
	 * Get the previous neighbor on the same row
	 * 
	 * @param column
	 * @param row
	 * @return a table element
	 */
	private TableElement getPreviousNeighborOnSameRow(int column, int row) {
		
		//Check if valid values have been passed in
		if (!isValidCoordinate(column, row)) {
			return null;
		}

		//Check if current point is on the border
		if (column == 0) {
			return null;
		}
		
		int currentElementOffset = getTableOffset(column, row);
		
		TableElement neighbor = this.table.get(currentElementOffset - 1);

		return neighbor;
		
	}
	
	/**
	 * Get the neighbor above on the previous row 
	 * 
	 * @param column
	 * @param row
	 * @return a table element
	 */
	private TableElement getNeighborAboveOnPreviousRow(int column, int row) {
		
		//Check if valid values have been passed in
		if (!isValidCoordinate(column, row)) {
			return null;
		}
		
		//Check if current point is on the border
		if (row == 0) {
			return null;
		}
		
		int currentElementOffset = getTableOffset(column, row);
		
		TableElement neighbor = this.table.get(currentElementOffset - this.target.length);

		return neighbor;		

	}
	
	/**
	 * Get the diagonal neighbor or previous row
	 * 
	 * @param column
	 * @param row
	 * @return a table element
	 */
	private TableElement getDiagonalNeighborOnPreviousRow(int column, int row) {
		
		//Check if valid values have been passed in
		if (!isValidCoordinate(column, row)) {
			return null;
		}
		
		//Check if current point is on the border
		if (column == 0 || row == 0) {
			return null;
		}
		
		int currentElementOffset = getTableOffset(column, row);
		
		TableElement neighbor = table.get(currentElementOffset - this.target.length - 1);
		
		return neighbor;
		
	}
	
	/**
	 * Check if the coordinates passed in are valid
	 * 
	 * @param column
	 * @param row
	 * @return true if coordinates are valid, else return false
	 */
	private boolean isValidCoordinate(int column, int row) {
		
		if (column > this.target.length - 1 || row > this.source.length - 1 || column < 0 || row < 0) {
			return false;
		} else {
			return true;
		}
				
	}
	
	/**
	 * Return table offset in array list given x and y coordinates
	 * 
	 * @param column
	 * @param row
	 * @return table offset
	 */
	private int getTableOffset(int column, int row) {
		
		return row * this.target.length + column;
		
	}
	
	/**
	 * @return a protected copy of the table
	 */
	public List<TableElement> getTable() {
		return Collections.unmodifiableList(this.table);
	}
	
	/**
	 * @return the source string
	 */
	public double[] getSource() {
		return this.source;
	}
	
	/**
	 * @return the target string
	 */
	public double[] getTarget() {
		return this.target;
	}
	
	/**
	 * @return the size of the source string plus one (for the null element at the beginning of the string)
	 */
	public int getSourceSize() {
		return this.source.length;
	}
	
	/**
	 * @return the size of the target string plus one (for the null element at the beginning of the string)
	 */
	public int getTargetSize() {
		return this.target.length;
	}
	
	/**
	 * @return the substitution cost if the source string is to be replaced by the target string
	 */
	public int getFullStringSubstitutionCost() {
		
		int fullStringSubstitutionCost = 0;
		
		//Add the cost of deleting each element in source 
		fullStringSubstitutionCost += this.source.length;
		
		//Add the cost of inserting each element from the target into the source 
		fullStringSubstitutionCost += this.target.length;
		
		return fullStringSubstitutionCost;

	}
	
	public double getWarpDistance() {
		return getElement(getTargetSize() - 1, getSourceSize() - 1).getAlignmentCost();
	}

}