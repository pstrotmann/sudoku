package org.strotmann.sudoku

class Sudoku {
	
	String eSudoku
	
	static constraints = {
		eSudoku validator: {String s ->
			if (s.find{!(('0'..'9')+(' ')).contains(it)})
			 return ['ungültige Eingabe']
		}
	}
	
	List getSudo () {
		//81-stelliger String auf 9x9 Liste xxx
		List l = []
		
		for (int i = 0; i < 9; i++) {
			String z = eSudoku.padRight(81).substring(i*9, i*9+9)
			List zL = []
			for (int j = 0; j < 9; j++) {
				if ( z.getAt(j) == ' ')
					zL += 0
				else
					zL += new Integer (z.getAt(j))
			}
			l += [zL]
		}
		l
	}
	
	List sudoDisplay () {
		//0 durch _ ersetzen
		List dList = []
		sudo.each {List z ->
			List dZ = []
			z.each {
				if (it == 0) 
					dZ += '_'
				else
					dZ += it
			}
			dList += [dZ]
		}
		dList += ['Prüfung der Eingabe']
		if (sudoOk)
			dList += ['ok']
		else
			dList += ['nicht ok']
		dList
	}
	
	Boolean getSudoOk () {
		sudoOk (sudo)
	}
	
	Boolean sudoOk (List s) {
		Boolean r = true
		for (int i = 0; i < 9; i++) {
			if(!sudoPartOk(sudoRow (s, i))) {r = false; return}
			if(!sudoPartOk(sudoCol (s, i))) {r = false; return}
			if(!sudoPartOk(sudoBlk (s, i))) {r = false; return}
		}
		r
	}
	
	Boolean sudoPartOk (List p) {
		Boolean r = true
		//part ist ok, wenn keine Zahl > 0 mehr als einmal vorkommt
		Map m = [1:0, 2:0, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0,]
		p.each {int i ->
			if (i > 0) m[i] += 1
		}
		m.each {key, value ->
			if (value > 1) {
				r = false
				return
			}
		}
		r
	}
	
	List sudoRow (List s, int i) {
		s[i]
	}
	
	List sudoCol (List s, int i) {
		List r = []
		s.each {List z ->
			r += z[i]
		}
		r
	}
	
	List sudoBlk (List s, int i) {
		List b = []
		
		int startI = i - i%3
		int u = i%3 * 3
		int o = i%3 * 3 + 2
		
		b += s[startI][u..o]
		b += s[startI+1][u..o]
		b += s[startI+2][u..o]
		
		b
	}
	
	List sudoSolve () {
		List s = solve1(sudo)
		solve (s)
	}
	
	List solve (List s) {
		List solved = s
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				if (s[i][j] == 0) {
					int k = 1
					while (k < 10 && s[i][j] == 0) {
						s[i][j] = k
						if (sudoOk(s))
							if (s.flatten().contains(0)) { 
								solved = solve (s)
								if (!solved)
									s[i][j] = 0
							}
							else
								solved = s
						else
							s[i][j] = 0
						k++
					}
				}
				if (s[i][j] == 0) {
					return null
				}
			}
		}
		solved
	}
	
	List solve1 (List s) {
		//füllt vorab alle Felder,die eindeutig sind, also keine Rekursion brauchen 
		Boolean changed = true
		while (changed) {
			changed = false
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if (s[i][j] == 0) {
						List row = sudoRow (s,i)
						List col = sudoCol (s,j)
						List blk = sudoBlk (s,blkNr(i,j))
						List u = row.plus(col).plus(blk)
						List diff = [1,2,3,4,5,6,7,8,9].minus(u.unique())
						if (diff.size() == 1) {
							s[i][j] = diff[0]
							changed = true
						}
					}
				}
			}
		}
		s
	}
	
	Integer blkNr (int i, int j) {
		Integer nr
		switch (j) {
			case 0..2:
				nr = i - i%3
				break
			case 3..5:
				nr = i - i%3 + 1
				break
			case 6..8:
				nr = i - i%3 + 2
				break
		}
		nr
	}
}
