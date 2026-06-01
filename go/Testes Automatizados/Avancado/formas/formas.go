package formas

import (
	"math"
)

type Forma interface {
	Area() float64
}

type Rectangle struct {
	Largura float64
	Altura  float64
}

func (r Rectangle) Area() float64 {
	return r.Largura * r.Altura
}

type Circle struct {
	Raio float64
}

func (c Circle) Area() float64 {
	return math.Pi * math.Pow(c.Raio, 2)
}
