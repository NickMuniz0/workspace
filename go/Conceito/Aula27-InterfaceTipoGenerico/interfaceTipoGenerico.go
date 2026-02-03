package main

func generico(interf interface{}) {
	println(interf)
}

func main() {
	generico("Texto")
	generico(123)
	generico(true)

	println(1, 2, "Teste", false, true, float32(10.5))

	mapa := map[string]interface{}{
		"nome":  "João",
		"idade": 30,
	}
	println(mapa["nome"].(string))
	println(mapa["idade"].(int))
}
