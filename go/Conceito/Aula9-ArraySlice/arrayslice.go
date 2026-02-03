package main

func main() {
	// Array Slice
	var array [5]int = [5]int{10, 20, 30, 40, 50} // Mesmo tipo de dados dentro do array
	println("Array:", array)

	array2 := [5]string{"A", "B", "C", "D", "E"}
	println("Array2:", array2)

	// Fixa o tamanho do array pela quantidade de elementos fornecidos
	//Não deixa o array dimamico apenas infere o tamanho baseado nos elementos
	array3 := [...]float64{1.1, 2.2, 3.3, 4.4, 5.5, 6.6}
	println("Array3:", array3)

	//Não precisa especificar o tamanho do slice
	//não é um array mas aponta para um array
	slice4 := []string{"X", "Y", "Z"}
	println("Slice4:", slice4[0], slice4[1], slice4[2])

	slice4 = append(slice4, "W") // Adiciona um elemento ao slice
	println("Slice4 após append:", slice4[0], slice4[1], slice4[2], slice4[3])

	slice5 := array3[1:4] // Cria um slice a partir do array3 (elementos 1, 2 e 3)
	println("Slice5:", slice5[0], slice5[1], slice5[2])
}
