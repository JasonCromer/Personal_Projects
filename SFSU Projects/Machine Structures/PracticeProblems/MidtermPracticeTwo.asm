			#int a 			$s0
			#int b			$s1
			#ptr			$s2
			#i 				$s3
			#t0->max	 	$t0
			.data
endl:		.asciiz 		"\n"
x:			.word 			0:10
			.text
main:		li 				$s3,0			#i = 0
			li				$s0,3			#a = 3
			li				$s1,6			#b = 6
			li 				$t0,10			#array size
			la				$s2,x			# *ptr = x

loop1:		sw				$s3,($s2)		#*ptr = i
			addi			$s2,$s2,4		#ptr++
			addi			$s3,$s3,1		#i++
			blt				$s3,$t0,loop1	#i < 10

			li 				$s3,0 			#i = 0
			la				$s2,x			#ptr -> base address

loop2:		bne				$s3,$s0,elif 	#if(i == a)
			sw				$s0,($s2)		#*ptr = a
			j				incr			#

elif:		bne				$s3,$s1,else	#if(i ==b)
			sw				$s1,($s2)		#*ptr = b
			j				incr			#

else:		li				$v0,1			#cout << *ptr << endl
			lw				$t1,($s2)		#t1 = *ptr
			move			$a0,$t1			#
			syscall							#
			li				$v0,4			#
			la				$a0,endl		#
			syscall							#

incr:		addi 			$s2,$s2,4		#ptr++
			addi			$s3,$s3,1		#i++
			blt				$s3,$t0,loop2	#

			li				$v0,10			#exit cleanly
			syscall							#