package main

import (
	"errors"
	"fmt"
)

func main() {
	// int8,int16,int32,int64
	int16 := 16
	fmt.Println(int16)

	//uint8,uint16,uint32,uint64( int sem sinal )

	//alias
	//int32 == rune
	var numero rune = 12456
	fmt.Println(numero)
	//int8  == byte
	var numero2 byte = 123
	fmt.Println(numero2)

	//float32
	var numero3 float32 = 12456.4
	fmt.Println(numero3)
	//int8  == byte
	var numero4 float64 = 123123123123.1
	fmt.Println(numero4)

	// char convert para inteiro
	char := 'B'
	fmt.Println(char)

	//Valor ZERO
	var texto string //vario
	fmt.Println(texto)

	var texto2 uint16 //zero
	fmt.Println(texto2)

	var booleano bool = true //false
	fmt.Println(booleano)

	var erro error
	fmt.Println(erro)

	var erro1 error = errors.New("ERRO INTERNO")

	fmt.Println(erro1)
}
