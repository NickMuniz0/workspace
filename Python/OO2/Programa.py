class Programa:
    def __init__(self,nome,ano):
        self._nome = nome
        self._ano= ano
        self._likes=0

    @property
    def nome(self):
        return self._nome

    def dar_like(self):
        self._likes+=1

    @property
    def likes(self):
        return self._likes

    def __str__(self):
        return '{} - {} - {}'.format(self._nome,self._ano,self._likes)
