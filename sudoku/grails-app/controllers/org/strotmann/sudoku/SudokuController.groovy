package org.strotmann.sudoku



import static org.springframework.http.HttpStatus.*
import grails.transaction.Transactional

@Transactional(readOnly = true)
class SudokuController {

    static allowedMethods = [save: "POST", update: "PUT", delete: "DELETE"]

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond Sudoku.list(params), model:[sudokuInstanceCount: Sudoku.count()]
    }

    def show(Sudoku sudokuInstance) {
        respond sudokuInstance
    }

    def create() {
        respond new Sudoku(params)
    }

    @Transactional
    def save(Sudoku sudokuInstance) {
        if (sudokuInstance == null) {
            notFound()
            return
        }

        if (sudokuInstance.hasErrors()) {
            respond sudokuInstance.errors, view:'create'
            return
        }

        sudokuInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'sudoku.label', default: 'Sudoku'), sudokuInstance.id])
                redirect sudokuInstance
            }
            '*' { respond sudokuInstance, [status: CREATED] }
        }
    }

    def edit(Sudoku sudokuInstance) {
        respond sudokuInstance
    }

    @Transactional
    def update(Sudoku sudokuInstance) {
        if (sudokuInstance == null) {
            notFound()
            return
        }

        if (sudokuInstance.hasErrors()) {
            respond sudokuInstance.errors, view:'edit'
            return
        }

        sudokuInstance.save flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'Sudoku.label', default: 'Sudoku'), sudokuInstance.id])
                redirect sudokuInstance
            }
            '*'{ respond sudokuInstance, [status: OK] }
        }
    }

    @Transactional
    def delete(Sudoku sudokuInstance) {

        if (sudokuInstance == null) {
            notFound()
            return
        }

        sudokuInstance.delete flush:true

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'Sudoku.label', default: 'Sudoku'), sudokuInstance.id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'sudoku.label', default: 'Sudoku'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
