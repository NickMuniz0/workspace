from conta import Conta

class Execute():
    
        conta = Conta(123,"Nick",55.5,1000)
        conta.extrato()
        conta.deposita(100)
        conta.extrato()
        conta.sacar(50)
        conta.extrato()

        conta2 = Conta(432,"Amanda",440,2000)

        conta2.transfere(200,conta)
        conta2.extrato()
        conta.extrato()

        conta.limite=10000
        print(conta.limite)

        print(conta.condigo_banco()['BB'])
        


