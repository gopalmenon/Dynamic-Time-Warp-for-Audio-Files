package menon.cs6890.assignment6;

public class TableElement {
	
	private TableElement minimumDistanceFromSameSource;
	private TableElement minimumDistanceFromSameTarget;
	private TableElement minimumDistanceFromDiagonal;
	private double alignmentCost;
	private int sourceElementOffset;
	private int targetElementOffset;
	
	/**
	 * Constructor
	 * 
	 * @param minimumDistanceFromSameSource
	 * @param minimumDistanceFromSameTarget
	 * @param minimumDistanceFromDiagonal
	 * @param alignmentCost
	 * @param sourceElementOffset
	 * @param targetElementOffset
	 */
	public TableElement(TableElement minimumDistanceFromSameSource,
			            TableElement minimumDistanceFromSameTarget,
			            TableElement minimumDistanceFromDiagonal,
			            double alignmentCost,
			            int sourceElementOffset,
			            int targetElementOffset) {
		
		if (alignmentCost < 0 || sourceElementOffset < 0 || targetElementOffset < 0) {
			throw new IllegalArgumentException("Negative values not allowed");
		}
		
		this.minimumDistanceFromSameSource = minimumDistanceFromSameSource;
		this.minimumDistanceFromSameTarget = minimumDistanceFromSameTarget;
		this.minimumDistanceFromDiagonal = minimumDistanceFromDiagonal;
		this.alignmentCost = alignmentCost;
		this.sourceElementOffset = sourceElementOffset;
		this.targetElementOffset = targetElementOffset;
	}
	
	
	/**
	 * @return the back trace element - the element with the least cost
	 */
	public TableElement getBackTraceElement() {
		
		TableElement returnValue = null;
		
		TableElement backTraceElement1 = this.minimumDistanceFromSameSource != null ? this.minimumDistanceFromSameSource : null;
		TableElement backTraceElement2 = this.minimumDistanceFromSameTarget != null ? this.minimumDistanceFromSameTarget : null;
		TableElement backTraceElement3 = this.minimumDistanceFromDiagonal != null ? this.minimumDistanceFromDiagonal : null;
		
		if (backTraceElement1 != null) {
			returnValue = backTraceElement1;
		}
		
		if (backTraceElement2 != null) {
			if (returnValue != null) {
				if (backTraceElement2.getAlignmentCost() < returnValue.getAlignmentCost()) {
					returnValue = backTraceElement2;
				}
			} else {
				returnValue = backTraceElement2;
			}
		}
		
		if (backTraceElement3 != null) {
			if (returnValue != null) {
				if (backTraceElement3.getAlignmentCost() < returnValue.getAlignmentCost()) {
					returnValue = backTraceElement3;
				}
			} else {
				returnValue = backTraceElement3;
			}
		}
		
		return returnValue;
				
	}
	

	public TableElement getMinimumDistanceFromSameSource() {
		return minimumDistanceFromSameSource;
	}


	public void setMinimumDistanceFromSameSource(
			TableElement minimumDistanceFromSameSource) {
		this.minimumDistanceFromSameSource = minimumDistanceFromSameSource;
	}


	public TableElement getMinimumDistanceFromSameTarget() {
		return minimumDistanceFromSameTarget;
	}


	public void setMinimumDistanceFromSameTarget(
			TableElement minimumDistanceFromSameTarget) {
		this.minimumDistanceFromSameTarget = minimumDistanceFromSameTarget;
	}


	public TableElement getMinimumDistanceFromDiagonal() {
		return minimumDistanceFromDiagonal;
	}


	public void setMinimumDistanceFromDiagonal(
			TableElement minimumDistanceFromDiagonal) {
		this.minimumDistanceFromDiagonal = minimumDistanceFromDiagonal;
	}
	
	public double getAlignmentCost() {
		return alignmentCost;
	}
	public void setAlignmentCost(double alignmentCost) {
		this.alignmentCost = alignmentCost;
	}


	public int getSourceElementOffset() {
		return sourceElementOffset;
	}


	public void setSourceElementOffset(int sourceElementOffset) {
		this.sourceElementOffset = sourceElementOffset;
	}


	public int getTargetElementOffset() {
		return targetElementOffset;
	}


	public void setTargetElementOffset(int targetElementOffset) {
		this.targetElementOffset = targetElementOffset;
	}

}
