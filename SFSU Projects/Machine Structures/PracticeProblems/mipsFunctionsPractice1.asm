# Implement the following in MIPS Assembly:
#
# int main(){
# 	int a = 5;
#	int result = changeA(a);
#	cout << result << endl;
# }
#
#
# int changeA(int val){
#	//Change a to 15
# 	val *= 2;
#	val += 5;
#	val = multiply(val);
#
#	return val;	
# }
# 
# int multiply(int val){
#	//Change val to 30
#	val *= 2;
#	return val;
# }
#
# Output should be: 30
#


				#a->$s0			$s0
				#res->$s1		$s1
				.data
endl:			.asciiz			"\n"
				.text
main:			li				$s0,5			#a = 5
				move			$a0,$s0			#
				jal				chngA 			#call changeA()
				move 			$s1,$v0			#

				li				$v0,1			#cout << result << endl
				move			$a0,$s1			#
				syscall							#
				li				$v0,4			#
				la				$a0,endl		#
				syscall							#

				li				$v0,10			#exit cleanly
				syscall							#



chngA:			mul 			$a0,$a0,2		#val *= 2
				addi			$a0,$a0,5		#val += 5

				addi			$sp,$sp,-8		#Allocate room on stack
				sw				$ra,($sp)		#
				sw				$s0,4($sp)		#
				move			$s0,$a0			#
				jal				multiply		#

				move			$s0,$v0			#return from multiply
				move			$v0,$s0			#store in value register

				lw				$ra,($sp)		#
				lw				$s0,4($sp)		#
				addi			$sp,$sp,8
				jr				$ra				#}

multiply:		mul 			$a0,$a0,2		#val *= 2
				move			$v0,$a0			#
				jr				$ra				#}
