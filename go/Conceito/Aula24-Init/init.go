package main

var n int

// Sera executado antes da func main
// Pode ter uma func init por arquivo
func init() {
	println("Inicializando o programa...")
	n = 10
}

func main() {
	println("Função main executada.")
	println(n)

}
