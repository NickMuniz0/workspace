class PlayList(list):
    def __init__(self,nome,programas): #minha classe 
        self.nome= nome
        super().__init__(programas) #para adicionar a list herdada
