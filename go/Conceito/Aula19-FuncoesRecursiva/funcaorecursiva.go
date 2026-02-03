package main

func Fibonacci(n int) int {
	if n <= 1 {
		return n
	}
	return Fibonacci(n-1) + Fibonacci(n-2)
}

func main() {
	// posicao := Fibonacci(10)
	posicao := 10
	for i := 0; i <= posicao; i++ {
		println(Fibonacci(i))
	}

}
