from Filme import Filme 
from Serie import Serie 
from PlayList import PlayList
from PlayList2 import PlayList2



tmep= Filme('Todo Mundo em panico', 1999,100)
demolidor = Serie('Demolidor', 2016,2)
demolidor.dar_like()
demolidor.dar_like()
demolidor.dar_like()
demolidor.dar_like()

vingadores = Filme('vingadores', 2018,160)
vingadores.dar_like()
atlanta = Serie('atlanta',2018,2)
atlanta.dar_like()
atlanta.dar_like()

filmes_e_series = [vingadores,atlanta,demolidor,tmep]
playlist_fim_de_semana = PlayList2('Fim de Semana', filmes_e_series)

print("tamanho da playlist {}".format(len(playlist_fim_de_semana)))

for programa in playlist_fim_de_semana:
    print(programa)
print('ta ou nao ta?  {}'.format(demolidor in playlist_fim_de_semana))