			#iter		$s0
			#limit		$s1
			#sum		$s2
			.data
			.text
main:		li 			$s0, 0			#i=0
			li 			$s1, 10			#limit = 10
			li 			$s2, 0			#sum = 0

			bge			$s0,$s1,ifout	#for(i=0;i<limit;i++)
for:		add			$s2,$s2,$s0		#sum = sum + 1
			addi		$s0,$s0,1		#i = i +1

			li			$v0, 1			#cout >> sum
			move		$a0, $s2		#
			syscall						#

			blt			$s0,$s1,for		#
ifout:
			li 			$v0, 10			#end program
			syscall
