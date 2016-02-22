			#iter		$s0
			#max		$s1

			.data
fizz:		.asciiz		"Fizz"
buzz:		.asciiz		"Buzz"
endl:		.asciiz		"\n"
			.text
main:		li			$s0,1			#i = 1
			li			$s1,100			#max = 100

			bgt			$s0,$s1,exit	#for(i = 1; i <= max; i++)
loop:		rem			$t0,$s0,3		#temp = i % 3
			rem			$t1,$s0,5		#temp = i % 5

			bnez		$t0,if2			#if(i % 3 == 0)
			li			$v0,4			#cout << fizz
			la			$a0,fizz 		#
			syscall						#

if2:		bnez		$t1,if3			#if(i % 5 == 0)
			li			$v0,4			#cout << buzz
			la			$a0,buzz		#
			syscall						#

if3:		beqz		$t0,prntln		#if($t0 == 0 || $t1 == 0)
			bnez		$t1,else		#

prntln:		li			$v0,4			#cout << endl
			la			$a0, endl		#
			syscall						#
			j			incre			#skip else

else:		li			$v0,1			#cout << i << endl
			move		$a0,$s0			#
			syscall						#
			li			$v0,4			#
			la			$a0,endl		#
			syscall						#

incre:		addi		$s0,$s0,1		#i++
			ble			$s0,$s1,loop	#restart loop

exit:		li			$v0, 10			#exit cleanly
			syscall						#