import colorama
from colorama import Fore, Style

# resultado = round(imc,2)
def valida_imc(resultado):
    if type(resultado) is str:
        resultado = float(resultado)

    if resultado < 17.00:
        # return "MAGREZA"
        return Fore.BLUE +"%.2f" % resultado
    if resultado >=  17.00 and  resultado < 18.50 :
        # return "MAGREZA"
        return Fore.YELLOW +"%.2f" % resultado

    if resultado >= 18.50 and resultado <= 24.99:
        # return "NORMAL"
        return Fore.GREEN  +"%.2f" % resultado

    if resultado >= 25 and resultado <= 29.99:
        # return "SOBREPESO"
        return Fore.CYAN  +"%.2f" % resultado

    if resultado >=30 and resultado <=34.99:
        # return "OBESIDADE GRAU I"
        return Fore.YELLOW + "%.2f" % resultado 

    if resultado >=35 and resultado <= 39.99:
        # return "OBESIDADE GRAU II"
        return Fore.MAGENTA  +"%.2f" % resultado

    if resultado >= 40:
        # return "OBESIDADE GRAU III"
        return Fore.RED + "%.2f" % resultado

    return Fore.RED + "%.2f" % resultado

def lista_altura(inicio,fim):
    indice = 0.05
    partida =  int(inicio/indice)+10
    comprimento = fim
    alcance = int(comprimento/indice)+1
    lista_coluna = []
    for x in range(partida,alcance):
        valor = "%.2f" %(x*0.05)
        lista_coluna.append('0'+valor)
    return lista_coluna

lista_altura = lista_altura(1,2)


def lista_peso(inicio,fim):
    indice = 5
    partida =  int(inicio)
    comprimento = fim
    alcance = int(comprimento)
    lista_coluna = []
    for x in range(partida,alcance,5):
        lista_coluna.append(x)
    return lista_coluna

lista_peso =lista_peso(50,115)


for z,linha in enumerate(lista_altura):
    if z==0:
        print("    ",end='  ')            # PRIMEIRO VALOR DA LABEL
    print(Fore.RESET+'{:4}'.format(linha), end='  ') # PRIMEIRA LINHA LABLE
print()
print()

for peso in lista_peso:
    print(Fore.RESET+'{:4}'.format(peso), end='  ') # PRIMEIRA COLUNA LABLE
    for z, altura in enumerate(lista_altura):
        resultado = "%.2f" % (peso/( float(altura)*float(altura)))
        if z==0:
            pass  #PRIMEIRA COLUNA 
        print('{:4}'.format(valida_imc(resultado)), end='  ')
    print()


altura = float(input(Fore.RESET+"Digite Sua ALtura: ").replace(",","."))
peso   = float(input(Fore.RESET+"Digite Seu Peso: ").replace(",","."))
imc    = round(peso / (altura*altura),2)

print(valida_imc(imc))
print(Fore.RESET)