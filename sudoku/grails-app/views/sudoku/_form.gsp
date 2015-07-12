<%@ page import="org.strotmann.sudoku.Sudoku" %>



<div class="fieldcontain ${hasErrors(bean: sudokuInstance, field: 'eSudoku', 'error')} required">
	<label for="eSudoku">
		<g:message code="sudoku.eSudoku.label" default="E Sudoku" />
		<span class="required-indicator">*</span>
	</label>
	<g:textField name="eSudoku" required="" value="${sudokuInstance?.eSudoku}"/>

</div>

