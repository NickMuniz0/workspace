package main

func fibonacci(n int) int {
	if n <= 1 {
		return n
	}
	return fibonacci(n-1) + fibonacci(n-2)
}

func main() {
	tamanho := 1000
	tarefas := make(chan int,tamanho)
	resultados := make(chan int,tamanho)

	go worker(tarefas,resultados)
	go worker(tarefas,resultados)
	go worker(tarefas,resultados)
	go worker(tarefas,resultados)


	for i := 0; i < 45; i++ {
		tarefas <- i
	}
	close(tarefas)

	for i := 0; i < 45; i++ {
		println(<-resultados)
	}
}

func worker(tarefas <-chan int, resultados chan<- int) {
	for n := range tarefas {
		resultados <- fibonacci(n)
	}
}