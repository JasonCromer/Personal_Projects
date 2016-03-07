#Convert the following to MIPS Assembly
#
#int main(){
#	int a = 2;
#	int max = 10;
#	int x[max];
#	int *ptr = x;
#
#	for(int i = 0; i < max; i++){
#		*ptr = modifyit(i);
#		cout << x[i] << endl;
#		ptr++;
#	}
#}
#
#
#
#int modifyIt(int arg){
#	int temp1;
#	int result;
#
#	temp1 = pow(arg, 2);
#	result = pow(arg, 3);
#	result = result + temp1 + 1;
#
#	return result;
#
#}
#
#int pow(int arg0, int arg1){
#	int product = 1;
#	for(int i = 0; i < arg1; i++){
#		product *= arg0;
#	}
#
#	return product;
#}
#
#	Expected output:
#1
#3
#13
#37
#81
#151
#253
#393
#577
#811



					#a 			$s0
					#max 		$s1
					#i 			$s2
					#ptr 		$s3
					.data
endl:				.asciiz		"\n"
x:					.word		0:10
					.text
main:				li			$s0,2				#a = 2
					li			$s1,10				#max = 10
					la			$s3,x				#*ptr = x
					li			$s2,0				#for(i=0;i<max;i++)
loop:				move		$a0,$s2				#*ptr = modifyIt(i)
					jal 		modit 				#
					sw			$v0,($s3)			#
					move 		$a0,$v0 			#

					li			$v0,1				#cout << x[i] << endl;
					lw			$s4,($s3)			#
					move		$a0,$s4				#
					syscall							#
					li			$v0,4				#
					la			$a0,endl			#
					syscall							#

					addi		$s3,$s3,4			#ptr++
					addi		$s2,$s2,1			#i++

					blt			$s2,$s1,loop		#

					li			$v0,10				#exit
					syscall							#

modit:				addi		$sp,$sp,-20			#allocate room on stack
					sw			$ra,16($sp)			
					sw			$s0,12($sp)
					sw			$s1,8($sp)
					sw			$s2,4($sp)
					sw			$s3,($sp)
					move		$s0,$a0 			#persist argument a

					li			$a1,2				#temp1 = pow(arg,2)
					jal 		pow					#
					move		$s1,$v0				#
					move		$a0,$s0				#return a to $a0

					li			$a1,3				#result = pow(arg,3)
					jal			pow					#
					add 		$v0,$v0,$s1			#result =result + temp1+1
					addi		$v0,$v0,1			#

					lw			$ra,16($sp)			#restore variables
					lw			$s0,12($sp)
					lw			$s1,8($sp)
					lw			$s2,4($sp)
					lw			$s3,($sp)
					addi		$sp,$sp,20			#deallocate space on stack

					jr			$ra					#return result

pow:				li			$t0,1				#product = 1
					li			$t1,0				#for(i=0;i<arg1;i++)
					bge			$t1,$a1,out			#
loop2:				mul			$t0,$t0,$a0 		#
					addi 		$t1,$t1,1			#i++
					blt			$t1,$a1,loop2		#

out:				move		$v0,$t0				#return product
					jr			$ra
