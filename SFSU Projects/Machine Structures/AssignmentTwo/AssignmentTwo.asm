					#inp1				$s0
					#inp2				$s1
					#ans				$s2

					.data
str1:				.asciiz				"Please enter an Integer: "
str2:				.asciiz				"First number divisible by second!"
					.text
main:				li					$v0, 4				#cout << str1
					la					$a0, str1			#
					syscall									#

					li					$v0, 5				#cin >> $s0
					syscall									#
					move 				$s0, $v0			#

					li					$v0, 4				#cout << str1
					la					$a0, str1			#
					syscall									#

					li					$v0, 5				#cin >> $s1
					syscall									#
					move				$s1, $v0			#

					rem					$s2, $s0, $s1  		#$s2 = $s0 % $s1

					bne					$s2, $0, ifout		#if($s2 == 0)
					li					$v0, 4				#cout << str2
					la					$a0, str2			#
					syscall									#
ifout:				
					li					$v0, 10				#exit cleanly
					syscall									#