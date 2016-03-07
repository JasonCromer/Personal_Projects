				#i->$s0			$s0
				#4->$s1			$s1
				.data
endl:			.asciiz			"\n"
				.text
main:			li				$s0,2				#i = 2
				li				$s1,4				#max = 4
													#for(i=2;i<=4;i++)
loop:			move			$a0,$s0				#result = poly(i)
				jal 			poly				#
				move			$a0,$v0				#

				li				$v0,1				#cout << result << endl;
				syscall								#
				li				$v0,4				#
				la				$a0,endl			#
				syscall

				addi			$s0,$s0,1			#i++
				ble				$s0,$s1,loop		#

				li				$v0,10				#exit cleanly
				syscall		

poly:			addi			$sp,$sp,-12			#allocate space on stack
				sw				$ra,8($sp)			#Store jump back to main
				sw				$s0,4($sp)			#store i
				sw				$s1,($sp)			#store max (4)

				move			$s0,$a0				#Store argument in $s0 register
				li				$a1,4				#store 4 in second argument
				jal				pow					#temp = pow(arg,4)
				move			$s1,$v0				#store temp1 in $s1

				move			$a0,$s0				#put argument back in $a0 register
				li				$a1,3				#
				jal				pow					#result = pow(arg,3)

				add 			$v0,$v0,$s1			#result = temp + result + 1
				addi			$v0,$v0,1			#

				lw				$ra,8($sp)			#
				lw				$s0,4($sp)			#
				lw				$s1,($sp)			#
				addi			$sp,$sp,12			#return result
				jr				$ra					#}

pow:			li				$t1,1				#product = 1

				li				$t0,0				#for(i = 0; i < arg1; i++)
				bge				$t0,$a1,out			#				
for:			mul				$t1,$t1,$a0			#product *= arg0;
				addi			$t0,$t0,1			#i++
				blt				$t0,$a1,for			#

out:			move			$v0,$t1				#return product
				jr				$ra					#