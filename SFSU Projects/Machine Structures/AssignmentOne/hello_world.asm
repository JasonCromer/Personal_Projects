.data
str:	.asciiz	"Hello World!"
endl:	.asciiz "\n"
.text
main:	li	$v0, 4
		la	$a0, str
		syscall

		li	$v0, 4
		la	$a0, endl
		syscall

		li	$v0, 4
		la	$a0, str
		syscall
	
		li	$v0, 4
		la	$a0, endl
		syscall

		li	$v0, 4
		la	$a0, str
		syscall
	
		li	$v0, 4
		la	$a0, endl
		syscall

		li	$v0, 10
		syscall
