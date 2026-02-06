package main
import (
	"time"
	"sync"
)

func main(){
	// Concorrencia != Paralelismo
	var waitGroup sync.WaitGroup
	waitGroup.Add(2)
	go func() {	
	escrever("Olá Mundo!")
	waitGroup.Done()

	}

	go func() {	
	escrever("Programando em Go!")
	waitGroup.Done()

	}

	waitGroup.Wait()
}

func escrever(texto string) {
	for i := 0; i < 5; i++ {
		println(texto)
		time.Sleep(1 * time.Second)

	}
}