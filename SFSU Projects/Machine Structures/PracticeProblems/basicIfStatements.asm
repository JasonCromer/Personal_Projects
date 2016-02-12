		#a			$s0
		#b			$s1
		#c			$s2
		#d			$s3

		.data
eqstr:		.asciiz 	"a==d"
neqstr:		.asciiz		"a!=b"
endl:		.asciiz		"\n"
done:		.asciiz		"done!"
		.text
main:		li			$s0,1			#a = 1
		li			$s1,2			#b = 2
		li			$s2,3			#c = 3
		li			$s3,4			#d = 4
		rem			$s3,$s3,3		#d = d % 3
		mul			$s2,$s0,$s1		#c = a * b

		bne			$s0,$s3,ifout1	#if(a==d)
		li			$v0,4			#print eqstr
		la			$a0,eqstr		#
		syscall
ifout1:
		li			$v0,4			#print endl
		la			$a0,endl		#
		syscall						#

		beq			$s0,$s1,ifout2	#if(a!=b)
		li			$v0,4			#print neqstr
		la			$a0,neqstr		#		
		syscall						#
ifout2:
		li			$v0,4			#print endl
		la			$a0,endl		#
		syscall						#

		li			$v0,4			#print done
		la			$a0,done		#
		syscall						#
