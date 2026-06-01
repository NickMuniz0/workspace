package main
import (
    "fmt"
    "time"
)

func main() {
    canal1 , canal2 := make(chan string), make(chan string)

    go func() {
        for{

            time.Sleep(time.Millisecond * 500)
            canal1 <- "Canal 1"
        }
    }()

    go func() {
        for {
            time.Sleep(time.Millisecond * 2)
            canal2 <- "Canal 2"
        }

    }()
    for {

        select {
        case mensagemCanal1 := <- canal1:
           fmt.Println(mensagemCanal1)
        case mensagemCanal2 := <- canal2:
           fmt.Println(mensagemCanal2)
        }

        // A vantagem do select é que ele espera por uma das mensagens chegar, e não bloqueia o programa, como no caso de ler diretamente do canal.
        //mensagemCanal1 := <- canal1
        //fmt.Println(mensagemCanal1)
        //
        //mensagemCanal2 := <- canal2
        //fmt.Println(mensagemCanal2)
    }

 }