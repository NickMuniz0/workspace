package formas

import (
	"math"
	"testing"
)

func TestArea(t *testing.T) {
	t.Run("Rectangle", func(t *testing.T) {
		ret := Rectangle{Largura: 10, Altura: 5}
		areaEsperada := 50.0
		areaRecebida := ret.Area()
		if areaEsperada != areaRecebida {
			//t.Errorf("Área esperada: %f, Área recebida: %f", areaEsperada, areaRecebida)
			t.Fatalf("Área esperada: %f, Área recebida: %f", areaEsperada, areaRecebida)

		}
	})

	t.Run("Circle", func(t *testing.T) {
		circ := Circle{Raio: 5}
		areaEsperada := math.Pi * 25
		areaRecebida := circ.Area()
		if areaEsperada != areaRecebida {
			t.Errorf("Área esperada: %f, Área recebida: %f", areaEsperada, areaRecebida)
		}

	})
}
