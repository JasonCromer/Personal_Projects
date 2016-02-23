#Translate this C++ code:
#int main(void){
#	int a = 3;
#	int b = 5;
#	int i = 0;
#
#	for(i = 1; i < (a*b); i++){
#		if(i % 3 == 0){
#			cout << "threes!";
#		}
#		if(i % 5 == 0){
#			cout << "fives!";
#		}
#		else if(i % 7 == 0){
#			cout << "sevens!";
#		}
#		else{
#			cout << i << "\n";
#		}
#	}
#}	

		
			#a			$s0
			#b			$s1
			#i			$s2
			.data
three:		.asciiz		"Threes!"
five:		.asciiz		"Fives!"
seven:		.asciiz		"Sevens!"
endl:		.asciiz		"\n"
			.text
main:		li			$s0,3				#a=3
			li			$s1,5				#b=5
			li			$s2,1				#i=1
			mul			$t0,$s0,$s1			#temp0 = a*b

			bge			$s2,$t0,forout		#for(i=1; i<(a*b); i++)
loop:		rem			$t1,$s2,3			#temp1 = i % 3
			rem			$t2,$s2,5			#temp2 = i % 5
			rem			$t3,$s2,7			#temp3 = i % 7

			bnez		$t1,if2				#if(temp1 == 0)
			li			$v0,4				#cout << "Threes!"
			la			$a0,three 			#
			syscall							#

if2:		bnez		$t2,elif			#if(temp2 == 0)
			li			$v0,4				#cout << "Fives!"
			la			$a0,five 			#
			syscall							#
			j			incr				#jump to end of loop

elif:		bnez		$t3,else			#if(temp3 == 0)
			li			$v0,4				#cout << "Sevens!"
			la			$a0,seven 			#
			syscall							#
			j 			incr				#jump to end of loop

else:		li			$v0,1				#cout << i << endl
			move		$a0,$s2				#
			syscall							#
			li			$v0,4				#
			la			$a0,endl			#
			syscall							#

incr:		addi 		$s2,$s2,1			#i++
			blt			$s2,$t0,loop 		#check i < (a*b)
forout:
			li			$v0,10				#exit cleanly
			syscall							#