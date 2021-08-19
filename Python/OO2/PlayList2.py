class PlayList2:
    def __init__(self,nome,programas):
        self.nome= nome
        self._programas = programas 
    
    @property
    def listagem(self):
        return self._programas

    @property
    def tamanho(self):
        return len(self._programas)

    #propriedade da listagem
    def __getitem__(self,item):
        return self._programas[item]


    def __len__(self):
        return len(self._programas)