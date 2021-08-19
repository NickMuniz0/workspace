from Programa import Programa


class Serie(Programa):
    def __init__(self,nome,ano,temporadas):
        super().__init__(nome,ano)
        self._temporadas=temporadas

    def __str__(self):
        return '{} - {} - {} - {}'.format(self._nome,self._ano,self._likes,self._temporadas)
