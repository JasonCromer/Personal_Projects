#int main(void){
#	int x[6];
#	int a = 0x4a;
#
#	for(int i = 0; i < 5; i++){
#		x[i] = a;
#		cout << i << endl;
#	}
#}



				#i 				$s0
				#5				$s1
				#0x4a			$t0
				#base			$t1
				#base+offset	$t2
				.data
x:				.word			0:6
endl:			.asciiz			"\n"
				.text
main:			li				$s1,5				#max = 5
				li				$t0,0x4a			#a = 0x4a
				la				$t1,x				#load base in memory
				li				$s0,0				#i = 0

loop:			sll				$t2,$s0,2			#for(i=0;i<5;i++)
				add				$t2,$t2,$t1			#add base to offset
				sw				$t0,($t2)			#x[i] = a;

				li				$v0,1				#cout << i << endl;
				move			$a0,$s0				#
				syscall								#
				li				$v0,4				#
				la 				$a0,endl			#
				syscall								#

				addi			$s0,$s0,1			#i++
				blt				$s0,$s1,loop		#

				li				$v0, 10				#exit cleanly
				syscall								#
