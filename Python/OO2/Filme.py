from Programa import Programa
class Filme(Programa):
    def __init__(self,nome,ano,duracao):
        super().__init__(nome,ano)
        self._duracao = duracao
    
    def __str__(self):
        return '{} - {} - {} - {}'.format(self._nome,self._ano,self._likes,self._duracao)

 