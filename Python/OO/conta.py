class Conta:
    def __init__(self,numero,titular,saldo,limite):
        self.__numero=numero
        self.__titular= titular
        self.__saldo=saldo
        self.__limite=limite
####################################################################
    @property
    def saldo(self):
        return self.__saldo
   
    @property
    def titular(self):
        return self.__titular
   
    @property
    def numero(self):
            return self.__numero
   
    @property
    def limite(self):
        return self.__limite
    
    @limite.setter
    def limite(self,limite):
        self.__limite=limite
    
    @staticmethod
    def condigo_banco():
        return {'BB':'001','Caixa':'104','Bradesco':'237'}
####################################################################
    def __pode_sacar(self,valor_a_sacar):
        valor_disponivel_a_sacar = (self.__saldo+self.__limite)
        return (valor_a_sacar<=valor_disponivel_a_sacar)
   
    def extrato(self):
        print(self.__saldo)

    def deposita(self,valor):
        self.__saldo+=valor

    def sacar(self,valor):
        if(self.__pode_sacar(valor)):
            self.__saldo-=valor
        else:
            print("Nao tem limite")

    def transfere(self,valor,destino):
        self.sacar(valor)
        destino.deposita(valor)
        

        
